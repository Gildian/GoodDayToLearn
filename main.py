#!/usr/bin/env python3
"""
Good Day To Learn - A Pomodoro Timer Application

Entry point for the application.
"""

import tkinter as tk
from src.main_window import GoodDayToLearnApp

def main():
    """Main entry point for the application."""
    root = tk.Tk()
    app = GoodDayToLearnApp(root)
    root.mainloop()

if __name__ == "__main__":
    main()
