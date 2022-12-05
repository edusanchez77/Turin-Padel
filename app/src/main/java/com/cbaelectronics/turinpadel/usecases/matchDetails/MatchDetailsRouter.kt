/**
 * Created by CbaElectronics by Eduardo Sanchez on 30/11/22 14:22.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.matchDetails

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.Match
import com.cbaelectronics.turinpadel.model.domain.Post
import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.usecases.addMatch.AddMatchActivity
import com.cbaelectronics.turinpadel.usecases.base.BaseActivityRouter

class MatchDetailsRouter: BaseActivityRouter {

    override fun intent(activity: Context): Intent = Intent(activity, MatchDetailsActivity::class.java)

    override fun launch(activity: Context) {
        activity.startActivity(intent(activity))
        (activity as Activity).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

    fun launch(activity: Context, match: Match){
        activity.startActivity(intent(activity).apply {
            putExtra(DatabaseField.MATCHES.key, Match.toJson(match))
        })
        (activity as Activity).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }
}