package com.example.imageapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imageapp.R
import com.example.imageapp.data.model.Template
import com.example.imageapp.databinding.ItemTemplateBinding

class TemplateAdapter(
    private val onTemplateClick: (Template) -> Unit
) : ListAdapter<Template, TemplateAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTemplateBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ViewHolder(private val binding: ItemTemplateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(template: Template, position: Int) {
            Glide.with(binding.root.context)
                .load(template.url)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.ivTemplate)

            binding.tvLabel.text = "Image ${position + 1}"
            binding.root.setOnClickListener { onTemplateClick(template) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Template>() {
            override fun areItemsTheSame(o: Template, n: Template) = o.url == n.url
            override fun areContentsTheSame(o: Template, n: Template) = o == n
        }
    }
}
