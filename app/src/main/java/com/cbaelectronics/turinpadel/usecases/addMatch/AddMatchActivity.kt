/**
 * Created by CbaElectronics by Eduardo Sanchez on 18/11/22 10:29.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.addMatch

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ActivityAddMatchBinding
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.itdev.nosfaltauno.util.extension.font
import com.itdev.nosfaltauno.util.extension.titleCustom
import java.text.SimpleDateFormat
import java.util.*

class AddMatchActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityAddMatchBinding
    private lateinit var viewModel: AddMatchViewModel

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
        binding = ActivityAddMatchBinding.inflate(layoutInflater)

        // Content
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this).get(AddMatchViewModel::class.java)

        // Setup
        localize()
        setup()
    }

    private fun localize() {

        binding.textViewAddMatchInfo.text = getString(viewModel.addMatchInfo)
        binding.textFieldFecha.hint = getString(viewModel.addMatchFechaPlaceholder)
        binding.textFieldVacantes.hint = getString(viewModel.addMatchVacantesPlaceholder)
        binding.textFieldCategoria.hint = getString(viewModel.addMatchCategoryPlaceholder)
        binding.textFieldGenero.hint = getString(viewModel.addMatchGenrePlaceholder)
        binding.buttonAddMatches.text = getString(viewModel.addMatchButton)

        optionsList()

    }

    private fun optionsList() {
        val arrayCategory = resources.getStringArray(R.array.array_settings_category)
        val arrayGenre = resources.getStringArray(R.array.match_genre)
        val categoryAdapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, arrayCategory)
        val genreAdapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, arrayGenre)

        binding.spinnerCategory.setAdapter(categoryAdapter)
        binding.spinnerGenre.setAdapter(genreAdapter)
    }

    private fun setup() {

        // UI

        supportActionBar?.titleCustom(this, getString(viewModel.addMatchTitle))
        binding.textViewAddMatchInfo.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(this, R.color.text)
        )

        binding.editTextVacantes.filters = arrayOf<InputFilter>(MinMaxFilter(1, 4))

        // Setup

        focus()
        buttons()

    }

    private fun focus() {
        binding.editTextFecha.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                getDateTimeCalendar()
                DatePickerDialog(
                    this,
                    R.style.themeOnverlay_timePicker,
                    this,
                    year,
                    month,
                    day
                ).show()
            }
        }
    }

    private fun buttons() {
        binding.buttonAddMatches.setOnClickListener {
            Toast.makeText(this, "Partido Guardado", Toast.LENGTH_SHORT).show()
        }
    }

    // Custom class to define min and max for the edit text
    inner class MinMaxFilter() : InputFilter {
        private var intMin: Int = 0
        private var intMax: Int = 0

        // Initialized
        constructor(minValue: Int, maxValue: Int) : this() {
            this.intMin = minValue
            this.intMax = maxValue
        }

        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dStart: Int, dEnd: Int): CharSequence? {
            try {
                val input = Integer.parseInt(dest.toString() + source.toString())
                if (isInRange(intMin, intMax, input)) {
                    return null
                }else{
                    Toast.makeText(applicationContext, getString(viewModel.alertWarning), Toast.LENGTH_SHORT).show()
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            return ""
        }

        // Check if input c is in between min a and max b and
        // returns corresponding boolean
        private fun isInRange(a: Int, b: Int, c: Int): Boolean {
            return if (b > a) c in a..b else c in b..a
        }
    }

    private fun getDateTimeCalendar() {
        TimeZone.getDefault()
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        vYear = p1
        vMonth = p2 + 1
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

        binding.editTextFecha.setText(newDate)
        binding.editTextFecha.clearFocus()
    }
}