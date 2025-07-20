package com.gooddaytolearn;

import javax.sound.sampled.*;
import java.io.File;
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
    
    // Custom file paths for user-selected sounds
    private String customMusicFile;
    private String customAlarmFile;
    
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
            // Load background music (try custom file first, then default)
            String musicPath = customMusicFile != null ? customMusicFile : AppConfig.MUSIC_FILE;
            musicClip = loadAudioClip(musicPath, customMusicFile != null);
            if (musicClip != null) {
                setClipVolume(musicClip, musicVolume);
            }
            
            // Load alarm sound (try custom file first, then default)
            String alarmPath = customAlarmFile != null ? customAlarmFile : AppConfig.ALARM_FILE;
            alarmClip = loadAudioClip(alarmPath, customAlarmFile != null);
            if (alarmClip != null) {
                setClipVolume(alarmClip, alarmVolume);
            }
        } catch (Exception e) {
            System.err.println("Error loading audio files: " + e.getMessage());
        }
    }
    
    /**
     * Load an audio clip from resources or external file.
     */
    private Clip loadAudioClip(String audioPath, boolean isExternalFile) {
        try {
            AudioInputStream audioInputStream;
            
            if (isExternalFile) {
                // Load from external file
                File audioFile = new File(audioPath);
                if (!audioFile.exists()) {
                    System.err.println("External audio file not found: " + audioPath);
                    return null;
                }
                audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            } else {
                // Load from resources
                InputStream audioStream = getClass().getResourceAsStream(audioPath);
                if (audioStream == null) {
                    System.err.println("Resource audio file not found: " + audioPath);
                    return null;
                }
                
                // Create a BufferedInputStream to support mark/reset
                InputStream bufferedStream = audioStream.markSupported() ? 
                    audioStream : new java.io.BufferedInputStream(audioStream);
                
                audioInputStream = AudioSystem.getAudioInputStream(bufferedStream);
            }
            
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading audio clip " + audioPath + ": " + e.getMessage());
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
     * Set custom music file path.
     */
    public void setCustomMusicFile(String filePath) {
        this.customMusicFile = filePath;
        reloadMusicFile();
    }
    
    /**
     * Set custom alarm file path.
     */
    public void setCustomAlarmFile(String filePath) {
        this.customAlarmFile = filePath;
        reloadAlarmFile();
    }
    
    /**
     * Get current custom music file path.
     */
    public String getCustomMusicFile() {
        return customMusicFile;
    }
    
    /**
     * Get current custom alarm file path.
     */
    public String getCustomAlarmFile() {
        return customAlarmFile;
    }
    
    /**
     * Reset music file to default.
     */
    public void resetMusicToDefault() {
        this.customMusicFile = null;
        reloadMusicFile();
    }
    
    /**
     * Reset alarm file to default.
     */
    public void resetAlarmToDefault() {
        this.customAlarmFile = null;
        reloadAlarmFile();
    }
    
    /**
     * Reload the music file.
     */
    private void reloadMusicFile() {
        // Stop current music if playing
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
        }
        
        // Close old clip
        if (musicClip != null) {
            musicClip.close();
        }
        
        // Load new music file
        String musicPath = customMusicFile != null ? customMusicFile : AppConfig.MUSIC_FILE;
        musicClip = loadAudioClip(musicPath, customMusicFile != null);
        if (musicClip != null) {
            setClipVolume(musicClip, musicVolume);
        }
    }
    
    /**
     * Reload the alarm file.
     */
    private void reloadAlarmFile() {
        // Close old clip
        if (alarmClip != null) {
            alarmClip.close();
        }
        
        // Load new alarm file
        String alarmPath = customAlarmFile != null ? customAlarmFile : AppConfig.ALARM_FILE;
        alarmClip = loadAudioClip(alarmPath, customAlarmFile != null);
        if (alarmClip != null) {
            setClipVolume(alarmClip, alarmVolume);
        }
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
