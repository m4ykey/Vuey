package com.example.vuey.data.remote.response.movie

import com.example.vuey.data.models.movie.detail.Genre
import com.example.vuey.data.models.movie.detail.SpokenLanguage

data class MovieDetailResponse(
    //val adult: Boolean,
    val backdrop_path: String,
    //val belongs_to_collection: Any,
    //val budget: Int,
    val genres: List<Genre>,
    //val homepage: String,
    val id: Int,
    //val imdb_id: String,
    //val original_language: String,
    //val original_title: String,
    val overview: String,
    //val popularity: Double,
    val poster_path: String,
    val release_date: String,
    //val revenue: Int,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    //val status: String,
    //val tagline: String,
    val title: String,
    //val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)