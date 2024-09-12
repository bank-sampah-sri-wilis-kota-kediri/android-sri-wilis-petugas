package com.bs.sriwilispetugas.ui.authorization

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilispetugas.helper.Result
import com.bs.sriwilispetugas.data.repository.AuthRepository
import com.bs.sriwilispetugas.data.room.LoginResponseEntity
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<Result<LoginResponseEntity>>()
    val loginResult: LiveData<Result<LoginResponseEntity>> = _loginResult

    fun login(phone: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = Result.Loading
            val result = repository.login(phone, password)
            _loginResult.value = result
        }
    }

}
