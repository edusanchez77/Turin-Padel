/**
 * Created by ITDev by Eduardo Sanchez on 31/3/2022.
 *  www.itdev.com
 */

package com.cbaelectronics.turinpadel.model.domain

data class PushNotification(
    val data: DatabaseNotifications,
    val to: String
)
