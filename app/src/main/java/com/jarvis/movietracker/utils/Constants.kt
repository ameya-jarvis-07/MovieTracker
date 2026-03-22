package com.jarvis.movietracker.utils

import com.jarvis.movietracker.BuildConfig

object Constants {
    const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
    val TMDB_API_KEY = BuildConfig.TMDB_API_KEY

    const val DEFAULT_PAGE = 1
    const val PAGE_SIZE = 20

    // Error Messages
    const val ERROR_NETWORK = "Network error. Please check your internet connection."
    const val ERROR_SERVER = "Server error. Please try again later."
    const val ERROR_GENERIC = "An error occurred. Please try again."
    const val ERROR_EMPTY_SEARCH = "Please enter a movie name to search."
    const val ERROR_NO_RESULTS = "No movies found."
}

