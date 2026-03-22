package com.jarvis.movietracker.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jarvis.movietracker.databinding.FragmentRecommendationsBinding
import com.jarvis.movietracker.ui.adapter.ReviewAdapter
import com.jarvis.movietracker.ui.viewmodel.RecommendationsViewModel
import com.jarvis.movietracker.data.repository.ReviewRepository
import com.jarvis.movietracker.data.local.MovieTrackerDatabase

class RecommendationsFragment : Fragment() {
    private var _binding: FragmentRecommendationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RecommendationsViewModel
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecommendationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = MovieTrackerDatabase.getInstance(requireContext())
        val repository = ReviewRepository(database)
        viewModel = ViewModelProvider(this, object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
                RecommendationsViewModel(repository) as T
        })[RecommendationsViewModel::class.java]

        setupRecyclerView()
        observeReviews()
    }

    private fun setupRecyclerView() {
        reviewAdapter = ReviewAdapter()
        binding.reviewsRecyclerView.apply {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeReviews() {
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            if (reviews.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
                binding.reviewsRecyclerView.visibility = View.GONE
            } else {
                binding.emptyState.visibility = View.GONE
                binding.reviewsRecyclerView.visibility = View.VISIBLE
                reviewAdapter.setReviews(reviews)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

