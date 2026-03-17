use jdbc_demo;;

-- 1. product_category
CREATE TABLE IF NOT EXISTS product_category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(255) NOT NULL,
    description TEXT
);

-- 2. supplier
CREATE TABLE IF NOT EXISTS supplier (
    id INT PRIMARY KEY AUTO_INCREMENT,
    supplier_name VARCHAR(255) NOT NULL,
    address TEXT,
    phone_number VARCHAR(20)
);

-- 3. role
CREATE TABLE IF NOT EXISTS role (
    id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(100) NOT NULL,
    hourly_rate DECIMAL(15, 2) DEFAULT 0
);

-- 4. customer
CREATE TABLE IF NOT EXISTS customer (
    id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(255),
    phone_number VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    status VARCHAR(50) DEFAULT 'active'
);

-- 5. payment_method
CREATE TABLE IF NOT EXISTS payment_method (
    id INT PRIMARY KEY AUTO_INCREMENT,
    method_name VARCHAR(100) NOT NULL
);

-- 6. voucher
CREATE TABLE IF NOT EXISTS voucher (
    id INT PRIMARY KEY AUTO_INCREMENT,
    promotion_name VARCHAR(255),
    start_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    end_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 7. employee
CREATE TABLE IF NOT EXISTS employee (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(20),
    dob DATE,
    address TEXT,
    basic_salary DECIMAL(15, 2) DEFAULT 0,
    status VARCHAR(50) DEFAULT 'active',
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES role(id)
);

-- 8. product
CREATE TABLE IF NOT EXISTS product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(255) NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    unit VARCHAR(50) DEFAULT 'unit',
    quantity INT DEFAULT 0,
    status VARCHAR(50) DEFAULT 'available',
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES product_category(id)
);

-- 9. shipper (inherits from employee 1:1)
CREATE TABLE IF NOT EXISTS shipper (
    id INT PRIMARY KEY,
    vehicle_plate_number VARCHAR(50),
    current_status VARCHAR(50) DEFAULT 'available',
    FOREIGN KEY (id) REFERENCES employee(id)
);

-- 10. timesheet
CREATE TABLE IF NOT EXISTS timesheet (
    id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT,
    hours_worked DECIMAL(5, 2) DEFAULT 0,
    work_date DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);

-- 11. goods_receipt (phiếu nhập hàng)
CREATE TABLE IF NOT EXISTS goods_receipt (
    id INT PRIMARY KEY AUTO_INCREMENT,
    received_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    supplier_id INT,
    total_quantity INT DEFAULT 0,
    total_price DECIMAL(15, 2) DEFAULT 0,
    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);

-- 12. ingredient
CREATE TABLE IF NOT EXISTS ingredient (
    id INT PRIMARY KEY AUTO_INCREMENT,
    ingredient_name VARCHAR(50),
    net_weight INT DEFAULT 0,
    quantity INT DEFAULT 0,
    total_weight INT DEFAULT 0,
    unit_price DECIMAL(15, 2) DEFAULT 0
);





-- 13. goods_receipt_detail
CREATE TABLE IF NOT EXISTS goods_receipt_detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    receipt_id INT,
    ingredient_id INT,
    net_weight INT DEFAULT 0,
    quantity INT DEFAULT 0,
    unit_price DECIMAL(15, 2) DEFAULT 0,
    FOREIGN KEY (receipt_id) REFERENCES goods_receipt(id),
    FOREIGN KEY (ingredient_id) REFERENCES ingredient(id)
);



-- 14. ingredient_product
CREATE TABLE IF NOT EXISTS ingredient_product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    ingredient_id INT,
    estimate INT DEFAULT 0,
    total_price DECIMAL(15, 2) DEFAULT 0,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (ingredient_id) REFERENCES ingredient(id)
);

-- 15. order (đơn đặt hàng chung)
CREATE TABLE IF NOT EXISTS `order` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    sub_total DECIMAL(15, 2) DEFAULT 0,
    total_amount DECIMAL(15, 2) DEFAULT 0,
    status VARCHAR(50) DEFAULT 'pending',
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- 16. online_order (specialization of order)
CREATE TABLE IF NOT EXISTS online_order (
    id INT PRIMARY KEY,
    customer_id INT,
    shipper_id INT,
    receiver_name VARCHAR(255),
    phone_number VARCHAR(20),
    address TEXT,
    shipping_fee DECIMAL(15, 2) DEFAULT 0,
    estimated_delivery_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    completed_time DATETIME DEFAULT NULL,
    status VARCHAR(50) DEFAULT 'pending',
    total_amount DECIMAL(15, 2) DEFAULT 0,
    FOREIGN KEY (id) REFERENCES `order`(id),
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (shipper_id) REFERENCES shipper(id)
);

-- 17. order_detail
CREATE TABLE IF NOT EXISTS order_detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    product_id INT,
    quantity INT DEFAULT 0,
    unit_price DECIMAL(15, 2) DEFAULT 0,
    FOREIGN KEY (order_id) REFERENCES `order`(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

-- 18. invoice
CREATE TABLE IF NOT EXISTS invoice (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    employee_id INT,
    order_id INT,
    payment_method_id INT,
    issued_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(15, 2) DEFAULT 0,
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    FOREIGN KEY (order_id) REFERENCES `order`(id),
    FOREIGN KEY (payment_method_id) REFERENCES payment_method(id)
);

-- 19. invoice_detail
CREATE TABLE IF NOT EXISTS invoice_detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    invoice_id INT,
    product_id INT,
    quantity INT DEFAULT 0,
    unit_price DECIMAL(15, 2) DEFAULT 0,
    FOREIGN KEY (invoice_id) REFERENCES invoice(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

-- 20. invoice_voucher_detail
CREATE TABLE IF NOT EXISTS invoice_voucher_detail  (
    id INT PRIMARY KEY AUTO_INCREMENT,
    invoice_id INT,
    voucher_id INT,
    discount_value DECIMAL(15, 2) DEFAULT 0,
    FOREIGN KEY (invoice_id) REFERENCES invoice(id),
    FOREIGN KEY (voucher_id) REFERENCES voucher(id)
);

DELIMITER $$

CREATE TRIGGER IF NOT EXISTS trg_ingredient_before_insert
BEFORE INSERT ON ingredient
FOR EACH ROW
BEGIN
    SET NEW.total_weight = COALESCE(NEW.net_weight, 0) * COALESCE(NEW.quantity, 0);
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER IF NOT EXISTS trg_ingredient_product_before_insert
BEFORE INSERT ON ingredient_product
FOR EACH ROW
BEGIN
    DECLARE ing_price DECIMAL(15,2);
    DECLARE ing_weight INT;

    SELECT unit_price, net_weight
    INTO ing_price, ing_weight
    FROM ingredient
    WHERE id = NEW.ingredient_id;

    SET NEW.total_price = COALESCE(NEW.estimate,0)
                          * COALESCE(ing_price,0)
                          / NULLIF(ing_weight,0);
END$$

DELIMITER ;
DELIMITER $$

CREATE TRIGGER IF NOT EXISTS trg_goods_receipt_detail_after_insert
AFTER INSERT ON goods_receipt_detail
FOR EACH ROW
BEGIN
    UPDATE ingredient ing
    SET ing.net_weight = COALESCE(new.net_weight,0),
        ing.quantity = COALESCE(NEW.quantity,0),
        ing.unit_price = COALESCE(NEW.unit_price, ing.unit_price),
        ing.total_weight = COALESCE(ing.total_weight,0)+(COALESCE(NEW.net_weight,0) * COALESCE(NEW.quantity,0))
    WHERE ing.id = NEW.ingredient_id;
END$$

DELIMITER ;




