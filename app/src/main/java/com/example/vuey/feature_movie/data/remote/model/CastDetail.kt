package com.example.vuey.feature_movie.data.remote.model

import com.google.gson.annotations.SerializedName

data class CastDetail(
    @SerializedName("cast_id")
    val castId: Int,
    val character: String,
    val id: Int,
    val name: String,
    @SerializedName("profile_path")
    val profilePath: String
)