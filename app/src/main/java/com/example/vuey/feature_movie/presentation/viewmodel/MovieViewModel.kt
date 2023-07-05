package com.example.vuey.feature_movie.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.feature_movie.data.local.entity.MovieEntity
import com.example.vuey.feature_movie.data.repository.MovieRepository
import com.example.vuey.feature_movie.presentation.viewmodel.ui_state.CastMovieUiState
import com.example.vuey.feature_movie.presentation.viewmodel.ui_state.DetailMovieUiState
import com.example.vuey.feature_movie.presentation.viewmodel.ui_state.SearchMovieUiState
import com.example.vuey.feature_movie.presentation.viewmodel.use_case.MovieUseCases
import com.example.vuey.util.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val useCase : MovieUseCases,
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieSearchUiState = MutableStateFlow(SearchMovieUiState())
    val movieSearchUiState : StateFlow<SearchMovieUiState> get() = _movieSearchUiState

    private val _movieDetailUiState = MutableStateFlow(DetailMovieUiState())
    val movieDetailUiState : StateFlow<DetailMovieUiState> get() = _movieDetailUiState

    private val _movieCastUiState = MutableStateFlow(CastMovieUiState())
    val movieCastUiState : StateFlow<CastMovieUiState> get() = _movieCastUiState

    private val _searchMovieInDatabase = MutableStateFlow<List<MovieEntity>>(emptyList())
    val searchMovieInDatabase : StateFlow<List<MovieEntity>> get() = _searchMovieInDatabase

    val allMovies = repository.getAllMovies()

    fun searchMovieDatabase(movieTitle : String) {
        viewModelScope.launch {
            repository.searchMovieInDatabase(movieTitle).collect { movieList ->
                _searchMovieInDatabase.emit(movieList)
            }
        }
    }

    fun getMovieById(movieId: Int) : Flow<MovieEntity> {
        return repository.getMovieById(movieId)
    }

    fun refreshDetail(movieId : Int) {
        getMovieDetail(movieId)
    }

    fun insertMovie(movieEntity: MovieEntity) {
        viewModelScope.launch {
            repository.insertMovie(movieEntity)
        }
    }

    fun deleteMovie(movieEntity: MovieEntity) {
        viewModelScope.launch {
            repository.deleteMovie(movieEntity)
        }
    }

    fun getMovieCast(movieId: Int) {
        useCase.getMovieCastUseCase(movieId).onEach { result ->
            when (result) {
                is Resource.Failure -> {
                    _movieCastUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = result.message ?: "An unexpected error occurred",
                            castMovieData = result.data ?: emptyList()
                        )
                    }
                }
                is Resource.Success -> {
                    _movieCastUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = null,
                            castMovieData = result.data ?: emptyList()
                        )
                    }
                }
                is Resource.Loading -> {
                    _movieCastUiState.update { prevState ->
                        prevState.copy(
                            isLoading = true,
                            isError = null,
                            castMovieData = result.data ?: emptyList()
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getMovieDetail(movieId : Int) {
        useCase.getMovieDetailUseCase(movieId).onEach { result ->
            when (result) {
                is Resource.Failure -> {
                    _movieDetailUiState.update { prevState ->
                        prevState.copy(
                            isLoading = false,
                            isError = result.message ?: "An unexpected error occurred",
                            detailMovieData = result.data
                        )
                    }
                }
                is Resource.Loading -> {
                    _movieDetailUiState.update { prevState ->
                        prevState.copy(
                            isLoading = true,
                            isError = null,
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