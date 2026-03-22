package com.jarvis.movietracker.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "reviews",
    foreignKeys = [
        ForeignKey(
            entity = WatchlistEntity::class,
            parentColumns = ["movieId"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ReviewEntity(
    @PrimaryKey
    val movieId: Int,
    val userRating: Float,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

