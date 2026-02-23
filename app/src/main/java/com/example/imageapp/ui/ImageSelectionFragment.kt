package com.example.imageapp.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.imageapp.databinding.FragmentImageSelectionBinding
import java.io.File
import java.io.FileOutputStream

class ImageSelectionFragment : Fragment() {

    private var _binding: FragmentImageSelectionBinding? = null
    private val binding get() = _binding!!
    private val args: ImageSelectionFragmentArgs by navArgs()

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                val filePath = copyUriToCache(uri)
                filePath?.let { navigateToProcessing(it) }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this).load(args.templateUrl).into(binding.ivTemplate)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.btnChooseImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            pickImageLauncher.launch(intent)
        }
    }

    private fun navigateToProcessing(imagePath: String) {
        android.util.Log.d("DEBUG", "imagePath: $imagePath")
        android.util.Log.d("DEBUG", "folder: ${args.folder}")
        android.util.Log.d("DEBUG", "fileName: ${args.fileName}")
        android.util.Log.d("DEBUG", "appName: ${args.appName}")

        val action = ImageSelectionFragmentDirections.actionImageSelectionToProcessing(
            imagePath = imagePath,
            folder = args.folder,
            fileName = args.fileName,
            appName = args.appName
        )
        findNavController().navigate(action)
    }

    /*
     Copies selected image URI to app cache dir so we have a real File for upload.
     */
    private fun copyUriToCache(uri: Uri): String? {
        return try {
            val fileName = getOriginalFileName(uri) ?: "img_${System.currentTimeMillis()}.jpg.jpeg.png"
            val file = File(requireContext().cacheDir, fileName)
            requireContext().contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output -> input.copyTo(output) }
            }
            file.absolutePath
        } catch (e: Exception) {
            android.util.Log.e("ImageSelection", "Failed to copy URI to cache: ${e.message}")
            null
        }
    }

    private fun getOriginalFileName(uri: Uri): String? {
        var name: String? = null
        requireContext().contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val col = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && col >= 0) name = cursor.getString(col)
        }
        return name
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}