#!/bin/bash

# Good Day To Learn - Standalone JAR Runner
# This script runs the standalone JAR version of the Pomodoro Timer

echo "Starting Good Day To Learn Pomodoro Timer..."

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed. Please install Java 11 or higher."
    exit 1
fi

# Check if JAR file exists
JAR_FILE="target/GoodDayToLearn.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "Error: JAR file not found at $JAR_FILE"
    echo "Please run 'mvn clean package' first to build the application."
    exit 1
fi

# Run the application
echo "Running: java -jar $JAR_FILE"
java -jar "$JAR_FILE"
