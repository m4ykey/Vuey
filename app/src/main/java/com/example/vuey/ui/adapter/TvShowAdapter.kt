package com.example.vuey.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.data.models.tv_show.SearchTvShow
import com.example.vuey.databinding.LayoutTvShowBinding
import com.example.vuey.ui.screens.tv_show.TvShowSearchFragmentDirections
import com.example.vuey.util.Constants.TMDB_IMAGE
import com.example.vuey.util.views.DiffUtils
import java.text.SimpleDateFormat
import java.util.Locale

class TvShowAdapter : RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder>() {

    private var tvShowResult = listOf<SearchTvShow>()

    fun submitTvShow(newTvShow : List<SearchTvShow>) {
        val oldTvShow = DiffUtils(newTvShow, tvShowResult)
        val result = DiffUtil.calculateDiff(oldTvShow)
        tvShowResult = newTvShow
        result.dispatchUpdatesTo(this)
    }

    class TvShowViewHolder(private val binding : LayoutTvShowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tvShowResult : SearchTvShow) {
            with(binding) {

                if (tvShowResult.poster_path != null) {
                    imgTvShow.load(TMDB_IMAGE + tvShowResult.poster_path) {
                        crossfade(true)
                        crossfade(500)
                    }
                } else {
                    imgTvShow.setImageResource(R.drawable.ic_movie_error)
                }

                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
                if (tvShowResult.first_air_date != null && !tvShowResult.first_air_date.isNullOrEmpty()) {
                    val tvShowYear = sdf.parse(tvShowResult.first_air_date)
                    val formattedDate = outputDateFormat.format(tvShowYear!!)
                    txtReleaseDate.text = formattedDate
                } else {
                    txtReleaseDate.visibility = View.GONE
                }

                val voteAverage = tvShowResult.vote_average
                val formattedVote = String.format("%.1f", voteAverage)
                txtVoteAverage.text = formattedVote

                txtTvShowTitle.text = tvShowResult.name
                txtDescription.text = tvShowResult.overview

                layoutTvShow.setOnClickListener {
                    val action = TvShowSearchFragmentDirections.actionTvShowSearchFragmentToTvShowDetailFragment(
                        tvShow = tvShowResult
                    )
                    it.findNavController().navigate(action)
                }

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TvShowViewHolder {
        val binding = LayoutTvShowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TvShowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        holder.bind(tvShowResult[position])
    }

    override fun getItemCount(): Int {
        return tvShowResult.size
    }
}