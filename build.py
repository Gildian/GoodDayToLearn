#!/usr/bin/env python3
"""
Build script for GoodDayToLearn standalone app
"""
import subprocess
import sys
import os
import platform

def build_app():
    """Build the standalone app using PyInstaller"""
    
    print("🚀 Building GoodDayToLearn standalone app...")
    print(f"Platform: {platform.system()}")
    
    # Check if required files exist
    if not os.path.exists("GoodDayToLearn.py"):
        print("❌ Error: GoodDayToLearn.py not found!")
        return False
    
    if not os.path.exists("rain.wav"):
        print("⚠️  Warning: rain.wav not found! Audio won't work.")
    
    if not os.path.exists("alarm.wav"):
        print("⚠️  Warning: alarm.wav not found! Alarm won't work.")
    
    # Determine the appropriate command based on platform
    system = platform.system()
    
    if system == "Darwin":  # macOS
        cmd = [
            "pyinstaller",
            "--onedir",
            "--windowed",
            "--name", "GoodDayToLearn",
            "--add-data", "rain.wav:.",
            "--add-data", "alarm.wav:.",
            "main.py"
        ]
        output_location = "dist/GoodDayToLearn.app"
    elif system == "Windows":
        cmd = [
            "pyinstaller",
            "--onefile",
            "--windowed",
            "--name", "GoodDayToLearn",
            "--add-data", "rain.wav;.",
            "--add-data", "alarm.wav;.",
            "main.py"
        ]
        output_location = "dist/GoodDayToLearn.exe"
    else:  # Linux
        cmd = [
            "pyinstaller",
            "--onefile",
            "--windowed",
            "--name", "GoodDayToLearn",
            "--add-data", "rain.wav:.",
            "--add-data", "alarm.wav:.",
            "main.py"
        ]
        output_location = "dist/GoodDayToLearn"
    
    try:
        # Run PyInstaller
        print("🔨 Running PyInstaller...")
        result = subprocess.run(cmd, check=True, capture_output=True, text=True)
        
        print("✅ Build completed successfully!")
        print(f"📦 Your app is located at: {output_location}")
        
        if system == "Darwin":
            print("\n🍎 macOS Instructions:")
            print("- Double-click the GoodDayToLearn.app to run")
            print("- If security warning appears, right-click and select 'Open'")
        elif system == "Windows":
            print("\n🪟 Windows Instructions:")
            print("- Double-click GoodDayToLearn.exe to run")
            print("- If Windows Defender blocks it, allow it to run")
        else:
            print("\n🐧 Linux Instructions:")
            print("- Make executable: chmod +x dist/GoodDayToLearn")
            print("- Run: ./dist/GoodDayToLearn")
        
        return True
        
    except subprocess.CalledProcessError as e:
        print(f"❌ Build failed: {e}")
        print("Error output:", e.stderr)
        return False
    except FileNotFoundError:
        print("❌ PyInstaller not found! Install it with: pip install pyinstaller")
        return False

if __name__ == "__main__":
    success = build_app()
    if not success:
        sys.exit(1)
