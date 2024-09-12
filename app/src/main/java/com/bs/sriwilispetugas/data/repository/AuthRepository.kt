package com.bs.sriwilispetugas.data.repository

import android.util.Log
import com.bs.sriwilispetugas.data.network.ApiServiceAuth
import com.bs.sriwilispetugas.data.response.LoginResponseDTO
import com.bs.sriwilispetugas.helper.Result
import com.bs.sriwilispetugas.data.room.AppDatabase
import com.bs.sriwilispetugas.data.room.LoginResponseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val apiService: ApiServiceAuth,
    private val appDatabase: AppDatabase
) {

    suspend fun login(phone: String, password: String): Result<LoginResponseEntity> {
        return try {
            val response = apiService.login(phone, password)
            if (response.isSuccessful) {
                val loginResponseDTO = response.body()
                if (loginResponseDTO?.dataPetugas != null) {
                    val loginResponseEntity = mapDtoToEntity(loginResponseDTO)
                    saveLoginResponseToRoom(loginResponseEntity)
                    Result.Success(loginResponseEntity)
                } else {
                    Result.Error("Empty Response Body")
                }
            } else {
                Result.Error("Failed to login: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }


    private fun mapDtoToEntity(dto: LoginResponseDTO): LoginResponseEntity {
        return LoginResponseEntity(
            id = 1,
            success = dto.success,
            message = dto.message,
            access_token = dto.dataPetugas?.access_token,
            nama_petugas = dto.dataPetugas?.nama_petugas
        )
    }

    private suspend fun saveLoginResponseToRoom(loginResponse: LoginResponseEntity) {
        withContext(Dispatchers.IO) {
            Log.d("Saving login response", loginResponse.access_token.toString())
            appDatabase.loginResponseDao().insert(loginResponse)
        }
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null

        fun getInstance(apiService: ApiServiceAuth, appDatabase: AppDatabase): AuthRepository {
            return instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, appDatabase).also { instance = it }
            }
        }
    }
}
