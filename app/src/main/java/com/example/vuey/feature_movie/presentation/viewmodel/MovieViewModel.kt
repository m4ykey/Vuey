package com.example.vuey.feature_movie.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.feature_movie.presentation.viewmodel.ui_state.SearchMovieUiState
import com.example.vuey.feature_movie.presentation.viewmodel.use_case.MovieUseCases
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
    private val useCase : MovieUseCases
) : ViewModel() {

    private val _movieSearchUiState = MutableStateFlow(SearchMovieUiState())
    val movieSearchUiState : StateFlow<SearchMovieUiState> get() = _movieSearchUiState

    fun searchMovie(movieName : String) {
        useCase.getMovieSearchUseCase(movieName).onEach { result ->
            when (result) {
                is Resource.Failure -> {
                    _movieSearchUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = result.message ?: "An unexpected error occurred.",
                            searchMovieData = result.data ?: emptyList()
                        )
                    }
                }
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
            }
        }.launchIn(viewModelScope)
    }
}