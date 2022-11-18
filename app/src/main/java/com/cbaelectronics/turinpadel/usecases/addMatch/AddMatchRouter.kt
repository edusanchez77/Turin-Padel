/**
 * Created by CbaElectronics by Eduardo Sanchez on 18/11/22 10:29.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.addMatch

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.usecases.base.BaseActivityRouter

class AddMatchRouter: BaseActivityRouter {

    override fun intent(activity: Context): Intent = Intent(activity, AddMatchActivity::class.java)

    override fun launch(activity: Context) {
        activity.startActivity(intent(activity))
        (activity as Activity).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }
}