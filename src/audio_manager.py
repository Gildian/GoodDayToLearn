"""
Audio manager for handling music and sound effects.
"""

import pygame
from .config import AppConfig

class AudioManager:
    """Manages audio playback for music and alarm sounds."""
    
    def __init__(self):
        """Initialize the audio manager."""
        self.music_volume = AppConfig.DEFAULT_MUSIC_VOLUME
        self.alarm_volume = AppConfig.DEFAULT_ALARM_VOLUME
        self.alarm_sound = None
        
        # Initialize pygame mixer
        pygame.mixer.init()
        self._load_audio_files()
    
    def _load_audio_files(self):
        """Load audio files for music and alarm."""
        try:
            # Load background music
            pygame.mixer.music.load(AppConfig.MUSIC_FILE)
            pygame.mixer.music.set_volume(self.music_volume)
            
            # Load alarm sound
            self.alarm_sound = pygame.mixer.Sound(AppConfig.ALARM_FILE)
            self.alarm_sound.set_volume(self.alarm_volume)
        except pygame.error as e:
            print(f"Error loading audio files: {e}")
    
    def set_music_volume(self, volume):
        """Set the music volume (0.0 to 1.0)."""
        self.music_volume = max(0.0, min(1.0, volume))
        pygame.mixer.music.set_volume(self.music_volume)
    
    def set_alarm_volume(self, volume):
        """Set the alarm volume (0.0 to 1.0)."""
        self.alarm_volume = max(0.0, min(1.0, volume))
        if self.alarm_sound:
            self.alarm_sound.set_volume(self.alarm_volume)
    
    def start_music(self):
        """Start playing background music on loop."""
        try:
            pygame.mixer.music.play(-1)  # Loop forever
        except pygame.error as e:
            print(f"Error starting music: {e}")
    
    def stop_music(self):
        """Stop playing background music."""
        pygame.mixer.music.stop()
    
    def play_alarm(self):
        """Play the alarm sound once."""
        if self.alarm_sound:
            try:
                self.alarm_sound.play()
            except pygame.error as e:
                print(f"Error playing alarm: {e}")
    
    def get_music_volume(self):
        """Get the current music volume."""
        return self.music_volume
    
    def get_alarm_volume(self):
        """Get the current alarm volume."""
        return self.alarm_volume
