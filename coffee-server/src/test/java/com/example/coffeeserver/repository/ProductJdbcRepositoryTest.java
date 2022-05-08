package com.example.coffeeserver.repository;

import com.example.coffeeserver.entity.product.Category;
import com.example.coffeeserver.entity.product.Product;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductJdbcRepositoryTest {

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
        public ProductJdbcRepository productJdbcRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
            return new ProductJdbcRepository(namedParameterJdbcTemplate);
        }
    }

    @Autowired
    ProductJdbcRepository productJdbcRepository;

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
        productJdbcRepository.deleteAll();
    }

    @AfterAll
    void cleanUp() {
        embeddedMysql.stop();
    }

    @Test
    @DisplayName("새 상품 추가")
    void insert() {
        Product product = new Product(UUID.randomUUID(), "ProductName1", Category.COFFEE, 3000, "product1 description");

        productJdbcRepository.insert(product);

        Optional<Product> productById = productJdbcRepository.findById(product.getProductId());
        assertThat(productById.isEmpty(), is(false));
    }

    @Test
    @DisplayName("전체 상품 조회")
    void findAll() {
        Product product1 = new Product(UUID.randomUUID(), "ProductName1", Category.COFFEE, 3000, "product1 description");
        Product product2 = new Product(UUID.randomUUID(), "ProductName2", Category.NON_COFFEE, 6000, "product2 description");
        Product product3 = new Product(UUID.randomUUID(), "ProductName3", Category.CAKE, 12000, "product3 description");

        productJdbcRepository.insert(product1);
        productJdbcRepository.insert(product2);
        productJdbcRepository.insert(product3);

        List<Product> products = productJdbcRepository.findAll();

        assertThat(products.isEmpty(), is(false));
        assertThat(products, hasSize(3));
    }

    @Test
    @DisplayName("상품 id로 상품 조회")
    void findById() {
        Product product = new Product(UUID.randomUUID(), "ProductName1", Category.COFFEE, 3000, "product1 description");

        productJdbcRepository.insert(product);

        Optional<Product> productById = productJdbcRepository.findById(product.getProductId());
        assertThat(productById.isEmpty(), is(false));
        assertThat(productById.get(), samePropertyValuesAs(product));
    }

    @Test
    @DisplayName("상품 이름으로 상품 조회")
    void findByName() {
        Product product = new Product(UUID.randomUUID(), "ProductName1", Category.COFFEE, 3000, "product1 description");

        productJdbcRepository.insert(product);

        List<Product> productByName = productJdbcRepository.findByName(product.getProductName());
        assertThat(productByName.isEmpty(), is(false));
        assertThat((productByName), hasSize(1));
        assertThat(productByName.get(0), samePropertyValuesAs(product));
    }

    @Test
    @DisplayName("검색 조건으로 상품 조회")
    void findByCondition() {
        Product product1 = new Product(UUID.randomUUID(), "ProductName1", Category.COFFEE, 3000, "product1 description");
        Product product2 = new Product(UUID.randomUUID(), "ProductName2", Category.NON_COFFEE, 6000, "product2 description");
        Product product3 = new Product(UUID.randomUUID(), "ProductName3", Category.COFFEE, 12000, "product3 description");

        productJdbcRepository.insert(product1);
        productJdbcRepository.insert(product2);
        productJdbcRepository.insert(product3);

        List<Product> productByCondition = productJdbcRepository.findByCondition(Optional.empty(), Optional.of(Long.valueOf(10000)), Optional.of(Category.COFFEE));
        assertThat(productByCondition.isEmpty(), is(false));
        assertThat((productByCondition), hasSize(1));
        assertThat(productByCondition.get(0), samePropertyValuesAs(product1));
    }

    @Test
    @DisplayName("상품 정보 변경")
    void update() {
        Product product = new Product(UUID.randomUUID(), "ProductName1", Category.COFFEE, 3000, "product1 description");
        productJdbcRepository.insert(product);
        product.changePrice(7000);

        productJdbcRepository.update(product);

        Optional<Product> productById = productJdbcRepository.findById(product.getProductId());
        assertThat(productById.isEmpty(), is(false));
        assertThat(productById.get(), samePropertyValuesAs(product));
    }

    @Test
    @DisplayName("상품 id로 상품 삭제")
    void deleteById() {
        Product product1 = new Product(UUID.randomUUID(), "ProductName1", Category.COFFEE, 3000, "product1 description");
        Product product2 = new Product(UUID.randomUUID(), "ProductName2", Category.NON_COFFEE, 6000, "product2 description");
        Product product3 = new Product(UUID.randomUUID(), "ProductName3", Category.CAKE, 12000, "product3 description");

        productJdbcRepository.insert(product1);
        productJdbcRepository.insert(product2);
        productJdbcRepository.insert(product3);

        productJdbcRepository.deleteById(product1.getProductId());
        List<Product> products = productJdbcRepository.findAll();
        assertThat(products, hasSize(2));
    }

    @Test
    @DisplayName("전체 상품 삭제")
    void deleteAll() {
        Product product1 = new Product(UUID.randomUUID(), "ProductName1", Category.COFFEE, 3000, "product1 description");
        Product product2 = new Product(UUID.randomUUID(), "ProductName2", Category.NON_COFFEE, 6000, "product2 description");
        Product product3 = new Product(UUID.randomUUID(), "ProductName3", Category.CAKE, 12000, "product3 description");
        productJdbcRepository.insert(product1);
        productJdbcRepository.insert(product2);
        productJdbcRepository.insert(product3);

        productJdbcRepository.deleteAll();

        List<Product> products = productJdbcRepository.findAll();
        assertThat(products, hasSize(0));
    }
}