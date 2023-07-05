package com.example.vuey.feature_movie.presentation

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vuey.R
import com.example.vuey.databinding.FragmentMovieBinding
import com.example.vuey.feature_movie.presentation.adapter.MovieAdapter
import com.example.vuey.feature_movie.presentation.viewmodel.MovieViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private var _binding : FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private val movieAdapter by lazy { MovieAdapter() }

    private val movieViewModel : MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNavigation()
        showBottomNavigation()
        with(binding) {
            movieRecyclerView.adapter = movieAdapter
        }
        lifecycleScope.launch {
            coroutineScope {
                movieViewModel.allMovies.collect { movies ->
                    movieAdapter.submitMovie(movies)
                }
                movieViewModel.searchMovieInDatabase.collect { movieList ->
                    movieAdapter.submitMovie(movieList)
                }
            }
        }
    }

    private fun setupNavigation() {
        with(binding) {
            toolbar.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.imgSearch -> {
                        val materialAlertLayout = LayoutInflater.from(requireContext())
                            .inflate(R.layout.material_alert_edit_text, null)
                        val etSearchMovie =
                            materialAlertLayout.findViewById<TextInputEditText>(R.id.etSearchAlbum)
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Search for a movie")
                            .setView(materialAlertLayout)
                            .setPositiveButton(getString(R.string.search)) { _, _ ->
                                movieViewModel.searchMovieDatabase(etSearchMovie.text.toString())
                            }
                            .setNegativeButton(getString(R.string.close)) { _, _ -> }
                            .show()
                        true
                    }
                    R.id.imgAdd -> {
                        findNavController().navigate(R.id.action_movieFragment_to_searchMovieFragment)
                        true
                    }
                    R.id.imgStatistics -> { true }
                    else -> { false }
                }
            }
            val searchItem = toolbar.menu.findItem(R.id.imgSearch)
            val addItem = toolbar.menu.findItem(R.id.imgAdd)
            val statisticsItem = toolbar.menu.findItem(R.id.imgStatistics)
            statisticsItem.icon.let {
                MenuItemCompat.setIconTintList(statisticsItem, ColorStateList.valueOf(Color.WHITE))
            }
            searchItem.icon.let {
                MenuItemCompat.setIconTintList(searchItem, ColorStateList.valueOf(Color.WHITE))
            }
            addItem.icon.let {
                MenuItemCompat.setIconTintList(addItem, ColorStateList.valueOf(Color.WHITE))
            }
        }
    }

    private fun showBottomNavigation() {
        val bottomNavigation = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}