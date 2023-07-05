package com.example.vuey.feature_movie.data.repository

import com.example.vuey.feature_movie.data.local.entity.MovieEntity
import com.example.vuey.feature_movie.data.remote.model.MovieCast
import com.example.vuey.feature_movie.data.remote.model.MovieDetail
import com.example.vuey.feature_movie.data.remote.model.MovieList
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun searchMovie(query : String) : Flow<Resource<List<MovieList>>>
    fun getMovieDetail(movieId : Int) : Flow<Resource<MovieDetail>>
    fun getMovieCast(movieId: Int) : Flow<Resource<List<MovieCast.CastDetail>>>

    suspend fun insertMovie(movieEntity: MovieEntity)
    suspend fun deleteMovie(movieEntity: MovieEntity)
    fun getAllMovies() : Flow<List<MovieEntity>>
    fun getMovieById(movieId : Int) : Flow<MovieEntity>
    fun searchMovieInDatabase(movieTitle : String) : Flow<List<MovieEntity>>
}