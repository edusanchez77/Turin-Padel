package com.cbaelectronics.turinpadel.util

import android.app.AlertDialog
import android.content.Context
import android.graphics.*
import android.graphics.BlurMaskFilter.Blur
import android.graphics.Matrix.ScaleToFit
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.model.domain.DatabaseNotifications
import com.cbaelectronics.turinpadel.model.domain.PushNotification
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesKey
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesProvider
import com.cbaelectronics.turinpadel.util.notifications.SendNotification
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import retrofit2.http.Body
import java.util.concurrent.TimeUnit


/**
 * Created by MoureDev by Brais Moure on 5/30/21.
 * www.mouredev.com
 */
object UIUtil {

    // Alert

    fun showAlert(context: Context, title: String, message: String, positive: String, positiveAction: (() -> Unit)? = null, negative: String? = null) {

        val builder = AlertDialog.Builder(context, R.style.CustomDialogTheme)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positive) { _, _ ->
            positiveAction?.let {
                it()
            }
        }
        negative?.let {
            builder.setNegativeButton(it) { _, _ ->
                // Do nothing
            }
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.light))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.dark))
    }

    // Push Notification

    fun pushNotification(title: String, body: String, type: String, user: String) {
        PushNotification(
            DatabaseNotifications(title, body, type, user),
            Constants.NEW_POST
        ).also {
            val notification = SendNotification()
            notification.sendNotification(it)
        }
    }


    // Private

    fun addShadow(bm: Bitmap, dstHeight: Int, dstWidth: Int, color: Int, size: Int, dx: Float, dy: Float): Bitmap? {

        val mask = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ALPHA_8)
        val scaleToFit = Matrix()
        val src = RectF(0f, 0f, bm.width.toFloat(), bm.height.toFloat())
        val dst = RectF(0f, 0f, dstWidth - dx, dstHeight - dy)
        scaleToFit.setRectToRect(src, dst, ScaleToFit.CENTER)
        val dropShadow = Matrix(scaleToFit)
        dropShadow.postTranslate(dx, dy)
        val maskCanvas = Canvas(mask)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        maskCanvas.drawBitmap(bm, scaleToFit, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        maskCanvas.drawBitmap(bm, dropShadow, paint)
        val filter = BlurMaskFilter(size.toFloat(), Blur.NORMAL)
        paint.reset()
        paint.isAntiAlias = true
        paint.color = color
        paint.maskFilter = filter
        paint.isFilterBitmap = true
        val ret = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888)
        val retCanvas = Canvas(ret)
        retCanvas.drawBitmap(mask, 0f, 0f, paint)
        retCanvas.drawBitmap(bm, scaleToFit, null)
        mask.recycle()
        return ret
    }

}