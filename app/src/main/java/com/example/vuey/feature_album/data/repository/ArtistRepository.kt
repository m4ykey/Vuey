package com.example.vuey.feature_album.data.repository

import com.example.vuey.feature_album.data.remote.model.last_fm.Artist
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {

    fun getArtistInfo(artistName : String) : Flow<Resource<Artist>>

}