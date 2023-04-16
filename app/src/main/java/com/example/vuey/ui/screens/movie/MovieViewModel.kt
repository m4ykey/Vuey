package com.example.vuey.ui.screens.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.data.database.model.MovieEntity
import com.example.vuey.data.models.movie.search.SearchMovie
import com.example.vuey.data.remote.response.movie.MovieCreditsResponse
import com.example.vuey.data.remote.response.movie.MovieDetailResponse
import com.example.vuey.data.repository.MovieRepository
import com.example.vuey.util.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movieSearch = MutableLiveData<Resource<List<SearchMovie>>>()
    val movieSearch : LiveData<Resource<List<SearchMovie>>> get() = _movieSearch

    private val _movieDetail = MutableLiveData<Resource<MovieDetailResponse>>()
    val movieDetail : LiveData<Resource<MovieDetailResponse>> get() = _movieDetail

    private val _movieCredits = MutableLiveData<Resource<MovieCreditsResponse>>()
    val movieCredits : LiveData<Resource<MovieCreditsResponse>> get() = _movieCredits

    val getAllMovies : LiveData<List<MovieEntity>> = movieRepository.getAllMovies()

    fun insertMovie(movieEntity: MovieEntity) {
        viewModelScope.launch {
            movieRepository.insertMovie(movieEntity)
        }
    }

    fun deleteMovie(movieEntity: MovieEntity) {
        viewModelScope.launch {
            movieRepository.deleteMovie(movieEntity)
        }
    }

    fun insertCast(castEntity: List<MovieEntity.CastEntity>) {
        viewModelScope.launch {
            movieRepository.insertCast(castEntity)
        }
    }

    fun getCast(movieId : Int) : LiveData<List<MovieEntity.CastEntity>> {
        return movieRepository.getCast(movieId)
    }

    fun movieCredit(movieId: Int) {
        viewModelScope.launch {
            _movieCredits.value = Resource.Loading()
            try {
                val response = movieRepository.movieCredits(movieId)
                _movieCredits.value = response.data?.let { Resource.Success(it) }
            } catch (e : Exception) {
                _movieCredits.value = Resource.Failure("Failed to fetch movie credits ${e.localizedMessage}")
            }
        }
    }

    fun movieDetail(movieId : Int) {
        viewModelScope.launch {
            _movieDetail.value = Resource.Loading()
            try {
                val response = movieRepository.movieDetails(movieId)
                _movieDetail.value = response.data?.let { Resource.Success(it) }
            } catch (e : Exception) {
                _movieDetail.value = Resource.Failure("Failed to fetch movie details ${e.localizedMessage}")
            }
        }
    }

    fun movieSearch(movieName : String) {
        viewModelScope.launch {
            _movieSearch.value = Resource.Loading()
            try {
                val response = movieRepository.searchMovie(movieName)
                _movieSearch.value = response.data?.let { Resource.Success(it) }
            } catch (e : Exception) {
                _movieSearch.value = Resource.Failure("Failed to fetch movie list ${e.localizedMessage}")
            }
        }
    }

}