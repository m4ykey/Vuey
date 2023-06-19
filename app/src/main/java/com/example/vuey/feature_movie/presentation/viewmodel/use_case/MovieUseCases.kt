package com.example.vuey.feature_movie.presentation.viewmodel.use_case

data class MovieUseCases(
    val getMovieSearchUseCase : MovieSearchUseCase,
    val getMovieDetailUseCase : MovieDetailUseCase,
    val getMovieCastUseCase : MovieCastUseCase
)