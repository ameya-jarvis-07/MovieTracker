# Movie Tracker App - Setup and Implementation Guide

## Overview
Movie Tracker is a complete Android application for searching, rating, and managing your favorite movies using The Movie Database (TMDB) API. The app features advanced search capabilities, a watchlist, personal ratings, and personalized recommendations based on your reviews.

## Project Architecture

### MVVM (Model-View-ViewModel)
The app follows the MVVM architecture pattern:
- **Model**: Data layer with repositories, entities, and API models
- **View**: UI layer with Fragments, Activities, and Adapters
- **ViewModel**: Business logic with LiveData observables

### Package Structure
```
com.jarvis.movietracker/
├── data/
│   ├── local/
│   │   ├── entities/
│   │   │   ├── WatchlistEntity.kt
│   │   │   └── ReviewEntity.kt
│   │   ├── dao/
│   │   │   ├── WatchlistDao.kt
│   │   │   └── ReviewDao.kt
│   │   └── MovieTrackerDatabase.kt
│   ├── remote/
│   │   ├── api/
│   │   │   ├── TmdbApiService.kt
│   │   │   └── RetrofitClient.kt
│   │   └── models/
│   │       ├── MovieDto.kt
│   │       └── SearchResponse.kt
│   └── repository/
│       ├── MovieRepository.kt
│       ├── WatchlistRepository.kt
│       └── ReviewRepository.kt
├── domain/
│   └── model/
│       └── Movie.kt
├── ui/
│   ├── activity/
│   │   └── MainActivity.kt
│   ├── fragment/
│   │   ├── SearchMovieFragment.kt
│   │   ├── WatchlistFragment.kt
│   │   ├── MovieDetailsFragment.kt
│   │   └── RecommendationsFragment.kt
│   ├── adapter/
│   │   ├── MovieAdapter.kt
│   │   ├── WatchlistAdapter.kt
│   │   └── ReviewAdapter.kt
│   └── viewmodel/
│       ├── SearchMovieViewModel.kt
│       ├── WatchlistViewModel.kt
│       ├── MovieDetailsViewModel.kt
│       └── RecommendationsViewModel.kt
└── utils/
    ├── Constants.kt
    ├── Extensions.kt
    └── Result.kt
```

## Prerequisites

1. **Android Studio**: Latest stable version
2. **Minimum API Level**: 24 (Android 7.0)
3. **Target API Level**: 35 (Android 15)
4. **JDK**: 11 or higher
5. **TMDB API Key**: Required for movie data

## Setup Instructions

### Step 1: Get TMDB API Key

1. Visit [The Movie Database (TMDB)](https://www.themoviedb.org/settings/api)
2. Create a free account
3. Request an API key
4. Copy your API key

### Step 2: Configure API Key

Open `app/build.gradle.kts` and replace `YOUR_API_KEY_HERE` with your actual TMDB API key:

```kotlin
buildTypes {
    debug {
        buildConfigField("String", "TMDB_API_KEY", "\"YOUR_API_KEY_HERE\"")
        buildConfigField("String", "TMDB_BASE_URL", "\"https://api.themoviedb.org/3/\"")
    }
    release {
        buildConfigField("String", "TMDB_API_KEY", "\"YOUR_API_KEY_HERE\"")
        buildConfigField("String", "TMDB_BASE_URL", "\"https://api.themoviedb.org/3/\"")
    }
}
```

### Step 3: Build and Run

1. Open the project in Android Studio
2. Sync Gradle files
3. Connect an Android device or start an emulator
4. Run the app: `./gradlew installDebug`

## Key Features

### 1. Search Movies
- Search for movies by title
- Pagination support for large result sets
- Display popular movies on load
- Real-time API integration

**Files**:
- `SearchMovieFragment.kt` - UI implementation
- `SearchMovieViewModel.kt` - Business logic
- `MovieRepository.kt` - Data fetching

### 2. Movie Details
- View comprehensive movie information
- Display ratings and release dates
- Load movie poster images
- Show recommendations based on the selected movie

**Files**:
- `MovieDetailsFragment.kt` - UI implementation
- `MovieDetailsViewModel.kt` - Business logic

### 3. Watchlist Management
- Add/remove movies to/from watchlist
- Mark movies as watched
- Local storage with Room Database
- Persistent storage across sessions

**Files**:
- `WatchlistFragment.kt` - UI implementation
- `WatchlistViewModel.kt` - Business logic
- `WatchlistEntity.kt` - Database model
- `WatchlistDao.kt` - Database access

### 4. Ratings and Reviews
- Rate movies with a 5-star system
- Add personal notes for each movie
- Local storage of reviews
- View all your reviews in the Recommendations tab

**Files**:
- `ReviewEntity.kt` - Database model
- `ReviewDao.kt` - Database access
- `ReviewRepository.kt` - Data access layer
- `ReviewAdapter.kt` - UI display

### 5. Recommendations
- View movies recommended based on TMDB API
- See your personal review history
- Track all movies you've rated

**Files**:
- `RecommendationsFragment.kt` - UI implementation
- `RecommendationsViewModel.kt` - Business logic

## Technologies Used

### Android Framework
- **AndroidX**: Core libraries and components
- **Lifecycle**: ViewModel, LiveData
- **Navigation**: Fragment navigation with safe args
- **Room**: Local database storage
- **RecyclerView**: List and grid displays

### Networking
- **Retrofit 2**: HTTP client for API calls
- **Gson**: JSON serialization/deserialization
- **OkHttp**: HTTP interceptor

### Asynchronous Programming
- **Coroutines**: Asynchronous operations
- **Flow**: Reactive data streams
- **LiveData**: Observable data holders

### Image Loading
- **Glide**: Efficient image loading and caching

### UI Components
- **Material Design 3**: Modern UI components
- **MaterialCardView**: Card layouts
- **ConstraintLayout**: Flexible layouts

## Database Schema

### Watchlist Table
```sql
CREATE TABLE watchlist (
    movieId INTEGER PRIMARY KEY,
    title TEXT NOT NULL,
    posterPath TEXT,
    voteAverage REAL NOT NULL,
    addedDate LONG NOT NULL,
    watched BOOLEAN NOT NULL
)
```

### Reviews Table
```sql
CREATE TABLE reviews (
    movieId INTEGER PRIMARY KEY,
    userRating REAL NOT NULL,
    notes TEXT NOT NULL,
    timestamp LONG NOT NULL,
    FOREIGN KEY (movieId) REFERENCES watchlist(movieId)
)
```

## API Endpoints Used

### Search Movies
```
GET /search/movie
Parameters: query, page, api_key
```

### Popular Movies
```
GET /movie/popular
Parameters: page, api_key
```

### Top Rated Movies
```
GET /movie/top_rated
Parameters: page, api_key
```

### Movie Details
```
GET /movie/{id}
Parameters: api_key
```

### Recommendations
```
GET /movie/{id}/recommendations
Parameters: page, api_key
```

## Error Handling

The app implements comprehensive error handling:
- Network errors with user-friendly messages
- API errors with detailed logging
- Empty result handling
- Loading states for all operations

Error messages are defined in:
- `Constants.kt` - Error message constants
- `Result.kt` - Error result wrapper class

## Performance Optimizations

1. **Image Caching**: Glide automatically caches images
2. **Database Queries**: Efficient Room queries with indices
3. **API Caching**: Retrofit with OkHttp caching
4. **Pagination**: Load data incrementally to reduce memory usage
5. **View Binding**: Efficient view reference management

## Testing

### Unit Tests (Optional)
Place unit tests in `app/src/test/java/`

### Instrumentation Tests
Place UI tests in `app/src/androidTest/java/`

## Future Enhancements

1. **User Authentication**: Firebase authentication
2. **Cloud Sync**: Backup watchlist to cloud
3. **Social Features**: Share reviews with friends
4. **Advanced Filtering**: Filter by genre, year, rating
5. **Offline Mode**: Full offline support
6. **Notifications**: Reminders for upcoming releases
7. **Dark Mode**: Complete dark mode support

## Troubleshooting

### API Key Issues
- Ensure API key is correctly placed in build.gradle.kts
- Verify API key is active on TMDB website
- Check internet connection

### Database Issues
- Clear app data: Settings → Apps → Movie Tracker → Storage → Clear Data
- Delete and reinstall the app

### Navigation Issues
- Ensure all fragments are declared in nav_graph.xml
- Check fragment IDs match navigation actions

### Image Loading Issues
- Verify poster paths are correct
- Check Glide is properly initialized
- Ensure internet permission is granted

## Dependencies

All dependencies are configured in `app/build.gradle.kts`:

```kotlin
// Core Android
androidx.core:core-ktx:1.12.0
androidx.appcompat:appcompat:1.6.1
material:material:1.11.0

// Lifecycle & ViewModel
androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0
androidx.lifecycle:lifecycle-livedata-ktx:2.7.0

// Room Database
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1

// Navigation
androidx.navigation:navigation-fragment-ktx:2.7.7
androidx.navigation:navigation-ui-ktx:2.7.7

// Networking
com.squareup.retrofit2:retrofit:2.9.0
com.squareup.retrofit2:converter-gson:2.9.0

// Coroutines
org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3

// Image Loading
com.github.bumptech.glide:glide:4.16.0

// RecyclerView
androidx.recyclerview:recyclerview:1.3.2
```

## Build Configuration

The app uses Gradle with Kotlin DSL for build configuration. Key settings:

- **Namespace**: com.jarvis.movietracker
- **compileSdk**: 35
- **minSdk**: 24
- **targetSdk**: 35
- **View Binding**: Enabled
- **Kapt**: Enabled for annotation processing

## ProGuard Rules

ProGuard rules are configured in `proguard-rules.pro`:
- Keep all model classes
- Keep Retrofit service interface
- Keep Gson annotations
- Keep LiveData/ViewModel classes

## License

This project is created for educational purposes.

## Support

For issues or questions:
1. Check the troubleshooting section
2. Review the code comments
3. Check TMDB API documentation
4. Consult Android documentation

---

**Version**: 1.0
**Last Updated**: 2026-03-22
**Created for**: Android Development Learning

