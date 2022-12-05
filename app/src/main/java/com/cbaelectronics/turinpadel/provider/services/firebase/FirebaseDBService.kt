/**
 * Created by ITDev by Eduardo Sanchez on 25/2/2022.
 *  www.itdev.com
 */

package com.cbaelectronics.turinpadel.provider.services.firebase

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cbaelectronics.turinpadel.model.domain.*
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.Constants.DEFAULT_COUNT_REQUEST
import com.cbaelectronics.turinpadel.util.Constants.FIXEDTURN_STATUS_CANCEL
import com.cbaelectronics.turinpadel.util.Constants.FIXEDTURN_STATUS_CONFIRM
import com.cbaelectronics.turinpadel.util.Constants.FIXEDTURN_STATUS_DELETED
import com.cbaelectronics.turinpadel.util.Constants.FIXEDTURN_STATUS_PENDING
import com.cbaelectronics.turinpadel.util.Constants.STATUS_DEFAULT
import com.cbaelectronics.turinpadel.util.Constants.STATUS_RESERVED
import com.cbaelectronics.turinpadel.util.Constants.TURN_DATE_FORMAT
import com.cbaelectronics.turinpadel.util.Constants.TYPE_FIXED_TURN
import com.cbaelectronics.turinpadel.util.Constants.TYPE_TURN
import com.cbaelectronics.turinpadel.util.Util
import com.cbaelectronics.turinpadel.util.Util.getOrder
import com.google.firebase.firestore.*
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
    FIXED_TURNS("fixedTurns"),
    POST("posts"),
    COMMENTS("comments"),
    SCHEDULE("schedule"),
    MATCHES("matches"),
    MATCHES_DETAILS("matchesDetails"),
    REQUEST("request"),

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

    // Fixed Turn
    FIXED_TURN_ID("fixedTurnId"),
    FIXED_TURN_DAY("fixedTurnDay"),
    FIXED_TURN_HOUR("fixedTurnHour"),
    FIXED_TURN_DATE("fixedTurnDate"),
    FIXED_TURN_STATUS("fixedTurnStatus"),
    FIXED_TURN_ORDER("fixedTurnOrder"),
    FIXED_TURN_RESERVED_BY("fixedTurnRevervedBy"),

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
    SCHEDULE_ID("schId"),
    SCHEDULE_TURN_TYPE("turnType"),
    SCHEDULE_DAY("schDay"),

    // Matches
    MATCH_ID("matchId"),
    MATCH_DATE("matchDate"),
    MATCH_VACANTES("matchVacantes"),
    MATCH_CATEGORY("matchCategory"),
    MATCH_GENRE("matchGenre"),
    MATCH_REQUEST("matchRequest"),
    MATCH_USER("matchUser"),

    // Matches Details
    PLAYER1("player1"),
    PLAYER2("player2"),
    PLAYER3("player3"),
    PLAYER4("player4"),

    // Request
    REQUEST_ADD_DATE("requestAddDate"),
    REQUEST_UPDATE_DATE("requestUpdateDate"),
    REQUEST_STATUS("requestStatus"),
    REQUEST_MATCH_CREATOR("requestMatchCreator"),
    REQUEST_TOKEN_CREATOR("requestTokenCreator")

}

object FirebaseDBService {

    // Properties
    private val usersRef = FirebaseFirestore.getInstance().collection(DatabaseField.USERS.key)
    private val turnRef = FirebaseFirestore.getInstance().collection(DatabaseField.TURNS.key)
    private val fixedTurnRef = FirebaseFirestore.getInstance().collection(DatabaseField.FIXED_TURNS.key)
    private val scheduleRef = FirebaseFirestore.getInstance().collection(DatabaseField.SCHEDULE.key)
    private val postRef = FirebaseFirestore.getInstance().collection(DatabaseField.POST.key)
    private val commentsRef = FirebaseFirestore.getInstance().collection(DatabaseField.COMMENTS.key)
    private val matchRef = FirebaseFirestore.getInstance().collection(DatabaseField.MATCHES.key)
    private val matchDetailsRef = FirebaseFirestore.getInstance().collection(DatabaseField.MATCHES_DETAILS.key)
    private val requestRef = FirebaseFirestore.getInstance().collection(DatabaseField.REQUEST.key)

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

        return withContext(Dispatchers.IO) {
            usersRef.document(email)
                .get()
                .await()

        }
    }

    fun updateUser(user: User, type: Int) {

        user.email?.let { id ->
            usersRef.document(id)
                .update(DatabaseField.TYPE.key, type)
        }

    }

    fun saveTurn(turn: Turn) {

        turn.date.let {
            turnRef.document().set(turn.toJSON())
        }

    }

    fun saveTurn(fixedTurn: FixedTurn){
        fixedTurn.date.let {
            fixedTurnRef.document().set(fixedTurn.toJSON())
        }
    }

    suspend fun saveFixedTurn(fixedTurn: FixedTurn): DocumentReference {

        return withContext(Dispatchers.IO) {
            fixedTurnRef.add(fixedTurn.toJSON())
                .await()
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

    fun loadFixedTurn(order: Int): LiveData<MutableList<FixedTurn>> {

        val mutableData = MutableLiveData<MutableList<FixedTurn>>()

        fixedTurnRef
            .orderBy(DatabaseField.FIXED_TURN_DATE.key, ASCENDING)
            .addSnapshotListener { value, error ->
                val listData = mutableListOf<FixedTurn>()

                val startDate = Date().addOrRemoveDays(order, true)
                val endDate = Date().addOrRemoveDays(order, false)
                var currentDay: String? = null

                for (document in value!!) {

                    //val sfd = SimpleDateFormat("EEEE")
                    val id = document.id
                    var day = document.getString(DatabaseField.FIXED_TURN_DAY.key)
                    val hour = document.getString(DatabaseField.FIXED_TURN_HOUR.key)
                    val curt = document.getString(DatabaseField.TURN_CURT.key)
                    val status = document.getString(DatabaseField.FIXED_TURN_STATUS.key)
                    val order = document.getLong(DatabaseField.FIXED_TURN_ORDER.key)?.toInt()
                    val date = document.getDate(DatabaseField.FIXED_TURN_DATE.key)

                    val datosUser =
                        document.data.get(DatabaseField.FIXED_TURN_RESERVED_BY.key) as Map<String, Any>
                    val name = datosUser.get(DatabaseField.DISPLAY_NAME.key).toString()
                    val email = datosUser.get(DatabaseField.EMAIL.key).toString()
                    val avatar = datosUser.get(DatabaseField.PROFILE_IMAGE_URL.key).toString()
                    val register =
                        document.getDate("${DatabaseField.POST_WRITER.key}.${DatabaseField.REGISTER_DATE.key}")
                    val token = datosUser.get(DatabaseField.TOKEN.key).toString()
                    val type = datosUser.get(DatabaseField.TYPE.key).toString().toInt()

                    val user = User(name, email, avatar, token, type, register)

                    if(currentDay == null){
                        currentDay = day
                    } else {
                        if (currentDay == day){
                            day = null
                        }else{
                            currentDay = day
                        }
                    }

                    if (date?.calendarDate()?.toDate()!! >= startDate && date.calendarDate()
                            .toDate()!! <= endDate
                    ) {
                        val fixedTurn =
                            FixedTurn(id, curt!!, day, hour!!, status!!, order!!, date, user)
                        listData.add(fixedTurn)
                    }

                }
                mutableData.value = listData
            }
        return mutableData
    }

    /*
    fun loadFixedTurnSchedule(user: User): LiveData<MutableList<FixedTurn>>{
        val mutableData = MutableLiveData<MutableList<FixedTurn>>()

        fixedTurnRef
            .whereEqualTo("${DatabaseField.FIXED_TURN_RESERVED_BY.key}.${DatabaseField.EMAIL.key}", user?.email)
            .orderBy(DatabaseField.FIXED_TURN_DATE.key, ASCENDING)
            .addSnapshotListener { value, error ->
                val listData = mutableListOf<FixedTurn>()

                for (document in value!!) {

                    //val sfd = SimpleDateFormat("EEEE")
                    val id = document.id
                    val day = document.getString(DatabaseField.FIXED_TURN_DAY.key)
                    val hour = document.getString(DatabaseField.FIXED_TURN_HOUR.key)
                    val curt = document.getString(DatabaseField.TURN_CURT.key)
                    val status = document.getString(DatabaseField.FIXED_TURN_STATUS.key)
                    val order = document.getLong(DatabaseField.FIXED_TURN_ORDER.key)?.toInt()
                    val date = document.getDate(DatabaseField.FIXED_TURN_DATE.key)

                    val datosUser = document.data.get(DatabaseField.FIXED_TURN_RESERVED_BY.key) as Map<String, Any>
                    val name = datosUser.get(DatabaseField.DISPLAY_NAME.key).toString()
                    val email = datosUser.get(DatabaseField.EMAIL.key).toString()
                    val avatar = datosUser.get(DatabaseField.PROFILE_IMAGE_URL.key).toString()
                    val register =
                        document.getDate("${DatabaseField.POST_WRITER.key}.${DatabaseField.REGISTER_DATE.key}")
                    val token = datosUser.get(DatabaseField.TOKEN.key).toString()
                    val type = datosUser.get(DatabaseField.TYPE.key).toString().toInt()

                    val user = User(name, email, avatar, token, type, register)

                    val fixedTurn = FixedTurn(id, curt!!, day!!, hour!!, status!!, order!!, date!!, user)
                    listData.add(fixedTurn)
                }
                mutableData.value = listData
            }

        return mutableData
    }
*/

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
                    STATUS_DEFAULT,
                    DatabaseField.TURN_RESERVE.key,
                    FieldValue.delete()
                )

            deleteSchedule(schedule.id)
        }

    }


    fun updateFixedTurn(fixedTurn: FixedTurn, status: String){
        fixedTurn.id.let { id ->
            fixedTurnRef.document(id!!)
                .update(DatabaseField.FIXED_TURN_STATUS.key, status)
        }
    }

    fun updateFixedTurn(schedule: Schedule, status: String) {

        schedule.turn.let { id ->
            val curt = schedule.curt
            val date = schedule.date
            val turn = Turn(curt = curt, date = date)

            //TODO: Hacer función en Util.kt para obtener el Order del día

            fixedTurnRef.document(id)
                .update(
                    DatabaseField.FIXED_TURN_STATUS.key, status
                )

            updateSchedule(schedule.id, status)

        }

    }

    fun updateTurn(turn: Turn) {

        turn.id.let {
            turnRef.document(it!!).update(
                DatabaseField.TURN_DATE.key,
                turn.date,
                DatabaseField.TURN_CURT.key,
                turn.curt
            )
        }

    }

    fun deleteTurn(id: String) {

        id.let {
            turnRef.document(it).delete()
        }

    }

    fun deleteFixedTurn(id: String) {

        id.let {
            fixedTurnRef.document(it).delete()
        }

    }

    suspend fun searchScheduleId(fixedTurnId: String): QuerySnapshot? {
        return withContext(Dispatchers.IO) {
            scheduleRef.whereEqualTo(DatabaseField.TURN_ID.key, fixedTurnId)
                .limit(1)
                .get()
                .await()
        }
    }

    fun saveSchedule(turn: Turn, user: User) {

        val schedule = hashMapOf(
            DatabaseField.TURN_ID.key to turn.id,
            DatabaseField.TURN_CURT.key to turn.curt,
            DatabaseField.TURN_DATE.key to turn.date,
            DatabaseField.SCHEDULE_TURN_TYPE.key to TYPE_TURN,
            DatabaseField.SCHEDULE_DAY.key to turn.date.calendarDate(),
            DatabaseField.TURN_RESERVE.key to user
        )

        user.email.let {
            scheduleRef.document().set(schedule)
        }
    }

    fun saveSchedule(turn: FixedTurn, user: User, id: String) {

        val schedule = hashMapOf(
            DatabaseField.TURN_ID.key to id,
            DatabaseField.TURN_CURT.key to turn.curt,
            DatabaseField.TURN_DATE.key to turn.date,
            DatabaseField.SCHEDULE_DAY.key to turn.date?.calendarDate(),
            DatabaseField.SCHEDULE_TURN_TYPE.key to TYPE_FIXED_TURN,
            DatabaseField.TURN_RESERVE.key to user,
            DatabaseField.FIXED_TURN_STATUS.key to turn.status
        )

        user.email.let {
            scheduleRef.document().set(schedule)
        }
    }

    fun loadSchedule(context: Context): LiveData<MutableList<Schedule>> {

        val mutableData = MutableLiveData<MutableList<Schedule>>()
        val order = getOrder(context, Date().dayOfWeek())
        val startDate = Date().addOrRemoveDays(order, true)
        val endDate = Date().addOrRemoveDays(order, false)

        scheduleRef
            .orderBy(DatabaseField.TURN_DATE.key, ASCENDING)
            .addSnapshotListener { value, error ->
                val listData = mutableListOf<Schedule>()
                var currentDay: String? = null
                for (document in value!!) {

                    val datosUser =
                        document.data.get(DatabaseField.TURN_RESERVE.key) as Map<String, Any>

                    val id = document.id
                    val turn = document.getString(DatabaseField.TURN_ID.key)
                    val curt = document.getString(DatabaseField.TURN_CURT.key)
                    val date = document.getDate(DatabaseField.TURN_DATE.key)
                    val turnType = document.getString(DatabaseField.SCHEDULE_TURN_TYPE.key)
                    val status = document.getString(DatabaseField.FIXED_TURN_STATUS.key)
                    var day: String? = document.getString(DatabaseField.SCHEDULE_DAY.key)

                    val name = datosUser.get(DatabaseField.DISPLAY_NAME.key).toString()
                    val email = datosUser.get(DatabaseField.EMAIL.key).toString()
                    val avatar = datosUser.get(DatabaseField.PROFILE_IMAGE_URL.key).toString()
                    val register =
                        document.getDate("${DatabaseField.POST_WRITER.key}.${DatabaseField.REGISTER_DATE.key}")
                    val token = datosUser.get(DatabaseField.TOKEN.key).toString()
                    val type = datosUser.get(DatabaseField.TYPE.key).toString().toInt()

                    val user = User(name, email, avatar, token, type, register)

                    if(currentDay == null){
                        currentDay = day
                    } else {
                        if (currentDay == day){
                            day = null
                        }else{
                            currentDay = day
                        }
                    }

                    if (date?.calendarDate()?.toDate()!! >= Date().calendarDate().toDate() && date?.calendarDate()
                            ?.toDate()!! <= endDate && (turnType == TYPE_TURN || (turnType == TYPE_FIXED_TURN && status == FIXEDTURN_STATUS_CONFIRM))
                    ) {

                        val schedule = Schedule(id, turn!!, curt!!, date!!, user!!, turnType!!, status, day)
                        listData.add(schedule)
                    }

                }
                mutableData.value = listData
            }

        return mutableData
    }


    fun loadSchedule(context: Context, user: User): LiveData<MutableList<Schedule>> {

        val mutableData = MutableLiveData<MutableList<Schedule>>()
        val order = getOrder(context, Date().dayOfWeek())
        val startDate = Date().addOrRemoveDays(order, true)
        val endDate = Date().addOrRemoveDays(order, false)

        scheduleRef
            .whereEqualTo(
                "${DatabaseField.TURN_RESERVE.key}.${DatabaseField.EMAIL.key}",
                user?.email
            )
            .orderBy(DatabaseField.TURN_DATE.key, ASCENDING)
            .addSnapshotListener { value, error ->
                val listData = mutableListOf<Schedule>()
                for (document in value!!) {

                    val id = document.id
                    val turn = document.getString(DatabaseField.TURN_ID.key)
                    val curt = document.getString(DatabaseField.TURN_CURT.key)
                    val date = document.getDate(DatabaseField.TURN_DATE.key)
                    val type = document.getString(DatabaseField.SCHEDULE_TURN_TYPE.key)
                    val status = document.getString(DatabaseField.FIXED_TURN_STATUS.key)

                    if (date?.calendarDate()?.toDate()!! >= startDate && date?.calendarDate()
                            ?.toDate()!! <= endDate
                    ){
                        val schedule = Schedule(id, turn!!, curt!!, date!!, user!!, type!!, status, date.calendarDate())
                        listData.add(schedule)
                    }

                }
                mutableData.value = listData
            }

        return mutableData
    }

    fun deleteSchedule(id: String) {

        id.let { id ->
            scheduleRef.document(id)
                .delete()
        }
    }

    suspend fun savePost(post: Post): DocumentReference {

        return withContext(Dispatchers.IO) {
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

    fun searchUser(): LiveData<MutableList<User>> {

        val mutableList = MutableLiveData<MutableList<User>>()

        usersRef.addSnapshotListener { value, error ->
            val listData = mutableListOf<User>()
            for (document in value!!) {
                val name = document.get(DatabaseField.DISPLAY_NAME.key).toString()
                val email = document.get(DatabaseField.EMAIL.key).toString()
                val avatar = document.get(DatabaseField.PROFILE_IMAGE_URL.key).toString()
                val register =
                    document.getDate(DatabaseField.REGISTER_DATE.key)
                val token = document.get(DatabaseField.TOKEN.key).toString()
                val type = document.get(DatabaseField.TYPE.key).toString().toInt()

                val user = User(name, email, avatar, token, type, register)

                listData.add(user)
            }
            mutableList.value = listData
        }

        return mutableList

    }

    fun updateSchedule(id: String, status: String){
        id.let { id ->
            scheduleRef.document(id)
                .update(DatabaseField.FIXED_TURN_STATUS.key, status)
        }
    }


    suspend fun save(match: Match): DocumentReference {

        return withContext(Dispatchers.IO) {
            matchRef.add(match.toJSON())
                .await()
        }

    }

    fun load(): LiveData<MutableList<Match>>{
        val mutableList = MutableLiveData<MutableList<Match>>()

        matchRef
            .addSnapshotListener { value, error ->
                val listData = mutableListOf<Match>()
                for (document in value!!) {
                    val datosUser =
                        document.data.get(DatabaseField.MATCH_USER.key) as Map<String, Any>

                    val id = document.id

                    val name = datosUser.get(DatabaseField.DISPLAY_NAME.key).toString()
                    val email = datosUser.get(DatabaseField.EMAIL.key).toString()
                    val avatar = datosUser.get(DatabaseField.PROFILE_IMAGE_URL.key).toString()
                    val register =
                        document.getDate("${DatabaseField.COMMENT_WRITER.key}.${DatabaseField.REGISTER_DATE.key}")
                    val token = datosUser.get(DatabaseField.TOKEN.key).toString()
                    val type = datosUser.get(DatabaseField.TYPE.key).toString().toInt()

                    val date = document.getDate(DatabaseField.MATCH_DATE.key)
                    val category = document.getString(DatabaseField.MATCH_CATEGORY.key)
                    val genre = document.getString(DatabaseField.MATCH_GENRE.key)
                    val vacantes = document.getLong(DatabaseField.MATCH_VACANTES.key)?.toInt()

                    val user = User(name, email, avatar, token, type, register)
                    val match = Match(id, date!!, vacantes!!, category!!, genre!!, user)

                    listData.add(match)
                }

                mutableList.value = listData
            }

        return mutableList
    }

    fun save(matchDetails: MatchDetails){
        matchDetails.matchId.let {
            matchDetailsRef.document().set(matchDetails.toJSON())
        }
    }

    fun loadMatchDetails(id: String): LiveData<MutableList<MatchDetails>>{

        val mutableList = MutableLiveData<MutableList<MatchDetails>>()

        id.let { id ->
            matchDetailsRef
                .document()
                .addSnapshotListener { value, error ->
                    value.let { documentSnapshot ->
                        val matchId = value?.getString(DatabaseField.MATCH_ID.key)
                        if(matchId == id){

                            val listData = mutableListOf<MatchDetails>()
                            val datosUserMap = value?.data?.get(DatabaseField.MATCH_USER.key) as Map<String, String>
                            val player1Map = value.data?.get(DatabaseField.PLAYER1.key) as Map<String, String>
                            val player2Map = value.data?.get(DatabaseField.PLAYER2.key) as Map<String, String>
                            val player3Map = value.data?.get(DatabaseField.PLAYER3.key) as Map<String, String>
                            val player4Map = value.data?.get(DatabaseField.PLAYER4.key) as Map<String, String>

                            val date = value.getDate(DatabaseField.MATCH_DATE.key)
                            val category = value.getString(DatabaseField.MATCH_CATEGORY.key)
                            val genre = value.getString(DatabaseField.MATCH_GENRE.key)
                            val vacantes = value.getLong(DatabaseField.MATCH_VACANTES.key)?.toInt()
                            val request = value.getLong(DatabaseField.MATCH_REQUEST.key)?.toInt()

                            val usrEmail = datosUserMap.get(DatabaseField.EMAIL.key)
                            val usrName = datosUserMap.get(DatabaseField.DISPLAY_NAME.key)
                            val usrPhoto = datosUserMap.get(DatabaseField.PROFILE_IMAGE_URL.key)
                            val usrToken = datosUserMap.get(DatabaseField.TOKEN.key)
                            val player1Email = player1Map.get(DatabaseField.EMAIL.key)
                            val player1Name = player1Map.get(DatabaseField.DISPLAY_NAME.key)
                            val player1Photo = player1Map.get(DatabaseField.PROFILE_IMAGE_URL.key)
                            val player1Token = player1Map.get(DatabaseField.TOKEN.key)
                            val player2Email = player2Map.get(DatabaseField.EMAIL.key)
                            val player2Name = player2Map.get(DatabaseField.DISPLAY_NAME.key)
                            val player2Photo = player2Map.get(DatabaseField.PROFILE_IMAGE_URL.key)
                            val player2Token = player2Map.get(DatabaseField.TOKEN.key)
                            val player3Email = player3Map.get(DatabaseField.EMAIL.key)
                            val player3Name = player3Map.get(DatabaseField.DISPLAY_NAME.key)
                            val player3Photo = player3Map.get(DatabaseField.PROFILE_IMAGE_URL.key)
                            val player3Token = player3Map.get(DatabaseField.TOKEN.key)
                            val player4Email = player4Map.get(DatabaseField.EMAIL.key)
                            val player4Name = player4Map.get(DatabaseField.DISPLAY_NAME.key)
                            val player4Photo = player4Map.get(DatabaseField.PROFILE_IMAGE_URL.key)
                            val player4Token = player4Map.get(DatabaseField.TOKEN.key)

                            val datosUser = User(usrName, usrEmail, usrPhoto, usrToken)
                            val player1 = User(player1Name, player1Email, player1Photo, player1Token)
                            val player2 = User(player2Name, player2Email, player2Photo, player2Token)
                            val player3 = User(player3Name, player3Email, player3Photo, player3Token)
                            val player4 = User(player4Name, player4Email, player4Photo, player4Token)

                            val matchDetails = MatchDetails(id, datosUser, date!!, genre!!, category!!, vacantes!!, request!!, player1, player2, player3, player4)
                            listData.add(matchDetails)

                            mutableList.value = listData
                        }
                    }
                }

        }

        return mutableList

    }

}
