package com.cbaelectronics.turinpadel.usecases.turn.curts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.model.domain.Turn
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService

class Curt1ViewModel : ViewModel() {

    // Properties

    val user = Session.instance.user ?: User()
    val settings = Session.instance.user?.settings ?: UserSettings()

    // Localization

    // Public
    fun loadTurn(curt: String, mDate: String): LiveData<MutableList<Turn>>{

        val mutableData = MutableLiveData<MutableList<Turn>>()

        FirebaseDBService.loadTurn(curt, mDate).observeForever{
            mutableData.value = it
        }

        return mutableData

    }

}