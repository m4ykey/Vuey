package com.example.vuey.ui.fragment.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vuey.R
import com.example.vuey.databinding.FragmentSearchAlbumBinding
import com.example.vuey.ui.adapter.AlbumAdapter
import com.example.vuey.ui.fragment.album.AlbumViewModel
import com.example.vuey.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchAlbumFragment : Fragment() {

    private var _binding: FragmentSearchAlbumBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlbumViewModel by viewModels()
    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeSearchAlbum()

        with(binding) {
            imgBack.setOnClickListener { findNavController().navigateUp() }
            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (etSearch.text.toString().isEmpty() || etSearch.text.toString().length <= 2
                    ) {
                        Toast.makeText(
                            requireContext(),
                            "Enter at least 3 characters",
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
                    showError()
                    hideLoading()
                }
                is Resource.Loading -> {
                    showLoading()
                }
            }
        }
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError() {
        Toast.makeText(requireContext(), viewModel.albumSearch.value?.message, Toast.LENGTH_SHORT)
            .show()
    }

    private fun setupRecyclerView() {
        albumAdapter = AlbumAdapter()
        binding.recyclerViewSearchAlbum.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = albumAdapter
        }
        albumAdapter.setOnItemClickListener(object : AlbumAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                findNavController().navigate(R.id.action_searchFragment_to_albumDetailFragment)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}