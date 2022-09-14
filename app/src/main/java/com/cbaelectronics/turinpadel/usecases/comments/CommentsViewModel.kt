/**
 * Created by CbaElectronics by Eduardo Sanchez on 1/6/22 11:59.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.Comment
import com.cbaelectronics.turinpadel.model.domain.Post
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService
import com.cbaelectronics.turinpadel.util.Constants

class CommentsViewModel: ViewModel() {

    // Properties

    var user = Session.instance.user ?: User()
    var settings = Session.instance.user?.settings ?: UserSettings()

    // Localization

    val title = R.string.postDetails_title
    val info = R.string.postDetails_comments
    val writeComment = R.string.postDetails_edittext_writeComment
    val alertOk = R.string.postDetails_alert_ok
    val alertError = R.string.postDetails_alert_error
    val alertIncomplete = R.string.postDetails_alert_incomplete
    val notificationTitle = R.string.notification_topic_comment_title
    val notificationBody = R.string.notification_topic_comment_body

    // Public

    fun save(post: String, message: String){
        val comment = Comment(post = post, message = message, user = user)
        FirebaseDBService.saveComment(comment)
        val topic = "${Constants.TOPIC_PATH}${post}"

        Session.instance.setupNotification(true, topic)
    }

    fun load(post: Post): LiveData<MutableList<Comment>>{
        val mutableList = MutableLiveData<MutableList<Comment>>()

        FirebaseDBService.loadComment(post).observeForever {
            mutableList.value = it
        }

        return mutableList
    }
}