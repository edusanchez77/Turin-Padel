/**
 * Created by CbaElectronics by Eduardo Sanchez on 14/6/22 10:16.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.onboarding

import androidx.lifecycle.ViewModel
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.Onboarding

class OnboardingViewModel: ViewModel() {

    // Properties

    val data = arrayListOf(
        Onboarding(0, R.drawable.logo_white, R.string.onboarding_page0_title, R.string.onboarding_page0_body),
        Onboarding(1, R.drawable.onboarding_calendar, R.string.onboarding_page1_title, R.string.onboarding_page1_body),
        Onboarding(2, R.drawable.onboarding_calendar_menos, R.string.onboarding_page2_title, R.string.onboarding_page2_body),
        Onboarding(3, R.drawable.onboarding_grandtable, R.string.onboarding_page3_title, R.string.onboarding_page3_body),
    )

    val pages = data.size

    // Localization

    val understoodText = R.string.understood
    val previousText = R.string.previous
    val nextText = R.string.next
}