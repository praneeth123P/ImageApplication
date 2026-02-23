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
import com.example.imageapp.R
import com.example.imageapp.databinding.FragmentSettingsBinding
import com.example.imageapp.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Display current language on the language row
        val currentLanguage = viewModel.getCurrentLanguage()
        binding.tvLanguageValue.text = currentLanguage //change tvLanguageValue to your actual view ID

        binding.rowLanguage.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_language)
        }
        binding.rowCreations.setOnClickListener {
            findNavController().navigate(R.id.creationsFragment)
        }
        binding.rowRateUs.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=${requireContext().packageName}")))
        }

        binding.rowShareApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Check out this app!")
            }
            startActivity(Intent.createChooser(intent, "Share App"))
        }
        binding.rowPrivacyPolicy.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://yourprivacypolicyurl.com")))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}