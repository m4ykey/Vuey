package com.example.vuey.util.utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.palette.graphics.Palette
import com.example.vuey.R
import com.example.vuey.util.network.SpotifyError
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
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

@SuppressLint("InflateParams")
fun showSnackbar(view: View, errorCode: String, duration: Int = Snackbar.LENGTH_SHORT) {

    val snackBar = Snackbar.make(view, errorCode, duration)
    snackBar.view.setBackgroundColor(Color.TRANSPARENT)

    val customSnackbar : View = LayoutInflater.from(view.context)
        .inflate(R.layout.custom_snackbar, null)

    val errorMessage = getSpotifyErrorMessage(errorCode)
    val txtError : TextView = customSnackbar.findViewById(R.id.txtError)
    txtError.text = errorMessage

    val snackbarLayout = snackBar.view as SnackbarLayout
    snackbarLayout.apply {
        setPadding(0,0,0,0)
        addView(customSnackbar, 0)
    }
    snackBar.show()
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

fun getSpotifyErrorMessage(errorCode : String) : String {
    return when (errorCode) {
        SpotifyError.code200 -> "Error 200: OK - The request has succeeded."
        SpotifyError.code201 -> "Error 201: Created - The request has been fulfilled and resulted in a new resource being created."
        SpotifyError.code202 -> "Error 202: Accepted - The request has been accepted for processing, but the processing has not been completed."
        SpotifyError.code204 -> "Error 204: No Content - The request has succeeded but returns no message body."
        SpotifyError.code304 -> "Error 304: Not Modified"
        SpotifyError.code400 -> "Error 400: Bad Request - The request could not be understood by the server due to malformed syntax."
        SpotifyError.code401 -> "Error 401: Unauthorized - The request requires user authentication or, if the request included authorization credentials, authorization has been refused for those credentials."
        SpotifyError.code403 -> "Error 403: Forbidden - The server understood the request, but is refusing to fulfill it."
        SpotifyError.code404 -> "Error 404: Not Found - The requested resource could not be found. This error can be due to a temporary or permanent condition."
        SpotifyError.code429 -> "Error 429: Too Many Requests."
        SpotifyError.code500 -> "Error 500: Internal Server Error."
        SpotifyError.code502 -> "Error 502: Bad Gateway - The server was acting as a gateway or proxy and received an invalid response from the upstream server."
        SpotifyError.code503 -> "Error 503: Service Unavailable - The server is currently unable to handle the request due to a temporary condition which will be alleviated after some delay. You can choose to resend the request again."
        else -> ""
    }
}