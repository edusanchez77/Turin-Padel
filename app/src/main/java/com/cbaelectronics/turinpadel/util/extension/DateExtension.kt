package com.itdev.nosfaltauno.util.extension

import android.content.Context
import android.util.Log
import com.cbaelectronics.turinpadel.model.domain.WeekdayType
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.Util.getOrder
import com.google.type.DayOfWeek
import java.sql.Timestamp
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

fun Date.dayOfWeek(): String{
    val formatter = SimpleDateFormat("EEEE", Locale.getDefault())
    return formatter.format(this)
}

fun Date.hourFixedTurnFormat(): String {

    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(this)

}

fun Date.hourFixedTurnFormatSchedule(): String {

    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return "${formatter.format(this)}hs."

}

fun Date.addDays(fecha: Date?, dias: Int): Date? {
    if (dias == 0) return fecha
    val calendar = Calendar.getInstance()
    calendar.time = fecha
    calendar.add(Calendar.DAY_OF_YEAR, dias)
    return calendar.time
}

fun Date.addDays(context: Context, order: Int, hour: String): String{

    val orderToday =  getOrder(context, Date().dayOfWeek())
    val duration = if (order == orderToday) 0 else order - orderToday
    val day = Timestamp(Date().time+ (1000 * 60 * 60 * (1 ?: 0) * (24 * duration)))

    return if (day < Timestamp(Date().time)) {
        Timestamp(Date().time+ (1000 * 60 * 60 * (1 ?: 0) * (24 * (7 + duration)))).calendarDate()
    }else{
        day.calendarDate()
    }

}

fun Date.addOrRemoveDays(count: Int, flagStart: Boolean): Date?{
    return if(flagStart)
        Timestamp(Date().time + (1000 * 60 * 60 * (1 ?: 0) * (24 * (-(count-1))))).calendarDate().toDate()
    else
        Timestamp(Date().time + (1000 * 60 * 60 * (1 ?: 0) * (24 * (8 - count)))).calendarDate().toDate()
}



fun Date.getWeekDayType(originalWeekDay: Int?): WeekdayType {

    if (originalWeekDay != null && originalWeekDay != WeekdayType.CUSTOM.index) {
        return weekdayType()
    }
    return WeekdayType.CUSTOM
}


enum class Weekday(val spanishName: String) {

    MONDAY("lun"),
    TUESDAY("mar"),
    WEDNESDAY("mie"),
    THURSDAY("jue"),
    FRIDAY("vie"),
    SATURDAY("sab"),
    SUNDAY("dom");

    fun dayOfWeek(): DayOfWeek {
        return when (this) {
            MONDAY -> DayOfWeek.MONDAY
            TUESDAY -> DayOfWeek.TUESDAY
            WEDNESDAY -> DayOfWeek.WEDNESDAY
            THURSDAY -> DayOfWeek.THURSDAY
            FRIDAY -> DayOfWeek.FRIDAY
            SATURDAY -> DayOfWeek.SATURDAY
            SUNDAY -> DayOfWeek.SUNDAY
        }
    }

}

fun Date.weekdayType(): WeekdayType {

    val calendar = GregorianCalendar(Constants.DEFAULT_LOCALE)
    calendar.time = this
    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        1 -> WeekdayType.SUNDAY
        2 -> WeekdayType.MONDAY
        3 -> WeekdayType.TUESDAY
        4 -> WeekdayType.WEDNESDAY
        5 -> WeekdayType.THURSDAY
        6 -> WeekdayType.FRIDAY
        7 -> WeekdayType.SATURDAY
        else -> WeekdayType.CUSTOM
    }
}






