/**
 * Created by CbaElectronics by Eduardo Sanchez on 21/9/22 08:45.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.SearchUser

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService

class SearchUserViewModel: ViewModel() {

    // Properties

    val user = Session.instance.user ?: User()
    val settings = Session.instance.user?.settings ?: UserSettings()

    // Localization

    val title = R.string.searchUser_info
    val search = R.string.searchUser_search
    val alert = R.string.searchUser_alert_message_1
    val positive = R.string.searchUser_alert_button_positive
    val negative = R.string.searchUser_alert_button_negative
    val ok = R.string.searchUser_alert_button_ok

    // Public

    fun searchUser(): LiveData<MutableList<User>> {

        val mutableList = MutableLiveData<MutableList<User>>()

        FirebaseDBService.searchUser().observeForever{
            mutableList.value = it
        }

        return mutableList

    }

    fun messageQuestion(context: Context, name: String): String{
        return context.getString(alert, name)
    }

    fun updateUser(user: User, type: Int){
        FirebaseDBService.updateUser(user, type)
    }

}