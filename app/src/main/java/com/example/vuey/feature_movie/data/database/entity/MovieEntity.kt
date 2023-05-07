package com.example.vuey.feature_movie.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.util.Constants.MOVIE_TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = MOVIE_TABLE_NAME)
data class MovieEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("movieId") val id: Int,
    @ColumnInfo("movieCastList") val movieCastList: List<MovieCastEntity>,
    @ColumnInfo("movieSpokenLanguages") val movieSpokenLanguageList: List<MovieSpokenLanguageEntity>,
    @ColumnInfo("movieGenreList") val movieGenreList: List<MovieGenreEntity>,
    @ColumnInfo("movieBackdropPath") val movieBackdropPath: String,
    @ColumnInfo("movieOverview") val movieOverview: String,
    @ColumnInfo("moviePosterPath") val moviePosterPath: String,
    @ColumnInfo("movieReleaseDate") val movieReleaseDate: String,
    @ColumnInfo("movieTitle") val movieTitle: String,
    @ColumnInfo("movieVoteAverage") val movieVoteAverage: String,
    @ColumnInfo("movieRuntime") val movieRuntime: String,
    @ColumnInfo("saveTime") val saveTime: Long = System.currentTimeMillis()
) : Parcelable


