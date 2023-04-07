package com.example.vuey.ui.screens.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.vuey.R
import com.example.vuey.databinding.FragmentSearchBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolBar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            chipAlbum.setOnClickListener {
                findNavController().navigate(R.id.action_searchFragment_to_searchAlbumFragment)
            }
            chipMovie.setOnClickListener {
                findNavController().navigate(R.id.action_searchFragment_to_searchMovieFragment)
            }
            chipTvShow.setOnClickListener {
                findNavController().navigate(R.id.action_searchFragment_to_tvShowSearchFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}