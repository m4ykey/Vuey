package com.example.vuey.feature_movie.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vuey.feature_movie.data.local.entity.CastEntity
import com.example.vuey.feature_movie.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie_table ORDER BY movieSaveTime ASC")
    fun getAllMovies() : Flow<List<MovieEntity>>

    @Query("SELECT * FROM movie_table WHERE movieId = :movieId")
    fun getMovieById(movieId : Int) : Flow<MovieEntity>

    @Query("SELECT * FROM cast_table WHERE movieId = :movieId")
    fun getCastById(movieId: Int) : Flow<List<CastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCast(castEntity: CastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieEntity: MovieEntity)

    @Delete
    suspend fun deleteCast(castEntity: CastEntity)

    @Delete
    suspend fun deleteMovie(movieEntity: MovieEntity)

}