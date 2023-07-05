package com.example.vuey.feature_album.presentation.viewmodel.use_cases

import com.example.vuey.feature_album.data.remote.model.last_fm.Artist
import com.example.vuey.feature_album.data.repository.ArtistRepository
import com.example.vuey.util.network.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtistUseCase @Inject constructor(
    private val repository: ArtistRepository
) {
    operator fun invoke(artistName : String) : Flow<Resource<Artist>> {
        return repository.getArtistInfo(artistName)
    }
}