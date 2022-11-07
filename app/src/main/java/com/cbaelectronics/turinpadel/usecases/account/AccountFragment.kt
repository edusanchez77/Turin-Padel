/**
 * Created by CbaElectronics by Eduardo Sanchez on 16/5/22 11:15.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.account

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.FragmentAccountBinding
import com.cbaelectronics.turinpadel.model.domain.Schedule
import com.cbaelectronics.turinpadel.model.domain.Turn
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.usecases.common.rows.ScheduleRecyclerViewAdapter
import com.cbaelectronics.turinpadel.usecases.settings.SettingsRouter
import com.cbaelectronics.turinpadel.util.Constants.FIXEDTURN_STATUS_CANCEL
import com.cbaelectronics.turinpadel.util.Constants.FIXEDTURN_STATUS_CONFIRM
import com.cbaelectronics.turinpadel.util.Constants.FIXEDTURN_STATUS_DELETED
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.font
import java.sql.Timestamp
import java.util.*

class AccountFragment : Fragment(), ScheduleRecyclerViewAdapter.onClickScheduleClickListener {

    private lateinit var _binding: FragmentAccountBinding
    private val binding get() = _binding!!
    private lateinit var viewModel: AccountViewModel
    private lateinit var adapter: ScheduleRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Content
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        // ViewModel
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        // Adapter
        adapter = ScheduleRecyclerViewAdapter(binding.root.context, this)

        // Setup
        localize()
        setup()

        return binding.root
    }

    private fun localize() {

        binding.textViewUsername.text = viewModel.user.displayName
        binding.textViewUserMail.text = viewModel.user.email
        Glide.with(this)
            .load(viewModel.user.photoProfile)
            .into(binding.imageViewAvatar)
        binding.textViewAccount.text = getString(viewModel.accountUserInfoText)

    }

    private fun setup() {

        // UI
        binding.textViewUsername.font(
            FontSize.HEAD,
            FontType.BOLD,
            ContextCompat.getColor(binding.root.context, R.color.light)
        )
        binding.textViewUserMail.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.light)
        )
        binding.textViewAccount.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        binding.recyclerViewAgenda.layoutManager = LinearLayoutManager(binding.root.context)
        binding.recyclerViewAgenda.adapter = adapter
        observeData()

        // Buttons
        buttons()

    }

    private fun observeData() {
        viewModel.loadSchedule(binding.root.context).observe(viewLifecycleOwner, Observer {
            adapter.setDataList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun buttons() {

        binding.imageButtonSettings.setOnClickListener {
            SettingsRouter().launch(binding.root.context)
        }

    }

    private fun createAlertDialog(schedule: Schedule, status: String){
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
            FIXEDTURN_STATUS_CANCEL -> getString(viewModel.cancel)
            FIXEDTURN_STATUS_DELETED -> getString(viewModel.deleted)
            FIXEDTURN_STATUS_CONFIRM -> getString(viewModel.confirm)
            else -> getString(viewModel.cancel)
        }

        mDialog.show()

        mBtnOK.setOnClickListener {
            mDialog.cancel()
            when(status){
                FIXEDTURN_STATUS_CANCEL -> viewModel.cancel(schedule)
                FIXEDTURN_STATUS_DELETED -> viewModel.delete(schedule)
                FIXEDTURN_STATUS_CONFIRM -> viewModel.confirm(schedule)
            }

        }

        mBtnCancel.setOnClickListener {
            mDialog.cancel()
        }
    }


    override fun onItemButtonCancelClick(schedule: Schedule) {
        createAlertDialog(schedule, FIXEDTURN_STATUS_CANCEL)
    }

    override fun onItemButtonDeleteClick(schedule: Schedule) {
        createAlertDialog(schedule, FIXEDTURN_STATUS_DELETED)
    }

    override fun onItemButtonConfirmClick(schedule: Schedule) {
        createAlertDialog(schedule, FIXEDTURN_STATUS_CONFIRM)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}