/**
 * Created by IT Dev by Eduardo Sanchez on 20/1/2022
 * Copyright (c) 2022 . All rights reserved.
 * www.itdev.com.ar
 */

package com.itdev.nosfaltauno.util.extension

import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.util.FontSize
import com.cbaelectronics.turinpadel.util.FontType

fun TextView.font(size: FontSize, type: FontType? = null, color: Int = ContextCompat.getColor(context, R.color.text)){

    var fontType = type

    if (type == null) {
        fontType = when (size) {
            FontSize.TITLE, FontSize.HEAD, FontSize.BUTTON, FontSize.CAPTION -> FontType.BOLD
            FontSize.SUBTITLE, FontSize.SUBHEAD -> FontType.LIGHT
            FontSize.BODY -> FontType.REGULAR
        }
    }

    setTextSize(TypedValue.COMPLEX_UNIT_SP, size.size.toFloat())
    setTypeface(Typeface.createFromAsset(context.assets, fontType!!.path), Typeface.NORMAL)
    setTextColor(color)
    includeFontPadding = false

}