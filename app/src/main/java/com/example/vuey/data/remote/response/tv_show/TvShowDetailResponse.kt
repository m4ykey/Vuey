package com.example.vuey.data.remote.response.tv_show

import android.os.Parcelable
import com.example.vuey.data.models.tv_show.Genre
import com.example.vuey.data.models.tv_show.Season
import com.example.vuey.data.models.tv_show.SpokenLanguage
import kotlinx.parcelize.Parcelize

@Parcelize
data class TvShowDetailResponse(
    val backdrop_path: String,
    val episode_run_time: List<Int>,
    val first_air_date: String,
    val genres: List<Genre>,
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String,
    var seasons: List<Season>,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val vote_average: Double,
    val vote_count: Int
) : Parcelable