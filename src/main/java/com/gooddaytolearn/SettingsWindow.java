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
     * Create all widgets for the settings window.
     */
    private void createWidgets() {
        setLayout(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("Timer Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(AppConfig.COLORS.get("text_primary"));
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Settings panel
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBackground(AppConfig.COLORS.get("settings_bg"));
        settingsPanel.setBorder(new EmptyBorder(0, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
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
     * Add action buttons.
     */
    private void addButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(AppConfig.COLORS.get("settings_bg"));
        
        // Save button
        JButton saveButton = createButton("Save", AppConfig.COLORS.get("button_green"), 
                AppConfig.COLORS.get("button_green_active"));
        saveButton.addActionListener(e -> saveSettings());
        buttonPanel.add(saveButton);
        
        // Cancel button
        JButton cancelButton = createButton("Cancel", AppConfig.COLORS.get("button_red"), 
                AppConfig.COLORS.get("button_red_active"));
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create a styled label.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(AppConfig.COLORS.get("text_primary"));
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return label;
    }
    
    /**
     * Style a spinner component.
     */
    private void styleSpinner(JSpinner spinner) {
        spinner.setPreferredSize(new Dimension(80, 25));
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setBackground(Color.WHITE);
        }
    }
    
    /**
     * Style a slider component.
     */
    private void styleSlider(JSlider slider) {
        slider.setBackground(AppConfig.COLORS.get("settings_bg"));
        slider.setForeground(AppConfig.COLORS.get("text_primary"));
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing(25);
        slider.setMinorTickSpacing(5);
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
        button.setPreferredSize(new Dimension(80, 30));
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
