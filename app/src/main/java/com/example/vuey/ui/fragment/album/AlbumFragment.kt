package com.example.vuey.ui.fragment.album

import android.annotation.SuppressLint
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
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        albumAdapter = AlbumAdapter(viewModel)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        bottomNavigationView.visibility = View.VISIBLE

        viewModel.getAllAlbums.observe(viewLifecycleOwner) { albumList ->
            val numberOfAlbums = albumList?.size ?: 0
            binding.txtAlbumSize.text = getString(R.string.number_of_albums) + " $numberOfAlbums"
        }

        binding.apply {
            fabSearch.setOnClickListener {
                findNavController().navigate(R.id.action_albumFragment_to_searchFragment)
            }
            viewModel.getAllAlbums.observe(viewLifecycleOwner) { albumList ->
                albumAdapter.submitAlbum(albumList)
            }
            recyclerView.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = albumAdapter
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}