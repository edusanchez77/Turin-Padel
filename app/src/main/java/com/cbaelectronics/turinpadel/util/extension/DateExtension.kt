package com.itdev.nosfaltauno.util.extension

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by MoureDev by Brais Moure on 5/10/21.
 * www.mouredev.com
 */

fun Date.toJSON(): String {
    val formatter = SimpleDateFormat("EEEE, dd 'de' MMMM, HH:mm", Locale.getDefault())
    return formatter.format(this)
}

fun Date.calendarDate() : String{
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(this)
}

fun Date.longFormat(): String {
    return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale.getDefault()).format(this).uppercaseFirst()
}

fun Date.mediumFormat(): String {
    return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault()).format(this).uppercaseFirst()
}

fun Date.shortFormat(): String {
    return DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(this).format(this).uppercaseFirst()
}

fun Date.customShortFormat(): String {

    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formatter.format(this)
}






