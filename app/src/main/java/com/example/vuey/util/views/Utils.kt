package com.example.vuey.util.views

import android.view.View
import com.example.vuey.feature_tv_show.data.api.search.SearchTvShow
import com.example.vuey.feature_tv_show.data.database.entity.TvShowEntity
import com.example.vuey.feature_tv_show.data.database.entity.TvShowEpisodeEntity
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale

fun Double.formatVoteAverage() : String = String.format("%.1f", this)

object DateUtils {
    fun formatAirDate(airDate: String?): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val year = try {
            sdf.parse(airDate!!)
        } catch (e: Exception) {
            null
        }
        return year?.let { date ->
            outputDateFormat.format(date)
        }
    }
}

fun SearchTvShow.toTvShowEntity() : TvShowEntity {
    return TvShowEntity(
        id = this.id,
        tvShowStatus = "",
        tvShowOverview = this.overview,
        tvShowName = this.name,
        tvShowBackdropPath = this.backdrop_path.toString(),
        tvShowPosterPath = this.poster_path.toString(),
        tvShowVoteAverage = this.vote_average.formatVoteAverage(),
        tvShowFirstAirDate = this.first_air_date,
        tvShowGenreList = emptyList(),
        tvShowEpisodeRun = 0,
        tvShowSpokenLanguage = emptyList(),
        tvShowCast = emptyList(),
        tvShowSeason = emptyList()
    )
}

fun TvShowEntity.toSearchTv() : SearchTvShow {
    return SearchTvShow(
        backdrop_path = this.tvShowBackdropPath,
        first_air_date = this.tvShowFirstAirDate,
        id = this.id,
        name = this.tvShowName,
        overview = this.tvShowOverview,
        popularity = 0.0,
        poster_path = this.tvShowPosterPath,
        vote_average = this.tvShowVoteAverage.replace(",", ".").toDouble(),
    )
}

fun showSnackbar(view : View, message : String, duration : Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, message, duration).show()
}