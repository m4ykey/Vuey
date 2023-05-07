package com.example.vuey.feature_album.data.api.token

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @FormUrlEncoded
    @POST("/api/token")
    suspend fun getAccessToken(
        @Header("Authorization") authHeader : String,
        @Field("grant_type") grantType : String
    ) : AccessTokenResponse

}