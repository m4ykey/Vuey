package com.example.vuey.feature_album.data.remote.token

import android.content.SharedPreferences
import android.util.Base64
import com.example.vuey.BuildConfig.SPOTIFY_CLIENT_ID
import com.example.vuey.BuildConfig.SPOTIFY_CLIENT_SECRET
import com.example.vuey.feature_album.data.remote.api.AuthApi
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class SpotifyInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val authApi: AuthApi
) : Interceptor {

    private val accessTokenKey = "access_token"

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val request = chain.request()

        val accessToken = sharedPreferences.getString(accessTokenKey, null) ?: getAccessToken()

        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        chain.proceed(newRequest)
    }

    suspend fun getAccessToken(): String {
        val authHeader = "Basic " + Base64.encodeToString(
            "${SPOTIFY_CLIENT_ID}:${SPOTIFY_CLIENT_SECRET}".toByteArray(),
            Base64.NO_WRAP
        )

        val response = authApi.getAccessToken(authHeader, "client_credentials")

        return response.accessToken
    }
}