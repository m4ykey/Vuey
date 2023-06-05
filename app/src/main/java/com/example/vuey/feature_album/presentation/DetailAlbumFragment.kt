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
import com.example.vuey.feature_album.data.remote.model.Tracks
import com.example.vuey.feature_album.presentation.adapter.TrackListAdapter
import com.example.vuey.feature_album.presentation.viewmodel.AlbumViewModel
import com.example.vuey.util.utils.DateUtils
import com.example.vuey.util.utils.showSnackbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeDetailAlbum()
        initRecyclerView()
        refreshUi()
        hideBottomNavigation()

        binding.toolBar.setNavigationOnClickListener { findNavController().navigateUp() }

        val albumDatabase = arguments.albumEntity

        detailViewModel.getAlbumById(albumDatabase.id).onEach { album ->
            isAlbumSaved = if (album == null) {
                binding.imgSave.setImageResource(R.drawable.ic_save_outlined)
                false
            } else {
                binding.imgSave.setImageResource(R.drawable.ic_save)
                true
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        val trackList = albumDatabase.trackList.map { trackEntity ->
            Tracks.AlbumItem(
                id = albumDatabase.id,
                trackName = trackEntity.trackName,
                artistList = trackEntity.artistList.map { artistEntity ->
                    Artist(
                        id = artistEntity.id,
                        artistName = artistEntity.name,
                        externalUrls = ExternalUrls(
                            spotify = artistEntity.externalUrls.spotify
                        )
                    )
                },
                durationMs = trackEntity.durationMs,
                externalUrls = ExternalUrls(
                    spotify = albumDatabase.externalUrls.spotify
                ),
                albumType = albumDatabase.albumType
            )
        }
        trackListAdapter.submitTrack(trackList)

        val albumEntity = AlbumEntity(
            albumLength = albumDatabase.albumLength,
            albumName = albumDatabase.albumName,
            albumType = albumDatabase.albumType,
            id = albumDatabase.id,
            releaseDate = DateUtils.formatAirDate(albumDatabase.releaseDate).toString(),
            totalTracks = albumDatabase.totalTracks,
            externalUrls = AlbumEntity.ExternalUrlsEntity(
                spotify = albumDatabase.externalUrls.spotify
            ),
            albumCover = albumDatabase.albumCover,
            artistList = albumDatabase.artistList,
            trackList = albumDatabase.trackList.map { track ->
                AlbumEntity.TrackListEntity(
                    durationMs = track.durationMs,
                    trackName = track.trackName,
                    artistList = track.artistList
                )
            }
        )

//        val albumCover = albumDatabase.albumCover.find { it.height == 640 && it.width == 640 }
        val artists : List<AlbumEntity.ArtistEntity> = albumDatabase.artistList
        val artistList = artists.joinToString(separator = ", ") { it.name }

        with(binding) {
            imgSave.setOnClickListener {
                isAlbumSaved = !isAlbumSaved
                if (isAlbumSaved) {
                    showSnackbar(requireView(), getString(R.string.added_to_library))
                    imgSave.setImageResource(R.drawable.ic_save)
                    detailViewModel.insertAlbum(albumEntity)
                } else {
                    showSnackbar(requireView(), getString(R.string.removed_from_library))
                    imgSave.setImageResource(R.drawable.ic_save_outlined)
                    detailViewModel.deleteAlbum(albumEntity)
                }
            }

            txtAlbumName.text = albumDatabase.albumName
            txtAlbumTime.text = albumDatabase.albumLength
            txtArtist.text = artistList
            txtInfo.text = "${albumDatabase.albumType.replaceFirstChar { it.uppercase() }} • " +
                    "${DateUtils.formatAirDate(albumDatabase.releaseDate)} • ${albumDatabase.totalTracks} " + getString(R.string.tracks)

            imgAlbum.load(albumDatabase.albumCover.url) {
                crossfade(true)
                crossfade(1000)
            }

            btnAlbum.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(albumDatabase.externalUrls.spotify)
                )
                startActivity(intent)
            }

            btnArtist.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(albumDatabase.artistList[0].externalUrls.spotify)
                )
                startActivity(intent)
            }
        }
    }

    private fun hideBottomNavigation() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomMenu)
        bottomNavigationView.visibility = View.GONE
    }

    private fun refreshUi() {
        binding.swipeRefresh.setOnRefreshListener {
            detailViewModel.refreshDetail(arguments.album.id)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewTracks.adapter = trackListAdapter
    }

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
                            val albumTimeSeconds = (time / 1000) % 60

                            val albumLength = if (albumTimeHour == 0) {
                                String.format("%d min %d ${getString(R.string.sec)}", albumTimeMinute, albumTimeSeconds)
                            } else if (albumTimeMinute == 0){
                                String.format("%d ${getString(R.string.hour)}", albumTimeHour)
                            } else {
                                String.format("%d ${getString(R.string.hour)} %d min", albumTimeHour, albumTimeMinute)
                            }

                            with(binding) {

                                txtAlbumName.text = albumDetail.albumName
                                txtAlbumTime.text = albumLength
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

                                val artistEntity = albumDetail.artistList.map { artist ->
                                    AlbumEntity.ArtistEntity(
                                        id = artist.id,
                                        name = artist.artistName,
                                        externalUrls = AlbumEntity.ExternalUrlsEntity(
                                            spotify = artist.externalUrls.spotify
                                        )
                                    )
                                }

                                val albumEntity = AlbumEntity(
                                    id = albumDetail.id,
                                    albumLength = albumLength,
                                    albumName = albumDetail.albumName,
                                    albumType = albumDetail.albumType,
                                    releaseDate = DateUtils.formatAirDate(albumDetail.releaseDate).toString(),
                                    totalTracks = albumDetail.totalTracks,
                                    externalUrls = AlbumEntity.ExternalUrlsEntity(
                                        spotify = albumDetail.externalUrls.spotify
                                    ),
                                    albumCover = albumDetail.imageList.firstOrNull()?.let { image ->
                                        AlbumEntity.ImageEntity(
                                            width = 640,
                                            height = 640,
                                            url = image.url
                                        )
                                    }!!,
                                    artistList = artistEntity,
                                    trackList = albumDetail.tracks.items.map { tracks ->
                                        AlbumEntity.TrackListEntity(
                                            durationMs = tracks.durationMs,
                                            trackName = tracks.trackName,
                                            artistList = artistEntity
                                        )
                                    }
                                )

                                detailViewModel.getAlbumById(arguments.albumEntity.id).onEach { album ->
                                    isAlbumSaved = if (album == null) {
                                        binding.imgSave.setImageResource(R.drawable.ic_save_outlined)
                                        false
                                    } else {
                                        binding.imgSave.setImageResource(R.drawable.ic_save)
                                        true
                                    }
                                }.launchIn(viewLifecycleOwner.lifecycleScope)

                                imgSave.setOnClickListener {
                                    isAlbumSaved = !isAlbumSaved
                                    if (isAlbumSaved) {
                                        showSnackbar(requireView(), getString(R.string.added_to_library))
                                        imgSave.setImageResource(R.drawable.ic_save)
                                        detailViewModel.insertAlbum(albumEntity)
                                    } else {
                                        showSnackbar(requireView(), getString(R.string.removed_from_library))
                                        imgSave.setImageResource(R.drawable.ic_save_outlined)
                                        detailViewModel.deleteAlbum(albumEntity)
                                    }
                                }
                            }
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