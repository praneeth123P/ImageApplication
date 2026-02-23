package com.example.imageapp.data.repository

import android.content.Context
import com.example.imageapp.data.model.Category
import com.example.imageapp.data.model.Template
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository(private val context: Context) {

    suspend fun loadCategories(): List<Category> = withContext(Dispatchers.IO) {
        val jsonString = context.assets.open("for_test.json")
            .bufferedReader()
            .use { it.readText() }

        val jsonObject = Gson().fromJson(jsonString, JsonObject::class.java)
        val categories = mutableListOf<Category>()

        jsonObject.entrySet().forEach { entry ->
            val categoryName = entry.key
            val templatesArray = entry.value.asJsonObject
                .getAsJsonArray("Templates")

            val templates = templatesArray.map { templateElement ->
                val templateObj = templateElement.asJsonObject
                Template(
                    url = templateObj.get("url").asString,
                    watch_ad = templateObj.get("watch_ad").asString
                )
            }
            categories.add(Category(name = categoryName, templates = templates))
        }
        categories
    }
}