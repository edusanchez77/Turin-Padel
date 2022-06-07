/**
 * Created by CbaElectronics by Eduardo Sanchez on 24/5/22 16:49.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.addTurn

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


    // Public

    fun save(turn: Turn) {
        FirebaseDBService.saveTurn(turn)
    }

}