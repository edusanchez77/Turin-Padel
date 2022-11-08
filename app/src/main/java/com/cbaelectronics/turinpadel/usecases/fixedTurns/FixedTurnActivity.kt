/**
 * Created by CbaElectronics by Eduardo Sanchez on 14/9/22 10:25.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.fixedTurns

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ActivityFixedTurnBinding
import com.cbaelectronics.turinpadel.model.domain.FixedTurn
import com.cbaelectronics.turinpadel.model.domain.Schedule
import com.cbaelectronics.turinpadel.usecases.addFixedTurn.AddFixedTurnActivity
import com.cbaelectronics.turinpadel.usecases.addFixedTurn.AddFixedTurnRouter
import com.cbaelectronics.turinpadel.usecases.common.rows.FixedTurnRecyclerViewAdapter
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.addClose
import com.itdev.nosfaltauno.util.extension.font

class FixedTurnActivity : AppCompatActivity(), FixedTurnRecyclerViewAdapter.onClickFixedTurnClickListener {

    private lateinit var binding: ActivityFixedTurnBinding
    private lateinit var viewModel: FixedTurnViewModel
    private lateinit var adapter: FixedTurnRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFixedTurnBinding.inflate(layoutInflater)

        // Content
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this).get(FixedTurnViewModel::class.java)

        // Adapter
        adapter = FixedTurnRecyclerViewAdapter(this, this)


        // Setup
        localize()
        setup()

    }

    private fun localize() {
        binding.textViewFixedTurnInfo.text = getString(viewModel.title)
        binding.buttonUpdateAddFixedTurn.text = getString(viewModel.button)
    }

    private fun setup() {
        addClose(this)

        // UI
        binding.textViewFixedTurnInfo.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        binding.recyclerViewFixedTurn.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFixedTurn.adapter = adapter

        observeData()
        buttons()
    }

    private fun buttons() {
        binding.buttonUpdateAddFixedTurn.setOnClickListener {
            AddFixedTurnRouter().launch(this)
        }
    }

    private fun observeData() {
        viewModel.loadFixedTurn(binding.root.context).observe(this, Observer {
            adapter.setDataList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun createAlertDialog(fixedTurn: FixedTurn, status: String){
        val mDialog = Dialog(binding.root.context)
        val mWindows = mDialog.window!!

        mWindows.attributes.windowAnimations = R.style.DialogAnimation
        mDialog.setContentView(R.layout.custom_dialog_opciones)
        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.setCancelable(false)
        val mIcon = mDialog.findViewById<LottieAnimationView>(R.id.lottieDialog)
        val mText = mDialog.findViewById<TextView>(R.id.txtDialog)
        val mBtnOK = mDialog.findViewById<Button>(R.id.btnDialogAcept)
        val mBtnCancel = mDialog.findViewById<Button>(R.id.btnDialogCancel)
        mIcon.setAnimation(R.raw.agenda)
        mText.text = when(status){
            Constants.FIXEDTURN_STATUS_CANCEL -> getString(viewModel.cancel)
            Constants.FIXEDTURN_STATUS_DELETED -> getString(viewModel.deleted)
            Constants.FIXEDTURN_STATUS_CONFIRM -> getString(viewModel.confirm)
            else -> getString(viewModel.cancel)
        }

        mDialog.show()

        mBtnOK.setOnClickListener {
            mDialog.cancel()
            when(status){
                Constants.FIXEDTURN_STATUS_CANCEL -> viewModel.cancel(fixedTurn)
                Constants.FIXEDTURN_STATUS_DELETED -> viewModel.delete(fixedTurn)
                Constants.FIXEDTURN_STATUS_CONFIRM -> viewModel.confirm(fixedTurn)
            }

        }

        mBtnCancel.setOnClickListener {
            mDialog.cancel()
        }
    }

    override fun onItemButtonCancelClick(fixedTurn: FixedTurn) {
        createAlertDialog(fixedTurn, Constants.FIXEDTURN_STATUS_CANCEL)
    }

    override fun onItemButtonDeleteClick(fixedTurn: FixedTurn) {
        createAlertDialog(fixedTurn, Constants.FIXEDTURN_STATUS_DELETED)
    }

    override fun onItemButtonConfirmClick(fixedTurn: FixedTurn) {
        createAlertDialog(fixedTurn, Constants.FIXEDTURN_STATUS_CONFIRM)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

}