# Movie Tracker - Complete Installation & Testing Guide

## 🚀 Full Installation Steps

### Prerequisites Checklist
- ✅ Android Studio installed (version 2024.1 or higher)
- ✅ JDK 11 or higher installed
- ✅ Android SDK 24+ installed
- ✅ Internet connection
- ✅ TMDB API key ready

---

## Phase 1: Project Setup

### Step 1.1: Open Project in Android Studio
```
1. Launch Android Studio
2. Click "Open an Existing Project"
3. Navigate to D:\CODES\MovieTracker
4. Click Open
5. Wait for project indexing to complete
```

### Step 1.2: Sync Gradle
```
1. Go to File → Sync Now
   OR
2. Click "Sync Now" in the notification bar
3. Wait for sync to complete
4. Check for any errors in Gradle console
```

### Step 1.3: Configure API Key
Edit `app/build.gradle.kts`:

**Find this line** (around line 28):
```kotlin
buildConfigField("String", "TMDB_API_KEY", "\"YOUR_API_KEY_HERE\"")
```

**Replace with your key** (example):
```kotlin
buildConfigField("String", "TMDB_API_KEY", "\"a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6\"")
```

**Do the same for release build** (around line 35):
```kotlin
buildConfigField("String", "TMDB_API_KEY", "\"a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6\"")
```

### Step 1.4: Verify Dependencies
All dependencies should be in `app/build.gradle.kts`. If any are missing, they'll show as errors:
- Check for red squiggles in code
- Right-click → Run 'Quick-Fix'
- Select "Add dependency"

---

## Phase 2: Building the App

### Step 2.1: Clean Build
```bash
# In terminal from project root:
./gradlew clean

# On Windows (alternative):
gradlew.bat clean
```

**Expected output**:
```
BUILD SUCCESSFUL
```

### Step 2.2: Full Build
```bash
./gradlew build

# Or in Android Studio:
# Build → Make Project
# Or press Ctrl+F9
```

**Expected time**: 2-5 minutes first time
**Expected output**:
```
BUILD SUCCESSFUL in Xs
```

### Step 2.3: Handle Build Errors (if any)

**Error: "API key not found"**
- Solution: Verify BuildConfig in build.gradle.kts

**Error: "Missing dependency"**
- Solution: Run Gradle sync again
- Alternative: `./gradlew build --refresh-dependencies`

**Error: Kotlin compilation failed**
- Solution: Check for syntax errors in .kt files
- Verify plugin: `id("kotlin-kapt")` in plugins

---

## Phase 3: Setting Up Device/Emulator

### Option A: Using Physical Device

#### A1: Enable Developer Mode
```
1. Go to Settings
2. Tap "Build Number" 7 times
3. Back to Settings
4. Enable "Developer Options"
5. Enable "USB Debugging"
```

#### A2: Connect Device
```
1. Plug device into USB
2. Allow USB debugging when prompted
3. Device should appear in Android Studio
4. Check via: adb devices
```

### Option B: Using Android Emulator

#### B1: Create Emulator
```
In Android Studio:
1. Tools → Device Manager
2. Click "Create Device"
3. Select "Pixel 6" (or any Pixel)
4. Choose Android 12 or higher
5. Name it "MovieTracker"
6. Click Create
```

#### B2: Start Emulator
```
1. In Device Manager
2. Click Play (▶) button
3. Wait for boot (2-3 minutes)
4. Emulator appears in run configuration
```

---

## Phase 4: Running the App

### Step 4.1: Select Run Configuration
```
In Android Studio:
1. Click the Run Configuration dropdown (top toolbar)
2. Select "app"
3. Click Play button (▶) or press Shift+F10
```

### Step 4.2: Monitor Build Process
```
Gradle Console shows:
✓ Compiling kotlin
✓ Building APK
✓ Installing APK
✓ Launching Activity

This takes 1-3 minutes
```

### Step 4.3: Wait for App to Load
```
Expected sequence:
1. "Installing app..."
2. App launches on device
3. MainActivity appears
4. Search tab loads with popular movies
```

**If stuck on "Installing app..."**
- Wait longer (can take 5 minutes)
- Check device connection: `adb devices`
- Try: Build → Clean Project → Rebuild

---

## Phase 5: Initial Testing

### Test 1: Search Functionality ✅
```
Steps:
1. App loads on Search tab
2. See grid of popular movies (2 columns)
3. Each movie shows: title, rating, poster
4. Tap search bar at top
5. Type: "Avengers"
6. Press Enter
7. Results appear after 2-3 seconds

Expected: 20 Avengers movies shown
Pass: ✅ if movies load
Fail: ✅ if error message appears
```

### Test 2: Movie Details ✅
```
Steps:
1. From search results
2. Tap any movie
3. Details screen loads
4. Shows: poster, title, rating, overview
5. Can scroll down to see recommendations

Expected: Smooth transition, all info visible
Pass: ✅ if all visible
Fail: ✅ check logcat for errors
```

### Test 3: Add to Watchlist ✅
```
Steps:
1. On movie details screen
2. Scroll down to "Add to Watchlist" button
3. Tap button (changes to "Remove from Watchlist")
4. Go back to Search tab
5. Tap Watchlist tab at bottom
6. Movie should appear in list

Expected: Movie added to watchlist
Pass: ✅ if movie appears
Fail: ✅ check database access
```

### Test 4: Rate Movie ✅
```
Steps:
1. On movie details screen
2. Scroll down to "Your Rating"
3. Tap stars to set rating (e.g., 4 stars)
4. Type review notes in text box
5. Tap "Save Review"
6. Go to Recommendations tab
7. Your review should appear in list

Expected: Rating saved and visible
Pass: ✅ if review appears
Fail: ✅ check local database
```

### Test 5: Recommendations ✅
```
Steps:
1. Open any movie details
2. Scroll to "Recommendations" section
3. Should see related movies grid
4. Tap any recommendation
5. Loads that movie's details

Expected: Smooth navigation between recommendations
Pass: ✅ if transitions work
Fail: ✅ check navigation setup
```

---

## Phase 6: Debugging Common Issues

### Issue 1: "No Internet Connection"
```
Error: Network call fails
Check:
1. Device internet: Settings → WiFi → Connected
2. Emulator internet: Settings → System → Internet
3. TMDB API status: https://www.themoviedb.org/status
4. Firewall/Proxy blocking

Solution:
- Reconnect to WiFi
- Restart emulator
- Clear app cache: Settings → Apps → Movie Tracker → Storage → Clear Cache
- Restart device
```

### Issue 2: "Invalid API Key"
```
Error: API returns 401 Unauthorized
Check:
1. API key in build.gradle.kts correct
2. No extra spaces or quotes
3. API key is activated on TMDB website

Solution:
1. Verify key: https://www.themoviedb.org/settings/api
2. Update build.gradle.kts with correct key
3. Rebuild: ./gradlew clean build
4. Reinstall app
```

### Issue 3: "Watchlist Empty After Restart"
```
Error: Data doesn't persist
Check:
1. Room database initialized
2. Data inserted into database
3. Device storage available

Solution:
1. First run is normal - add a movie first
2. Check device storage: Settings → Storage
3. Uninstall and reinstall app
4. Check logcat for database errors
```

### Issue 4: "Images Not Loading"
```
Error: Movie posters show placeholder
Check:
1. Internet connection working
2. Glide dependency installed
3. Image URL format correct

Solution:
1. Check internet connection
2. Clear Glide cache: Settings → Apps → Movie Tracker → Storage → Clear Cache
3. Restart app
4. Check Glide logs in logcat
```

### Issue 5: "Crashes on Launch"
```
Error: App force closes immediately
Check:
1. Logcat output
2. API key configured
3. Manifest permissions

Solution:
1. View logcat: View → Tool Windows → Logcat
2. Look for red error lines
3. Common fixes:
   - Add internet permission
   - Update API key
   - Clean build cache
```

---

## Phase 7: View Logs for Debugging

### Accessing Logcat
```
In Android Studio:
1. View → Tool Windows → Logcat
2. Click on your device in dropdown
3. Filter by "movietracker" package
4. Look for errors/warnings
```

### Common Log Messages
```
✅ "MainActivity created" - App launching
✅ "Fetching movies..." - API call starting
✅ "Movies fetched successfully" - API success
❌ "API Error: 401" - Invalid key
❌ "Network error" - No internet
❌ "Database error" - Storage issue
```

### Search for Errors
```
In Logcat:
1. Click search box at top
2. Type: "ERROR" or "Exception"
3. Shows all error messages
4. Click to see full stack trace
```

---

## Phase 8: Advanced Testing

### Test: Network Throttling
```
To simulate slow network:
1. Android Studio → Device Manager
2. Click [...] menu on emulator
3. Extended Controls
4. Network tab
5. Set to "Slow 3G" or "Slow 4G"
6. Test app responsiveness
```

### Test: Offline Mode
```
To test without internet:
1. Settings → WiFi → Turn Off
2. Settings → Mobile Data → Turn Off (if device)
3. Try to search - should show error
4. Turn internet back on
5. Search works again
```

### Test: Multiple Users (Reset Data)
```
To clear app data and start fresh:
1. Settings → Apps → Movie Tracker
2. Tap "Storage" or "Storage & cache"
3. Clear Cache (keeps app installed)
4. OR Clear Storage (full reset)
5. Relaunch app

Note: Clears watchlist and reviews
```

---

## Phase 9: Performance Testing

### Monitor Memory Usage
```
In Android Studio:
1. View → Tool Windows → Profiler
2. Start recording
3. Use app normally (search, open details)
4. Stop recording
5. Check Memory graph for leaks
```

### Monitor Network Requests
```
In Android Studio:
1. View → Tool Windows → Profiler
2. Network tab
3. Use app features
4. Monitor request times
5. Check for failed requests
```

### Monitor CPU Usage
```
In Android Studio:
1. View → Tool Windows → Profiler
2. CPU tab
3. Use app normally
4. Should stay below 50%
5. No red flags = good performance
```

---

## Phase 10: Testing Checklist

Complete these tests to ensure everything works:

### Basic Functionality
- [ ] App launches without crashes
- [ ] Search tab loads with popular movies
- [ ] Can search for movies by name
- [ ] Results load with pagination
- [ ] Movie details screen opens
- [ ] Images load correctly
- [ ] Recommendations display

### Watchlist Feature
- [ ] Can add movie to watchlist
- [ ] Button changes text after adding
- [ ] Watchlist tab shows added movie
- [ ] Can mark movie as watched
- [ ] Can delete from watchlist
- [ ] Data persists after restart

### Rating & Review
- [ ] Can rate movie (1-5 stars)
- [ ] Can add review notes
- [ ] Can save review
- [ ] Recommendations tab shows reviews
- [ ] Reviews sort by date
- [ ] Can update review

### Navigation
- [ ] Bottom navigation works
- [ ] Can switch between tabs
- [ ] Movie details opens from search
- [ ] Movie details opens from watchlist
- [ ] Can navigate between recommendations
- [ ] Back button works properly

### Error Handling
- [ ] Search with no internet shows error
- [ ] Empty search shows message
- [ ] Invalid API shows error
- [ ] Network timeouts handled
- [ ] Database errors handled

---

## Quick Reference: Commands

```bash
# Clean and build
./gradlew clean build

# Build only
./gradlew build

# Install on device
./gradlew installDebug

# Run app
./gradlew run

# Run tests
./gradlew test

# View dependencies
./gradlew dependencies

# Show task list
./gradlew tasks

# Clean just
./gradlew clean

# Build and install
./gradlew build installDebug
```

---

## Uninstalling & Fresh Install

```bash
# Uninstall
./gradlew uninstallDebug

# Clean everything
./gradlew clean

# Full fresh build and install
./gradlew clean build installDebug

# OR in Android Studio:
Build → Clean Project
Build → Rebuild Project
Run → Run 'app'
```

---

## Troubleshooting Summary

| Issue | Quick Fix |
|-------|-----------|
| App won't install | `./gradlew clean build installDebug` |
| Gradle sync fails | File → Sync Now → Invalidate Cache |
| API key error | Update build.gradle.kts with correct key |
| No internet in emulator | Enable WiFi in emulator settings |
| Crashes on launch | Check logcat for errors |
| Images not loading | Clear cache and restart |
| Watchlist empty | Add first movie to initialize |
| Search returns nothing | Verify API key is active |
| App too slow | Check network throttling is off |

---

## Success Indicators ✅

You've successfully installed and tested the app when:

1. ✅ App launches without errors
2. ✅ Popular movies load immediately
3. ✅ Search returns results in <3 seconds
4. ✅ Movie details load with image
5. ✅ Can add to watchlist
6. ✅ Can rate and review
7. ✅ Recommendations display
8. ✅ Data persists after restart
9. ✅ No crashes when navigating
10. ✅ Error messages are user-friendly

---

## Next Steps After Installation

1. ✅ Run all tests from Phase 5
2. ✅ Try all features from Phase 8
3. ✅ Check performance from Phase 9
4. ✅ Complete testing checklist
5. ✅ Explore the code
6. ✅ Add new features
7. ✅ Customize UI
8. ✅ Optimize performance
9. ✅ Deploy to Play Store (optional)

---

## Need Help?

1. **Check README.md** - Full documentation
2. **Check QUICKSTART.md** - Quick setup
3. **Check API_CONFIGURATION.md** - API setup
4. **Check logcat** - Error messages
5. **Check code comments** - Inline documentation
6. **Check TMDB docs** - API documentation

---

## System Requirements

| Component | Minimum | Recommended |
|-----------|---------|-------------|
| Android API | 24 | 35 |
| RAM | 2 GB | 4 GB |
| Storage | 200 MB | 500 MB |
| Internet | 2 Mbps | 10 Mbps |
| Processor | ARM v7 | ARM v8 |

---

**Congratulations! Your Movie Tracker app is ready to use! 🎬**

For detailed information, see README.md and other documentation files.

