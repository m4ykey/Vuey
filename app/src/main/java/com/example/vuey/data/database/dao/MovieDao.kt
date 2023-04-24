package com.example.vuey.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.vuey.data.database.model.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movieEntity: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCast(castEntity: List<MovieEntity.CastEntity>)

    @Query("SELECT * FROM cast_table WHERE movieId = :movieId")
    fun getCast(movieId : Int) : LiveData<List<MovieEntity.CastEntity>>

    @Query("SELECT * FROM movie_table ORDER BY saveTime ASC")
    fun getAllMovies() : LiveData<List<MovieEntity>>

    @Delete
    suspend fun deleteMovie(movieEntity: MovieEntity)

    @Delete
    suspend fun deleteCast(castEntity: List<MovieEntity.CastEntity>)

}