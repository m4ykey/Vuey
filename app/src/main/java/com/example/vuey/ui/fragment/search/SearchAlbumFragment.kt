package com.example.vuey.ui.fragment.search

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vuey.R
import com.example.vuey.databinding.FragmentSearchAlbumBinding
import com.example.vuey.ui.adapter.SearchAlbumAdapter
import com.example.vuey.ui.fragment.album.AlbumViewModel
import com.example.vuey.util.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchAlbumFragment : Fragment() {

    private var _binding: FragmentSearchAlbumBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlbumViewModel by viewModels()
    private lateinit var albumAdapter: SearchAlbumAdapter

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
            requireActivity().findViewById(R.id.bottomNavigation)
        bottomNavigationView.visibility = View.GONE

        setupRecyclerView()
        observeSearchAlbum()

        with(binding) {
            imgBack.setOnClickListener { findNavController().navigateUp() }
            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (etSearch.text.toString().isEmpty() || etSearch.text.toString().length <= 1
                    ) {
                        Toast.makeText(
                            requireContext(),
                            R.string.character_message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.searchAlbum(etSearch.text.toString())
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }
    }

    private fun observeSearchAlbum() {
        viewModel.albumSearch.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { albumList ->
                        albumAdapter.submitAlbum(albumList)
                    }
                    hideLoading()
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    hideLoading()
                }
                is Resource.Loading -> {
                    showLoading()
                }
            }
        }
    }

    private fun hideLoading() {
    }


    private fun showLoading() {
    }

    private fun setupRecyclerView() {
        albumAdapter = SearchAlbumAdapter()
        binding.recyclerViewSearchAlbum.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = albumAdapter
        }
    }
}