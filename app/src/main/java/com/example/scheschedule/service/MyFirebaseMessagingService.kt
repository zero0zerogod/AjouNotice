package com.example.scheschedule.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.scheschedule.MainActivity
import com.example.scheschedule.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // 푸시 알림( notification ) + 데이터( data ) 처리
        val type = remoteMessage.data["type"] ?: ""

        val noti = remoteMessage.notification
        val title = noti?.title ?: "새 공지사항"
        val body = noti?.body ?: "[${type}] 공지가 등록되었습니다."

        sendNotification(title, body, type)
    }

    private fun sendNotification(title: String, messageBody: String, type: String) {
        val channelId = "default_channel"
        val channelName = "Default"

        // 알림 채널 생성
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            // 진동 설정 가능
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 클릭 시 MainActivity로 이동하되, “type”을 넘겨서 NavGraph에서 해당 화면으로 이동
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("noticeType", type)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // 알림 생성 및 표시
        val notificationBuilder = NotificationCompat.Builder(this, channelId).setContentTitle(title)
            .setContentText(messageBody).setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true).setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0, 500, 500, 500)) // 진동 예시

        notificationManager.notify(0, notificationBuilder.build())
    }
}
