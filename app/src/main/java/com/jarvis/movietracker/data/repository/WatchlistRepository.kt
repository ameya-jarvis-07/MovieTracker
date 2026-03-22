package com.jarvis.movietracker.data.repository

import com.jarvis.movietracker.data.local.MovieTrackerDatabase
import com.jarvis.movietracker.data.local.entities.WatchlistEntity
import kotlinx.coroutines.flow.Flow

class WatchlistRepository(private val database: MovieTrackerDatabase) {
    private val watchlistDao = database.watchlistDao()

    fun getAllWatchlist(): Flow<List<WatchlistEntity>> = watchlistDao.getAllWatchlist()

    fun isInWatchlist(movieId: Int): Flow<Boolean> = watchlistDao.isInWatchlist(movieId)

    suspend fun addToWatchlist(watchlistEntity: WatchlistEntity) {
        watchlistDao.insertWatchlist(watchlistEntity)
    }

    suspend fun removeFromWatchlist(movieId: Int) {
        watchlistDao.deleteWatchlistByMovieId(movieId)
    }

    suspend fun updateWatchedStatus(movieId: Int, watched: Boolean) {
        watchlistDao.updateWatchedStatus(movieId, watched)
    }

    fun getWatchlistItem(movieId: Int): Flow<WatchlistEntity?> = watchlistDao.getWatchlistItem(movieId)
}

