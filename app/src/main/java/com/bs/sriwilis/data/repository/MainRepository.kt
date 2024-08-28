package com.bs.sriwilis.data.repository

import android.util.Log
import com.bs.sriwilis.data.network.ApiServiceMain
import com.bs.sriwilis.data.preference.UserPreferences
import com.bs.sriwilis.data.response.AddCategoryRequest
import com.bs.sriwilis.data.response.CategoryResponse
import com.bs.sriwilis.data.response.GetAllUserResponse
import com.bs.sriwilis.data.response.RegisterUserResponse
import com.bs.sriwilis.data.response.UserItem
import kotlinx.coroutines.flow.Flow
import com.bs.sriwilis.helper.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class MainRepository(
    private val apiService: ApiServiceMain,
    val userPreferences: UserPreferences
) {

    fun getToken(): Flow<String?> {
        return userPreferences.token
    }

    suspend fun logout() {
        userPreferences.clearUserDetails()
    }

    suspend fun registerUser(phone: String, password: String, name: String, address: String, balance: String): Result<RegisterUserResponse> {
        return try {
            val token = userPreferences.token.first() ?: ""

            val response = apiService.registerUser(token, phone, password, name, address, balance)
            if (response.isSuccessful) {
                val registerResponse = response.body()
                if (registerResponse != null) {
                    Result.Success(registerResponse)
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Failed to register: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("Register", "Registration error", e)
            Result.Error("Register error: ${e.message}")
        }
    }

    suspend fun getUserById(userId: String): Result<UserItem> {
        return try {
            val token = userPreferences.token.first() ?: ""
            val response = apiService.getUserById(userId, "Bearer $token")

            if (response.isSuccessful) {
                val userDetailsResponse = response.body()
                if (userDetailsResponse != null) {
                    val userItem = userDetailsResponse.data
                    if (userItem != null) {
                        Result.Success(userItem)
                    } else {
                        Result.Error("User not found")
                    }
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Failed to fetch user details: ${response.message()} (${response.code()})")
            }
        } catch (e: Exception) {
            Log.e("GetUserDetails", "Error fetching user details", e)
            Result.Error("Error fetching user details: ${e.message}")
        }
    }



    suspend fun editUser(userId: String, phone: String, name: String, address: String, balance: Double): Result<RegisterUserResponse> {
        return try {
            val token = userPreferences.token.first() ?: ""

            val response = apiService.editUser(userId, token, phone, name, address, balance)
            if (response.isSuccessful) {
                val editResponse = response.body()
                if (editResponse != null) {
                    Result.Success(editResponse)
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Failed to edit: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("EditUser", "Edit error", e)
            Result.Error("Edit error: ${e.message}")
        }
    }

    suspend fun deleteUser(userId: String): Result<Boolean> {
        return try {
            val token = userPreferences.token.first() ?: ""
            val response = apiService.deleteUser(userId, "Bearer $token")

            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error("Failed to remove bookmark: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("MainRepository", "Exception occurred: ${e.message}")
            Result.Error("Error occurred: ${e.message}")
        }
    }


    suspend fun getUser(): Result<GetAllUserResponse> {
        return try {
            val token = userPreferences.token.first() ?: ""
            Log.d("tokenmainrepository", "$token")
            val response = apiService.getAllUser("Bearer $token")

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Error("Response body is null")
                }
            } else {
                Result.Error("Failed to fetch saved news: ${response.message()} (${response.code()})")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }

    suspend fun addCategory(
        token: String,
        name: String,
        price: String,
        type: String,
        imageBase64: String
    ): Result<CategoryResponse> {
        val categoryRequest = AddCategoryRequest(
            nama_kategori = name,
            harga_kategori = price,
            jenis_kategori = type,
            gambar_kategori = imageBase64
        )
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.addCategory("Bearer $token", categoryRequest)
                if (response.isSuccessful) {
                    val categoryResponse = response.body()
                    if (categoryResponse != null) {
                        Result.Success(categoryResponse)
                    } else {
                        Result.Error("Empty response body")
                    }
                } else {
                    Result.Error("Failed to fetch saved news: ${response.message()} (${response.code()})")
                }
            } catch (e: Exception) {
                Result.Error("Exception occured: ${e.message}")
            }
        }
    }

    companion object {
        @Volatile
        private var instance: MainRepository? = null

        fun getInstance(apiServiceMain: ApiServiceMain, userPreferences: UserPreferences): MainRepository {
            return instance ?: synchronized(this) {
                instance ?: MainRepository(apiServiceMain, userPreferences).also { instance = it }
            }
        }
    }
}