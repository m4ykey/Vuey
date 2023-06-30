package com.example.vuey.feature_album.presentation.adapter

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.databinding.LayoutTopTracksBinding
import com.example.vuey.util.utils.DiffUtils
import com.example.vuey.util.utils.PreDrawListener

class TopTracksAdapter : RecyclerView.Adapter<TopTracksAdapter.TopTracksViewHolder>() {

    private var topTracks = emptyList<Track>()

    fun submitTopTracks(newData : List<Track>) {
        val oldData = topTracks.toList()
        topTracks = newData
        DiffUtil.calculateDiff(DiffUtils(oldData, newData)).dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class TopTracksViewHolder(private val binding : LayoutTopTracksBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(topTracks : Track) {

            val image = topTracks.album.imageList.find { it.height == 640 && it.width == 640 }

            with(binding) {

                val preDraw = PreDrawListener(imgAlbum, layoutAlbum)
                imgAlbum.apply {
                    load(image?.url) { error(R.drawable.album_error) }
                    viewTreeObserver.addOnPreDrawListener(preDraw)
                }

                txtAlbum.text = topTracks.album.albumName
                txtArtist.text = topTracks.artists.joinToString(separator = ", ") { it.artistName }

                imgPlayPreview.setOnClickListener {
                    val mediaPlayer = MediaPlayer()

                    mediaPlayer.setDataSource(topTracks.preview_url)

                    mediaPlayer.prepare()
                    mediaPlayer.start()
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopTracksAdapter.TopTracksViewHolder {
        val binding = LayoutTopTracksBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TopTracksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopTracksAdapter.TopTracksViewHolder, position: Int) {
        holder.bind(topTracks[position])
    }

    override fun getItemCount(): Int {
        return topTracks.size
    }
}