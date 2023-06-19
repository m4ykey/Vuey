package com.example.vuey.feature_movie.presentation

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vuey.R
import com.example.vuey.databinding.FragmentSearchMovieBinding
import com.example.vuey.feature_movie.presentation.adapter.MovieAdapter
import com.example.vuey.feature_movie.presentation.viewmodel.MovieViewModel
import com.example.vuey.util.utils.showSnackbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchMovieFragment : Fragment() {

    private var _binding : FragmentSearchMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MovieViewModel by viewModels()
    private val movieAdapter by lazy { MovieAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeSearchMovie()
        hideBottomNavigation()
        searchMovie()

        with(binding) {
            toolBar.apply {
                setNavigationOnClickListener { findNavController().navigateUp() }
                setOnMenuItemClickListener { menuItem ->
                    when(menuItem.itemId) {
                        R.id.clearText -> {
                            etSearch.setText("")
                            true
                        }
                        else -> { false }
                    }
                }
                val menuItem = toolBar.menu.findItem(R.id.clearText)
                menuItem.icon.let {
                    MenuItemCompat.setIconTintList(menuItem, ColorStateList.valueOf(Color.WHITE))
                }
            }

            recyclerViewMovie.apply {
                adapter = movieAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun searchMovie() {
        with(binding) {
            etSearch.addTextChangedListener(object : TextWatcher {

                private val DELAY : Long = 500
                private var searchHandler = Handler()

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    searchHandler.removeCallbacksAndMessages(null)

                    val searchMovie = etSearch.text.toString()

                    if (searchMovie.isNotEmpty()) {
                        progressBar.visibility = View.VISIBLE
                        searchHandler.postDelayed({
                            viewModel.searchMovie(searchMovie)
                            progressBar.visibility = View.GONE
                        }, DELAY)
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
            })
        }
    }

    private fun observeSearchMovie() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieSearchUiState.collect { uiState ->
                    when {
                        uiState.isLoading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        uiState.isError?.isNotEmpty() == true -> {
                            binding.progressBar.visibility = View.GONE
                            showSnackbar(requireView(), uiState.isError.toString(), Snackbar.LENGTH_LONG)
                        }
                        uiState.searchMovieData.isNotEmpty() -> {
                            binding.progressBar.visibility = View.GONE
                            movieAdapter.submitMovie(uiState.searchMovieData)
                        }
                    }
                }
            }
        }
    }

    private fun hideBottomNavigation() {
        val bottomNavigation = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}