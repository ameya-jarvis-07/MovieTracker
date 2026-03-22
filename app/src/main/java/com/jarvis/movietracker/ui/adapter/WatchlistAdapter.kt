package com.jarvis.movietracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.movietracker.databinding.ItemWatchlistBinding
import com.jarvis.movietracker.data.local.entities.WatchlistEntity
import com.jarvis.movietracker.utils.loadImage

class WatchlistAdapter(
    private val onMovieClick: (WatchlistEntity) -> Unit,
    private val onDeleteClick: (WatchlistEntity) -> Unit,
    private val onWatchedToggle: (WatchlistEntity) -> Unit
) : RecyclerView.Adapter<WatchlistAdapter.WatchlistViewHolder>() {
    private var watchlistItems = listOf<WatchlistEntity>()

    fun setWatchlist(items: List<WatchlistEntity>) {
        watchlistItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
        return WatchlistViewHolder(
            ItemWatchlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        holder.bind(watchlistItems[position])
    }

    override fun getItemCount(): Int = watchlistItems.size

    inner class WatchlistViewHolder(private val binding: ItemWatchlistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WatchlistEntity) {
            binding.apply {
                watchlistTitle.text = item.title
                watchlistRating.text = String.format("%.1f", item.voteAverage)
                watchlistPoster.loadImage(item.posterPath)
                watchlistCheckbox.isChecked = item.watched

                root.setOnClickListener { onMovieClick(item) }
                deleteButton.setOnClickListener { onDeleteClick(item) }
                watchlistCheckbox.setOnCheckedChangeListener { _, isChecked ->
                    onWatchedToggle(item.copy(watched = isChecked))
                }
            }
        }
    }
}

