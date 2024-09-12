package com.bs.sriwilispetugas.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bs.sriwilispetugas.data.repository.MainRepository
import com.bs.sriwilispetugas.data.repository.modelhelper.CardPesanan
import com.bs.sriwilispetugas.helper.Result
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: MainRepository): ViewModel() {
    private val _historyPesanans = MutableLiveData<Result<List<CardPesanan>>>()
    val historyPesanans: LiveData<Result<List<CardPesanan>>> get() = _historyPesanans

    fun getCombinedPesananData() {
        viewModelScope.launch {
            _historyPesanans.postValue(Result.Loading)
            val result = repository.getCombinedPesananData()
            _historyPesanans.postValue(result)
        }
    }

    suspend fun syncData(): Result<Unit> {
        return repository.syncData()
    }
}