package com.cbaelectronics.turinpadel.usecases.turn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.Turn
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService

class TurnViewModel : ViewModel() {

    // Properties

    val user = Session.instance.user ?: User()
    val settings = Session.instance.user?.settings ?: UserSettings()

    // Localization

    val curt1 = R.string.turn_curt_1
    val curt2 = R.string.turn_curt_2
    val info = R.string.turn_info
    val alertReserve = R.string.turn_reserve_alert_1
    val alertInfo = R.string.turn_reserve_alert_2
    val alertOk = R.string.turn_reserve_alert_3
    val button = R.string.turn_button_addTurn

    // Public
    fun loadTurn(curt: String, mDate: String): LiveData<MutableList<Turn>> {

        val mutableData = MutableLiveData<MutableList<Turn>>()

        FirebaseDBService.loadTurn(curt, mDate).observeForever{
            mutableData.value = it
        }

        return mutableData

    }

    fun reserveTurn(turn: Turn){

        FirebaseDBService.updateTurn(turn, user)
        FirebaseDBService.saveSchedule(turn, user)

    }

}