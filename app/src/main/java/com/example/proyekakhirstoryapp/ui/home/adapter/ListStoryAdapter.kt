package com.example.proyekakhirstoryapp.ui.home.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyekakhirstoryapp.R
import com.example.proyekakhirstoryapp.data.db.model.StoryModel
import com.example.proyekakhirstoryapp.databinding.ItemRowStoryBinding
import com.example.proyekakhirstoryapp.ui.detailstory.DetailStoryActivity
import com.example.proyekakhirstoryapp.utils.DiffUtilCallback

class ListStoryAdapter :
    PagingDataAdapter<StoryModel, ListStoryAdapter.ViewHolder>(DiffUtilCallback()) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(val binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)

        holder.apply {
            Glide.with(holder.itemView.context).load(story?.image).into(holder.binding.ivItemPhoto)
            binding.tvItemName.text = story?.name
            binding.tvItemDesc.text = story?.description
            binding.tvCreateDesc.text = itemView.context.resources.getString(
                R.string.story_created_at,
                story?.createdAt?.subSequence(0, 9)
            )

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(story)
                val intentToDetailStory = Intent(itemView.context, DetailStoryActivity::class.java)
                intentToDetailStory.putExtra(KEY_ID, story?.id)
                holder.itemView.context.startActivity(intentToDetailStory)
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(stories: StoryModel?)
    }

    companion object {
        const val KEY_ID = "key_id"
    }
}