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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vuey.R
import com.example.vuey.databinding.FragmentAlbumBinding
import com.example.vuey.feature_album.presentation.adapter.AlbumAdapter
import com.example.vuey.feature_album.presentation.viewmodel.AlbumViewModel
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

        initRecyclerView()
        showAllAlbums()
        setupNavigation()
    }

    private fun setupNavigation() {
        with(binding) {
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.imgSearch -> {
                        true
                    }
                    R.id.imgAdd -> {
                        findNavController().navigate(R.id.action_albumFragment_to_searchAlbumFragment)
                        true
                    }
                    else -> { false }
                }
            }
            val addItem = toolbar.menu.findItem(R.id.imgAdd)
            val searchItem = toolbar.menu.findItem(R.id.imgSearch)
            searchItem.icon.let {
                MenuItemCompat.setIconTintList(searchItem, ColorStateList.valueOf(Color.WHITE))
            }
            addItem.icon.let {
                MenuItemCompat.setIconTintList(addItem, ColorStateList.valueOf(Color.WHITE))
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
            layoutManager = LinearLayoutManager(requireContext())
            adapter = albumAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}