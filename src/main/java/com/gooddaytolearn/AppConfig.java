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
    
    // Color scheme
    public static final Map<String, Color> COLORS = new HashMap<>();
    static {
        COLORS.put("background", new Color(44, 62, 80));           // Dark blue-gray
        COLORS.put("text_primary", new Color(236, 240, 241));      // Light gray
        COLORS.put("text_secondary", new Color(189, 195, 199));    // Medium gray
        COLORS.put("text_muted", new Color(149, 165, 166));        // Muted gray
        COLORS.put("work_time", new Color(231, 76, 60));           // Red
        COLORS.put("break_time", new Color(243, 156, 18));         // Orange
        COLORS.put("button_green", new Color(39, 174, 96));        // Green
        COLORS.put("button_green_active", new Color(46, 204, 113));
        COLORS.put("button_red", new Color(231, 76, 60));          // Red
        COLORS.put("button_red_active", new Color(192, 57, 43));
        COLORS.put("button_blue", new Color(52, 152, 219));        // Blue
        COLORS.put("button_blue_active", new Color(41, 128, 185));
        COLORS.put("button_purple", new Color(155, 89, 182));      // Purple
        COLORS.put("button_purple_active", new Color(142, 68, 173));
        COLORS.put("settings_bg", new Color(52, 73, 94));          // Darker gray
        COLORS.put("slider_active_blue", new Color(52, 152, 219));
        COLORS.put("slider_active_red", new Color(231, 76, 60));
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
