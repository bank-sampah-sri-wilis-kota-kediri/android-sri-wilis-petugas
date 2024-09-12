package com.bs.sriwilispetugas.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bs.sriwilispetugas.helper.InjectionAuth
import com.bs.sriwilispetugas.helper.InjectionMain
import com.bs.sriwilispetugas.ui.authorization.LoginViewModel
import com.bs.sriwilispetugas.ui.history.HistoryViewModel
import com.bs.sriwilispetugas.ui.homepage.HomePageViewModel
import com.bs.sriwilispetugas.ui.homepage.PesananDetailViewModel
import com.bs.sriwilispetugas.ui.settings.SettingViewModel
import com.bs.sriwilispetugas.ui.splashscreen.WelcomeViewModel

class ViewModelFactory private constructor(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                val repository = InjectionAuth.provideRepository(context)
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                val repository = InjectionMain.provideRepository(context)
                WelcomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                val repository = InjectionMain.provideRepository(context)
                SettingViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomePageViewModel::class.java) -> {
                val repository = InjectionMain.provideRepository(context)
                HomePageViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PesananDetailViewModel::class.java) -> {
                val repository = InjectionMain.provideRepository(context)
                PesananDetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                val repository = InjectionMain.provideRepository(context)
                HistoryViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(context).also { instance = it }
            }
    }
}
