"""
Timer logic for the Pomodoro technique.
"""

import time
import threading
from .config import AppConfig

class PomodoroTimer:
    """Handles Pomodoro timer logic and state management."""
    
    def __init__(self, on_timer_complete=None, on_timer_update=None):
        """
        Initialize the Pomodoro timer.
        
        Args:
            on_timer_complete: Callback function called when timer completes
            on_timer_update: Callback function called every second during countdown
        """
        self.work_time = AppConfig.DEFAULT_WORK_TIME
        self.short_break_time = AppConfig.DEFAULT_SHORT_BREAK
        self.long_break_time = AppConfig.DEFAULT_LONG_BREAK
        
        self.time_left = self.work_time
        self.current_interval = 0  # Track current interval (0-3)
        self.is_break = False
        self.running = False
        
        # Callbacks
        self.on_timer_complete = on_timer_complete
        self.on_timer_update = on_timer_update
        
        # Threading
        self._timer_thread = None
    
    def start(self):
        """Start the timer."""
        if not self.running:
            self.running = True
            self._timer_thread = threading.Thread(target=self._countdown)
            self._timer_thread.daemon = True
            self._timer_thread.start()
    
    def stop(self):
        """Stop the timer."""
        self.running = False
    
    def reset(self):
        """Reset the timer to initial state."""
        self.running = False
        self.current_interval = 0
        self.is_break = False
        self.time_left = self.work_time
    
    def _countdown(self):
        """Internal countdown loop."""
        while self.time_left > 0 and self.running:
            if self.on_timer_update:
                self.on_timer_update(self.time_left)
            
            time.sleep(1)
            self.time_left -= 1
        
        if self.time_left == 0 and self.running:
            self._handle_timer_complete()
    
    def _handle_timer_complete(self):
        """Handle timer completion and transition to next phase."""
        if not self.is_break:
            # Just finished a work interval
            self.current_interval += 1
            
            if self.current_interval >= 4:
                # Time for long break after 4 intervals
                self.is_break = True
                self.time_left = self.long_break_time
                self.current_interval = 0  # Reset interval counter
            else:
                # Time for short break
                self.is_break = True
                self.time_left = self.short_break_time
        else:
            # Just finished a break, back to work
            self.is_break = False
            self.time_left = self.work_time
        
        self.running = False
        
        if self.on_timer_complete:
            self.on_timer_complete()
    
    def get_time_display(self):
        """Get formatted time string for display."""
        mins, secs = divmod(self.time_left, 60)
        return f"{mins:02d}:{secs:02d}"
    
    def get_status_text(self):
        """Get current status text."""
        if self.is_break:
            if self.current_interval == 0:
                return "Long Break Time"
            else:
                return "Short Break Time"
        else:
            return f"Work Time - Interval {self.current_interval + 1}/4"
    
    def get_phase_message(self):
        """Get phase transition message."""
        if self.is_break:
            if self.current_interval == 0:
                return "Long Break!"
            else:
                return "Short Break!"
        else:
            return "Back to Work!"
    
    def set_work_time(self, minutes):
        """Set work time in minutes."""
        self.work_time = minutes * 60
        if not self.is_break and not self.running:
            self.time_left = self.work_time
    
    def set_short_break_time(self, minutes):
        """Set short break time in minutes."""
        self.short_break_time = minutes * 60
    
    def set_long_break_time(self, minutes):
        """Set long break time in minutes."""
        self.long_break_time = minutes * 60
    
    def get_work_time_minutes(self):
        """Get work time in minutes."""
        return self.work_time // 60
    
    def get_short_break_minutes(self):
        """Get short break time in minutes."""
        return self.short_break_time // 60
    
    def get_long_break_minutes(self):
        """Get long break time in minutes."""
        return self.long_break_time // 60
