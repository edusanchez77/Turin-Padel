/**
 * Created by ITDev by Eduardo Sanchez on 17/3/2022.
 *  www.itdev.com
 */

package com.cbaelectronics.turinpadel.model.domain

import java.util.*

data class Request(
    val requestId: String,
    val usrEmail: String,
    val usrName: String,
    val usrPhoto: String,
    val usrToken: String,
    val usrCategory: String,
    val usrPosition: String,
    val matchId: String,
    val requestDate: Date,
    val matchCreator: String,
    val tokenCreator: String
)
