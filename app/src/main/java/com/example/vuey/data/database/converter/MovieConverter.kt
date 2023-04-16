package com.example.vuey.data.database.converter

import androidx.room.TypeConverter
import com.example.vuey.data.database.model.MovieEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MovieConverter {

    @TypeConverter
    fun fromGenreJson(json : String) : List<MovieEntity.GenreEntity> {
        val type = object : TypeToken<List<MovieEntity.GenreEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toGenreJson(genres : List<MovieEntity.GenreEntity>) : String {
        return Gson().toJson(genres)
    }

    @TypeConverter
    fun fromSpokenLanguagesJson(json : String) : List<MovieEntity.SpokenLanguageEntity> {
        val type = object : TypeToken<List<MovieEntity.SpokenLanguageEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toSpokenLanguagesJson(languages : List<MovieEntity.SpokenLanguageEntity>) : String {
        return Gson().toJson(languages)
    }

    @TypeConverter
    fun fromCastJson(json : String) : List<MovieEntity.CastEntity> {
        val type = object : TypeToken<List<MovieEntity.CastEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toCastJson(cast : List<MovieEntity.CastEntity>) : String {
        return Gson().toJson(cast)
    }

}