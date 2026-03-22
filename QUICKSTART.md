# Quick Start Guide - Movie Tracker App

## 5-Minute Setup

### 1. Get Your API Key (2 minutes)
- Go to https://www.themoviedb.org/settings/api
- Sign up for a free account
- Request an API key
- Copy the key

### 2. Update the Build Configuration (1 minute)
Open `app/build.gradle.kts` and find:
```kotlin
buildConfigField("String", "TMDB_API_KEY", "\"YOUR_API_KEY_HERE\"")
```
Replace `YOUR_API_KEY_HERE` with your actual API key.

### 3. Run the App (2 minutes)
```bash
# From the project root directory
./gradlew installDebug

# Or in Android Studio: Click Run → Run 'app'
```

## Using the App

### Search for Movies
1. Tap the **Search** tab
2. Use the search bar to find movies
3. Tap on any movie to view details
4. Swipe up to see recommendations

### Add to Watchlist
1. View a movie's details
2. Tap **Add to Watchlist** button
3. Go to **Watchlist** tab to see your list
4. Check the box to mark as watched
5. Tap Delete to remove from list

### Rate and Review
1. Open a movie's details
2. Scroll down to "Your Rating"
3. Set a star rating (0-5 stars)
4. Add optional notes
5. Tap **Save Review**

### View Recommendations
1. Tap the **Recommendations** tab
2. See all your reviews and ratings
3. Movies are sorted by most recent review

## Project Structure

```
MovieTracker/
├── app/
│   ├── build.gradle.kts (← Update API key here!)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/jarvis/movietracker/
│   │   │   │   ├── data/          (Repositories & Database)
│   │   │   │   ├── domain/        (Business Models)
│   │   │   │   ├── ui/            (Fragments & Activities)
│   │   │   │   └── utils/         (Helpers & Constants)
│   │   │   ├── res/
│   │   │   │   ├── layout/        (XML UI layouts)
│   │   │   │   ├── navigation/    (nav_graph.xml)
│   │   │   │   └── values/        (Strings & Colors)
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                   (Unit tests)
│   │   └── androidTest/            (UI tests)
│   └── proguard-rules.pro
├── build.gradle.kts                (Root build config)
└── README.md                        (Full documentation)
```

## Key Classes

### Search
- **SearchMovieViewModel.kt** - Handles search logic
- **SearchMovieFragment.kt** - UI for search
- **MovieAdapter.kt** - Displays movie grid

### Watchlist
- **WatchlistViewModel.kt** - Manages watchlist
- **WatchlistFragment.kt** - UI for watchlist
- **WatchlistAdapter.kt** - Displays watchlist items

### Reviews
- **MovieDetailsViewModel.kt** - Handles movie details
- **MovieDetailsFragment.kt** - Shows movie info & reviews
- **ReviewAdapter.kt** - Displays reviews

### Data Layer
- **MovieRepository.kt** - API calls
- **WatchlistRepository.kt** - Local watchlist storage
- **ReviewRepository.kt** - Local reviews storage
- **MovieTrackerDatabase.kt** - Room database

## Common Issues & Solutions

### Issue: "API key not valid"
**Solution**: 
- Verify key in build.gradle.kts
- Clean and rebuild: `./gradlew clean build`

### Issue: App crashes on search
**Solution**:
- Check internet connection
- Verify API key is active
- Check logcat for errors: `./gradlew logcat`

### Issue: Watchlist empty after restart
**Solution**:
- First time watchlist initialization is normal
- Add a movie to create database
- Data persists after first entry

### Issue: Images not loading
**Solution**:
- Ensure internet permission granted
- Check Glide dependency is installed
- Clear app cache: Settings → Apps → Movie Tracker → Storage → Clear Cache

## Architecture Overview

```
API (TMDB) ─────┐
                ├─→ RetrofitClient ─→ TmdbApiService
                │
                ├─→ MovieRepository
                │                    ┌─→ SearchMovieViewModel ─→ SearchMovieFragment
Data Models ────┤                    │
                ├─→ WatchlistRepository ─→ WatchlistViewModel ─→ WatchlistFragment
                │                    │
Room Database ──┤                    ├─→ MovieDetailsViewModel ─→ MovieDetailsFragment
                │                    │
                └─→ ReviewRepository ─→ RecommendationsViewModel ─→ RecommendationsFragment
```

## Database

### Tables

**Watchlist**
```
movieId (PK) | title | posterPath | voteAverage | addedDate | watched
```

**Reviews**
```
movieId (PK) | userRating | notes | timestamp
```

## Navigation Flow

```
MainActivity
├── SearchMovieFragment
│   └── MovieDetailsFragment
├── WatchlistFragment
│   └── MovieDetailsFragment
└── RecommendationsFragment
```

## Gradle Commands

```bash
# Build
./gradlew build

# Run on device
./gradlew installDebug

# Run tests
./gradlew test

# Clean build
./gradlew clean build

# Check dependencies
./gradlew dependencies
```

## Debugging

### View Logs
```bash
./gradlew logcat
```

### Enable Debug Logging
In Constants.kt, log API calls:
```kotlin
// Add logging interceptor to RetrofitClient
val logging = HttpLoggingInterceptor()
logging.level = HttpLoggingInterceptor.Level.BODY
```

## Performance Tips

1. **Pagination**: Search loads 20 movies per page
2. **Image Caching**: Glide caches poster images locally
3. **Database Queries**: Room queries are optimized with indices
4. **Network**: Retrofit with OkHttp caching

## Code Examples

### Making an API Call
```kotlin
// In Repository
fun searchMovies(query: String): Flow<Result<List<Movie>>> = flow {
    emit(Result.Loading)
    try {
        val response = apiService.searchMovies(query, 1, Constants.TMDB_API_KEY)
        emit(Result.Success(response.results.map { it.toDomainModel() }))
    } catch (e: Exception) {
        emit(Result.Error(e))
    }
}
```

### Observing Data in Fragment
```kotlin
viewModel.searchResults.observe(viewLifecycleOwner) { result ->
    when (result) {
        is Result.Loading -> showProgress()
        is Result.Success -> adapter.setMovies(result.data)
        is Result.Error -> showError(result.exception.message)
    }
}
```

### Room Database Query
```kotlin
@Query("SELECT * FROM watchlist ORDER BY addedDate DESC")
fun getAllWatchlist(): Flow<List<WatchlistEntity>>
```

## Next Steps

1. ✅ Get API key
2. ✅ Configure build.gradle.kts
3. ✅ Run the app
4. 🔄 Add a movie to watchlist
5. 🔄 Rate a movie
6. 🔄 Explore recommendations

## Learning Resources

- [Android Official Docs](https://developer.android.com/docs)
- [TMDB API Docs](https://developer.themoviedb.org/docs)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [Jetpack Navigation](https://developer.android.com/jetpack/androidx/releases/navigation)

## API Reference (Quick)

| Endpoint | Purpose |
|----------|---------|
| `/search/movie` | Search movies by query |
| `/movie/popular` | Get popular movies |
| `/movie/top_rated` | Get top rated movies |
| `/movie/{id}` | Get movie details |
| `/movie/{id}/recommendations` | Get recommendations |

## Support

Stuck? Try:
1. Check logcat for error messages
2. Verify API key is valid
3. Check internet connection
4. Read README.md for full documentation
5. Check Android Studio error messages

---

**Happy Coding! 🎬**

