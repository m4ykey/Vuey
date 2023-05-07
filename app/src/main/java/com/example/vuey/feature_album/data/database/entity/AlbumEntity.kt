package com.example.vuey.feature_album.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vuey.util.Constants.ALBUM_TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = ALBUM_TABLE_NAME)
data class AlbumEntity(
    @ColumnInfo("trackList") val trackList: List<TrackListEntity>,
    @ColumnInfo("saveTime") val saveTime: Long = System.currentTimeMillis(),
    @ColumnInfo("albumType") val albumType: String,
    @ColumnInfo("releaseDate") val release: String,
    @ColumnInfo("artistList") val artistList: List<ArtistEntity>,
    @ColumnInfo("externalUrls") val externalUrls: ExternalUrlsEntity,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id") val id: String,
    @ColumnInfo("imageList") val imageList: List<ImageEntity>,
    @ColumnInfo("albumName") val albumName: String,
    @ColumnInfo("totalTracks") val totalTracks: Int,
    @ColumnInfo("albumLength") val albumLength : String
) : Parcelable {

    @Parcelize
    data class TrackListEntity(
        @ColumnInfo("durationMs") val durationMs: Int,
        @ColumnInfo("trackNumber") val trackNumber: Int,
        @ColumnInfo("albumName") val albumName: String,
        @ColumnInfo("artistList") val artistList: List<ArtistEntity>,
        @ColumnInfo("discNumber") val discNumber : Int
    ) : Parcelable

    @Parcelize
    data class ArtistEntity(
        @ColumnInfo("externalUrls")  val externalUrls: ExternalUrlsEntity,
        @ColumnInfo("id") val id: String,
        @ColumnInfo("name") val name: String
    ) : Parcelable

    @Parcelize
    data class ExternalUrlsEntity(
        @ColumnInfo("spotify") val spotify: String
    ) : Parcelable

    @Parcelize
    data class ImageEntity(
        @ColumnInfo("height") val height: Int,
        @ColumnInfo("url") val url: String,
        @ColumnInfo("width") val width: Int
    ) : Parcelable

}
