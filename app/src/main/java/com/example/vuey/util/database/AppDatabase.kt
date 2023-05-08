package com.example.vuey.util.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vuey.feature_album.data.database.converter.AlbumConverter
import com.example.vuey.feature_album.data.database.dao.AlbumDao
import com.example.vuey.feature_album.data.database.entity.AlbumEntity
import com.example.vuey.feature_movie.data.database.converter.MovieConverter
import com.example.vuey.feature_movie.data.database.dao.MovieDao
import com.example.vuey.feature_movie.data.database.entity.MovieCastEntity
import com.example.vuey.feature_movie.data.database.entity.MovieEntity
import com.example.vuey.feature_tv_show.data.database.converter.TvShowConverter
import com.example.vuey.feature_tv_show.data.database.dao.TvShowDao
import com.example.vuey.feature_tv_show.data.database.entity.TvShowEntity
import com.example.vuey.util.Constants.DATABASE_VERSION

@Database(
    entities = [
        AlbumEntity::class,
        MovieEntity::class,
        MovieCastEntity::class,
        TvShowEntity::class,
        TvShowEntity.TvShowGenreEntity::class,
        TvShowEntity.TvShowCastEntity::class,
        TvShowEntity.TvShowEpisodeEntity::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(AlbumConverter::class, MovieConverter::class, TvShowConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun albumDao(): AlbumDao
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao

}