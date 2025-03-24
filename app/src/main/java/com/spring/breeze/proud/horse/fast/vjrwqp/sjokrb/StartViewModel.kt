package com.spring.breeze.proud.horse.fast.vjrwqp.sjokrb

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spring.breeze.proud.horse.fast.cenklaj.cesa.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StartViewModel : BaseViewModel() {
    val liveHomeData = MutableLiveData<Boolean>()
    val isRotationActive = MutableLiveData<Boolean>()

    init {
        startRotation()
    }

    private fun startRotation() {
        viewModelScope.launch {
            isRotationActive.value = true
            delay(2000)
            isRotationActive.value = false
            liveHomeData.value = true
        }
    }
}
