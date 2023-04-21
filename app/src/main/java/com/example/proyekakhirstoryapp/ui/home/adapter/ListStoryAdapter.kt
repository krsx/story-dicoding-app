package com.example.proyekakhirstoryapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyekakhirstoryapp.data.api.response.ListStoryItem
import com.example.proyekakhirstoryapp.databinding.ItemRowStoryBinding

class ListStoryAdapter(private val listStory: List<ListStoryItem?>) :
    RecyclerView.Adapter<ListStoryAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listStory.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (listStory[position] != null) {
            Glide.with(holder.itemView.context).load(listStory[position]!!.photoUrl)
                .into(holder.binding.storyImage)
            holder.binding.storyName.text = listStory[position]!!.name
            holder.binding.storyDesc.text = listStory[position]!!.description
            holder.binding.storyCreateDesc.text = listStory[position]!!.createdAt!!.subSequence(0, 9)
        }

    }
}