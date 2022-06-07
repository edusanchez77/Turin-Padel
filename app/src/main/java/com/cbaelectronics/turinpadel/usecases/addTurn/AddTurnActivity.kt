/**
 * Created by CbaElectronics by Eduardo Sanchez on 24/5/22 16:47.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.addTurn

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ActivityAddTurnBinding
import com.cbaelectronics.turinpadel.model.domain.Turn
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.Constants.TURN_DATE_FORMAT
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.addClose
import com.itdev.nosfaltauno.util.extension.font
import com.itdev.nosfaltauno.util.extension.hideSoftInput
import java.text.SimpleDateFormat
import java.util.*

class AddTurnActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityAddTurnBinding
    private lateinit var viewModel: AddTurnViewModel

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var vDay = 0
    var vMonth = 0
    var vYear = 0
    var vHour = 0
    var vMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTurnBinding.inflate(layoutInflater)

        // Content
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this).get(AddTurnViewModel::class.java)

        // Setup
        localize()
        setup()
        footer()

    }

    private fun localize() {
        binding.textViewAddTurn.text = getString(viewModel.info)
        binding.buttonSaveTurn.text = getString(viewModel.save)
        binding.buttonCancelTurn.text = getString(viewModel.cancel)
    }

    private fun setup() {

        addClose(this)

        // UI
        binding.textViewAddTurn.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        // Date and Time
        
        binding.etHorarioTurnoLibre.setOnFocusChangeListener { view, hasFocus -> 
            if(hasFocus){
                hideSoftInput()
                getDateTimeCalendar()
                DatePickerDialog(this, R.style.themeOnverlay_timePicker,this, year, month, day).show()
            }
        }
        
        // Curts
        
        val arrayCanchas = resources.getStringArray(R.array.strCanchas)
        val adapterCanchas = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            arrayCanchas
        )
        binding.etCanchaTurnoLibre.setAdapter(adapterCanchas)

    }

    private fun getDateTimeCalendar(){

        TimeZone.getDefault()
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)

    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        vYear = p1
        vMonth = p2+1
        vDay = p3

        getDateTimeCalendar()

        TimePickerDialog(this, R.style.themeOnverlay_timePicker, this, hour, minute, true).show()
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        vHour = p1
        vMinute = p2

        val vFecha = "$vDay/$vMonth/$vYear $vHour:$vMinute"
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val date = sdf.parse(vFecha)
        val newDate = sdf.format(date)

        binding.etHorarioTurnoLibre.setText(newDate)
    }


    @SuppressLint("SimpleDateFormat")
    private fun footer() {
        // Button Cancel
        
        binding.buttonCancelTurn.setOnClickListener { 
            onBackPressed()
        }
        
        // Button Save
        
        binding.buttonSaveTurn.setOnClickListener {

            // Save DataBase

            val date = binding.etHorarioTurnoLibre.text.toString()
            val curt = binding.etCanchaTurnoLibre.text.toString()

            val sdf = SimpleDateFormat(TURN_DATE_FORMAT)
            val date1 = sdf.parse(date)

            val turn = Turn(curt = curt, date = date1)
            viewModel.save(turn)

            // Limpio campos

            binding.etHorarioTurnoLibre.text = null
            binding.etCanchaTurnoLibre.text = null
        }
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