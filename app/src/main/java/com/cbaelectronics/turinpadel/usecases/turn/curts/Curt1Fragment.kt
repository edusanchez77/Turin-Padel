/**
 * Created by CbaElectronics by Eduardo Sanchez on 16/5/22 12:48.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.turn.curts

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.FragmentCurt1Binding
import com.cbaelectronics.turinpadel.model.domain.Turn
import com.cbaelectronics.turinpadel.usecases.common.rows.TurnsRecyclerViewAdapter
import com.cbaelectronics.turinpadel.usecases.turn.TurnViewModel
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.font
import java.sql.Timestamp
import java.util.*

class Curt1Fragment(pDate: String) : Fragment(), TurnsRecyclerViewAdapter.onClickTurnClickListener {

    private var _binding: FragmentCurt1Binding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TurnViewModel
    private lateinit var adapter: TurnsRecyclerViewAdapter
    private val mDate = pDate

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Content
        _binding = FragmentCurt1Binding.inflate(inflater, container, false)

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
        binding.textViewInfoCurt1.text = getString(viewModel.info)
    }

    private fun setup() {

        // UI
        binding.textViewInfoCurt1.font(
            FontSize.SUBHEAD,
            FontType.BOLD,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        // RecyclerView
        binding.recyclerViewCurt1.layoutManager = LinearLayoutManager(binding.root.context)
        binding.recyclerViewCurt1.adapter = adapter

        observeData()
    }

    private fun observeData() {
        viewModel.loadTurn(getString(R.string.turn_curt_1), mDate).observe(viewLifecycleOwner, Observer {
            adapter.setDataList(it)
            adapter.notifyDataSetChanged()

            if(it.isNotEmpty()){
                binding.textViewInfoCurt1.visibility = View.GONE
            }

        })
    }

    override fun onItemClick(turn: Turn) {
        Toast.makeText(binding.root.context, "Eliminar turno", Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(turn: Turn) {
        /*if(viewModel.user.type == Constants.TYPE_ADMIN){

        }else{
            Toast.makeText(binding.root.context, "No tenes permisos para modificar el turno", Toast.LENGTH_SHORT).show()
        }*/
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
            //asyncAwait(pId, pHorario, pCancha, pStatus)
        }

        mBtnCancel.setOnClickListener {
            mDialog.cancel()
        }
        
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            1 -> {
                //Toast.makeText(binding.root.context, item.groupId.toString(), Toast.LENGTH_SHORT).show()
                adapter.deleteTurn(item.groupId)
                true
            }
            2 -> {
                //Toast.makeText(binding.root.context, item.groupId.toString(), Toast.LENGTH_SHORT).show()
                adapter.deleteTurn(item.groupId)
                true
            }
            else -> {
                super.onContextItemSelected(item)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.textViewInfoCurt1.visibility = View.VISIBLE
        //registerForContextMenu(binding.recyclerViewCurt1)
    }



}