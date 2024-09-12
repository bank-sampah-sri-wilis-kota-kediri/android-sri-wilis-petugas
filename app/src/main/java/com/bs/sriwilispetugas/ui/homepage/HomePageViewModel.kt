package com.bs.sriwilispetugas.ui.homepage

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilispetugas.data.repository.MainRepository
import com.bs.sriwilispetugas.data.repository.modelhelper.CardPesanan
import com.bs.sriwilispetugas.data.response.ChangeStatusPesananSampahResponse
import kotlinx.coroutines.launch
import com.bs.sriwilispetugas.helper.Result

class HomePageViewModel(private val repository: MainRepository): ViewModel() {
    private val _pesanans = MutableLiveData<Result<List<CardPesanan>>>()
    val pesanans: LiveData<Result<List<CardPesanan>>> get() = _pesanans

    private val _changeStatus = MutableLiveData<Result<ChangeStatusPesananSampahResponse>>()
    val changeStatus: LiveData<Result<ChangeStatusPesananSampahResponse>> get() = _changeStatus

    fun getCombinedPesananData() {
        viewModelScope.launch {
            _pesanans.postValue(Result.Loading)
            val result = repository.getCombinedPesananData()
            _pesanans.postValue(result)
        }
    }

    fun changeStatusPesananSampahResponse(idPesanan: String, status: String) {
        viewModelScope.launch {
            _changeStatus.postValue(Result.Loading)
            val result = repository.changeStatusPesananSampahResponse(idPesanan, status)
            _changeStatus.postValue(result)
        }
    }

    suspend fun syncData(): Result<Unit> {
        return repository.syncData()
    }

}