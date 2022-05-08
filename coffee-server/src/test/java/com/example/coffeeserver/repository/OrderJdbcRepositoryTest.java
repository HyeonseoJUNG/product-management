package com.example.coffeeserver.repository;

import com.example.coffeeserver.entity.customer.Customer;
import com.example.coffeeserver.entity.customer.Email;
import com.example.coffeeserver.entity.customer.PhoneNumber;
import com.example.coffeeserver.entity.order.Order;
import com.example.coffeeserver.entity.order.OrderItem;
import com.example.coffeeserver.entity.order.OrderStatus;
import com.example.coffeeserver.entity.product.Category;
import com.example.coffeeserver.entity.product.Product;
import com.example.coffeeserver.vo.OrderVo;
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

import java.time.LocalDateTime;
import java.util.*;

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
class OrderJdbcRepositoryTest {
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

        @Bean
        public ProductJdbcRepository productJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
            return new ProductJdbcRepository(namedParameterJdbcTemplate);
        }

        @Bean
        public OrderJdbcRepository orderJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
            return new OrderJdbcRepository(namedParameterJdbcTemplate);
        }
    }

    @Autowired
    CustomerJdbcRepository customerJdbcRepository;

    @Autowired
    ProductJdbcRepository productJdbcRepository;

    @Autowired
    OrderJdbcRepository orderJdbcRepository;

    EmbeddedMysql embeddedMysql;

    private static final Customer customer = new Customer(UUID.randomUUID(), "customer01", new Email("custome@gmail.com"), new PhoneNumber("010-1234-1234"));

    private static final Product product1 = new Product(UUID.randomUUID(), "ProductName1", Category.COFFEE, 3000, "product1 description");
    private static final Product product2 = new Product(UUID.randomUUID(), "ProductName2", Category.NON_COFFEE, 6000, "product2 description");
    private static final Product product3 = new Product(UUID.randomUUID(), "ProductName3", Category.CAKE, 8000, "product3 description");

    private static final OrderItem orderItem1 = new OrderItem(product1.getProductId(), product1.getCategory(), product1.getPrice(), 2);
    private static final OrderItem orderItem2 = new OrderItem(product2.getProductId(), product2.getCategory(), product2.getPrice(), 1);
    private static final OrderItem orderItem3 = new OrderItem(product3.getProductId(), product3.getCategory(), product3.getPrice(), 3);

    private static final List<OrderItem> orderItemList = Arrays.asList(orderItem1, orderItem2, orderItem3);

    private static final OrderVo orderVo = new OrderVo(UUID.randomUUID(), customer.getCustomerId(), "customer address", orderItemList, OrderStatus.PAYMENT_CONFIRMED, LocalDateTime.now(), null);

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

        customerJdbcRepository.insert(customer);
        productJdbcRepository.insert(product1);
        productJdbcRepository.insert(product2);
        productJdbcRepository.insert(product3);
    }

    @AfterEach
    void cleanUpDb() {
        orderJdbcRepository.deleteAll();
    }

    @AfterAll
    void cleanUp() {
        embeddedMysql.stop();
    }

    @Test
    @DisplayName("새 주문 추가")
    void insert() {
        OrderVo order = new OrderVo(UUID.randomUUID(), customer.getCustomerId(), "customer address", orderItemList, OrderStatus.PAYMENT_CONFIRMED, LocalDateTime.now(), null);
        orderJdbcRepository.insert(order);

        Optional<Order> OrderById = orderJdbcRepository.findById(order.getOrderId());
        assertThat(OrderById.isEmpty(), is(false));
        assertThat(OrderById.get(), samePropertyValuesAs(order.toOrderEntity()));
    }

    @Test
    @DisplayName("주문 삭제")
    void deleteById() {
        OrderVo order = new OrderVo(UUID.randomUUID(), customer.getCustomerId(), "customer address", orderItemList, OrderStatus.PAYMENT_CONFIRMED, LocalDateTime.now(), null);
        orderJdbcRepository.insert(order);

        orderJdbcRepository.deleteById(order.getOrderId());

        Optional<Order> OrderById = orderJdbcRepository.findById(order.getOrderId());
        assertThat(OrderById.isEmpty(), is(true));
    }
}