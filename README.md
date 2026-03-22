# MovieTracker

Android app for browsing and tracking movies with the TMDB API. The project supports movie search, details, recommendations, a local watchlist, and personal ratings/review notes.

## Features
- Search TMDB movies from the main tab
- View movie details, poster art, overview, language, and rating
- Save movies to a local watchlist and mark them as watched
- Add a personal rating and notes for each movie
- Browse TMDB recommendations from the details screen
- Single-activity architecture with Navigation Component and ViewBinding
- Material 3 themed UI with a modern expressive style

## Tech Stack
- Kotlin, AndroidX, Material 3
- Retrofit + Gson for networking
- Coroutines for async work
- Room for local storage
- ViewModel + LiveData for UI state
- Glide for image loading
- Navigation Component for screen flow

## Prerequisites
- Android Studio Iguana or later
- JDK 11
- TMDB API key (create at https://developer.themoviedb.org/)

## Setup
1. Clone the repo and open in Android Studio.
2. Add your TMDB API key:
   - Edit `app/build.gradle.kts` and replace the `TMDB_API_KEY` values in both `debug` and `release` buildTypes with your key.
   - Avoid committing real keys; consider using a local `gradle.properties` entry and referencing it if you externalize secrets later.
3. Sync Gradle.
4. Run the app on an emulator or device.

## Build & Run
```powershell
./gradlew.bat assembleDebug
./gradlew.bat installDebug
```
Run from Android Studio to deploy to a device or emulator.

## Tests
```powershell
./gradlew.bat testDebug
./gradlew.bat connectedAndroidTest
```

## Project Structure
- `app/src/main/java/com/jarvis/movietracker` – features (ui, domain, data)
- `app/src/main/res` – layouts, navigation graph, drawables, values
- `app/build.gradle.kts` – Android config, TMDB keys, dependencies

## Notes
- API base URL is `https://api.themoviedb.org/3/`.
- The app now uses a Material 3 NoActionBar theme with a top toolbar and bottom navigation.
- Proguard/R8 is enabled for release builds; update `proguard-rules.pro` if you add new libraries.
