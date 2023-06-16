package com.example.vuey.feature_movie.data.repository

import com.example.vuey.feature_movie.data.remote.api.MovieApi
import com.example.vuey.feature_movie.data.remote.model.MovieList
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi
) : MovieRepository {
    override fun searchMovie(query: String): Flow<Resource<List<MovieList>>> {
        return flow {

            emit(Resource.Loading())

            try {
                val movieResponse = movieApi.searchMovie(query).results
                emit(Resource.Success(movieResponse))
            } catch (e : IOException) {
                emit(
                    Resource.Failure(
                        message = e.localizedMessage ?: "No internet connection",
                        data = null
                    )
                )
            } catch (e : HttpException) {
                emit(Resource.Failure(
                    message = e.localizedMessage ?: "An unexpected error occurred",
                    data = null
                ))
            }
        }
    }
}