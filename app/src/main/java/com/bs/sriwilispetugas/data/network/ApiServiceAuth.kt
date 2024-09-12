package com.bs.sriwilispetugas.data.network

import com.bs.sriwilispetugas.data.response.LoginResponseDTO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiServiceAuth {
    @FormUrlEncoded
    @POST("petugas/login")
    suspend fun login(
        @Field("no_hp_petugas") no_hp_petugas: String,
        @Field("password_petugas") password_petugas: String
    ): Response<LoginResponseDTO>
}