package com.example.vuey

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.databinding.LayoutAlbumBinding
import com.example.vuey.model.Album

class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    private var albumResult = listOf<Album>()

    fun submitAlbum(newAlbum : List<Album>) {
        val oldAlbum = DiffUtils(albumResult, newAlbum)
        val result = DiffUtil.calculateDiff(oldAlbum)
        albumResult = newAlbum
        result.dispatchUpdatesTo(this)
    }

    class AlbumViewHolder(private val binding : LayoutAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(albumResult : Album) {
            with(binding){
                val extraLarge = albumResult.image.find { it.size == "extralarge"}
                imgAlbum.load(extraLarge?.text)
                txtAlbum.text = albumResult.name
                txtArtist.text = albumResult.artist
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumViewHolder {
        val binding = LayoutAlbumBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albumResult[position])
    }

    override fun getItemCount(): Int {
        return albumResult.size
    }
}