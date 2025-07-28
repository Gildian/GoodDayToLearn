package com.gooddaytolearn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * Main application window and UI components.
 */
public class GoodDayToLearnApp extends JFrame {
    
    private AudioManager audioManager;
    private PomodoroTimer timer;
    private TrayIcon trayIcon;
    private SystemTray systemTray;
    
    // UI Components
    private JLabel clockLabel;
    private JLabel timerLabel;
    private JLabel statusLabel;
    private JLabel cyclesLabel;
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
        setupKeyboardShortcuts();
        setupSystemTray();
        updateDisplay();
        
        // Setup window close behavior
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (systemTray != null && trayIcon != null) {
                    setVisible(false);
                    trayIcon.displayMessage("Good Day To Learn", 
                        "Application minimized to tray. Right-click tray icon for options.", 
                        TrayIcon.MessageType.INFO);
                } else {
                    audioManager.cleanup();
                    System.exit(0);
                }
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
     * Create all UI widgets with modern dark theme styling.
     */
    private void createWidgets() {
        setLayout(new BorderLayout());
        
        // Main content panel with gradient-like effect
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Subtle gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, AppConfig.COLORS.get("background"),
                    0, getHeight(), AppConfig.COLORS.get("panel_bg")
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);
        
        // Add spacing at top
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Title label with modern typography
        JLabel titleLabel = new JLabel(AppConfig.WINDOW_TITLE, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        titleLabel.setForeground(AppConfig.COLORS.get("text_primary"));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        
        
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Real-time clock (placeholder for future feature)
        clockLabel = new JLabel("", SwingConstants.CENTER);
        clockLabel.setFont(new Font("SF Mono", Font.PLAIN, 13));
        clockLabel.setForeground(AppConfig.COLORS.get("text_accent"));
        clockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(clockLabel);
        
        mainPanel.add(Box.createVerticalStrut(40));
        
        // Main timer display with modern styling
        timerLabel = new JLabel("25:00", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // Draw progress ring
                if (timer != null) {
                    drawProgressRing(g2d);
                }
                
                // Add subtle glow effect around timer
                Color glowColor = timer != null && timer.isBreak() ? 
                    new Color(100, 210, 255, 15) : new Color(255, 95, 95, 15);
                for (int i = 8; i >= 0; i--) {
                    g2d.setColor(new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), i * 2));
                    g2d.fillRoundRect(i, i, getWidth() - i * 2, getHeight() - i * 2, 20, 20);
                }
                
                super.paintComponent(g);
            }
            
            private void drawProgressRing(Graphics2D g2d) {
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int radius = Math.min(getWidth(), getHeight()) / 2 - 20;
                
                // Calculate progress (simplified approach)
                String timeDisplay = timer.getTimeDisplay();
                String[] parts = timeDisplay.split(":");
                double currentTime = Double.parseDouble(parts[0]) * 60 + Double.parseDouble(parts[1]);
                
                // Estimate total time based on timer state
                double totalTime = timer.isBreak() ? 
                    (timer.getShortBreakMinutes() * 60) : (timer.getWorkTimeMinutes() * 60);
                
                double progress = timer.isRunning() ? 1.0 - (currentTime / totalTime) : 0.0;
                progress = Math.max(0.0, Math.min(1.0, progress)); // Clamp between 0 and 1
                
                // Draw background ring
                g2d.setStroke(new BasicStroke(4.0f));
                g2d.setColor(AppConfig.COLORS.get("slider_track"));
                g2d.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
                
                // Draw progress ring
                if (progress > 0) {
                    g2d.setColor(timer.isBreak() ? 
                        AppConfig.COLORS.get("break_time") : AppConfig.COLORS.get("work_time"));
                    int startAngle = 90; // Start from top
                    int arcAngle = (int) (360 * progress);
                    g2d.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 
                               startAngle, -arcAngle);
                }
            }
        };
        timerLabel.setFont(new Font("SF Pro Display", Font.BOLD, 64));
        timerLabel.setForeground(AppConfig.COLORS.get("work_time"));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.add(timerLabel);
        
        mainPanel.add(Box.createVerticalStrut(15));
        
        // Status label with better typography
        statusLabel = new JLabel("Work Time - Interval 1/4", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 16));
        statusLabel.setForeground(AppConfig.COLORS.get("text_muted"));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(statusLabel);
        
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Completed cycles counter
        cyclesLabel = new JLabel("Completed Cycles: 0", SwingConstants.CENTER);
        cyclesLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        cyclesLabel.setForeground(AppConfig.COLORS.get("text_muted"));
        cyclesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(cyclesLabel);
        
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Control buttons
        createControlButtons(mainPanel);
        
        // Add flexible space at bottom
        mainPanel.add(Box.createVerticalGlue());
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Create modern control buttons with improved spacing and effects.
     */
    private void createControlButtons(JPanel parent) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        // Start button
        startButton = createModernButton("Start", "button_green", "button_green_active");
        startButton.addActionListener(e -> startTimer());
        buttonPanel.add(startButton);
        
        // Stop button
        stopButton = createModernButton("Stop", "button_red", "button_red_active");
        stopButton.addActionListener(e -> stopTimer());
        buttonPanel.add(stopButton);
        
        // Reset button
        resetButton = createModernButton("Reset", "button_blue", "button_blue_active");
        resetButton.addActionListener(e -> resetTimer());
        buttonPanel.add(resetButton);
        
        // Settings button
        settingsButton = createModernButton("Settings", "button_purple", "button_purple_active");
        settingsButton.addActionListener(e -> openSettings());
        buttonPanel.add(settingsButton);
        
        parent.add(buttonPanel);
    }
    
    /**
     * Create a modern styled button with hover effects and rounded corners.
     */
    private JButton createModernButton(String text, String bgColorKey, String activeColorKey) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Determine button color based on state
                Color bgColor;
                if (!isEnabled()) {
                    // Disabled state - use muted color
                    bgColor = AppConfig.COLORS.get("button_bg");
                } else if (getModel().isPressed() || getModel().isRollover()) {
                    // Active/hover state
                    bgColor = AppConfig.COLORS.get(activeColorKey);
                } else {
                    // Normal state
                    bgColor = AppConfig.COLORS.get(bgColorKey);
                }
                
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Add subtle inner shadow when pressed
                if (getModel().isPressed() && isEnabled()) {
                    g2d.setColor(new Color(0, 0, 0, 20));
                    g2d.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 11, 11);
                }
                
                // Draw text with appropriate color
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                
                // Use different text color for disabled buttons
                Color textColor = isEnabled() ? getForeground() : AppConfig.COLORS.get("text_muted");
                g2d.setColor(textColor);
                g2d.drawString(getText(), textX, textY);
            }
        };
        
        button.setFont(new Font("SF Pro Text", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(90, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add smooth hover transition effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.repaint();
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.repaint();
            }
        });
        
        return button;
    }
    
    /**
     * Setup keyboard shortcuts for better accessibility.
     */
    private void setupKeyboardShortcuts() {
        // Create input and action maps
        JRootPane rootPane = getRootPane();
        
        // Space bar to start/stop timer
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("SPACE"), "toggleTimer");
        rootPane.getActionMap().put("toggleTimer", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer.isRunning()) {
                    stopTimer();
                } else {
                    startTimer();
                }
            }
        });
        
        // R key to reset timer
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("R"), "resetTimer");
        rootPane.getActionMap().put("resetTimer", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTimer();
            }
        });
        
        // S key to open settings (when timer is not running)
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("S"), "openSettings");
        rootPane.getActionMap().put("openSettings", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!timer.isRunning()) {
                    openSettings();
                }
            }
        });
        
        // Escape key to stop timer
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("ESCAPE"), "stopTimer");
        rootPane.getActionMap().put("stopTimer", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer.isRunning()) {
                    stopTimer();
                }
            }
        });
    }
    
    /**
     * Setup system tray integration.
     */
    private void setupSystemTray() {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray is not supported");
            return;
        }
        
        systemTray = SystemTray.getSystemTray();
        
        // Create tray icon image (simple colored circle) 
        Image trayImage = createTrayIconImage();
        
        // Create popup menu
        PopupMenu popup = new PopupMenu();
        
        MenuItem showItem = new MenuItem("Show");
        showItem.addActionListener(e -> {
            setVisible(true);
            setState(JFrame.NORMAL);
            toFront();
        });
        
        MenuItem startStopItem = new MenuItem("Start/Stop");
        startStopItem.addActionListener(e -> {
            if (timer.isRunning()) {
                stopTimer();
            } else {
                startTimer();
            }
        });
        
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> {
            if (trayIcon != null) systemTray.remove(trayIcon);
            audioManager.cleanup();
            System.exit(0);
        });
        
        popup.add(showItem);
        popup.add(startStopItem);
        popup.addSeparator();
        popup.add(exitItem);
        
        // Create tray icon
        trayIcon = new TrayIcon(trayImage, "Good Day To Learn", popup);
        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener(e -> {
            setVisible(true);
            setState(JFrame.NORMAL);
            toFront();
        });
        
        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("Could not add system tray icon: " + e.getMessage());
        }
    }
    
    /**
     * Create a simple tray icon image.
     */
    private Image createTrayIconImage() {
        int size = 16;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw a simple colored circle
        g2d.setColor(AppConfig.COLORS.get("accent_primary"));
        g2d.fillOval(2, 2, size - 4, size - 4);
        
        g2d.dispose();
        return image;
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
            updateDisplay(); // Update button states immediately
        }
    }
    
    /**
     * Stop the timer.
     */
    private void stopTimer() {
        if (timer.isRunning()) {
            timer.stop();
            audioManager.stopMusic();
            updateDisplay();
        }
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
        SwingUtilities.invokeLater(() -> {
            updateTimerDisplay();
            // Ensure progress ring updates
            timerLabel.repaint();
        });
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
        updateCyclesDisplay();
        updateButtonStates();
    }
    
    /**
     * Update button states based on timer status.
     */
    private void updateButtonStates() {
        boolean isRunning = timer.isRunning();
        
        // Enable/disable buttons based on timer state
        startButton.setEnabled(!isRunning);
        stopButton.setEnabled(isRunning);
        resetButton.setEnabled(true); // Always enabled
        settingsButton.setEnabled(!isRunning); // Disable settings during timer
        
        // Update button text based on state
        if (!isRunning) {
            if (timer.isBreak()) {
                startButton.setText("Start Break");
            } else {
                startButton.setText("Start Work");
            }
        } else {
            startButton.setText("Running...");
        }
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
        
        // Update status color - minimal approach
        statusLabel.setForeground(AppConfig.COLORS.get("text_muted"));
    }
    
    /**
     * Update the completed cycles display.
     */
    private void updateCyclesDisplay() {
        int completedCycles = timer.getCompletedCycles();
        cyclesLabel.setText("Completed Cycles: " + completedCycles);
    }
}
