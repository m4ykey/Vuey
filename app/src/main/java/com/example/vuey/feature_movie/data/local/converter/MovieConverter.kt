package com.example.vuey.feature_movie.data.local.converter

import androidx.room.TypeConverter
import com.example.vuey.feature_movie.data.local.entity.MovieEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MovieConverter {

    @TypeConverter
    fun fromGenreJson(json : String) : List<MovieEntity.MovieGenreEntity> {
        val type = object : TypeToken<List<MovieEntity.MovieGenreEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toGenreJson(genres : List<MovieEntity.MovieGenreEntity>) : String {
        return Gson().toJson(genres)
    }

    @TypeConverter
    fun fromSpokenLanguagesJson(json : String) : List<MovieEntity.MovieSpokenLanguageEntity> {
        val type = object : TypeToken<List<MovieEntity.MovieSpokenLanguageEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toSpokenLanguagesJson(languages : List<MovieEntity.MovieSpokenLanguageEntity>) : String {
        return Gson().toJson(languages)
    }

    @TypeConverter
    fun fromCastJson(json : String) : List<MovieEntity.MovieCastEntity> {
        val type = object : TypeToken<List<MovieEntity.MovieCastEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toCastJson(cast : List<MovieEntity.MovieCastEntity>) : String {
        return Gson().toJson(cast)
    }

}