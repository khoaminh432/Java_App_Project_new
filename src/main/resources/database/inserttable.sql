-- Product Category
INSERT INTO Product_Category (category_name, description) VALUES
('Electronics', 'Gadgets, phones, and laptops'),
('Beverages', 'Soft drinks, coffee, and tea'),
('Foods', 'Snacks and instant meals'),
('Home Appliances', 'Fridge, microwave, and fans'),
('Stationery', 'Pens, notebooks, and office supplies');

-- Supplier
INSERT INTO Supplier (supplier_name, address, phone_number) VALUES
('Global Tech Inc', '123 Silicon Valley, USA', '0123456789'),
('Fresh Drinks Co', '45 Highland Road, VN', '0987654321'),
('Foodie Distribution', '78 Street Food, Thailand', '0222333444'),
('Home Living Ltd', '12 Furniture Ave, China', '0555666777'),
('Office World', '90 Business Park, VN', '0888999000');

-- Role
INSERT INTO Role (role_name, hourly_rate) VALUES
('Manager', 50.00),
('Sales Staff', 20.00),
('Warehouse Keeper', 18.00),
('Shipper', 15.00),
('Security', 12.00);

-- Customer
INSERT INTO Customer (full_name, phone_number, email, password, status) VALUES
('Nguyen Van A', '0901234567', 'ana@example.com', 'hashed_pass_1', 'Active'),
('Le Thi B', '0902345678', 'beethib@example.com', 'hashed_pass_2', 'Active'),
('Tran Van C', '0903456789', 'ceev@example.com', 'hashed_pass_3', 'Inactive'),
('Pham Thi D', '0904567890', 'deept@example.com', 'hashed_pass_4', 'Active'),
('Hoang Van E', '0905678901', 'eehv@example.com', 'hashed_pass_5', 'Active');

-- Payment Method
INSERT INTO Payment_Method (method_name) VALUES
('Cash'),
('Credit Card'),
('E-Wallet (Momo)'),
('Bank Transfer'),
('VNPAY');

-- Voucher
INSERT INTO Voucher (promotion_name, start_date, end_date) VALUES
('New Year Sale', '2026-01-01', '2026-01-15'),
('Black Friday', '2026-11-20', '2026-11-30'),
('Member Discount', '2026-01-01', '2026-12-31'),
('Summer Deal', '2026-06-01', '2026-08-31'),
('Flash Sale 12.12', '2026-12-12', '2026-12-13');
-- Employee
INSERT INTO Employee (first_name, last_name, phone_number, dob, address, basic_salary, status, role_id) VALUES
('John', 'Smith', '0111222333', '1990-05-15', 'New York', 5000.00, 'Working', 1),
('Minh', 'Hoang', '0222333444', '1995-10-20', 'Hanoi', 2000.00, 'Working', 2),
('Lan', 'Anh', '0333444555', '1998-02-28', 'HCM City', 1800.00, 'Working', 3),
('Cuong', 'Do', '0444555666', '1992-12-12', 'Da Nang', 1500.00, 'Working', 4),
('Bao', 'Quoc', '0555666777', '1988-07-07', 'Can Tho', 1200.00, 'Working', 5);

-- Product
INSERT INTO Product (product_name, unit_price, unit, quantity, status, category_id) VALUES
('iPhone 15', 1000.00, 'Unit', 50, 'Available', 1),
('Coca Cola', 0.50, 'Can', 500, 'Available', 2),
('Oreo Cookies', 1.20, 'Pack', 200, 'Available', 3),
('Ceiling Fan', 45.00, 'Unit', 30, 'Available', 4),
('A4 Paper', 5.00, 'Ream', 100, 'Available', 5);

-- Shipper (ID mapping from Employee table)
INSERT INTO Shipper (id, vehicle_plate_number, current_status) VALUES
(4, '29-A1 12345', 'Busy'),
(1, '29-B1 67890', 'Idle'), -- Example of a manager who can ship
(2, '29-C1 55555', 'Idle'),
(3, '29-D1 99999', 'Busy'),
(5, '29-E1 11111', 'Idle');

-- Timesheet
INSERT INTO Timesheet (employee_id, hours_worked, work_date) VALUES
(1, 8.0, '2026-01-10'),
(2, 7.5, '2026-01-10'),
(3, 8.0, '2026-01-10'),
(4, 10.0, '2026-01-10'),
(5, 8.0, '2026-01-10');
-- Goods Receipt
INSERT INTO Goods_Receipt (supplier_id, total_quantity, total_price) VALUES
(1, 10, 10000.00),
(2, 100, 50.00),
(3, 50, 60.00),
(4, 5, 225.00),
(5, 20, 100.00);

-- Order
INSERT INTO `Order` (customer_id, sub_total, total_amount, status) VALUES
(1, 1000.00, 1000.00, 'Completed'),
(2, 0.50, 0.50, 'Processing'),
(3, 1.20, 1.20, 'Completed'),
(4, 45.00, 50.00, 'Shipping'),
(5, 10.00, 10.00, 'Cancelled');

-- Online Order (ID mapping from Order table)
INSERT INTO Online_Order (id, customer_id, shipper_id, receiver_name, phone_number, address, shipping_fee, status, total_amount) VALUES
(4, 4, 4, 'Pham Thi D', '0904567890', 'Hanoi Sector 1', 5.00, 'Shipping', 50.00),
(2, 2, 4, 'Le Thi B', '0902345678', 'HCM City District 3', 0.00, 'Processing', 0.50);

-- Order Detail
INSERT INTO Order_Detail (order_id, product_id, quantity, unit_price) VALUES
(1, 1, 1, 1000.00),
(2, 2, 1, 0.50),
(3, 3, 1, 1.20),
(4, 4, 1, 45.00),
(5, 5, 2, 5.00);

-- Invoice
INSERT INTO Invoice (customer_id, employee_id, order_id, payment_method_id, total_amount) VALUES
(1, 2, 1, 2, 1000.00),
(3, 2, 3, 1, 1.20);

-- Invoice Detail
INSERT INTO Invoice_Detail (invoice_id, product_id, quantity, unit_price) VALUES
(1, 1, 1, 1000.00),
(2, 3, 1, 1.20);

-- Invoice Voucher Detail
INSERT INTO Invoice_Voucher_Detail (invoice_id, voucher_id, discount_value) VALUES
(1, 1, 50.00),
(2, 3, 0.20);       