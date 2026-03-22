package com.jarvis.movietracker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.movietracker.data.repository.MovieRepository
import com.jarvis.movietracker.data.repository.ReviewRepository
import com.jarvis.movietracker.data.repository.WatchlistRepository
import com.jarvis.movietracker.data.local.entities.ReviewEntity
import com.jarvis.movietracker.data.local.entities.WatchlistEntity
import com.jarvis.movietracker.domain.model.Movie
import com.jarvis.movietracker.utils.Result
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val movieRepository: MovieRepository,
    private val watchlistRepository: WatchlistRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {
    private val _movieDetails = MutableLiveData<Result<Movie>>()
    val movieDetails: LiveData<Result<Movie>> = _movieDetails

    private val _recommendations = MutableLiveData<Result<List<Movie>>>()
    val recommendations: LiveData<Result<List<Movie>>> = _recommendations

    private val _isInWatchlist = MutableLiveData(false)
    val isInWatchlist: LiveData<Boolean> = _isInWatchlist

    private val _review = MutableLiveData<ReviewEntity?>()
    val review: LiveData<ReviewEntity?> = _review

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            movieRepository.getMovieDetails(movieId).collect { result ->
                _movieDetails.value = result
            }
        }
    }

    fun getRecommendations(movieId: Int) {
        viewModelScope.launch {
            movieRepository.getRecommendations(movieId).collect { result ->
                _recommendations.value = result
            }
        }
    }

    fun checkWatchlistStatus(movieId: Int) {
        viewModelScope.launch {
            watchlistRepository.isInWatchlist(movieId).collect { inWatchlist ->
                _isInWatchlist.value = inWatchlist
            }
        }
    }

    fun addToWatchlist(movie: Movie) {
        viewModelScope.launch {
            val watchlistEntity = WatchlistEntity(
                movieId = movie.id,
                title = movie.title,
                posterPath = movie.posterPath,
                voteAverage = movie.voteAverage
            )
            watchlistRepository.addToWatchlist(watchlistEntity)
            _isInWatchlist.value = true
        }
    }

    fun removeFromWatchlist(movieId: Int) {
        viewModelScope.launch {
            watchlistRepository.removeFromWatchlist(movieId)
            _isInWatchlist.value = false
        }
    }

    fun getReview(movieId: Int) {
        viewModelScope.launch {
            reviewRepository.getReviewByMovieId(movieId).collect { review ->
                _review.value = review
            }
        }
    }

    fun saveReview(movieId: Int, rating: Float, notes: String) {
        viewModelScope.launch {
            val review = ReviewEntity(
                movieId = movieId,
                userRating = rating,
                notes = notes
            )
            reviewRepository.addReview(review)
            _review.value = review
        }
    }

    fun updateReview(movieId: Int, rating: Float, notes: String) {
        viewModelScope.launch {
            reviewRepository.updateReview(movieId, rating, notes)
            _review.value = ReviewEntity(movieId, rating, notes)
        }
    }
}

