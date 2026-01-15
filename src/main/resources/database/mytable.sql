-- 1. Product Category
CREATE TABLE Product_Category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(255) NOT NULL,
    description TEXT
);

-- 2. Supplier
CREATE TABLE Supplier (
    id INT PRIMARY KEY AUTO_INCREMENT,
    supplier_name VARCHAR(255) NOT NULL,
    address TEXT,
    phone_number VARCHAR(20)
);

-- 3. Employee Role
CREATE TABLE Role (
    id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(100) NOT NULL,
    hourly_rate DECIMAL(15, 2) DEFAULT 0
);

-- 4. Customer
CREATE TABLE Customer (
    id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(255),
    phone_number VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    status VARCHAR(50)
);

-- 5. Payment Method
CREATE TABLE Payment_Method (
    id INT PRIMARY KEY AUTO_INCREMENT,
    method_name VARCHAR(100) NOT NULL
);

-- 6. Voucher
CREATE TABLE Voucher (
    id INT PRIMARY KEY AUTO_INCREMENT,
    promotion_name VARCHAR(255),
    start_date DATETIME,
    end_date DATETIME
);
-- 7. Employee
CREATE TABLE Employee (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(20),
    dob DATE,
    address TEXT,
    basic_salary DECIMAL(15, 2),
    status VARCHAR(50),
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES Role(id)
);

-- 8. Product
CREATE TABLE Product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(255) NOT NULL,
    unit_price DECIMAL(15, 2) NOT NULL,
    unit VARCHAR(50),
    quantity INT DEFAULT 0,
    status VARCHAR(50),
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES Product_Category(id)
);

-- 9. Shipper (Inherits from Employee 1:1)
CREATE TABLE Shipper (
    id INT PRIMARY KEY,
    vehicle_plate_number VARCHAR(50),
    current_status VARCHAR(50),
    FOREIGN KEY (id) REFERENCES Employee(id)
);

-- 10. Timesheet
CREATE TABLE Timesheet (
    id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT,
    hours_worked DECIMAL(5, 2),
    work_date DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (employee_id) REFERENCES Employee(id)
);
-- 11. Goods Receipt (Phiếu nhập hàng)
CREATE TABLE Goods_Receipt (
    id INT PRIMARY KEY AUTO_INCREMENT,
    received_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    supplier_id INT,
    total_quantity INT,
    total_price DECIMAL(15, 2),
    FOREIGN KEY (supplier_id) REFERENCES Supplier(id)
);

-- 12. Goods Receipt Detail
CREATE TABLE Goods_Receipt_Detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    receipt_id INT,
    product_id INT,
    quantity INT,
    unit_price DECIMAL(15, 2),
    FOREIGN KEY (receipt_id) REFERENCES Goods_Receipt(id),
    FOREIGN KEY (product_id) REFERENCES Product(id)
);

-- 13. Order (Đơn đặt hàng chung)
CREATE TABLE `Order` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    sub_total DECIMAL(15, 2),
    total_amount DECIMAL(15, 2),
    status VARCHAR(50),
    FOREIGN KEY (customer_id) REFERENCES Customer(id)
);

-- 14. Online Order (Specialization of Order)
CREATE TABLE Online_Order (
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
    FOREIGN KEY (id) REFERENCES `Order`(id),
    FOREIGN KEY (customer_id) REFERENCES Customer(id),
    FOREIGN KEY (shipper_id) REFERENCES Shipper(id)
);

-- 15. Order Detail
CREATE TABLE Order_Detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    product_id INT,
    quantity INT,
    unit_price DECIMAL(15, 2),
    FOREIGN KEY (order_id) REFERENCES `Order`(id),
    FOREIGN KEY (product_id) REFERENCES Product(id)
);
-- 16. Invoice
CREATE TABLE Invoice (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    employee_id INT,
    order_id INT,
    payment_method_id INT,
    issued_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(15, 2),
    FOREIGN KEY (customer_id) REFERENCES Customer(id),
    FOREIGN KEY (employee_id) REFERENCES Employee(id),
    FOREIGN KEY (order_id) REFERENCES `Order`(id),
    FOREIGN KEY (payment_method_id) REFERENCES Payment_Method(id)
);

-- 17. Invoice Detail
CREATE TABLE Invoice_Detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    invoice_id INT,
    product_id INT,
    quantity INT,
    unit_price DECIMAL(15, 2),
    FOREIGN KEY (invoice_id) REFERENCES Invoice(id),
    FOREIGN KEY (product_id) REFERENCES Product(id)
);

-- 18. Invoice Voucher Detail
CREATE TABLE Invoice_Voucher_Detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    invoice_id INT,
    voucher_id INT,
    discount_value DECIMAL(15, 2),
    FOREIGN KEY (invoice_id) REFERENCES Invoice(id),
    FOREIGN KEY (voucher_id) REFERENCES Voucher(id)
);