package com.example.vuey.data.repository

import com.example.vuey.data.local.movie.search.SearchMovie
import com.example.vuey.data.remote.api.movie.MovieApi
import com.example.vuey.util.network.Resource
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieApi: MovieApi
) {

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

}