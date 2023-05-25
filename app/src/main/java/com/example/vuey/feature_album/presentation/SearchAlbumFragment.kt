package com.example.vuey.feature_album.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vuey.R
import com.example.vuey.databinding.FragmentSearchAlbumBinding
import com.example.vuey.feature_album.presentation.viewmodel.AlbumViewModel
import com.example.vuey.feature_album.presentation.adapter.AlbumAdapter
import com.example.vuey.util.utils.showSnackbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchAlbumFragment : Fragment() {

    private var _binding: FragmentSearchAlbumBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: AlbumViewModel by viewModels()
    private val albumAdapter by lazy { AlbumAdapter(true) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView: BottomNavigationView =
            requireActivity().findViewById(R.id.bottomMenu)
        bottomNavigationView.visibility = View.GONE

        searchAlbum()
        observeSearchAlbum()

        with(binding) {
            toolBar.setNavigationOnClickListener { findNavController().navigateUp() }
            recyclerViewAlbum.apply {
                adapter = albumAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
        }
    }

    private fun searchAlbum() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchQuery = binding.etSearch.text.toString()
                if (searchQuery.isEmpty()) {
                    showSnackbar(requireView(), getString(R.string.empty_search_query))
                } else {
                    searchViewModel.searchAlbum(searchQuery)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun observeSearchAlbum() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.albumSearchUiState.collect { uiState ->
                    when {
                        uiState.isLoading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        uiState.searchAlbumData.isNotEmpty() -> {
                            binding.progressBar.visibility = View.GONE
                            albumAdapter.submitAlbum(uiState.searchAlbumData)
                        }
                        uiState.isError?.isNotEmpty() == true -> {
                            binding.progressBar.visibility = View.GONE
                            showSnackbar(requireView(), uiState.isError.toString(), Snackbar.LENGTH_LONG)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}