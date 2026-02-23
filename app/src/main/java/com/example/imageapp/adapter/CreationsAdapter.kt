package com.example.imageapp.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imageapp.data.model.Creation
import com.example.imageapp.databinding.ItemCreationBinding


class CreationsAdapter(
    private val onDelete: (Creation) -> Unit,
    private val onShare: (Creation) -> Unit
) : ListAdapter<Creation, CreationsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCreationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class ViewHolder(private val binding: ItemCreationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(creation: Creation) {
            Glide.with(binding.root.context)
                .load(Uri.parse(creation.imagePath))
                .centerCrop()
                .into(binding.ivCreation)
            binding.btnDelete.setOnClickListener { onDelete(creation) }
            binding.btnShare.setOnClickListener { onShare(creation) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Creation>() {
            override fun areItemsTheSame(o: Creation, n: Creation) = o.id == n.id
            override fun areContentsTheSame(o: Creation, n: Creation) = o == n
        }
    }
}