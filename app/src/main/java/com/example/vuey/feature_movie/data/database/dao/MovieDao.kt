package com.example.vuey.feature_movie.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.vuey.feature_movie.data.database.entity.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieEntity: MovieEntity)

    @Query("SELECT * FROM movie_table ORDER BY saveTime ASC")
    fun getAllMovies() : LiveData<List<MovieEntity>>

    @Query("SELECT * FROM movie_table WHERE movieId = :movieId")
    fun getMovieById(movieId: Int) : LiveData<MovieEntity>

    @Delete
    suspend fun deleteMovie(movieEntity: MovieEntity)

}