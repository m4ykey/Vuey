package com.example.vuey.data.repository

import androidx.lifecycle.LiveData
import com.example.vuey.data.database.dao.MovieDao
import com.example.vuey.data.database.model.MovieEntity
import com.example.vuey.data.models.movie.search.SearchMovie
import com.example.vuey.data.remote.api.MovieApi
import com.example.vuey.data.remote.response.movie.MovieCreditsResponse
import com.example.vuey.data.remote.response.movie.MovieDetailResponse
import com.example.vuey.util.network.Resource
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao
) {

    suspend fun insertMovie(movie : MovieEntity) {
        return movieDao.insertMovie(movie)
    }

    suspend fun deleteMovie(movie: MovieEntity) {
        return movieDao.deleteMovie(movie)
    }

    fun getAllMovies() : LiveData<List<MovieEntity>> {
        return movieDao.getAllMovies()
    }

    suspend fun insertCast(cast : List<MovieEntity.CastEntity>) {
        return movieDao.insertCast(cast)
    }

    fun getCast(movieId: Int) : LiveData<List<MovieEntity.CastEntity>> {
        return movieDao.getCast(movieId)
    }

    suspend fun searchMovie(movieName : String) : Resource<List<SearchMovie>> {
        return try {
            val response = movieApi.searchMovie(query = movieName)
            if (response.isSuccessful) {
                val movieSearchResponse = response.body()
                val movies = movieSearchResponse!!.results
                Resource.Success(movies)
            } else {
                Resource.Failure("Failed to fetch movie list")
            }
        } catch (e : Exception) {
            Resource.Failure("Failed to fetch movie list ${e.localizedMessage}")
        }
    }

    suspend fun movieDetails(movieId : Int) : Resource<MovieDetailResponse> {
        return try {
            val response = movieApi.movieDetails(movieId)
            if (response.isSuccessful) {
                val movieDetailResponse = response.body()
                val movieDetail = movieDetailResponse!!
                Resource.Success(movieDetail)
            } else {
                Resource.Failure("Failed to fetch movie details")
            }
        } catch (e : Exception) {
            Resource.Failure("Failed to fetch movie details ${e.localizedMessage}")
        }
    }

    suspend fun movieCredits(movieId : Int) : Resource<MovieCreditsResponse> {
        return try {
            val response = movieApi.movieCredits(movieId)
            if (response.isSuccessful) {
                val movieCreditsResponse = response.body()
                val movieCredit = movieCreditsResponse!!
                Resource.Success(movieCredit)
            } else {
                Resource.Failure("Failed to fetch movie credits")
            }
        } catch (e : Exception) {
            Resource.Failure("Failed to fetch movie credits ${e.localizedMessage}")
        }
    }
}