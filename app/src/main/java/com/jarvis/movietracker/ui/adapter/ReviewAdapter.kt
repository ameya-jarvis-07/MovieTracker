package com.jarvis.movietracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.movietracker.databinding.ItemReviewBinding
import com.jarvis.movietracker.data.local.entities.ReviewEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    private var reviews = listOf<ReviewEntity>()

    fun setReviews(newReviews: List<ReviewEntity>) {
        reviews = newReviews
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int = reviews.size

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ReviewEntity) {
            binding.apply {
                reviewRating.text = String.format("%.1f", review.userRating)
                reviewNotes.text = review.notes.takeIf { it.isNotEmpty() } ?: "No notes"
                reviewDate.text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    .format(Date(review.timestamp))
                ratingBar.rating = review.userRating
            }
        }
    }
}

