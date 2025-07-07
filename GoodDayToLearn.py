import tkinter as tk
import time
import threading
import pygame
from datetime import datetime

class GoodDayToLearnApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Good Day To Learn")
        self.root.geometry("700x450")
        self.root.configure(bg="#2c3e50")  # Dark blue-gray background
        self.root.resizable(False, False)

        # Pomodoro settings
        self.work_time = 25 * 60  # 25 minutes work
        self.short_break_time = 5 * 60  # 5 minutes short break
        self.long_break_time = 20 * 60  # 20 minutes long break
        
        # Volume settings
        self.music_volume = 0.5  # Default music volume (0.0 to 1.0)
        self.alarm_volume = 0.7  # Default alarm volume (0.0 to 1.0)
        
        self.time_left = self.work_time
        self.current_interval = 0  # Track current interval (0-3)
        self.is_break = False  # Track if currently in break mode

        self.running = False

        # Title label
        title_label = tk.Label(root, text="Good Day To Learn", 
                              font=("Helvetica", 24, "bold"), 
                              bg="#2c3e50", fg="#ecf0f1")
        title_label.pack(pady=20)

        # Real-time clock
        self.clock_label = tk.Label(root, text="", 
                                   font=("Helvetica", 14), 
                                   bg="#2c3e50", fg="#bdc3c7")
        self.clock_label.pack(pady=5)

        # Main timer display
        self.label = tk.Label(root, text="25:00", 
                             font=("Helvetica", 48, "bold"), 
                             bg="#2c3e50", fg="#e74c3c")
        self.label.pack(pady=30)

        # Status label to show current phase
        self.status_label = tk.Label(root, text="Work Time - Interval 1/4", 
                                    font=("Helvetica", 16), 
                                    bg="#2c3e50", fg="#95a5a6")
        self.status_label.pack(pady=10)

        # Button frame for better layout
        button_frame = tk.Frame(root, bg="#2c3e50")
        button_frame.pack(pady=30)

        # Styled buttons
        self.start_button = tk.Button(button_frame, text="Start", 
                                     command=self.start_timer,
                                     font=("Helvetica", 12, "bold"),
                                     bg="#27ae60", fg="black",
                                     activebackground="#2ecc71",
                                     relief="flat", padx=20, pady=10,
                                     cursor="hand2")
        self.start_button.pack(side=tk.LEFT, padx=10)

        self.stop_button = tk.Button(button_frame, text="Stop", 
                                    command=self.stop_timer,
                                    font=("Helvetica", 12, "bold"),
                                    bg="#e74c3c", fg="black",
                                    activebackground="#c0392b",
                                    relief="flat", padx=20, pady=10,
                                    cursor="hand2")
        self.stop_button.pack(side=tk.LEFT, padx=10)

        self.reset_button = tk.Button(button_frame, text="Reset", 
                                     command=self.reset_timer,
                                     font=("Helvetica", 12, "bold"),
                                     bg="#3498db", fg="black",
                                     activebackground="#2980b9",
                                     relief="flat", padx=20, pady=10,
                                     cursor="hand2")
        self.reset_button.pack(side=tk.LEFT, padx=10)

        self.settings_button = tk.Button(button_frame, text="Settings", 
                                        command=self.open_settings,
                                        font=("Helvetica", 12, "bold"),
                                        bg="#9b59b6", fg="black",
                                        activebackground="#8e44ad",
                                        relief="flat", padx=20, pady=10,
                                        cursor="hand2")
        self.settings_button.pack(side=tk.LEFT, padx=10)

        pygame.mixer.init()
        pygame.mixer.music.load("rain.mp3")  # Place a rain sound file here
        pygame.mixer.music.set_volume(self.music_volume)  # Set initial music volume
        
        # Load alarm sound
        self.alarm_sound = pygame.mixer.Sound("alarm.mp3")
        self.alarm_sound.set_volume(self.alarm_volume)  # Set initial alarm volume

    def start_timer(self):
        if not self.running:
            self.running = True
            # Only play music during work sessions, not breaks
            if not self.is_break:
                pygame.mixer.music.play(-1)  # Loop forever
            threading.Thread(target=self.countdown).start()
    
    def stop_timer(self):
        self.running = False
        self.update_display()
        self.update_status_label()
        pygame.mixer.music.stop()

    def reset_timer(self):
        self.running = False
        self.current_interval = 0
        self.is_break = False
        self.time_left = self.work_time
        self.update_display()
        self.update_status_label()
        pygame.mixer.music.stop()

    def countdown(self):
        while self.time_left > 0 and self.running:
            mins, secs = divmod(self.time_left, 60)
            self.label.config(text=f"{mins:02d}:{secs:02d}")
            time.sleep(1)
            self.time_left -= 1
        
        if self.time_left == 0 and self.running:
            self.handle_timer_complete()

    def handle_timer_complete(self):
        pygame.mixer.music.stop()
        
        # Play alarm sound when timer completes
        self.alarm_sound.play()
        
        if not self.is_break:
            # Just finished a work interval
            self.current_interval += 1
            
            if self.current_interval >= 4:
                # Time for long break after 4 intervals
                self.is_break = True
                self.time_left = self.long_break_time
                self.label.config(text="Long Break!")
                self.current_interval = 0  # Reset interval counter
            else:
                # Time for short break
                self.is_break = True
                self.time_left = self.short_break_time
                self.label.config(text="Short Break!")
        else:
            # Just finished a break, back to work
            self.is_break = False
            self.time_left = self.work_time
            self.label.config(text="Back to Work!")
            # Start playing music again when returning to work
            pygame.mixer.music.play(-1)
        
        self.update_display()
        self.update_status_label()
        self.running = False

    def update_display(self):
        mins, secs = divmod(self.time_left, 60)
        self.label.config(text=f"{mins:02d}:{secs:02d}")
        
        # Update color based on timer state
        if self.is_break:
            self.label.config(fg="#f39c12")  # Orange for breaks
        else:
            self.label.config(fg="#e74c3c")  # Red for work time

    def update_status_label(self):
        if self.is_break:
            if self.current_interval == 0:
                self.status_label.config(text="Long Break Time", fg="#f39c12")
            else:
                self.status_label.config(text="Short Break Time", fg="#f39c12")
        else:
            self.status_label.config(text=f"Work Time - Interval {self.current_interval + 1}/4", fg="#95a5a6")

    def open_settings(self):
        settings_window = tk.Toplevel(self.root)
        settings_window.title("Settings")
        settings_window.geometry("600x750")
        settings_window.configure(bg="#34495e")
        settings_window.resizable(False, False)
        
        # Center the window
        settings_window.transient(self.root)
        settings_window.grab_set()
        
        # Title
        title_label = tk.Label(settings_window, text="Timer Settings", 
                              font=("Helvetica", 18, "bold"), 
                              bg="#34495e", fg="#ecf0f1")
        title_label.pack(pady=20)
        
        # Settings frame
        settings_frame = tk.Frame(settings_window, bg="#34495e")
        settings_frame.pack(pady=10)
        
        # Work time setting
        work_frame = tk.Frame(settings_frame, bg="#34495e")
        work_frame.pack(pady=10)
        tk.Label(work_frame, text="Work Time (minutes):", 
                font=("Helvetica", 12), bg="#34495e", fg="#ecf0f1").pack()
        work_time_var = tk.StringVar(value=str(self.work_time // 60))
        work_time_entry = tk.Entry(work_frame, textvariable=work_time_var, 
                                  font=("Helvetica", 12), width=10, 
                                  justify="center", relief="flat", 
                                  bg="#ecf0f1", fg="#2c3e50")
        work_time_entry.pack(pady=5)
        
        # Short break time setting
        short_frame = tk.Frame(settings_frame, bg="#34495e")
        short_frame.pack(pady=10)
        tk.Label(short_frame, text="Short Break Time (minutes):", 
                font=("Helvetica", 12), bg="#34495e", fg="#ecf0f1").pack()
        short_break_var = tk.StringVar(value=str(self.short_break_time // 60))
        short_break_entry = tk.Entry(short_frame, textvariable=short_break_var, 
                                    font=("Helvetica", 12), width=10, 
                                    justify="center", relief="flat", 
                                    bg="#ecf0f1", fg="#2c3e50")
        short_break_entry.pack(pady=5)
        
        # Long break time setting
        long_frame = tk.Frame(settings_frame, bg="#34495e")
        long_frame.pack(pady=10)
        tk.Label(long_frame, text="Long Break Time (minutes):", 
                font=("Helvetica", 12), bg="#34495e", fg="#ecf0f1").pack()
        long_break_var = tk.StringVar(value=str(self.long_break_time // 60))
        long_break_entry = tk.Entry(long_frame, textvariable=long_break_var, 
                                   font=("Helvetica", 12), width=10, 
                                   justify="center", relief="flat", 
                                   bg="#ecf0f1", fg="#2c3e50")
        long_break_entry.pack(pady=5)
        
        # Volume settings
        volume_frame = tk.Frame(settings_frame, bg="#34495e")
        volume_frame.pack(pady=15)
        
        # Music volume setting
        music_vol_frame = tk.Frame(volume_frame, bg="#34495e")
        music_vol_frame.pack(pady=5)
        tk.Label(music_vol_frame, text="Music Volume:", 
                font=("Helvetica", 12), bg="#34495e", fg="#ecf0f1").pack()
        music_volume_scale = tk.Scale(music_vol_frame, from_=0, to=100, 
                                     orient=tk.HORIZONTAL, 
                                     bg="#2c3e50", fg="#ecf0f1", 
                                     highlightthickness=0,
                                     troughcolor="#34495e", 
                                     activebackground="#3498db",
                                     length=200)
        music_volume_scale.set(int(self.music_volume * 100))
        music_volume_scale.pack(pady=5)
        
        # Alarm volume setting
        alarm_vol_frame = tk.Frame(volume_frame, bg="#34495e")
        alarm_vol_frame.pack(pady=5)
        tk.Label(alarm_vol_frame, text="Alarm Volume:", 
                font=("Helvetica", 12), bg="#34495e", fg="#ecf0f1").pack()
        alarm_volume_scale = tk.Scale(alarm_vol_frame, from_=0, to=100, 
                                     orient=tk.HORIZONTAL, 
                                     bg="#2c3e50", fg="#ecf0f1", 
                                     highlightthickness=0,
                                     troughcolor="#34495e", 
                                     activebackground="#e74c3c",
                                     length=200)
        alarm_volume_scale.set(int(self.alarm_volume * 100))
        alarm_volume_scale.pack(pady=5)
        
        # Save and Cancel buttons
        button_frame = tk.Frame(settings_window, bg="#34495e")
        button_frame.pack(pady=30)
        
        def save_settings():
            try:
                # Validate and convert input
                work_mins = int(work_time_var.get())
                short_mins = int(short_break_var.get())
                long_mins = int(long_break_var.get())
                
                # Check for reasonable values
                if work_mins < 1 or work_mins > 120:
                    raise ValueError("Work time must be between 1 and 120 minutes")
                if short_mins < 1 or short_mins > 60:
                    raise ValueError("Short break must be between 1 and 60 minutes")
                if long_mins < 1 or long_mins > 120:
                    raise ValueError("Long break must be between 1 and 120 minutes")
                
                # Update settings
                self.work_time = work_mins * 60
                self.short_break_time = short_mins * 60
                self.long_break_time = long_mins * 60
                
                # Update volume settings
                self.music_volume = music_volume_scale.get() / 100.0
                self.alarm_volume = alarm_volume_scale.get() / 100.0
                
                # Apply volume changes
                pygame.mixer.music.set_volume(self.music_volume)
                self.alarm_sound.set_volume(self.alarm_volume)
                
                # Reset timer if not running
                if not self.running:
                    self.reset_timer()
                
                settings_window.destroy()
                
            except ValueError as e:
                # Clear any existing error labels
                for widget in settings_window.winfo_children():
                    if isinstance(widget, tk.Label) and widget.cget("fg") == "#e74c3c":
                        widget.destroy()
                
                error_label = tk.Label(settings_window, text=str(e), 
                                     fg="#e74c3c", bg="#34495e", 
                                     font=("Helvetica", 10))
                error_label.pack(pady=5)
                settings_window.after(3000, error_label.destroy)  # Remove error after 3 seconds
        
        def cancel_settings():
            settings_window.destroy()
        
        save_button = tk.Button(button_frame, text="Save", 
                               command=save_settings,
                               font=("Helvetica", 12, "bold"),
                               bg="#27ae60", fg="black",
                               activebackground="#2ecc71",
                               relief="flat", padx=20, pady=8,
                               cursor="hand2")
        save_button.pack(side=tk.LEFT, padx=15)
        
        cancel_button = tk.Button(button_frame, text="Cancel", 
                                 command=cancel_settings,
                                 font=("Helvetica", 12, "bold"),
                                 bg="#e74c3c", fg="black",
                                 activebackground="#c0392b",
                                 relief="flat", padx=20, pady=8,
                                 cursor="hand2")
        cancel_button.pack(side=tk.LEFT, padx=15)

if __name__ == "__main__":
    root = tk.Tk()
    app = GoodDayToLearnApp(root)
    root.mainloop()