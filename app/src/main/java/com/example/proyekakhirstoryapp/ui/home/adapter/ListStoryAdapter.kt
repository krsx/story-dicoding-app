package com.example.proyekakhirstoryapp.ui.home.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyekakhirstoryapp.R
import com.example.proyekakhirstoryapp.data.api.response.ListStoryItem
import com.example.proyekakhirstoryapp.databinding.ItemRowStoryBinding
import com.example.proyekakhirstoryapp.ui.detailstory.DetailStoryActivity

class ListStoryAdapter(private val listStory: List<ListStoryItem?>) :
    RecyclerView.Adapter<ListStoryAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root)

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listStory.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (listStory[position] != null) {
            Glide.with(holder.itemView.context).load(listStory[position]!!.photoUrl)
                .into(holder.binding.ivItemPhoto)
            holder.binding.tvItemName.text = listStory[position]!!.name
            holder.binding.tvItemDesc.text = listStory[position]!!.description
            holder.binding.tvCreateDesc.text = holder.itemView.context.resources.getString(R.string.story_created_at, listStory[position]!!.createdAt!!.subSequence(0, 9))
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listStory[position])
            val intentToDetailStory =
                Intent(holder.itemView.context, DetailStoryActivity::class.java)
            intentToDetailStory.putExtra(KEY_ID, listStory[position]?.id)
            holder.itemView.context.startActivity(intentToDetailStory)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(stories: ListStoryItem?)
    }

    companion object{
        const val KEY_ID = "key_id"
    }
}