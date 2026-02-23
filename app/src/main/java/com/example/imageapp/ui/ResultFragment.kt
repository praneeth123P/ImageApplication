package com.example.imageapp.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.imageapp.R
import com.example.imageapp.databinding.FragmentResultBinding
import com.example.imageapp.utils.ImageFormat
import com.example.imageapp.viewmodel.ResultViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ResultViewModel by viewModels()
    private val args: ResultFragmentArgs by navArgs()

    private var loadedBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.isEnabled = false
        binding.btnShare.isEnabled = false

        loadImageManually(args.resultUrl)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnHome.setOnClickListener { findNavController().navigate(R.id.homeFragment) }

//        binding.btnSave.setOnClickListener {
//            loadedBitmap?.let { viewModel.saveImageToGallery(it) }
//                ?: Snackbar.make(binding.root, "Image not loaded", Snackbar.LENGTH_SHORT).show()
//        }
        binding.btnSave.setOnClickListener {
            val bitmap = getBitmap()
            if (bitmap != null) {
                viewModel.saveImageToGallery(bitmap, ImageFormat.PNG)
            } else {
                Snackbar.make(requireView(), "Image not ready yet", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.btnShare.setOnClickListener { shareImage() }

        observeViewModel()
    }

    private fun getBitmap(): Bitmap? =
        (binding.ivResult.drawable as? BitmapDrawable)?.bitmap

    private fun loadImageManually(urlStr: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL(urlStr)
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 15_000
                connection.readTimeout = 15_000
                connection.doInput = true
                connection.connect()

                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                if (bitmap == null) throw Exception("Bitmap decode failed")

                withContext(Dispatchers.Main) {
                    loadedBitmap = bitmap
                    binding.ivResult.setImageBitmap(bitmap)
                    binding.btnSave.isEnabled = true
                    binding.btnShare.isEnabled = true
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Snackbar.make(
                        binding.root,
                        "Failed to load image from server",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun shareImage(uri: Uri? = null) {
        val bitmap = loadedBitmap ?: return

        val file = File(requireContext().cacheDir, "share_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, it)
        }

        val shareUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, shareUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Share Image"))
    }

    private fun observeViewModel() {
        viewModel.saveSuccess.observe(viewLifecycleOwner) {
            Snackbar.make(
                binding.root,
                if (it) "Saved to Gallery!" else "Save failed",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}