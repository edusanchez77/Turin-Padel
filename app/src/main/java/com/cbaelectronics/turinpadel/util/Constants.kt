package com.cbaelectronics.turinpadel.util

import java.util.*

object Constants {



    // Generic
    const val TIME_GAME = 90
    const val ADMIN_LOGIN = "eduardosanchez77@gmail.com"

    // Login
    const val LOGIN_GOOGLE = 100

    // Users
    const val USER = 0
    const val CREATOR = 1
    const val ADMIN = 2

    // Settings
    const val CATEGORY_DEFAULT = "No definida"
    const val POSITION_DEFAULT = "No definida"
    const val NOTIFICATION_TURN_DEFAULT = true
    const val NOTIFICATION_POST_DEFAULT = true

    // Maps
    const val REQUEST_CODE_LOCATION = 0
    const val REQUEST_CODE_ADDRESS_OK = 0
    const val REQUEST_CODE_ADDRESS_CANCEL = 1

    // Turns
    const val STATUS_DEFAULT = 0
    const val STATUS_RESERVED = 1
    const val STATUS_OUTOFTIME = 2
    const val TIME_UNTIL_CANCEL = 1
    const val STATUS_AVAILABLE_YES = "Disponible"
    const val STATUS_AVAILABLE_NO = "Reservado"

    // Grandtable
    const val DEFAULT_COUNT_COMMENTS = 0

    // Request
    const val REQUEST_STATUS_PENDING = "P"
    const val REQUEST_STATUS_ACCEPTED = "A"
    const val REQUEST_STATUS_DENY = "N"

    // Remote notifications
    const val TOPIC_PATH = "/topics/"
    const val NEW_COMMENT = "/topics/"
    const val NEW_MATCH = "/topics/newMatch"
    const val NEW_TURN = "/topics/newTurn"
    const val NEW_POST = "/topics/newPost"

    // Retrofit


    // Locale
    val DEFAULT_LOCALE = Locale( "en_US_POSIX")
    const val JSON_DATE_FORMAT = "MMM d, yyyy HH:mm:ss"
    const val TURN_DATE_FORMAT = "dd/MM/yyyy HH:mm"

    // Networks
    const val TURINPADEL_URI = "https://turinpadel.com.ar"
    const val TWITTER_CBAELECTRONICS_URI = "https://twitter.com/cba_electronics"
    const val INSTAGRAM_CBAELECTRONICS_URI = "https://instagram.com/cbaelectronics"
    const val FACEBOOK_CBAELECTRONICS_URI = "https://facebook.com/cbaelectronics"
    const val TWITTER_URI = "https://twitter.com/"
    const val INSTAGRAM_URI = "https://instagram.com/"
    const val FACEBOOK_URI = "https://facebook.com/"

}

