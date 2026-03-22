package com.jarvis.movietracker.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey
    val movieId: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Double,
    val addedDate: Long = System.currentTimeMillis(),
    val watched: Boolean = false
)

