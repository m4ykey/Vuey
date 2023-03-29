package com.example.vuey.ui.adapter
//
//import android.content.Intent
//import android.net.Uri
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.example.vuey.data.local.album.detail.Tracks
//import com.example.vuey.databinding.LayoutAlbumTrackListBinding
//import com.example.vuey.util.DiffUtils
//
//class TrackListAdapter : RecyclerView.Adapter<TrackListAdapter.TrackViewHolder>() {
//
//    private var trackResult = listOf<Tracks.Track>()
//
//    fun submitTrack(newTrack: List<Tracks.Track>) {
//        val oldTrack = DiffUtils(trackResult, newTrack)
//        val result = DiffUtil.calculateDiff(oldTrack)
//        trackResult = newTrack
//        result.dispatchUpdatesTo(this)
//    }
//
//    class TrackViewHolder(private val binding: LayoutAlbumTrackListBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(trackResult: Tracks.Track) {
//            with(binding) {
//                txtArtist.text = trackResult.artist.name
//                txtTrackName.text = trackResult.trackName
//                txtRank.text = trackResult.attr.rank.toString()
//                val duration = trackResult.duration.toString()
//
//                val formattedDuration =
//                    "%d:%02d".format(duration.toInt() / 60, duration.toInt() % 60)
//                txtDuration.text = formattedDuration
//
//                linearLayoutTracks.setOnClickListener {
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trackResult.url))
//                    root.context.startActivity(intent)
//                }
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
//        val binding = LayoutAlbumTrackListBinding.inflate(
//            LayoutInflater.from(parent.context), parent, false
//        )
//        return TrackViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
//        holder.bind(trackResult[position])
//    }
//
//    override fun getItemCount(): Int {
//        return trackResult.size
//    }
//}