package com.jarvis.movietracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jarvis.movietracker.data.local.entities.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(reviewEntity: ReviewEntity)

    @Delete
    suspend fun deleteReview(reviewEntity: ReviewEntity)

    @Query("SELECT * FROM reviews WHERE movieId = :movieId")
    fun getReviewByMovieId(movieId: Int): Flow<ReviewEntity?>

    @Query("SELECT * FROM reviews ORDER BY timestamp DESC")
    fun getAllReviews(): Flow<List<ReviewEntity>>

    @Query("UPDATE reviews SET userRating = :rating, notes = :notes WHERE movieId = :movieId")
    suspend fun updateReview(movieId: Int, rating: Float, notes: String)

    @Query("DELETE FROM reviews WHERE movieId = :movieId")
    suspend fun deleteReviewByMovieId(movieId: Int)
}

