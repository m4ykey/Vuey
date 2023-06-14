package com.example.vuey.util.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vuey.feature_album.data.local.converter.AlbumConverter
import com.example.vuey.feature_album.data.local.dao.AlbumDao
import com.example.vuey.feature_album.data.local.entity.AlbumEntity
import com.example.vuey.util.Constants.DATABASE_VERSION

@Database(
    entities = [
        AlbumEntity::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(AlbumConverter::class)
abstract class VueyDatabase : RoomDatabase() {

    abstract fun albumDao(): AlbumDao

}