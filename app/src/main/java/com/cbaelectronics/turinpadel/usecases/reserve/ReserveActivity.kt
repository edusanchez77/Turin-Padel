/**
 * Created by CbaElectronics by Eduardo Sanchez on 14/6/22 10:11.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.reserve

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ActivityReserveBinding
import com.cbaelectronics.turinpadel.usecases.common.rows.ReserveRecyclerViewAdapter
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.addClose
import com.itdev.nosfaltauno.util.extension.font

class ReserveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReserveBinding
    private lateinit var viewModel: ReserveViewModel
    private lateinit var adapter: ReserveRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReserveBinding.inflate(layoutInflater)

        // Content
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this).get(ReserveViewModel::class.java)

        // Adapter
        adapter = ReserveRecyclerViewAdapter(this)

        // Setup
        localize()
        setup()
    }

    private fun localize() {

        binding.textViewReserve.text = getString(viewModel.title)

    }

    private fun setup() {

        addClose(this)

        // UI
        binding.textViewReserve.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        binding.recyclerViewReserve.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewReserve.adapter = adapter

        observeData()

    }

    private fun observeData() {
        viewModel.loadReserve().observe(this, Observer {
            adapter.setDataList(it)
            adapter.notifyDataSetChanged()
        })
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