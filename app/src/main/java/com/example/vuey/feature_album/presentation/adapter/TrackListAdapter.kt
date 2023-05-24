package com.example.vuey.feature_album.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vuey.feature_album.data.remote.model.Artist
import com.example.vuey.databinding.LayoutAlbumTrackListBinding
import com.example.vuey.feature_album.data.remote.model.Tracks
import com.example.vuey.util.utils.DiffUtils

class TrackListAdapter : RecyclerView.Adapter<TrackListAdapter.TrackViewHolder>() {

    private var trackResult = listOf<Tracks.AlbumItem>()

    fun submitTrack(newTrack: List<Tracks.AlbumItem>) {
        val oldTrack = DiffUtils(trackResult, newTrack)
        val result = DiffUtil.calculateDiff(oldTrack)
        trackResult = newTrack
        result.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    class TrackViewHolder(private val binding: LayoutAlbumTrackListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(trackResult: Tracks.AlbumItem) {
            with(binding) {

                val artistList : List<Artist> = trackResult.artistList
                val artists = artistList.joinToString(separator = ", ") { it.artistName }
                txtArtist.text = artists
                txtTrackName.text = trackResult.albumName

                val seconds = trackResult.durationMs / 1000
                val trackDuration = String.format("%d:%02d", seconds / 60, seconds % 60)
                txtDuration.text = trackDuration
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = LayoutAlbumTrackListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackResult[position])
    }

    override fun getItemCount(): Int {
        return trackResult.size
    }
}