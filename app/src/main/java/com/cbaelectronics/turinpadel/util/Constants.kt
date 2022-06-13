package com.cbaelectronics.turinpadel.util

import java.util.*

object Constants {



    // Generic
    const val TIME_GAME = 90
    const val ADMIN_LOGIN = "eduardosanchez77@gmail.com"

    // Login
    const val LOGIN_GOOGLE = 100

    // Users
    const val TYPE_USER = 0
    const val TYPE_ADMIN = 1
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
    const val TWITIMER_URI = "https://twitimer.com"
    const val TWITCH_MOUREDEV_URI = "https://twitch.tv/mouredev"
    const val DISCORD_MOUREDEV_URI = "https://discord.gg/U3KjjfUfUJ"
    const val YOUTUBE_MOUREDEV_URI = "https://youtube.com/mouredevapps"
    const val TWITTER_MOUREDEV_URI = "https://twitter.com/mouredev"
    const val INSTAGRAM_MOUREDEV_URI = "https://instagram.com/mouredev"
    const val TIKTOK_MOUREDEV_URI = "https://tiktok.com/@mouredev"
    const val GITHUB_MOUREDEV_URI = "https://github.com/mouredev"
    const val DISCORD_URI = "https://discord.gg/"
    const val YOUTUBE_URI = "https://youtube.com/"
    const val TWITTER_URI = "https://twitter.com/"
    const val INSTAGRAM_URI = "https://instagram.com/"
    const val TIKTOK_URI = "https://tiktok.com/@"
}

