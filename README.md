# JavaFX Desktop Application (Maven)

## 📌 Giới thiệu

Đây là đồ án **ứng dụng desktop Java** được xây dựng theo hướng **thực tế**, sử dụng **JavaFX** để làm giao diện, **Maven** để quản lý dự án và thư viện, kết hợp **JDBC + MySQL** cho tầng dữ liệu. Ứng dụng được tổ chức theo mô hình **MVC** nhằm đảm bảo dễ mở rộng, dễ bảo trì và phù hợp với các đồ án học phần cũng như ứng dụng thực tế.

---

## 🛠️ Công nghệ sử dụng

### 1️⃣ Java (JDK 17+)

* Ngôn ngữ lập trình chính của dự án
* Sử dụng các tính năng hiện đại của Java (Lambda, Stream, OOP)
* Đảm bảo hiệu năng và tính ổn định lâu dài

---

### 2️⃣ JavaFX

* Framework xây dựng giao diện desktop
* Cung cấp các thành phần UI như:

  * Stage, Scene
  * Button, TextField, TextArea
  * VBox, HBox, BorderPane
* Hỗ trợ CSS và FXML (nếu mở rộng)

---

### 3️⃣ Maven

* Công cụ quản lý dự án và dependency
* Giúp:

  * Quản lý thư viện JavaFX
  * Build, clean, run project dễ dàng
  * Chuẩn hóa cấu trúc dự án

Lệnh chạy ứng dụng:

```bash
mvn clean javafx:run
```

---

### 4️⃣ JDBC (Java Database Connectivity)

* Cầu nối giữa Java và cơ sở dữ liệu
* Thực hiện các thao tác:

  * Kết nối database
  * CRUD (Create, Read, Update, Delete)
  * Thực thi SQL

---

### 5️⃣ MySQL

* Hệ quản trị cơ sở dữ liệu quan hệ
* Lưu trữ dữ liệu của ứng dụng
* Kết nối thông qua JDBC Driver

---

### 6️⃣ Mô hình MVC (Model – View – Controller)

* **Model**: xử lý dữ liệu, JDBC, entity
* **View**: giao diện JavaFX
* **Controller**: xử lý logic, nhận sự kiện từ View

Ưu điểm:

* Tách biệt rõ ràng các tầng
* Dễ bảo trì và mở rộng
* Phù hợp với đồ án và dự án thực tế

---

## 📂 Cấu trúc thư mục (tham khảo)

```
src/main/java/
├── com.myapp
│   ├── app        # Main JavaFX
│   ├── controller # Controller (MVC)
│   ├── model      # Model, Entity
│   ├── dao        # JDBC, Database Access
│   └── util       # Helper, DB connection

src/main/resources/
├── fxml           # Giao diện (nếu dùng FXML)
├── css            # CSS JavaFX
└── application.properties
```

---

## 🎯 Mục tiêu đồ án

* Áp dụng kiến thức Java vào dự án thực tế
* Hiểu và sử dụng Maven
* Xây dựng giao diện desktop bằng JavaFX
* Kết nối và thao tác với cơ sở dữ liệu MySQL
* Rèn luyện tư duy tổ chức code theo MVC

---

## 🚀 Hướng phát triển

* Hoàn thiện chức năng CRUD
* Áp dụng FXML + CSS
* Phân quyền người dùng
* Đóng gói ứng dụng (.exe / .jar)

---

## 👤 Sinh viên thực hiện

* Họ tên: ……………………
* Môn học: Lập trình Java / Lập trình ứng dụng
* Giảng viên hướng dẫn: ……………………

---

> ✅ Đồ án sử dụng các công nghệ phổ biến trong thực tế, phù hợp cho học tập và phát triển lâu dài.
