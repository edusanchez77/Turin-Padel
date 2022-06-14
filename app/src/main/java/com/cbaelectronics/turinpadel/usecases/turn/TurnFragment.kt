/**
 * Created by CbaElectronics by Eduardo Sanchez on 16/5/22 11:16.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.turn

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.PagerSnapHelper
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.FragmentTurnBinding
import com.cbaelectronics.turinpadel.usecases.addTurn.AddTurnRouter
import com.cbaelectronics.turinpadel.usecases.common.rows.HorizontalRecyclerCalendarAdapter
import com.cbaelectronics.turinpadel.usecases.common.rows.RecyclerCalendarConfiguration
import com.cbaelectronics.turinpadel.usecases.common.tabs.tabCurtsAdapter
import com.cbaelectronics.turinpadel.util.CalendarUtils
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.Constants.ADMIN
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class TurnFragment : Fragment() {

    private var _binding: FragmentTurnBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TurnViewModel
    private lateinit var mDate: String
    private lateinit var adapter: tabCurtsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Content
        _binding = FragmentTurnBinding.inflate(inflater, container, false)

        // ViewModel
        viewModel = ViewModelProvider(this).get(TurnViewModel::class.java)

        // Localize
        localize()

        // Setup
        calendar()
        footer()

        return binding.root
    }


    private fun localize() {
        binding.buttonUpdate.text = getString(viewModel.button)
    }

    private fun calendar() {

        val date = Date()
        date.time = System.currentTimeMillis()

        val startCal = Calendar.getInstance()

        val endCal = Calendar.getInstance()
        endCal.time = date
        endCal.add(Calendar.MONTH, 3)

        val configuration: RecyclerCalendarConfiguration =
            RecyclerCalendarConfiguration(
                calenderViewType = RecyclerCalendarConfiguration.CalenderViewType.HORIZONTAL,
                calendarLocale = Locale.getDefault(),
                includeMonthHeader = true
            )


        mDate = CalendarUtils.dateStringFromFormat(
            locale = Locale.forLanguageTag("es-ES"),
            date = date,
            format = CalendarUtils.DISPLAY_DATE_TURNO
        ).toString()

        adapter = tabCurtsAdapter(this, mDate)

        val calendarAdapterHorizontal: HorizontalRecyclerCalendarAdapter =
            HorizontalRecyclerCalendarAdapter(
                startDate = startCal.time,
                endDate = endCal.time,
                configuration = configuration,
                selectedDate = date,
                dateSelectListener = object : HorizontalRecyclerCalendarAdapter.OnDateSelected {
                    override fun onDateSelected(date: Date) {

                        mDate = CalendarUtils.dateStringFromFormat(
                            locale = Locale.forLanguageTag("es-ES"),
                            date = date,
                            format = CalendarUtils.DISPLAY_DATE_TURNO
                        ).toString()

                        adapter = tabCurtsAdapter(this@TurnFragment, mDate)
                        adapter.notifyDataSetChanged()
                        binding.viewPagerCurts.adapter = adapter

                    }
                }
            )

        binding.turnRecyclerViewCalendar.adapter = calendarAdapterHorizontal

        val snapHelper = PagerSnapHelper() // Or LinearSnapHelper
        snapHelper.attachToRecyclerView(binding.turnRecyclerViewCalendar)


        binding.viewPagerCurts.adapter = adapter
        binding.tabLayoutCanchas.setTabTextColors(ContextCompat.getColor(binding.root.context, R.color.background), ContextCompat.getColor(binding.root.context, R.color.light))
        binding.tabLayoutCanchas.setSelectedTabIndicatorColor(ContextCompat.getColor(binding.root.context, R.color.secondary))

        val tabLayoutMediator = TabLayoutMediator(binding.tabLayoutCanchas, binding.viewPagerCurts,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when(position){
                    0 -> {
                        tab.text = getString(viewModel.curt1).uppercase()
                    }
                    1 -> {
                        tab.text = getString(viewModel.curt2).uppercase()
                    }
                }
            }
        )
        tabLayoutMediator.attach()

    }


    private fun footer(){

        if(viewModel.user.type!!.toInt() == ADMIN){
            binding.layoutButtons.visibility = View.VISIBLE
        }else{
            binding.layoutButtons.visibility = View.GONE
        }

        binding.buttonUpdate.setOnClickListener {
            AddTurnRouter().launch(binding.root.context)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}