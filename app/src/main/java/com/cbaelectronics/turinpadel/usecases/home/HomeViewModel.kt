/**
 * Created by CbaElectronics by Eduardo Sanchez on 10/5/22 15:14.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.model.session.Session

class HomeViewModel: ViewModel() {

    // Properties

    var user = Session.instance.user ?: User()
    var settings = Session.instance.user?.settings ?: UserSettings()

    // Public

}