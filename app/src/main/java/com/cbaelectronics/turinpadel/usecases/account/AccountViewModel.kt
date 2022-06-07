package com.cbaelectronics.turinpadel.usecases.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.Schedule
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService

class AccountViewModel : ViewModel() {

    // Properties

    val user = Session.instance.user ?: User()
    val settings = Session.instance.user?.settings ?: UserSettings()

    // Localization

    val accountUserInfoText = R.string.account_user_schedule
    val cancel = R.string.schedule_dialog_cancel

    // Public

    fun loadSchedule(): LiveData<MutableList<Schedule>> {

        val mutableList = MutableLiveData<MutableList<Schedule>>()

        FirebaseDBService.loadSchedule(user).observeForever{
            mutableList.value = it
        }

        return mutableList

    }


    fun cancel(schedule: Schedule){
        FirebaseDBService.updateTurn(schedule)
    }

}