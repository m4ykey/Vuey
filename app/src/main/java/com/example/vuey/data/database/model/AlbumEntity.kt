package com.example.vuey.data.database.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.util.Constants.ALBUM_TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = ALBUM_TABLE_NAME)
data class AlbumEntity(
    val trackList: List<TrackListEntity>,
    val saveTime: Long = System.currentTimeMillis(),
    val albumType: String,
    val release_date: String,
    val artists: List<ArtistEntity>,
    val externalUrls: ExternalUrlsEntity,
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val images: List<ImageEntity>,
    val albumName: String,
    val totalTracks: Int
) : Parcelable {

    @Parcelize
    data class TrackListEntity(
        val durationMs: Int,
        val trackNumber: Int,
        val albumName: String,
        val artists: List<ArtistEntity>
    ) : Parcelable

    @Parcelize
    data class ArtistEntity(
        val externalUrls: ExternalUrlsEntity,
        val id: String,
        val name: String
    ) : Parcelable

    @Parcelize
    data class ExternalUrlsEntity(
        val spotify: String
    ) : Parcelable

    @Parcelize
    data class ImageEntity(
        val height: Int,
        val url: String,
        val width: Int
    ) : Parcelable

}
