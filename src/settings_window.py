"""
Settings window for configuring timer and audio settings.
"""

import tkinter as tk
from .config import AppConfig

class SettingsWindow:
    """Settings window for timer and audio configuration."""
    
    def __init__(self, parent, timer, audio_manager, on_settings_saved=None):
        """
        Initialize the settings window.
        
        Args:
            parent: Parent window
            timer: PomodoroTimer instance
            audio_manager: AudioManager instance
            on_settings_saved: Callback function called when settings are saved
        """
        self.timer = timer
        self.audio_manager = audio_manager
        self.on_settings_saved = on_settings_saved
        
        # Create settings window
        self.window = tk.Toplevel(parent)
        self.window.title("Settings")
        self.window.geometry(f"{AppConfig.SETTINGS_WINDOW_WIDTH}x{AppConfig.SETTINGS_WINDOW_HEIGHT}")
        self.window.configure(bg=AppConfig.COLORS['settings_bg'])
        self.window.resizable(False, False)
        
        # Center the window
        self.window.transient(parent)
        self.window.grab_set()
        
        self._create_widgets()
    
    def _create_widgets(self):
        """Create all widgets for the settings window."""
        # Title
        title_label = tk.Label(
            self.window, 
            text="Timer Settings",
            font=("Helvetica", 18, "bold"),
            bg=AppConfig.COLORS['settings_bg'],
            fg=AppConfig.COLORS['text_primary']
        )
        title_label.pack(pady=20)
        
        # Settings frame
        settings_frame = tk.Frame(self.window, bg=AppConfig.COLORS['settings_bg'])
        settings_frame.pack(pady=10)
        
        # Timer settings
        self._create_timer_settings(settings_frame)
        
        # Volume settings
        self._create_volume_settings(settings_frame)
        
        # Buttons
        self._create_buttons()
    
    def _create_timer_settings(self, parent):
        """Create timer configuration widgets."""
        # Work time setting
        work_frame = tk.Frame(parent, bg=AppConfig.COLORS['settings_bg'])
        work_frame.pack(pady=10)
        tk.Label(
            work_frame,
            text="Work Time (minutes):",
            font=("Helvetica", 12),
            bg=AppConfig.COLORS['settings_bg'],
            fg=AppConfig.COLORS['text_primary']
        ).pack()
        
        self.work_time_var = tk.StringVar(value=str(self.timer.get_work_time_minutes()))
        self.work_time_entry = tk.Entry(
            work_frame,
            textvariable=self.work_time_var,
            font=("Helvetica", 12),
            width=10,
            justify="center",
            relief="flat",
            bg=AppConfig.COLORS['text_primary'],
            fg=AppConfig.COLORS['background']
        )
        self.work_time_entry.pack(pady=5)
        
        # Short break time setting
        short_frame = tk.Frame(parent, bg=AppConfig.COLORS['settings_bg'])
        short_frame.pack(pady=10)
        tk.Label(
            short_frame,
            text="Short Break Time (minutes):",
            font=("Helvetica", 12),
            bg=AppConfig.COLORS['settings_bg'],
            fg=AppConfig.COLORS['text_primary']
        ).pack()
        
        self.short_break_var = tk.StringVar(value=str(self.timer.get_short_break_minutes()))
        self.short_break_entry = tk.Entry(
            short_frame,
            textvariable=self.short_break_var,
            font=("Helvetica", 12),
            width=10,
            justify="center",
            relief="flat",
            bg=AppConfig.COLORS['text_primary'],
            fg=AppConfig.COLORS['background']
        )
        self.short_break_entry.pack(pady=5)
        
        # Long break time setting
        long_frame = tk.Frame(parent, bg=AppConfig.COLORS['settings_bg'])
        long_frame.pack(pady=10)
        tk.Label(
            long_frame,
            text="Long Break Time (minutes):",
            font=("Helvetica", 12),
            bg=AppConfig.COLORS['settings_bg'],
            fg=AppConfig.COLORS['text_primary']
        ).pack()
        
        self.long_break_var = tk.StringVar(value=str(self.timer.get_long_break_minutes()))
        self.long_break_entry = tk.Entry(
            long_frame,
            textvariable=self.long_break_var,
            font=("Helvetica", 12),
            width=10,
            justify="center",
            relief="flat",
            bg=AppConfig.COLORS['text_primary'],
            fg=AppConfig.COLORS['background']
        )
        self.long_break_entry.pack(pady=5)
    
    def _create_volume_settings(self, parent):
        """Create volume configuration widgets."""
        volume_frame = tk.Frame(parent, bg=AppConfig.COLORS['settings_bg'])
        volume_frame.pack(pady=15)
        
        # Music volume setting
        music_vol_frame = tk.Frame(volume_frame, bg=AppConfig.COLORS['settings_bg'])
        music_vol_frame.pack(pady=5)
        tk.Label(
            music_vol_frame,
            text="Music Volume:",
            font=("Helvetica", 12),
            bg=AppConfig.COLORS['settings_bg'],
            fg=AppConfig.COLORS['text_primary']
        ).pack()
        
        self.music_volume_scale = tk.Scale(
            music_vol_frame,
            from_=0,
            to=100,
            orient=tk.HORIZONTAL,
            bg=AppConfig.COLORS['background'],
            fg=AppConfig.COLORS['text_primary'],
            highlightthickness=0,
            troughcolor=AppConfig.COLORS['settings_bg'],
            activebackground=AppConfig.COLORS['slider_active_blue'],
            length=200
        )
        self.music_volume_scale.set(int(self.audio_manager.get_music_volume() * 100))
        self.music_volume_scale.pack(pady=5)
        
        # Alarm volume setting
        alarm_vol_frame = tk.Frame(volume_frame, bg=AppConfig.COLORS['settings_bg'])
        alarm_vol_frame.pack(pady=5)
        tk.Label(
            alarm_vol_frame,
            text="Alarm Volume:",
            font=("Helvetica", 12),
            bg=AppConfig.COLORS['settings_bg'],
            fg=AppConfig.COLORS['text_primary']
        ).pack()
        
        self.alarm_volume_scale = tk.Scale(
            alarm_vol_frame,
            from_=0,
            to=100,
            orient=tk.HORIZONTAL,
            bg=AppConfig.COLORS['background'],
            fg=AppConfig.COLORS['text_primary'],
            highlightthickness=0,
            troughcolor=AppConfig.COLORS['settings_bg'],
            activebackground=AppConfig.COLORS['slider_active_red'],
            length=200
        )
        self.alarm_volume_scale.set(int(self.audio_manager.get_alarm_volume() * 100))
        self.alarm_volume_scale.pack(pady=5)
    
    def _create_buttons(self):
        """Create save and cancel buttons."""
        button_frame = tk.Frame(self.window, bg=AppConfig.COLORS['settings_bg'])
        button_frame.pack(pady=30)
        
        save_button = tk.Button(
            button_frame,
            text="Save",
            command=self._save_settings,
            font=("Helvetica", 12, "bold"),
            bg=AppConfig.COLORS['button_green'],
            fg="black",
            activebackground=AppConfig.COLORS['button_green_active'],
            relief="flat",
            padx=20,
            pady=8,
            cursor="hand2"
        )
        save_button.pack(side=tk.LEFT, padx=15)
        
        cancel_button = tk.Button(
            button_frame,
            text="Cancel",
            command=self._cancel_settings,
            font=("Helvetica", 12, "bold"),
            bg=AppConfig.COLORS['button_red'],
            fg="black",
            activebackground=AppConfig.COLORS['button_red_active'],
            relief="flat",
            padx=20,
            pady=8,
            cursor="hand2"
        )
        cancel_button.pack(side=tk.LEFT, padx=15)
    
    def _save_settings(self):
        """Save all settings and close the window."""
        try:
            # Validate and convert input
            work_mins = int(self.work_time_var.get())
            short_mins = int(self.short_break_var.get())
            long_mins = int(self.long_break_var.get())
            
            # Check for reasonable values
            if work_mins < AppConfig.MIN_WORK_TIME or work_mins > AppConfig.MAX_WORK_TIME:
                raise ValueError(f"Work time must be between {AppConfig.MIN_WORK_TIME} and {AppConfig.MAX_WORK_TIME} minutes")
            if short_mins < AppConfig.MIN_SHORT_BREAK or short_mins > AppConfig.MAX_SHORT_BREAK:
                raise ValueError(f"Short break must be between {AppConfig.MIN_SHORT_BREAK} and {AppConfig.MAX_SHORT_BREAK} minutes")
            if long_mins < AppConfig.MIN_LONG_BREAK or long_mins > AppConfig.MAX_LONG_BREAK:
                raise ValueError(f"Long break must be between {AppConfig.MIN_LONG_BREAK} and {AppConfig.MAX_LONG_BREAK} minutes")
            
            # Update timer settings
            self.timer.set_work_time(work_mins)
            self.timer.set_short_break_time(short_mins)
            self.timer.set_long_break_time(long_mins)
            
            # Update volume settings
            music_volume = self.music_volume_scale.get() / 100.0
            alarm_volume = self.alarm_volume_scale.get() / 100.0
            self.audio_manager.set_music_volume(music_volume)
            self.audio_manager.set_alarm_volume(alarm_volume)
            
            # Call callback if provided
            if self.on_settings_saved:
                self.on_settings_saved()
            
            self.window.destroy()
            
        except ValueError as e:
            self._show_error(str(e))
    
    def _cancel_settings(self):
        """Cancel settings changes and close the window."""
        self.window.destroy()
    
    def _show_error(self, message):
        """Show an error message."""
        # Clear any existing error labels
        for widget in self.window.winfo_children():
            if isinstance(widget, tk.Label) and widget.cget("fg") == AppConfig.COLORS['button_red']:
                widget.destroy()
        
        error_label = tk.Label(
            self.window,
            text=message,
            fg=AppConfig.COLORS['button_red'],
            bg=AppConfig.COLORS['settings_bg'],
            font=("Helvetica", 10)
        )
        error_label.pack(pady=5)
        self.window.after(3000, error_label.destroy)  # Remove error after 3 seconds
