package com.example.vuey.ui.fragment.album

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vuey.R
import com.example.vuey.databinding.FragmentSearchAlbumBinding
import com.example.vuey.ui.adapter.AlbumAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchAlbumFragment : Fragment() {

    private var _binding : FragmentSearchAlbumBinding? = null
    private val binding get() = _binding!!
    private lateinit var albumAdapter: AlbumAdapter
    private val viewModel : AlbumViewModel by viewModels()

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

        viewModel.albumResponse.observe(viewLifecycleOwner) {
            it.let {
                albumAdapter.submitAlbum(it)
            }
        }

        with(binding){

            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.searchAlbum(etSearch.text.toString())
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }
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