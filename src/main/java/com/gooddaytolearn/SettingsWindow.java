package com.gooddaytolearn;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Settings window for configuring timer and audio settings.
 */
public class SettingsWindow extends JDialog {
    
    private PomodoroTimer timer;
    private AudioManager audioManager;
    private Runnable onSettingsSaved;
    
    private JSpinner workTimeSpinner;
    private JSpinner shortBreakSpinner;
    private JSpinner longBreakSpinner;
    private JSlider musicVolumeSlider;
    private JSlider alarmVolumeSlider;
    
    /**
     * Initialize the settings window.
     * 
     * @param parent Parent window
     * @param timer PomodoroTimer instance
     * @param audioManager AudioManager instance
     * @param onSettingsSaved Callback function called when settings are saved
     */
    public SettingsWindow(JFrame parent, PomodoroTimer timer, AudioManager audioManager, Runnable onSettingsSaved) {
        super(parent, "Settings", true);
        this.timer = timer;
        this.audioManager = audioManager;
        this.onSettingsSaved = onSettingsSaved;
        
        setupWindow();
        createWidgets();
        pack();
        setLocationRelativeTo(parent);
    }
    
    /**
     * Configure the settings window.
     */
    private void setupWindow() {
        setSize(AppConfig.SETTINGS_WINDOW_WIDTH, AppConfig.SETTINGS_WINDOW_HEIGHT);
        setResizable(false);
        getContentPane().setBackground(AppConfig.COLORS.get("settings_bg"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    /**
     * Create all widgets for the settings window with modern styling.
     */
    private void createWidgets() {
        setLayout(new BorderLayout());
        
        // Title with modern typography
        JLabel titleLabel = new JLabel("Timer Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 20));
        titleLabel.setForeground(AppConfig.COLORS.get("text_primary"));
        titleLabel.setBorder(new EmptyBorder(25, 0, 25, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Settings panel with modern card-like appearance
        JPanel settingsPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded card background
                g2d.setColor(AppConfig.COLORS.get("panel_bg"));
                g2d.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 16, 16);
                
                // Add subtle border
                g2d.setColor(AppConfig.COLORS.get("border_color"));
                g2d.drawRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 16, 16);
            }
        };
        settingsPanel.setOpaque(false);
        settingsPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Timer settings
        addTimerSettings(settingsPanel, gbc);
        
        // Audio settings
        addAudioSettings(settingsPanel, gbc);
        
        add(settingsPanel, BorderLayout.CENTER);
        
        // Buttons
        addButtons();
    }
    
    /**
     * Add timer settings controls.
     */
    private void addTimerSettings(JPanel panel, GridBagConstraints gbc) {
        // Work time
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel workLabel = createLabel("Work Time (minutes):");
        panel.add(workLabel, gbc);
        
        gbc.gridx = 1;
        workTimeSpinner = new JSpinner(new SpinnerNumberModel(timer.getWorkTimeMinutes(), 
                AppConfig.MIN_WORK_TIME, AppConfig.MAX_WORK_TIME, 1));
        styleSpinner(workTimeSpinner);
        panel.add(workTimeSpinner, gbc);
        
        // Short break time
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel shortBreakLabel = createLabel("Short Break (minutes):");
        panel.add(shortBreakLabel, gbc);
        
        gbc.gridx = 1;
        shortBreakSpinner = new JSpinner(new SpinnerNumberModel(timer.getShortBreakMinutes(), 1, 30, 1));
        styleSpinner(shortBreakSpinner);
        panel.add(shortBreakSpinner, gbc);
        
        // Long break time
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel longBreakLabel = createLabel("Long Break (minutes):");
        panel.add(longBreakLabel, gbc);
        
        gbc.gridx = 1;
        longBreakSpinner = new JSpinner(new SpinnerNumberModel(timer.getLongBreakMinutes(), 1, 60, 1));
        styleSpinner(longBreakSpinner);
        panel.add(longBreakSpinner, gbc);
    }
    
    /**
     * Add audio settings controls.
     */
    private void addAudioSettings(JPanel panel, GridBagConstraints gbc) {
        // Music volume
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel musicVolumeLabel = createLabel("Music Volume:");
        panel.add(musicVolumeLabel, gbc);
        
        gbc.gridy = 4;
        musicVolumeSlider = new JSlider(0, 100, (int)(audioManager.getMusicVolume() * 100));
        styleSlider(musicVolumeSlider);
        panel.add(musicVolumeSlider, gbc);
        
        // Alarm volume
        gbc.gridy = 5;
        JLabel alarmVolumeLabel = createLabel("Alarm Volume:");
        panel.add(alarmVolumeLabel, gbc);
        
        gbc.gridy = 6;
        alarmVolumeSlider = new JSlider(0, 100, (int)(audioManager.getAlarmVolume() * 100));
        styleSlider(alarmVolumeSlider);
        panel.add(alarmVolumeSlider, gbc);
    }
    
    /**
     * Add modern action buttons.
     */
    private void addButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(AppConfig.COLORS.get("settings_bg"));
        buttonPanel.setBorder(new EmptyBorder(20, 0, 25, 0));
        
        // Save button
        JButton saveButton = createModernButton("Save", "button_green", "button_green_active");
        saveButton.addActionListener(e -> saveSettings());
        buttonPanel.add(saveButton);
        
        // Cancel button
        JButton cancelButton = createModernButton("Cancel", "button_red", "button_red_active");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create a modern styled button for settings window.
     */
    private JButton createModernButton(String text, String bgColorKey, String activeColorKey) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded background
                Color bgColor = getModel().isPressed() || getModel().isRollover() ? 
                    AppConfig.COLORS.get(activeColorKey) : AppConfig.COLORS.get(bgColorKey);
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Draw text
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                
                g2d.setColor(getForeground());
                g2d.drawString(getText(), textX, textY);
            }
        };
        
        button.setFont(new Font("SF Pro Text", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(80, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    /**
     * Create a styled label with modern typography.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(AppConfig.COLORS.get("text_primary"));
        label.setFont(new Font("SF Pro Text", Font.PLAIN, 13));
        return label;
    }
    
    /**
     * Style a spinner component with dark theme.
     */
    private void styleSpinner(JSpinner spinner) {
        spinner.setPreferredSize(new Dimension(90, 30));
        
        // Style the editor
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setBackground(AppConfig.COLORS.get("button_bg"));
            textField.setForeground(AppConfig.COLORS.get("text_primary"));
            textField.setCaretColor(AppConfig.COLORS.get("text_primary"));
            textField.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        }
        
        // Style the spinner buttons
        for (Component comp : spinner.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setBackground(AppConfig.COLORS.get("button_bg"));
                button.setForeground(AppConfig.COLORS.get("text_primary"));
                button.setBorder(BorderFactory.createEmptyBorder());
            }
        }
    }
    
    /**
     * Style a slider component with modern dark theme.
     */
    private void styleSlider(JSlider slider) {
        slider.setOpaque(false);
        slider.setForeground(AppConfig.COLORS.get("text_primary"));
        slider.setPaintTicks(false);
        slider.setPaintLabels(false);
        slider.setMajorTickSpacing(25);
        slider.setMinorTickSpacing(5);
        
        // Set custom UI for better dark theme support
        slider.putClientProperty("Slider.trackColor", AppConfig.COLORS.get("slider_track"));
        slider.putClientProperty("Slider.thumbColor", AppConfig.COLORS.get("accent_primary"));
    }
    
    /**
     * Save the current settings.
     */
    private void saveSettings() {
        // Update timer settings
        timer.setWorkTime((Integer) workTimeSpinner.getValue());
        timer.setShortBreakTime((Integer) shortBreakSpinner.getValue());
        timer.setLongBreakTime((Integer) longBreakSpinner.getValue());
        
        // Update audio settings
        audioManager.setMusicVolume(musicVolumeSlider.getValue() / 100.0);
        audioManager.setAlarmVolume(alarmVolumeSlider.getValue() / 100.0);
        
        // Callback to parent
        if (onSettingsSaved != null) {
            onSettingsSaved.run();
        }
        
        dispose();
    }
}
