package com.example.vuey.data.remote.api

import com.example.vuey.util.Constants.LAST_FM_API_KEY
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiClient @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val url = chain.request().url
            .newBuilder()
            .addQueryParameter("api_key", LAST_FM_API_KEY)
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}