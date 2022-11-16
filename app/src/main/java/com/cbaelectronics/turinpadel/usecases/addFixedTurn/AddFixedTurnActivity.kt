package com.cbaelectronics.turinpadel.usecases.addFixedTurn

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.databinding.ActivityAddFixedTurnBinding
import com.cbaelectronics.turinpadel.model.domain.FixedTurn
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.WeekdayType
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesKey
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesProvider
import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.usecases.SearchUser.SearchUserActivity
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.Constants.FIXEDTURN_STATUS_PENDING
import com.cbaelectronics.turinpadel.util.Constants.NEW_FIXED_TURN
import com.cbaelectronics.turinpadel.util.Constants.TYPE_FIXED_TURN
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType
import com.cbaelectronics.turinpadel.util.UIUtil
import com.itdev.nosfaltauno.util.extension.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class AddFixedTurnActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityAddFixedTurnBinding
    private lateinit var viewModel: AddFixedTurnViewModel
    private var day: String? = null
    private var dateEditText: String? = null
    private var curtEditText: String? = null
    private var userEditText: String? = null
    private var bundle: Bundle? = null
    private var user:User? = null
    //private var token: String? = null

    private val responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult ->
        if (activityResult.resultCode == RESULT_OK){
            val userName = activityResult.data?.getStringExtra(DatabaseField.DISPLAY_NAME.key).orEmpty()
            val userEmail = activityResult.data?.getStringExtra(DatabaseField.EMAIL.key).orEmpty()
            val userPhoto = activityResult.data?.getStringExtra(DatabaseField.PROFILE_IMAGE_URL.key).orEmpty()
            val token = activityResult.data?.getStringExtra(DatabaseField.TOKEN.key).orEmpty()
            val registerDate = activityResult.data?.getStringExtra(DatabaseField.REGISTER_DATE.key).orEmpty()
            val userType = activityResult.data?.getStringExtra(DatabaseField.TYPE.key).orEmpty()

            user = User(userName, userEmail, userPhoto, token, userType.toInt(), registerDate.parseFirebase())
            binding.etReservedBy.setText(user?.displayName)
        }
    }

    var hour = 0
    var minute = 0

    var vHour = 0
    var vMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFixedTurnBinding.inflate(layoutInflater)
        // Content
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this).get(AddFixedTurnViewModel::class.java)

        // Setup
        localize()
        setup()
        footer()
    }

    private fun localize() {
        binding.textViewAddFixedTurn.text = getString(viewModel.info)
        binding.textViewAddFixedTurnDay.text = getString(viewModel.day)
        binding.textViewAddFixedTurnHour.text = getString(viewModel.hour)
        binding.textViewAddFixedTurnCurt.text = getString(viewModel.curt)
        binding.textViewAddFixedTurnReservedBy.text = getString(viewModel.reservedBy)
        binding.buttonSaveTurn.text = getString(viewModel.save)
        binding.buttonCancelTurn.text = getString(viewModel.back)
    }

    private fun setup() {
        addClose(this)

        // UI
        binding.textViewAddFixedTurn.font(
            FontSize.BODY,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        binding.textViewAddFixedTurnDay.font(
            FontSize.BUTTON,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        binding.textViewAddFixedTurnHour.font(
            FontSize.BUTTON,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        binding.textViewAddFixedTurnCurt.font(
            FontSize.BUTTON,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        binding.textViewAddFixedTurnReservedBy.font(
            FontSize.BUTTON,
            FontType.REGULAR,
            ContextCompat.getColor(binding.root.context, R.color.text)
        )

        // Date and Time

        binding.etHorarioTurnoLibre.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                hideSoftInput()
                getDateTimeCalendar()
                TimePickerDialog(this, R.style.themeOnverlay_timePicker, this, hour, minute, true).show()
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

        // User

        binding.etReservedBy.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus){
                hideSoftInput()
                val intent = Intent(this, SearchUserActivity::class.java)
                responseLauncher.launch(intent)
            }
        }

        setupInfo()
    }

    private fun setupInfo() {

        // Day
        binding.selectDay.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener{
                group, checkedId ->
                val radio_langange: RadioButton = findViewById(checkedId)
                day = when (radio_langange.id){
                    R.id.radioButtonLunes -> getString(WeekdayType.MONDAY.nameKey).uppercaseFirst()
                    R.id.radioButtonMartes -> getString(WeekdayType.TUESDAY.nameKey).uppercaseFirst()
                    R.id.radioButtonMiercoles -> getString(WeekdayType.WEDNESDAY.nameKey).uppercaseFirst()
                    R.id.radioButtonJueves -> getString(WeekdayType.THURSDAY.nameKey).uppercaseFirst()
                    R.id.radioButtonViernes -> getString(WeekdayType.FRIDAY.nameKey).uppercaseFirst()
                    R.id.radioButtonSabado -> getString(WeekdayType.SATURDAY.nameKey).uppercaseFirst()
                    R.id.radioButtonDomingo -> getString(WeekdayType.SUNDAY.nameKey).uppercaseFirst()
                    else -> null
                }
                checkEnable()
            }
        )

        // Date

        binding.etHorarioTurnoLibre.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                dateEditText = binding.etHorarioTurnoLibre.text.toString()
                checkEnable()
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
            checkEnable()
        }

        // User

        binding.etReservedBy.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userEditText = binding.etReservedBy.text.toString()
                checkEnable()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Do nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                // Do nothing
            }
        })

    }

    private fun checkEnable() {
        if(bundle?.isEmpty == false){

            val dateTurn = bundle?.get(DatabaseField.TURN_DATE.key).toString().parseFirebase().customShortFormat()
            val curtTurn = bundle?.getString(DatabaseField.TURN_CURT.key).toString()

            if(curtTurn != curtEditText || dateTurn != dateEditText){
                enableSave()
            }else{
                disableSave()
            }

        }else{

            if (!day.isNullOrEmpty() && !dateEditText.isNullOrEmpty() && !curtEditText.isNullOrEmpty() && !userEditText.isNullOrEmpty())  {
                enableSave()
            }

        }

    }

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

            if (day!!.isNotEmpty() && dateEditText!!.isNotEmpty() && curtEditText!!.isNotEmpty() && userEditText!!.isNotEmpty()) {

                val hour = binding.etHorarioTurnoLibre.text.toString()
                val curt = binding.etCanchaTurnoLibre.text.toString()
                val order =  when(day){
                    getString(WeekdayType.MONDAY.nameKey) -> WeekdayType.MONDAY.index
                    getString(WeekdayType.TUESDAY.nameKey) -> WeekdayType.TUESDAY.index
                    getString(WeekdayType.WEDNESDAY.nameKey) -> WeekdayType.WEDNESDAY.index
                    getString(WeekdayType.THURSDAY.nameKey) -> WeekdayType.THURSDAY.index
                    getString(WeekdayType.FRIDAY.nameKey) -> WeekdayType.FRIDAY.index
                    getString(WeekdayType.SATURDAY.nameKey) -> WeekdayType.SATURDAY.index
                    getString(WeekdayType.SUNDAY.nameKey) -> WeekdayType.SUNDAY.index
                    else -> 0
                }

                val date = Timestamp(Date().time).addDays(binding.root.context, order, hour)
                val date1 = "$date $hour"
                val sdf = SimpleDateFormat(Constants.TURN_DATE_FORMAT)
                val turnDate = sdf.parse(date1)

                val fixedTurn = FixedTurn(curt = curt, day = day.toString(), hour = hour, status = FIXEDTURN_STATUS_PENDING, order = order, date = turnDate, user = user)
                saveFixedTurn(fixedTurn, user!!)

                createNotification()
                subscribeTopic()
                clearEditText()
                showAlert(alertOk)
                bundle?.clear()
                disableSave()

            } else {
                showAlert(alertIncomplete)
            }
        }
    }

    private fun saveFixedTurn(fixedTurn: FixedTurn, user: User) = runBlocking {

        withContext(Dispatchers.Default) {
            viewModel.saveFixedTurn(fixedTurn, user)
        }

        //TODO saveSchedule

    }

    private fun subscribeTopic() {
        Session.instance.setupNotification(true, NEW_FIXED_TURN)
    }

    private fun disableSave() {
        binding.buttonSaveTurn.enable(false)
        binding.buttonCancelTurn.text = getString(viewModel.back)
    }

    private fun enableSave() {
        binding.buttonSaveTurn.enable(true)
        binding.buttonCancelTurn.text = getString(viewModel.cancel)
    }

    private fun createNotification() {
        val title = getString(viewModel.notificationTitle)
        val body = getString(viewModel.notificationBody)
        val type = TYPE_FIXED_TURN
        val user = user
            //PreferencesProvider.string(binding.root.context, PreferencesKey.AUTH_USER).toString()
        Log.d("NotificationFixedTurn Token", user?.token.toString())
        UIUtil.pushNotification(title, body, user?.token.toString(), user?.toJSON().toString())
    }



    private fun clearEditText() {
        binding.etHorarioTurnoLibre.text = null
        binding.etCanchaTurnoLibre.text = null
        binding.etReservedBy.text = null
    }

    private fun showAlert(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getDateTimeCalendar() {

        TimeZone.getDefault()
        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)

    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        vHour = p1
        vMinute = p2

        val vFecha = "$vHour:$vMinute"
        val sdf = SimpleDateFormat("HH:mm")
        val date = sdf.parse(vFecha)
        val newDate = sdf.format(date)

        binding.etHorarioTurnoLibre.setText(newDate)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

}