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
import com.example.vuey.ui.fragment.album.AlbumViewModel
import com.example.vuey.util.DiffUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AlbumAdapter(
    private val viewModel: AlbumViewModel
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    private var albumResult = listOf<AlbumEntity>()

    fun submitAlbum(newAlbum: List<AlbumEntity>) {
        val oldAlbum = DiffUtils(albumResult, newAlbum)
        val result = DiffUtil.calculateDiff(oldAlbum)
        albumResult = newAlbum
        result.dispatchUpdatesTo(this)
    }

    class AlbumViewHolder(private val binding: LayoutAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(albumResult: AlbumEntity, viewModel: AlbumViewModel) {
            with(binding) {

                val image = albumResult.images.find { it.height == 640 && it.width == 640 }

                imgAlbum.load(image!!.url) {
                    crossfade(true)
                    crossfade(500)
                    error(R.drawable.album_error)
                }
                txtAlbum.text = albumResult.albumName
                val artists: List<AlbumEntity.ArtistEntity> = albumResult.artists
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
                    val action = AlbumFragmentDirections.actionAlbumFragmentToAlbumDetailFragment(
                        albumEntity = albumResult,
                        album = album
                    )
                    it.findNavController().navigate(action)
                }
                layoutAlbum.setOnLongClickListener {
                    MaterialAlertDialogBuilder(root.context)
                        .setTitle(R.string.delete_album)
                        .setMessage(root.context.resources.getString(R.string.delete_album_message) + " " + albumResult.albumName + "?")
                        .setPositiveButton(R.string.yes) { _, _ ->
                            viewModel.deleteAlbum(albumResult)
                        }
                        .setNegativeButton(R.string.no) { _, _ -> }
                        .show()
                    true
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
        holder.bind(albumResult[position], viewModel)
    }
}