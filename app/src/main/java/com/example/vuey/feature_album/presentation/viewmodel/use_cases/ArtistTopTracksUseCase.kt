package com.example.vuey.feature_album.presentation.viewmodel.use_cases

import com.example.vuey.feature_album.data.repository.AlbumRepository
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtistTopTracksUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    operator fun invoke(artistId : String) : Flow<Resource<List<Track>>> {
        return repository.getArtistTopTracks(artistId)
    }
}