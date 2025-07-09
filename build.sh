#!/bin/bash

# Good Day To Learn - Java Build Script
# This script builds and packages the Java Pomodoro Timer application

echo "Building Good Day To Learn Java Application..."

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed. Please install Maven first."
    exit 1
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed. Please install Java 11 or higher."
    exit 1
fi

# Clean previous builds
echo "Cleaning previous builds..."
mvn clean

# Compile the project
echo "Compiling the project..."
mvn compile
if [ $? -ne 0 ]; then
    echo "Error: Compilation failed!"
    exit 1
fi

# Run tests (if any)
echo "Running tests..."
mvn test

# Package the application
echo "Packaging the application..."
mvn package
if [ $? -ne 0 ]; then
    echo "Error: Packaging failed!"
    exit 1
fi

echo "Build completed successfully!"
echo "Executable JAR created: target/GoodDayToLearn.jar"
echo ""
echo "To run the application:"
echo "  java -jar target/GoodDayToLearn.jar"
echo ""
echo "Or use Maven:"
echo "  mvn exec:java"
