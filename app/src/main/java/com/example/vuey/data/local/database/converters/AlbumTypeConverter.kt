package com.example.vuey.data.local.database.converters

import androidx.room.TypeConverter
import com.example.vuey.data.local.database.model.AlbumEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlbumTypeConverter {

    private val gson = Gson()

    // tracks
    @TypeConverter
    fun fromTrack(tracks : List<AlbumEntity.Track>?) : String? {
        if (tracks == null) {
            return null
        }
        val type = object : TypeToken<List<AlbumEntity.Track>>() {}.type
        return gson.toJson(tracks, type)
    }

    @TypeConverter
    fun toTrack(track : String?) : List<AlbumEntity.Track>? {
        if (track == null) {
            return null
        }
        val type = object : TypeToken<List<AlbumEntity.Track>>() {}.type
        return gson.fromJson(track, type)
    }

    // tag
    @TypeConverter
    fun fromTag(tags : List<AlbumEntity.Tag>?) : String? {
        if (tags == null) {
            return null
        }
        val type = object : TypeToken<List<AlbumEntity.Tag>>() {}.type
        return gson.toJson(tags, type)
    }

    @TypeConverter
    fun toTag(tag : String?) : List<AlbumEntity.Tag>? {
        if (tag == null) {
            return null
        }
        val type = object : TypeToken<List<AlbumEntity.Tag>>() {}.type
        return gson.fromJson(tag, type)
    }

    // image
    @TypeConverter
    fun fromImage(images : List<AlbumEntity.Image>?) : String? {
        if (images == null) {
            return null
        }
        val type = object : TypeToken<List<AlbumEntity.Image>>() {}.type
        return gson.toJson(images, type)
    }

    @TypeConverter
    fun toImage(image : String?) : List<AlbumEntity.Image>? {
        if (image == null) {
            return null
        }
        val type = object : TypeToken<List<AlbumEntity.Image>>() {}.type
        return gson.fromJson(image, type)
    }

}