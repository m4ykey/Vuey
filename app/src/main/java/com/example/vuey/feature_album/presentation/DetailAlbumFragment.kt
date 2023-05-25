package com.example.vuey.feature_album.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.vuey.R
import com.example.vuey.databinding.FragmentAlbumDetailBinding
import com.example.vuey.feature_album.data.local.entity.AlbumEntity
import com.example.vuey.feature_album.data.remote.model.Artist
import com.example.vuey.feature_album.data.remote.model.ExternalUrls
import com.example.vuey.feature_album.presentation.adapter.TrackListAdapter
import com.example.vuey.feature_album.presentation.viewmodel.AlbumViewModel
import com.example.vuey.util.utils.DateUtils
import com.example.vuey.util.utils.showSnackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailAlbumFragment : Fragment() {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel: AlbumViewModel by viewModels()
    private val arguments: DetailAlbumFragmentArgs by navArgs()
    private val trackListAdapter by lazy { TrackListAdapter() }
    private var isAlbumSaved = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailViewModel.getAlbumDetail(arguments.album.id)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeDetailAlbum()
        initRecyclerView()

        binding.toolBar.setNavigationOnClickListener { findNavController().navigateUp() }

    }

    private fun initRecyclerView() {
        binding.recyclerViewTracks.adapter = trackListAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun observeDetailAlbum() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.albumDetailUiState.collect { uiState ->
                    when {
                        uiState.isLoading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        uiState.isError?.isNotEmpty() == true -> {
                            binding.progressBar.visibility = View.GONE
                            showSnackbar(requireView(), uiState.isError.toString(), Snackbar.LENGTH_LONG)
                        }
                        uiState.detailAlbumData != null -> {
                            binding.progressBar.visibility = View.GONE

                            val albumDetail = uiState.detailAlbumData

                            val albumCover = albumDetail.imageList.find { it.height == 640 && it.width == 640 }
                            val artists : List<Artist> = albumDetail.artistList
                            val artistName = artists.joinToString(separator = ", ") { it.artistName }

                            var time = 0
                            for (track in albumDetail.tracks.items) {
                                time += track.durationMs
                            }

                            val albumTimeHour = time / (1000 * 60 * 60)
                            val albumTimeMinute = (time / (1000 * 60)) % 60

                            val albumTime = if (albumTimeHour == 0) {
                                String.format("%d min", albumTimeMinute)
                            } else {
                                String.format("%d h %d min", albumTimeHour, albumTimeMinute)
                            }

                            with(binding) {

                                txtAlbumName.text = albumDetail.albumName
                                txtAlbumTime.text = albumTime
                                txtArtist.text = artistName
                                txtInfo.text = "${albumDetail.albumType.replaceFirstChar { it.uppercase() }} • " +
                                        "${DateUtils.formatAirDate(albumDetail.releaseDate)} • ${albumDetail.totalTracks} " + getString(R.string.tracks)

                                imgAlbum.load(albumCover?.url) {
                                    crossfade(true)
                                    crossfade(1000)
                                    error(R.drawable.album_error)
                                }

                                btnAlbum.setOnClickListener {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(albumDetail.externalUrls.spotify)
                                    )
                                    startActivity(intent)
                                }
                                btnArtist.setOnClickListener {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(albumDetail.artistList[0].externalUrls.spotify)
                                    )
                                    startActivity(intent)
                                }

                                albumDetail.let { trackList -> trackListAdapter.submitTrack(trackList.tracks.items) }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun AlbumEntity.ExternalUrlsEntity.toExternalUrls(): ExternalUrls {
        return ExternalUrls(
            spotify = this.spotify
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}