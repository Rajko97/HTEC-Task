package com.htec.task.ui.main.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.htec.task.databinding.PostsFeedRecyclerItemBinding
import com.htec.task.model.db.PostDBModel

class PostsFeedRecyclerAdapter : PagingDataAdapter<PostDBModel ,PostsFeedRecyclerAdapter.PostsViewHolder>(DIFF_CALLBACKS) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val binding = PostsFeedRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostsViewHolder(binding)
    }

    inner class PostsViewHolder(private val binding: PostsFeedRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostDBModel) {
            binding.tvPostTitle.text = item.title
            binding.tvPostBody.text = item.body
            binding.recyclerItem.setOnClickListener {
                val action = PostsFeedFragmentDirections.actionPostsFeedFragmentToPostDetailsFragment(item)
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val currentItem = getItem(position)

        if(currentItem != null)
            holder.bind(currentItem)
    }

    companion object {
        private val DIFF_CALLBACKS = object : DiffUtil.ItemCallback<PostDBModel>() {

            override fun areItemsTheSame(oldItem: PostDBModel, newItem: PostDBModel): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: PostDBModel, newItem: PostDBModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}