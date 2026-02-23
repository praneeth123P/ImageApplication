package com.example.imageapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imageapp.adapter.LanguageAdapter
import com.example.imageapp.databinding.FragmentLanguageBinding
import com.example.imageapp.utils.LanguageHelper
import com.example.imageapp.utils.LanguagePrefs
import com.example.imageapp.viewmodel.LanguageViewModel

class LanguageFragment : Fragment() {

    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LanguageViewModel by viewModels()
    private lateinit var adapter: LanguageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        setupSearch()

        binding.btnDone.setOnClickListener {
            viewModel.selectedLanguage.value?.let { code ->
                viewModel.saveLanguage(code)
                requireActivity().recreate()
            }
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        adapter = LanguageAdapter { language ->
            viewModel.selectLanguage(language.code)
        }
        binding.rvLanguages.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@LanguageFragment.adapter
        }
    }

    private fun observeViewModel() {
        viewModel.filteredLanguages.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.selectedLanguage.observe(viewLifecycleOwner) {
            adapter.setSelectedCode(it)
        }
    }

    private fun setupSearch() {
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.searchLanguages(text?.toString() ?: "")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}