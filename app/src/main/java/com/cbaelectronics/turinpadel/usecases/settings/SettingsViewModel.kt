/**
 * Created by CbaElectronics by Eduardo Sanchez on 17/5/22 16:35.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesProvider
import com.cbaelectronics.turinpadel.usecases.login.LoginRouter

class SettingsViewModel: ViewModel() {

    // Properties

    val user = Session.instance.user ?: User()
    val settings = Session.instance.user?.settings ?: UserSettings()

    // Localization

    val professional = R.string.settings_info_professional
    val notifications = R.string.settings_info_notifications
    val category = R.string.settings_category
    val position = R.string.settings_position
    val closeSession = R.string.settings_button_cerrarSession
    val saveSettings = R.string.settings_button_save
    val alertLogout = R.string.settings_alert_logout
    val alertButtonOk = R.string.settings_alert_button_ok
    val alertButtonCancel = R.string.settings_alert_button_cancel
    val messageOk = R.string.settings_message_update_ok
    val messageError = R.string.settings_message_update_error
    val notificationTurn = R.string.settings_notification_turn
    val notificationPost = R.string.settings_notification_post

    // Public

    fun close(context: SettingsActivity) {
        PreferencesProvider.clear(context)
        LoginRouter().launch(context)
    }


    fun enableSave(context: Context) : Boolean {
        return Session.instance.savedSettings(context) != settings
    }


    fun save(context: Context) {
        Session.instance.save(context, settings)
    }

}