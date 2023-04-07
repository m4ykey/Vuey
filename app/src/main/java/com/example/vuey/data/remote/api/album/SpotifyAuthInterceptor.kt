package com.example.vuey.data.remote.api.album

import android.content.SharedPreferences
import android.util.Base64
import androidx.core.content.edit
import com.example.vuey.data.remote.response.album.token.AuthApi
import com.example.vuey.util.Constants.SPOTIFY_CLIENT_ID
import com.example.vuey.util.Constants.SPOTIFY_CLIENT_SECRET
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class SpotifyAuthInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val authApi: AuthApi
) : Interceptor {

    private val accessTokenKey = "access_token"
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        val accessToken = sharedPreferences.getString(accessTokenKey, null)
        if (accessToken != null) {
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            return chain.proceed(newRequest)
        }

        val newAccessToken = getAccessToken()
        sharedPreferences.edit {
            putString(accessTokenKey, newAccessToken)
        }

        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $newAccessToken")
            .build()

        return chain.proceed(newRequest)
    }

    fun getAccessToken() : String = runBlocking {
        val authHeader = "Basic " + Base64.encodeToString(
            "${SPOTIFY_CLIENT_ID}:${SPOTIFY_CLIENT_SECRET}".toByteArray(),
            Base64.NO_WRAP
        )

        val response = authApi.getAccessToken(authHeader, "client_credentials")

        response.accessToken
    }
}