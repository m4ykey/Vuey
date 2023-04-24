package com.example.vuey.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.data.models.tv_show.Season
import com.example.vuey.databinding.LayoutTvSeasonBinding
import com.example.vuey.ui.screens.tv_show.TvShowDetailFragmentDirections
import com.example.vuey.util.Constants.TMDB_IMAGE
import com.example.vuey.util.views.DiffUtils

class SeasonsAdapter(
    private val tvShowId: Int
) : RecyclerView.Adapter<SeasonsAdapter.SeasonViewHolder>() {

    private var seasonsResult = listOf<Season>()

    fun submitSeason(newSeason: List<Season>) {
        val oldSeason = DiffUtils(newSeason, seasonsResult)
        val result = DiffUtil.calculateDiff(oldSeason)
        seasonsResult = newSeason
        result.dispatchUpdatesTo(this)
    }

    class SeasonViewHolder(private val binding: LayoutTvSeasonBinding, private val tvShowId : Int) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(seasonsResult: Season) {

            with(binding) {

                layoutSeason.setOnClickListener {
                    val action = TvShowDetailFragmentDirections.actionTvShowDetailFragmentToTvShowSeasonsFragment(
                        tvShowId = tvShowId,
                        season = seasonsResult
                    )
                    it.findNavController().navigate(action)
                }

                if (seasonsResult.poster_path != null) {
                    imgTvShowPoster.load(TMDB_IMAGE + seasonsResult.poster_path) {
                        crossfade(true)
                        crossfade(500)
                    }
                    txtSeason.text = root.context.getString(R.string.season) + " ${seasonsResult.season_number}"
                } else {
                    imgTvShowPoster.setImageResource(R.drawable.ic_tv)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SeasonViewHolder {
        val binding = LayoutTvSeasonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SeasonViewHolder(binding, tvShowId)
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) {
        holder.bind(seasonsResult[position])
    }

    override fun getItemCount(): Int {
        return seasonsResult.size
    }
}
