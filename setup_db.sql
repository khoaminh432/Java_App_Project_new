-- Drop database cũ và tạo mới
DROP DATABASE IF EXISTS jdbc_demo;
CREATE DATABASE jdbc_demo;

-- Chạy file SQL chính
USE jdbc_demo;

-- 1. product_category
CREATE TABLE product_category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(255) NOT NULL,
    description TEXT
);

-- 2. supplier
CREATE TABLE supplier (
    id INT PRIMARY KEY AUTO_INCREMENT,
    supplier_name VARCHAR(255) NOT NULL,
    address TEXT,
    phone_number VARCHAR(20)
);

-- 3. role
CREATE TABLE role (
    id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(100) NOT NULL,
    hourly_rate DECIMAL(15, 2) DEFAULT 0
);

-- 4. customer
CREATE TABLE customer (
    id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(255),
    phone_number VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    status VARCHAR(50)
);

-- 5. payment_method
CREATE TABLE payment_method (
    id INT PRIMARY KEY AUTO_INCREMENT,
    method_name VARCHAR(100) NOT NULL
);

-- 6. voucher
CREATE TABLE voucher (
    id INT PRIMARY KEY AUTO_INCREMENT,
    promotion_name VARCHAR(255),
    start_date DATETIME,
    end_date DATETIME
);

-- 7. employee
CREATE TABLE employee (
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
);

-- 8. product
CREATE TABLE product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(255) NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    unit VARCHAR(50),
    quantity INT DEFAULT 0,
    status VARCHAR(50),
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES product_category(id)
);

-- 9. shipper (inherits from employee 1:1)
CREATE TABLE shipper (
    id INT PRIMARY KEY,
    vehicle_plate_number VARCHAR(50),
    current_status VARCHAR(50),
    FOREIGN KEY (id) REFERENCES employee(id)
);

-- 10. timesheet
CREATE TABLE timesheet (
    id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT,
    hours_worked DECIMAL(5, 2),
    work_date DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);

-- 11. goods_receipt (phiếu nhập hàng)
CREATE TABLE goods_receipt (
    id INT PRIMARY KEY AUTO_INCREMENT,
    received_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    supplier_id INT,
    total_quantity INT,
    total_price DECIMAL(15, 2),
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);

-- 12. ingredient
CREATE TABLE ingredient (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ingredient_name VARCHAR(50),
    net_weight INT,
    quantity INT
);

-- 13. goods_receipt_detail
CREATE TABLE goods_receipt_detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    receipt_id INT,
    ingredient_id INT,
    quantity INT,
    unit_price DECIMAL(15, 2),
    FOREIGN KEY (receipt_id) REFERENCES goods_receipt(id),
    FOREIGN KEY (ingredient_id) REFERENCES ingredient(id)
);

-- 14. ingredient_product
CREATE TABLE ingredient_product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    ingredient_id INT,
    estimate INT,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (ingredient_id) REFERENCES ingredient(id)
);

-- 15. order (đơn đặt hàng chung)
CREATE TABLE `order` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    sub_total DECIMAL(15, 2),
    total_amount DECIMAL(15, 2),
    status VARCHAR(50),
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- 16. online_order (specialization of order)
CREATE TABLE online_order (
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
);

-- 17. order_detail
CREATE TABLE order_detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    product_id INT,
    quantity INT,
    unit_price DECIMAL(15, 2),
    FOREIGN KEY (order_id) REFERENCES `order`(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

-- 18. invoice
CREATE TABLE invoice (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    employee_id INT,
    order_id INT,
    payment_method_id INT,
    issued_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(15, 2),
    status VARCHAR(50) DEFAULT 'NEW',
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    FOREIGN KEY (order_id) REFERENCES `order`(id),
    FOREIGN KEY (payment_method_id) REFERENCES payment_method(id)
);

-- 19. invoice_detail
CREATE TABLE invoice_detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    invoice_id INT,
    product_id INT,
    quantity INT,
    unit_price DECIMAL(15, 2),
    FOREIGN KEY (invoice_id) REFERENCES invoice(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

-- 20. invoice_voucher_detail
CREATE TABLE invoice_voucher_detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    invoice_id INT,
    voucher_id INT,
    discount_value DECIMAL(15, 2),
    FOREIGN KEY (invoice_id) REFERENCES invoice(id),
    FOREIGN KEY (voucher_id) REFERENCES voucher(id)
);

-- INSERT some sample data
INSERT INTO role (role_name, hourly_rate) VALUES ('Manager', 25.00);
INSERT INTO role (role_name, hourly_rate) VALUES ('Staff', 12.50);
INSERT INTO product_category (category_name, description) VALUES ('Beverages', 'Coffee and drinks');
INSERT INTO payment_method (method_name) VALUES ('Cash');
INSERT INTO payment_method (method_name) VALUES ('Card');
INSERT INTO payment_method (method_name) VALUES ('Online Transfer');

-- Sample products
INSERT INTO product (product_name, unit_price, unit, quantity, status, category_id) 
VALUES ('Espresso', 2.50, 'cup', 100, 'AVAILABLE', 1);
INSERT INTO product (product_name, unit_price, unit, quantity, status, category_id) 
VALUES ('Cappuccino', 3.50, 'cup', 100, 'AVAILABLE', 1);
INSERT INTO product (product_name, unit_price, unit, quantity, status, category_id) 
VALUES ('Latte', 3.75, 'cup', 100, 'AVAILABLE', 1);

-- Sample customers
INSERT INTO customer (full_name, phone_number, email, password, status) 
VALUES ('Nguyen Van A', '0901234567', 'customer1@mail.com', 'pass123', 'ACTIVE');
INSERT INTO customer (full_name, phone_number, email, password, status) 
VALUES ('Tran Thi B', '0912345678', 'customer2@mail.com', 'pass123', 'ACTIVE');

-- Sample employees
INSERT INTO employee (first_name, last_name, phone_number, dob, address, basic_salary, status, role_id) 
VALUES ('Hoang', 'Van Da', '0909123456', '1990-01-15', 'Ha Noi', 500.00, 'ACTIVE', 1);
INSERT INTO employee (first_name, last_name, phone_number, dob, address, basic_salary, status, role_id) 
VALUES ('Pham', 'Thi Em', '0908123456', '1995-05-20', 'Ho Chi Minh', 300.00, 'ACTIVE', 2);

-- Sample orders
INSERT INTO `order` (customer_id, sub_total, total_amount, status) 
VALUES (1, 10.00, 10.00, 'COMPLETED');
INSERT INTO `order` (customer_id, sub_total, total_amount, status) 
VALUES (2, 7.50, 7.50, 'COMPLETED');

-- Sample invoices
INSERT INTO invoice (customer_id, employee_id, order_id, payment_method_id, total_amount, status) 
VALUES (1, 1, 1, 1, 10.00, 'NEW');
INSERT INTO invoice (customer_id, employee_id, order_id, payment_method_id, total_amount, status) 
VALUES (2, 2, 2, 2, 7.50, 'PAID');

-- Sample invoice details
INSERT INTO invoice_detail (invoice_id, product_id, quantity, unit_price) 
VALUES (1, 1, 2, 2.50);
INSERT INTO invoice_detail (invoice_id, product_id, quantity, unit_price) 
VALUES (1, 2, 2, 2.50);
INSERT INTO invoice_detail (invoice_id, product_id, quantity, unit_price) 
VALUES (2, 3, 2, 3.75);

-- Show success message
SELECT 'Database setup completed successfully!' as status;
