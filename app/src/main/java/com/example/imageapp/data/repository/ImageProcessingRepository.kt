package com.example.imageapp.data.repository

import com.example.imageapp.data.model.UploadResponse
import com.example.imageapp.data.network.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}

class ImageProcessingRepository {

    suspend fun uploadImage(
        folder: String,
        fileName: String,
        appName: String,
        imageFile: File
    ): Result<UploadResponse> {
        return try {
            val requestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())

            val multipartBody = MultipartBody.Part.createFormData(
                "sourceImage", imageFile.name, requestBody
            )

            val response = RetrofitClient.apiService.uploadImage(
                folder = folder,
                fileName = fileName,
                appName = "NaturePhotoFramesandEditor",
                sourceImage = multipartBody
            )

            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                Result.Error("Upload failed [${response.code()}]: $errorMsg")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message ?: "Unknown error occurred"}")
        }
    }

    fun getResultUrl(requestId: Double): String {
        return RetrofitClient.getResultUrl(requestId)
    }
}