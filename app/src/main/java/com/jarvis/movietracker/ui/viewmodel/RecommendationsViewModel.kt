package com.jarvis.movietracker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.movietracker.data.repository.ReviewRepository
import com.jarvis.movietracker.data.local.entities.ReviewEntity
import kotlinx.coroutines.launch

class RecommendationsViewModel(private val reviewRepository: ReviewRepository) : ViewModel() {
    private val _reviews = MutableLiveData<List<ReviewEntity>>()
    val reviews: LiveData<List<ReviewEntity>> = _reviews

    init {
        loadAllReviews()
    }

    fun loadAllReviews() {
        viewModelScope.launch {
            reviewRepository.getAllReviews().collect { reviews ->
                _reviews.value = reviews.sortedByDescending { it.timestamp }
            }
        }
    }
}

