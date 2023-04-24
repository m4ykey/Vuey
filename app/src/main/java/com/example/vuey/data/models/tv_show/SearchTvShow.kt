package com.example.vuey.data.models.tv_show

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchTvShow(
    val backdrop_path: String?,
    val first_air_date: String,
    val id: Int,
    val name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val vote_average: Double,
    val vote_count: Int
) : Parcelable