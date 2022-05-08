CREATE TABLE products
(
    product_id   BINARY(16) PRIMARY KEY,
    product_name VARCHAR(20) NOT NULL,
    category     VARCHAR(50) NOT NULL,
    price        bigint      NOT NULL,
    description  VARCHAR(500) DEFAULT NULL,
    created_at   datetime(6) NOT NULL,
    updated_at   datetime(6)  DEFAULT NULL
);

CREATE TABLE customers
(
    customer_id   BINARY(16) PRIMARY KEY,
    customer_name varchar(20) NOT NULL,
    email         varchar(50) NOT NULL,
    phone_number  varchar(13) NOT NULL,
    created_at    datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at    datetime(6)          DEFAULT NULL,
    last_login_at datetime(6)          DEFAULT NULL,
    CONSTRAINT unq_user_email UNIQUE (email),
    CONSTRAINT unq_user_phone_number UNIQUE (phone_number)
);

CREATE TABLE orders
(
    order_id     binary(16) PRIMARY KEY,
    customer_id  binary(16)   NOT NULL,
    address      VARCHAR(200) NOT NULL,
    order_status VARCHAR(50)  NOT NULL,
    created_at   datetime(6)  NOT NULL,
    updated_at   datetime(6) DEFAULT NULL,
    CONSTRAINT fk_order_to_customer_id FOREIGN KEY (customer_id) REFERENCES customers (customer_id)
);

CREATE TABLE order_items
(
    seq        bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_id   binary(16)  NOT NULL,
    product_id binary(16)  NOT NULL,
    category   VARCHAR(50) NOT NULL,
    price      bigint      NOT NULL,
    quantity   int         NOT NULL,
    created_at datetime(6) NOT NULL,
    INDEX (order_id),
    CONSTRAINT fk_order_items_to_order FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_to_product FOREIGN KEY (product_id) REFERENCES products (product_id)
);