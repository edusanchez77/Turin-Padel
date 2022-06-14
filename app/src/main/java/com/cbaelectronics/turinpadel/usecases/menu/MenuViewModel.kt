/**
 * Created by CbaElectronics by Eduardo Sanchez on 19/5/22 16:20.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.menu

import android.content.Context
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.User
import com.cbaelectronics.turinpadel.model.domain.UserSettings
import com.cbaelectronics.turinpadel.model.session.Session
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.Util

enum class Network {

    TWITTER, INSTAGRAM, FACEBOOK;

    val uri: String
        get() {
            return when (this) {
                TWITTER -> Constants.TWITTER_CBAELECTRONICS_URI
                INSTAGRAM -> Constants.INSTAGRAM_CBAELECTRONICS_URI
                FACEBOOK -> Constants.FACEBOOK_CBAELECTRONICS_URI
            }
        }

}

class MenuViewModel: ViewModel() {

    // Properties

    val user = Session.instance.user ?: User()
    val settings = Session.instance.user?.settings ?: UserSettings()

    // Localization

    val byText = R.string.menu_by
    val infoText = R.string.menu_info
    val siteText = R.string.menu_site
    val onboardingText = R.string.menu_onboarding
    val admin = R.string.menu_admin
    val schedule = R.string.menu_reserve

    // Public

    fun versionText(context: Context): String {
        return context.getString(R.string.menu_version, Util.version())
    }

    fun open(context: Context, network: Network) {
        Util.openBrowser(context, network.uri)
    }

    fun openSite(context: Context) {
        Util.openBrowser(context, Constants.TURINPADEL_URI)
    }

}