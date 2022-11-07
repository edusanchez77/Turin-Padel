/**
 * Created by CbaElectronics by Eduardo Sanchez on 21/9/22 16:17.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.model.domain

import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.util.Constants
import com.google.gson.GsonBuilder
import com.itdev.nosfaltauno.util.extension.Weekday
import java.sql.Timestamp
import java.util.*

data class FixedTurn(
    val id: String? = null,
    val curt: String,
    val day: String?,
    val hour: String,
    val status: String = Constants.FIXEDTURN_STATUS_PENDING,
    val order: Int,
    val date: Date? = Timestamp(Date().time),
    val user: User? = null
){

    fun toJSON(): Map<String, Any> {

        val JSON: MutableMap<String, Any> = mutableMapOf(
            DatabaseField.TURN_CURT.key to (curt ?: ""),
            DatabaseField.FIXED_TURN_DAY.key to (day ?: ""),
            DatabaseField.FIXED_TURN_HOUR.key to (hour ?: ""),
            DatabaseField.FIXED_TURN_ORDER.key to (order ?: ""),
            DatabaseField.FIXED_TURN_STATUS.key to (status ?: ""),
            "fixedTurnDate" to (date ?: "")
        )

        JSON[DatabaseField.FIXED_TURN_RESERVED_BY.key] = userToJSON()

        return JSON

    }

    private fun userToJSON(): Map<String, Any> {
        return user?.toJSON() ?: mutableMapOf()
    }


    companion object {

        fun toJson(fixedTurn: FixedTurn): String {
            return GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create().toJson(fixedTurn)
        }

        fun fromJson(json: String): FixedTurn? {
            return GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create().fromJson(json, FixedTurn::class.java)
        }

    }
}

enum class WeekdayType(val index: Int) {

    CUSTOM(0),
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    val nameKey: Int
        get() {
            return when (this) {
                MONDAY -> R.string.days_monday
                TUESDAY -> R.string.days_tuesday
                WEDNESDAY -> R.string.days_wednesday
                THURSDAY -> R.string.days_thursday
                FRIDAY -> R.string.days_friday
                SATURDAY -> R.string.days_saturday
                SUNDAY -> R.string.days_sunday
                else -> R.string.days_sunday
            }
        }


    fun toDateWeekday(): Weekday? {
        return when (this) {
            CUSTOM -> null
            MONDAY -> Weekday.MONDAY
            TUESDAY -> Weekday.TUESDAY
            WEDNESDAY -> Weekday.WEDNESDAY
            THURSDAY -> Weekday.THURSDAY
            FRIDAY -> Weekday.FRIDAY
            SATURDAY -> Weekday.SATURDAY
            SUNDAY -> Weekday.SUNDAY
        }
    }

    companion object {

        fun valueFrom(index: Int): WeekdayType {
            return values().find { it.index == index } ?: CUSTOM
        }
    }

}
