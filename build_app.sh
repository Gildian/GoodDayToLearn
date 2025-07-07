#!/bin/bash

# Create standalone app for GoodDayToLearn
echo "Creating standalone GoodDayToLearn app..."

# Activate virtual environment
source .venv/bin/activate

# Create the app using PyInstaller
pyinstaller --onedir --windowed --name "GoodDayToLearn" --add-data "rain.mp3:." GoodDayToLearn.py

echo "App created successfully!"
echo "You can find your app at: dist/GoodDayToLearn.app"
echo "Double-click the app to run it!"
