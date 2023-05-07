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
                txtName.text = episodeResult.name
                txtEpisodeNumber.text =
                    root.context.getString(R.string.episode) + " ${episodeResult.episode_number}"

                if (episodeResult.air_date != null && !episodeResult.air_date.isNullOrEmpty()) {
                    txtInfo.text = DateUtils.formatAirDate(episodeResult.air_date) + " • " + episodeResult.runtime + "min"
                } else {
                    txtInfo.text = "${episodeResult.runtime}min"
                }

                if (episodeResult.still_path != null) {
                    imgEpisodePoster.load(Constants.TMDB_IMAGE + episodeResult.still_path) {
                        crossfade(true)
                        crossfade(2000)
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
        return episodeResult.size
    }
}