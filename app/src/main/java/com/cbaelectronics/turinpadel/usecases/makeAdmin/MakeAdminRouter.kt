/**
 * Created by CbaElectronics by Eduardo Sanchez on 14/6/22 10:12.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.makeAdmin

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.usecases.base.BaseActivityRouter

class MakeAdminRouter: BaseActivityRouter {

    override fun intent(activity: Context): Intent = Intent(activity, MakeAdminActivity::class.java)

    override fun launch(activity: Context) {
        activity.startActivity(intent(activity))
        (activity as Activity).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

}