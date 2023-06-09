package com.example.vuey.feature_movie.data.repository

import com.example.vuey.feature_movie.data.remote.model.Cast
import com.example.vuey.feature_movie.data.remote.model.MovieDetail
import com.example.vuey.feature_movie.data.remote.model.SearchMovie
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun searchMovie(query : String) : Flow<Resource<List<SearchMovie>>>
    fun movieDetails(movie_id : Int) : Flow<Resource<MovieDetail>>
    fun movieCredits(movie_id: Int) : Flow<Resource<List<Cast>>>

}