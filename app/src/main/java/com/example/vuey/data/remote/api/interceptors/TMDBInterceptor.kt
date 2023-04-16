package com.example.vuey.data.remote.api.interceptors

import com.example.vuey.util.Constants.TMDB_API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class TMDBInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val url = chain.request().url
            .newBuilder()
            .addQueryParameter("api_key", TMDB_API_KEY)
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}