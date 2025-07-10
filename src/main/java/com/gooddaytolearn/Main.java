package com.gooddaytolearn;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Good Day To Learn - A Pomodoro Timer Application
 * 
 * Entry point for the application.
 */
public class Main {
    
    /**
     * Main entry point for the application.
     */
    public static void main(String[] args) {
        // Set modern dark look and feel
        try {
            // Try to use FlatLaf Dark theme for better modern appearance
            // If not available, fall back to system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Set additional UI properties for better dark theme support
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("ScrollBar.trackArc", 999);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.width", 8);
            
            // Set default colors for better consistency
            UIManager.put("Panel.background", AppConfig.COLORS.get("background"));
            UIManager.put("Button.background", AppConfig.COLORS.get("button_bg"));
            UIManager.put("TextField.background", AppConfig.COLORS.get("button_bg"));
            UIManager.put("Spinner.background", AppConfig.COLORS.get("button_bg"));
            UIManager.put("Slider.background", AppConfig.COLORS.get("background"));
            
            // Smooth font rendering
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
            
        } catch (Exception e) {
            System.err.println("Could not set look and feel: " + e.getMessage());
        }
        
        // Create and show the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                GoodDayToLearnApp app = new GoodDayToLearnApp();
                app.setVisible(true);
            } catch (Exception e) {
                System.err.println("Error starting application: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
