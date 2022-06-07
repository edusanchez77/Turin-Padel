/**
 * Created by CbaElectronics by Eduardo Sanchez on 26/5/22 14:40.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.model.domain

import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.util.Constants
import com.google.gson.GsonBuilder
import java.util.*

data class Turn(
    val id: String? = null,
    val curt: String,
    val date: Date,
    val status: Int = Constants.DEFAULT_STATUS
){
    fun toJSON(): Map<String, Any> {

        val JSON: MutableMap<String, Any> = mutableMapOf(
            DatabaseField.TURN_CURT.key to (curt ?: ""),
            DatabaseField.TURN_DATE.key to (date ?: ""),
            DatabaseField.TURN_STATUS.key to (status ?: "")
        )

        return JSON

    }

    companion object {

        fun toJson(turn: Turn): String {
            return GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create().toJson(turn)
        }

        fun fromJson(json: String): Turn? {
            return GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create().fromJson(json, Turn::class.java)
        }

    }
}
