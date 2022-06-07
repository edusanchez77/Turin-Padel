/**
 * Created by CbaElectronics by Eduardo Sanchez on 2/6/22 09:02.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.model.domain

import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.util.Constants
import com.google.gson.GsonBuilder
import java.sql.Timestamp
import java.util.*

data class Comment(
    val id: String? = null,
    val post: String,
    val message: String,
    val date: Date = Timestamp(Date().time),
    val user: User
){
    fun toJSON(): Map<String, Any> {

        val JSON: MutableMap<String, Any> = mutableMapOf(
            DatabaseField.POST_ID.key to (post ?: ""),
            DatabaseField.COMMENT_MESSAGE.key to (message ?: ""),
            DatabaseField.COMMENT_ADD_DATE.key to (date ?: ""),
            DatabaseField.COMMENT_WRITER.key to (user ?: ""),
        )

        return JSON

    }

    companion object {

        fun toJson(comment: Comment): String {
            return GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create().toJson(comment)
        }

        fun fromJson(json: String): Comment? {
            return GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create()
                .fromJson(json, Comment::class.java)
        }

    }

}
