package com.example.vuey.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vuey.data.database.converter.AlbumConverter
import com.example.vuey.data.database.converter.MovieConverter
import com.example.vuey.data.database.dao.AlbumDao
import com.example.vuey.data.database.dao.MovieDao
import com.example.vuey.data.database.model.AlbumEntity
import com.example.vuey.data.database.model.MovieEntity
import com.example.vuey.util.Constants.DATABASE_VERSION

@Database(
    entities = [
        AlbumEntity::class,
        MovieEntity::class,
        MovieEntity.CastEntity::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(AlbumConverter::class, MovieConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun albumDao(): AlbumDao
    abstract fun movieDao(): MovieDao
}