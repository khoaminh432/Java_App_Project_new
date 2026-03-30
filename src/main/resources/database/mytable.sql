SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

CREATE DATABASE IF NOT EXISTS jdbc_demo
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE jdbc_demo;

-- 1. product_category
CREATE TABLE IF NOT EXISTS product_category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(255) NOT NULL,
    description TEXT
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 2. supplier
CREATE TABLE IF NOT EXISTS supplier (
    id INT PRIMARY KEY AUTO_INCREMENT,
    supplier_name VARCHAR(255) NOT NULL,
    address TEXT,
    phone_number VARCHAR(20),
    status INT DEFAULT 1
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 3. role
CREATE TABLE IF NOT EXISTS role (
    id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(100) NOT NULL,
    hourly_rate DECIMAL(15, 2) DEFAULT 0
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 4. customer
CREATE TABLE IF NOT EXISTS customer (
    id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(255),
    phone_number VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    status VARCHAR(50)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 5. payment_method
CREATE TABLE IF NOT EXISTS payment_method (
    id INT PRIMARY KEY AUTO_INCREMENT,
    method_name VARCHAR(100) NOT NULL
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 6. voucher
CREATE TABLE IF NOT EXISTS voucher (
    id INT PRIMARY KEY AUTO_INCREMENT,
    promotion_name VARCHAR(255),
    start_date DATETIME,
    end_date DATETIME
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 7. employee
CREATE TABLE IF NOT EXISTS employee (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(20),
    dob DATE,
    address TEXT,
    basic_salary DECIMAL(15, 2),
    status VARCHAR(50),
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES role(id)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 8. product
CREATE TABLE IF NOT EXISTS product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(255) NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    unit VARCHAR(50),
    quantity INT DEFAULT 0,
    status VARCHAR(50),
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES product_category(id)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 9. shipper (inherits from employee 1:1)
CREATE TABLE IF NOT EXISTS shipper (
    id INT PRIMARY KEY,
    vehicle_plate_number VARCHAR(50),
    current_status VARCHAR(50),
    FOREIGN KEY (id) REFERENCES employee(id)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 10. timesheet
CREATE TABLE IF NOT EXISTS timesheet ( 
    id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT,
    hours_worked DECIMAL(5, 2),
    work_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employee(id)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 11. goods_receipt (phiếu nhập hàng)
CREATE TABLE IF NOT EXISTS goods_receipt (
    id INT PRIMARY KEY AUTO_INCREMENT,
    received_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    supplier_id INT,
    total_quantity INT,
    total_price DECIMAL(15, 2),
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 12. ingredient
CREATE TABLE IF NOT EXISTS ingredient (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ingredient_name VARCHAR(50),
    net_weight INT,
    quantity INT
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 13. goods_receipt_detail
CREATE TABLE IF NOT EXISTS goods_receipt_detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    receipt_id INT,
    ingredient_id INT,
    quantity INT,
    unit_price DECIMAL(15, 2),
    FOREIGN KEY (receipt_id) REFERENCES goods_receipt(id),
    FOREIGN KEY (ingredient_id) REFERENCES ingredient(id)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 14. ingredient_product
CREATE TABLE IF NOT EXISTS ingredient_product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    ingredient_id INT,
    estimate INT,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (ingredient_id) REFERENCES ingredient(id)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 15. order (đơn đặt hàng chung)
CREATE TABLE IF NOT EXISTS `order` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    sub_total DECIMAL(15, 2),
    total_amount DECIMAL(15, 2),
    status VARCHAR(50),
    FOREIGN KEY (customer_id) REFERENCES customer(id)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 16. online_order (specialization of order)
CREATE TABLE IF NOT EXISTS online_order (
    id INT PRIMARY KEY,
    customer_id INT,
    shipper_id INT,
    receiver_name VARCHAR(255),
    phone_number VARCHAR(20),
    address TEXT,
    shipping_fee DECIMAL(15, 2),
    estimated_delivery_time DATETIME,
    completed_time DATETIME,
    status VARCHAR(50),
    total_amount DECIMAL(15, 2),
    FOREIGN KEY (id) REFERENCES `order`(id),
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (shipper_id) REFERENCES shipper(id)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 17. order_detail
CREATE TABLE IF NOT EXISTS order_detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    product_id INT,
    quantity INT,
    unit_price DECIMAL(15, 2),
    FOREIGN KEY (order_id) REFERENCES `order`(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 18. invoice
CREATE TABLE IF NOT EXISTS invoice (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    employee_id INT,
    order_id INT,
    payment_method_id INT,
    issued_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(15, 2),
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    FOREIGN KEY (order_id) REFERENCES `order`(id),
    FOREIGN KEY (payment_method_id) REFERENCES payment_method(id)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 19. invoice_detail
CREATE TABLE IF NOT EXISTS invoice_detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    invoice_id INT,
    product_id INT,
    quantity INT,
    unit_price DECIMAL(15, 2),
    FOREIGN KEY (invoice_id) REFERENCES invoice(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 20. invoice_voucher_detail
CREATE TABLE IF NOT EXISTS invoice_voucher_detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    invoice_id INT,
    voucher_id INT,
    discount_value DECIMAL(15, 2),
    FOREIGN KEY (invoice_id) REFERENCES invoice(id),
    FOREIGN KEY (voucher_id) REFERENCES voucher(id)
)
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
