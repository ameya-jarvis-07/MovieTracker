# TMDB API Configuration Guide

## Getting Your API Key

### Step 1: Create a TMDB Account
1. Visit https://www.themoviedb.org/
2. Click "Sign Up" in top right
3. Create a new account or sign in with Google/GitHub
4. Verify your email

### Step 2: Request API Key
1. Go to https://www.themoviedb.org/settings/api
2. Click "Create" in the API section
3. Select "Developer" option
4. Accept terms and conditions
5. Fill in your application details:
   - **Application Name**: Movie Tracker
   - **Application URL**: Your website or GitHub profile
   - **Application Summary**: Personal movie tracking app
   - **API Usage**: Education/Personal Use

### Step 3: Copy Your API Key
1. Your API key will be displayed on the settings page
2. Look for "API Key (v3 auth)"
3. Copy this key - you'll need it

---

## Configuring the API Key in the App

### Method 1: Build Configuration (RECOMMENDED)

Edit `app/build.gradle.kts`:

```kotlin
android {
    // ... other config ...
    
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
}
```

**Example**:
```kotlin
buildConfigField("String", "TMDB_API_KEY", "\"a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6\"")
```

### Method 2: Local Properties File

Create or edit `local.properties`:
```properties
tmdb.api.key=YOUR_API_KEY_HERE
```

Then in `build.gradle.kts`:
```kotlin
buildTypes {
    debug {
        buildConfigField("String", "TMDB_API_KEY", "\"${project.findProperty("tmdb.api.key")}\"")
    }
}
```

---

## API Key Security

### Development Security
- ✅ Store in `build.gradle.kts` for development
- ✅ Use `BuildConfig` for runtime access
- ✅ Never commit API key to version control

### Production Security
- ⚠️ Use backend server as proxy
- ⚠️ Implement API authentication
- ⚠️ Add ProGuard rules to obfuscate API key
- ⚠️ Monitor API usage in TMDB console

### Git Security
Add to `.gitignore`:
```
# Gradle
.gradle/
build/
gradle.properties
local.properties

# API Keys
*.properties
**/*BuildConfig*
```

---

## API Endpoints Available

### 1. Search Movies
```
GET https://api.themoviedb.org/3/search/movie
Parameters:
  - query (required): Movie title to search
  - page (optional): Page number (default: 1)
  - api_key (required): Your API key

Example:
GET https://api.themoviedb.org/3/search/movie?query=avengers&api_key=YOUR_KEY&page=1
```

### 2. Popular Movies
```
GET https://api.themoviedb.org/3/movie/popular
Parameters:
  - page (optional): Page number (default: 1)
  - api_key (required): Your API key

Example:
GET https://api.themoviedb.org/3/movie/popular?api_key=YOUR_KEY&page=1
```

### 3. Top Rated Movies
```
GET https://api.themoviedb.org/3/movie/top_rated
Parameters:
  - page (optional): Page number (default: 1)
  - api_key (required): Your API key

Example:
GET https://api.themoviedb.org/3/movie/top_rated?api_key=YOUR_KEY&page=1
```

### 4. Movie Details
```
GET https://api.themoviedb.org/3/movie/{movie_id}
Parameters:
  - movie_id (required): TMDB movie ID
  - api_key (required): Your API key

Example:
GET https://api.themoviedb.org/3/movie/550?api_key=YOUR_KEY
```

### 5. Recommendations
```
GET https://api.themoviedb.org/3/movie/{movie_id}/recommendations
Parameters:
  - movie_id (required): TMDB movie ID
  - page (optional): Page number (default: 1)
  - api_key (required): Your API key

Example:
GET https://api.themoviedb.org/3/movie/550/recommendations?api_key=YOUR_KEY&page=1
```

---

## API Response Examples

### Search Response
```json
{
  "page": 1,
  "results": [
    {
      "id": 550,
      "title": "Fight Club",
      "overview": "An insomniac office worker...",
      "poster_path": "/pB8BM8dQwzsxjlkd815_7d4b2r4.jpg",
      "backdrop_path": "/tsKy2UwVycFFFAucAdbwI0JjE7n.jpg",
      "vote_average": 8.8,
      "vote_count": 28000,
      "release_date": "1999-10-15",
      "popularity": 61.416,
      "original_language": "en"
    }
  ],
  "total_pages": 45,
  "total_results": 892
}
```

### Movie Details Response
```json
{
  "id": 550,
  "title": "Fight Club",
  "overview": "An insomniac office worker...",
  "poster_path": "/pB8BM8dQwzsxjlkd815_7d4b2r4.jpg",
  "backdrop_path": "/tsKy2UwVycFFFAucAdbwI0JjE7n.jpg",
  "vote_average": 8.8,
  "vote_count": 28000,
  "release_date": "1999-10-15",
  "popularity": 61.416,
  "original_language": "en"
}
```

---

## Image URLs

### Poster Images
Base URL: `https://image.tmdb.org/t/p/w500`

Full URL:
```
https://image.tmdb.org/t/p/w500/pB8BM8dQwzsxjlkd815_7d4b2r4.jpg
```

**Available Sizes**:
- `w92` - 92px width
- `w154` - 154px width
- `w185` - 185px width (good for mobile)
- `w342` - 342px width (recommended for screens)
- `w500` - 500px width
- `w780` - 780px width
- `original` - Full resolution

### Used in App
```
w500 - Used for grid displays and details
```

---

## Code Implementation

### In Constants.kt
```kotlin
object Constants {
    const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
    val TMDB_API_KEY = BuildConfig.TMDB_API_KEY
}
```

### In RetrofitClient.kt
```kotlin
object RetrofitClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    val apiService: TmdbApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApiService::class.java)
    }
}
```

### In TmdbApiService.kt
```kotlin
interface TmdbApiService {
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String
    ): SearchResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieDto
}
```

---

## API Limits & Rate Limiting

### Free Tier Limits
- **Requests**: 40 requests per 10 seconds
- **Monthly**: Up to 1000 requests/day
- **Features**: All available

### Best Practices
- ✅ Cache responses locally
- ✅ Use pagination (20 results per page)
- ✅ Implement retry logic
- ✅ Add request delays

### Monitoring Usage
1. Visit https://www.themoviedb.org/settings/api
2. Check "Usage Stats" section
3. Monitor daily requests
4. Upgrade if needed

---

## Troubleshooting

### "Invalid API Key"
**Solution**:
1. Verify key in build.gradle.kts
2. Check for extra spaces
3. Generate new key if needed
4. Clean and rebuild: `./gradlew clean build`

### "Unauthorized"
**Solution**:
1. Verify API key is activated
2. Check key format (should be alphanumeric)
3. Wait a few minutes after key creation
4. Try different endpoint

### "Too Many Requests"
**Solution**:
1. Wait 10 seconds
2. Implement exponential backoff
3. Check rate limits in TMDB dashboard
4. Consider upgrading account

### Empty Results
**Solution**:
1. Verify search query is valid
2. Check movie exists in TMDB database
3. Try different search terms
4. Check pagination parameters

### Image URLs Not Loading
**Solution**:
1. Verify base URL: `https://image.tmdb.org/t/p/`
2. Check poster_path is not null
3. Use correct image size (w500)
4. Check internet connection

---

## Testing the API

### Using cURL
```bash
# Search for movies
curl "https://api.themoviedb.org/3/search/movie?query=fight%20club&api_key=YOUR_KEY"

# Get popular movies
curl "https://api.themoviedb.org/3/movie/popular?api_key=YOUR_KEY"

# Get movie details
curl "https://api.themoviedb.org/3/movie/550?api_key=YOUR_KEY"
```

### Using Postman
1. Import TMDB collection
2. Set `api_key` variable
3. Test endpoints
4. View response formatting

### In Android
The app automatically handles API calls through `MovieRepository`:
```kotlin
viewModel.searchMovies("avengers")
// Makes API call automatically
// Updates UI with results
```

---

## API Documentation Links

- **Official Docs**: https://developer.themoviedb.org/docs
- **API Reference**: https://www.themoviedb.org/talk/categories/5047958519c29526b40002d1
- **Status**: https://www.themoviedb.org/status
- **Support Forum**: https://www.themoviedb.org/talk

---

## Environment Variables (Alternative Setup)

### Create .env file
```
TMDB_API_KEY=your_api_key_here
TMDB_BASE_URL=https://api.themoviedb.org/3/
```

### Use in build.gradle.kts
```kotlin
buildConfigField("String", "TMDB_API_KEY", "\"${System.getenv("TMDB_API_KEY")}\"")
```

---

## Quick Reference

| Item | Value |
|------|-------|
| Base URL | https://api.themoviedb.org/3/ |
| Image Base URL | https://image.tmdb.org/t/p/w500 |
| Rate Limit | 40 requests/10 seconds |
| Response Format | JSON |
| Authentication | API Key in query parameter |
| SSL | Required |

---

## Next Steps

1. ✅ Create TMDB account
2. ✅ Get API key
3. ✅ Configure in build.gradle.kts
4. ✅ Build and run app
5. ✅ Test search functionality
6. ✅ Monitor API usage

---

**You're all set! Start building with TMDB API! 🎬**

