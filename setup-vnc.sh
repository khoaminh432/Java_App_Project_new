#!/bin/bash

echo "=== Cài đặt VNC Server cho Codespaces ==="

# Cài đặt các gói cần thiết
echo "Đang cài đặt các gói..."
sudo apt-get update
sudo apt-get install -y \
    tigervnc-standalone-server \
    tigervnc-common \
    fluxbox \
    novnc \
    websockify \
    x11-xserver-utils \
    dbus-x11

# Tạo thư mục VNC config
mkdir -p ~/.vnc

# Tạo mật khẩu VNC (mặc định: "codespace")
echo "Thiết lập mật khẩu VNC..."
echo "codespace" | vncpasswd -f > ~/.vnc/passwd
chmod 600 ~/.vnc/passwd

# Tạo file xstartup
cat > ~/.vnc/xstartup << 'EOF'
#!/bin/sh
unset SESSION_MANAGER
unset DBUS_SESSION_BUS_ADDRESS
exec fluxbox &
EOF

chmod +x ~/.vnc/xstartup

echo "✅ Cài đặt hoàn tất!"
echo ""
echo "Để chạy VNC server, sử dụng: ./start-vnc.sh"
