/**
 * Created by CbaElectronics by Eduardo Sanchez on 14/6/22 10:10.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.makeAdmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ActivityMakeAdminBinding
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.usecases.common.rows.UsersRecyclerViewAdapter
import com.cbaelectronics.turinpadel.util.Constants.CREATOR
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.cbaelectronics.turinpadel.util.UIUtil
import com.itdev.nosfaltauno.util.extension.addClose
import com.itdev.nosfaltauno.util.extension.font

class MakeAdminActivity : AppCompatActivity(), UsersRecyclerViewAdapter.onClickUserListener {

    private lateinit var binding: ActivityMakeAdminBinding
    private lateinit var viewModel: MakeAdminViewModel
    private lateinit var adapter: UsersRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeAdminBinding.inflate(layoutInflater)

        // Content
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this)[MakeAdminViewModel::class.java]

        // Adapter
        adapter = UsersRecyclerViewAdapter(this, this)

        // Setup
        localize()
        setup()
    }

    private fun localize() {

        binding.textViewMakeAdminInfo.text = getString(viewModel.title)
        binding.searchViewMakeAdmin.queryHint = getString(viewModel.search)

    }

    private fun setup() {

        addClose(this)

        // UI
        binding.textViewMakeAdminInfo.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        binding.recyclerViewMakeAdmin.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMakeAdmin.adapter = adapter

        setupSearch()
        observeData()

    }

    private fun observeData() {
        viewModel.searchUser().observe(this, Observer {
            adapter.setDataList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun setupSearch() {

        binding.searchViewMakeAdmin.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                adapter.filter.filter(query)
                adapter.notifyDataSetChanged()
                return true
            }
        })


    }

    private fun update(user: User) {

        viewModel.updateUser(user, CREATOR)

        val title = getString(viewModel.alertTitle)
        val message = getString(viewModel.reminder)
        val ok = getString(viewModel.ok)
        UIUtil.showAlert(
            context = this,
            title = title,
            message = message,
            positive = ok
        )
    }


    override fun onItemClick(user: User) {
        val message = viewModel.messageQuestion(this, user.displayName!!)
        val positive = getString(viewModel.positive)
        val negative = getString(viewModel.negative)

        UIUtil.showAlert(
            context = this,
            message = message,
            positive = positive,
            positiveAction = { update(user) },
            negative = negative
        )
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