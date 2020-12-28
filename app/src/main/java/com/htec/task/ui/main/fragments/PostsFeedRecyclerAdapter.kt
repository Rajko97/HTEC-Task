package com.htec.task.ui.main.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.htec.task.R
import com.htec.task.model.db.PostDBModel

class PostsFeedRecyclerAdapter : PagingDataAdapter<PostDBModel ,PostsFeedRecyclerAdapter.PostsViewHolder>(DIFF_CALLBACKS) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        return PostsViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.posts_feed_recycler_item,
                parent,
                false
            )
        )
    }

    inner class PostsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPostTitle = itemView.findViewById<TextView>(R.id.tvPostTitle)
        private val tvPostBody = itemView.findViewById<TextView>(R.id.tvPostBody)
        private val recyclerItem = itemView.findViewById<ConstraintLayout>(R.id.recyclerItem)

        fun bind(item: PostDBModel) {
            tvPostTitle.text = item.title
            tvPostBody.text = item.body
            recyclerItem.setOnClickListener {
                val action = PostsFeedFragmentDirections.actionPostsFeedFragmentToPostDetailsFragment(
                    item
                )
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        return holder.bind(getItem(position)!!)
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