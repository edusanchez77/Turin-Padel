/**
 * Created by ITDev by Eduardo Sanchez on 8/4/2022.
 *  www.itdev.com
 */

package com.cbaelectronics.turinpadel.model.domain

import android.util.Log
import com.google.gson.GsonBuilder
import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.util.Constants
import org.json.JSONException
import java.sql.Timestamp
import java.util.*

data class User(
    var displayName: String? = null,
    var email: String? = null,
    var photoProfile: String? = null,
    var token: String? = null,
    var type: Int? = null,
    val registerDate: Date? = Timestamp(Date().time)
){

    var settings: UserSettings? = null

    fun toJSON(): Map<String, Any> {

        val JSON: MutableMap<String, Any> = mutableMapOf(
            DatabaseField.EMAIL.key to (email ?: ""),
            DatabaseField.DISPLAY_NAME.key to (displayName ?: ""),
            DatabaseField.PROFILE_IMAGE_URL.key to (photoProfile ?: ""),
            DatabaseField.TOKEN.key to (token ?: ""),
            DatabaseField.TYPE.key to (type ?: ""),

            DatabaseField.REGISTER_DATE.key to (registerDate ?: "")
        )

        JSON[DatabaseField.SETTINGS.key] = settingsToJSON()

        return JSON
    }


    private fun settingsToJSON(): MutableMap<String, Any> {
        return settings?.toJSON() ?: mutableMapOf()
    }

    companion object {

        fun toJson(user: User): String {
            return GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create().toJson(user)
        }

        fun fromJson(json: String): User? {
            return GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create().fromJson(json, User::class.java)
        }

    }

}

data class UserSettings(
    var category: String = Constants.CATEGORY_DEFAULT,
    var position: String = Constants.POSITION_DEFAULT,
    var notificationTurn: Boolean = Constants.NOTIFICATION_TURN_DEFAULT,
    var notificationPost: Boolean = Constants.NOTIFICATION_POST_DEFAULT
){
    fun toJSON() : MutableMap<String, Any>{

        return mutableMapOf(
            DatabaseField.PROFILE_CATEGORY.key to category,
            DatabaseField.PROFILE_POSITION.key to position,
            DatabaseField.NOTIFICATION_TURN.key to notificationTurn,
            DatabaseField.NOTIFICATION_POST.key to notificationPost
        )

    }

}