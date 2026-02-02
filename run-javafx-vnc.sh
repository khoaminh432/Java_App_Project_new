#!/bin/bash

# Kiểm tra VNC server đang chạy
if ! pgrep Xtigervnc > /dev/null; then
    echo "❌ VNC server chưa chạy!"
    echo "Chạy ./start-vnc.sh trước"
    exit 1
fi

echo "🚀 Khởi động ứng dụng JavaFX..."
echo ""
echo "📍 Vị trí: /workspaces/Java_App_Project_new"
echo "☕ Java: Java 21 LTS"
echo "🖥️  Display: :1"
echo ""

# Set environment
export DISPLAY=:1
export JAVA_HOME=/usr/local/sdkman/candidates/java/21.0.9-ms

# Chạy ứng dụng
echo "⏳ Đang build và chạy JavaFX application..."
mvn javafx:run
