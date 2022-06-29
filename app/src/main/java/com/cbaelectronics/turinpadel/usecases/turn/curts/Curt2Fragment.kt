/**
 * Created by CbaElectronics by Eduardo Sanchez on 16/5/22 12:49.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.turn.curts

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.FragmentCurt2Binding
import com.cbaelectronics.turinpadel.model.domain.Turn
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService
import com.cbaelectronics.turinpadel.usecases.addTurn.AddTurnRouter
import com.cbaelectronics.turinpadel.usecases.common.rows.TurnsRecyclerViewAdapter
import com.cbaelectronics.turinpadel.usecases.turn.TurnViewModel
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.cbaelectronics.turinpadel.util.UIUtil
import com.itdev.nosfaltauno.util.extension.font
import kotlinx.coroutines.runBlocking
import java.sql.Timestamp
import java.util.*

class Curt2Fragment(pDate: String) : Fragment(), TurnsRecyclerViewAdapter.onClickTurnClickListener {

    private lateinit var viewModel: TurnViewModel
    private var _binding: FragmentCurt2Binding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TurnsRecyclerViewAdapter
    private val mDate = pDate

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Content
        _binding = FragmentCurt2Binding.inflate(inflater, container, false)

        // ViewModel
        viewModel = ViewModelProvider(this).get(TurnViewModel::class.java)

        // Adapter
        adapter = TurnsRecyclerViewAdapter(binding.root.context, this)

        // Setup
        localize()
        setup()

        return binding.root
    }

    private fun localize() {
        binding.textViewInfoCurt2.text = getString(viewModel.info)
    }

    private fun setup() {

        // UI
        binding.textViewInfoCurt2.font(
            FontSize.SUBHEAD,
            FontType.BOLD,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        // RecyclerView
        binding.recyclerViewCurt2.layoutManager = LinearLayoutManager(binding.root.context)
        binding.recyclerViewCurt2.adapter = adapter

        observeData()
    }

    private fun observeData() {
        viewModel.loadTurn(getString(R.string.turn_curt_2), mDate)
            .observe(viewLifecycleOwner, Observer {
                adapter.setDataList(it)
                adapter.notifyDataSetChanged()

                if (it.isNotEmpty()) {
                    binding.textViewInfoCurt2.visibility = View.GONE
                }
            })
    }

    private fun showMessageOk() {

        val alertInfo = getString(viewModel.alertInfo)
        val alertOk = getString(viewModel.alertOk)
        val button = getString(viewModel.buttonAlertOk)

        val mDialog = Dialog(binding.root.context)
        val mWindows = mDialog.window!!

        mWindows.attributes.windowAnimations = R.style.DialogAnimation
        mDialog.setContentView(R.layout.custom_dialog_error)
        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.setCancelable(false)
        val mIcon = mDialog.findViewById<LottieAnimationView>(R.id.lottieDialogError)
        val mText = mDialog.findViewById<TextView>(R.id.txtDialog)
        val mBtnOK = mDialog.findViewById<Button>(R.id.btnDialog)
        mIcon.setAnimation(R.raw.turnos)
        mText.text = alertInfo
        mBtnOK.text = button

        mBtnOK.setOnClickListener {
            mDialog.cancel()
            Toast.makeText(binding.root.context, alertOk, Toast.LENGTH_SHORT).show()
        }

        mDialog.show()
    }

    override fun onItemButtonClick(turn: Turn) {

        val mDialog = Dialog(binding.root.context)
        val mWindows = mDialog.window!!

        val mToday = Timestamp(Date().time)

        mWindows.attributes.windowAnimations = R.style.DialogAnimation
        mDialog.setContentView(R.layout.custom_dialog_opciones)
        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.setCancelable(false)
        val mIcon = mDialog.findViewById<LottieAnimationView>(R.id.lottieDialog)
        val mText = mDialog.findViewById<TextView>(R.id.txtDialog)
        val mBtnOK = mDialog.findViewById<Button>(R.id.btnDialogAcept)
        val mBtnCancel = mDialog.findViewById<Button>(R.id.btnDialogCancel)
        mIcon.setAnimation(R.raw.agenda)
        mText.text = getString(viewModel.alertReserve)

        if (turn.date > mToday){
            mDialog.show()
        }else{
            Toast.makeText(binding.root.context, "Turno vencido", Toast.LENGTH_SHORT).show()
        }

        mBtnOK.setOnClickListener {
            mDialog.cancel()
            viewModel.reserveTurn(turn)
            showMessageOk()
        }

        mBtnCancel.setOnClickListener {
            mDialog.cancel()
        }

    }

    override fun onItemLongClick(turn: Turn) {
        if(viewModel.user.type == Constants.USER){
            Toast.makeText(binding.root.context, "No tenes permisos para modificar el turno", Toast.LENGTH_SHORT).show()
        }else{
            UIUtil.showOptions(binding.root.context, {editTurn(turn)}, { question(turn) })
        }
    }

    private fun editTurn(turn: Turn){
        AddTurnRouter().launch(binding.root.context, turn)
    }

    private fun question(turn: Turn){

        UIUtil.showAlert(
            context = binding.root.context,
            message = binding.root.context.getString(R.string.turn_delete_question),
            positive = binding.root.context.getString(R.string.turn_delete_button_positive),
            positiveAction = { delete(turn.id.toString()) },
            negative = binding.root.context.getString(R.string.turn_delete_button_negative)
        )

    }

    private fun delete(id: String){

        // Delete Database
        FirebaseDBService.deleteTurn(id)

        // Alert
        UIUtil.showAlert(
            context = binding.root.context,
            message = binding.root.context.getString(R.string.turn_delete_info_message),
            positive = binding.root.context.getString(R.string.turn_delete_button_ok)
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.textViewInfoCurt2.visibility = View.VISIBLE
    }

}