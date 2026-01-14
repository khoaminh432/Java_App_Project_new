#!/bin/bash

# Run script for Java Demo Application

echo "Running Java Demo Application..."
echo ""

# Check if compiled classes exist
if [ ! -d "target/classes" ]; then
    echo "Application not built yet. Running build.sh first..."
    ./build.sh
fi

# Run the application
java -cp target/classes com.demo.app.Main
