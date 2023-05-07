package com.example.vuey.feature_tv_show.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.util.Constants.TV_SHOW_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TV_SHOW_TABLE)
data class TvShowEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo("id") val id: Int,
    @ColumnInfo("tvShowName") val tvShowName: String,
    @ColumnInfo("tvShowOverview") val tvShowOverview : String,
    @ColumnInfo("tvShowStatus") val tvShowStatus : String,
    @ColumnInfo("tvShowBackdropPath") val tvShowBackdropPath : String,
    @ColumnInfo("tvShowPosterPath") val tvShowPosterPath : String,
    @ColumnInfo("tvShowVoteAverage") val tvShowVoteAverage : String,
    @ColumnInfo("tvShowFirstAirDate") val tvShowFirstAirDate : String,
    @ColumnInfo("tvShowGenreList") val tvShowGenreList : List<TvShowGenreEntity>,
    @ColumnInfo("tvShowEpisodeRun") val tvShowEpisodeRun : Int,
    @ColumnInfo("tvShowSpokenLanguageList") val tvShowSpokenLanguage: List<TvShowSpokenLanguageEntity>,
    @ColumnInfo("tvShowCast") val tvShowCast: List<TvShowCastEntity>,
    @ColumnInfo("tvShowSeasons") val tvShowSeason : List<TvShowSeasonEntity>,
    @ColumnInfo("tvShowEpisodes") val tvShowEpisode : List<TvShowEpisodeEntity> = emptyList()
) : Parcelable
