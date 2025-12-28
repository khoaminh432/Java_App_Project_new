#!/bin/bash

# Build script for Java Demo Application

echo "Building Java Demo Application..."

# Create output directory
mkdir -p target/classes

# Compile Java files
javac -d target/classes src/main/java/com/demo/app/*.java

if [ $? -eq 0 ]; then
    echo "Build successful!"
    echo "To run the application, use: ./run.sh"
else
    echo "Build failed!"
    exit 1
fi
