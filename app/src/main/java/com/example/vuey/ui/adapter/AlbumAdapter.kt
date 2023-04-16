package com.example.vuey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.data.database.model.AlbumEntity
import com.example.vuey.data.models.album.search.Album
import com.example.vuey.databinding.LayoutAlbumBinding
import com.example.vuey.ui.screens.album.AlbumFragmentDirections
import com.example.vuey.ui.screens.album.AlbumViewModel
import com.example.vuey.ui.screens.search.SearchAlbumFragmentDirections
import com.example.vuey.util.views.DiffUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AlbumAdapter(
    private val viewModel: AlbumViewModel,
    private val isFromApi : Boolean
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    private var albumResult = listOf<Album>()
    private var albumResultEntity = listOf<AlbumEntity>()

    fun submitAlbum(newAlbum: List<Album>) {
        val oldAlbum = DiffUtils(albumResult, newAlbum)
        val result = DiffUtil.calculateDiff(oldAlbum)
        albumResult = newAlbum
        result.dispatchUpdatesTo(this)
    }

    fun submitAlbumEntity(newAlbum: List<AlbumEntity>) {
        val oldAlbum = DiffUtils(albumResultEntity, newAlbum)
        val result = DiffUtil.calculateDiff(oldAlbum)
        albumResultEntity = newAlbum
        result.dispatchUpdatesTo(this)
    }

    inner class AlbumViewHolder(private val binding: LayoutAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(albumResult : Album) {

            val albumEntity = AlbumEntity(
                albumName = albumResult.albumName,
                albumType = albumResult.album_type,
                artists = albumResult.artists.map {
                    AlbumEntity.ArtistEntity(
                        externalUrls = AlbumEntity.ExternalUrlsEntity(
                            spotify = it.external_urls.spotify
                        ),
                        id = it.id,
                        name = it.name
                    )
                },
                externalUrls = AlbumEntity.ExternalUrlsEntity(
                    spotify = albumResult.external_urls.spotify
                ),
                id = albumResult.id,
                images = albumResult.images.map {
                    AlbumEntity.ImageEntity(
                        height = it.height,
                        url = it.url,
                        width = it.width
                    )
                },
                totalTracks = albumResult.total_tracks,
                trackList = emptyList(),
                release_date = ""
            )

            val image = albumResult.images.find { it.width == 640 && it.height == 640 }
            val artists : List<Album.Artist> = albumResult.artists
            val artistNames = artists.joinToString(separator = ", ") { it.name }

            with(binding) {
                imgAlbum.load(image?.url) {
                    crossfade(true)
                    crossfade(500)
                    error(R.drawable.album_error)
                }
                txtAlbum.text = albumResult.albumName
                txtArtist.text = artistNames

                layoutAlbum.setOnClickListener {
                    val action = SearchAlbumFragmentDirections.actionSearchAlbumFragmentToAlbumDetailFragment(
                        album = albumResult,
                        albumEntity = albumEntity
                    )
                    it.findNavController().navigate(action)
                }
            }
        }
        fun bind(albumResultEntity : AlbumEntity) {

            val image = albumResultEntity.images.find { it.width == 640 && it.height == 640 }
            val artists : List<AlbumEntity.ArtistEntity> = albumResultEntity.artists
            val artistNames = artists.joinToString(separator = ", ") { it.name }

            val album = Album(
                albumName = albumResultEntity.albumName,
                album_type = albumResultEntity.albumType,
                id = albumResultEntity.id,
                total_tracks = albumResultEntity.totalTracks,
                artists = albumResultEntity.artists.map {
                    Album.Artist(
                        id = it.id,
                        name = it.name,
                        external_urls = Album.ExternalUrls(
                            spotify = it.externalUrls.spotify
                        )
                    )
                },
                external_urls = Album.ExternalUrls(
                    spotify = albumResultEntity.externalUrls.spotify
                ),
                images = albumResultEntity.images.map {
                    Album.Image(
                        height = it.height,
                        width = it.width,
                        url = it.url
                    )
                }
            )

            with(binding) {
                imgAlbum.load(image?.url) {
                    crossfade(true)
                    crossfade(500)
                    error(R.drawable.album_error)
                }
                txtAlbum.text = albumResultEntity.albumName
                txtArtist.text = artistNames
                layoutAlbum.setOnLongClickListener {
                    MaterialAlertDialogBuilder(root.context)
                        .setTitle(R.string.delete_album)
                        .setMessage(root.context.getString(R.string.delete_album_message) + " ${albumResultEntity.albumName}?")
                        .setPositiveButton(R.string.yes) { _, _ ->
                            viewModel.deleteAlbum(albumResultEntity)
                        }
                        .setNegativeButton(R.string.no) { _, _ -> }
                        .show()
                    true
                }
                layoutAlbum.setOnClickListener {
                    val action = AlbumFragmentDirections.actionAlbumFragmentToAlbumDetailFragment(
                        album = album,
                        albumEntity = albumResultEntity
                    )
                    it.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = LayoutAlbumBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlbumViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (isFromApi) {
            albumResult.size
        } else {
            albumResultEntity.size
        }
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        if (isFromApi) {
            holder.bind(albumResult[position])
        } else {
            holder.bind(albumResultEntity[position])
        }
    }
}