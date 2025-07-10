package com.gooddaytolearn;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration settings for the Good Day To Learn application.
 */
public class AppConfig {
    
    // Window settings
    public static final String WINDOW_TITLE = "Good Day To Learn";
    public static final int WINDOW_WIDTH = 700;
    public static final int WINDOW_HEIGHT = 450;
    public static final boolean WINDOW_RESIZABLE = false;
    
    // Modern Dark Theme Color Scheme
    public static final Map<String, Color> COLORS = new HashMap<>();
    static {
        // Background colors - Deep dark with subtle blue undertones
        COLORS.put("background", new Color(18, 18, 20));           // Almost black with slight blue
        COLORS.put("settings_bg", new Color(22, 22, 25));          // Slightly lighter for settings
        COLORS.put("panel_bg", new Color(28, 28, 32));             // Card/panel background
        COLORS.put("button_bg", new Color(38, 38, 42));            // Button base background
        
        // Text colors - High contrast with modern hierarchy
        COLORS.put("text_primary", new Color(255, 255, 255));      // Pure white for main text
        COLORS.put("text_secondary", new Color(200, 200, 205));    // Slightly dimmed white
        COLORS.put("text_muted", new Color(140, 140, 150));        // Muted gray for secondary info
        COLORS.put("text_accent", new Color(120, 120, 128));       // Very muted for hints
        
        // Timer state colors - Vibrant but not harsh
        COLORS.put("work_time", new Color(255, 95, 95));           // Soft red for work time
        COLORS.put("break_time", new Color(100, 210, 255));        // Bright blue for break time
        COLORS.put("timer_glow", new Color(80, 200, 255, 20));     // Subtle glow effect
        
        // Modern button colors with better accessibility
        COLORS.put("button_green", new Color(46, 204, 113));       // Bright green
        COLORS.put("button_green_active", new Color(39, 174, 96)); // Darker on hover
        COLORS.put("button_green_text", new Color(255, 255, 255)); // White text
        
        COLORS.put("button_red", new Color(255, 95, 95));          // Soft red
        COLORS.put("button_red_active", new Color(230, 70, 70));   // Darker on hover
        COLORS.put("button_red_text", new Color(255, 255, 255));   // White text
        
        COLORS.put("button_blue", new Color(100, 170, 255));       // Modern blue
        COLORS.put("button_blue_active", new Color(80, 150, 235)); // Darker on hover
        COLORS.put("button_blue_text", new Color(255, 255, 255));  // White text
        
        COLORS.put("button_purple", new Color(155, 135, 255));     // Modern purple
        COLORS.put("button_purple_active", new Color(135, 115, 235)); // Darker on hover
        COLORS.put("button_purple_text", new Color(255, 255, 255)); // White text
        
        // Accent and highlight colors
        COLORS.put("accent_primary", new Color(100, 170, 255));    // Primary brand blue
        COLORS.put("accent_secondary", new Color(155, 135, 255));  // Secondary purple
        COLORS.put("border_color", new Color(55, 55, 60));         // Subtle borders
        COLORS.put("hover_overlay", new Color(255, 255, 255, 8)); // Subtle hover effect
        
        // Slider colors
        COLORS.put("slider_active_blue", new Color(100, 170, 255));
        COLORS.put("slider_active_red", new Color(255, 95, 95));
        COLORS.put("slider_track", new Color(45, 45, 50));         // Slider track background
    }
    
    // Timer defaults (in seconds)
    public static final int DEFAULT_WORK_TIME = 25 * 60;      // 25 minutes
    public static final int DEFAULT_SHORT_BREAK = 5 * 60;     // 5 minutes
    public static final int DEFAULT_LONG_BREAK = 20 * 60;     // 20 minutes
    
    // Volume defaults (0.0 to 1.0)
    public static final double DEFAULT_MUSIC_VOLUME = 0.5;
    public static final double DEFAULT_ALARM_VOLUME = 0.7;
    
    // Audio files
    public static final String MUSIC_FILE = "/rain.wav";
    public static final String ALARM_FILE = "/alarm.wav";
    
    // Timer validation limits
    public static final int MIN_WORK_TIME = 1;      // minutes
    public static final int MAX_WORK_TIME = 120;    // minutes
    
    // Settings window dimensions
    public static final int SETTINGS_WINDOW_WIDTH = 400;
    public static final int SETTINGS_WINDOW_HEIGHT = 350;
}
