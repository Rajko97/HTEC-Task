package com.htec.task.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.*
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.htec.task.R
import com.htec.task.ui.main.data.MainViewModel
import kotlinx.android.synthetic.main.fragment_posts_feed.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter


class PostsFeedFragment : Fragment() {
    private lateinit var viewModel : MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_posts_feed, container, false)

        v.contentLoadingProgressBar.show()

        val adapter = PostsFeedRecyclerAdapter()
        v.recyclerPostsList.apply {
            with(LinearLayoutManager(context)) {
                layoutManager = this
                addItemDecoration(DividerItemDecoration(context, this.orientation))
            }
            this.adapter = adapter
        }

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        viewModel.readAllData.observe(viewLifecycleOwner) {
            v.contentLoadingProgressBar.hide()
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
            CoroutineScope(Main).launch {
                withContext(Main) {
                    v.tvEmptyListError.visibility = if(adapter.itemCount == 0) View.VISIBLE else View.GONE
                }
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading}
                .collect {
                    val list = adapter.snapshot()
                    withContext(Main) {
                        v.tvEmptyListError.visibility =
                                if (/*adapter.itemCount == 0*/ list.size == 0) View.VISIBLE else View.GONE
                    }
                }
        }

        v.swipeRefresh.setOnRefreshListener {
            viewModel.fetchByClient().observe(viewLifecycleOwner, { data ->
                val resId = if(data) R.string.success_refreshed_posts_list else R.string.failed_to_refresh_list
                Toast.makeText(requireContext(), getString(resId), Toast.LENGTH_SHORT).show()
                v.swipeRefresh.isRefreshing = false
            })
        }

        activity?.invalidateOptionsMenu()
        return v
    }
}