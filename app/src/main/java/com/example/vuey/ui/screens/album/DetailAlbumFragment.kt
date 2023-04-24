package com.example.vuey.ui.screens.album

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.example.vuey.data.models.album.detail.AlbumItem
import com.example.vuey.data.models.album.detail.Artist
import com.example.vuey.data.models.album.detail.ExternalUrls
import com.example.vuey.databinding.FragmentAlbumDetailBinding
import com.example.vuey.ui.adapter.TrackListAdapter
import com.example.vuey.util.network.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
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

        val bottomNavigationView: BottomNavigationView =
            requireActivity().findViewById(R.id.bottomMenu)
        bottomNavigationView.visibility = View.GONE

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
                    type = arguments.albumEntity.albumType,
                    disc_number = trackEntity.discNumber
                )
            }
            trackListAdapter.submitTrack(trackList)
            txtAlbumTime.text = databaseArguments.albumLength

            val saveAlbumToDatabase = AlbumEntity(
                albumLength = databaseArguments.albumLength,
                albumName = databaseArguments.albumName,
                albumType = databaseArguments.albumType,
                id = databaseArguments.id,
                artists = databaseArguments.artists,
                externalUrls = databaseArguments.externalUrls,
                images = databaseArguments.images,
                release_date = databaseArguments.release_date,
                totalTracks = databaseArguments.totalTracks,
                trackList = databaseArguments.trackList
            )

            val sharedPref = context?.getSharedPreferences("album_pref", Context.MODE_PRIVATE)
            var isAlbumLiked = sharedPref?.getBoolean("isAlbumLiked", false)

            if (isAlbumLiked == true) {
                imgSave.setImageResource(R.drawable.ic_save)
            } else {
                imgSave.setImageResource(R.drawable.ic_save_outlined)
            }

            imgSave.setOnClickListener {
                isAlbumLiked = !isAlbumLiked!!

                sharedPref?.edit()?.putBoolean("isAlbumLiked", isAlbumLiked!!)?.apply()

                if (isAlbumLiked as Boolean) {
                    Snackbar.make(view, R.string.added_to_library, Snackbar.LENGTH_SHORT).show()
                    imgSave.setImageResource(R.drawable.ic_save)
                    viewModel.insertAlbum(saveAlbumToDatabase)
                } else {
                    Snackbar.make(view, R.string.removed_from_library, Snackbar.LENGTH_SHORT).show()
                    imgSave.setImageResource(R.drawable.ic_save_outlined)
                    viewModel.deleteAlbum(saveAlbumToDatabase)
                }
            }
        }

        viewModel.getAlbum(arguments.album.id)
        Log.i("AlbumId", "onViewCreated: ${arguments.album.id}")
        viewModel.albumDetail.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideLoading()

                    with(binding) {

                        // Get response data
                        val albumDetail = response.data!!

                        // find album image
                        val albumImage =
                            albumDetail.images.find { it.width == 640 && it.height == 640 }

                        var albumTime = 0
                        for (track in albumDetail.tracks.items) {
                            albumTime += track.duration_ms
                        }

                        val albumTimeHour = albumTime / (1000 * 60 * 60)
                        val albumTimeMinute = (albumTime / (1000 * 60)) % 60

                        val formattedAlbumTime = if (albumTimeHour == 0) {
                            String.format("%d min", albumTimeMinute)
                        } else {
                            String.format("%d h %d min", albumTimeHour, albumTimeMinute)
                        }
                        txtAlbumTime.text = formattedAlbumTime

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
                        albumDetail.let { trackList -> trackListAdapter.submitTrack(trackList.tracks.items) }

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
                                    artists = artistList,
                                    discNumber = tracks.disc_number
                                )
                            },
                            albumLength = formattedAlbumTime
                        )

                        val sharedPref = context?.getSharedPreferences("album_pref", Context.MODE_PRIVATE)
                        var isAlbumLiked = sharedPref?.getBoolean("isAlbumLiked", false)

                        if (isAlbumLiked == true) {
                            imgSave.setImageResource(R.drawable.ic_save)
                        } else {
                            imgSave.setImageResource(R.drawable.ic_save_outlined)
                        }

                        imgSave.setOnClickListener {
                            isAlbumLiked = !isAlbumLiked!!

                            sharedPref?.edit()?.putBoolean("isAlbumLiked", isAlbumLiked!!)?.apply()

                            if (isAlbumLiked as Boolean) {
                                Snackbar.make(view, R.string.added_to_library, Snackbar.LENGTH_SHORT).show()
                                imgSave.setImageResource(R.drawable.ic_save)
                                viewModel.insertAlbum(saveAlbumToDatabase)
                            } else {
                                Snackbar.make(view, R.string.removed_from_library, Snackbar.LENGTH_SHORT).show()
                                imgSave.setImageResource(R.drawable.ic_save_outlined)
                                viewModel.deleteAlbum(saveAlbumToDatabase)
                            }
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