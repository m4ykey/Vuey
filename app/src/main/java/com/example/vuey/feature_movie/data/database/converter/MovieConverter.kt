package com.example.vuey.feature_movie.data.database.converter

import androidx.room.TypeConverter
import com.example.vuey.feature_movie.data.database.entity.MovieCastEntity
import com.example.vuey.feature_movie.data.database.entity.MovieGenreEntity
import com.example.vuey.feature_movie.data.database.entity.MovieSpokenLanguageEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MovieConverter {

    @TypeConverter
    fun fromGenreJson(json : String) : List<MovieGenreEntity> {
        val type = object : TypeToken<List<MovieGenreEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toGenreJson(genres : List<MovieGenreEntity>) : String {
        return Gson().toJson(genres)
    }

    @TypeConverter
    fun fromSpokenLanguagesJson(json : String) : List<MovieSpokenLanguageEntity> {
        val type = object : TypeToken<List<MovieSpokenLanguageEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toSpokenLanguagesJson(languages : List<MovieSpokenLanguageEntity>) : String {
        return Gson().toJson(languages)
    }

    @TypeConverter
    fun fromCastJson(json : String) : List<MovieCastEntity> {
        val type = object : TypeToken<List<MovieCastEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toCastJson(cast : List<MovieCastEntity>) : String {
        return Gson().toJson(cast)
    }

}