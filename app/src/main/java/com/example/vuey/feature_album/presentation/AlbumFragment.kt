package com.example.vuey.feature_album.presentation

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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vuey.R
import com.example.vuey.databinding.FragmentAlbumBinding
import com.example.vuey.feature_album.presentation.adapter.AlbumAdapter
import com.example.vuey.feature_album.presentation.viewmodel.AlbumViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    private val albumViewModel: AlbumViewModel by viewModels()
    private val albumAdapter by lazy { AlbumAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBottomNavigation()
        initRecyclerView()
        showAllAlbums()
        setupNavigation()
    }

    private fun setupNavigation() {
        with(binding) {
            fabSearch.setOnClickListener { findNavController().navigate(R.id.action_albumFragment_to_searchAlbumFragment) }
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.imgStatistics -> {
                        findNavController().navigate(R.id.action_albumFragment_to_statisticsAlbumFragment)
                        true
                    }
                    else -> { false }
                }
            }
            val menuItem = toolbar.menu.findItem(R.id.imgStatistics)
            menuItem.icon.let {
                MenuItemCompat.setIconTintList(menuItem, ColorStateList.valueOf(Color.WHITE))
            }
        }
    }

    private fun showAllAlbums() {
        lifecycleScope.launch {
            albumViewModel.allAlbums.collect { albums ->
                albumAdapter.submitAlbums(albums)
            }
        }
    }

    private fun initRecyclerView() {
        binding.albumRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = albumAdapter
        }
    }

    private fun showBottomNavigation() {
        val bottomNavigationView: BottomNavigationView =
            requireActivity().findViewById(R.id.bottomMenu)
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}