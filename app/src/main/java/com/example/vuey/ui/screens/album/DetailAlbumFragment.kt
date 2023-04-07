package com.example.vuey.ui.screens.album

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
import com.example.vuey.data.database.model.AlbumEntity
import com.example.vuey.data.local.album.detail.AlbumItem
import com.example.vuey.data.local.album.detail.Artist
import com.example.vuey.data.local.album.detail.ExternalUrls
import com.example.vuey.databinding.FragmentAlbumDetailBinding
import com.example.vuey.ui.adapter.TrackListAdapter
import com.example.vuey.util.network.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAlbum(arguments.album.id)

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())

        val totalTracks = getString(R.string.total_tracks)

        with(binding) {
            recyclerViewTracks.apply {
                adapter = trackListAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            imgBack.setOnClickListener { findNavController().navigateUp() }

            // get data from database
            val databaseArguments = arguments.albumEntity
            val imageDatabase =
                databaseArguments.images.find { it.width == 640 && it.height == 640 }
            imgAlbum.load(imageDatabase?.url)
            txtAlbumName.text = databaseArguments.albumName
            val artistsDatabase: List<AlbumEntity.ArtistEntity> = databaseArguments.artists
            val artistNameDatabase = artistsDatabase.joinToString(separator = ", ") { it.name }
            txtArtist.text = artistNameDatabase
            txtInfo.text =
                "${databaseArguments.albumType.replaceFirstChar { it.uppercase() }} • " +
                        "${databaseArguments.release_date} • $totalTracks ${databaseArguments.totalTracks}"
            btnAlbum.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(databaseArguments.externalUrls.spotify))
                startActivity(intent)
            }
            btnArtist.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(databaseArguments.artists[0].externalUrls.spotify)
                )
                startActivity(intent)
            }
            val trackList = databaseArguments.trackList.map { trackEntity ->
                AlbumItem(
                    id = arguments.album.id + trackEntity.trackNumber,
                    name = trackEntity.albumName,
                    artists = trackEntity.artists.map { artistEntity ->
                        Artist(
                            id = artistEntity.id,
                            name = artistEntity.name,
                            external_urls = artistEntity.externalUrls.toExternalUrls()
                        )
                    },
                    duration_ms = trackEntity.durationMs,
                    track_number = trackEntity.trackNumber,
                    external_urls = arguments.albumEntity.externalUrls.toExternalUrls(),
                    type = arguments.albumEntity.albumType
                )
            }
            trackListAdapter.submitTrack(trackList)
        }


        viewModel.albumDetail.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()

                    // Get response data
                    val albumDetail = response.data

                    // find album image
                    val albumImage =
                        albumDetail!!.images.find { it.width == 640 && it.height == 640 }

                    // format album year
                    val albumYear = sdf.parse(albumDetail.release_date)
                    val formattedDate = outputDateFormat.format(albumYear!!)

                    // get data to save in database
                    val artistList = albumDetail.artists.map { artist ->
                        AlbumEntity.ArtistEntity(
                            name = artist.name,
                            id = artist.id,
                            externalUrls = AlbumEntity.ExternalUrlsEntity(
                                spotify = artist.external_urls.spotify
                            )
                        )
                    }
                    val saveAlbumToDatabase = AlbumEntity(
                        albumType = albumDetail.album_type,
                        id = arguments.album.id,
                        albumName = albumDetail.albumName,
                        totalTracks = albumDetail.total_tracks,
                        artists = artistList,
                        images = albumDetail.images.map { image ->
                            AlbumEntity.ImageEntity(
                                width = image.width,
                                height = image.height,
                                url = image.url
                            )
                        },
                        externalUrls = AlbumEntity.ExternalUrlsEntity(
                            spotify = albumDetail.external_urls.spotify
                        ),
                        release_date = formattedDate,
                        trackList = albumDetail.tracks.items.map { tracks ->
                            AlbumEntity.TrackListEntity(
                                durationMs = tracks.duration_ms,
                                trackNumber = tracks.track_number,
                                albumName = tracks.name,
                                artists = artistList
                            )
                        }
                    )

                    // save album in database
                    viewModel.insertAlbum(saveAlbumToDatabase)

                    with(binding) {

                        // Display data from API
                        imgAlbum.load(albumImage?.url) {
                            error(R.drawable.album_error)
                            crossfade(true)
                            crossfade(1000)
                        }
                        txtAlbumName.text = albumDetail.albumName
                        val artists: List<Artist> = albumDetail.artists
                        val artistNames = artists.joinToString(separator = ", ") { it.name }
                        txtArtist.text = artistNames

                        txtInfo.text =
                            "${albumDetail.album_type.replaceFirstChar { it.uppercase() }} • " +
                                    "$formattedDate • " + totalTracks + " ${albumDetail.total_tracks}"

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
                        albumDetail.let { trackList ->
                            trackListAdapter.submitTrack(trackList.tracks.items)
                        }
                    }
                }
                is Resource.Failure -> {
                    hideLoading()
                    val errorAlert = MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Error")
                        .setMessage(response.message)
                        .setPositiveButton("Ok") { _, _ -> }
                    errorAlert.show()
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