package com.jarvis.movietracker.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jarvis.movietracker.databinding.FragmentWatchlistBinding
import com.jarvis.movietracker.R
import com.jarvis.movietracker.ui.adapter.WatchlistAdapter
import com.jarvis.movietracker.ui.viewmodel.WatchlistViewModel
import com.jarvis.movietracker.data.repository.WatchlistRepository
import com.jarvis.movietracker.data.local.MovieTrackerDatabase

class WatchlistFragment : Fragment() {
    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WatchlistViewModel
    private lateinit var watchlistAdapter: WatchlistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = MovieTrackerDatabase.getInstance(requireContext())
        val repository = WatchlistRepository(database)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
                WatchlistViewModel(repository) as T
        })[WatchlistViewModel::class.java]

        setupRecyclerView()
        observeWatchlist()
    }

    private fun setupRecyclerView() {
        watchlistAdapter = WatchlistAdapter(
            onMovieClick = { item ->
                val bundle = Bundle().apply {
                    putInt("movieId", item.movieId)
                }
                findNavController().navigate(R.id.action_watchlist_to_details, bundle)
            },
            onDeleteClick = { item ->
                viewModel.removeFromWatchlist(item.movieId)
            },
            onWatchedToggle = { item ->
                viewModel.updateWatchedStatus(item.movieId, item.watched)
            }
        )
        binding.watchlistRecyclerView.apply {
            adapter = watchlistAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeWatchlist() {
        viewModel.watchlist.observe(viewLifecycleOwner) { items ->
            if (items.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
                binding.watchlistRecyclerView.visibility = View.GONE
            } else {
                binding.emptyState.visibility = View.GONE
                binding.watchlistRecyclerView.visibility = View.VISIBLE
                watchlistAdapter.setWatchlist(items)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

