/**
 * Created by CbaElectronics by Eduardo Sanchez on 31/5/22 12:20.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.model.domain

import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.util.Constants
import com.google.gson.GsonBuilder
import java.util.*

data class Schedule(
    val id: String,
    val turn: String,
    val curt: String,
    val date: Date,
    val user: User,
    val turnType: String,
    val status: String?,
    val day: String?
) {
    fun toJSON(): Map<String, Any> {

        val JSON: MutableMap<String, Any> = mutableMapOf(
            DatabaseField.SCHEDULE_ID.key to (id ?: ""),
            DatabaseField.TURN_ID.key to (turn ?: ""),
            DatabaseField.TURN_CURT.key to (curt ?: ""),
            DatabaseField.TURN_DATE.key to (date ?: ""),
            DatabaseField.FIXED_TURN_STATUS.key to (status ?: ""),
            DatabaseField.SCHEDULE_DAY.key to (day ?: ""),

            DatabaseField.DISPLAY_NAME.key to (user.displayName ?: ""),
            DatabaseField.EMAIL.key to (user.email ?: ""),
            DatabaseField.PROFILE_IMAGE_URL.key to (user.photoProfile ?: ""),
            DatabaseField.TOKEN.key to (user.token ?: "")
        )

        return JSON

    }

    companion object {

        fun toJson(turn: Turn): String {
            return GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create().toJson(turn)
        }

        fun fromJson(json: String): Turn? {
            return GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create()
                .fromJson(json, Turn::class.java)
        }

    }
}
