package com.jarvis.movietracker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.movietracker.data.repository.MovieRepository
import com.jarvis.movietracker.domain.model.Movie
import com.jarvis.movietracker.utils.Result
import kotlinx.coroutines.launch

class SearchMovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private val _searchResults = MutableLiveData<Result<List<Movie>>>()
    val searchResults: LiveData<Result<List<Movie>>> = _searchResults

    private val _currentPage = MutableLiveData(1)
    val currentPage: LiveData<Int> = _currentPage

    private val _allMovies = MutableLiveData<List<Movie>>(emptyList())
    val allMovies: LiveData<List<Movie>> = _allMovies

    fun searchMovies(query: String, page: Int = 1) {
        viewModelScope.launch {
            movieRepository.searchMovies(query, page).collect { result ->
                _searchResults.value = result
                if (result is Result.Success) {
                    if (page == 1) {
                        _allMovies.value = result.data
                    } else {
                        _allMovies.value = (_allMovies.value ?: emptyList()) + result.data
                    }
                    _currentPage.value = page
                }
            }
        }
    }

    fun getPopularMovies(page: Int = 1) {
        viewModelScope.launch {
            movieRepository.getPopularMovies(page).collect { result ->
                _searchResults.value = result
                if (result is Result.Success) {
                    if (page == 1) {
                        _allMovies.value = result.data
                    } else {
                        _allMovies.value = (_allMovies.value ?: emptyList()) + result.data
                    }
                    _currentPage.value = page
                }
            }
        }
    }

    fun loadMore(query: String? = null) {
        val nextPage = (_currentPage.value ?: 1) + 1
        if (query.isNullOrEmpty()) {
            getPopularMovies(nextPage)
        } else {
            searchMovies(query, nextPage)
        }
    }
}

