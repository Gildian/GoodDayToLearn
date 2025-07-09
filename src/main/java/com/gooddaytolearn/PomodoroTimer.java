package com.gooddaytolearn;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 * Handles Pomodoro timer logic and state management.
 */
public class PomodoroTimer {
    
    private int workTime;
    private int shortBreakTime;
    private int longBreakTime;
    
    private int timeLeft;
    private int currentInterval; // Track current interval (0-3)
    private boolean isBreak;
    private boolean running;
    
    // Callbacks
    private Runnable onTimerComplete;
    private Consumer<Integer> onTimerUpdate;
    
    // Timer
    private Timer timer;
    
    /**
     * Initialize the Pomodoro timer.
     * 
     * @param onTimerComplete Callback function called when timer completes
     * @param onTimerUpdate Callback function called every second during countdown
     */
    public PomodoroTimer(Runnable onTimerComplete, Consumer<Integer> onTimerUpdate) {
        this.workTime = AppConfig.DEFAULT_WORK_TIME;
        this.shortBreakTime = AppConfig.DEFAULT_SHORT_BREAK;
        this.longBreakTime = AppConfig.DEFAULT_LONG_BREAK;
        
        this.timeLeft = this.workTime;
        this.currentInterval = 0;
        this.isBreak = false;
        this.running = false;
        
        this.onTimerComplete = onTimerComplete;
        this.onTimerUpdate = onTimerUpdate;
    }
    
    /**
     * Start the timer.
     */
    public void start() {
        if (!running) {
            running = true;
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    countdown();
                }
            }, 0, 1000); // Run every second
        }
    }
    
    /**
     * Stop the timer.
     */
    public void stop() {
        running = false;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    
    /**
     * Reset the timer to initial state.
     */
    public void reset() {
        running = false;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        currentInterval = 0;
        isBreak = false;
        timeLeft = workTime;
    }
    
    /**
     * Internal countdown logic.
     */
    private void countdown() {
        if (timeLeft > 0 && running) {
            if (onTimerUpdate != null) {
                onTimerUpdate.accept(timeLeft);
            }
            timeLeft--;
        } else if (timeLeft == 0 && running) {
            handleTimerComplete();
        }
    }
    
    /**
     * Handle timer completion and transition to next phase.
     */
    private void handleTimerComplete() {
        if (!isBreak) {
            // Just finished a work interval
            currentInterval++;
            
            if (currentInterval >= 4) {
                // Time for long break after 4 intervals
                isBreak = true;
                timeLeft = longBreakTime;
                currentInterval = 0; // Reset interval counter
            } else {
                // Time for short break
                isBreak = true;
                timeLeft = shortBreakTime;
            }
        } else {
            // Just finished a break, back to work
            isBreak = false;
            timeLeft = workTime;
        }
        
        running = false;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        
        if (onTimerComplete != null) {
            onTimerComplete.run();
        }
    }
    
    /**
     * Get formatted time string for display.
     */
    public String getTimeDisplay() {
        int mins = timeLeft / 60;
        int secs = timeLeft % 60;
        return String.format("%02d:%02d", mins, secs);
    }
    
    /**
     * Get current status text.
     */
    public String getStatusText() {
        if (isBreak) {
            if (currentInterval == 0) {
                return "Long Break Time";
            } else {
                return "Short Break Time";
            }
        } else {
            return String.format("Work Time - Interval %d/4", currentInterval + 1);
        }
    }
    
    /**
     * Get phase transition message.
     */
    public String getPhaseMessage() {
        if (isBreak) {
            if (currentInterval == 0) {
                return "Long Break!";
            } else {
                return "Short Break!";
            }
        } else {
            return "Back to Work!";
        }
    }
    
    // Getters and setters
    public boolean isRunning() {
        return running;
    }
    
    public boolean isBreak() {
        return isBreak;
    }
    
    public void setWorkTime(int minutes) {
        this.workTime = minutes * 60;
        if (!isBreak && !running) {
            this.timeLeft = this.workTime;
        }
    }
    
    public void setShortBreakTime(int minutes) {
        this.shortBreakTime = minutes * 60;
    }
    
    public void setLongBreakTime(int minutes) {
        this.longBreakTime = minutes * 60;
    }
    
    public int getWorkTimeMinutes() {
        return workTime / 60;
    }
    
    public int getShortBreakMinutes() {
        return shortBreakTime / 60;
    }
    
    public int getLongBreakMinutes() {
        return longBreakTime / 60;
    }
}
