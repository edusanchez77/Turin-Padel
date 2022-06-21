/**
 * Created by CbaElectronics by Eduardo Sanchez on 14/6/22 10:11.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.makeAdmin

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.Schedule
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService
import com.cbaelectronics.turinpadel.util.Util

class MakeAdminViewModel: ViewModel() {

    // Properties

    val user = Session.instance.user ?: User()
    val settings = Session.instance.user?.settings ?: UserSettings()

    // Localization

    val title = R.string.makeAdmin_info
    val search = R.string.makeAdmin_search
    val alertTitle = R.string.makeAdmin_alert_title
    val reminder = R.string.makeAdmin_alert_message_2
    val positive = R.string.makeAdmin_alert_button_positive
    val negative = R.string.makeAdmin_alert_button_negative
    val ok = R.string.makeAdmin_alert_button_ok

    // Public

    fun searchUser(): LiveData<MutableList<User>>{

        val mutableList = MutableLiveData<MutableList<User>>()

        FirebaseDBService.searchUser().observeForever{
            mutableList.value = it
        }

        return mutableList

    }

    fun messageQuestion(context: Context, name: String): String{
        return context.getString(R.string.makeAdmin_alert_message_1, name)
    }

    fun updateUser(user: User, type: Int){
        FirebaseDBService.updateUser(user, type)
    }


}