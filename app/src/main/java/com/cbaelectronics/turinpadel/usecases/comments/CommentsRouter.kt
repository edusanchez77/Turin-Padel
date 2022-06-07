/**
 * Created by CbaElectronics by Eduardo Sanchez on 1/6/22 12:00.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.usecases.comments

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.Post
import com.cbaelectronics.turinpadel.provider.services.firebase.DatabaseField
import com.cbaelectronics.turinpadel.usecases.base.BaseActivityRouter
import com.cbaelectronics.turinpadel.usecases.home.HomeActivity
import java.io.Serializable

class CommentsRouter: BaseActivityRouter {

    override fun intent(activity: Context): Intent = Intent(activity, CommentsActivity::class.java)

    override fun launch(activity: Context) {
        activity.startActivity(intent(activity))
        (activity as Activity).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

    fun launch(activity: Context, post: Post){
        activity.startActivity(intent(activity).apply {
            putExtra(DatabaseField.POST.key, Post.toJson(post))
        })
        (activity as Activity).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

}