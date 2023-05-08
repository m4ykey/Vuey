package com.example.vuey.feature_tv_show.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.util.Constants
import com.example.vuey.util.Constants.TV_SHOW_CAST_TABLE
import com.example.vuey.util.Constants.TV_SHOW_EPISODE_TABLE
import com.example.vuey.util.Constants.TV_SHOW_GENRE_TABLE
import com.example.vuey.util.Constants.TV_SHOW_SEASON_TABLE
import com.example.vuey.util.Constants.TV_SHOW_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TV_SHOW_TABLE)
data class TvShowEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo("id") val id: Int,
    @ColumnInfo("saveTime") val saveTime : Long = System.currentTimeMillis(),
    @ColumnInfo("tvShowName") val tvShowName: String,
    @ColumnInfo("tvShowOverview") val tvShowOverview : String,
    @ColumnInfo("tvShowStatus") val tvShowStatus : String,
    @ColumnInfo("tvShowBackdropPath") val tvShowBackdropPath : String,
    @ColumnInfo("tvShowPosterPath") val tvShowPosterPath : String,
    @ColumnInfo("tvShowVoteAverage") val tvShowVoteAverage : String,
    @ColumnInfo("tvShowFirstAirDate") val tvShowFirstAirDate : String,
    @ColumnInfo("tvShowGenreList") val tvShowGenreList : List<TvShowGenreEntity> = emptyList(),
    @ColumnInfo("tvShowEpisodeRun") val tvShowEpisodeRun : Int,
    @ColumnInfo("tvShowSpokenLanguageList") val tvShowSpokenLanguage: List<TvShowSpokenLanguageEntity> = emptyList(),
    @ColumnInfo("tvShowCast") val tvShowCast: List<TvShowCastEntity> = emptyList(),
    @ColumnInfo("tvShowSeasons") val tvShowSeason : List<TvShowSeasonEntity> = emptyList(),
    @ColumnInfo("tvShowEpisodes") val tvShowEpisode : List<TvShowEpisodeEntity> = emptyList()
) : Parcelable {

    @Parcelize
    @Entity(tableName = TV_SHOW_CAST_TABLE)
    data class TvShowCastEntity(
        @ColumnInfo("castName") val castName : String,
        @ColumnInfo("castProfilePath") val castProfilePath : String,
        @ColumnInfo("castCharacter") val castCharacter : String,
        @ColumnInfo("tvShowId") val tvShowId : Int,
        @PrimaryKey(autoGenerate = false) @ColumnInfo("id") val id : Int
    ) : Parcelable

    @Parcelize
    @Entity(tableName = TV_SHOW_EPISODE_TABLE)
    data class TvShowEpisodeEntity(
        @ColumnInfo("episodeNumber") val episodeNumber : Int,
        @ColumnInfo("episodeStillPath") val episodeStillPath : String,
        @ColumnInfo("episodeRuntime") val episodeRuntime : Int,
        @ColumnInfo("showId") val showId : Int,
        @ColumnInfo("seasonNumber") val seasonNumber : Int,
        @ColumnInfo("episodeName") val episodeName : String,
        @ColumnInfo("episodeAirDate") val episodeAirDate : String,
        @PrimaryKey(autoGenerate = false) @ColumnInfo("id") val id : Int
    ) : Parcelable

    @Parcelize
    @Entity(tableName = TV_SHOW_GENRE_TABLE)
    data class TvShowGenreEntity(
        @PrimaryKey(autoGenerate = false) @ColumnInfo("id") val id : Int,
        @ColumnInfo("tvShowGenreName") val tvShowGenreName : String
    ) : Parcelable


    @Parcelize
    @Entity(tableName = TV_SHOW_SEASON_TABLE)
    data class TvShowSeasonEntity(
        @ColumnInfo("seasonId") val id : Int,
        @ColumnInfo("seasonNumber") val seasonNumber : Int,
        @ColumnInfo("seasonName") val seasonName : String,
        @ColumnInfo("seasonEpisodeCount") val seasonEpisodeCount : Int
    ) : Parcelable

    @Parcelize
    data class TvShowSpokenLanguageEntity(
        @ColumnInfo("tvShowSpokenLanguageName") val tvShowSpokenLanguageName : String
    ) : Parcelable

}
