use jdbc_demo;

-- 1. product_category
INSERT INTO product_category (category_name, description) VALUES
('Cà phê', 'Các loại cà phê nguyên chất và pha chế'),
('Trà', 'Trà các loại: trà sữa, trà trái cây, trà thảo mộc'),
('Nước ép & Sinh tố', 'Nước ép trái cây tươi và sinh tố'),
('Bánh ngọt', 'Các loại bánh ngọt, bánh mì, bánh kem'),
('Đồ ăn nhẹ', 'Đồ ăn vặt, snack'),
('Đồ uống đặc biệt', 'Các loại đồ uống đặc biệt của quán'),
('Combo', 'Các gói combo tiết kiệm'),
('Đồ uống lạnh', 'Các loại đồ uống lạnh'),
('Đồ uống nóng', 'Các loại đồ uống nóng'),
('Topping', 'Các loại topping thêm');

-- 2. supplier
INSERT INTO supplier (supplier_name, address, phone_number) VALUES
('Công ty Cà phê Trung Nguyên', '123 Nguyễn Văn Linh, Q.7, TP.HCM', '0281234567'),
('Nhà cung cấp sữa Vinamilk', '456 Lý Thường Kiệt, Q.10, TP.HCM', '0282345678'),
('Công ty Trà Lipton', '789 Cách Mạng Tháng 8, Q.3, TP.HCM', '0283456789'),
('Nhà cung cấp trái cây tươi', '321 Nguyễn Tri Phương, Q.5, TP.HCM', '0284567890'),
('Công ty Bánh kẹo Hải Hà', '654 Lê Lợi, Q.1, TP.HCM', '0285678901'),
('Nhà cung cấp đường Biên Hòa', '987 Hai Bà Trưng, Q.1, TP.HCM', '0286789012'),
('Công ty Đá viên sạch', '147 Pasteur, Q.1, TP.HCM', '0287890123'),
('Nhà cung cấp cốc ly Minh Long', '258 Nguyễn Thị Minh Khai, Q.1, TP.HCM', '0288901234'),
('Công ty Topping Richy', '369 Phạm Ngũ Lão, Q.1, TP.HCM', '0289012345'),
('Nhà cung cấp nước suối La Vie', '741 Nguyễn Văn Cừ, Q.5, TP.HCM', '0280123456');

-- 3. role
INSERT INTO role (role_name, hourly_rate) VALUES
('Quản lý', 50000.00),
('Nhân viên pha chế', 35000.00),
('Nhân viên thu ngân', 30000.00),
('Nhân viên phục vụ', 25000.00),
('Shipper', 20000.00),
('Quản lý kho', 40000.00),
('Trưởng ca', 45000.00),
('Nhân viên vệ sinh', 20000.00),
('Pha chế chính', 40000.00),
('Nhân viên bán hàng online', 30000.00);

-- 4. customer
INSERT INTO customer (full_name, phone_number, email, password, status) VALUES
('Nguyễn Văn An', '0901111111', 'nguyenvanan@gmail.com', 'password123', 'active'),
('Trần Thị Bình', '0902222222', 'tranthibinh@gmail.com', 'password123', 'active'),
('Lê Minh Châu', '0903333333', 'leminhchau@gmail.com', 'password123', 'active'),
('Phạm Văn Đức', '0904444444', 'phamvanduc@gmail.com', 'password123', 'active'),
('Hoàng Thị Em', '0905555555', 'hoangthiem@gmail.com', 'password123', 'inactive'),
('Vũ Văn Phong', '0906666666', 'vuvanphong@gmail.com', 'password123', 'active'),
('Đặng Thị Quỳnh', '0907777777', 'dangthiquynh@gmail.com', 'password123', 'active'),
('Bùi Văn Hải', '0908888888', 'buivanhai@gmail.com', 'password123', 'active'),
('Đỗ Thị Lan', '0909999999', 'dothilan@gmail.com', 'password123', 'active'),
('Ngô Văn Minh', '0900000000', 'ngovanminh@gmail.com', 'password123', 'inactive');

-- 5. payment_method
INSERT INTO payment_method (method_name) VALUES
('Tiền mặt'),
('Chuyển khoản ngân hàng'),
('Visa/Mastercard'),
('MoMo'),
('ZaloPay'),
('VNPay'),
('ShopeePay'),
('Thẻ thành viên'),
('Ví điện tử'),
('QR Code');

-- 6. voucher
INSERT INTO voucher (promotion_name, start_date, end_date) VALUES
('Giảm 20% cho đơn đầu tiên', '2024-01-01 00:00:00', '2024-12-31 23:59:59'),
('Giảm 30K cho đơn từ 150K', '2024-02-01 00:00:00', '2024-06-30 23:59:59'),
('Mua 1 tặng 1', '2024-03-01 00:00:00', '2024-03-31 23:59:59'),
('Giảm 15% tất cả đồ uống', '2024-04-01 00:00:00', '2024-04-30 23:59:59'),
('Freeship 5KM', '2024-05-01 00:00:00', '2024-05-31 23:59:59'),
('Giảm 50% sinh tố', '2024-06-01 00:00:00', '2024-06-15 23:59:59'),
('Combo gia đình 199K', '2024-07-01 00:00:00', '2024-07-31 23:59:59'),
('Tích điểm 2x', '2024-08-01 00:00:00', '2024-08-31 23:59:59'),
('Giảm 25K bánh ngọt', '2024-09-01 00:00:00', '2024-09-30 23:59:59'),
('Đồ uống thứ 2 chỉ 50K', '2024-10-01 00:00:00', '2024-10-31 23:59:59');

-- 7. employee
INSERT INTO employee (first_name, last_name, phone_number, dob, address, basic_salary, status, role_id) VALUES
('Mai', 'Văn Hùng', '0911111111', '1990-05-15', '12 Lê Lợi, Q.1, TP.HCM', 15000000.00, 'active', 1),
('Lê', 'Thị Mai', '0912222222', '1995-08-20', '34 Nguyễn Huệ, Q.1, TP.HCM', 12000000.00, 'active', 2),
('Trần', 'Văn Nam', '0913333333', '1998-03-10', '56 Pasteur, Q.3, TP.HCM', 10000000.00, 'active', 3),
('Nguyễn', 'Thị Hương', '0914444444', '1997-11-25', '78 CMT8, Q.10, TP.HCM', 9000000.00, 'active', 4),
('Phạm', 'Văn Tài', '0915555555', '1996-07-30', '90 Lý Tự Trọng, Q.1, TP.HCM', 8000000.00, 'active', 5),
('Hoàng', 'Thị Thu', '0916666666', '1994-02-14', '112 Nguyễn Thị Minh Khai, Q.3, TP.HCM', 11000000.00, 'active', 6),
('Vũ', 'Văn Sơn', '0917777777', '1993-09-05', '134 Hai Bà Trưng, Q.1, TP.HCM', 13000000.00, 'active', 7),
('Đặng', 'Thị Nga', '0918888888', '1999-12-12', '156 Điện Biên Phủ, Q.3, TP.HCM', 7000000.00, 'active', 8),
('Bùi', 'Văn Đạt', '0919999999', '1992-06-18', '178 Trần Hưng Đạo, Q.5, TP.HCM', 11500000.00, 'active', 9),
('Đỗ', 'Thị Hoa', '0910000000', '2000-04-22', '190 Nguyễn Văn Cừ, Q.5, TP.HCM', 9500000.00, 'active', 10);

-- 8. product
INSERT INTO product (product_name, unit_price, unit, quantity, status, category_id) VALUES
('Cà phê đen đá', 25000.00, 'ly', 100, 'available', 1),
('Cà phê sữa đá', 30000.00, 'ly', 150, 'available', 1),
('Trà sữa trân châu', 45000.00, 'ly', 200, 'available', 2),
('Trà đào cam sả', 40000.00, 'ly', 180, 'available', 2),
('Sinh tố bơ', 50000.00, 'ly', 120, 'available', 3),
('Nước ép cam', 35000.00, 'ly', 90, 'available', 3),
('Bánh su kem', 20000.00, 'cái', 50, 'available', 4),
('Bánh mì sandwich', 15000.00, 'cái', 80, 'available', 4),
('Khoai tây chiên', 30000.00, 'phần', 60, 'available', 5),
('Cà phê caramel macchiato', 55000.00, 'ly', 130, 'available', 6);

-- 9. shipper
INSERT INTO shipper (id, vehicle_plate_number, current_status) VALUES
(5, '51A-12345', 'available'),
(6, '51A-23456', 'busy'),
(7, '51A-34567', 'available'),
(8, '51A-45678', 'available'),
(9, '51A-56789', 'busy'),
(10, '51A-67890', 'available'),
(1, '51A-78901', 'available'),
(2, '51A-89012', 'offline'),
(3, '51A-90123', 'available'),
(4, '51A-01234', 'busy');

-- 10. timesheet
INSERT INTO timesheet (employee_id, hours_worked, work_date) VALUES
(1, 8.0, '2024-03-01'),
(2, 7.5, '2024-03-01'),
(3, 8.0, '2024-03-01'),
(4, 6.0, '2024-03-01'),
(5, 9.0, '2024-03-01'),
(6, 8.0, '2024-03-01'),
(7, 7.0, '2024-03-01'),
(8, 8.5, '2024-03-01'),
(9, 8.0, '2024-03-01'),
(10, 7.5, '2024-03-01');

-- 11. goods_receipt
INSERT INTO goods_receipt (received_date, supplier_id, total_quantity, total_price) VALUES
('2024-03-01 08:00:00', 1, 100, 5000000.00),
('2024-03-01 09:30:00', 2, 50, 2500000.00),
('2024-03-02 10:00:00', 3, 200, 6000000.00),
('2024-03-02 14:00:00', 4, 150, 4500000.00),
('2024-03-03 08:30:00', 5, 80, 3200000.00),
('2024-03-03 11:00:00', 6, 300, 9000000.00),
('2024-03-04 09:00:00', 7, 1000, 2000000.00),
('2024-03-04 13:30:00', 8, 500, 5000000.00),
('2024-03-05 10:30:00', 9, 200, 4000000.00),
('2024-03-05 15:00:00', 10, 100, 1500000.00);

-- 12. ingredient
INSERT INTO ingredient (ingredient_name, net_weight, quantity) VALUES
('Cà phê Arabica', 1000, 50),
('Sữa đặc có đường', 380, 100),
('Trà đen', 500, 80),
('Đào tươi', 1000, 30),
('Bơ', 500, 20),
('Cam tươi', 1000, 40),
('Bột mì', 1000, 25),
('Khoai tây', 1000, 15),
('Đường trắng', 1000, 200),
('Caramel syrup', 1000, 50);

-- 13. goods_receipt_detail
INSERT INTO goods_receipt_detail (receipt_id, ingredient_id, quantity, unit_price) VALUES
(1, 1, 50, 100000.00),
(1, 9, 50, 50000.00),
(2, 2, 100, 25000.00),
(3, 3, 200, 30000.00),
(4, 4, 150, 30000.00),
(5, 7, 80, 40000.00),
(6, 9, 300, 30000.00),
(7, 10, 1000, 2000.00),
(8, 8, 500, 10000.00),
(9, 5, 200, 20000.00);

-- 14. ingredient_product
INSERT INTO ingredient_product (product_id, ingredient_id, estimate, unit_price) VALUES
(1, 1, 20, 100000.00),
(1, 9, 10, 50000.00),
(2, 1, 20, 100000.00),
(2, 2, 15, 25000.00),
(2, 9, 10, 50000.00),
(3, 3, 30, 30000.00),
(3, 2, 20, 25000.00),
(3, 9, 15, 20000.00),
(4, 3, 25, 30000.00),
(4, 4, 20, 30000.00);

-- 15. order
INSERT INTO `order` (customer_id, order_date, sub_total, total_amount, status) VALUES
(1, '2024-03-01 08:30:00', 75000.00, 75000.00, 'completed'),
(2, '2024-03-01 09:15:00', 120000.00, 120000.00, 'completed'),
(3, '2024-03-01 10:00:00', 85000.00, 85000.00, 'completed'),
(4, '2024-03-01 11:30:00', 150000.00, 150000.00, 'processing'),
(5, '2024-03-01 12:45:00', 95000.00, 95000.00, 'completed'),
(6, '2024-03-01 14:00:00', 180000.00, 180000.00, 'completed'),
(7, '2024-03-01 15:30:00', 65000.00, 65000.00, 'processing'),
(8, '2024-03-01 16:15:00', 140000.00, 140000.00, 'completed'),
(9, '2024-03-01 17:45:00', 110000.00, 110000.00, 'completed'),
(10, '2024-03-01 18:30:00', 80000.00, 80000.00, 'cancelled');

-- 16. online_order
INSERT INTO online_order (id, customer_id, shipper_id, receiver_name, phone_number, address, shipping_fee, estimated_delivery_time, completed_time, status, total_amount) VALUES
(4, 4, 5, 'Phạm Văn Đức', '0904444444', '123 Nguyễn Văn Linh, Q.7, TP.HCM', 15000.00, '2024-03-01 12:30:00', NULL, 'delivering', 165000.00),
(7, 7, 6, 'Đặng Thị Quỳnh', '0907777777', '456 Lê Văn Sỹ, Q.3, TP.HCM', 10000.00, '2024-03-01 16:00:00', NULL, 'preparing', 75000.00),
(10, 10, 7, 'Ngô Văn Minh', '0900000000', '789 Lý Thường Kiệt, Q.10, TP.HCM', 20000.00, '2024-03-01 19:00:00', NULL, 'cancelled', 100000.00);

-- 17. order_detail
INSERT INTO order_detail (order_id, product_id, quantity, unit_price) VALUES
(1, 1, 2, 25000.00),
(1, 7, 1, 20000.00),
(2, 3, 2, 45000.00),
(2, 8, 2, 15000.00),
(3, 2, 1, 30000.00),
(3, 5, 1, 50000.00),
(4, 4, 3, 40000.00),
(4, 9, 1, 30000.00),
(5, 6, 2, 35000.00),
(5, 7, 1, 20000.00);

-- 18. invoice
INSERT INTO invoice (customer_id, employee_id, order_id, payment_method_id, issued_date, total_amount) VALUES
(1, 3, 1, 1, '2024-03-01 08:35:00', 75000.00),
(2, 3, 2, 2, '2024-03-01 09:20:00', 120000.00),
(3, 3, 3, 3, '2024-03-01 10:05:00', 85000.00),
(5, 4, 5, 4, '2024-03-01 12:50:00', 95000.00),
(6, 3, 6, 1, '2024-03-01 14:05:00', 180000.00),
(8, 4, 8, 5, '2024-03-01 16:20:00', 140000.00),
(9, 3, 9, 6, '2024-03-01 17:50:00', 110000.00);

-- 19. invoice_detail
INSERT INTO invoice_detail (invoice_id, product_id, quantity, unit_price) VALUES
(1, 1, 2, 25000.00),
(1, 7, 1, 20000.00),
(2, 3, 2, 45000.00),
(2, 8, 2, 15000.00),
(3, 2, 1, 30000.00),
(3, 5, 1, 50000.00),
(4, 6, 2, 35000.00),
(4, 7, 1, 20000.00),
(5, 10, 2, 55000.00),
(5, 9, 2, 30000.00);

-- 20. invoice_voucher_detail
INSERT INTO invoice_voucher_detail (invoice_id, voucher_id, discount_value) VALUES
(2, 1, 24000.00),
(3, 2, 30000.00),
(5, 3, 90000.00),
(6, 4, 21000.00),
(7, 5, 15000.00);