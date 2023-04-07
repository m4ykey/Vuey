package com.example.vuey.ui.screens.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.data.local.movie.search.SearchMovie
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