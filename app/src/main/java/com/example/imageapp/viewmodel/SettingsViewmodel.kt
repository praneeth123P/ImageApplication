package com.example.imageapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    fun getCurrentLanguage(): String = prefs.getString("language", "en") ?: "en"
}