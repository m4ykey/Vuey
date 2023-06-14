package com.example.vuey.feature_movie.use_cases

data class MovieUseCases(
    val getMovieSearchUseCase : MovieSearchUseCase,
    val getMovieDetailUseCase : MovieDetailUseCase,
    val getMovieCreditsUseCase : MovieCreditsUseCase
)
