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
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
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
