package com.example.vuey.feature_album.data.local.converter

import androidx.room.TypeConverter
import com.example.vuey.feature_album.data.local.entity.AlbumEntity
import com.example.vuey.feature_album.data.local.entity.AlbumStatisticsEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlbumConverter {

    @TypeConverter
    fun fromArtistStatisticsJson(json : String) : List<AlbumStatisticsEntity.ArtistStatisticsEntity> {
        val type = object : TypeToken<List<AlbumStatisticsEntity.ArtistStatisticsEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toArtistStatisticsJson(artists : List<AlbumStatisticsEntity.ArtistStatisticsEntity>) : String {
        return Gson().toJson(artists)
    }

    @TypeConverter
    fun fromArtistJson(json : String) : List<AlbumEntity.ArtistEntity> {
        val type = object : TypeToken<List<AlbumEntity.ArtistEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toArtistJson(artists : List<AlbumEntity.ArtistEntity>) : String {
        return Gson().toJson(artists)
    }

    @TypeConverter
    fun fromExternalUrlsJson(json : String) : AlbumEntity.ExternalUrlsEntity {
        val type = object : TypeToken<AlbumEntity.ExternalUrlsEntity>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toExternalUrlsJson(externalUrlsEntity: AlbumEntity.ExternalUrlsEntity) : String {
        return Gson().toJson(externalUrlsEntity)
    }

    @TypeConverter
    fun fromImageJson(json : String) : AlbumEntity.ImageEntity {
        val type = object : TypeToken<AlbumEntity.ImageEntity>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toImageJson(images : AlbumEntity.ImageEntity) : String {
        return Gson().toJson(images)
    }

    @TypeConverter
    fun fromTrackJson(json : String) : List<AlbumEntity.TrackListEntity> {
        val type = object : TypeToken<List<AlbumEntity.TrackListEntity>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toTrackJson(tracks : List<AlbumEntity.TrackListEntity>) : String {
        return Gson().toJson(tracks)
    }

}