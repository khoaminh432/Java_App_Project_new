# 🖥️ Hướng Dẫn Hiển Thị Desktop trên Codespaces

## 📋 Các Bước Thực Hiện

### Bước 1: Cài đặt VNC Server (Chỉ cần làm 1 lần)

```bash
./setup-vnc.sh
```

Script này sẽ:
- ✅ Cài đặt TigerVNC Server
- ✅ Cài đặt noVNC (VNC qua trình duyệt web)
- ✅ Cài đặt Fluxbox (window manager nhẹ)
- ✅ Thiết lập mật khẩu VNC mặc định: **codespace**

### Bước 2: Khởi động VNC Server

```bash
./start-vnc.sh
```

### Bước 3: Mở Desktop trong Browser

1. **Tự động**: VS Code sẽ hiển thị thông báo "Your application running on port 6080 is available"
   - Click **"Open in Browser"**

2. **Thủ công**: 
   - Mở tab **PORTS** (bên cạnh tab Terminal)
   - Tìm port **6080**
   - Click vào biểu tượng **🌐** (Open in Browser)
   - Hoặc click chuột phải → **"Open in Browser"**

3. Trong trang web noVNC:
   - Click **"Connect"**
   - Nhập mật khẩu: **codespace**
   - Click **"Send Credentials"**

### Bước 4: Chạy Ứng Dụng JavaFX

Sau khi đã vào được desktop, mở Terminal và chạy:

```bash
./run-javafx-vnc.sh
```

Hoặc thủ công:
```bash
export DISPLAY=:1
export JAVA_HOME=/usr/local/sdkman/candidates/java/21.0.9-ms
mvn javafx:run
```

## 🎯 Cách Sử Dụng Nhanh

```bash
# Lần đầu tiên
./setup-vnc.sh      # Cài đặt (1 lần duy nhất)
./start-vnc.sh      # Khởi động VNC

# Sau đó mở browser theo hướng dẫn
# Rồi chạy app:
./run-javafx-vnc.sh
```

## 🛑 Dừng VNC Server

```bash
./stop-vnc.sh
```

## 🔧 Troubleshooting

### Port 6080 không xuất hiện
```bash
# Kiểm tra xem noVNC có đang chạy không
ps aux | grep websockify

# Restart VNC
./stop-vnc.sh
./start-vnc.sh
```

### Không kết nối được VNC
```bash
# Kiểm tra VNC server
ps aux | grep Xvnc

# Nếu không có, restart:
./start-vnc.sh
```

### Muốn đổi mật khẩu VNC
```bash
vncpasswd
# Nhập mật khẩu mới 2 lần
# Sau đó restart VNC
./stop-vnc.sh
./start-vnc.sh
```

## 📱 Các Phím Tắt trong Desktop

- **Click chuột phải** trên desktop → Menu
- **Alt + Tab**: Chuyển cửa sổ
- Mở Terminal trong VNC: Click chuột phải → Terminal

## ⚙️ Thông Tin Kỹ Thuật

- VNC Display: `:1` (port 5901)
- noVNC Web: Port `6080`
- Window Manager: Fluxbox
- Độ phân giải: 1280x720 (có thể thay đổi trong start-vnc.sh)

## 🎨 Tùy Chỉnh Độ Phân Giải

Sửa file `start-vnc.sh`, dòng:
```bash
vncserver :1 -geometry 1280x720 -depth 24
```

Đổi thành (ví dụ):
```bash
vncserver :1 -geometry 1920x1080 -depth 24
```

## 🚀 Lợi Ích

- ✅ Xem được giao diện JavaFX trực tiếp trên browser
- ✅ Không cần cài đặt gì trên máy local
- ✅ Hoạt động hoàn toàn trên cloud
- ✅ Có thể debug UI trực quan
