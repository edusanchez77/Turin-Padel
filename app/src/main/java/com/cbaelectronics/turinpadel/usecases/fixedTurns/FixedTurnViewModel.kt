/**
 * Created by CbaElectronics by Eduardo Sanchez on 14/9/22 11:23.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.fixedTurns

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
import java.sql.Timestamp
import java.util.*

class FixedTurnViewModel: ViewModel() {

    // Properties

    val user = Session.instance.user ?: User()
    val settings = Session.instance.user?.settings ?: UserSettings()

    // Localization

    val title = R.string.fixedTurn_info
    val button = R.string.fixedTurn_button
    val cancel = R.string.schedule_dialog_cancel
    val confirm = R.string.schedule_dialog_confirm
    val deleted = R.string.schedule_dialog_deleted

    // Public

    fun loadFixedTurn(context: Context): LiveData<MutableList<FixedTurn>> {

        val mutableData = MutableLiveData<MutableList<FixedTurn>>()

        val orderToday =  when(Timestamp(Date().time).dayOfWeek().uppercaseFirst()){
            context.getString(WeekdayType.MONDAY.nameKey) -> WeekdayType.MONDAY.index
            context.getString(WeekdayType.TUESDAY.nameKey) -> WeekdayType.TUESDAY.index
            context.getString(WeekdayType.WEDNESDAY.nameKey) -> WeekdayType.WEDNESDAY.index
            context.getString(WeekdayType.THURSDAY.nameKey) -> WeekdayType.THURSDAY.index
            context.getString(WeekdayType.FRIDAY.nameKey) -> WeekdayType.FRIDAY.index
            context.getString(WeekdayType.SATURDAY.nameKey) -> WeekdayType.SATURDAY.index
            context.getString(WeekdayType.SUNDAY.nameKey) -> WeekdayType.SUNDAY.index
            else -> 0
        }

        FirebaseDBService.loadFixedTurn(orderToday).observeForever {
            mutableData.value = it
        }

        return mutableData

    }

    fun cancel(fixedTurn: FixedTurn){
        updateFixedTurn(fixedTurn, FIXEDTURN_STATUS_CANCEL)
        val newFixedTurn = createNewFixedTurn(fixedTurn)
        val fixedTurnId = saveFixedTurn(newFixedTurn)
        val scheduleId = searchScheduleId(fixedTurn.id!!)
        deleteSchedule(scheduleId)
        saveSchedule(newFixedTurn, fixedTurn.user!!, fixedTurnId) //TODO: DeleteSchedule
        saveTurn(fixedTurn)
    }

    fun delete(fixedTurn: FixedTurn){
        updateFixedTurn(fixedTurn, FIXEDTURN_STATUS_DELETED)
        val scheduleId = searchScheduleId(fixedTurn.id!!)
        deleteSchedule(scheduleId)
        saveTurn(fixedTurn)
    }

    fun confirm(fixedTurn: FixedTurn){

        val scheduleId = searchScheduleId(fixedTurn.id!!)
        val newFixedTurn = createNewFixedTurn(fixedTurn)
        val fixedTurnId = saveFixedTurn(newFixedTurn)
        updateFixedTurn(fixedTurn, Constants.FIXEDTURN_STATUS_CONFIRM)
        //saveSchedule(fixedTurn, fixedTurn.user!!, fixedTurnId) //TODO: updateSchedule
        updateSchedule(scheduleId, FIXEDTURN_STATUS_CONFIRM)

    }


    // Private

    private fun createNewFixedTurn(fixedTurn: FixedTurn): FixedTurn {
        return FixedTurn(
            null,
            fixedTurn.curt,
            fixedTurn.date?.dayOfWeek()?.uppercaseFirst(),
            fixedTurn.date!!.hourFixedTurnFormat(),
            Constants.FIXEDTURN_STATUS_PENDING,
            4, //TODO: Modificar order
            fixedTurn.date.addDays(fixedTurn.date, WEEK),
            fixedTurn.user
        )
    }

    private fun saveTurn(fixedTurn: FixedTurn) {
        val turn = Turn(curt = fixedTurn.curt, date = fixedTurn.date!!)
        FirebaseDBService.saveTurn(turn)
    }

    private fun saveFixedTurn(fixedTurn: FixedTurn): String = runBlocking {
        val documentReference = FirebaseDBService.saveFixedTurn(fixedTurn)
        return@runBlocking documentReference.id
    }

    private fun updateFixedTurn(fixedTurn: FixedTurn, status: String){
        FirebaseDBService.updateFixedTurn(fixedTurn, status)
    }

    private fun saveSchedule(fixedTurn: FixedTurn, user: User, id: String) {
        FirebaseDBService.saveSchedule(fixedTurn, user, id)
    }

    private fun deleteSchedule(id: String) {
        FirebaseDBService.deleteSchedule(id)
    }

    private fun searchScheduleId(fixedTurnId: String): String = runBlocking {
        val documentReference = FirebaseDBService.searchScheduleId(fixedTurnId)
        documentReference?.documents?.map { doc ->
            return@runBlocking doc.id
        }.toString()
    }

    private fun updateSchedule(id: String, status: String){
        FirebaseDBService.updateSchedule(id, status)
    }

}