/**
 * Created by CbaElectronics by Eduardo Sanchez on 1/12/22 14:09.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.model.domain

import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.Constants.DEFAULT_COUNT_REQUEST
import com.google.gson.GsonBuilder
import java.util.*

data class MatchDetails(
    val matchId: String,
    val user: User,
    val matchDate: Date,
    val matchGenre: String,
    val matchCategory: String,
    val matchVacantes: Int,
    val matchRequest: Int = DEFAULT_COUNT_REQUEST,
    val player1: User,
    val player2: User,
    val player3: User,
    val player4: User
){
    fun toJSON(): Map<String, Any> {

        val JSON: MutableMap<String, Any> = mutableMapOf(
            DatabaseField.MATCH_ID.key to (matchId ?: ""),
            DatabaseField.MATCH_DATE.key to (matchDate ?: ""),
            DatabaseField.MATCH_VACANTES.key to (matchVacantes ?: ""),
            DatabaseField.MATCH_CATEGORY.key to (matchCategory ?: ""),
            DatabaseField.MATCH_GENRE.key to (matchGenre ?: ""),
            DatabaseField.MATCH_REQUEST.key to (matchRequest ?: ""),
            DatabaseField.MATCH_USER.key to (user ?: ""),
            DatabaseField.PLAYER1.key to (player1 ?: ""),
            DatabaseField.PLAYER2.key to (player2 ?: ""),
            DatabaseField.PLAYER3.key to (player3 ?: ""),
            DatabaseField.PLAYER4.key to (player4 ?: ""),
        )

        return JSON

    }

    companion object {

        fun toJson(matchDetails: MatchDetails): String {
            return GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create().toJson(matchDetails)
        }

        fun fromJson(json: String): MatchDetails? {
            return GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create()
                .fromJson(json, MatchDetails::class.java)
        }

    }
}
