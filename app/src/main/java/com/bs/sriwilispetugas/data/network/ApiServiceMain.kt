package com.bs.sriwilispetugas.data.network

import com.bs.sriwilispetugas.data.response.ChangeStatusPesananSampahResponse
import com.bs.sriwilispetugas.data.response.NasabahResponseDTO
import com.bs.sriwilispetugas.data.response.PesananSampahResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiServiceMain {

    @GET("pesanan/show-all-pesanan-sampah-keranjang")
    suspend fun getAllPesananSampahKeranjang(
        @Header("Authorization") token: String
    ): Response<PesananSampahResponseDTO>

    @PUT("pesanan/update-status-berhasil/{id}")
        suspend fun changeStatusPesananSampahBerhasilResponse(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ChangeStatusPesananSampahResponse>

    @PUT("pesanan/update-status-gagal/{id}")
    suspend fun changeStatusPesananSampahGagalResponse(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ChangeStatusPesananSampahResponse>

    @GET("nasabah/show-all")
    suspend fun getAllNasabah(
        @Header("Authorization") token: String
    ): Response<NasabahResponseDTO>


}