package com.example.vuey.feature_album.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.databinding.LayoutAlbumBinding
import com.example.vuey.feature_album.data.api.search.Album
import com.example.vuey.feature_album.data.database.entity.AlbumEntity
import com.example.vuey.feature_album.presentation.AlbumFragmentDirections
import com.example.vuey.feature_album.presentation.SearchAlbumFragmentDirections
import com.example.vuey.util.utils.DiffUtils
import com.example.vuey.util.utils.toAlbum
import com.example.vuey.util.utils.toAlbumEntity

class AlbumAdapter(
    private val isFromApi : Boolean
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    private var albumResult = listOf<Album>()
    private var albumResultEntity = listOf<AlbumEntity>()

    fun submitAlbum(newAlbum: List<Album>) {
        val oldAlbum = DiffUtils(albumResult, newAlbum)
        val result = DiffUtil.calculateDiff(oldAlbum)
        albumResult = newAlbum
        result.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    fun submitAlbumEntity(newAlbum: List<AlbumEntity>) {
        val oldAlbum = DiffUtils(albumResultEntity, newAlbum)
        val result = DiffUtil.calculateDiff(oldAlbum)
        albumResultEntity = newAlbum
        result.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class AlbumViewHolder(private val binding: LayoutAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(albumResult : Album) {

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
                        albumEntity = albumResult.toAlbumEntity()
                    )
                    it.findNavController().navigate(action)
                }
            }
        }

        fun bind(albumResultEntity : AlbumEntity) {

            val images = albumResultEntity.imageList.find { it.width == 640 && it.height == 640 }
            val artists : List<AlbumEntity.ArtistEntity> = albumResultEntity.artistList
            val artistNames = artists.joinToString(separator = ", ") { it.name }

            with(binding) {
                imgAlbum.load(images?.url) {
                    crossfade(true)
                    crossfade(500)
                    error(R.drawable.album_error)
                }
                txtAlbum.text = albumResultEntity.albumName
                txtArtist.text = artistNames
                layoutAlbum.setOnClickListener {
                    val action = AlbumFragmentDirections.actionAlbumFragmentToAlbumDetailFragment(
                        album = albumResultEntity.toAlbum(),
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