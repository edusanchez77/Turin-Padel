/**
 * Created by CbaElectronics by Eduardo Sanchez on 1/6/22 11:53.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.comments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ActivityCommentsBinding
import com.cbaelectronics.turinpadel.model.domain.Post
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesKey
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesProvider
import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.usecases.common.rows.CommentsRecyclerViewAdapter
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.cbaelectronics.turinpadel.util.UIUtil.pushNotification
import com.cbaelectronics.turinpadel.util.notifications.Constants
import com.itdev.nosfaltauno.util.extension.font
import com.itdev.nosfaltauno.util.extension.hideSoftInput
import com.itdev.nosfaltauno.util.extension.mediumFormat

class CommentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentsBinding
    private lateinit var viewModel: CommentsViewModel
    private lateinit var adapter: CommentsRecyclerViewAdapter
    private lateinit var postJSON: String
    private lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)

        // Content
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this).get(CommentsViewModel::class.java)

        // Adapter
        adapter = CommentsRecyclerViewAdapter(binding.root.context)

        // Data
        val bundle = intent.extras
        postJSON = bundle?.getString(DatabaseField.POST.key).toString()
        Log.d("NotificationFixedTurn CommentPostJson", postJSON)
        post = Post.fromJson(postJSON)!!

        // Localize
        localize()

        // Setup
        setup()
    }

    private fun localize() {
        Glide.with(this)
            .load(post.user.photoProfile)
            .into(binding.imageViewCommentUserAvatar)
        binding.textViewCommentMessage.text = post.message
        binding.textViewCommentUserName.text = post.user.displayName
        binding.textViewCommentPostDate.text = post.date.mediumFormat()
        binding.textViewCommentTitleComment.text = getString(viewModel.title)
        binding.textViewCommentCount.text = getString(viewModel.info)
        binding.editTextCommentWriteComment.hint = getString(viewModel.writeComment)
    }

    private fun setup() {

        // ActionBar
        supportActionBar?.hide()
        binding.toolbarComment.setNavigationOnClickListener { onBackPressed() }
        binding.toolbarComment.navigationIcon?.setTint(
            ContextCompat.getColor(
                binding.root.context,
                R.color.light
            )
        )

        // UI
        binding.textViewCommentUserName.font(
            FontSize.SUBHEAD,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.light)
        )
        binding.textViewCommentPostDate.font(
            FontSize.CAPTION,
            FontType.LIGHT,
            ContextCompat.getColor(binding.root.context, R.color.light)
        )
        binding.textViewCommentMessage.font(
            FontSize.HEAD,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.light)
        )
        binding.textViewCommentTitleComment.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )
        binding.textViewCommentCount.font(
            FontSize.SUBHEAD,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        // Data
        binding.recyclerViewComments.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewComments.adapter = adapter
        binding.recyclerViewComments.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        observeData()

        // Buttons
        buttons()
    }

    private fun observeData() {
        viewModel.load(post).observe(this, Observer {
            adapter.setDataList(it)
            adapter.notifyDataSetChanged()

            if (it.isNotEmpty()) {
                binding.textViewCommentTitleComment.visibility = View.VISIBLE
                binding.textViewCommentCount.visibility = View.GONE
            }else{
                binding.textViewCommentTitleComment.visibility = View.GONE
                binding.textViewCommentCount.visibility = View.VISIBLE
            }
        })
    }

    private fun buttons() {
        binding.buttonCommentSendComment.setOnClickListener {
            val message = binding.editTextCommentWriteComment.text.toString()
            val alertOk = getString(viewModel.alertOk)
            val alertError = getString(viewModel.alertError)
            val alertIncomplete = getString(viewModel.alertIncomplete)

            if (message.isNullOrBlank()) {
                Toast.makeText(this, alertIncomplete, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.save(post.id!!, message)
                createNotification()

                hideSoftInput()
                binding.editTextCommentWriteComment.text = null

                Toast.makeText(this, alertOk, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createNotification() {
        val title = getString(viewModel.notificationTitle)
        val body = getString(viewModel.notificationBody)
        val type = Constants.TYPE_COMMENT
        val user = PreferencesProvider.string(binding.root.context, PreferencesKey.AUTH_USER).toString()

        pushNotification(title, body, type, user, post.id, Post.toJson(post))
    }

    override fun onSupportNavigateUp(): Boolean {
        this.onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_back_in_up, R.anim.slide_back_out_up)
    }


}