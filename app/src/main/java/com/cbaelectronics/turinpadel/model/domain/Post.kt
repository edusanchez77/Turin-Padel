/**
 * Created by CbaElectronics by Eduardo Sanchez on 31/5/22 16:30.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.model.domain

import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.util.Constants
import com.google.gson.GsonBuilder
import java.sql.Timestamp
import java.util.*

data class Post(
    val id: String? = null,
    val message: String,
    val date: Date = Timestamp(Date().time),
    val comments: Int = Constants.DEFAULT_COUNT_COMMENTS,
    val user: User
){
    fun toJSON(): Map<String, Any> {

        val JSON: MutableMap<String, Any> = mutableMapOf(
            DatabaseField.POST_MESSAGE.key to (message ?: ""),
            DatabaseField.POST_COUNT_COMMENTS.key to (comments ?: ""),
            DatabaseField.POST_ADD_DATE.key to (date ?: ""),
            DatabaseField.POST_WRITER.key to (user ?: ""),
        )

        return JSON

    }

    companion object {

        fun toJson(post: Post): String {
            return GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create().toJson(post)
        }

        fun fromJson(json: String): Post? {
            return GsonBuilder().setDateFormat(Constants.JSON_DATE_FORMAT).create()
                .fromJson(json, Post::class.java)
        }

    }
}
