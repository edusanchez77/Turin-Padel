/**
 * Created by CbaElectronics by Eduardo Sanchez on 16/5/22 11:16.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.grandtable

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.FragmentGrandtableBinding
import com.cbaelectronics.turinpadel.model.domain.Post
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesKey
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesProvider
import com.cbaelectronics.turinpadel.usecases.comments.CommentsRouter
import com.cbaelectronics.turinpadel.usecases.common.rows.GrandtableRecyclerViewAdapter
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.cbaelectronics.turinpadel.util.UIUtil.pushNotification
import com.cbaelectronics.turinpadel.util.notifications.Constants.Companion.TYPE_POST
import com.itdev.nosfaltauno.util.extension.font

class GrandtableFragment : Fragment(), GrandtableRecyclerViewAdapter.onClickPostClickListener {

    private lateinit var _binding: FragmentGrandtableBinding
    private val binding get() = _binding!!
    private lateinit var viewModel: GrandtableViewModel
    private lateinit var adapter: GrandtableRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Content
        _binding = FragmentGrandtableBinding.inflate(inflater, container, false)

        // ViewModel
        viewModel = ViewModelProvider(this).get(GrandtableViewModel::class.java)

        // Adapter
        adapter = GrandtableRecyclerViewAdapter(binding.root.context, this)

        // Setup
        localize()
        setup()

        return binding.root
    }

    private fun localize() {

        binding.textViewGrandtableTitle.text = getString(viewModel.info)
        binding.textViewPostCount.text = getString(viewModel.comment)
        binding.editTextGrandtableWritePost.hint = getString(viewModel.write)

    }

    private fun setup() {

        // UI
        binding.textViewGrandtableTitle.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        binding.textViewPostCount.font(
            FontSize.SUBHEAD,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        // Data
        binding.recyclerViewGrandtable.layoutManager = LinearLayoutManager(binding.root.context)
        binding.recyclerViewGrandtable.adapter = adapter
        observeData()

        // Buttons
        buttons()

    }

    private fun observeData() {
        viewModel.load().observe(viewLifecycleOwner, Observer { post ->
            adapter.setDataList(post)
            adapter.notifyDataSetChanged()

            if (post.isNotEmpty()) {
                binding.textViewGrandtableTitle.visibility = View.VISIBLE
                binding.textViewPostCount.visibility = View.GONE
            }else{
                binding.textViewGrandtableTitle.visibility = View.GONE
                binding.textViewPostCount.visibility = View.VISIBLE
            }
        })
    }

    private fun buttons() {
        binding.buttonGrandtableSendPost.setOnClickListener {
            val message = binding.editTextGrandtableWritePost.text.toString()
            val alertOk = getString(viewModel.alertOk)
            val alertError = getString(viewModel.alertError)
            val alertIncomplete = getString(viewModel.alertIncomplete)

            if (message.isNullOrBlank()) {
                Toast.makeText(binding.root.context, alertIncomplete, Toast.LENGTH_SHORT).show()
            } else {
                viewModel.save(message)
                createNotification()
                hideSoftInput()
                binding.editTextGrandtableWritePost.text = null

                Toast.makeText(binding.root.context, alertOk, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createNotification() {
        val title = getString(viewModel.notificationTitle)
        val body = getString(viewModel.notificationBody)
        val type = TYPE_POST
        val user = PreferencesProvider.string(binding.root.context, PreferencesKey.AUTH_USER).toString()

        pushNotification(title, body, type, user,)
    }


    override fun onItemClick(post: Post) {
        CommentsRouter().launch(binding.root.context, post)
    }

    fun hideSoftInput() {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }


}