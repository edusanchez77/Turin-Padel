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
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ActivityAddTurnBinding
import com.cbaelectronics.turinpadel.model.domain.Turn
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesKey
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesProvider
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.Constants.TURN_DATE_FORMAT
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.cbaelectronics.turinpadel.util.UIUtil
import com.cbaelectronics.turinpadel.util.UIUtil.pushNotification
import com.cbaelectronics.turinpadel.util.notifications.Constants.Companion.TYPE_TURN
import com.itdev.nosfaltauno.util.extension.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class AddTurnActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityAddTurnBinding
    private lateinit var viewModel: AddTurnViewModel

    private var dateEditText: String? = null
    private var curtEditText: String? = null

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
        binding.buttonCancelTurn.text = getString(viewModel.back)
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
            if (hasFocus) {
                hideSoftInput()
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

        // Curts

        val arrayCanchas = resources.getStringArray(R.array.strCanchas)
        val adapterCanchas = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            arrayCanchas
        )
        binding.etCanchaTurnoLibre.setAdapter(adapterCanchas)

        setupInfo()

    }


    private fun setupInfo() {

        // Date

        binding.etHorarioTurnoLibre.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                dateEditText = binding.etHorarioTurnoLibre.text.toString()
                checkEnableSave()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Do nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                // Do nothing
            }
        })

        // Curt

        binding.etCanchaTurnoLibre.setOnItemClickListener { adapterView, view, i, l ->
            hideSoftInput()
            curtEditText = binding.etCanchaTurnoLibre.text.toString()
            checkEnableSave()
        }

    }

    private fun checkEnableSave() {
        if (!dateEditText.isNullOrEmpty() && !curtEditText.isNullOrEmpty()) {
            binding.buttonSaveTurn.enable(true)
            binding.buttonCancelTurn.text = getString(viewModel.cancel)
        }
    }

    private fun disableSave() {
        binding.buttonSaveTurn.enable(false)
        binding.buttonCancelTurn.text = getString(viewModel.back)
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

        binding.etHorarioTurnoLibre.setText(newDate)
    }


    @SuppressLint("SimpleDateFormat")
    private fun footer() {

        // Button Cancel

        binding.buttonCancelTurn.setOnClickListener {
            onBackPressed()
        }

        // Button Save

        binding.buttonSaveTurn.enable(false)
        binding.buttonSaveTurn.setOnClickListener {

            val alertOk = getString(viewModel.alertOk)
            val alertIncomplete = getString(viewModel.alertIncomplete)
            val alertOutOfTime = getString(viewModel.alertOutOfTime)

            // Save DataBase

            val date = binding.etHorarioTurnoLibre.text.toString()
            val curt = binding.etCanchaTurnoLibre.text.toString()

            if (date.isNotEmpty() && curt.isNotEmpty()) {

                val sdf = SimpleDateFormat(TURN_DATE_FORMAT)
                val date1 = sdf.parse(date)

                val turn = Turn(curt = curt, date = date1)
                viewModel.save(turn)

                createNotification()
                clearEditText()
                showAlert(alertOk)
                disableSave()

            } else {
                showAlert(alertIncomplete)

            }
        }
    }

    private fun createNotification() {
        val title = getString(viewModel.notificationTitle)
        val body = getString(viewModel.notificationBody)
        val type = TYPE_TURN
        val user = PreferencesProvider.string(binding.root.context, PreferencesKey.AUTH_USER).toString()

        pushNotification(title, body, type, user)
    }

    private fun clearEditText() {
        binding.etHorarioTurnoLibre.text = null
        binding.etCanchaTurnoLibre.text = null
    }

    private fun showAlert(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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