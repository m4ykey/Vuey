package com.example.vuey.feature_tv_show.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.util.Constants.TV_SHOW_GENRE_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TV_SHOW_GENRE_TABLE)
data class TvShowGenreEntity(
    @ColumnInfo("tvShowGenreId") @PrimaryKey(autoGenerate = false) val tvShowGenreId : Int,
    @ColumnInfo("tvShowGenreName") val tvShowGenreName : String
) : Parcelable
