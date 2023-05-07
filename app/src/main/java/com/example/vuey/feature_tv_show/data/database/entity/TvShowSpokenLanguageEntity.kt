package com.example.vuey.feature_tv_show.data.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class TvShowSpokenLanguageEntity(
    @ColumnInfo("tvShowSpokenLanguageName") val tvShowSpokenLanguageName : String
) : Parcelable
