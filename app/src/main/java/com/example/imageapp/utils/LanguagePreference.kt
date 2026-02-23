package com.example.imageapp.utils

import android.content.Context
import java.util.Locale

object LanguagePrefs {

    private const val PREF_NAME = "language_prefs"
    private const val KEY_LANGUAGE = "selected_language"

    fun saveLanguage(context: Context, languageCode: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, languageCode)
            .apply()
    }

    fun getLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // If user already selected → return it
        val saved = prefs.getString(KEY_LANGUAGE, null)
        if (saved != null) return saved

        // First launch → get device language
        val deviceLang = Locale.getDefault().language

        // Supported languages only
        val supported = listOf("en", "hi", "te", "ta","ka")
        val finalLang = if (supported.contains(deviceLang)) deviceLang else "en"

        saveLanguage(context, finalLang)
        return finalLang
    }
}