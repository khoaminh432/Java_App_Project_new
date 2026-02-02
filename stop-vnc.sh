#!/bin/bash

echo "Đang dừng VNC server..."

# Dừng VNC server
vncserver -kill :1 2>/dev/null

# Dừng noVNC
pkill -f websockify 2>/dev/null

echo "✅ Đã dừng VNC server"
