/**
 * Created by ITDev by Eduardo Sanchez on 18/2/2022.
 *  www.itdev.com
 */

package com.cbaelectronics.turinpadel.provider.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.Constants.USER

enum class PreferencesKey(val value: String){
    TOKEN("token"),
    AUTH_USER("authUser"),
    MAIL_USER("mailUser"),
    PHOTO_USER("photoUser"),
    TYPE("typeUser"),
    USER_CATEGORY("userCategory"),
    USER_POSITION("userPosition"),
    FIREBASE_AUTH_UID("firebaseAuthUid"),
    ONBOARDING("onboarding"),
    FIRST_SYNC("firstSync"),
    SETTINGS("settings")
}

object PreferencesProvider {

    fun set(context: Context, key: PreferencesKey, value: String) {
        val editor = prefs(context).edit()
        editor.putString(key.value, value).apply()
    }

    fun string(context: Context, key: PreferencesKey): String? {
        return prefs(context).getString(key.value, null)
    }

    fun set(context: Context, key: PreferencesKey, value: Boolean) {
        val editor = prefs(context).edit()
        editor.putBoolean(key.value, value).apply()
    }

    fun int(context: Context, key: PreferencesKey): Int{
        return prefs(context).getInt(key.value, USER)
    }

    fun bool(context: Context, key: PreferencesKey): Boolean? {
        return prefs(context).getBoolean(key.value, false)
    }

    fun remove(context: Context, key: PreferencesKey) {
        val editor = prefs(context).edit()
        editor.remove(key.value).apply()
    }

    // Elimina las SharedPreferences del dominio app
    fun clear(context: Context) {
        val editor = prefs(context).edit()
        editor.clear().apply()
    }

    // Private

    private fun prefs(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

}
