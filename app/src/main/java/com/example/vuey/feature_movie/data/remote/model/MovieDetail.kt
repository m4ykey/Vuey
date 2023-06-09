package com.example.vuey.feature_movie.data.remote.model

data class MovieDetail(
    val backdrop_path: String?,
    val genres: List<Genre>,
    val id: Int,
    val overview: String,
    val poster_path: String?,
    val release_date: String,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    val title: String,
    val vote_average: Double,
)