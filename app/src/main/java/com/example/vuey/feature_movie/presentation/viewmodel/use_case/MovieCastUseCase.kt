package com.example.vuey.feature_movie.presentation.viewmodel.use_case

import com.example.vuey.feature_movie.data.remote.model.CastDetail
import com.example.vuey.feature_movie.data.repository.MovieRepository
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieCastUseCase @Inject constructor(
    private val repository: MovieRepository
){
    operator fun invoke(movieId : Int) : Flow<Resource<List<CastDetail>>> {
        return repository.getMovieCast(movieId)
    }
}