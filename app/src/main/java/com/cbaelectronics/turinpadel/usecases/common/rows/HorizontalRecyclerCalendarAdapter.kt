/**
 * Created by Cba Electronics on 23/6/21 15:04
 * Copyright (c) 2021 . All rights reserved.
 */

package com.cbaelectronics.turinpadel.usecases.common.rows

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ItemCalendarHorizontalBinding
import com.cbaelectronics.turinpadel.util.CalendarUtils
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.font
import java.util.*

class HorizontalRecyclerCalendarAdapter(
    startDate: Date,
    endDate: Date,
    private val configuration: RecyclerCalendarConfiguration,
    private var selectedDate: Date,
    private val dateSelectListener: OnDateSelected
) : RecyclerCalendarBaseAdapter(startDate, endDate, configuration) {

    interface OnDateSelected {
        fun onDateSelected(date: Date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_horizontal, parent, false)

        val binding = MonthCalendarViewHolder(view)

        binding.textViewDay.font(FontSize.CAPTION, FontType.REGULAR, ContextCompat.getColor(parent.context, R.color.text))
        binding.textViewDate.font(FontSize.TITLE, FontType.BOLD, ContextCompat.getColor(parent.context, R.color.text))
        binding.textViewMonth.font(FontSize.CAPTION, FontType.REGULAR, ContextCompat.getColor(parent.context, R.color.text))

        return MonthCalendarViewHolder(
            view
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        calendarItem: RecyclerCalenderViewItem
    ) {
        val monthViewHolder: MonthCalendarViewHolder = holder as MonthCalendarViewHolder
        val context: Context = monthViewHolder.itemView.context

        monthViewHolder.itemView.visibility = View.VISIBLE

        monthViewHolder.itemView.setOnClickListener(null)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            monthViewHolder.itemView.background = null
        } else {
            monthViewHolder.itemView.setBackgroundDrawable(null)
        }

        monthViewHolder.cardViewDate.setCardBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.background
            )
        )
        monthViewHolder.textViewDay.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.text
            )
        )
        monthViewHolder.textViewDate.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.text
            )
        )

        monthViewHolder.textViewMonth.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.text
            )
        )

        if (calendarItem.isHeader) {
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.time = calendarItem.date

            val month: String = CalendarUtils.dateStringFromFormat(
                locale = configuration.calendarLocale,
                date = selectedCalendar.time,
                format = CalendarUtils.DISPLAY_MONTH_FORMAT
            ) ?: ""
            val year = selectedCalendar[Calendar.YEAR].toLong()

            monthViewHolder.textViewDay.text = year.toString()
            monthViewHolder.textViewDate.text = month

            monthViewHolder.itemView.setOnClickListener(null)
        } else if (calendarItem.isEmpty) {
            monthViewHolder.itemView.visibility = View.GONE
            monthViewHolder.textViewDay.text = ""
            monthViewHolder.textViewDate.text = ""
        } else {
            val calendarDate = Calendar.getInstance()
            calendarDate.time = calendarItem.date

            val stringCalendarTimeFormat: String =
                CalendarUtils.dateStringFromFormat(
                    locale = configuration.calendarLocale,
                    date = calendarItem.date,
                    format = CalendarUtils.DB_DATE_FORMAT
                )
                    ?: ""
            val stringSelectedTimeFormat: String =
                CalendarUtils.dateStringFromFormat(
                    locale = configuration.calendarLocale,
                    date = selectedDate,
                    format = CalendarUtils.DB_DATE_FORMAT
                ) ?: ""

            if (stringCalendarTimeFormat == stringSelectedTimeFormat) {
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    monthViewHolder.itemView.background =
                        ContextCompat.getDrawable(context, R.drawable.layout_round_corner_filled)
                } else {
                    monthViewHolder.itemView.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.layout_round_corner_filled
                        )
                    )
                }

                 */

                monthViewHolder.cardViewDate.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.primary
                    )
                )


                monthViewHolder.textViewDay.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.light
                    )
                )

                monthViewHolder.textViewDate.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.light
                    )
                )

                monthViewHolder.textViewMonth.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.light
                    )
                )





            }

            val day: String = CalendarUtils.dateStringFromFormat(
                locale = Locale.forLanguageTag("es-ES"),
                date = calendarDate.time,
                format = CalendarUtils.DISPLAY_WEEK_DAY_FORMAT
            ) ?: ""

            val month: String = CalendarUtils.dateStringFromFormat(
                locale = Locale.forLanguageTag("es-ES"),
                date = calendarDate.time,
                format = CalendarUtils.DISPLAY_MONTH_FORMAT
            ) ?: ""

            monthViewHolder.textViewDay.text = day.uppercase()
            monthViewHolder.textViewMonth.text = month
            monthViewHolder.textViewDate.text =
                CalendarUtils.dateStringFromFormat(
                    locale = Locale.forLanguageTag("es-ES"),
                    date = calendarDate.time,
                    format = CalendarUtils.DISPLAY_DATE_FORMAT
                ) ?: ""

            monthViewHolder.itemView.setOnClickListener {
                selectedDate = calendarItem.date
                dateSelectListener.onDateSelected(calendarItem.date)
                notifyDataSetChanged()
            }


        }
    }

    class MonthCalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ItemCalendarHorizontalBinding.bind(itemView)

        val cardViewDate: CardView = itemView.findViewById(R.id.cardViewDate)
        val textViewDay: TextView = itemView.findViewById(R.id.textCalenderItemHorizontalDay)
        val textViewDate: TextView = itemView.findViewById(R.id.textCalenderItemHorizontalDate)
        val textViewMonth: TextView = itemView.findViewById(R.id.textCalenderItemHorizontalMonth)
    }
}