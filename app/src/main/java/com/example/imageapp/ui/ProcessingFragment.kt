package com.example.imageapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.imageapp.databinding.FragmentProcessingBinding
import com.example.imageapp.viewmodel.ProcessingViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.File

class ProcessingFragment : Fragment() {

    private var _binding: FragmentProcessingBinding? = null
    private val binding get() = _binding!!

    //  Correct ViewModel name
    private val viewModel: ProcessingViewModel by viewModels()

    private val args: ProcessingFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProcessingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        // Start processing
        viewModel.processImage(
            folder = args.folder,
            fileName = args.fileName,
            appName = args.appName,
            imageFile = File(args.imagePath)
        )
    }

    private fun observeViewModel() {

        viewModel.progress.observe(viewLifecycleOwner) { progress ->
            binding.circularProgress.progress = progress
        }

        viewModel.resultUrl.observe(viewLifecycleOwner) { url ->
            url?.let {
                val action =
                    ProcessingFragmentDirections.actionProcessingToResult(resultUrl = it)
                findNavController().navigate(action)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}