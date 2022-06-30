package com.itdev.nosfaltauno.util.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.util.UIConstants
import com.cbaelectronics.turinpadel.util.Util


/**
 * Created by MoureDev by Brais Moure on 5/18/21.
 * www.mouredev.com
 */

fun ActionBar.titleLogo(context: Context) {

    setDisplayShowHomeEnabled(true)
    setDisplayHomeAsUpEnabled(false)
    setHomeButtonEnabled(false)
    setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.primary)))

    val drawable = ContextCompat.getDrawable(context, R.drawable.logo_padel_full)
    val bitmap = (drawable as BitmapDrawable).bitmap
    val height = Util.dpToPixel(context, UIConstants.LOGO_HEIGHT).toInt()
    val width = (height * bitmap.width) / bitmap.height
    val resizedDrawable = BitmapDrawable(context.resources, Bitmap.createScaledBitmap(bitmap, width, height, true))

    setIcon(resizedDrawable)
    title = ""
    elevation = 0F
}

fun ActionBar.titleCustom(context: Context, title: String){

    val drawable = ContextCompat.getDrawable(context, R.drawable.keyboard_arrow_left)
    drawable?.setColorFilter(ContextCompat.getColor(context, R.color.light), PorterDuff.Mode.SRC_ATOP);
    setDisplayHomeAsUpEnabled(true)
    setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.primary)))

    this.setHomeAsUpIndicator(drawable)

    this.title = title

}

fun ActionBar.transparent(context: Context){
    val drawable = ContextCompat.getDrawable(context, R.drawable.keyboard_arrow_left)
    drawable?.setColorFilter(ContextCompat.getColor(context, R.color.light), PorterDuff.Mode.SRC_ATOP);
    setDisplayHomeAsUpEnabled(true)
    setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.primary)))

    this.setHomeAsUpIndicator(drawable)
    this.elevation = 0F

    this.title = ""
}
