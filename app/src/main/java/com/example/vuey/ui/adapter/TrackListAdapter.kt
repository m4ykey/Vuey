package com.example.vuey.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vuey.data.local.album.detail.AlbumItem
import com.example.vuey.databinding.LayoutAlbumTrackListBinding
import com.example.vuey.util.DiffUtils

class TrackListAdapter : RecyclerView.Adapter<TrackListAdapter.TrackViewHolder>() {

    private var trackResult = listOf<AlbumItem>()

    fun submitTrack(newTrack: List<AlbumItem>) {
        val oldTrack = DiffUtils(trackResult, newTrack)
        val result = DiffUtil.calculateDiff(oldTrack)
        trackResult = newTrack
        result.dispatchUpdatesTo(this)
    }

    class TrackViewHolder(private val binding: LayoutAlbumTrackListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trackResult: AlbumItem) {
            with(binding) {

                txtRank.text = trackResult.track_number.toString()
                txtArtist.text = trackResult.artists[0].name
                txtTrackName.text = trackResult.name

                val seconds = trackResult.duration_ms / 1000
                val formattedDuration = String.format("%d:%02d", seconds / 60, seconds % 60)
                txtDuration.text = formattedDuration

                linearLayoutTracks.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trackResult.external_urls.spotify))
                    root.context.startActivity(intent)
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
        holder.bind(trackResult[position])
    }

    override fun getItemCount(): Int {
        return trackResult.size
    }
}