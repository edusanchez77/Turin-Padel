package com.cbaelectronics.turinpadel.usecases.account

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.*
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.Constants.FIXEDTURN_STATUS_CANCEL
import com.cbaelectronics.turinpadel.util.Constants.FIXEDTURN_STATUS_CONFIRM
import com.cbaelectronics.turinpadel.util.Constants.FIXEDTURN_STATUS_DELETED
import com.cbaelectronics.turinpadel.util.Constants.WEEK
import com.itdev.nosfaltauno.util.extension.addDays
import com.itdev.nosfaltauno.util.extension.dayOfWeek
import com.itdev.nosfaltauno.util.extension.hourFixedTurnFormat
import com.itdev.nosfaltauno.util.extension.uppercaseFirst
import kotlinx.coroutines.runBlocking

class AccountViewModel : ViewModel() {

    // Properties

    val user = Session.instance.user ?: User()
    val settings = Session.instance.user?.settings ?: UserSettings()

    // Localization

    val accountUserInfoText = R.string.account_user_schedule
    val cancel = R.string.schedule_dialog_cancel
    val confirm = R.string.schedule_dialog_confirm
    val deleted = R.string.schedule_dialog_deleted

    // Public

    fun loadSchedule(context: Context): LiveData<MutableList<Schedule>> {

        val mutableList = MutableLiveData<MutableList<Schedule>>()

        FirebaseDBService.loadSchedule(context, user).observeForever {
            mutableList.value = it
        }

        return mutableList

    }


    fun cancel(schedule: Schedule) {
        when (schedule.turnType) {
            Constants.TYPE_TURN -> updateTurn(schedule)
            Constants.TYPE_FIXED_TURN -> {
                updateFixedTurn(schedule, FIXEDTURN_STATUS_CANCEL)
                val fixedTurn = createFixedTurn(schedule)
                val id = saveFixedTurn(fixedTurn)
                deleteSchedule(schedule.id)
                saveSchedule(fixedTurn, schedule.user, id)
                saveTurn(schedule)
            }
        }

    }

    fun delete(schedule: Schedule) {
        updateFixedTurn(schedule, FIXEDTURN_STATUS_DELETED)
        deleteSchedule(schedule.id)
        saveTurn(schedule)
    }

    fun confirm(schedule: Schedule) {

        val fixedTurn = createFixedTurn(schedule)
        val id = saveFixedTurn(fixedTurn)
        updateFixedTurn(schedule, FIXEDTURN_STATUS_CONFIRM)
        saveSchedule(fixedTurn, schedule.user, id)

    }


    // Private

    private fun createFixedTurn(schedule: Schedule): FixedTurn {
        return FixedTurn(
            null,
            schedule.curt,
            schedule.date.dayOfWeek().uppercaseFirst(),
            schedule.date.hourFixedTurnFormat(),
            Constants.FIXEDTURN_STATUS_PENDING,
            4, //TODO: Modificar order
            schedule.date.addDays(schedule.date, WEEK),
            schedule.user
        )
    }

    private fun saveTurn(schedule: Schedule) {
        val turn = Turn(curt = schedule.curt, date = schedule.date)
        FirebaseDBService.saveTurn(turn)
    }

    private fun saveFixedTurn(fixedTurn: FixedTurn): String = runBlocking {
        val documentReference = FirebaseDBService.saveFixedTurn(fixedTurn)
        return@runBlocking documentReference.id
    }

    private fun saveSchedule(fixedTurn: FixedTurn, user: User, id: String) {
        FirebaseDBService.saveSchedule(fixedTurn, user, id)
    }

    private fun updateTurn(schedule: Schedule) {
        FirebaseDBService.updateTurn(schedule)
    }

    private fun updateFixedTurn(schedule: Schedule, status: String) {
        FirebaseDBService.updateFixedTurn(schedule, status)
    }

    private fun deleteFixedTurn(id: String) {
        FirebaseDBService.deleteFixedTurn(id)
    }

    private fun deleteSchedule(id: String) {
        FirebaseDBService.deleteSchedule(id)
    }

}