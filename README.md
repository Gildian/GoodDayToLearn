# Good Day To Learn - Pomodoro Timer

A desktop Pomodoro Timer application built with Java Swing that helps you manage your work sessions and breaks using the Pomodoro Technique.

## Features

- **Pomodoro Timer**: 25-minute work sessions followed by short breaks
- **Break Management**: Automatic short breaks (5 minutes) and long breaks (20 minutes) after every 4 work sessions
- **Background Music**: Relaxing rain sounds during work sessions
- **Audio Alerts**: Alarm sound when work/break sessions complete
- **Customizable Settings**: Adjust work time, break durations, and volume levels
- **Modern UI**: Clean, dark-themed interface with intuitive controls

## Requirements

- Java 11 or higher
- Maven 3.6 or higher (for building from source)

## Quick Start (Standalone)

The application can be run as a standalone JAR file:

1. **Download or build the JAR:**
   ```bash
   mvn clean package
   ```

2. **Run the standalone application:**
   ```bash
   java -jar target/GoodDayToLearn.jar
   ```
   
   Or use the convenience script:
   ```bash
   ./run-standalone.sh
   ```

The standalone JAR includes all dependencies and audio files, making it completely portable.

## Building and Running

### Using Maven

1. **Compile the project:**
   ```bash
   mvn compile
   ```

2. **Run the application:**
   ```bash
   mvn exec:java
   ```

3. **Build executable JAR:**
   ```bash
   mvn package
   ```

4. **Run the executable JAR:**
   ```bash
   java -jar target/GoodDayToLearn.jar
   ```

### Development

The project structure follows Maven conventions:
```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── gooddaytolearn/
│   │           ├── Main.java
│   │           ├── GoodDayToLearnApp.java
│   │           ├── PomodoroTimer.java
│   │           ├── AudioManager.java
│   │           ├── SettingsWindow.java
│   │           └── AppConfig.java
│   └── resources/
│       ├── rain.wav
│       └── alarm.wav
└── pom.xml
```

## How to Use

1. **Start Timer**: Click the "Start" button to begin a 25-minute work session
2. **Background Music**: Rain sounds will play automatically during work sessions
3. **Break Time**: After completing a work session, take a 5-minute break
4. **Long Break**: After 4 work sessions, enjoy a 20-minute long break
5. **Settings**: Customize timer durations and audio volumes through the Settings window
6. **Controls**: Use Stop to pause, Reset to restart, or Settings to configure

## Audio Files

The application includes two audio files:
- `rain.wav`: Background music for work sessions
- `alarm.wav`: Alert sound for session completion

## Configuration

All settings can be modified through the Settings window or by editing the `AppConfig.java` file:
- Work session duration (1-120 minutes)
- Short break duration (1-30 minutes)
- Long break duration (1-60 minutes)
- Music and alarm volume levels (0-100%)

## License

This project is open source and available under the MIT License.
