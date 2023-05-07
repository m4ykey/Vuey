package com.example.vuey.feature_tv_show.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.util.Constants.TV_SHOW_CAST_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TV_SHOW_CAST_TABLE)
data class TvShowCastEntity(
    @ColumnInfo("castName") val castName : String,
    @ColumnInfo("castProfilePath") val castProfilePath : String,
    @ColumnInfo("castCharacter") val castCharacter : String,
    @ColumnInfo("tvShowId") val tvShowId : Int,
    @PrimaryKey(autoGenerate = false) @ColumnInfo("id") val id : Int
) : Parcelable
