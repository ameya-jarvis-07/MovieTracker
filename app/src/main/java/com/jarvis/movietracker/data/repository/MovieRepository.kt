package com.jarvis.movietracker.data.repository

import com.jarvis.movietracker.data.local.MovieTrackerDatabase
import com.jarvis.movietracker.data.remote.api.RetrofitClient
import com.jarvis.movietracker.domain.model.Movie
import com.jarvis.movietracker.utils.Constants
import com.jarvis.movietracker.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepository(private val database: MovieTrackerDatabase) {
    private val apiService = RetrofitClient.apiService

    fun searchMovies(query: String, page: Int = 1): Flow<Result<List<Movie>>> = flow {
        try {
            emit(Result.Loading)
            if (query.isEmpty()) {
                emit(Result.Error(Exception(Constants.ERROR_EMPTY_SEARCH)))
                return@flow
            }
            val response = apiService.searchMovies(query, page, Constants.TMDB_API_KEY)
            val movies = response.results.map { it.toDomainModel() }
            emit(Result.Success(movies))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun getPopularMovies(page: Int = 1): Flow<Result<List<Movie>>> = flow {
        try {
            emit(Result.Loading)
            val response = apiService.getPopularMovies(page, Constants.TMDB_API_KEY)
            val movies = response.results.map { it.toDomainModel() }
            emit(Result.Success(movies))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun getTopRatedMovies(page: Int = 1): Flow<Result<List<Movie>>> = flow {
        try {
            emit(Result.Loading)
            val response = apiService.getTopRatedMovies(page, Constants.TMDB_API_KEY)
            val movies = response.results.map { it.toDomainModel() }
            emit(Result.Success(movies))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun getMovieDetails(movieId: Int): Flow<Result<Movie>> = flow {
        try {
            emit(Result.Loading)
            val response = apiService.getMovieDetails(movieId, Constants.TMDB_API_KEY)
            emit(Result.Success(response.toDomainModel()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    fun getRecommendations(movieId: Int, page: Int = 1): Flow<Result<List<Movie>>> = flow {
        try {
            emit(Result.Loading)
            val response = apiService.getRecommendations(movieId, page, Constants.TMDB_API_KEY)
            val movies = response.results.map { it.toDomainModel() }
            emit(Result.Success(movies))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    private fun com.jarvis.movietracker.data.remote.models.MovieDto.toDomainModel() = Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        voteAverage = voteAverage,
        voteCount = voteCount,
        releaseDate = releaseDate,
        popularity = popularity,
        originalLanguage = originalLanguage
    )
}

