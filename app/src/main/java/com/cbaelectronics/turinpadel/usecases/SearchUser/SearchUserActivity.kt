/**
 * Created by CbaElectronics by Eduardo Sanchez on 20/9/22 16:33.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.SearchUser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ActivitySearchUserBinding
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.usecases.common.rows.UsersRecyclerViewAdapter
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.cbaelectronics.turinpadel.util.UIUtil
import com.itdev.nosfaltauno.util.extension.addClose
import com.itdev.nosfaltauno.util.extension.font

class SearchUserActivity : AppCompatActivity(), UsersRecyclerViewAdapter.onClickUserListener {


    private lateinit var binding: ActivitySearchUserBinding
    private lateinit var viewModel: SearchUserViewModel
    private lateinit var adapter: UsersRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchUserBinding.inflate(layoutInflater)

        // Content
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this).get(SearchUserViewModel::class.java)

        // Adapter
        adapter = UsersRecyclerViewAdapter(this, this)

        // Setup
        localize()
        setup()

    }

    private fun localize() {
        binding.textViewSearchUserInfo.text = getString(viewModel.title)
        binding.searchViewSearchUser.queryHint = getString(viewModel.search)
    }

    private fun setup() {
        addClose(this)

        // UI
        binding.textViewSearchUserInfo.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        binding.recyclerViewSearchUser.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSearchUser.adapter = adapter

        setupSearch()
        observeData()
    }

    private fun setupSearch() {

        binding.searchViewSearchUser.setOnQueryTextListener(object :
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

    private fun observeData() {
        viewModel.searchUser().observe(this, Observer {
            adapter.setDataList(it)
            adapter.notifyDataSetChanged()
        })
    }


    override fun onItemClick(user: User) {
        val message = viewModel.messageQuestion(this, user.displayName!!)
        val positive = getString(viewModel.positive)
        val negative = getString(viewModel.negative)

        UIUtil.showAlert(
            context = this,
            message = message,
            positive = positive,
            positiveAction = {
                val intent = Intent()
                intent.putExtra(DatabaseField.EMAIL.key, user.email)
                intent.putExtra(DatabaseField.DISPLAY_NAME.key, user.displayName)
                intent.putExtra(DatabaseField.PROFILE_IMAGE_URL.key, user.photoProfile)
                intent.putExtra(DatabaseField.TOKEN.key, user.token)
                intent.putExtra(DatabaseField.REGISTER_DATE.key, user.registerDate.toString())
                intent.putExtra(DatabaseField.TYPE.key, user.type.toString())
                intent.putExtra(DatabaseField.PROFILE_CATEGORY.key, user.settings?.category)
                intent.putExtra(DatabaseField.PROFILE_POSITION.key, user.settings?.position)
                setResult(RESULT_OK, intent)
                Log.d("CATEGORY", user.settings?.category.toString())
                finish()
            },
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