package com.example.vuey.feature_album.data.repository

import com.example.vuey.feature_album.data.local.dao.AlbumDao
import com.example.vuey.feature_album.data.local.entity.AlbumEntity
import com.example.vuey.feature_album.data.remote.api.AlbumApi
import com.example.vuey.feature_album.data.remote.api.ArtistApi
import com.example.vuey.feature_album.data.remote.model.last_fm.Artist
import com.example.vuey.feature_album.data.remote.model.spotify.album_search.Album
import com.example.vuey.feature_album.data.remote.model.spotify.album_detail.AlbumDetail
import com.example.vuey.feature_album.data.remote.token.SpotifyInterceptor
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlbumRepositoryImpl @Inject constructor(
    private val albumApi: AlbumApi,
    private val albumDao: AlbumDao,
    private val spotifyInterceptor: SpotifyInterceptor,
    private val artistApi: ArtistApi
) : AlbumRepository {

    override suspend fun insertAlbum(albumEntity: AlbumEntity) {
        return albumDao.insertAlbum(albumEntity)
    }

    override suspend fun deleteAlbum(albumEntity: AlbumEntity) {
        return albumDao.deleteAlbum(albumEntity)
    }

    override fun getAllAlbums(): Flow<List<AlbumEntity>> {
        return albumDao.getAllAlbums()
    }

    override fun getAlbumById(albumId: String): Flow<AlbumEntity> {
        return albumDao.getAlbumById(albumId)
    }

    override fun getAlbumCount(): Flow<Int> {
        return albumDao.getAlbumCount()
    }

    override fun getTotalTracks(): Flow<Int> {
        return albumDao.getTotalTracks()
    }

    override fun getTotalLength(): Flow<Int> {
        return albumDao.getTotalLength()
    }

    override fun searchAlbumInDatabase(searchQuery: String): Flow<List<AlbumEntity>> {
        return albumDao.searchAlbumInDatabase(searchQuery)
    }

    override fun getArtistTopTracks(artistId: String): Flow<Resource<List<Track>>> {
        return flow {
            emit(Resource.Loading())

            try {
                val artistResponse = artistApi.getArtistTopTracks(
                    artistId = artistId,
                    token = "Bearer ${spotifyInterceptor.getAccessToken()}"
                ).tracks
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

    override fun getArtist(artistId: String): Flow<Resource<ArtistDetail>> {
        return flow {
            emit(Resource.Loading())

            try {
                val albumResponse = artistApi.getArtist(
                    artistId = artistId,
                    token = "Bearer ${spotifyInterceptor.getAccessToken()}"
                )
                emit(Resource.Success(albumResponse))
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

    override fun searchAlbum(albumName: String): Flow<Resource<List<Album>>> {
        return flow {
            emit(Resource.Loading())

            try {
                val albumResponse = albumApi.searchAlbum(
                    token = "Bearer ${spotifyInterceptor.getAccessToken()}",
                    query = albumName
                ).albums.items
                emit(Resource.Success(albumResponse))

            } catch (e: HttpException) {
                emit(
                    Resource.Failure(
                        message = e.localizedMessage ?: "An unexpected error occurred",
                        data = null
                    )
                )
            } catch (e: IOException) {
                emit(
                    Resource.Failure(
                        message = e.localizedMessage ?: "No internet connection",
                        data = null
                    )
                )
            }
        }
    }

    override fun getAlbum(albumId: String): Flow<Resource<AlbumDetail>> {
        return flow {
            emit(Resource.Loading())

            try {
                val albumResponse = albumApi.getAlbum(
                    token = "Bearer ${spotifyInterceptor.getAccessToken()}",
                    albumId = albumId
                )
                emit(Resource.Success(albumResponse))
            } catch (e: HttpException) {
                emit(
                    Resource.Failure(
                        message = e.localizedMessage ?: "An unexpected error occurred",
                        data = null
                    )
                )
            } catch (e: IOException) {
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