package com.example.vuey.data.repository

import com.example.vuey.data.remote.api.TvShowApi
import javax.inject.Inject

class TvShowRepository @Inject constructor(
    private val tvShowApi: TvShowApi
){
}