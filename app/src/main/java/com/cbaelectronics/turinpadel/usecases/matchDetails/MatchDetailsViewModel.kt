/**
 * Created by CbaElectronics by Eduardo Sanchez on 30/11/22 14:22.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.matchDetails

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.MatchDetails
import com.cbaelectronics.turinpadel.model.domain.Post
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService

class MatchDetailsViewModel: ViewModel() {

    // Properties

    val user = Session.instance.user ?: User()
    val settings = Session.instance.user?.settings ?: UserSettings()

    // Localization

    val team1 = R.string.matchDetails_team_1
    val team2 = R.string.matchDetails_team_2
    val available = R.string.matchDetails_player_available_yes
    val noAvailable = R.string.matchDetails_player_available_no
    val iconAvailable = R.drawable.available
    val iconNoAvailable = R.drawable.user
    val button = R.string.matchDetails_button
    val requestTitle = R.string.matchDetails_request_title
    val requestBody = R.string.matchDetails_request_body
    val requestButtonOk = R.string.matchDetails_request_button_ok
    val requestButtonCancel = R.string.matchDetails_request_button_cancel
    val requestAlertOkTitle = R.string.matchDetails_request_alert_ok_title
    val requestAlertOkBody = R.string.matchDetails_request_alert_ok_body
    val requestAlertError = R.string.matchDetails_request_alert_error
    val notificationTitle = R.string.notification_topic_match_request_title
    val notificationBody = R.string.notification_topic_match_request_body

    // Public

    fun load(id: String): LiveData<MutableList<MatchDetails>>{
        var mutableList = MutableLiveData<MutableList<MatchDetails>>()

        FirebaseDBService.loadMatchDetails(id).observeForever {
            mutableList.value = it
        }

        return mutableList
    }

    fun vacantesText(context: Context, vacantes: Int): String {

        return when (vacantes) {
            0 -> context.getString(R.string.matchDetails_vacantes_completed)
            1 -> context.getString(R.string.matchDetails_vacantes_1)
            else -> context.getString(R.string.matchDetails_vacantes_n, vacantes)
        }

    }

    fun iconAvailable(context: Context): Drawable? {
        return ContextCompat.getDrawable(context, R.drawable.available)
    }

    fun iconNoAvailable(context: Context): Drawable? {
        return ContextCompat.getDrawable(context, R.drawable.user)
    }

}