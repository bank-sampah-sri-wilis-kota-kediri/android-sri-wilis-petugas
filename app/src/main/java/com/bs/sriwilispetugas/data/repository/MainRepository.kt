package com.bs.sriwilispetugas.data.repository

import android.util.Log
import com.bs.sriwilispetugas.data.network.ApiServiceMain
import com.bs.sriwilispetugas.data.room.AppDatabase
import com.bs.sriwilispetugas.data.repository.mapping.Mapping
import com.bs.sriwilispetugas.data.repository.modelhelper.CardDetailPesanan
import com.bs.sriwilispetugas.data.repository.modelhelper.CardPesanan
import com.bs.sriwilispetugas.data.response.ChangeStatusPesananSampahResponse
import com.bs.sriwilispetugas.data.room.NasabahEntity
import com.bs.sriwilispetugas.data.room.PesananSampahKeranjangEntity
import com.bs.sriwilispetugas.helper.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(
    private val apiService: ApiServiceMain,
    private val appDatabase: AppDatabase
) {
    private val mappingPesananSampah = Mapping()

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            appDatabase.loginResponseDao().deleteAll()
            appDatabase.pesananSampahkeranjangDao().deleteAllPesananSampahKeranjang()
            appDatabase.pesananSampahDao().deleteAllPesananSampah()
            appDatabase.nasabahDao().deleteAllNasabah()
        }
    }


    suspend fun getToken(): String? {
        val loginResponse = appDatabase.loginResponseDao().getLoginResponseById(1)
        return loginResponse?.access_token
    }

    suspend fun getAllPesanan(): Result<List<PesananSampahKeranjangEntity>> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            val response = apiService.getAllPesananSampahKeranjang("Bearer $token")

            if (response.isSuccessful) {
                val responseBody = response.body() ?: return Result.Error("Response body is null")

                // Mapping dari DTO ke Entitas Room
                val (keranjangEntities, sampahEntities) = mappingPesananSampah.mapPesananSampahApiResponseDtoToEntities(responseBody)

                // Simpan data ke database Room (opsional, jika perlu disimpan)
                withContext(Dispatchers.IO) {
                    appDatabase.pesananSampahkeranjangDao().insertAllPesananSampahKeranjang(keranjangEntities)
                    appDatabase.pesananSampahDao().insertAllPesananSampah(sampahEntities)
                }
                Result.Success(keranjangEntities)
            } else {
                Result.Error("Failed to fetch data: ${response.message()} (${response.code()})")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }


    suspend fun getAllNasabah(): Result<List<NasabahEntity>> {
        return try {
            val token = getToken() ?: return Result.Error("Token is null")

            val response = apiService.getAllNasabah("Bearer $token")

            if (response.isSuccessful) {
                val responseBody = response.body() ?: return Result.Error("Response body is null")

                // Mapping dari DTO ke Entitas Room
                val nasabahEntities = mappingPesananSampah.mapNasabahResponseDtoToEntity(responseBody)

                // Simpan data ke database Room (opsional, jika perlu disimpan)
                withContext(Dispatchers.IO) {
                    appDatabase.nasabahDao().insertAllNasabah(nasabahEntities)
                }

                Result.Success(nasabahEntities)
            } else {
                Result.Error("Failed to fetch data: ${response.message()} (${response.code()})")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }


    suspend fun getCombinedPesananData(): Result<List<CardPesanan>> {
        return withContext(Dispatchers.IO) {
            try {
                val combinedData = appDatabase.pesananSampahkeranjangDao().getCombinedPesananData()
                Log.d("cek combined data", combinedData.toString())
                Result.Success(combinedData)
            } catch (e: Exception) {
                Result.Error("Error occurred: ${e.message}")
            }
        }
    }

    suspend fun getDataDetailPesananSampahKeranjang(idPesanan: String): Result<CardPesanan> {
        return withContext(Dispatchers.IO) {
            try {
                val detailPesananSampahKeranjang = appDatabase.pesananSampahkeranjangDao().getDataDetailPesananSampahKeranjang(idPesanan)
                Result.Success(detailPesananSampahKeranjang)
            } catch (e: Exception) {
                Result.Error("Error occurred: ${e.message}")
            }
        }
    }

    suspend fun getPesananSampah(idPesanan: String): Result<List<CardDetailPesanan>> {
        return withContext(Dispatchers.IO) {
            try {
                val combinedData = appDatabase.pesananSampahkeranjangDao().getPesananSampah(idPesanan)
                Result.Success(combinedData)
            } catch (e: Exception) {
                Result.Error("Error occurred: ${e.message}")
            }
        }
    }


    suspend fun changeStatusPesananSampahResponse(idPesanan: String, status: String): Result<ChangeStatusPesananSampahResponse>{
        return try {
            val token = getToken() ?: return Result.Error("Token is null")
            val response = when (status) {
                "selesai diantar" -> apiService.changeStatusPesananSampahBerhasilResponse("Bearer $token", idPesanan)
                "gagal" -> apiService.changeStatusPesananSampahGagalResponse("Bearer $token", idPesanan)
                else -> return Result.Error("Invalid status: $status")
            }
            if (response.isSuccessful) {
                val responseBody = response.body() ?: return Result.Error("Response body is null")
                Result.Success(responseBody)
            } else {
                Result.Error("Failed to fetch data: ${response.message()} (${response.code()})")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }


    suspend fun syncData(): Result<Unit> {
        return try {
            appDatabase.pesananSampahkeranjangDao().deleteAllPesananSampahKeranjang()
            appDatabase.pesananSampahDao().deleteAllPesananSampah()
            appDatabase.nasabahDao().deleteAllNasabah()

            val pesananResult = getAllPesanan()
            if (pesananResult is Result.Error) {
                return Result.Error("Failed to sync pesanan: ${pesananResult.error}")
            }

            val nasabahResult = getAllNasabah()
            if (nasabahResult is Result.Error) {
                return Result.Error("Failed to sync nasabah: ${nasabahResult.error}")
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Error occurred during synchronization: ${e.message}")
        }
    }


    companion object {
        @Volatile
        private var instance: MainRepository? = null

        fun getInstance(apiServiceMain: ApiServiceMain, appDatabase: AppDatabase): MainRepository {
            return instance ?: synchronized(this) {
                instance ?: MainRepository(apiServiceMain, appDatabase).also { instance = it }
            }
        }
    }
}
