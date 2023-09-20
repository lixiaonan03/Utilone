package com.lxn.utilone.fcm

import MyWorker
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.lxn.utilone.util.Log
import kotlinx.coroutines.*
import javax.inject.Inject


/**
 * firebase 推送消息处理的
 * @author：李晓楠
 * 时间：2023/6/19 15:03
 */
class MyFirebaseMessagingService : FirebaseMessagingService(){


    private var job: Job? = null


    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.i("lxnPush", "Refreshed token: $token")

    }




    private fun isLongRunningJob() = true

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i("lxnPush", "onMessageReceived ===: ${remoteMessage.toString()}")
        super.onMessageReceived(remoteMessage)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.i("lxnPush", "Message data payload: ${remoteMessage.data}")

            // Check if data needs to be processed by long running job
            if (isLongRunningJob()) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
        }

        // Check if message contains a notification payload.
        Log.i("lxnPush", "remoteMessage.notification:=== ${remoteMessage.notification}")
        remoteMessage.notification?.let {
            Log.i("lxnPush",  "Message Notification Body: ${it.body}")

        }
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
    }


    /**
     * Schedule async work using WorkManager.
     * 暂时没啥用
     */
    private fun scheduleJob() {
        // [START dispatch_job]
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
        WorkManager.getInstance(this).beginWith(work).enqueue()
        // [END dispatch_job]
    }


    override fun onMessageSent(msgId: String) {
        Log.i("lxnPush", "onMessageSent ===: ${msgId.toString()}")
        super.onMessageSent(msgId)
    }
}