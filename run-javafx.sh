#!/bin/bash

# Script to run JavaFX application in a dev container with virtual display

echo "Setting up virtual display for JavaFX..."

# Check if Xvfb is installed
if ! command -v Xvfb &> /dev/null; then
    echo "Xvfb not found. Installing..."
    sudo apt-get update && sudo apt-get install -y xvfb
fi

# Set display
export DISPLAY=:99

# Check if Xvfb is already running
if ! pgrep -x "Xvfb" > /dev/null; then
    echo "Starting virtual display server..."
    Xvfb :99 -screen 0 1024x768x24 > /dev/null 2>&1 &
    sleep 2
else
    echo "Virtual display server already running"
fi

# Set JAVA_HOME to Java 21
export JAVA_HOME=/usr/local/sdkman/candidates/java/21.0.9-ms

echo "Running JavaFX application..."
mvn javafx:run
