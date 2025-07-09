#!/bin/bash

# Run script for Good Day To Learn Java Application

echo "Starting Good Day To Learn Pomodoro Timer..."

# Check if classes exist
if [ ! -d "target/classes" ]; then
    echo "Classes not found. Running compilation first..."
    ./compile.sh
fi

# Run the application
java -cp target/classes com.gooddaytolearn.Main
