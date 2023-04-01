package com.example.vuey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.data.database.model.AlbumEntity
import com.example.vuey.data.local.album.search.Album
import com.example.vuey.databinding.LayoutAlbumBinding
import com.example.vuey.ui.fragment.album.AlbumFragmentDirections
import com.example.vuey.util.DiffUtils

class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    private var albumResult = listOf<AlbumEntity>()

    fun submitAlbum(newAlbum: List<AlbumEntity>) {
        val oldAlbum = DiffUtils(albumResult, newAlbum)
        val result = DiffUtil.calculateDiff(oldAlbum)
        albumResult = newAlbum
        result.dispatchUpdatesTo(this)
    }

    class AlbumViewHolder(private val binding : LayoutAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(albumResult : AlbumEntity) {
            with(binding) {

                val image = albumResult.images.find { it.height == 640 && it.width == 640 }

                imgAlbum.load(image!!.url) {
                    crossfade(true)
                    crossfade(500)
                    error(R.drawable.album_error)
                }
                txtAlbum.text = albumResult.albumName
                val artists : List<AlbumEntity.ArtistEntity> = albumResult.artists
                val artistNames = artists.joinToString(separator = ", ") { it.name }
                txtArtist.text = artistNames

                layoutAlbum.setOnClickListener {
                    val album = Album(
                        albumName = albumResult.albumName,
                        artists = albumResult.artists.map { artist ->
                            Album.Artist(
                                id = artist.id,
                                external_urls = Album.ExternalUrls(
                                    spotify = artist.externalUrls.spotify
                                ),
                                name = artist.name
                            )
                        },
                        external_urls = Album.ExternalUrls(
                            spotify = albumResult.externalUrls.spotify
                        ),
                        id = albumResult.id,
                        images = albumResult.images.map { image ->
                            Album.Image(
                                height = image.height,
                                width = image.width,
                                url = image.url
                            )
                        },
                        album_type = albumResult.albumType,
                        total_tracks = albumResult.totalTracks
                    )
                    val trackList = albumResult.trackList.map { tracks ->
                        AlbumEntity.TrackListEntity(
                            durationMs = tracks.durationMs,
                            trackNumber = tracks.trackNumber,
                            albumName = tracks.albumName,
                            artists = tracks.artists
                        )
                    }
                    val albumEntity = AlbumEntity(
                        albumName = albumResult.albumName,
                        albumType = albumResult.albumType,
                        release_date = albumResult.release_date,
                        artists = artists,
                        id = albumResult.id,
                        totalTracks = albumResult.totalTracks,
                        images = albumResult.images,
                        trackList = trackList,
                        externalUrls = albumResult.externalUrls,
                    )
                    val action = AlbumFragmentDirections.actionAlbumFragmentToAlbumDetailFragment(
                        albumEntity = albumEntity,
                        album = album
                    )
                    it.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = LayoutAlbumBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AlbumViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return albumResult.size
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albumResult[position])
    }
}