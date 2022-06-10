/**
 * Created by ITDev by Eduardo Sanchez on 2/4/2022.
 *  www.itdev.com
 */

package com.cbaelectronics.turinpadel.util.notifications

import android.util.Log
import com.cbaelectronics.turinpadel.model.domain.PushNotification
import com.itdev.nosfaltauno.provider.services.retrofit.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendNotification {

    fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d("NOTIFICACION 2", "OK")
                } else {
                    Log.d("ERROR: ", response.message())
                }

            } catch (e: Exception) {
                Log.d("ERROR CATCH: ", e.toString())
            }
        }

}