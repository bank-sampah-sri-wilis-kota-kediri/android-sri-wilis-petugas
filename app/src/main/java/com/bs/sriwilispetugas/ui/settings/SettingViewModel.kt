package com.bs.sriwilispetugas.ui.settings

import androidx.lifecycle.ViewModel
import com.bs.sriwilispetugas.data.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel(private val repository: MainRepository): ViewModel() {

    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.logout()
        }
    }

}