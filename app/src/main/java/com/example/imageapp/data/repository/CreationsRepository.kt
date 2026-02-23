package com.example.imageapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.imageapp.data.model.Creation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CreationsRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("CreationsPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val KEY = "saved_creations"

    fun getAllCreations(): List<Creation> {
        val json = prefs.getString(KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<Creation>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun saveCreation(creation: Creation) {
        val current = getAllCreations().toMutableList()
        current.add(0, creation) // Add newest first
        prefs.edit().putString(KEY, gson.toJson(current)).apply()
    }

    fun deleteCreation(id: String) {
        val current = getAllCreations().toMutableList()
        current.removeAll { it.id == id }
        prefs.edit().putString(KEY, gson.toJson(current)).apply()
    }
}
