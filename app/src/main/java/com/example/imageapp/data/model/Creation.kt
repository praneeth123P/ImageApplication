package com.example.imageapp.data.model

data class Creation(
    val id: String = System.currentTimeMillis().toString(),
    val imagePath: String,
    val createdAt: Long = System.currentTimeMillis()
)