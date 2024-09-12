package com.bs.sriwilispetugas.ui.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bs.sriwilispetugas.data.repository.MainRepository
import kotlinx.coroutines.launch
import com.bs.sriwilispetugas.helper.Result

class WelcomeViewModel(private val repository: MainRepository) : ViewModel() {

    suspend fun fetchToken(): String? {
        return repository.getToken()
    }

    suspend fun syncData(): Result<Unit> {
        return repository.syncData()
    }
}
