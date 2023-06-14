package com.example.vuey.feature_album.use_cases

import com.example.vuey.feature_album.data.repository.AlbumRepository
import com.example.vuey.feature_album.data.remote.model.Album
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumSearchUseCase @Inject constructor(
    private val repository: AlbumRepository
    ) {
    operator fun invoke(albumName : String) : Flow<Resource<List<Album>>> {
        return repository.searchAlbum(albumName)
    }
}