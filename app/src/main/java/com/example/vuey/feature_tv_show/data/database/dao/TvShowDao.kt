package com.example.vuey.feature_tv_show.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vuey.feature_tv_show.data.database.entity.TvShowEntity

@Dao
interface TvShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvShow(tvShowEntity: TvShowEntity)

    @Query("SELECT * FROM tv_show_table ORDER BY saveTime ASC")
    fun getAllTvShows() : LiveData<List<TvShowEntity>>

    @Query("SELECT * FROM tv_show_table WHERE id = :tvShowId")
    fun getTvShowById(tvShowId : Int) : LiveData<TvShowEntity>

    @Delete
    suspend fun deleteTvShow(tvShowEntity: TvShowEntity)
}