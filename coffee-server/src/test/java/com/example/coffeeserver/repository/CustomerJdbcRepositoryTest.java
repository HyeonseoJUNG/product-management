package com.example.coffeeserver.repository;

import com.example.coffeeserver.entity.customer.Customer;
import com.example.coffeeserver.entity.customer.Email;
import com.example.coffeeserver.entity.customer.PhoneNumber;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.config.Charset.UTF8;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.distribution.Version.v8_0_11;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerJdbcRepositoryTest {

    @Configuration
    static class Config {

        @Bean
        public DataSource dataSource() {
            HikariDataSource dataSource = DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost:2298/test-coffee_mgmt")
                    .username("test")
                    .password("test*#(@22")
                    .type(HikariDataSource.class)
                    .build();

            return dataSource;
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }

        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
            return new NamedParameterJdbcTemplate(jdbcTemplate);
        }

        @Bean
        public CustomerJdbcRepository customerJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
            return new CustomerJdbcRepository(namedParameterJdbcTemplate);
        }
    }

    @Autowired
    CustomerJdbcRepository customerJdbcRepository;

    EmbeddedMysql embeddedMysql;

    @BeforeAll
    void setUp() {
        MysqldConfig mysqldConfig = aMysqldConfig(v8_0_11)
                .withCharset(UTF8)
                .withPort(2298)
                .withUser("test", "test*#(@22")
                .withTimeZone("Asia/Seoul")
                .build();

        embeddedMysql = anEmbeddedMysql(mysqldConfig)
                .addSchema("test-coffee_mgmt", classPathScript("schema.sql"))
                .start();
    }

    @AfterEach
    void cleanUpDb() {
        customerJdbcRepository.deleteAll();
    }

    @AfterAll
    void cleanUp() {
        embeddedMysql.stop();
    }

    @Test
    @DisplayName("전체 고객 조회")
    void findAll() {
        Customer customer1 = new Customer(UUID.randomUUID(), "test-user1", new Email("test-user1@gmail.com"), new PhoneNumber("010-1234-5678"));
        Customer customer2 = new Customer(UUID.randomUUID(), "test-user2", new Email("test-user2@gmail.com"), new PhoneNumber("010-1234-2222"));
        Customer customer3 = new Customer(UUID.randomUUID(), "test-user3", new Email("test-user3@gmail.com"), new PhoneNumber("010-1234-3333"));

        customerJdbcRepository.insert(customer1);
        customerJdbcRepository.insert(customer2);
        customerJdbcRepository.insert(customer3);

        List<Customer> customers = customerJdbcRepository.findAll();

        assertThat(customers.isEmpty(), is(false));
        assertThat(customers, hasSize(3));
    }

    @Test
    @DisplayName("새 고객 추가")
    void insert() {
        Customer customer = new Customer(UUID.randomUUID(), "test-user1", new Email("test-user1@gmail.com"), new PhoneNumber("010-1234-5678"));

        customerJdbcRepository.insert(customer);

        Optional<Customer> customerById = customerJdbcRepository.findById(customer.getCustomerId());
        assertThat(customerById.isEmpty(), is(false));
    }


    @Test
    @DisplayName("고객 정보 변경: 고객 이름 변경")
    void updateName() {
        Customer customer = new Customer(UUID.randomUUID(), "test-user1", new Email("test-user1@gmail.com"), new PhoneNumber("010-1234-5678"));
        customerJdbcRepository.insert(customer);
        customer.changeCustomerName("test-user1-changed");

        customerJdbcRepository.update(customer);

        Optional<Customer> customerById = customerJdbcRepository.findById(customer.getCustomerId());
        assertThat(customerById.isEmpty(), is(false));
        assertThat(customerById.get(), samePropertyValuesAs(customer));
    }

    @Test
    @DisplayName("고객 정보 변경: 고객 폰번호 변경 (중복X)")
    void updateNonDuplicatePhoneNumber() {
        Customer customer1 = new Customer(UUID.randomUUID(), "test-user1", new Email("test-user1@gmail.com"), new PhoneNumber("010-1234-5678"));
        Customer customer2 = new Customer(UUID.randomUUID(), "test-user2", new Email("test-user2@gmail.com"), new PhoneNumber("010-1234-2222"));
        customerJdbcRepository.insert(customer1);
        customerJdbcRepository.insert(customer2);
        customer1.changePhoneNumber(new PhoneNumber("010-1234-2308"));

        customerJdbcRepository.update(customer1);

        Optional<Customer> customerById = customerJdbcRepository.findById(customer1.getCustomerId());
        assertThat(customerById.isEmpty(), is(false));
        assertThat(customerById.get(), samePropertyValuesAs(customer1));
    }

    @Test
    @DisplayName("고객 정보 변경: 고객 폰번호 변경 (중복 O)")
    void updateDuplicatePhoneNumber() {
        Customer customer1 = new Customer(UUID.randomUUID(), "test-user1", new Email("test-user1@gmail.com"), new PhoneNumber("010-1234-1111"));
        Customer customer2 = new Customer(UUID.randomUUID(), "test-user2", new Email("test-user2@gmail.com"), new PhoneNumber("010-1234-2222"));
        customerJdbcRepository.insert(customer1);
        customerJdbcRepository.insert(customer2);
        customer1.changePhoneNumber(new PhoneNumber("010-1234-2222"));

        Assertions.assertThrows(Exception.class, () -> {
            customerJdbcRepository.update(customer1);
        });

        assertThatThrownBy(() -> customerJdbcRepository.update(customer1)).hasMessageContaining("Duplicate");
    }

    @Test
    @DisplayName("고객 id로 고객 조회")
    void findById() {
        Customer customer = new Customer(UUID.randomUUID(), "test-user1", new Email("test-user1@gmail.com"), new PhoneNumber("010-1234-5678"));

        customerJdbcRepository.insert(customer);

        Optional<Customer> customerById = customerJdbcRepository.findById(customer.getCustomerId());
        assertThat(customerById.isEmpty(), is(false));
        assertThat(customerById.get(), samePropertyValuesAs(customer));
    }

    @Test
    @DisplayName("고객 이름으로 고객 조회")
    void findByName() {
        Customer customer = new Customer(UUID.randomUUID(), "test-user1", new Email("test-user1@gmail.com"), new PhoneNumber("010-1234-5678"));
        customerJdbcRepository.insert(customer);

        List<Customer> customerByName = customerJdbcRepository.findByName(customer.getCustomerName());
        assertThat(customerByName.isEmpty(), is(false));
        assertThat((customerByName), hasSize(1));
        assertThat(customerByName.get(0), samePropertyValuesAs(customer));
    }

    @Test
    @DisplayName("고객 이메일로 고객 조회")
    void findByEmail() {
        Customer customer = new Customer(UUID.randomUUID(), "test-user1", new Email("test-user1@gmail.com"), new PhoneNumber("010-1234-5678"));

        customerJdbcRepository.insert(customer);

        Optional<Customer> customerByEmail = customerJdbcRepository.findByEmail(customer.getEmail());
        assertThat(customerByEmail.isEmpty(), is(false));
        assertThat(customerByEmail.get(), samePropertyValuesAs(customer));
    }

    @Test
    @DisplayName("고객 폰번호로 고객 조회")
    void findByPhoneNumber() {
        Customer customer = new Customer(UUID.randomUUID(), "test-user1", new Email("test-user1@gmail.com"), new PhoneNumber("010-1234-5678"));

        customerJdbcRepository.insert(customer);

        Optional<Customer> customerByPhoneNumber = customerJdbcRepository.findByPhoneNumber(customer.getPhoneNumber());
        assertThat(customerByPhoneNumber.isEmpty(), is(false));
        assertThat(customerByPhoneNumber.get(), samePropertyValuesAs(customer));
    }

    @Test
    @DisplayName("고객 ID로 고객 삭제")
    void deleteById() {
        Customer customer1 = new Customer(UUID.randomUUID(), "test-user1", new Email("test-user1@gmail.com"), new PhoneNumber("010-1234-5678"));
        Customer customer2 = new Customer(UUID.randomUUID(), "test-user2", new Email("test-user2@gmail.com"), new PhoneNumber("010-1234-2222"));
        Customer customer3 = new Customer(UUID.randomUUID(), "test-user3", new Email("test-user3@gmail.com"), new PhoneNumber("010-1234-3333"));

        customerJdbcRepository.insert(customer1);
        customerJdbcRepository.insert(customer2);
        customerJdbcRepository.insert(customer3);

        customerJdbcRepository.deleteById(customer1.getCustomerId());

        List<Customer> customers = customerJdbcRepository.findAll();
        assertThat(customers, hasSize(2));
    }

    @Test
    @DisplayName("전체 고객 삭제")
    void deleteAll() {
        Customer customer1 = new Customer(UUID.randomUUID(), "test-user1", new Email("test-user1@gmail.com"), new PhoneNumber("010-1234-5678"));
        Customer customer2 = new Customer(UUID.randomUUID(), "test-user2", new Email("test-user2@gmail.com"), new PhoneNumber("010-1234-2222"));
        Customer customer3 = new Customer(UUID.randomUUID(), "test-user3", new Email("test-user3@gmail.com"), new PhoneNumber("010-1234-3333"));

        customerJdbcRepository.insert(customer1);
        customerJdbcRepository.insert(customer2);
        customerJdbcRepository.insert(customer3);

        customerJdbcRepository.deleteAll();

        List<Customer> customers = customerJdbcRepository.findAll();
        assertThat(customers, hasSize(0));
    }
}