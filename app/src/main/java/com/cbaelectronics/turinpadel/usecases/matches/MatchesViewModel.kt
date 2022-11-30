/**
 * Created by CbaElectronics by Eduardo Sanchez on 17/11/22 23:01.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.Match
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.provider.services.firebase.FirebaseDBService

class MatchesViewModel : ViewModel() {

    // Properties

    var user = Session.instance.user ?: User()
    var settings = Session.instance.user?.settings ?: UserSettings()

    // Localization

    val title = R.string.matches_title
    val info = R.string.matches_info
    val fotterInfo = R.string.matches_footer_info
    val button = R.string.matches_footer_button

    // Public

    fun load(): LiveData<MutableList<Match>> {

        var mutableList = MutableLiveData<MutableList<Match>>()

        FirebaseDBService.load().observeForever {
            mutableList.value = it
        }

        return mutableList

    }

}