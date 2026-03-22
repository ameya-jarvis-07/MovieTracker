package com.jarvis.movietracker.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.jarvis.movietracker.databinding.FragmentSearchMovieBinding
import com.jarvis.movietracker.ui.adapter.MovieAdapter
import com.jarvis.movietracker.ui.viewmodel.SearchMovieViewModel
import com.jarvis.movietracker.data.repository.MovieRepository
import com.jarvis.movietracker.data.local.MovieTrackerDatabase
import com.jarvis.movietracker.utils.Result

class SearchMovieFragment : Fragment() {
    private var _binding: FragmentSearchMovieBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SearchMovieViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = MovieTrackerDatabase.getInstance(requireContext())
        val repository = MovieRepository(database)
        viewModel = ViewModelProvider(this, object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T =
                SearchMovieViewModel(repository) as T
        })[SearchMovieViewModel::class.java]

        setupRecyclerView()
        setupSearchView()
        observeSearchResults()
        loadPopularMovies()
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter { movie ->
            val bundle = Bundle().apply {
                putInt("movieId", movie.id)
            }
            findNavController().navigate(
                com.jarvis.movietracker.R.id.action_search_to_details,
                bundle
            )
        }
        binding.moviesRecyclerView.apply {
            adapter = movieAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.searchMovies(query, 1)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        binding.loadMoreButton.setOnClickListener {
            val query = binding.searchView.query.toString()
            viewModel.loadMore(if (query.isEmpty()) null else query)
        }
    }

    private fun observeSearchResults() {
        viewModel.searchResults.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.loadMoreButton.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val currentPage = viewModel.currentPage.value ?: 1
                    if (currentPage == 1) {
                        movieAdapter.setMovies(result.data)
                    } else {
                        movieAdapter.addMovies(result.data)
                    }
                    binding.loadMoreButton.visibility = if (result.data.isNotEmpty()) View.VISIBLE else View.GONE
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorMessage.visibility = View.VISIBLE
                    binding.errorMessage.text = result.exception.message ?: "Unknown error"
                }
            }
        }
    }

    private fun loadPopularMovies() {
        viewModel.getPopularMovies(1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

