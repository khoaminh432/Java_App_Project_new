#!/bin/bash

echo "=== Khởi động VNC Server ==="

# Kiểm tra xem VNC đã được cài đặt chưa
if ! command -v vncserver &> /dev/null; then
    echo "VNC chưa được cài đặt. Chạy ./setup-vnc.sh trước"
    exit 1
fi

# Tạo file .Xresources nếu chưa có
touch ~/.Xresources

# Tạo hoặc cập nhật xstartup script
mkdir -p ~/.vnc
cat > ~/.vnc/xstartup << 'XSTARTUP_EOF'
#!/bin/sh
[ -r $HOME/.Xresources ] && xrdb $HOME/.Xresources
xterm -geometry 80x24+10+10 -ls &
fluxbox
XSTARTUP_EOF
chmod +x ~/.vnc/xstartup

# Dừng VNC server cũ nếu có
vncserver -kill :1 2>/dev/null || true
pkill -f websockify 2>/dev/null || true

# Chờ một chút
sleep 1

# Khởi động VNC server
echo "Đang khởi động VNC server trên display :1..."
vncserver :1 -geometry 1280x720 -depth 24

# Chờ VNC server khởi động
sleep 2

# Kiểm tra VNC server có chạy không
if ! pgrep -x Xtigervnc > /dev/null; then
    echo "❌ Lỗi: VNC server không khởi động được!"
    echo "Chi tiết lỗi:"
    cat ~/.vnc/*.log 2>/dev/null | tail -20
    exit 1
fi

# Khởi động noVNC (web-based VNC viewer)
echo "Đang khởi động noVNC trên port 6080..."
websockify -D --web=/usr/share/novnc/ 6080 localhost:5901

# Chờ noVNC khởi động
sleep 1

# Kiểm tra noVNC có chạy không
if ! pgrep -f websockify > /dev/null; then
    echo "❌ Lỗi: noVNC không khởi động được!"
    exit 1
fi

echo ""
echo "✅ VNC Server đã khởi động!"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📺 Cách truy cập giao diện desktop:"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "1. VS Code sẽ tự động phát hiện port 6080"
echo "2. Click vào tab 'PORTS' (cạnh tab Terminal)"
echo "3. Tìm port 6080, click vào biểu tượng 🌐 để mở"
echo "4. Hoặc click chuột phải → 'Open in Browser'"
echo ""
echo "🔑 Mật khẩu VNC: codespace"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "Để chạy ứng dụng JavaFX:"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "export DISPLAY=:1"
echo "export JAVA_HOME=/usr/local/sdkman/candidates/java/21.0.9-ms"
echo "mvn javafx:run"
echo ""
echo "Hoặc sử dụng: ./run-javafx-vnc.sh"
echo ""
