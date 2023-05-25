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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.vuey.R
import com.example.vuey.databinding.FragmentAlbumDetailBinding
import com.example.vuey.feature_album.data.local.entity.AlbumEntity
import com.example.vuey.feature_album.data.remote.model.Artist
import com.example.vuey.feature_album.data.remote.model.ExternalUrls
import com.example.vuey.feature_album.data.remote.model.Tracks
import com.example.vuey.feature_album.presentation.adapter.TrackListAdapter
import com.example.vuey.feature_album.presentation.viewmodel.AlbumViewModel
import com.example.vuey.util.network.Resource
import com.example.vuey.util.utils.DateUtils
import com.example.vuey.util.utils.showSnackbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailAlbumFragment : Fragment() {

    private var _binding: FragmentAlbumDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlbumViewModel by viewModels()
    private val arguments: DetailAlbumFragmentArgs by navArgs()
    private lateinit var trackListAdapter: TrackListAdapter
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
        trackListAdapter = TrackListAdapter()
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView: BottomNavigationView =
            requireActivity().findViewById(R.id.bottomMenu)
        bottomNavigationView.visibility = View.GONE

        with(binding) {
            recyclerViewTracks.apply {
                adapter = trackListAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            toolBar.setNavigationOnClickListener { findNavController().navigateUp() }

            val databaseArguments = arguments.albumEntity

            val imageDatabase =
                databaseArguments.imageList.find { it.width == 640 && it.height == 640 }
            imgAlbum.load(imageDatabase?.url)

            txtAlbumName.text = databaseArguments.albumName
            val artistsDatabase: List<AlbumEntity.ArtistEntity> = databaseArguments.artistList

            val artistNameDatabase = artistsDatabase.joinToString(separator = ", ") { it.name }
            txtArtist.text = artistNameDatabase

            txtInfo.text =
                "${databaseArguments.albumType.replaceFirstChar { it.uppercase() }} • " +
                        "${DateUtils.formatAirDate(databaseArguments.releaseDate)} • " + " ${databaseArguments.totalTracks} " + getString(R.string.tracks)

            btnAlbum.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(databaseArguments.externalUrls.spotify))
                startActivity(intent)
            }
            btnArtist.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(databaseArguments.artistList[0].externalUrls.spotify)
                )
                startActivity(intent)
            }
            val trackList = databaseArguments.trackList.map { trackEntity ->
                Tracks.AlbumItem(
                    id = databaseArguments.id,
                    trackName = trackEntity.trackName,
                    artistList = trackEntity.artistList.map { artistEntity ->
                        Artist(
                            id = artistEntity.id,
                            artistName = artistEntity.name,
                            externalUrls = artistEntity.externalUrls.toExternalUrls()
                        )
                    },
                    durationMs = trackEntity.durationMs,
                    externalUrls = databaseArguments.externalUrls.toExternalUrls(),
                    albumType = databaseArguments.albumType
                )
            }
            trackListAdapter.submitTrack(trackList)
            txtAlbumTime.text = databaseArguments.albumLength

            val albumEntity = AlbumEntity(
                albumLength = databaseArguments.albumLength,
                albumName = databaseArguments.albumName,
                albumType = databaseArguments.albumType,
                id = databaseArguments.id,
                artistList = databaseArguments.artistList,
                externalUrls = databaseArguments.externalUrls,
                imageList = databaseArguments.imageList,
                releaseDate = databaseArguments.releaseDate,
                totalTracks = databaseArguments.totalTracks,
                trackList = databaseArguments.trackList
            )

//            viewModel.getAlbumById(arguments.albumEntity.id)
//                .observe(viewLifecycleOwner) { album ->
//                    isAlbumSaved = if (album == null) {
//                        imgSave.setImageResource(R.drawable.ic_save_outlined)
//                        false
//                    } else {
//                        imgSave.setImageResource(R.drawable.ic_save)
//                        true
//                    }
//                }

            imgSave.setOnClickListener {
                isAlbumSaved = !isAlbumSaved
                if (isAlbumSaved) {
                    showSnackbar(requireView(), getString(R.string.added_to_library))
                    //viewModel.insertAlbum(albumEntity)
                    imgSave.setImageResource(R.drawable.ic_save)
                } else {
                    showSnackbar(requireView(), getString(R.string.removed_from_library))
                    //viewModel.deleteAlbum(albumEntity)
                    imgSave.setImageResource(R.drawable.ic_save_outlined)
                }
            }
        }

        //viewModel.getAlbum(arguments.album.id)
        viewModel.albumDetail.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()

                    with(binding) {

                        val albumDetail = response.data!!

                        val albumImage =
                            albumDetail.images.find { it.width == 640 && it.height == 640 }

                        var albumTime = 0
                        for (track in albumDetail.tracks.items) {
                            albumTime += track.durationMs
                        }

                        val albumTimeHour = albumTime / (1000 * 60 * 60)
                        val albumTimeMinute = (albumTime / (1000 * 60)) % 60

                        val formattedAlbumTime = if (albumTimeHour == 0) {
                            String.format("%d min", albumTimeMinute)
                        } else {
                            String.format("%d h %d min", albumTimeHour, albumTimeMinute)
                        }
                        txtAlbumTime.text = formattedAlbumTime

                        imgAlbum.load(albumImage?.url) {
                            error(R.drawable.album_error)
                            crossfade(true)
                            crossfade(1000)
                        }
                        txtAlbumName.text = albumDetail.albumName
                        val artists: List<Artist> = albumDetail.artists
                        val artistNames = artists.joinToString(separator = ", ") { it.artistName }
                        txtArtist.text = artistNames

                        txtInfo.text =
                            "${albumDetail.album_type.replaceFirstChar { it.uppercase() }} • " +
                                    "${DateUtils.formatAirDate(albumDetail.release_date)} • " + " ${albumDetail.total_tracks} " + getString(R.string.tracks)

                        btnAlbum.setOnClickListener {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(arguments.album.externalUrls.spotify)
                            )
                            startActivity(intent)
                        }
                        btnArtist.setOnClickListener {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(albumDetail.artists[0].externalUrls.spotify)
                            )
                            startActivity(intent)
                        }

                        albumDetail.let { trackList -> trackListAdapter.submitTrack(trackList.tracks.items) }

                        val artistList = albumDetail.artists.map { artist ->
                            AlbumEntity.ArtistEntity(
                                name = artist.artistName,
                                id = artist.id,
                                externalUrls = AlbumEntity.ExternalUrlsEntity(
                                    spotify = artist.externalUrls.spotify
                                )
                            )
                        }
                        val albumEntity = AlbumEntity(
                            albumType = albumDetail.album_type,
                            id = albumDetail.id,
                            albumName = albumDetail.albumName,
                            totalTracks = albumDetail.total_tracks,
                            artistList = artistList,
                            albumLength = formattedAlbumTime,
                            releaseDate = DateUtils.formatAirDate(albumDetail.release_date).toString(),
                            imageList = albumDetail.images.map { image ->
                                AlbumEntity.ImageEntity(
                                    width = image.width,
                                    height = image.height,
                                    url = image.url
                                )
                            },
                            externalUrls = AlbumEntity.ExternalUrlsEntity(
                                spotify = albumDetail.external_urls.spotify
                            ),
                            trackList = albumDetail.tracks.items.map { tracks ->
                                AlbumEntity.TrackListEntity(
                                    durationMs = tracks.durationMs,
                                    trackName = tracks.trackName,
                                    artistList = artistList
                                )
                            }
                        )

//                        viewModel.getAlbumById(arguments.albumEntity.id)
//                            .observe(viewLifecycleOwner) { album ->
//                                isAlbumSaved = if (album == null) {
//                                    imgSave.setImageResource(R.drawable.ic_save_outlined)
//                                    false
//                                } else {
//                                    imgSave.setImageResource(R.drawable.ic_save)
//                                    true
//                                }
//                            }

                        imgSave.setOnClickListener {
                            isAlbumSaved = !isAlbumSaved
                            if (isAlbumSaved) {
                                showSnackbar(requireView(), getString(R.string.added_to_library))
                                //viewModel.insertAlbum(albumEntity)
                                imgSave.setImageResource(R.drawable.ic_save)
                            } else {
                                showSnackbar(requireView(), getString(R.string.removed_from_library))
                                //viewModel.deleteAlbum(albumEntity)
                                imgSave.setImageResource(R.drawable.ic_save_outlined)
                            }
                        }
                    }
                }

                is Resource.Failure -> {
                    hideLoading()
                    showSnackbar(requireView(), "${response.message}", Snackbar.LENGTH_LONG)
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

    private fun AlbumEntity.ExternalUrlsEntity.toExternalUrls(): ExternalUrls {
        return ExternalUrls(
            spotify = this.spotify
        )
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}