package com.example.vuey.feature_movie.presentation.viewmodel.use_case

import com.example.vuey.feature_movie.data.remote.model.MovieList
import com.example.vuey.feature_movie.data.repository.MovieRepository
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieSearchUseCase @Inject constructor(
    private val repository: MovieRepository
){
    operator fun invoke(query : String) : Flow<Resource<List<MovieList>>> {
        return repository.searchMovie(query)
    }
}