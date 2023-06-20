package com.example.vuey.feature_movie.data.repository

import com.example.vuey.feature_movie.data.local.entity.CastEntity
import com.example.vuey.feature_movie.data.local.entity.MovieEntity
import com.example.vuey.feature_movie.data.remote.model.CastDetail
import com.example.vuey.feature_movie.data.remote.model.MovieDetail
import com.example.vuey.feature_movie.data.remote.model.MovieList
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun searchMovie(query : String) : Flow<Resource<List<MovieList>>>
    fun getMovieDetail(movieId : Int) : Flow<Resource<MovieDetail>>
    fun getMovieCast(movieId: Int) : Flow<Resource<List<CastDetail>>>

    suspend fun insertMovie(movieEntity: MovieEntity)
    suspend fun deleteMovie(movieEntity: MovieEntity)
    fun getAllMovies() : Flow<List<MovieEntity>>
    fun getMovieById(movieId : Int) : Flow<MovieEntity>

    fun getCastById(movieId: Int) : Flow<List<CastEntity>>
    suspend fun insertCast(castEntity: CastEntity)
    suspend fun deleteCast(castEntity: CastEntity)

}