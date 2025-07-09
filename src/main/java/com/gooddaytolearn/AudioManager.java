package com.gooddaytolearn;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Manages audio playback for music and alarm sounds.
 */
public class AudioManager {
    
    private double musicVolume;
    private double alarmVolume;
    private Clip musicClip;
    private Clip alarmClip;
    
    /**
     * Initialize the audio manager.
     */
    public AudioManager() {
        this.musicVolume = AppConfig.DEFAULT_MUSIC_VOLUME;
        this.alarmVolume = AppConfig.DEFAULT_ALARM_VOLUME;
        loadAudioFiles();
    }
    
    /**
     * Load audio files for music and alarm.
     */
    private void loadAudioFiles() {
        try {
            // Load background music
            musicClip = loadAudioClip(AppConfig.MUSIC_FILE);
            if (musicClip != null) {
                setClipVolume(musicClip, musicVolume);
            }
            
            // Load alarm sound
            alarmClip = loadAudioClip(AppConfig.ALARM_FILE);
            if (alarmClip != null) {
                setClipVolume(alarmClip, alarmVolume);
            }
        } catch (Exception e) {
            System.err.println("Error loading audio files: " + e.getMessage());
        }
    }
    
    /**
     * Load an audio clip from resources.
     */
    private Clip loadAudioClip(String resourcePath) {
        try {
            InputStream audioStream = getClass().getResourceAsStream(resourcePath);
            if (audioStream == null) {
                System.err.println("Audio file not found: " + resourcePath);
                return null;
            }
            
            // Create a BufferedInputStream to support mark/reset
            InputStream bufferedStream = audioStream.markSupported() ? 
                audioStream : new java.io.BufferedInputStream(audioStream);
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading audio clip " + resourcePath + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Set the volume of a clip.
     */
    private void setClipVolume(Clip clip, double volume) {
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(Math.max(0.0001, volume)) / Math.log(10.0) * 20.0);
            gainControl.setValue(Math.max(gainControl.getMinimum(), Math.min(dB, gainControl.getMaximum())));
        }
    }
    
    /**
     * Set the music volume (0.0 to 1.0).
     */
    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (musicClip != null) {
            setClipVolume(musicClip, this.musicVolume);
        }
    }
    
    /**
     * Set the alarm volume (0.0 to 1.0).
     */
    public void setAlarmVolume(double volume) {
        this.alarmVolume = Math.max(0.0, Math.min(1.0, volume));
        if (alarmClip != null) {
            setClipVolume(alarmClip, this.alarmVolume);
        }
    }
    
    /**
     * Start playing background music on loop.
     */
    public void startMusic() {
        if (musicClip != null) {
            try {
                musicClip.setFramePosition(0);
                musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            } catch (Exception e) {
                System.err.println("Error starting music: " + e.getMessage());
            }
        }
    }
    
    /**
     * Stop playing background music.
     */
    public void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
        }
    }
    
    /**
     * Play the alarm sound.
     */
    public void playAlarm() {
        if (alarmClip != null) {
            try {
                alarmClip.setFramePosition(0);
                alarmClip.start();
            } catch (Exception e) {
                System.err.println("Error playing alarm: " + e.getMessage());
            }
        }
    }
    
    /**
     * Get current music volume.
     */
    public double getMusicVolume() {
        return musicVolume;
    }
    
    /**
     * Get current alarm volume.
     */
    public double getAlarmVolume() {
        return alarmVolume;
    }
    
    /**
     * Clean up audio resources.
     */
    public void cleanup() {
        if (musicClip != null) {
            musicClip.close();
        }
        if (alarmClip != null) {
            alarmClip.close();
        }
    }
}
