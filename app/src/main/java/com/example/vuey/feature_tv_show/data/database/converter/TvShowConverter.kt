package com.example.vuey.feature_tv_show.data.database.converter

import androidx.room.TypeConverter
import com.example.vuey.feature_tv_show.data.database.entity.TvShowCastEntity
import com.example.vuey.feature_tv_show.data.database.entity.TvShowEpisodeEntity
import com.example.vuey.feature_tv_show.data.database.entity.TvShowGenreEntity
import com.example.vuey.feature_tv_show.data.database.entity.TvShowSeasonEntity
import com.example.vuey.feature_tv_show.data.database.entity.TvShowSpokenLanguageEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TvShowConverter {

    @TypeConverter
    fun fromGenreListJson(json : String) : List<TvShowGenreEntity> {
        val type = object : TypeToken<List<TvShowGenreEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toGenreListJson(genreList : List<TvShowGenreEntity>) : String {
        return Gson().toJson(genreList)
    }

    @TypeConverter
    fun fromSpokenLanguageListJson(json : String) : List<TvShowSpokenLanguageEntity> {
        val type = object : TypeToken<List<TvShowSpokenLanguageEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toSpokenLanguageListJson(spokenLanguageList : List<TvShowSpokenLanguageEntity>) : String {
        return Gson().toJson(spokenLanguageList)
    }

    @TypeConverter
    fun fromCastListJson(json : String) : List<TvShowCastEntity> {
        val type = object : TypeToken<List<TvShowCastEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toCastListJson(castList : List<TvShowCastEntity>) : String {
        return Gson().toJson(castList)
    }

    @TypeConverter
    fun fromSeasonListJson(json : String) : List<TvShowSeasonEntity> {
        val type = object : TypeToken<List<TvShowSeasonEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toSeasonListJson(seasonList : List<TvShowSeasonEntity>) : String {
        return Gson().toJson(seasonList)
    }

    @TypeConverter
    fun fromEpisodeListJson(json : String) : List<TvShowEpisodeEntity> {
        val type = object : TypeToken<List<TvShowEpisodeEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toEpisodeListJson(episodeList : List<TvShowEpisodeEntity>) : String {
        return Gson().toJson(episodeList)
    }

}