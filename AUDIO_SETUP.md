# Audio File Instructions

## 🎵 Setting Up Audio

Your GoodDayToLearn app requires a `rain.mp3` file for background audio during work sessions.

### Option 1: Use Your Own Audio File
1. Find or download a rain sound file (or any relaxing audio)
2. Convert it to MP3 format if needed
3. Rename it to `rain.mp3`
4. Place it in the project root directory

### Option 2: Download a Free Rain Sound
**Recommended free sources:**
- **Pixabay**: https://pixabay.com/sound-effects/search/rain/
- **Freesound**: https://freesound.org/search/?q=rain
- **Zapsplat** (requires free account): https://zapsplat.com

### Option 3: Record Your Own
- Use any audio recording app
- Record ambient sounds (rain, white noise, etc.)
- Export as MP3
- Name it `rain.mp3`

### Option 4: Use a Silent File (No Audio)
If you don't want audio, create an empty audio file:
```bash
# macOS/Linux
touch rain.mp3

# Or download a short silent MP3 file
```

## 📋 Requirements:
- **File name**: Must be exactly `rain.mp3`
- **Format**: MP3 audio file
- **Location**: Project root directory (same folder as GoodDayToLearn.py)
- **Size**: Keep under 10MB for better performance

## 🔧 Why This File Isn't in Git:
- Audio files are typically large (this one was 3.4MB)
- Git repositories should focus on code, not binary assets
- Users can choose their own preferred audio

Once you have your `rain.mp3` file in place, the app will work perfectly with background audio during work sessions!
