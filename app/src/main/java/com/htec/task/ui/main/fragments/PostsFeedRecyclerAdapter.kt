package com.htec.task.ui.main.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.htec.task.R
import com.htec.task.model.db.PostDBModel

class PostsFeedRecyclerAdapter : RecyclerView.Adapter<PostsFeedRecyclerAdapter.PostsViewHolder>() {
    private var postsList = emptyList<PostDBModel>()

    fun setData(newData : List<PostDBModel>) {
        postsList = newData
        notifyDataSetChanged()
    }

    class PostsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val tvPostTitle = itemView.findViewById<TextView>(R.id.tvPostTitle)
        private val tvPostBody = itemView.findViewById<TextView>(R.id.tvPostBody)
        private val recyclerItem = itemView.findViewById<ConstraintLayout>(R.id.recyclerItem)

        fun bind(item : PostDBModel) {
            tvPostTitle.text = item.title
            tvPostBody.text = item.body
            recyclerItem.setOnClickListener {
                val action = PostsFeedFragmentDirections.actionPostsFeedFragmentToPostDetailsFragment(item)
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        return PostsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.posts_feed_recycler_item, parent,false))
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        holder.bind(postsList[position])
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

}