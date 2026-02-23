package com.example.imageapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imageapp.R
import com.example.imageapp.adapter.CreationsAdapter
import com.example.imageapp.databinding.FragmentCreationsBinding
import com.example.imageapp.viewmodel.CreationsViewModel

class CreationsFragment : Fragment() {

    private var _binding: FragmentCreationsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreationsViewModel by viewModels()
    private lateinit var adapter: CreationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CreationsAdapter(
            onDelete = { creation -> viewModel.deleteCreation(creation) },
            onShare = { creation -> shareCreation(creation.imagePath) }
        )

        binding.rvCreations.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@CreationsFragment.adapter
        }

        binding.btnStartCreating.setOnClickListener {
            findNavController().navigate(R.id.imageSelectionFragment)
        }

        viewModel.creations.observe(viewLifecycleOwner) { creations ->
            adapter.submitList(creations)
            val isEmpty = creations.isEmpty()
            binding.layoutEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
            binding.rvCreations.visibility = if (isEmpty) View.GONE else View.VISIBLE
        }
    }

    private fun shareCreation(imagePath: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, Uri.parse(imagePath))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Share via"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}