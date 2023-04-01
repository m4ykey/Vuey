package com.example.vuey.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vuey.data.database.converter.AlbumConverter
import com.example.vuey.data.database.dao.AlbumDao
import com.example.vuey.data.database.model.AlbumEntity
import com.example.vuey.util.Constants.DATABASE_VERSION

@Database(
    entities = [AlbumEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(AlbumConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun albumDao() : AlbumDao

}