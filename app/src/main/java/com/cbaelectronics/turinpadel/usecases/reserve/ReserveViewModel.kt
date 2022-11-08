/**
 * Created by CbaElectronics by Eduardo Sanchez on 14/6/22 10:11.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.reserve

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.Comment
import com.cbaelectronics.turinpadel.model.domain.Schedule
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService

class ReserveViewModel: ViewModel() {

    // Properties

    val user = Session.instance.user ?: User()
    val settings = Session.instance.user?.settings ?: UserSettings()

    // Localization

    val title = R.string.reserve_info

    // Public

    fun loadReserve(context: Context): LiveData<MutableList<Schedule>>{

        val mutableData = MutableLiveData<MutableList<Schedule>>()

        FirebaseDBService.loadSchedule(context).observeForever {
            mutableData.value = it
        }

        return mutableData

    }

}