package com.example.vuey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.data.local.album.Item
import com.example.vuey.databinding.LayoutAlbumBinding
import com.example.vuey.ui.fragment.search.SearchFragmentDirections
import com.example.vuey.util.DiffUtils

class SearchAlbumAdapter : RecyclerView.Adapter<SearchAlbumAdapter.AlbumViewHolder>() {

    private var albumResult = listOf<Item>()

    fun submitAlbum(newAlbum: List<Item>) {
        val oldAlbum = DiffUtils(albumResult, newAlbum)
        val result = DiffUtil.calculateDiff(oldAlbum)
        albumResult = newAlbum
        result.dispatchUpdatesTo(this)
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

    class AlbumViewHolder(
        private val binding: LayoutAlbumBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(albumResult: Item) {
            with(binding) {

                val image = albumResult.images.find { it.height == 640 && it.width == 640 }

                imgAlbum.load(image!!.url) {
                    crossfade(true)
                    crossfade(500)
                    error(R.drawable.album_error)
                }
                txtAlbum.text = albumResult.albumName
                txtArtist.text = albumResult.artists[0].name

                layoutAlbum.setOnClickListener {
                    val action = SearchFragmentDirections.actionSearchFragmentToAlbumDetailFragment()
                    it.findNavController().navigate(action)
                }
            }
        }
    }
}


