/**
 * Created by CbaElectronics by Eduardo Sanchez on 20/9/22 12:45.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.addFixedTurn

import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.FixedTurn
import com.cbaelectronics.turinpadel.model.domain.Turn
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class AddFixedTurnViewModel: ViewModel() {

    // Properties

    var user = Session.instance.user ?: User()
    var settings = Session.instance.user?.settings ?: UserSettings()

    // Localization
    val info = R.string.addFixedTurn_info
    val day = R.string.addFixedTurn_data_day
    val hour = R.string.addFixedTurn_data_hour
    val curt = R.string.addFixedTurn_data_curt
    val reservedBy = R.string.addFixedTurn_data_reservedBy
    val save = R.string.addTurn_button_save
    val cancel = R.string.addTurn_button_cancel
    val back = R.string.addTurn_button_back
    val alertIncomplete = R.string.addTurn_alert_incomplete
    val alertError = R.string.addTurn_alert_error
    val alertOk = R.string.addTurn_alert_ok
    val alertOutOfTime = R.string.addTurn_alert_outoftime
    val notificationTitle = R.string.notification_topic_newFixedTurn_title
    val notificationBody = R.string.notification_topic_newFixedTurn_body


    // Public

    fun saveFixedTurn(fixedTurn: FixedTurn, user: User) = runBlocking {
        val documentReference = FirebaseDBService.saveFixedTurn(fixedTurn)
        val id = documentReference.id

        saveSchedule(fixedTurn, user, id)
    }

    // Private

    private fun saveSchedule(fixedTurn: FixedTurn, user: User, id: String){

        FirebaseDBService.saveSchedule(fixedTurn, user, id)

    }

}