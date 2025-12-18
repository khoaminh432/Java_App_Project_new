# JavaFX Desktop Application

Ứng dụng desktop JavaFX được xây dựng với mục tiêu mô phỏng quy trình phát triển phần mềm thực tế: giao diện trực quan, kiến trúc MVC rõ ràng, backend kết nối MySQL thông qua JDBC và được chuẩn hóa bằng Maven.

---

## 🌟 Tính năng nổi bật
- Đăng nhập người dùng cơ bản với xử lý ở tầng `service`
- Tổ chức code theo MVC giúp tách biệt UI, logic và dữ liệu
- Sử dụng JavaFX CSS/FXML để dễ dàng thay đổi giao diện
- Tích hợp MySQL qua JDBC, có thể mở rộng CRUD nhanh chóng
- Build/run thống nhất bằng Maven, phù hợp cho CI/CD

---

## 🧰 Công nghệ chính
| Thành phần | Mô tả |
| --- | --- |
| Java 17+ | Ngôn ngữ chính, tận dụng các tính năng hiện đại (Records, Stream API, OOP) |
| JavaFX 20+ | Xây dựng giao diện desktop, hỗ trợ Scene Builder, CSS, FXML |
| Maven 3.9+ | Quản lý dependency và lifecycle `clean`, `test`, `javafx:run` |
| JDBC + MySQL | Thao tác cơ sở dữ liệu quan hệ, dễ triển khai trên XAMPP/Cloud |
| JUnit 5 | Viết và chạy unit test cho `service`/`dao` |

---

## 🏗️ Kiến trúc & Vai trò thư mục
| Tầng | Mô tả |
| --- | --- |
| `my_app.model` | Khai báo entity (POJO) phản ánh bảng dữ liệu |
| `my_app.dao` | Chứa lớp thao tác SQL/JDBC, quản lý truy vấn |
| `my_app.service` | Chứa nghiệp vụ, validate dữ liệu trước khi gọi DAO |
| `my_app.controller` | Lắng nghe sự kiện JavaFX, điều phối dữ liệu giữa View ↔ Service |
| `my_app.util` | Tiện ích chung (kết nối DB, helper) |

---

## 📁 Cấu trúc dự án
```
JAVA_App_Project_new
├── pom.xml
├── docs/
│   └── erd/
├── src/
│   ├── main/
│   │   ├── java/my_app/
│   │   │   ├── App.java
│   │   │   ├── controller/LoginController.java
│   │   │   ├── dao/UserDao.java
│   │   │   ├── model/User.java
│   │   │   ├── service/UserService.java
│   │   │   └── util/DBConnection.java
│   │   └── resources/
│   │       ├── fxml/homepage.fxml
│   │       ├── css/style.css
│   │       └── images/
│   └── test/java/my_app/AppTest.java
└── target/ (build output – không commit)
```

---

## ⚙️ Yêu cầu hệ thống
- JDK 17 trở lên (`java -version` để kiểm tra)
- Maven 3.9 trở lên (`mvn -v`)
- MySQL 8.x (hoặc tương thích) và driver `mysql-connector-j`
- IDE hỗ trợ JavaFX (IntelliJ, VS Code với JavaFX plugin, Scene Builder tùy chọn)

---

## 🚀 Khởi chạy nhanh
1. **Cài đặt dependency** (Maven tự tải khi build lần đầu):
   ```bash
   mvn clean install
   ```
2. **Chạy ứng dụng JavaFX**:
   ```bash
   mvn clean javafx:run
   ```
3. **Chạy unit test** (tùy chọn):
   ```bash
   mvn test
   ```

> Gợi ý: cấu hình `JAVA_HOME` và `PATH` để IDE/terminal nhận đúng JDK 17.

---

## 🗄️ Cấu hình cơ sở dữ liệu
| Tham số | Ý nghĩa |
| --- | --- |
| `DBConnection.URL` | Chuỗi JDBC, ví dụ `jdbc:mysql://localhost:3306/my_app_db` |
| `DBConnection.USER` | Tài khoản MySQL |
| `DBConnection.PASSWORD` | Mật khẩu tương ứng |

Các bước khởi tạo nhanh:
1. Tạo schema `my_app_db` (tùy đổi tên cho phù hợp).
2. Import file SQL (nếu có) trong thư mục `docs/` hoặc tự tạo bảng mẫu `users`.
3. Cập nhật thông tin kết nối trong `my_app.util.DBConnection`.

---

## 🔍 Quy trình phát triển gợi ý
1. Thiết kế giao diện bằng Scene Builder → xuất `*.fxml` vào `src/main/resources/fxml`.
2. Liên kết `fx:controller` với lớp trong `my_app.controller`.
3. Cài đặt logic trong `controller`, giao tiếp với `service`.
4. `service` kiểm tra dữ liệu, gọi `dao` để truy vấn MySQL.
5. Viết unit test cho `service`/`dao` trong `src/test/java`.

---

## 🧭 Lộ trình mở rộng
- Thêm phân quyền (admin/user) và ghi nhớ phiên đăng nhập
- Xây dựng bộ CRUD hoàn chỉnh cho bảng `users`
- Áp dụng CSS nâng cao, animation, Dark/Light theme
- Đóng gói thành file `.jar` hoặc native installer với `jlink`
- Tách cấu hình DB ra file `.properties` hoặc `.env`

---

## 👤 Thông tin sinh viên
- Họ tên: _Điền họ tên_  
- Môn học: _Lập trình Java / Phát triển ứng dụng_  
- Giảng viên hướng dẫn: _Tên GV_  

---

## 📎 Tài liệu tham khảo trong repo
- `docs/erd`: sơ đồ ERD, use case, class diagram phục vụ bảo vệ đồ án
- `README.md`: hướng dẫn setup nhanh và mô tả kiến trúc
- `LICENSE`: giấy phép sử dụng mã nguồn (nếu cần công bố)

> ✅ Dự án mang tính thực tiễn, sẵn sàng để trình bày trong đồ án hoặc phát triển thành sản phẩm hoàn chỉnh.