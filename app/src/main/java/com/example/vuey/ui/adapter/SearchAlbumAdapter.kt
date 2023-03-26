package com.example.vuey.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.data.local.album.search.Album
import com.example.vuey.databinding.LayoutAlbumBinding
import com.example.vuey.ui.fragment.search.SearchFragmentDirections
import com.example.vuey.util.DiffUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SearchAlbumAdapter : RecyclerView.Adapter<SearchAlbumAdapter.AlbumViewHolder>() {

    private var albumResult = listOf<Album>()

    fun submitAlbum(newAlbum: List<Album>) {
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
        fun bind(albumResult: Album) {
            with(binding) {
                val extraLarge = albumResult.image.find { it.size == "extralarge" }
                imgAlbum.load(extraLarge?.image) {
                    crossfade(500)
                    crossfade(true)
                    error(R.drawable.album_error)
                }
                txtAlbum.text = albumResult.albumName
                txtArtist.text = albumResult.artist

                layoutAlbum.setOnClickListener {
                    if (albumResult.albumName.isNotEmpty() && albumResult.artist.isNotEmpty() && extraLarge!!.image.isNotEmpty()) {
                        val action =
                            SearchFragmentDirections.actionSearchFragmentToAlbumDetailFragment(
                                albumResult
                            )
                        it.findNavController().navigate(action)
                    } else {
                        val errorDialog = MaterialAlertDialogBuilder(root.context)
                            .setTitle(R.string.album_error_title)
                            .setMessage(R.string.album_error_message)
                            .setPositiveButton("Ok") { _, _ -> }
                            .create()
                        errorDialog.show()
                    }
                }
            }
        }
    }
}

