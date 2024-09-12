package com.bs.sriwilispetugas.helper

import ApiConfig
import android.content.Context
import com.bs.sriwilispetugas.data.repository.AuthRepository
import com.bs.sriwilispetugas.data.repository.MainRepository
import com.bs.sriwilispetugas.data.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

object InjectionAuth {
    fun provideRepository(context: Context): AuthRepository {
        val apiService = ApiConfig.getAuthService()
        val appDatabase = AppDatabase.getInstance(context)
        return AuthRepository.getInstance(apiService, appDatabase)
    }
}

object InjectionMain {
    fun provideRepository(context: Context): MainRepository {
        val appDatabase = AppDatabase.getInstance(context)
        val mainRepository = MainRepository.getInstance(ApiConfig.getMainService(""), appDatabase)
        val token: String = runBlocking(Dispatchers.IO) {
            mainRepository.getToken() ?: ""
        }

        val apiServiceMain = ApiConfig.getMainService(token)
        return MainRepository.getInstance(apiServiceMain, appDatabase)
    }
}
