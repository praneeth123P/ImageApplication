package com.example.imageapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.imageapp.databinding.ItemLanguageBinding
import com.example.imageapp.viewmodel.Language


class LanguageAdapter(
    private val onSelect: (Language) -> Unit
) : ListAdapter<Language, LanguageAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var selectedCode: String = "en"

    fun setSelectedCode(code: String) {
        val old = currentList.indexOfFirst { it.code == selectedCode }
        val new = currentList.indexOfFirst { it.code == code }
        selectedCode = code
        if (old >= 0) notifyItemChanged(old)
        if (new >= 0) notifyItemChanged(new)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLanguageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class ViewHolder(private val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(language: Language) {
            binding.tvFlag.text = language.flagEmoji
            binding.tvLanguageName.text = language.name
            binding.radioButton.isChecked = selectedCode == language.code
            binding.root.setOnClickListener { onSelect(language) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Language>() {
            override fun areItemsTheSame(o: Language, n: Language) = o.code == n.code
            override fun areContentsTheSame(o: Language, n: Language) = o == n
        }
    }
}