package com.example.vuey.ui.screens.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vuey.R
import com.example.vuey.databinding.FragmentAlbumBinding
import com.example.vuey.ui.adapter.AlbumAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumFragment : Fragment() {

    private var _binding : FragmentAlbumBinding? = null
    private val binding get() = _binding!!
    private val viewModel : AlbumViewModel by viewModels()
    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        albumAdapter = AlbumAdapter(viewModel, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            viewModel.getAllAlbums.observe(viewLifecycleOwner) { albumList ->
                albumAdapter.submitAlbumEntity(albumList)
            }
            recyclerView.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = albumAdapter
            }

            fabSearch.setOnClickListener {
                findNavController().navigate(R.id.action_albumFragment_to_searchAlbumFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}