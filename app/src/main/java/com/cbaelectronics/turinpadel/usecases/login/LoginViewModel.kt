/**
 * Created by CbaElectronics by Eduardo Sanchez on 10/5/22 15:13.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class LoginViewModel : ViewModel() {

    // Properties

    var user = Session.instance.user ?: User()
    var settings = Session.instance.user?.settings ?: UserSettings()

    // Localization

    val titleText = R.string.login_title
    val bodyText = R.string.login_body
    val buttonText = R.string.login_button
    val alertWait = R.string.login_alert_wait
    val alertErrorUser = R.string.login_alert_error_user
    val errorToken = R.string.login_alert_error_token


    // Public

    fun saveUser(user: User) {

        FirebaseDBService.save(user)

    }

    fun save(context: Context) {

        Session.instance.save(context, user)

    }

    fun load(email: String) = runBlocking {

        var usr: User?
        var userSettings: UserSettings?
        async {
            val documentSnapshot = FirebaseDBService.load(email)
            if (documentSnapshot != null) {
                if (documentSnapshot.exists()) {
                    usr = User(
                        documentSnapshot.getString(DatabaseField.DISPLAY_NAME.key),
                        documentSnapshot.getString(DatabaseField.EMAIL.key),
                        documentSnapshot.getString(DatabaseField.PROFILE_IMAGE_URL.key),
                        documentSnapshot.getString(DatabaseField.TOKEN.key),
                        documentSnapshot.getLong(DatabaseField.TYPE.key)?.toInt(),
                        documentSnapshot.getDate(DatabaseField.REGISTER_DATE.key)
                    )
                    userSettings = UserSettings(
                        documentSnapshot.getString("${DatabaseField.SETTINGS.key}.${DatabaseField.PROFILE_CATEGORY.key}").toString(),
                        documentSnapshot.getString("${DatabaseField.SETTINGS.key}.${DatabaseField.PROFILE_POSITION.key}").toString(),
                        documentSnapshot.getBoolean("${DatabaseField.SETTINGS.key}.${DatabaseField.NOTIFICATION_TURN.key}")!!,
                        documentSnapshot.getBoolean("${DatabaseField.SETTINGS.key}.${DatabaseField.NOTIFICATION_POST.key}")!!
                    )

                    user = usr as User
                    user.settings = userSettings

                }
            }

        }

    }

    fun configure(context: Context) {

        Session.instance.configure(context)

    }

}