import tkinter as tk
import time
import threading
import pygame

class PomodoroApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Pomodrip")
        self.root.geometry("300x150")

        self.time_left = 25 * 60  # 25 minutes
        self.running = False

        self.label = tk.Label(root, text="25:00", font=("Helvetica", 32))
        self.label.pack(pady=20)

        self.start_button = tk.Button(root, text="Start", command=self.start_timer)
        self.start_button.pack()

        self.reset_button = tk.Button(root, text="Reset", command=self.reset_timer)
        self.reset_button.pack()

        pygame.mixer.init()
        pygame.mixer.music.load("rain.mp3")  # Place a rain sound file here

    def start_timer(self):
        if not self.running:
            self.running = True
            pygame.mixer.music.play(-1)  # Loop forever
            threading.Thread(target=self.countdown).start()

    def reset_timer(self):
        self.running = False
        self.time_left = 25 * 60
        self.update_display()
        pygame.mixer.music.stop()

    def countdown(self):
        while self.time_left > 0 and self.running:
            mins, secs = divmod(self.time_left, 60)
            self.label.config(text=f"{mins:02d}:{secs:02d}")
            time.sleep(1)
            self.time_left -= 1
        if self.time_left == 0:
            self.label.config(text="Time's up!")
            pygame.mixer.music.stop()

    def update_display(self):
        mins, secs = divmod(self.time_left, 60)
        self.label.config(text=f"{mins:02d}:{secs:02d}")

if __name__ == "__main__":
    root = tk.Tk()
    app = PomodoroApp(root)
    root.mainloop()