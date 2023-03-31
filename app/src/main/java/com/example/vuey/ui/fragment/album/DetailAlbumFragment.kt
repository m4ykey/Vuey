package com.example.vuey.ui.fragment.album

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.vuey.R
import com.example.vuey.data.local.album.detail.Artist
import com.example.vuey.databinding.FragmentAlbumDetailBinding
import com.example.vuey.ui.adapter.TrackListAdapter
import com.example.vuey.util.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailAlbumFragment : Fragment() {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlbumViewModel by viewModels()
    private val arguments: DetailAlbumFragmentArgs by navArgs()
    private lateinit var trackListAdapter: TrackListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trackListAdapter = TrackListAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView: BottomNavigationView =
            requireActivity().findViewById(R.id.bottomNavigation)
        bottomNavigationView.visibility = View.GONE

        viewModel.getAlbum(arguments.album.id)

        with(binding) {
            recyclerViewTracks.apply {
                adapter = trackListAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            imgBack.setOnClickListener { findNavController().navigateUp() }
        }

        viewModel.albumDetail.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()

                    val albumDetail = response.data
                    val albumImage =
                        albumDetail!!.images.find { it.width == 640 && it.height == 640 }

                    with(binding) {

                        imgAlbum.load(albumImage?.url) {
                            error(R.drawable.album_error)
                            crossfade(true)
                            crossfade(1000)
                        }
                        txtAlbum.text = albumDetail.albumName
                        val artists : List<Artist> = albumDetail.artists
                        val artistNames = artists.joinToString(separator = ", ") { it.name }
                        txtArtist.text = artistNames

                        btnAlbum.setOnClickListener {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(arguments.album.external_urls.spotify)
                            )
                            startActivity(intent)
                        }
                        btnArtist.setOnClickListener {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(albumDetail.artists[0].external_urls.spotify)
                            )
                            startActivity(intent)
                        }
                        response.data.let { trackList ->
                            trackListAdapter.submitTrack(trackList.tracks.items)
                        }
                    }

                }
                is Resource.Failure -> {
                    hideLoading()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}