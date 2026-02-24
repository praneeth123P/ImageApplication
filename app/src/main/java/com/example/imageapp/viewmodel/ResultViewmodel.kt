package com.example.imageapp.viewmodel

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.imageapp.data.model.Creation
import com.example.imageapp.data.repository.CreationsRepository
import com.example.imageapp.utils.ImageFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ResultViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CreationsRepository(application)

    private val _saveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = _saveSuccess

    private val _savedUri = MutableLiveData<Uri?>()
    val savedUri: LiveData<Uri?> = _savedUri

    // MAIN METHOD WITH FORMAT SUPPORT
    fun saveImageToGallery(
        bitmap: Bitmap,
        format: ImageFormat = ImageFormat.JPEG
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val context = getApplication<Application>()

                val (extension, mimeType, compressFormat) = when (format) {
                    ImageFormat.JPG, ImageFormat.JPEG -> Triple(
                        "jpg",
                        "image/jpeg",
                        Bitmap.CompressFormat.JPEG
                    )
                    ImageFormat.PNG -> Triple(
                        "png",
                        "image/png",
                        Bitmap.CompressFormat.PNG
                    )
                    ImageFormat.WEBP -> Triple(
                        "webp",
                        "image/webp",
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                            Bitmap.CompressFormat.WEBP_LOSSY
                        else
                            Bitmap.CompressFormat.WEBP
                    )
                }

                val fileName = "PhotoStyle_${System.currentTimeMillis()}.$extension"
                var savedUri: Uri? = null

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    val contentValues = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                        put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                        put(
                            MediaStore.Images.Media.RELATIVE_PATH,
                            Environment.DIRECTORY_PICTURES + "/PhotoStyle"
                        )
                    }

                    savedUri = context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    )

                    savedUri?.let { uri ->
                        context.contentResolver.openOutputStream(uri)?.use { out ->
                            bitmap.compress(compressFormat, 95, out)
                        }
                    }

                } else {

                    val dir = File(
                        Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES
                        ),
                        "PhotoStyle"
                    )
                    if (!dir.exists()) dir.mkdirs()

                    val file = File(dir, fileName)
                    FileOutputStream(file).use { out ->
                        bitmap.compress(compressFormat, 95, out)
                    }
                    savedUri = Uri.fromFile(file)
                }

                // Save to local creations
                savedUri?.let {
                    repository.saveCreation(Creation(imagePath = it.toString()))
                }

                withContext(Dispatchers.Main) {
                    _savedUri.value = savedUri
                    _saveSuccess.value = true
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _saveSuccess.value = false
                }
            }
        }
    }
}