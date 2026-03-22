package com.jarvis.movietracker.data.repository

import com.jarvis.movietracker.data.local.MovieTrackerDatabase
import com.jarvis.movietracker.data.local.entities.ReviewEntity
import kotlinx.coroutines.flow.Flow

class ReviewRepository(private val database: MovieTrackerDatabase) {
    private val reviewDao = database.reviewDao()

    fun getReviewByMovieId(movieId: Int): Flow<ReviewEntity?> = reviewDao.getReviewByMovieId(movieId)

    fun getAllReviews(): Flow<List<ReviewEntity>> = reviewDao.getAllReviews()

    suspend fun addReview(reviewEntity: ReviewEntity) {
        reviewDao.insertReview(reviewEntity)
    }

    suspend fun updateReview(movieId: Int, rating: Float, notes: String) {
        reviewDao.updateReview(movieId, rating, notes)
    }

    suspend fun deleteReview(movieId: Int) {
        reviewDao.deleteReviewByMovieId(movieId)
    }
}

