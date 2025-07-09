package com.gooddaytolearn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main application window and UI components.
 */
public class GoodDayToLearnApp extends JFrame {
    
    private AudioManager audioManager;
    private PomodoroTimer timer;
    
    // UI Components
    private JLabel clockLabel;
    private JLabel timerLabel;
    private JLabel statusLabel;
    private JButton startButton;
    private JButton stopButton;
    private JButton resetButton;
    private JButton settingsButton;
    
    /**
     * Initialize the main application.
     */
    public GoodDayToLearnApp() {
        setupWindow();
        
        // Initialize components
        audioManager = new AudioManager();
        timer = new PomodoroTimer(
            this::onTimerComplete,
            this::onTimerUpdate
        );
        
        // Create UI
        createWidgets();
        updateDisplay();
        
        // Setup window close behavior
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                audioManager.cleanup();
                System.exit(0);
            }
        });
    }
    
    /**
     * Configure the main window.
     */
    private void setupWindow() {
        setTitle(AppConfig.WINDOW_TITLE);
        setSize(AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);
        getContentPane().setBackground(AppConfig.COLORS.get("background"));
        setResizable(AppConfig.WINDOW_RESIZABLE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    /**
     * Create all UI widgets.
     */
    private void createWidgets() {
        setLayout(new BorderLayout());
        
        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(AppConfig.COLORS.get("background"));
        
        // Title label
        JLabel titleLabel = new JLabel(AppConfig.WINDOW_TITLE, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(AppConfig.COLORS.get("text_primary"));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        mainPanel.add(titleLabel);
        
        // Real-time clock (placeholder for future feature)
        clockLabel = new JLabel("", SwingConstants.CENTER);
        clockLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        clockLabel.setForeground(AppConfig.COLORS.get("text_secondary"));
        clockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        clockLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        mainPanel.add(clockLabel);
        
        // Main timer display
        timerLabel = new JLabel("25:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        timerLabel.setForeground(AppConfig.COLORS.get("work_time"));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        mainPanel.add(timerLabel);
        
        // Status label
        statusLabel = new JLabel("Work Time - Interval 1/4", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        statusLabel.setForeground(AppConfig.COLORS.get("text_muted"));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        mainPanel.add(statusLabel);
        
        // Control buttons
        createControlButtons(mainPanel);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Create control buttons.
     */
    private void createControlButtons(JPanel parent) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(AppConfig.COLORS.get("background"));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Start button
        startButton = createButton("Start", AppConfig.COLORS.get("button_green"), 
                AppConfig.COLORS.get("button_green_active"));
        startButton.addActionListener(e -> startTimer());
        buttonPanel.add(startButton);
        
        // Stop button
        stopButton = createButton("Stop", AppConfig.COLORS.get("button_red"), 
                AppConfig.COLORS.get("button_red_active"));
        stopButton.addActionListener(e -> stopTimer());
        buttonPanel.add(stopButton);
        
        // Reset button
        resetButton = createButton("Reset", AppConfig.COLORS.get("button_blue"), 
                AppConfig.COLORS.get("button_blue_active"));
        resetButton.addActionListener(e -> resetTimer());
        buttonPanel.add(resetButton);
        
        // Settings button
        settingsButton = createButton("Settings", AppConfig.COLORS.get("button_purple"), 
                AppConfig.COLORS.get("button_purple_active"));
        settingsButton.addActionListener(e -> openSettings());
        buttonPanel.add(settingsButton);
        
        parent.add(buttonPanel);
    }
    
    /**
     * Create a styled button.
     */
    private JButton createButton(String text, Color bgColor, Color activeColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(80, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(activeColor);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * Start the timer.
     */
    private void startTimer() {
        if (!timer.isRunning()) {
            timer.start();
            // Only play music during work sessions, not breaks
            if (!timer.isBreak()) {
                audioManager.startMusic();
            }
        }
    }
    
    /**
     * Stop the timer.
     */
    private void stopTimer() {
        timer.stop();
        audioManager.stopMusic();
        updateDisplay();
    }
    
    /**
     * Reset the timer.
     */
    private void resetTimer() {
        timer.reset();
        audioManager.stopMusic();
        updateDisplay();
    }
    
    /**
     * Open the settings window.
     */
    private void openSettings() {
        new SettingsWindow(this, timer, audioManager, this::onSettingsSaved).setVisible(true);
    }
    
    /**
     * Handle timer completion.
     */
    private void onTimerComplete() {
        SwingUtilities.invokeLater(() -> {
            audioManager.stopMusic();
            audioManager.playAlarm();
            
            // Update display with phase message
            timerLabel.setText(timer.getPhaseMessage());
            updateDisplay();
        });
    }
    
    /**
     * Handle timer updates during countdown.
     */
    private void onTimerUpdate(Integer timeLeft) {
        SwingUtilities.invokeLater(this::updateTimerDisplay);
    }
    
    /**
     * Handle settings being saved.
     */
    private void onSettingsSaved() {
        // Reset timer if not running
        if (!timer.isRunning()) {
            resetTimer();
        }
    }
    
    /**
     * Update the display with current timer state.
     */
    private void updateDisplay() {
        updateTimerDisplay();
        updateStatusDisplay();
    }
    
    /**
     * Update the timer display.
     */
    private void updateTimerDisplay() {
        timerLabel.setText(timer.getTimeDisplay());
        
        // Update color based on timer state
        if (timer.isBreak()) {
            timerLabel.setForeground(AppConfig.COLORS.get("break_time"));
        } else {
            timerLabel.setForeground(AppConfig.COLORS.get("work_time"));
        }
    }
    
    /**
     * Update the status display.
     */
    private void updateStatusDisplay() {
        String statusText = timer.getStatusText();
        statusLabel.setText(statusText);
        
        // Update status color
        if (timer.isBreak()) {
            statusLabel.setForeground(AppConfig.COLORS.get("break_time"));
        } else {
            statusLabel.setForeground(AppConfig.COLORS.get("text_muted"));
        }
    }
}
