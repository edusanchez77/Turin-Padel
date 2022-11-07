/**
 * Created by CbaElectronics by Eduardo Sanchez on 20/9/22 12:46.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.addFixedTurn

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.Turn
import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.usecases.base.BaseActivityRouter

class AddFixedTurnRouter: BaseActivityRouter {

    override fun intent(activity: Context): Intent = Intent(activity, AddFixedTurnActivity::class.java)

    override fun launch(activity: Context) {
        activity.startActivity(intent(activity))
        (activity as Activity).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

    fun launch(activity: Context, turn: Turn){
        activity.startActivity(intent(activity).apply {
            putExtra(DatabaseField.TURN_ID.key, turn.id)
            putExtra(DatabaseField.TURN_DATE.key, turn.date.toString())
            putExtra(DatabaseField.TURN_CURT.key, turn.curt)
        })
        (activity as Activity).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

}