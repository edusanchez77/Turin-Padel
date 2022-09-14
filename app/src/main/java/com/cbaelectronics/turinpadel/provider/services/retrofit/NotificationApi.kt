/**
 * Created by ITDev by Eduardo Sanchez on 31/3/2022.
 *  www.itdev.com
 */

package com.itdev.nosfaltauno.provider.services.retrofit

import com.cbaelectronics.turinpadel.model.domain.PushNotification
import com.cbaelectronics.turinpadel.util.notifications.Constants.Companion.SERVER_KEY
import com.cbaelectronics.turinpadel.util.notifications.Constants.Companion.CONTENT_TYPE
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {

    @Headers("Authorization: key=${SERVER_KEY}", "Content-Type:${CONTENT_TYPE}")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>

}