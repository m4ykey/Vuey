package com.example.vuey.feature_album.presentation.album

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.example.vuey.databinding.FragmentSearchAlbumBinding
import com.example.vuey.feature_album.presentation.adapter.AlbumAdapter
import com.example.vuey.feature_album.presentation.viewmodel.AlbumViewModel
import com.example.vuey.util.network.SpotifyError
import com.example.vuey.util.utils.showSnackbarSpotifyError
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchAlbumFragment : Fragment() {

    private var _binding: FragmentSearchAlbumBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: AlbumViewModel by viewModels()
    private val albumAdapter by lazy { AlbumAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchAlbum()
        observeSearchAlbum()
        setupToolbar()
        hideBottomNavigation()
        binding.recyclerViewAlbum.apply {
            adapter = albumAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

    private fun setupToolbar() {
        with(binding) {
            toolBar.setNavigationOnClickListener { findNavController().navigateUp() }
            toolBar.setOnMenuItemClickListener { menuItem ->
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
    }

    private fun searchAlbum() {
        with(binding) {
            etSearch.addTextChangedListener(object : TextWatcher {

                private val DELAY : Long = 500
                private var searchHandler = Handler()

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    searchHandler.removeCallbacksAndMessages(null)

                    val searchAlbum = etSearch.text.toString()

                    if (searchAlbum.isNotEmpty()) {
                        progressBar.visibility = View.VISIBLE
                        searchHandler.postDelayed({
                            lifecycleScope.launch {
                                searchViewModel.searchAlbum(searchAlbum)
                            }
                            progressBar.visibility = View.GONE
                        }, DELAY)
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
            })
        }
    }

    private fun observeSearchAlbum() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.albumSearchUiState.collect { uiState ->
                    with(binding) {
                        when {
                            uiState.isLoading -> {
                                progressBar.visibility = View.VISIBLE
                            }
                            uiState.searchAlbumData.isNotEmpty() -> {
                                progressBar.visibility = View.GONE
                                albumAdapter.submitAlbums(uiState.searchAlbumData)
                            }
                            uiState.isError?.isNotEmpty() == true -> {
                                progressBar.visibility = View.GONE
                                val error = uiState.isError.toString()
                                if (error != SpotifyError.code200) {
                                    showSnackbarSpotifyError(
                                        requireView(),
                                        error,
                                        Snackbar.LENGTH_LONG
                                    )
                                } else {
                                    Log.i("Error", "Error 200 occurred $error")
                                }
                            }
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