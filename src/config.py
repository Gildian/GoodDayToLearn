"""
Configuration settings for the Good Day To Learn application.
"""

class AppConfig:
    """Application configuration settings."""
    
    # Window settings
    WINDOW_TITLE = "Good Day To Learn"
    WINDOW_WIDTH = 700
    WINDOW_HEIGHT = 450
    WINDOW_RESIZABLE = False
    
    # Color scheme
    COLORS = {
        'background': '#2c3e50',       # Dark blue-gray
        'text_primary': '#ecf0f1',     # Light gray
        'text_secondary': '#bdc3c7',   # Medium gray
        'text_muted': '#95a5a6',       # Muted gray
        'work_time': '#e74c3c',        # Red
        'break_time': '#f39c12',       # Orange
        'button_green': '#27ae60',     # Green
        'button_green_active': '#2ecc71',
        'button_red': '#e74c3c',       # Red
        'button_red_active': '#c0392b',
        'button_blue': '#3498db',      # Blue
        'button_blue_active': '#2980b9',
        'button_purple': '#9b59b6',    # Purple
        'button_purple_active': '#8e44ad',
        'settings_bg': '#34495e',      # Darker gray
        'slider_active_blue': '#3498db',
        'slider_active_red': '#e74c3c'
    }
    
    # Timer defaults (in seconds)
    DEFAULT_WORK_TIME = 25 * 60      # 25 minutes
    DEFAULT_SHORT_BREAK = 5 * 60     # 5 minutes
    DEFAULT_LONG_BREAK = 20 * 60     # 20 minutes
    
    # Volume defaults (0.0 to 1.0)
    DEFAULT_MUSIC_VOLUME = 0.5
    DEFAULT_ALARM_VOLUME = 0.7
    
    # Audio files
    MUSIC_FILE = "rain.wav"
    ALARM_FILE = "alarm.wav"
    
    # Timer validation limits
    MIN_WORK_TIME = 1      # minutes
    MAX_WORK_TIME = 120    # minutes
    MIN_SHORT_BREAK = 1    # minutes
    MAX_SHORT_BREAK = 60   # minutes
    MIN_LONG_BREAK = 1     # minutes
    MAX_LONG_BREAK = 120   # minutes
    
    # UI Settings
    SETTINGS_WINDOW_WIDTH = 600
    SETTINGS_WINDOW_HEIGHT = 750
