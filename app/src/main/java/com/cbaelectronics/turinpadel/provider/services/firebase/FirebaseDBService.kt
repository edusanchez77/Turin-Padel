/**
 * Created by ITDev by Eduardo Sanchez on 25/2/2022.
 *  www.itdev.com
 */

package com.cbaelectronics.turinpadel.provider.services.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cbaelectronics.turinpadel.model.domain.*
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.Constants.STATUS_OUTOFTIME
import com.cbaelectronics.turinpadel.util.Constants.STATUS_RESERVED
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction.ASCENDING
import com.google.firebase.firestore.Query.Direction.DESCENDING
import com.itdev.nosfaltauno.util.extension.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

enum class DatabaseField(val key: String) {

    // Schemes
    USERS("users"),
    TURNS("turns"),
    POST("posts"),
    COMMENTS("comments"),
    SCHEDULE("schedule"),

    // Generic Field
    SETTINGS("settings"),

    // User
    DISPLAY_NAME("displayName"),
    EMAIL("email"),
    PROFILE_IMAGE_URL("photoProfile"),
    REGISTER_DATE("registerDate"),
    TOKEN("tokenDevice"),
    TYPE("type"),
    PROFILE_CATEGORY("profileCategory"),
    PROFILE_POSITION("profilePosition"),
    NOTIFICATION_TURN("notificationTurn"),
    NOTIFICATION_POST("notificationPost"),

    // Turns
    TURN_ID("turnId"),
    TURN_CURT("turnCurt"),
    TURN_DATE("turnDate"),
    TURN_STATUS("turnStatus"),
    TURN_RESERVE("turnReserve"),

    // Post
    POST_MESSAGE("postMessage"),
    POST_ADD_DATE("postAddDate"),
    POST_COUNT_COMMENTS("postCountComments"),
    POST_WRITER("postWriter"),

    // Comments
    POST_ID("postId"),
    COMMENT_MESSAGE("commentMessage"),
    COMMENT_ADD_DATE("commentAddDate"),
    COMMENT_WRITER("postWriter"),

    // Schedule
    SCHEDULE_ID("schId")

}

object FirebaseDBService {

    // Properties
    val usersRef = FirebaseFirestore.getInstance().collection(DatabaseField.USERS.key)
    val turnRef = FirebaseFirestore.getInstance().collection(DatabaseField.TURNS.key)
    val scheduleRef = FirebaseFirestore.getInstance().collection(DatabaseField.SCHEDULE.key)
    val postRef = FirebaseFirestore.getInstance().collection(DatabaseField.POST.key)
    val commentsRef = FirebaseFirestore.getInstance().collection(DatabaseField.COMMENTS.key)

    // Properties RealDataBase
    /*private val userRef = FirebaseDatabase.getInstance().getReference(DatabaseField.USERS.key)
    private val turnsRef = FirebaseDatabase.getInstance().getReference(DatabaseField.TURNS.key)*/


    // Public

    fun save(user: User) {

        user.email?.let { login ->
            usersRef.document(login).set(user.toJSON())
        }
    }

    fun saveSettings(user: User) {

        user.email?.let { login ->
            usersRef.document(login)
                .update(DatabaseField.SETTINGS.key, user.settings!!.toJSON())
        }

    }

    suspend fun load(email: String): DocumentSnapshot? {

        return withContext(Dispatchers.IO){
            usersRef.document(email)
                .get()
                .await()

        }
    }

    fun saveTurn(turn: Turn) {

        turn.date.let {
            turnRef.document().set(turn.toJSON())
        }

    }

    fun loadTurn(curt: String, mDate: String): LiveData<MutableList<Turn>> {

        val mutableData = MutableLiveData<MutableList<Turn>>()

        turnRef.whereEqualTo(DatabaseField.TURN_CURT.key, curt)
            .whereGreaterThan(DatabaseField.TURN_DATE.key, Timestamp(Date().time))
            .addSnapshotListener { value, error ->
                val listData = mutableListOf<Turn>()

                for (document in value!!) {

                    val sfd = SimpleDateFormat("dd/MM/yyyy")
                    val id = document.id
                    val date = document.getDate(DatabaseField.TURN_DATE.key)
                    val curt = document.getString(DatabaseField.TURN_CURT.key)
                    val status = document.getLong(DatabaseField.TURN_STATUS.key)?.toInt()

                    if (mDate == sfd.format(date)) {
                        val turn = Turn(id, curt!!, date!!, status!!)
                        listData.add(turn)
                    }

                }
                mutableData.value = listData
            }
        return mutableData
    }


    fun updateTurn(turn: Turn, user: User) {

        turn.id.let { id ->
            turnRef.document(id!!)
                .update(
                    DatabaseField.TURN_STATUS.key,
                    STATUS_RESERVED,
                    DatabaseField.TURN_RESERVE.key,
                    user
                )
        }
    }

    fun updateTurn(schedule: Schedule) {

        schedule.turn.let { id ->
            turnRef.document(id!!)
                .update(
                    DatabaseField.TURN_STATUS.key,
                    Constants.STATUS_DEFAULT,
                    DatabaseField.TURN_RESERVE.key,
                    FieldValue.delete()
                )
            scheduleRef.document(schedule.id)
                .delete()
        }

    }

    fun saveSchedule(turn: Turn, user: User) {

        val schedule = hashMapOf(
            DatabaseField.TURN_ID.key to turn.id,
            DatabaseField.TURN_CURT.key to turn.curt,
            DatabaseField.TURN_DATE.key to turn.date,
            DatabaseField.TURN_RESERVE.key to user
        )

        user.email.let {
            scheduleRef.document().set(schedule)
        }
    }

    fun loadSchedule(): LiveData<MutableList<Schedule>> {

        val mutableData = MutableLiveData<MutableList<Schedule>>()

        scheduleRef
            .orderBy(DatabaseField.TURN_DATE.key, ASCENDING)
            .addSnapshotListener { value, error ->
                val listData = mutableListOf<Schedule>()
                for (document in value!!) {

                    val datosUser =
                        document.data.get(DatabaseField.TURN_RESERVE.key) as Map<String, Any>

                    val id = document.id
                    val turn = document.getString(DatabaseField.TURN_ID.key)
                    val curt = document.getString(DatabaseField.TURN_CURT.key)
                    val date = document.getDate(DatabaseField.TURN_DATE.key)

                    val name = datosUser.get(DatabaseField.DISPLAY_NAME.key).toString()
                    val email = datosUser.get(DatabaseField.EMAIL.key).toString()
                    val avatar = datosUser.get(DatabaseField.PROFILE_IMAGE_URL.key).toString()
                    val register =
                        document.getDate("${DatabaseField.POST_WRITER.key}.${DatabaseField.REGISTER_DATE.key}")
                    val token = datosUser.get(DatabaseField.TOKEN.key).toString()
                    val type = datosUser.get(DatabaseField.TYPE.key).toString().toInt()

                    val user = User(name, email, avatar, token, type, register)


                    if(date?.calendarDate()!! >= Date().calendarDate()){

                        val schedule = Schedule(id, turn!!, curt!!, date!!, user!!)
                        listData.add(schedule)
                    }

                }
                mutableData.value = listData
            }

        return mutableData
    }


    fun loadSchedule(user: User): LiveData<MutableList<Schedule>> {

        val mutableData = MutableLiveData<MutableList<Schedule>>()

        scheduleRef
            .whereEqualTo("${DatabaseField.TURN_RESERVE.key}.${DatabaseField.EMAIL.key}", user?.email)
            .orderBy(DatabaseField.TURN_DATE.key, ASCENDING)
            .addSnapshotListener { value, error ->
                val listData = mutableListOf<Schedule>()
                for (document in value!!) {

                    val id = document.id
                    val turn = document.getString(DatabaseField.TURN_ID.key)
                    val curt = document.getString(DatabaseField.TURN_CURT.key)
                    val date = document.getDate(DatabaseField.TURN_DATE.key)

                    if(date?.calendarDate()!! >= Date().calendarDate()){

                        val schedule = Schedule(id, turn!!, curt!!, date!!, user!!)
                        listData.add(schedule)
                    }

                }
                mutableData.value = listData
            }

        return mutableData
    }


    suspend fun savePost(post: Post) : DocumentReference{

        return withContext(Dispatchers.IO){
            postRef.add(post.toJSON())
                .await()
        }

    }


    fun loadPost(): LiveData<MutableList<Post>> {

        val mutableData = MutableLiveData<MutableList<Post>>()

        postRef
            .orderBy(DatabaseField.POST_ADD_DATE.key, DESCENDING)
            .addSnapshotListener { value, error ->
                val listData = mutableListOf<Post>()
                for (document in value!!) {

                    val datosUser =
                        document.data.get(DatabaseField.POST_WRITER.key) as Map<String, Any>

                    val id = document.id
                    val message = document.getString(DatabaseField.POST_MESSAGE.key)
                    val date = document.getDate(DatabaseField.POST_ADD_DATE.key)
                    val comment = document.getLong(DatabaseField.POST_COUNT_COMMENTS.key)?.toInt()
                    val name = datosUser.get(DatabaseField.DISPLAY_NAME.key).toString()
                    val email = datosUser.get(DatabaseField.EMAIL.key).toString()
                    val avatar = datosUser.get(DatabaseField.PROFILE_IMAGE_URL.key).toString()
                    val register =
                        document.getDate("${DatabaseField.POST_WRITER.key}.${DatabaseField.REGISTER_DATE.key}")
                    val token = datosUser.get(DatabaseField.TOKEN.key).toString()
                    val type = datosUser.get(DatabaseField.TYPE.key).toString().toInt()

                    val user = User(name, email, avatar, token, type, register)
                    val post = Post(id, message!!, date!!, comment!!, user)

                    listData.add(post)
                }
                mutableData.value = listData
            }

        return mutableData

    }

    fun saveComment(comment: Comment) {

        comment.post.let { post ->
            commentsRef.document().set(comment.toJSON())
            postRef.document(post)
                .update(DatabaseField.POST_COUNT_COMMENTS.key, FieldValue.increment(1))
        }

    }

    fun loadComment(post: Post): LiveData<MutableList<Comment>> {

        val mutableData = MutableLiveData<MutableList<Comment>>()

        post.id.let { post ->
            commentsRef
                .whereEqualTo(DatabaseField.POST_ID.key, post)
                .orderBy(DatabaseField.COMMENT_ADD_DATE.key, ASCENDING)
                .addSnapshotListener { value, error ->
                    val listData = mutableListOf<Comment>()
                    for (document in value!!) {

                        val datosUser =
                            document.data.get(DatabaseField.COMMENT_WRITER.key) as Map<String, Any>

                        val id = document.id
                        val message = document.getString(DatabaseField.COMMENT_MESSAGE.key)
                        val date = document.getDate(DatabaseField.COMMENT_ADD_DATE.key)
                        val post = document.getString(DatabaseField.POST_ID.key)
                        val name = datosUser.get(DatabaseField.DISPLAY_NAME.key).toString()
                        val email = datosUser.get(DatabaseField.EMAIL.key).toString()
                        val avatar = datosUser.get(DatabaseField.PROFILE_IMAGE_URL.key).toString()
                        val register =
                            document.getDate("${DatabaseField.COMMENT_WRITER.key}.${DatabaseField.REGISTER_DATE.key}")
                        val token = datosUser.get(DatabaseField.TOKEN.key).toString()
                        val type = datosUser.get(DatabaseField.TYPE.key).toString().toInt()

                        val user = User(name, email, avatar, token, type, register)
                        val comment = Comment(id, post!!, message!!, date!!, user)

                        listData.add(comment)
                    }
                    mutableData.value = listData
                }
        }

        return mutableData

    }


}
