package com.htec.task.ui.main.fragments

import android.graphics.text.LineBreaker
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.htec.task.R
import com.htec.task.ui.main.data.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_post_details.view.*

class PostDetailsFragment : Fragment() {
    private lateinit var viewModel : MainViewModel
    private val args by navArgs<PostDetailsFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_post_details, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        v.tvPostTitle.text = args.post.title
        v.tvPostTitle.text = args.post.title
        v.tvPostBody.apply {
            text = args.post.body
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
            }
        }

        CompositeDisposable().add(
            viewModel.fetchAuthorData(args.post.ownerId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({response ->
                    v.tvName.text = getString(R.string.by_author, response.fullName)
                    v.tvEmail.text = response.email
                }, {t ->
                    v.tvName.text = getString(R.string.unknown_author)
                }))

        activity?.invalidateOptionsMenu()
        setHasOptionsMenu(true)
        return v
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_item_delete -> deleteThisPost()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteThisPost() {
        viewModel.removePost(args.post)
        findNavController().popBackStack()
        Toast.makeText(requireContext(), getString(R.string.post_deleted_successfully), Toast.LENGTH_SHORT).show()
    }
}