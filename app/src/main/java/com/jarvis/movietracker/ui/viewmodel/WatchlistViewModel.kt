package com.jarvis.movietracker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.movietracker.data.repository.WatchlistRepository
import com.jarvis.movietracker.data.local.entities.WatchlistEntity
import kotlinx.coroutines.launch

class WatchlistViewModel(private val watchlistRepository: WatchlistRepository) : ViewModel() {
    private val _watchlist = MutableLiveData<List<WatchlistEntity>>()
    val watchlist: LiveData<List<WatchlistEntity>> = _watchlist

    init {
        loadWatchlist()
    }

    fun loadWatchlist() {
        viewModelScope.launch {
            watchlistRepository.getAllWatchlist().collect { movies ->
                _watchlist.value = movies
            }
        }
    }

    fun addToWatchlist(watchlistEntity: WatchlistEntity) {
        viewModelScope.launch {
            watchlistRepository.addToWatchlist(watchlistEntity)
        }
    }

    fun removeFromWatchlist(movieId: Int) {
        viewModelScope.launch {
            watchlistRepository.removeFromWatchlist(movieId)
        }
    }

    fun updateWatchedStatus(movieId: Int, watched: Boolean) {
        viewModelScope.launch {
            watchlistRepository.updateWatchedStatus(movieId, watched)
        }
    }
}

