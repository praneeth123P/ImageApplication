package com.example.imageapp.data.network

import com.example.imageapp.data.model.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    //POST
    @Multipart
    @POST("upload/{folder}/{fileName}")
    suspend fun uploadImage(
        @Path("folder") folder: String,
        @Path("fileName") fileName: String,
        @Query("app_name") appName: String = "NaturePhotoFramesandEditor",
        @Header("accept") accept: String = "application/json",
        @Header("fcmtoken") fcmToken: String = "null",
        @Header("x-firebase-appcheck") firebaseAppCheck: String = "null",
        @Part sourceImage: MultipartBody.Part
    ): Response<UploadResponse>
}