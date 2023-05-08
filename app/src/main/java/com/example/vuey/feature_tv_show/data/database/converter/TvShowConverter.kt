package com.example.vuey.feature_tv_show.data.database.converter

import androidx.room.TypeConverter
import com.example.vuey.feature_tv_show.data.database.entity.TvShowEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TvShowConverter {

    @TypeConverter
    fun fromGenreListJson(json : String) : List<TvShowEntity.TvShowGenreEntity> {
        val type = object : TypeToken<List<TvShowEntity.TvShowGenreEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toGenreListJson(genreList : List<TvShowEntity.TvShowGenreEntity>) : String {
        return Gson().toJson(genreList)
    }

    @TypeConverter
    fun fromSpokenLanguageListJson(json : String) : List<TvShowEntity.TvShowSpokenLanguageEntity> {
        val type = object : TypeToken<List<TvShowEntity.TvShowSpokenLanguageEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toSpokenLanguageListJson(spokenLanguageList : List<TvShowEntity.TvShowSpokenLanguageEntity>) : String {
        return Gson().toJson(spokenLanguageList)
    }

    @TypeConverter
    fun fromCastListJson(json : String) : List<TvShowEntity.TvShowCastEntity> {
        val type = object : TypeToken<List<TvShowEntity.TvShowCastEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toCastListJson(castList : List<TvShowEntity.TvShowCastEntity>) : String {
        return Gson().toJson(castList)
    }

    @TypeConverter
    fun fromSeasonListJson(json : String) : List<TvShowEntity.TvShowSeasonEntity> {
        val type = object : TypeToken<List<TvShowEntity.TvShowSeasonEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toSeasonListJson(seasonList : List<TvShowEntity.TvShowSeasonEntity>) : String {
        return Gson().toJson(seasonList)
    }

    @TypeConverter
    fun fromEpisodeListJson(json : String) : List<TvShowEntity.TvShowEpisodeEntity> {
        val type = object : TypeToken<List<TvShowEntity.TvShowEpisodeEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toEpisodeListJson(episodeList : List<TvShowEntity.TvShowEpisodeEntity>) : String {
        return Gson().toJson(episodeList)
    }

}