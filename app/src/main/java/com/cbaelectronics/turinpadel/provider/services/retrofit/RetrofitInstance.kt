/**
 * Created by ITDev by Eduardo Sanchez on 31/3/2022.
 *  www.itdev.com
 */

package com.itdev.nosfaltauno.provider.services.retrofit

import com.cbaelectronics.turinpadel.util.notifications.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitInstance {

    companion object{
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api by lazy {
            retrofit.create(NotificationApi::class.java)
        }
    }

}