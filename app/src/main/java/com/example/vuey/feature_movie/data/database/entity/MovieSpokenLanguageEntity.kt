package com.example.vuey.feature_movie.data.database.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieSpokenLanguageEntity(
    val name: String
) : Parcelable