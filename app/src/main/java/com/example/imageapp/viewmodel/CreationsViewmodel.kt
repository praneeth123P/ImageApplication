package com.example.imageapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.imageapp.data.model.Creation
import com.example.imageapp.data.repository.CreationsRepository

class CreationsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CreationsRepository(application)

    private val _creations = MutableLiveData<List<Creation>>()
    val creations: LiveData<List<Creation>> = _creations

    init {
        loadCreations()
    }

    fun loadCreations() {
        _creations.value = repository.getAllCreations()
    }

    fun deleteCreation(creation: Creation) {
        repository.deleteCreation(creation.id)
        loadCreations() // Refresh list
    }
}