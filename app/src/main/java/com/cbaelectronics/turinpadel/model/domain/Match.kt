/**
 * Created by ITDev by Eduardo Sanchez on 5/3/2022.
 *  www.itdev.com
 */

package com.cbaelectronics.turinpadel.model.domain

import java.util.*

data class Match(
    val usrEmail: String,
    val usrName: String,
    val usrPhoto: String,
    val usrToken: String,
    val usrCategory: String,
    val usrPosition: String,
    val matchId: String,
    val matchDate: Date,
    val matchVacantes: Int,
    val matchCategory: String?,
    val matchGenre: String?,
)
