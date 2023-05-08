package com.example.vuey.feature_album.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vuey.feature_album.data.api.detail.AlbumItem
import com.example.vuey.feature_album.data.api.detail.Artist
import com.example.vuey.databinding.LayoutAlbumTrackListBinding
import com.example.vuey.util.utils.DiffUtils

class TrackListAdapter : RecyclerView.Adapter<TrackListAdapter.TrackViewHolder>() {

    private var trackResult = listOf<AlbumItem>()
    private var lastTrackInAlbum = 0

    fun submitTrack(newTrack: List<AlbumItem>) {
        val oldTrack = DiffUtils(trackResult, newTrack)
        val result = DiffUtil.calculateDiff(oldTrack)
        trackResult = newTrack
        result.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    class TrackViewHolder(private val binding: LayoutAlbumTrackListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(trackResult: AlbumItem, showAlbum : Boolean) {
            with(binding) {

                txtRank.text = trackResult.track_number.toString()
                val artists : List<Artist> = trackResult.artists
                val artistList = artists.joinToString(separator = ", ") { it.name }
                txtArtist.text = artistList
                txtTrackName.text = trackResult.name

                val seconds = trackResult.duration_ms / 1000
                val formattedDuration = String.format("%d:%02d", seconds / 60, seconds % 60)
                txtDuration.text = formattedDuration

                if (showAlbum && trackResult.track_number == 1) {
                    linearLayoutAlbum.visibility = View.VISIBLE
                    txtDiscNumber.text = "Album ${trackResult.disc_number}"
                } else {
                    linearLayoutAlbum.visibility = View.GONE
                }
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
        val currentTrack = trackResult[position]
        val showAlbumInfo = currentTrack.track_number == 1 || currentTrack.track_number <= lastTrackInAlbum
        holder.bind(currentTrack, showAlbumInfo)
    }

    override fun getItemCount(): Int {
        return trackResult.size
    }
}