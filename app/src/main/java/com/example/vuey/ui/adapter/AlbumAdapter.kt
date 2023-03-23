package com.example.vuey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.databinding.LayoutAlbumBinding
import com.example.vuey.util.DiffUtils
import com.example.vuey.data.local.album.search.Album

class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    private var albumResult = listOf<Album>()
    private lateinit var albumListener : OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        albumListener = listener
    }

    fun submitAlbum(newAlbum : List<Album>) {
        val oldAlbum = DiffUtils(albumResult, newAlbum)
        val result = DiffUtil.calculateDiff(oldAlbum)
        albumResult = newAlbum
        result.dispatchUpdatesTo(this)
    }

    class AlbumViewHolder(
        private val binding : LayoutAlbumBinding,
        listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(albumResult : Album) {
            with(binding){
                val extraLarge = albumResult.image.find { it.size == "extralarge"}
                imgAlbum.load(extraLarge?.image) {
                    crossfade(500)
                    crossfade(true)
                    error(R.drawable.album_error)
                }
                txtAlbum.text = albumResult.albumName
                txtArtist.text = albumResult.artist
            }
        }
        init {
            binding.layoutAlbum.setOnClickListener {
                listener.onItemClick(adapterPosition)
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
        return AlbumViewHolder(binding, albumListener)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albumResult[position])
    }

    override fun getItemCount(): Int {
        return albumResult.size
    }
}