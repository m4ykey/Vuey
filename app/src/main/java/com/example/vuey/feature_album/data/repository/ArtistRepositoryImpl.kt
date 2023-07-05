package com.example.vuey.feature_album.data.repository

import com.example.vuey.feature_album.data.remote.api.ArtistApi
import com.example.vuey.feature_album.data.remote.model.last_fm.Artist
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val artistApi: ArtistApi
) : ArtistRepository {

    override fun getArtistInfo(artistName: String): Flow<Resource<Artist>> {
        return flow {
            emit(Resource.Loading())

            try {
                val artistResponse = artistApi.getArtistInfo(artistName).artist
                emit(Resource.Success(artistResponse))
            } catch (e : HttpException) {
                emit(
                    Resource.Failure(
                        message = e.localizedMessage ?: "An unexpected error occurred",
                        data = null
                    )
                )
            } catch (e : IOException) {
                emit(
                    Resource.Failure(
                        message = e.localizedMessage ?: "No internet connection",
                        data = null
                    )
                )
            }
        }
    }
}