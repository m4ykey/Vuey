package com.example.vuey.feature_movie.use_cases

import com.example.vuey.feature_movie.data.remote.model.Cast
import com.example.vuey.feature_movie.data.repository.MovieRepository
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieCreditsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int) : Flow<Resource<List<Cast>>> {
        return repository.movieCredits(movieId)
    }
}