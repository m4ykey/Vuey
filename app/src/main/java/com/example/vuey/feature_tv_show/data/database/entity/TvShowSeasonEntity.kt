package com.example.vuey.feature_tv_show.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.util.Constants.TV_SHOW_SEASON_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TV_SHOW_SEASON_TABLE)
data class TvShowSeasonEntity(
    @ColumnInfo("seasonId") @PrimaryKey(autoGenerate = false) val id : Int,
    @ColumnInfo("seasonNumber") val seasonNumber : Int,
    @ColumnInfo("seasonName") val seasonName : String,
    @ColumnInfo("seasonEpisodeCount") val seasonEpisodeCount : Int
) : Parcelable
