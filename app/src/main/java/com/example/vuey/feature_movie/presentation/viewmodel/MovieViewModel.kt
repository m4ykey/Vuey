package com.example.vuey.feature_movie.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.feature_movie.data.repository.MovieRepository
import com.example.vuey.feature_movie.ui_state.CreditsMovieUiState
import com.example.vuey.feature_movie.ui_state.DetailMovieUiState
import com.example.vuey.feature_movie.ui_state.SearchMovieUiState
import com.example.vuey.feature_movie.use_cases.MovieUseCases
import com.example.vuey.util.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val useCase : MovieUseCases
) : ViewModel() {

    private val _movieSearchUiState = MutableStateFlow(SearchMovieUiState())
    val movieSearchUiState : StateFlow<SearchMovieUiState> get() = _movieSearchUiState

    private val _movieDetailUiState = MutableStateFlow(DetailMovieUiState())
    val movieDetailUiState : StateFlow<DetailMovieUiState> get() = _movieDetailUiState

    private val _movieCreditsUiState = MutableStateFlow(CreditsMovieUiState())
    val movieCreditsUiState : StateFlow<CreditsMovieUiState> get() = _movieCreditsUiState

    fun getMovieCredits(movieId: Int) {
        useCase.getMovieCreditsUseCase(movieId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _movieCreditsUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = null,
                            movieCreditsData = result.data ?: emptyList()
                        )
                    }
                }
                is Resource.Failure -> {
                    _movieCreditsUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = result.message ?: "An unexpected error occurred.",
                            movieCreditsData = result.data ?: emptyList()
                        )
                    }
                }
                is Resource.Loading -> {
                    _movieCreditsUiState.update { prevState ->
                        prevState.copy(
                            isLoading = true,
                            isError = null,
                            movieCreditsData = result.data ?: emptyList()
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getMovieDetail(movieId : Int) {
        useCase.getMovieDetailUseCase(movieId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _movieDetailUiState.update { prevState ->
                        prevState.copy(
                            isLoading = true,
                            isError = null,
                            detailMovieData = result.data
                        )
                    }
                }
                is Resource.Failure -> {
                    _movieDetailUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = result.message ?: "An unexpected error occurred.",
                            detailMovieData = result.data
                        )
                    }
                }
                is Resource.Success -> {
                    _movieDetailUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = null,
                            detailMovieData = result.data
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun searchMovie(movieName : String) {
        useCase.getMovieSearchUseCase(movieName).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _movieSearchUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = null,
                            searchMovieData = result.data ?: emptyList()
                        )
                    }
                }
                is Resource.Loading -> {
                    _movieSearchUiState.update { prevState ->
                        prevState.copy(
                            isLoading = true,
                            isError = null,
                            searchMovieData = result.data ?: emptyList()
                        )
                    }
                }
                is Resource.Failure -> {
                    _movieSearchUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = result.message ?: "An unexpected error occurred."
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}