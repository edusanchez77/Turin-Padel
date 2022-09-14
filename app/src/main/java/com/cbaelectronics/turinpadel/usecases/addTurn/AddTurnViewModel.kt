/**
 * Created by CbaElectronics by Eduardo Sanchez on 24/5/22 16:49.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.addTurn

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.Turn
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService

class AddTurnViewModel: ViewModel() {

    // Properties

    var user = Session.instance.user ?: User()
    var settings = Session.instance.user?.settings ?: UserSettings()

    // Localization
    val info = R.string.addTurn_info
    val save = R.string.addTurn_button_save
    val cancel = R.string.addTurn_button_cancel
    val back = R.string.addTurn_button_back
    val alertIncomplete = R.string.addTurn_alert_incomplete
    val alertError = R.string.addTurn_alert_error
    val alertOk = R.string.addTurn_alert_ok
    val alertOutOfTime = R.string.addTurn_alert_outoftime
    val notificationTitle = R.string.notification_topic_newturn_title
    val notificationBody = R.string.notification_topic_newturn_body


    // Public

    fun save(turn: Turn) {
        if(turn.id.isNullOrEmpty()) {
            FirebaseDBService.saveTurn(turn)
        } else{
            FirebaseDBService.updateTurn(turn)
        }






    }


}