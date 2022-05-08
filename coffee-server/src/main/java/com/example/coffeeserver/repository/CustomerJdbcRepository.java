package com.example.coffeeserver.repository;

import com.example.coffeeserver.entity.customer.Customer;
import com.example.coffeeserver.entity.customer.Email;
import com.example.coffeeserver.entity.customer.PhoneNumber;
import com.example.coffeeserver.repository.query.CustomerSqlQuery;
import com.example.coffeeserver.utils.Util;
import com.example.exception.DataNotInsertedException;
import com.example.exception.DataNotUpdatedException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class CustomerJdbcRepository implements CustomerRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final RowMapper<Customer> customerRowMapper = (resultSet, rowNum) -> {
        UUID customerId = Util.toUUID(resultSet.getBytes("customer_id"));
        String customerName = resultSet.getString("customer_name");
        Email email = new Email(resultSet.getString("email"));
        PhoneNumber phoneNumber = new PhoneNumber(resultSet.getString("phone_number"));
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = resultSet.getTimestamp("updated_at") != null ?
                resultSet.getTimestamp("updated_at").toLocalDateTime() : null;
        LocalDateTime lastLoginAt = resultSet.getTimestamp("last_login_at") != null ?
                resultSet.getTimestamp("last_login_at").toLocalDateTime() : null;

        return new Customer(customerId, customerName, email, phoneNumber, createdAt, updatedAt, lastLoginAt);
    };

    private Map<String, Object> toParamMap(Customer customer) {
        return new HashMap<>(){{
            put("customerId", customer.getCustomerId().toString().getBytes());
            put("customerName", customer.getCustomerName());
            put("email", customer.getEmail().getAddress());
            put("phoneNumber", customer.getPhoneNumber().getPhoneNumber());
            put("createdAt", Timestamp.valueOf(customer.getCreatedAt()));
            put("updatedAt", customer.getUpdatedAt() != null ? Timestamp.valueOf(customer.getUpdatedAt()) : null);
            put("lastLoginAt", customer.getLastLoginAt() != null ? Timestamp.valueOf(customer.getLastLoginAt()) : null);
        }};
    }

    public CustomerJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * customer 생성
     * @param customer
     * @return
     */
    @Override
    public Customer insert(Customer customer) {
        int update = jdbcTemplate.update(CustomerSqlQuery.INSERT.getQuery(), toParamMap(customer));

        if (update != 1) {
            throw new DataNotInsertedException();
        }

        return customer;
    }

    /**
     * 전체 customer 조회
     * @return
     */
    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query(CustomerSqlQuery.SELECT_ALL.getQuery(), customerRowMapper);
    }

    /**
     * id로 customer 조회
     * @param customerId
     * @return
     */
    @Override
    public Optional<Customer> findById(UUID customerId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(CustomerSqlQuery.SELECT_BY_ID.getQuery(),
                            Collections.singletonMap("customerId", customerId.toString().getBytes()),
                            customerRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * 이름으로 customer 조회
     * @param name
     * @return
     */
    @Override
    public List<Customer> findByName(String name) {
        return jdbcTemplate.query(
                CustomerSqlQuery.SELECT_BY_NAME.getQuery(),
                Collections.singletonMap("customerName", name),
                customerRowMapper);
    }

    /**
     * 이메일로 customer 조회
     * @param email
     * @return
     */
    @Override
    public Optional<Customer> findByEmail(Email email) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(CustomerSqlQuery.SELECT_BY_EMAIL.getQuery(),
                            Collections.singletonMap("email", email.getAddress()),
                            customerRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * 폰번호로 customer 조회
     * @param phoneNumber
     * @return
     */
    @Override
    public Optional<Customer> findByPhoneNumber(PhoneNumber phoneNumber) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(CustomerSqlQuery.SELECT_BY_PHONE_NUMBER.getQuery(),
                            Collections.singletonMap("phoneNumber", phoneNumber.getPhoneNumber()),
                            customerRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * customer 업데이트
     * @param customer
     * @return
     */
    @Override
    public Customer update(Customer customer) {
        int update = jdbcTemplate.update(CustomerSqlQuery.UPDATE.getQuery(), toParamMap(customer));

        if (update != 1) {
            throw new DataNotUpdatedException();
        }

        return customer;
    }

    /**
     * id로 customer 삭제
     * @param customerId
     */
    @Override
    public void deleteById(UUID customerId) {
        jdbcTemplate.update(
                CustomerSqlQuery.DELETE_BY_ID.getQuery(),
                Collections.singletonMap("customerId", customerId.toString().getBytes()));
    }

    /**
     * 전체 customer 삭제
     */
    @Override
    public void deleteAll() {
        jdbcTemplate.update(CustomerSqlQuery.DELETE_ALL.getQuery(), Collections.emptyMap());
    }
}
