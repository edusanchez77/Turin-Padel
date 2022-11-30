/**
 * Created by CbaElectronics by Eduardo Sanchez on 18/11/22 10:29.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.addMatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.Match
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService

class AddMatchViewModel: ViewModel() {

    // Properties

    val user = Session.instance.user ?: User()
    val settings = Session.instance.user?.settings ?: UserSettings()

    // Localization

    val addMatchTitle = R.string.addMatches_title
    val addMatchInfo = R.string.addMatches_info
    val addMatchFechaPlaceholder = R.string.addMatches_editText_fecha
    val addMatchVacantesPlaceholder = R.string.addMatches_editText_vacantes
    val addMatchCategoryPlaceholder = R.string.addMatches_editText_category
    val addMatchGenrePlaceholder = R.string.addMatches_editText_genre
    val addMatchButton = R.string.addMatches_button
    val alertWarning = R.string.addMatches_alert_warning
    val alertOk = R.string.addMatches_alert_ok
    val alertError = R.string.addMatches_alert_error
    val alertIncomplete = R.string.addMatches_alert_incomplete
    val notificationTitle = R.string.notification_topic_newmatch_title
    val notificationBody = R.string.notification_topic_newmatch_body

    // Public

    fun save(match: Match){
        FirebaseDBService.save(match)
    }

}