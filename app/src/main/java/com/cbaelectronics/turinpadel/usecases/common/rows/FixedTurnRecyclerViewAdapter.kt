/**
 * Created by CbaElectronics by Eduardo Sanchez on 31/5/22 12:49.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.common.rows

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ContentItemFixedTurnScheduleBinding
import com.cbaelectronics.turinpadel.model.domain.FixedTurn
import com.cbaelectronics.turinpadel.model.domain.Schedule
import com.cbaelectronics.turinpadel.util.Constants.FIXEDTURN_STATUS_CANCEL
import com.cbaelectronics.turinpadel.util.Constants.FIXEDTURN_STATUS_CONFIRM
import com.cbaelectronics.turinpadel.util.Constants.FIXEDTURN_STATUS_DELETED
import com.cbaelectronics.turinpadel.util.Constants.FIXEDTURN_STATUS_PENDING
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.*
import java.sql.Timestamp
import java.util.*

class FixedTurnRecyclerViewAdapter(
    private val context: Context,
    private val itemClickListener: FixedTurnRecyclerViewAdapter.onClickFixedTurnClickListener
) : RecyclerView.Adapter<FixedTurnRecyclerViewAdapter.ViewHolder>() {

    interface onClickFixedTurnClickListener {
        fun onItemButtonCancelClick(fixedTurn: FixedTurn)
        fun onItemButtonDeleteClick(fixedTurn: FixedTurn)
        fun onItemButtonConfirmClick(fixedTurn: FixedTurn)
    }

    private var dataList = mutableListOf<FixedTurn>()

    fun setDataList(data: MutableList<FixedTurn>) {
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.content_item_fixed_turn_schedule, parent, false)
        val viewHolder = ViewHolder(view)

        // UI
        val binding = viewHolder.binding

        binding.textViewFixedTurnDay.font(
            FontSize.HEAD,
            FontType.BOLD,
            ContextCompat.getColor(context, R.color.text)
        )
        binding.textViewFixedTurnDayText.font(
            FontSize.HEAD,
            FontType.BOLD,
            ContextCompat.getColor(context, R.color.text)
        )
        binding.textViewFixedTurnDate.font(
            FontSize.SUBHEAD,
            FontType.BOLD,
            ContextCompat.getColor(context, R.color.text)
        )
        binding.textViewFixedTurnCurt.font(
            FontSize.BUTTON,
            FontType.REGULAR,
            ContextCompat.getColor(context, R.color.text)
        )
        binding.textViewFixedTurnReservedBy.font(
            FontSize.BUTTON,
            FontType.REGULAR,
            ContextCompat.getColor(context, R.color.text)
        )
        binding.textViewFixedTurnStatus.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(context, R.color.light)
        )


        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fixedTurn = dataList[position]
        holder.bindView(fixedTurn)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ContentItemFixedTurnScheduleBinding.bind(itemView)

        fun bindView(fixedTurn: FixedTurn) {

            // Localize
            binding.textViewFixedTurnDay.text = fixedTurn.day?.subSequence(0, 1)
            binding.textViewFixedTurnDayText.text = fixedTurn.day?.uppercaseFirst()
            binding.textViewFixedTurnDate.text = fixedTurn.date?.hourFixedTurnFormatSchedule()
            binding.textViewFixedTurnCurt.text = fixedTurn.curt
            binding.buttonCancelFixedTurn.text = context.getString(R.string.schedule_button_fixedTurn_cancel)
            binding.buttonSaveFixedTurn.text = context.getString(R.string.schedule_button_fixedTurn_confirm)
            binding.buttonDeleteFixedTurn.text = context.getString(R.string.schedule_button_fixedTurn_delete)
            Glide.with(context).load(fixedTurn.user?.photoProfile)
                .into(binding.imageViewFixedTurneAvatarUser)
            binding.textViewFixedTurnReservedBy.text = fixedTurn.user?.displayName
            binding.textViewFixedTurnStatus.text = when (fixedTurn.status) {
                FIXEDTURN_STATUS_CONFIRM -> context.getString(R.string.fixedTurn_status_confirm)
                FIXEDTURN_STATUS_CANCEL -> context.getString(R.string.fixedTurn_status_cancel)
                FIXEDTURN_STATUS_DELETED -> context.getString(R.string.fixedTurn_status_deleted)
                else -> context.getString(R.string.fixedTurn_status_pending)
            }

            // Setup

            val colorStatus = when(fixedTurn.status){
                FIXEDTURN_STATUS_CONFIRM -> ContextCompat.getColor(context, R.color.confirm)
                FIXEDTURN_STATUS_CANCEL -> ContextCompat.getColor(context, R.color.cancel)
                FIXEDTURN_STATUS_PENDING -> ContextCompat.getColor(context, R.color.pending)
                else -> ContextCompat.getColor(context, R.color.cancel)
            }

            binding.linearLayoutFixedTurnScheduleDay.visibility = if(fixedTurn.day.isNullOrEmpty()) View.GONE else View.VISIBLE
            binding.cardViewFixedTurnStatus.setCardBackgroundColor(colorStatus)
            binding.layoutConfirmFixedTurnButtons.visibility = if(fixedTurn.status == FIXEDTURN_STATUS_PENDING) View.VISIBLE else View.GONE

            // Buttons

            binding.buttonCancelFixedTurn.setOnClickListener {
                itemClickListener.onItemButtonCancelClick(fixedTurn)
            }

            binding.buttonSaveFixedTurn.setOnClickListener {
                itemClickListener.onItemButtonConfirmClick(fixedTurn)
            }

            binding.buttonDeleteFixedTurn.setOnClickListener {
                itemClickListener.onItemButtonDeleteClick(fixedTurn)
            }

        }


    }

}