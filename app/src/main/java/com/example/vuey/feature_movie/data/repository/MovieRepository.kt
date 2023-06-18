package com.example.vuey.feature_movie.data.repository

import com.example.vuey.feature_movie.data.remote.model.MovieDetail
import com.example.vuey.feature_movie.data.remote.model.MovieList
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun searchMovie(query : String) : Flow<Resource<List<MovieList>>>
    fun getMovieDetail(movieId : Int) : Flow<Resource<MovieDetail>>

}