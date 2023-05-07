package com.example.vuey.feature_tv_show.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vuey.feature_movie.data.response.MovieTvShowCreditsResponse
import com.example.vuey.feature_tv_show.data.api.search.SearchTvShow
import com.example.vuey.feature_tv_show.data.database.entity.TvShowCastEntity
import com.example.vuey.feature_tv_show.data.database.entity.TvShowEntity
import com.example.vuey.feature_tv_show.data.database.entity.TvShowEpisodeEntity
import com.example.vuey.feature_tv_show.data.database.entity.TvShowSeasonEntity
import com.example.vuey.feature_tv_show.data.repository.TvShowRepository
import com.example.vuey.feature_tv_show.data.response.TvShowDetailResponse
import com.example.vuey.feature_tv_show.data.response.TvShowSeasonResponse
import com.example.vuey.util.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowViewModel @Inject constructor(
    private val tvShowRepository: TvShowRepository
) : ViewModel() {

    private val _searchTvShow = MutableLiveData<Resource<List<SearchTvShow>>>()
    val searchTvShow : LiveData<Resource<List<SearchTvShow>>> get() = _searchTvShow

    private val _detailTvShow = MutableLiveData<Resource<TvShowDetailResponse>>()
    val detailTvShow : LiveData<Resource<TvShowDetailResponse>> get() = _detailTvShow

    private val _tvShowCredits = MutableLiveData<Resource<MovieTvShowCreditsResponse>>()
    val tvShowCredits : LiveData<Resource<MovieTvShowCreditsResponse>> get() = _tvShowCredits

    private val _tvShowSeasons = MutableLiveData<Resource<TvShowSeasonResponse>>()
    val tvShowSeasons : LiveData<Resource<TvShowSeasonResponse>> get() = _tvShowSeasons

    val getAllTvShows : LiveData<List<TvShowEntity>> = tvShowRepository.getAllTvShows()

    fun getTvShowById(tvShowId: Int) : LiveData<TvShowEntity> {
        return tvShowRepository.getTvShowById(tvShowId)
    }

    fun insertTvShow(tvShowEntity: TvShowEntity) {
        viewModelScope.launch {
            tvShowRepository.insertTvShow(tvShowEntity)
        }
    }

    fun deleteTvShow(tvShowEntity: TvShowEntity) {
        viewModelScope.launch {
            tvShowRepository.deleteTvShow(tvShowEntity)
        }
    }

    fun insertAllEpisodes(episodeList : List<TvShowEpisodeEntity>) {
        viewModelScope.launch {
            tvShowRepository.insertAllEpisodes(episodeList)
        }
    }

    fun getEpisodesById(showId : Int, seasonNumber: Int) : LiveData<List<TvShowEpisodeEntity>> {
        return tvShowRepository.getEpisodesById(showId, seasonNumber)
    }

    fun tvShowSeason(tvShowId: Int, seasonNumber : Int) {
        viewModelScope.launch {
            _tvShowSeasons.value = Resource.Loading()
            try {
                val response = tvShowRepository.tvShowSeason(tvShowId, seasonNumber)
                _tvShowSeasons.value = response.data?.let { Resource.Success(it) }
            } catch (e : Exception) {
                _tvShowSeasons.value = Resource.Failure("Failed to fetch tv show seasons ${e.localizedMessage}")
            }
        }
    }

    fun tvShowCredit(tvShowId: Int) {
        viewModelScope.launch {
            _tvShowCredits.value = Resource.Loading()
            try {
                val response = tvShowRepository.tvShowCredits(tvShowId)
                _tvShowCredits.value = response.data?.let { Resource.Success(it) }
            } catch (e : Exception) {
                _tvShowCredits.value = Resource.Failure("Failed to fetch tv show credits ${e.localizedMessage}")
            }
        }
    }

    fun getDetailTvShow(tvShowId : Int) {
        viewModelScope.launch {
            _detailTvShow.value = Resource.Loading()
            try {
                val response = tvShowRepository.getDetailTvShow(tvShowId)
                _detailTvShow.value = response.data?.let { Resource.Success(it) }
            } catch (e : Exception) {
                _detailTvShow.value = Resource.Failure("Failed to fetch movie details ${e.localizedMessage}")
            }
        }
    }

    fun tvShowSearch(tvShowName : String) {
        viewModelScope.launch {
            _searchTvShow.value = Resource.Loading()
            try {
                val response = tvShowRepository.searchTvShow(tvShowName)
                _searchTvShow.value = response.data?.let { Resource.Success(it) }
            } catch (e : Exception) {
                _searchTvShow.value = Resource.Failure("Failed to fetch movie list ${e.localizedMessage}")
            }
        }
    }
}