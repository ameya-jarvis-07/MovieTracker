package com.jarvis.movietracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jarvis.movietracker.data.local.entities.WatchlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlist(watchlistEntity: WatchlistEntity)

    @Delete
    suspend fun deleteWatchlist(watchlistEntity: WatchlistEntity)

    @Query("SELECT * FROM watchlist ORDER BY addedDate DESC")
    fun getAllWatchlist(): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist WHERE movieId = :movieId")
    fun getWatchlistItem(movieId: Int): Flow<WatchlistEntity?>

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE movieId = :movieId)")
    fun isInWatchlist(movieId: Int): Flow<Boolean>

    @Query("DELETE FROM watchlist WHERE movieId = :movieId")
    suspend fun deleteWatchlistByMovieId(movieId: Int)

    @Query("UPDATE watchlist SET watched = :watched WHERE movieId = :movieId")
    suspend fun updateWatchedStatus(movieId: Int, watched: Boolean)
}

