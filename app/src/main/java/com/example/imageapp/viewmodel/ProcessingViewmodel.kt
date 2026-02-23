package com.example.imageapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imageapp.data.repository.ImageProcessingRepository
import com.example.imageapp.data.repository.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class ProcessingViewModel : ViewModel() {

    private val repository = ImageProcessingRepository()

    private val _resultUrl = MutableLiveData<String?>()
    val resultUrl: LiveData<String?> = _resultUrl

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    fun processImage(folder: String, fileName: String, appName: String,imageFile: File) {
        viewModelScope.launch {
            _progress.value = 0

            // Animate progress while API runs
            val progressJob = launch {
                var p = 0
                while (p < 85) {
                    delay(700)
                    p += (3..7).random()
                    if (p > 85) p = 85
                    _progress.value = p
                }
            }

            when (val result = repository.uploadImage(folder, fileName, appName,imageFile)) {
                is Result.Success -> {
                    // Wait 10 seconds before polling result (as per API doc)
                    delay(10_000)
                    progressJob.cancel()
                    _progress.value = 100
                    _resultUrl.value = repository.getResultUrl(result.data.request_id)
                }
                is Result.Error -> {
                    progressJob.cancel()
                    _error.value = result.message
                }
            }
        }
    }
}