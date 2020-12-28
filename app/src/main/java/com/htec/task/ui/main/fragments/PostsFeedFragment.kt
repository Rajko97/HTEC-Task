package com.htec.task.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.htec.task.R
import com.htec.task.ui.main.data.MainViewModel
import kotlinx.android.synthetic.main.fragment_posts_feed.view.*

class PostsFeedFragment : Fragment() {
    private lateinit var viewModel : MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_posts_feed, container, false)

        val adapter = PostsFeedRecyclerAdapter()
        v.recyclerPostsList.apply {
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            addItemDecoration(DividerItemDecoration(context, lm.orientation))
            this.adapter = adapter
        }

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        viewModel.readAllData.observe(viewLifecycleOwner, Observer { postsList ->
            adapter.setData(postsList)
        })

        val swipeRefreshLayout = v.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchPostList()
            Toast.makeText(requireContext(), getString(R.string.success_refreshed_posts_list), Toast.LENGTH_SHORT).show()
            swipeRefreshLayout.isRefreshing = false
        }
        activity?.invalidateOptionsMenu()
        return v
    }
}