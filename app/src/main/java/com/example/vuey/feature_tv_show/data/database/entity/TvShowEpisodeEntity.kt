package com.example.vuey.feature_tv_show.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.util.Constants.TV_SHOW_EPISODE_TABLE
import kotlinx.parcelize.Parcelize

@Entity(tableName = TV_SHOW_EPISODE_TABLE)
@Parcelize
data class TvShowEpisodeEntity(
    @ColumnInfo("episodeNumber") val episodeNumber : Int,
    @ColumnInfo("episodeStillPath") val episodeStillPath : String,
    @ColumnInfo("episodeRuntime") val episodeRuntime : Int,
    @ColumnInfo("showId") val showId : Int,
    @ColumnInfo("seasonNumber") val seasonNumber : Int,
    @ColumnInfo("episodeName") val episodeName : String,
    @ColumnInfo("episodeAirDate") val episodeAirDate : String,
    @ColumnInfo("id") @PrimaryKey(autoGenerate = false) val id : Int
) : Parcelable
