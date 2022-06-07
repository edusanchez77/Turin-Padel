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
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ContentItemScheduleBinding
import com.cbaelectronics.turinpadel.model.domain.Schedule
import com.cbaelectronics.turinpadel.model.domain.Turn
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.cbaelectronics.turinpadel.util.Util
import com.itdev.nosfaltauno.util.extension.enable
import com.itdev.nosfaltauno.util.extension.font
import com.itdev.nosfaltauno.util.extension.longFormat
import java.util.*

class ScheduleRecyclerViewAdapter(private val context: Context, private val itemClickListener: ScheduleRecyclerViewAdapter.onClickScheduleClickListener): RecyclerView.Adapter<ScheduleRecyclerViewAdapter.ViewHolder>() {

    interface onClickScheduleClickListener{
        fun onItemButtonClick(schedule: Schedule)
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

        binding.textViewScheduleDate.font(FontSize.HEAD, FontType.BOLD, ContextCompat.getColor(context, R.color.text))
        binding.textViewScheduleCurt.font(FontSize.BUTTON, FontType.REGULAR, ContextCompat.getColor(context, R.color.text))
        binding.countDown.font(FontSize.BUTTON, FontType.REGULAR, ContextCompat.getColor(context, R.color.text))

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

            binding.textViewScheduleDate.text = schedule.date.longFormat()
            binding.textViewScheduleCurt.text = schedule.curt
            binding.buttonScheduleCancel.text = context.getString(R.string.schedule_button)

            // Buttons
            binding.buttonScheduleCancel.setOnClickListener {
                itemClickListener.onItemButtonClick(schedule)
            }

            // Timer
            timer(context, schedule)

        }

        private fun timer(context: Context, schedule: Schedule) {
            timer?.cancel()

            //binding.imageViewScheduleCountdown.setImageResource(R.drawable.time_clock_circle)
            //binding.imageViewScheduleCountdown.setColorFilter(ContextCompat.getColor(this@ScheduleRecyclerViewAdapter.context, R.color.light))

            val endDate = schedule.date?.time!! - Date().time

            timer = object : CountDownTimer(endDate, 1000) {

                override fun onTick(millisUntilFinished: Long) {

                    binding.countDown.text = Util.countdown(this@ScheduleRecyclerViewAdapter.context, millisUntilFinished)
                }

                override fun onFinish() {

                    binding.countDown.text = this@ScheduleRecyclerViewAdapter.context.getText(R.string.schedule_countDown_live).toString().uppercase()
                    binding.imageViewScheduleCountdown.setImageResource(R.drawable.button_record)
                    binding.imageViewScheduleCountdown.setColorFilter(ContextCompat.getColor(this@ScheduleRecyclerViewAdapter.context, R.color.live))
                    binding.buttonScheduleCancel.visibility = View.GONE
                    binding.buttonScheduleCancel.enable(false)
                    timer?.cancel()
                }

            }

            timer?.start()
        }
    }

}