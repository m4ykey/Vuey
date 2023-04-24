package com.example.vuey.data.models.movie.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchMovie(
    val backdrop_path: String?,
    val id: Int,
    val overview: String,
    val poster_path: String?,
    val release_date: String,
    val title: String,
    val vote_average: Double,
    val vote_count: Int
) : Parcelable