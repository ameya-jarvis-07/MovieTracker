package com.jarvis.movietracker.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jarvis.movietracker.databinding.FragmentMovieDetailsBinding
import com.jarvis.movietracker.ui.adapter.MovieAdapter
import com.jarvis.movietracker.ui.viewmodel.MovieDetailsViewModel
import com.jarvis.movietracker.data.repository.MovieRepository
import com.jarvis.movietracker.data.repository.ReviewRepository
import com.jarvis.movietracker.data.repository.WatchlistRepository
import com.jarvis.movietracker.data.local.MovieTrackerDatabase
import com.jarvis.movietracker.data.local.entities.WatchlistEntity
import com.jarvis.movietracker.utils.Result
import com.jarvis.movietracker.utils.loadImage
import com.jarvis.movietracker.R

class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var recommendationAdapter: MovieAdapter
    private var movieId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieId = arguments?.getInt("movieId") ?: 0
        if (movieId == 0) return

        val database = MovieTrackerDatabase.getInstance(requireContext())
        val movieRepository = MovieRepository(database)
        val watchlistRepository = WatchlistRepository(database)
        val reviewRepository = ReviewRepository(database)

        viewModel = ViewModelProvider(this, object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
                MovieDetailsViewModel(movieRepository, watchlistRepository, reviewRepository) as T
        })[MovieDetailsViewModel::class.java]

        setupRecommendationsList()
        setupWatchlistButton()
        setupReviewSection()
        observeMovieDetails()
        observeRecommendations()
        observeWatchlistStatus()

        viewModel.getMovieDetails(movieId)
        viewModel.getRecommendations(movieId)
        viewModel.checkWatchlistStatus(movieId)
        viewModel.getReview(movieId)
    }

    private fun setupRecommendationsList() {
        recommendationAdapter = MovieAdapter { movie ->
            val bundle = Bundle().apply {
                putInt("movieId", movie.id)
            }
            findNavController().navigate(R.id.action_details_to_details, bundle)
        }
        binding.recommendationsRecyclerView.apply {
            adapter = recommendationAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun setupWatchlistButton() {
        binding.addToWatchlistButton.setOnClickListener {
            val movie = (viewModel.movieDetails.value as? Result.Success)?.data ?: return@setOnClickListener
            viewModel.addToWatchlist(movie)
        }
    }

    private fun setupReviewSection() {
        binding.saveReviewButton.setOnClickListener {
            val rating = binding.ratingBar.rating
            val notes = binding.reviewNotes.text.toString()

            if (viewModel.review.value != null) {
                viewModel.updateReview(movieId, rating, notes)
            } else {
                viewModel.saveReview(movieId, rating, notes)
            }
        }
    }

    private fun observeMovieDetails() {
        viewModel.movieDetails.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val movie = result.data
                    binding.apply {
                        movieTitle.text = movie.title
                        movieOverview.text = movie.overview
                        movieRating.text = String.format("%.1f", movie.voteAverage)
                        movieReleaseDate.text = "Release: ${movie.releaseDate ?: "N/A"}"
                        movieLanguage.text = "Language: ${movie.originalLanguage?.uppercase() ?: "N/A"}"
                        moviePoster.loadImage(movie.posterPath)
                    }
                }
                is Result.Error -> binding.errorMessage.apply {
                    visibility = View.VISIBLE
                    text = result.exception.message
                }
            }
        }
    }

    private fun observeRecommendations() {
        viewModel.recommendations.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> binding.recommendationsProgressBar.visibility = View.VISIBLE
                is Result.Success -> {
                    binding.recommendationsProgressBar.visibility = View.GONE
                    if (result.data.isNotEmpty()) {
                        binding.recommendationsSection.visibility = View.VISIBLE
                        recommendationAdapter.setMovies(result.data)
                    } else {
                        binding.recommendationsSection.visibility = View.GONE
                    }
                }
                is Result.Error -> binding.recommendationsProgressBar.visibility = View.GONE
            }
        }
    }

    private fun observeWatchlistStatus() {
        viewModel.isInWatchlist.observe(viewLifecycleOwner) { inWatchlist ->
            binding.addToWatchlistButton.text = if (inWatchlist) "Remove from Watchlist" else "Add to Watchlist"
            binding.addToWatchlistButton.isSelected = inWatchlist
        }

        viewModel.review.observe(viewLifecycleOwner) { review ->
            if (review != null) {
                binding.ratingBar.rating = review.userRating
                binding.reviewNotes.setText(review.notes)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

