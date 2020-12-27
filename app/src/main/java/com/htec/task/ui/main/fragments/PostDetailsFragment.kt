package com.htec.task.ui.main.fragments

import android.graphics.text.LineBreaker
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.htec.task.R
import com.htec.task.repository.retrofit.ResultWrapper.Success
import com.htec.task.ui.main.data.MainViewModel
import kotlinx.android.synthetic.main.fragment_post_details.view.*

const val KEY_IS_DIALOG_SHOWING = "isDialogShowing"

class PostDetailsFragment : Fragment() {
    private val args by navArgs<PostDetailsFragmentArgs>()
    private lateinit var viewModel : MainViewModel
    private var confirmDialog : AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_post_details, container, false)

        if(savedInstanceState?.getBoolean(KEY_IS_DIALOG_SHOWING) == true)
            showConfirmationDialog()

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        v.contentLoadingProgressBar.show()
        viewModel.authorData(args.post.ownerId).observe(viewLifecycleOwner, { response ->
            v.tvPostTitle.text = args.post.title
            v.tvPostBody.apply {
                text = args.post.body
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
                }
            }
            when (response) {
                is Success -> {
                    v.tvName.text = getString(R.string.by_author, response.value.fullName)
                    v.tvEmail.text = response.value.email
                }
                else -> {
                    v.tvName.text = getString(R.string.failed_to_load_author)
                }
            }
            v.contentLoadingProgressBar.hide()
        })
        activity?.invalidateOptionsMenu()
        setHasOptionsMenu(true)
        return v
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(KEY_IS_DIALOG_SHOWING, confirmDialog?.isShowing ?: false)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.cancelFetchingAuthorData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_item_delete -> showConfirmationDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showConfirmationDialog() {
        confirmDialog?.show() ?: createDialog().also { confirmDialog = it }.show()
    }

    private fun createDialog(): AlertDialog = MaterialAlertDialogBuilder(requireContext())
        .setTitle(getString(R.string.dialog_delete_post_title))
        .setMessage(getString(R.string.dialog_delete_post_message))
        .setPositiveButton(getString(R.string.dialog_options_yes)) { _, _ ->
            viewModel.removePost(args.post)
            findNavController().popBackStack()
            Toast.makeText(requireContext(), getString(R.string.post_deleted_successfully), Toast.LENGTH_SHORT).show()
        }
        .setNegativeButton(getString(R.string.dialog_option_no)) { _, _ -> }
        .create()
}