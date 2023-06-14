package com.example.vuey.feature_movie.use_cases

import com.example.vuey.feature_movie.data.remote.model.SearchMovie
import com.example.vuey.feature_movie.data.repository.MovieRepository
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieSearchUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(movieName : String) : Flow<Resource<List<SearchMovie>>> {
        return repository.searchMovie(movieName)
    }
}