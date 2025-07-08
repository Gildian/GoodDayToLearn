# Good Day To Learn - Pomodoro Timer

A modern, organized Pomodoro timer application built with Python and Tkinter.

## Project Structure

```
GoodDayToLearn/
├── main.py                    # Application entry point
├── src/                       # Source code directory
│   ├── __init__.py           # Package initialization
│   ├── config.py             # Configuration settings
│   ├── audio_manager.py      # Audio handling (music & alarms)
│   ├── timer.py              # Pomodoro timer logic
│   ├── settings_window.py    # Settings UI window
│   └── main_window.py        # Main application window
├── rain.mp3                  # Background music file
├── alarm.wav                 # Alarm sound file
├── GoodDayToLearn.spec      # PyInstaller specification
└── requirements.txt          # Python dependencies
```

## Features

- **Pomodoro Timer**: 25-minute work sessions with 5-minute short breaks and 20-minute long breaks
- **Audio Support**: Background music during work sessions and alarm notifications
- **Volume Controls**: Separate volume controls for music and alarm sounds
- **Customizable Settings**: Adjust timer durations and audio levels
- **Clean UI**: Modern, dark-themed interface
- **Modular Design**: Well-organized code structure for easy maintenance

## Running the Application

### From Source
```bash
python main.py
```

### Building Executable
```bash
pyinstaller GoodDayToLearn.spec
```

## Code Organization

### `config.py`
Contains all application configuration settings including:
- Window dimensions and styling
- Color scheme
- Default timer values
- Audio file paths
- Validation limits

### `audio_manager.py` 
Handles all audio functionality:
- Background music playback
- Alarm sound effects
- Volume control
- Error handling for audio issues

### `timer.py`
Core Pomodoro timer logic:
- Timer state management
- Work/break cycle tracking
- Time formatting
- Callback system for UI updates

### `settings_window.py`
Settings configuration interface:
- Timer duration settings
- Volume controls
- Input validation
- Settings persistence

### `main_window.py`
Main application interface:
- UI layout and styling
- Button controls
- Display updates
- Component coordination

## Benefits of This Structure

1. **Separation of Concerns**: Each file has a specific responsibility
2. **Easy Maintenance**: Changes to one feature don't affect others
3. **Reusability**: Components can be easily reused or tested independently
4. **Scalability**: New features can be added without restructuring existing code
5. **Configuration Management**: All settings centralized in one place
