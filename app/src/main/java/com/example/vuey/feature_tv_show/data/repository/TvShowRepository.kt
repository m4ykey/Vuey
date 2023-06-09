package com.example.vuey.feature_tv_show.data.repository

import androidx.lifecycle.LiveData
import com.example.vuey.feature_movie.data.remote.model.Credits
import com.example.vuey.feature_tv_show.data.api.TvShowApi
import com.example.vuey.feature_tv_show.data.api.search.SearchTvShow
import com.example.vuey.feature_tv_show.data.database.dao.TvShowDao
import com.example.vuey.feature_tv_show.data.database.entity.TvShowEntity
import com.example.vuey.feature_tv_show.data.response.TvShowDetailResponse
import com.example.vuey.feature_tv_show.data.response.TvShowSeasonResponse
import com.example.vuey.util.network.Resource
import javax.inject.Inject

class TvShowRepository @Inject constructor(
    private val tvShowApi: TvShowApi,
    private val tvShowDao: TvShowDao
) {

    suspend fun insertTvShow(tvShowEntity: TvShowEntity) {
        return tvShowDao.insertTvShow(tvShowEntity)
    }

    suspend fun deleteTvShow(tvShowEntity: TvShowEntity) {
        return tvShowDao.deleteTvShow(tvShowEntity)
    }

    fun getAllTvShows() : LiveData<List<TvShowEntity>> {
        return tvShowDao.getAllTvShows()
    }

    fun getTvShowById(tvShowId: Int) : LiveData<TvShowEntity> {
        return tvShowDao.getTvShowById(tvShowId)
    }

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

    suspend fun tvShowCredits(tvShowId : Int) : Resource<Credits> {
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