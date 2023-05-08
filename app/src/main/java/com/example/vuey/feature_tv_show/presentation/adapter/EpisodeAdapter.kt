package com.example.vuey.feature_tv_show.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.databinding.LayoutEpisodeBinding
import com.example.vuey.feature_tv_show.data.api.season.Episode
import com.example.vuey.util.Constants
import com.example.vuey.util.utils.DateUtils
import com.example.vuey.util.utils.DiffUtils

class EpisodeAdapter : RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    private var episodeResult = listOf<Episode>()

    fun submitEpisode(newEpisode: List<Episode>) {
        val oldEpisode = DiffUtils(newEpisode, episodeResult)
        val result = DiffUtil.calculateDiff(oldEpisode)
        episodeResult = newEpisode
        result.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    class EpisodeViewHolder(private val binding: LayoutEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(episodeResult: Episode) {

            with(binding) {
                txtEpisodeName.text = "${episodeResult.episode_number}. " + episodeResult.name
                txtEpisodeRuntime.text = "${episodeResult.runtime}min"
                txtOverview.text = episodeResult.overview

                if (episodeResult.still_path != null) {
                    imgEpisodePoster.load(Constants.TMDB_IMAGE_ORIGINAL + episodeResult.still_path) {
                        crossfade(true)
                        crossfade(500)
                    }
                } else {
                    imgEpisodePoster.setImageResource(R.drawable.ic_movie_error)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EpisodeViewHolder {
        val binding = LayoutEpisodeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EpisodeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(episodeResult[position])
    }

    override fun getItemCount(): Int {
        return if (episodeResult.size > 10) {
            10
        } else {
            episodeResult.size
        }
    }
}