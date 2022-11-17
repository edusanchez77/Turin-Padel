/**
 * Created by CbaElectronics by Eduardo Sanchez on 31/5/22 12:49.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.common.rows

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ContentItemScheduleBinding
import com.cbaelectronics.turinpadel.model.domain.Schedule
import com.cbaelectronics.turinpadel.model.domain.Turn
import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.Constants.FIXEDTURN_STATUS_PENDING
import com.cbaelectronics.turinpadel.util.Constants.GAME_TIME
import com.cbaelectronics.turinpadel.util.Constants.SECOND
import com.cbaelectronics.turinpadel.util.Constants.TIME_UNTIL_CANCEL
import com.cbaelectronics.turinpadel.util.Constants.TYPE_FIXED_TURN
import com.cbaelectronics.turinpadel.util.Constants.TYPE_TURN
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.cbaelectronics.turinpadel.util.Util
import com.cbaelectronics.turinpadel.util.countUp.CountUpTimer
import com.itdev.nosfaltauno.util.extension.enable
import com.itdev.nosfaltauno.util.extension.font
import com.itdev.nosfaltauno.util.extension.longFormat
import java.util.*
import kotlin.time.Duration.Companion.milliseconds

class ScheduleRecyclerViewAdapter(private val context: Context, private val itemClickListener: ScheduleRecyclerViewAdapter.onClickScheduleClickListener): RecyclerView.Adapter<ScheduleRecyclerViewAdapter.ViewHolder>() {

    interface onClickScheduleClickListener{
        fun onItemButtonCancelClick(schedule: Schedule)
        fun onItemButtonDeleteClick(schedule: Schedule)
        fun onItemButtonConfirmClick(schedule: Schedule)
    }

    private var dataList = mutableListOf<Schedule>()

    fun setDataList(data: MutableList<Schedule>){
        dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.content_item_schedule, parent, false)
        val viewHolder = ViewHolder(view)

        // UI
        val binding = viewHolder.binding

        binding.textViewScheduleDate.font(FontSize.SUBHEAD, FontType.BOLD, ContextCompat.getColor(context, R.color.text))
        binding.textViewScheduleCurt.font(FontSize.BUTTON, FontType.REGULAR, ContextCompat.getColor(context, R.color.text))
        binding.textViewScheduleClip.font(FontSize.CAPTION, FontType.REGULAR, ContextCompat.getColor(context, R.color.text))
        binding.countDown.font(FontSize.BUTTON, FontType.REGULAR, ContextCompat.getColor(context, R.color.text))
        binding.chronometerItemSchedule.font(FontSize.CAPTION, FontType.REGULAR, ContextCompat.getColor(context, R.color.text))
        binding.chronometerItemInfo.font(FontSize.CAPTION, FontType.REGULAR, ContextCompat.getColor(context, R.color.text))

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schedule = dataList[position]
        holder.bindView(schedule)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val binding = ContentItemScheduleBinding.bind(itemView)
        private var timer: CountDownTimer? = null

        fun bindView(schedule: Schedule){

            // Localize
            binding.textViewScheduleDate.text = schedule.date.longFormat()
            binding.textViewScheduleCurt.text = schedule.curt
            binding.buttonScheduleCancel.text = context.getString(R.string.schedule_button_cancel)
            binding.chronometerItemInfo.text = context.getString(R.string.schedule_chronometer_info)
            binding.buttonDeleteFixedTurn.text = context.getString(R.string.schedule_button_fixedTurn_delete)
            binding.buttonCancelFixedTurn.text = context.getString(R.string.schedule_button_fixedTurn_cancel)
            binding.buttonSaveFixedTurn.text = context.getString(R.string.schedule_button_fixedTurn_confirm)
            binding.textViewScheduleClip.text = context.getString(R.string.schedule_clip_fixedTurn)

            // Setup
            binding.chronometerItemSchedule.visibility = View.GONE
            binding.chronometerItemInfo.visibility = View.GONE
            binding.buttonScheduleCancel.visibility = if(schedule.turnType == TYPE_TURN) View.VISIBLE else View.GONE
            binding.imageViewClipFixedTurn.visibility = if(schedule.turnType == TYPE_FIXED_TURN) View.VISIBLE else View.GONE
            binding.textViewScheduleClip.visibility = if(schedule.turnType == TYPE_FIXED_TURN) View.VISIBLE else View.GONE
            binding.layoutConfirmFixedTurnButtons.visibility = if(schedule.turnType == TYPE_FIXED_TURN && schedule.status == FIXEDTURN_STATUS_PENDING) View.VISIBLE else View.GONE

            // Buttons
            binding.buttonScheduleCancel.setOnClickListener {
                itemClickListener.onItemButtonCancelClick(schedule)
            }

            binding.buttonCancelFixedTurn.setOnClickListener {
                itemClickListener.onItemButtonCancelClick(schedule)
            }

            binding.buttonSaveFixedTurn.setOnClickListener {
                itemClickListener.onItemButtonConfirmClick(schedule)
            }

            binding.buttonDeleteFixedTurn.setOnClickListener {
                itemClickListener.onItemButtonDeleteClick(schedule)
            }



            // Timer
            timer(context, schedule)

        }

        private fun timer(context: Context, schedule: Schedule) {
            timer?.cancel()

            val endDate = schedule.date?.time!! - Date().time

            timer = object : CountDownTimer(endDate, SECOND.toLong()) {

                override fun onTick(millisUntilFinished: Long) {

                    binding.countDown.text = Util.countdown(context, millisUntilFinished)

                    if(millisUntilFinished < Util.hourToMilliseconds(TIME_UNTIL_CANCEL)){
                        binding.buttonScheduleCancel.visibility = View.GONE
                        binding.buttonScheduleCancel.enable(false)
                        binding.layoutConfirmFixedTurnButtons.visibility = View.GONE
                        binding.buttonDeleteFixedTurn.enable(false)
                        binding.buttonCancelFixedTurn.enable(false)
                        binding.buttonSaveFixedTurn.enable(false)

                        if(schedule.turnType == TYPE_FIXED_TURN){
                            updateTurnStatus(schedule, Constants.FIXEDTURN_STATUS_CONFIRM) //Revisar el estado del turno cuando se acaba el tiempo para confirmar/cancelar
                        }
                    }

                }

                override fun onFinish() {

                    binding.countDown.text = context.getText(R.string.schedule_countDown_live).toString().uppercase()
                    binding.imageViewScheduleCountdown.setImageResource(R.drawable.button_record)
                    binding.imageViewScheduleCountdown.setColorFilter(ContextCompat.getColor(context, R.color.live))
                    binding.buttonScheduleCancel.visibility = View.GONE
                    binding.buttonScheduleCancel.enable(false)
                    binding.layoutConfirmFixedTurnButtons.visibility = View.GONE
                    binding.buttonDeleteFixedTurn.enable(false)
                    binding.buttonCancelFixedTurn.enable(false)
                    binding.buttonSaveFixedTurn.enable(false)

                    timer?.cancel()

                    chronometer(context, schedule)

                }

            }

            timer?.start()
        }

        private fun chronometer(context: Context, schedule: Schedule){

            binding.chronometerItemSchedule.visibility = View.VISIBLE
            binding.chronometerItemInfo.visibility = View.VISIBLE

            timer?.cancel()

            val endDate = Calendar.getInstance()
            endDate.time = schedule.date
            endDate.add(Calendar.MINUTE, GAME_TIME)
            val turnDuration = endDate.time.time - Date().time

            timer = object : CountDownTimer(turnDuration, SECOND.toLong()) {

                override fun onTick(millisUntilFinished: Long) {

                    binding.chronometerItemSchedule.text = Util.countdown(context, millisUntilFinished)

                }

                override fun onFinish() {

                    binding.countDown.text = this@ScheduleRecyclerViewAdapter.context.getText(R.string.schedule_countDown_finish).toString().uppercase()
                    binding.chronometerItemSchedule.visibility = View.GONE
                    binding.chronometerItemInfo.visibility = View.GONE
                    binding.imageViewScheduleCountdown.setImageResource(R.drawable.button_record)
                    binding.imageViewScheduleCountdown.setColorFilter(ContextCompat.getColor(context, R.color.text))

                    timer?.cancel()

                }

            }

            timer?.start()

        }

        private fun updateTurnStatus(schedule: Schedule, status: String) {
            FirebaseDBService.updateFixedTurn(schedule, status)
        }
    }


}