package com.example.vuey.data.database.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.util.Constants.CAST_TABLE_NAME
import com.example.vuey.util.Constants.MOVIE_TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = MOVIE_TABLE_NAME)
data class MovieEntity(
    val cast : List<CastEntity>,
    val spokenLanguage: List<SpokenLanguageEntity>,
    val genre: List<GenreEntity>,
    val backdrop_path: String,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: String,
    val vote_count: Int,
    val runtime: String,
    val saveTime: Long = System.currentTimeMillis()
) : Parcelable {

    @Parcelize
    data class SpokenLanguageEntity(
        val name: String
    ) : Parcelable

    @Parcelize
    data class GenreEntity(
        val name: String
    ) : Parcelable

    @Parcelize
    @Entity(tableName = CAST_TABLE_NAME)
    data class CastEntity(
        val name: String,
        val profile_path: String?,
        val character: String,
        val movieId : Int,
        @PrimaryKey(autoGenerate = false)
        val id : Int
    ) : Parcelable
}


