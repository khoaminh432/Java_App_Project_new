-- 1. Product Category
INSERT INTO Product_Category (category_name, description) VALUES
('Cà phê', 'Các loại cà phê truyền thống và đặc biệt'),
('Trà', 'Trà các loại, trà hoa quả'),
('Nước ép', 'Nước ép trái cây tươi'),
('Sinh tố', 'Sinh tố trái cây'),
('Bánh ngọt', 'Bánh ngọt tự làm'),
('Đồ ăn nhẹ', 'Snack, sandwich'),
('Đồ uống đá xay', 'Các loại đá xay'),
('Cà phê máy', 'Cà phê pha máy'),
('Trà sữa', 'Các loại trà sữa'),
('Đồ uống theo mùa', 'Đồ uống đặc biệt theo mùa');

-- 2. Supplier
INSERT INTO Supplier (supplier_name, address, phone_number) VALUES
('Công ty Cà phê Trung Nguyên', '123 Nguyễn Văn Linh, Q7, TP.HCM', '02838456789'),
('Nhà cung cấp Sữa Vinamilk', '456 Lê Lợi, Q1, TP.HCM', '02838234567'),
('Công ty Trà Lipton', '789 Pasteur, Q3, TP.HCM', '02838345678'),
('Nhập khẩu Trái cây Fresh', '321 Cách Mạng Tháng 8, Q10, TP.HCM', '02838765432'),
('Cung cấp Bánh ABC', '654 Nguyễn Trãi, Q5, TP.HCM', '02838654321'),
('Nhà cung cấp Đường Biên Hòa', '987 Hai Bà Trưng, Q1, TP.HCM', '02838543210'),
('Công ty Đồ uống Sài Gòn', '147 Phạm Ngọc Thạch, Q3, TP.HCM', '02838987654'),
('Cung cấp Nguyên liệu Pha chế', '258 Lý Tự Trọng, Q1, TP.HCM', '02838789012'),
('Nhập khẩu Cà phê Ý', '369 Điện Biên Phủ, Bình Thạnh, TP.HCM', '02838432109'),
('Cung cấp Vật tư One-time', '741 Lê Văn Sỹ, Tân Bình, TP.HCM', '02838321098');

-- 3. Employee Role
INSERT INTO Role (role_name, hourly_rate) VALUES
('Quản lý', 60000.00),
('Thu ngân', 35000.00),
('Pha chế', 40000.00),
('Phục vụ', 30000.00),
('Shipper', 25000.00),
('Tạp vụ', 25000.00),
('Bảo vệ', 28000.00),
('Kế toán', 50000.00),
('Trưởng ca', 45000.00),
('Nhân viên part-time', 28000.00);

-- 4. Customer
INSERT INTO Customer (full_name, phone_number, email, password, status) VALUES
('Nguyễn Văn An', '0901234567', 'an.nguyen@gmail.com', 'hashed_password_1', 'ACTIVE'),
('Trần Thị Bình', '0912345678', 'binh.tran@gmail.com', 'hashed_password_2', 'ACTIVE'),
('Lê Minh Cường', '0923456789', 'cuong.le@gmail.com', 'hashed_password_3', 'ACTIVE'),
('Phạm Thị Dung', '0934567890', 'dung.pham@gmail.com', 'hashed_password_4', 'INACTIVE'),
('Hoàng Văn Em', '0945678901', 'em.hoang@gmail.com', 'hashed_password_5', 'ACTIVE'),
('Vũ Thị Phương', '0956789012', 'phuong.vu@gmail.com', 'hashed_password_6', 'ACTIVE'),
('Đặng Minh Hải', '0967890123', 'hai.dang@gmail.com', 'hashed_password_7', 'ACTIVE'),
('Bùi Thị Kim', '0978901234', 'kim.bui@gmail.com', 'hashed_password_8', 'INACTIVE'),
('Ngô Văn Long', '0989012345', 'long.ngo@gmail.com', 'hashed_password_9', 'ACTIVE'),
('Đỗ Thị Mai', '0990123456', 'mai.do@gmail.com', 'hashed_password_10', 'ACTIVE');

-- 5. Payment Method
INSERT INTO Payment_Method (method_name) VALUES
('Tiền mặt'),
('Thẻ ngân hàng'),
('Ví điện tử Momo'),
('Ví điện tử ZaloPay'),
('Thẻ tín dụng'),
('Internet Banking'),
('QR Code'),
('Chuyển khoản'),
('Thẻ quà tặng'),
('Trả sau');

-- 6. Voucher
INSERT INTO Voucher (promotion_name, start_date, end_date) VALUES
('Giảm 20% cho khách mới', '2024-01-01 00:00:00', '2024-12-31 23:59:59'),
('Mua 1 tặng 1 cà phê', '2024-02-01 00:00:00', '2024-02-29 23:59:59'),
('Giảm 30% cuối tuần', '2024-03-01 00:00:00', '2024-03-31 23:59:59'),
('Freeship đơn từ 100k', '2024-01-15 00:00:00', '2024-06-15 23:59:59'),
('Tặng bánh ngọt', '2024-04-01 00:00:00', '2024-04-30 23:59:59'),
('Giảm 15% sinh nhật', '2024-01-01 00:00:00', '2024-12-31 23:59:59'),
('Combo sáng 50k', '2024-05-01 00:00:00', '2024-05-31 23:59:59'),
('Tích điểm 2x', '2024-06-01 00:00:00', '2024-08-31 23:59:59'),
('Giảm 10% đơn online', '2024-03-15 00:00:00', '2024-09-15 23:59:59'),
('Voucher Tết 50k', '2024-01-20 00:00:00', '2024-02-20 23:59:59');

-- 7. Employee
INSERT INTO Employee (first_name, last_name, phone_number, dob, address, basic_salary, status, role_id) VALUES
('Minh', 'Nguyễn', '0901111111', '1990-05-15', '123 Lý Thường Kiệt, Q10, TP.HCM', 15000000.00, 'ACTIVE', 1),
('Lan', 'Trần', '0902222222', '1995-08-20', '456 Lê Duẩn, Q1, TP.HCM', 8000000.00, 'ACTIVE', 2),
('Hùng', 'Lê', '0903333333', '1992-11-10', '789 Nguyễn Tri Phương, Q5, TP.HCM', 9000000.00, 'ACTIVE', 3),
('Hương', 'Phạm', '0904444444', '1998-03-25', '321 Cộng Hòa, Tân Bình, TP.HCM', 7000000.00, 'ACTIVE', 4),
('Tuấn', 'Hoàng', '0905555555', '1993-07-12', '654 Trường Chinh, Tân Phú, TP.HCM', 6000000.00, 'ACTIVE', 5),
('Thảo', 'Vũ', '0906666666', '1996-09-05', '987 Nguyễn Oanh, Gò Vấp, TP.HCM', 5500000.00, 'ACTIVE', 6),
('Hải', 'Đặng', '0907777777', '1991-12-30', '147 Phan Văn Trị, Bình Thạnh, TP.HCM', 8500000.00, 'ACTIVE', 7),
('Kim', 'Bùi', '0908888888', '1994-04-18', '258 Lý Chính Thắng, Q3, TP.HCM', 10000000.00, 'INACTIVE', 8),
('Long', 'Ngô', '0909999999', '1997-06-22', '369 Nguyễn Thái Sơn, Gò Vấp, TP.HCM', 7500000.00, 'ACTIVE', 9),
('Mai', 'Đỗ', '0910000000', '1999-01-08', '741 Lê Quang Định, Bình Thạnh, TP.HCM', 5000000.00, 'ACTIVE', 10);

-- 8. Product
INSERT INTO Product (product_name, unit_price, unit, quantity, status, category_id) VALUES
('Cà phê đen đá', 25000.00, 'ly', 100, 'AVAILABLE', 1),
('Cà phê sữa đá', 30000.00, 'ly', 150, 'AVAILABLE', 1),
('Trà đào cam sả', 35000.00, 'ly', 80, 'AVAILABLE', 2),
('Nước ép cam', 40000.00, 'ly', 60, 'AVAILABLE', 3),
('Sinh tố bơ', 45000.00, 'ly', 50, 'AVAILABLE', 4),
('Bánh su kem', 20000.00, 'cái', 40, 'AVAILABLE', 5),
('Sandwich gà', 35000.00, 'phần', 70, 'AVAILABLE', 6),
('Cookie socola', 15000.00, 'cái', 120, 'AVAILABLE', 5),
('Trà sữa trân châu', 45000.00, 'ly', 90, 'AVAILABLE', 9),
('Caramel macchiato', 50000.00, 'ly', 75, 'AVAILABLE', 8);

-- 9. Shipper
INSERT INTO Shipper (id, vehicle_plate_number, current_status) VALUES
(5, '59A1-12345', 'AVAILABLE'),
(8, '51B2-67890', 'BUSY'),
(10, '59C3-54321', 'AVAILABLE');

-- 10. Timesheet
INSERT INTO Timesheet (employee_id, hours_worked, work_date) VALUES
(1, 8.0, '2024-03-01'),
(2, 7.5, '2024-03-01'),
(3, 8.0, '2024-03-01'),
(4, 8.0, '2024-03-01'),
(5, 6.0, '2024-03-01'),
(6, 8.0, '2024-03-01'),
(7, 12.0, '2024-03-01'),
(8, 8.0, '2024-03-01'),
(9, 8.0, '2024-03-01'),
(10, 4.0, '2024-03-01');

-- 11. Goods Receipt
INSERT INTO Goods_Receipt (received_date, supplier_id, total_quantity, total_price) VALUES
('2024-02-28 09:00:00', 1, 50, 5000000.00),
('2024-02-29 10:30:00', 2, 100, 3000000.00),
('2024-03-01 08:45:00', 3, 80, 2400000.00),
('2024-03-01 14:20:00', 4, 60, 1800000.00),
('2024-02-27 11:15:00', 5, 40, 800000.00),
('2024-02-26 13:40:00', 6, 200, 2000000.00),
('2024-02-28 16:00:00', 7, 150, 4500000.00),
('2024-03-01 09:30:00', 8, 90, 2700000.00),
('2024-02-29 15:10:00', 9, 70, 7000000.00),
('2024-02-25 10:00:00', 10, 30, 900000.00);

-- 12. Goods Receipt Detail
INSERT INTO Goods_Receipt_Detail (receipt_id, product_id, quantity, unit_price) VALUES
(1, 1, 50, 100000.00),
(2, 2, 100, 30000.00),
(3, 3, 80, 30000.00),
(4, 4, 60, 30000.00),
(5, 5, 40, 20000.00),
(6, 6, 200, 10000.00),
(7, 7, 150, 30000.00),
(8, 8, 90, 30000.00),
(9, 9, 70, 100000.00),
(10, 10, 30, 30000.00);

-- 13. Order
INSERT INTO `Order` (customer_id, order_date, sub_total, total_amount, status) VALUES
(1, '2024-03-01 08:30:00', 75000.00, 75000.00, 'COMPLETED'),
(2, '2024-03-01 09:15:00', 120000.00, 120000.00, 'COMPLETED'),
(3, '2024-03-01 10:00:00', 65000.00, 65000.00, 'PROCESSING'),
(4, '2024-03-01 11:30:00', 85000.00, 85000.00, 'PENDING'),
(5, '2024-03-01 12:45:00', 140000.00, 140000.00, 'COMPLETED'),
(6, '2024-03-01 14:20:00', 55000.00, 55000.00, 'COMPLETED'),
(7, '2024-03-01 15:10:00', 90000.00, 90000.00, 'CANCELLED'),
(8, '2024-03-01 16:30:00', 110000.00, 110000.00, 'PROCESSING'),
(9, '2024-03-01 17:45:00', 70000.00, 70000.00, 'COMPLETED'),
(10, '2024-03-01 18:20:00', 95000.00, 95000.00, 'PENDING');

-- 14. Online Order
INSERT INTO Online_Order (id, customer_id, shipper_id, receiver_name, phone_number, address, shipping_fee, estimated_delivery_time, completed_time, status, total_amount) VALUES
(2, 2, 5, 'Trần Thị Bình', '0912345678', '456 Lê Lợi, Q1, TP.HCM', 15000.00, '2024-03-01 10:00:00', '2024-03-01 09:50:00', 'DELIVERED', 135000.00),
(5, 5, 10, 'Hoàng Văn Em', '0945678901', '654 Trường Chinh, Tân Phú, TP.HCM', 10000.00, '2024-03-01 13:30:00', '2024-03-01 13:15:00', 'DELIVERED', 150000.00),
(8, 8, 5, 'Bùi Thị Kim', '0978901234', '258 Lý Chính Thắng, Q3, TP.HCM', 20000.00, '2024-03-01 17:00:00', NULL, 'DELIVERING', 130000.00);

-- 15. Order Detail
INSERT INTO Order_Detail (order_id, product_id, quantity, unit_price) VALUES
(1, 1, 2, 25000.00),
(1, 6, 1, 20000.00),
(1, 8, 2, 15000.00),
(2, 2, 3, 30000.00),
(2, 7, 1, 35000.00),
(3, 3, 1, 35000.00),
(3, 5, 1, 45000.00),
(4, 4, 2, 40000.00),
(5, 9, 2, 45000.00),
(5, 10, 1, 50000.00);

-- 16. Invoice
INSERT INTO Invoice (customer_id, employee_id, order_id, payment_method_id, issued_date, total_amount) VALUES
(1, 2, 1, 1, '2024-03-01 08:35:00', 75000.00),
(2, 2, 2, 2, '2024-03-01 09:20:00', 135000.00),
(3, 2, 3, 3, '2024-03-01 10:05:00', 65000.00),
(4, 2, 4, 1, '2024-03-01 11:35:00', 85000.00),
(5, 2, 5, 4, '2024-03-01 12:50:00', 150000.00),
(6, 2, 6, 1, '2024-03-01 14:25:00', 55000.00),
(7, 2, 7, 2, '2024-03-01 15:15:00', 90000.00),
(8, 2, 8, 3, '2024-03-01 16:35:00', 130000.00),
(9, 2, 9, 1, '2024-03-01 17:50:00', 70000.00),
(10, 2, 10, 5, '2024-03-01 18:25:00', 95000.00);

-- 17. Invoice Detail
INSERT INTO Invoice_Detail (invoice_id, product_id, quantity, unit_price) VALUES
(1, 1, 2, 25000.00),
(1, 6, 1, 20000.00),
(1, 8, 2, 15000.00),
(2, 2, 3, 30000.00),
(2, 7, 1, 35000.00),
(3, 3, 1, 35000.00),
(3, 5, 1, 45000.00),
(4, 4, 2, 40000.00),
(5, 9, 2, 45000.00),
(5, 10, 1, 50000.00);

-- 18. Invoice Voucher Detail
INSERT INTO Invoice_Voucher_Detail (invoice_id, voucher_id, discount_value) VALUES
(2, 1, 27000.00),
(5, 3, 45000.00),
(8, 9, 13000.00);