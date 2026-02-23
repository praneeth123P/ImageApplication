package com.example.imageapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imageapp.adapter.TemplateAdapter
import com.example.imageapp.databinding.FragmentHomeBinding
import com.example.imageapp.viewmodel.HomeViewModel
import com.google.android.material.chip.Chip

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var templateAdapter: TemplateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        templateAdapter = TemplateAdapter { template ->
            val folder = extractFolder(template.url)
            val fileName = extractFileName(template.url)

            if (folder.isBlank() || fileName.isBlank()) {
                android.util.Log.e("UPLOAD_DEBUG", "Invalid folder/fileName from URL: ${template.url}")
                return@TemplateAdapter
            }

            val action = HomeFragmentDirections.actionHomeToImageSelection(
                templateUrl = template.url,
                folder = folder,
                fileName = fileName
            )
            findNavController().navigate(action)
        }
        binding.rvTemplates.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = templateAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            binding.chipGroupCategories.removeAllViews()
            categories.forEachIndexed { index, category ->
                val chip = Chip(requireContext()).apply {
                    text = category.name
                    isCheckable = true
                    isChecked = index == 0
                    setOnClickListener { viewModel.selectCategory(category) }
                }
                binding.chipGroupCategories.addView(chip)
            }
        }

        viewModel.selectedCategory.observe(viewLifecycleOwner) { category ->
            category?.let { templateAdapter.submitList(it.templates) }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    /*
      extracts folder from URL.
     "http://.../serverside_images/modelingWomen/modelingWomen_4.jpg" → "modelingWomen"
     */
    private fun extractFolder(url: String): String {
        val cleanUrl = url.substringBefore("?")
        val segments = cleanUrl.trimEnd('/').split("/")
        return segments.getOrNull(segments.size - 2)?.trim() ?: ""
    }

    /*
     extracts file name from URL.
     e.g. "http://.../modelingWomen/modelingWomen_4.jpg" → "modelingWomen_4.jpg"
     */
    private fun extractFileName(url: String): String {
        return url
            .substringAfterLast("/")
            .substringBefore("?")
            .trim()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}