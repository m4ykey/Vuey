package com.example.vuey.feature_movie.data.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.util.Constants.MOVIE_CAST_TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = MOVIE_CAST_TABLE_NAME)
data class MovieCastEntity(
    val name: String,
    val profile_path: String,
    val character: String,
    val movieId : Int,
    @PrimaryKey(autoGenerate = false)
    val id : Int
) : Parcelable