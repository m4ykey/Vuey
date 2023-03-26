package com.example.vuey.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vuey.data.local.database.converters.AlbumTypeConverter
import com.example.vuey.data.local.database.dao.AlbumDao
import com.example.vuey.data.local.database.model.AlbumEntity
import com.example.vuey.util.Constants.DATABASE_VERSION

@Database(
    entities = [AlbumEntity::class],
    version = DATABASE_VERSION
)
@TypeConverters(AlbumTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun albumDao() : AlbumDao

}