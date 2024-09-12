package com.bs.sriwilispetugas.ui.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilispetugas.data.repository.MainRepository
import com.bs.sriwilispetugas.data.repository.modelhelper.CardDetailPesanan
import com.bs.sriwilispetugas.data.repository.modelhelper.CardPesanan
import com.bs.sriwilispetugas.data.response.ChangeStatusPesananSampahResponse
import com.bs.sriwilispetugas.helper.Result
import kotlinx.coroutines.launch

class PesananDetailViewModel(private val repository: MainRepository): ViewModel() {
    private val _pesanans = MutableLiveData<Result<CardPesanan>>()
    val pesanans: LiveData<Result<CardPesanan>> get() = _pesanans

    private val _pesananSampah = MutableLiveData<Result<List<CardDetailPesanan>>>()
    val pesananSampah: LiveData<Result<List<CardDetailPesanan>>> get() = _pesananSampah

    private val _changeStatus = MutableLiveData<Result<ChangeStatusPesananSampahResponse>>()
    val changeStatus: LiveData<Result<ChangeStatusPesananSampahResponse>> get() = _changeStatus


    fun getDataDetailPesananSampahKeranjang(idPesanan: String) {
        viewModelScope.launch {
            _pesanans.postValue(Result.Loading)
            val result = repository.getDataDetailPesananSampahKeranjang(idPesanan)
            _pesanans.postValue(result)
        }
    }

    fun getPesananSampah(idPesanan: String) {
        viewModelScope.launch {
            _pesananSampah.postValue(Result.Loading)
            val result = repository.getPesananSampah(idPesanan)
            _pesananSampah.postValue(result)
        }
    }

    fun changeStatusPesananSampahResponse(idPesanan: String, status: String) {
        viewModelScope.launch {
            _changeStatus.postValue(Result.Loading)
            val result = repository.changeStatusPesananSampahResponse(idPesanan, status)
            _changeStatus.postValue(result)
        }
    }
}