package com.example.vuey.data.repository

import com.example.vuey.data.models.tv_show.SearchTvShow
import com.example.vuey.data.remote.api.TvShowApi
import com.example.vuey.data.remote.response.movie.MovieTvShowCreditsResponse
import com.example.vuey.data.remote.response.tv_show.TvShowDetailResponse
import com.example.vuey.data.remote.response.tv_show.TvShowSeasonResponse
import com.example.vuey.util.network.Resource
import javax.inject.Inject

class TvShowRepository @Inject constructor(
    private val tvShowApi: TvShowApi
) {

    suspend fun searchTvShow(tvShowName : String) : Resource<List<SearchTvShow>> {

        return try {
            val response = tvShowApi.searchTvShow(tvShowName)
            if (response.isSuccessful) {
                val tvShowSearchResponse = response.body()
                val tvShows = tvShowSearchResponse!!.results
                Resource.Success(tvShows)
            } else {
                Resource.Failure("Failed to fetch tv show list")
            }
        } catch (e : Exception) {
            Resource.Failure("Failed to fetch tv show list ${e.localizedMessage}")
        }
    }

    suspend fun getDetailTvShow(tvShowId : Int) : Resource<TvShowDetailResponse> {

        return try {
            val response = tvShowApi.detailTvShow(tvShowId)
            if (response.isSuccessful) {
                val tvShowDetailResponse = response.body()
                val tvShows = tvShowDetailResponse!!
                Resource.Success(tvShows)
            } else {
                Resource.Failure("Failed to fetch tv show details")
            }
        } catch (e : Exception) {
            Resource.Failure("Failed to fetch tv show details ${e.localizedMessage}")
        }
    }

    suspend fun tvShowCredits(tvShowId : Int) : Resource<MovieTvShowCreditsResponse> {
        return try {
            val response = tvShowApi.tvShowCredits(tvShowId)
            if (response.isSuccessful) {
                val tvShowCreditsResponse = response.body()
                val tvShowCredit = tvShowCreditsResponse!!
                Resource.Success(tvShowCredit)
            } else {
                Resource.Failure("Failed to fetch tv show credits")
            }
        } catch (e : Exception) {
            Resource.Failure("Failed to fetch tv show credits ${e.localizedMessage}")
        }
    }

    suspend fun tvShowSeason(tvShowId : Int, seasonNumber : Int) : Resource<TvShowSeasonResponse> {
        return try {
            val response = tvShowApi.tvShowSeasons(tvShowId, seasonNumber)
            if (response.isSuccessful) {
                val tvShowSeasonResponse = response.body()
                val tvShowSeason = tvShowSeasonResponse!!
                Resource.Success(tvShowSeason)
            } else {
                Resource.Failure("Failed to fetch tv show seasons")
            }
        } catch (e : Exception) {
            Resource.Failure("Failed to fetch tv show seasons ${e.localizedMessage}")
        }
    }

}