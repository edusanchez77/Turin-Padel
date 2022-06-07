package com.cbaelectronics.turinpadel.usecases.grandtable

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.Post
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService

class GrandtableViewModel : ViewModel() {

    // Properties

    var user = Session.instance.user ?: User()
    var settings = Session.instance.user?.settings ?: UserSettings()

    // Localization
    val info = R.string.grandtable_info
    val comment = R.string.grandtable_comment
    val write = R.string.grandtable_edittext_writePost
    val button = R.string.grandtable_button
    val alertOk = R.string.grandtable_alert_ok
    val alertError = R.string.grandtable_alert_error
    val alertIncomplete = R.string.grandtable_alert_incomplete

    // Public

    fun save(message: String){
        val post = Post(message = message, user = user)
        FirebaseDBService.savePost(post)
    }


    fun load(): MutableLiveData<MutableList<Post>>{

        var mutableList = MutableLiveData<MutableList<Post>>()

        FirebaseDBService.loadPost().observeForever {
            mutableList.value = it
        }

        return mutableList

    }

}