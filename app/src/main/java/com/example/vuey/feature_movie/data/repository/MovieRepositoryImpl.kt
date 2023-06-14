package com.example.vuey.feature_movie.data.repository

import com.example.vuey.feature_movie.data.local.dao.MovieDao
import com.example.vuey.feature_movie.data.remote.api.MovieApi
import com.example.vuey.feature_movie.data.remote.model.Cast
import com.example.vuey.feature_movie.data.remote.model.MovieDetail
import com.example.vuey.feature_movie.data.remote.model.SearchMovie
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao
) : MovieRepository {

    override fun searchMovie(query: String): Flow<Resource<List<SearchMovie>>> {
        return flow {
            emit(Resource.Loading())

            try {
                val movieResponse = movieApi.searchMovie(query).results
                emit(Resource.Success(movieResponse))

            } catch (e: HttpException) {
                emit(
                    Resource.Failure(
                        message = e.localizedMessage ?: "An unexpected error occurred",
                        data = null
                    )
                )
            } catch (e: IOException) {
                emit(
                    Resource.Failure(
                        message = e.localizedMessage ?: "No internet connection",
                        data = null
                    )
                )
            }
        }
    }

    override fun movieDetails(movie_id: Int): Flow<Resource<MovieDetail>> {
        return flow {
            emit(Resource.Loading())

            try {
                val movieResponse = movieApi.movieDetails(id = movie_id)
                emit(Resource.Success(movieResponse))

            } catch (e : HttpException) {
                emit(
                    Resource.Failure(
                        message = e.localizedMessage ?: "An unexpected error occurred",
                        data = null
                    )
                )
            } catch (e : IOException) {
                emit(
                    Resource.Failure(
                        message = e.localizedMessage ?: "No internet connection",
                        data = null
                    )
                )
            }
        }
    }

    override fun movieCredits(movie_id: Int): Flow<Resource<List<Cast>>> {
        return flow {
            emit(Resource.Loading())

            try {
                val movieResponse = movieApi.movieCredits(id = movie_id).cast
                emit(Resource.Success(movieResponse))
            } catch (e : HttpException) {
                emit(
                    Resource.Failure(
                        message = e.localizedMessage ?: "An unexpected error occurred",
                        data = null
                    )
                )
            } catch (e : IOException) {
                emit(
                    Resource.Failure(
                        message = e.localizedMessage ?: "No internet connection",
                        data = null
                    )
                )
            }
        }
    }
}