/**
 * Created by ITDev by Eduardo Sanchez on 31/3/2022.
 *  www.itdev.com
 */

package com.cbaelectronics.turinpadel.model.domain

data class DatabaseNotifications(
    val title: String,
    val body: String,
    val type: String,
    val user: String? = null,
    val id: String? = null,
    val flag: String? = null
)
