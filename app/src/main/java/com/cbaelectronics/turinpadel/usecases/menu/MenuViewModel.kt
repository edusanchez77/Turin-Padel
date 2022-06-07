/**
 * Created by CbaElectronics by Eduardo Sanchez on 19/5/22 16:20.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.menu

import android.content.Context
import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.util.Constants
import com.cbaelectronics.turinpadel.util.Util

class MenuViewModel: ViewModel() {

    // Localization

    val byText = R.string.menu_by
    val infoText = R.string.menu_info
    val siteText = R.string.menu_site
    val onboardingText = R.string.menu_onboarding

    // Public

    fun versionText(context: Context): String {
        return context.getString(R.string.menu_version, Util.version())
    }

    fun openSite(context: Context) {
        Util.openBrowser(context, Constants.TWITIMER_URI)
    }

}