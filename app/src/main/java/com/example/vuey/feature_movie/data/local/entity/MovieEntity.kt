package com.example.vuey.feature_movie.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.util.Constants
import com.example.vuey.util.Constants.MOVIE_TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = MOVIE_TABLE_NAME)
data class MovieEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("movieId") val id: Int,
    @ColumnInfo("movieCastList") val movieCastList: List<MovieCastEntity> = emptyList(),
    @ColumnInfo("movieSpokenLanguages") val movieSpokenLanguageList: List<MovieSpokenLanguageEntity> = emptyList(),
    @ColumnInfo("movieGenreList") val movieGenreList: List<MovieGenreEntity> = emptyList(),
    @ColumnInfo("movieBackdropPath") val movieBackdropPath: String,
    @ColumnInfo("movieOverview") val movieOverview: String,
    @ColumnInfo("moviePosterPath") val moviePosterPath: String,
    @ColumnInfo("movieReleaseDate") val movieReleaseDate: String,
    @ColumnInfo("movieTitle") val movieTitle: String,
    @ColumnInfo("movieVoteAverage") val movieVoteAverage: String,
    @ColumnInfo("movieRuntime") val movieRuntime: Int,
    @ColumnInfo("saveTime") val saveTime: Long = System.currentTimeMillis()
) : Parcelable {

    @Parcelize
    @Entity(tableName = Constants.MOVIE_CAST_TABLE_NAME)
    data class MovieCastEntity(
        @ColumnInfo("castName") val castName: String,
        @ColumnInfo("castProfilePath") val castProfilePath: String,
        @ColumnInfo("castCharacter") val castCharacter: String,
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo("id") val id : Int
    ) : Parcelable

    @Parcelize
    data class MovieGenreEntity(
        @ColumnInfo("genreName") val genreName: String
    ) : Parcelable

    @Parcelize
    data class MovieSpokenLanguageEntity(
        @ColumnInfo("spokenLanguageName") val spokenLanguageName: String
    ) : Parcelable

}


