/**
 * Created by CbaElectronics by Eduardo Sanchez on 23/5/22 16:52.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.model.session

import android.content.Context
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesKey
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesProvider
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService

class Session {

    // Initializacion

    companion object {
        val instance = Session()
    }

    // Properties

    var user: User? = null
        private set


    // Life Cycle

    fun configure(context: Context) {

        PreferencesProvider.string(context, PreferencesKey.AUTH_USER)?.let {
            user = User.fromJson(it)
            user?.settings = User.fromJson(it)?.settings
        }

    }


    // Public

    fun save(context: Context, settings: UserSettings){

        val savedSettings = savedSettings(context)

        if (savedSettings != settings) {
            user?.settings = settings
            user?.let { user ->
                save(context, user)
                FirebaseDBService.saveSettings(user)
            }
        }

    }

    fun savedSettings(context: Context): UserSettings?{

        PreferencesProvider.string(context, PreferencesKey.AUTH_USER)?.let {
            return User.fromJson(it)!!.settings
        }

        return null

    }

    // Private

    fun save(context: Context, user: User) {

        PreferencesProvider.set(context, PreferencesKey.AUTH_USER, User.toJson(user))

    }

}