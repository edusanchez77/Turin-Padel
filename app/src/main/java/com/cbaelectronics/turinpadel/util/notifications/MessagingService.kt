package com.cbaelectronics.turinpadel.util.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.cbaelectronics.turinpadel.R
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesKey
import com.cbaelectronics.turinpadel.provider.preferences.PreferencesProvider
import com.cbaelectronics.turinpadel.usecases.home.HomeActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import kotlin.random.Random

/**
 * Created by MoureDev by Brais Moure on 20/03/2019.
 * www.mouredev.com
 */
class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.i("FCM token:", token)

        //Session.instance.setupNotification()
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val user = PreferencesProvider.string(this, PreferencesKey.AUTH_USER)

        // Valido el user que creo la notificación. Si el usuario que la genera no es el mismo que el CurrenId
        if (message.data[Constants.USER] == user){
            val channelId = getString(R.string.notification_channel_id)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val intent = when(message.data[Constants.TYPE]){
                Constants.TYPE_POST -> {
                    Intent(this, HomeActivity::class.java).apply {
                        putExtra(Constants.FRAGMENT_ID, Constants.FRAGMENT_POST)
                    }
                }
                Constants.TYPE_TURN -> {
                    Intent(this, HomeActivity::class.java).apply {
                        putExtra(Constants.FRAGMENT_ID, Constants.FRAGMENT_TURN)
                    }
                }
                else -> Intent(this, HomeActivity::class.java)
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationId = Random.nextInt()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                createNotificationChannel(notificationManager, channelId)
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            val args = Bundle()
            args.putString(Constants.FRAGMENT_ID, Constants.FRAGMENT_POST)

            val pendingIntentFragment = NavDeepLinkBuilder(this)
                .setComponentName(HomeActivity::class.java)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.home_menu_turn)
                .setArguments(args)
                .createPendingIntent()

            val notification = NotificationCompat.Builder(this, channelId)
                .setContentTitle(message.data[Constants.TITLE])
                .setContentText(message.data[Constants.BODY])
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(notificationId, notification)
        }

    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        notificationManager: NotificationManager,
        channelId: String
    ) {
        val channelName = getString(R.string.notification_channel_name)
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = getString(R.string.notification_channel_description)
            enableLights(true)
            lightColor = ContextCompat.getColor(applicationContext, R.color.primary)
        }

        notificationManager.createNotificationChannel(channel)
    }


}
