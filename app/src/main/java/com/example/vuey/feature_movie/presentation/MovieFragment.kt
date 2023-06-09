package com.example.vuey.feature_movie.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vuey.R
import com.example.vuey.databinding.FragmentMovieBinding
import com.example.vuey.feature_movie.presentation.adapter.MovieAdapter
import com.example.vuey.feature_movie.presentation.viewmodel.MovieViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private var _binding : FragmentMovieBinding? = null
    private val binding get() = _binding!!
    private val viewModel : MovieViewModel by viewModels()
    private val movieAdapter by lazy { MovieAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView : BottomNavigationView = requireActivity().findViewById(R.id.bottomMenu)
        bottomNavigationView.visibility = View.VISIBLE

        with(binding) {

            recyclerViewMovie.apply {
                adapter = movieAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            fabSearch.setOnClickListener { findNavController().navigate(R.id.action_movieFragment_to_searchMovieFragment) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}