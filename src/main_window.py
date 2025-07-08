"""
Main application window and UI components.
"""

import tkinter as tk
from .config import AppConfig
from .timer import PomodoroTimer
from .audio_manager import AudioManager
from .settings_window import SettingsWindow

class GoodDayToLearnApp:
    """Main application class for the Good Day To Learn Pomodoro timer."""
    
    def __init__(self, root):
        """Initialize the main application."""
        self.root = root
        self._setup_window()
        
        # Initialize components
        self.audio_manager = AudioManager()
        self.timer = PomodoroTimer(
            on_timer_complete=self._on_timer_complete,
            on_timer_update=self._on_timer_update
        )
        
        # Create UI
        self._create_widgets()
        self._update_display()
    
    def _setup_window(self):
        """Configure the main window."""
        self.root.title(AppConfig.WINDOW_TITLE)
        self.root.geometry(f"{AppConfig.WINDOW_WIDTH}x{AppConfig.WINDOW_HEIGHT}")
        self.root.configure(bg=AppConfig.COLORS['background'])
        self.root.resizable(AppConfig.WINDOW_RESIZABLE, AppConfig.WINDOW_RESIZABLE)
    
    def _create_widgets(self):
        """Create all UI widgets."""
        # Title label
        title_label = tk.Label(
            self.root,
            text=AppConfig.WINDOW_TITLE,
            font=("Helvetica", 24, "bold"),
            bg=AppConfig.COLORS['background'],
            fg=AppConfig.COLORS['text_primary']
        )
        title_label.pack(pady=20)
        
        # Real-time clock (placeholder for future feature)
        self.clock_label = tk.Label(
            self.root,
            text="",
            font=("Helvetica", 14),
            bg=AppConfig.COLORS['background'],
            fg=AppConfig.COLORS['text_secondary']
        )
        self.clock_label.pack(pady=5)
        
        # Main timer display
        self.timer_label = tk.Label(
            self.root,
            text="25:00",
            font=("Helvetica", 48, "bold"),
            bg=AppConfig.COLORS['background'],
            fg=AppConfig.COLORS['work_time']
        )
        self.timer_label.pack(pady=30)
        
        # Status label
        self.status_label = tk.Label(
            self.root,
            text="Work Time - Interval 1/4",
            font=("Helvetica", 16),
            bg=AppConfig.COLORS['background'],
            fg=AppConfig.COLORS['text_muted']
        )
        self.status_label.pack(pady=10)
        
        # Control buttons
        self._create_control_buttons()
    
    def _create_control_buttons(self):
        """Create control buttons."""
        button_frame = tk.Frame(self.root, bg=AppConfig.COLORS['background'])
        button_frame.pack(pady=30)
        
        # Start button
        self.start_button = tk.Button(
            button_frame,
            text="Start",
            command=self._start_timer,
            font=("Helvetica", 12, "bold"),
            bg=AppConfig.COLORS['button_green'],
            fg="black",
            activebackground=AppConfig.COLORS['button_green_active'],
            relief="flat",
            padx=20,
            pady=10,
            cursor="hand2"
        )
        self.start_button.pack(side=tk.LEFT, padx=10)
        
        # Stop button
        self.stop_button = tk.Button(
            button_frame,
            text="Stop",
            command=self._stop_timer,
            font=("Helvetica", 12, "bold"),
            bg=AppConfig.COLORS['button_red'],
            fg="black",
            activebackground=AppConfig.COLORS['button_red_active'],
            relief="flat",
            padx=20,
            pady=10,
            cursor="hand2"
        )
        self.stop_button.pack(side=tk.LEFT, padx=10)
        
        # Reset button
        self.reset_button = tk.Button(
            button_frame,
            text="Reset",
            command=self._reset_timer,
            font=("Helvetica", 12, "bold"),
            bg=AppConfig.COLORS['button_blue'],
            fg="black",
            activebackground=AppConfig.COLORS['button_blue_active'],
            relief="flat",
            padx=20,
            pady=10,
            cursor="hand2"
        )
        self.reset_button.pack(side=tk.LEFT, padx=10)
        
        # Settings button
        self.settings_button = tk.Button(
            button_frame,
            text="Settings",
            command=self._open_settings,
            font=("Helvetica", 12, "bold"),
            bg=AppConfig.COLORS['button_purple'],
            fg="black",
            activebackground=AppConfig.COLORS['button_purple_active'],
            relief="flat",
            padx=20,
            pady=10,
            cursor="hand2"
        )
        self.settings_button.pack(side=tk.LEFT, padx=10)
    
    def _start_timer(self):
        """Start the timer."""
        if not self.timer.running:
            self.timer.start()
            # Only play music during work sessions, not breaks
            if not self.timer.is_break:
                self.audio_manager.start_music()
    
    def _stop_timer(self):
        """Stop the timer."""
        self.timer.stop()
        self.audio_manager.stop_music()
        self._update_display()
    
    def _reset_timer(self):
        """Reset the timer."""
        self.timer.reset()
        self.audio_manager.stop_music()
        self._update_display()
    
    def _open_settings(self):
        """Open the settings window."""
        SettingsWindow(
            self.root,
            self.timer,
            self.audio_manager,
            on_settings_saved=self._on_settings_saved
        )
    
    def _on_timer_complete(self):
        """Handle timer completion."""
        self.audio_manager.stop_music()
        self.audio_manager.play_alarm()
        
        # Update display with phase message
        self.timer_label.config(text=self.timer.get_phase_message())
        self._update_display()
    
    def _on_timer_update(self, time_left):
        """Handle timer updates during countdown."""
        self.root.after(0, self._update_timer_display)
    
    def _on_settings_saved(self):
        """Handle settings being saved."""
        # Reset timer if not running
        if not self.timer.running:
            self._reset_timer()
    
    def _update_display(self):
        """Update the display with current timer state."""
        self._update_timer_display()
        self._update_status_display()
    
    def _update_timer_display(self):
        """Update the timer display."""
        self.timer_label.config(text=self.timer.get_time_display())
        
        # Update color based on timer state
        if self.timer.is_break:
            self.timer_label.config(fg=AppConfig.COLORS['break_time'])
        else:
            self.timer_label.config(fg=AppConfig.COLORS['work_time'])
    
    def _update_status_display(self):
        """Update the status display."""
        status_text = self.timer.get_status_text()
        self.status_label.config(text=status_text)
        
        # Update status color
        if self.timer.is_break:
            self.status_label.config(fg=AppConfig.COLORS['break_time'])
        else:
            self.status_label.config(fg=AppConfig.COLORS['text_muted'])
