/**
 * Created by ITDev by Eduardo Sanchez on 5/3/2022.
 *  www.itdev.com
 */

package com.cbaelectronics.turinpadel.model.domain

import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.util.Constants
import com.google.gson.GsonBuilder
import java.util.*

data class Match(
    val id: String? = null,
    val date: Date,
    val vacantes: Int,
    val category: String,
    val genre: String,
    val user: User
){
    fun toJSON(): Map<String, Any> {

        val JSON: MutableMap<String, Any> = mutableMapOf(
            DatabaseField.MATCH_DATE.key to (date ?: ""),
            DatabaseField.MATCH_VACANTES.key to (vacantes ?: ""),
            DatabaseField.MATCH_CATEGORY.key to (category ?: ""),
            DatabaseField.MATCH_GENRE.key to (genre ?: ""),
            DatabaseField.MATCH_USER.key to (user ?: "")
        )

        return JSON

    }

    companion object {

        fun toJson(match: Match): String {
            return GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create().toJson(match)
        }

        fun fromJson(json: String): Match? {
            return GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create()
                .fromJson(json, Match::class.java)
        }

    }
}
