package com.jarvis.movietracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.movietracker.R
import com.jarvis.movietracker.databinding.ItemMovieBinding
import com.jarvis.movietracker.domain.model.Movie
import com.jarvis.movietracker.utils.loadImage

class MovieAdapter(
    private val onMovieClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private var movies = listOf<Movie>()

    fun setMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    fun addMovies(newMovies: List<Movie>) {
        movies = movies + newMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.apply {
                movieTitle.text = movie.title
                movieRating.text = String.format("%.1f", movie.voteAverage)
                moviePoster.loadImage(movie.posterPath)
                root.setOnClickListener { onMovieClick(movie) }
            }
        }
    }
}

