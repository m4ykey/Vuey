package com.example.vuey.feature_tv_show.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vuey.feature_tv_show.data.database.entity.TvShowCastEntity
import com.example.vuey.feature_tv_show.data.database.entity.TvShowEntity
import com.example.vuey.feature_tv_show.data.database.entity.TvShowEpisodeEntity
import com.example.vuey.feature_tv_show.data.database.entity.TvShowSeasonEntity

@Dao
interface TvShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvShow(tvShowEntity: TvShowEntity)

    @Query("SELECT * FROM tv_show_table")
    fun getAllTvShows() : LiveData<List<TvShowEntity>>

    @Query("SELECT * FROM tv_show_table WHERE id = :tvShowId")
    fun getTvShowById(tvShowId : Int) : LiveData<TvShowEntity>

    @Delete
    suspend fun deleteTvShow(tvShowEntity: TvShowEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEpisodes(tvShowEpisodeEntity: List<TvShowEpisodeEntity>)

    @Query("SELECT * FROM tv_show_episode_table WHERE showId = :showId AND seasonNumber = :seasonNumber")
    fun getEpisodesById(showId: Int, seasonNumber : Int) : LiveData<List<TvShowEpisodeEntity>>

    @Query("DELETE FROM tv_show_episode_table WHERE showId = :showId")
    fun deleteAllEpisodes(showId: Int) : Int

}