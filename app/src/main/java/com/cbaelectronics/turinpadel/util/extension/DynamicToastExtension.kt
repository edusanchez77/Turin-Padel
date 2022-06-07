/**
 * Created by CbaElectronics by Eduardo Sanchez on 30/5/22 09:47.
 *  www.cbaelectronics.com.ar
 */

package com.cbaelectronics.turinpadel.util.extension

import androidx.core.content.ContextCompat
import com.pranavpandey.android.dynamic.toasts.DynamicToast

fun DynamicToast.edu(): DynamicToast.Config{

    return DynamicToast.Config.getInstance()
        .setIconSize(10)

}