package com.example.vuey.util.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.palette.graphics.Palette
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale

fun Double.formatVoteAverage(): String = String.format("%.1f", this)

object DateUtils {
    fun formatAirDate(airDate: String?): String? {
        return airDate?.let {
            val inputDateFormat = if (it.length > 4) SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ) else SimpleDateFormat("yyyy", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
            val date = try {
                inputDateFormat.parse(it)
            } catch (e: Exception) {
                null
            }
            date?.let { d ->
                outputDateFormat.format(d)
            }
        }
    }
}

fun showSnackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, message, duration).show()
}

class PreDrawListener(private val imgAlbum : ImageView, private val layoutAlbum : MaterialCardView)
    : ViewTreeObserver.OnPreDrawListener {
    override fun onPreDraw(): Boolean {
        imgAlbum.viewTreeObserver.removeOnPreDrawListener(this)

        if (imgAlbum.isShown) {
            val drawable = imgAlbum.drawable
            if (drawable is BitmapDrawable) {
                val originalBitmap = drawable.bitmap
                val bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)
                Palette.from(bitmap).generate { palette ->
                    val dominantColor = palette?.dominantSwatch?.rgb ?: Color.GRAY
                    layoutAlbum.strokeColor = dominantColor
                }
            }
        }
        return true
    }
}