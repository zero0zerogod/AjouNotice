package com.example.scheschedule.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.scheschedule.MainActivity
import com.example.scheschedule.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // 데이터 payload 예: {"type": "general"}
        val type = remoteMessage.data["type"] ?: "home"
        Log.d("MyFirebaseMessagingService", "수신한 타입: $type")

        // 알림 메시지가 있으면 제목/내용 설정
        val noti = remoteMessage.notification
        val title = noti?.title ?: "새 공지사항"
        val body = noti?.body ?: "[$type] 공지가 등록되었습니다."

        // 실제 알림 생성
        sendNotification(title, body, type)

        // 앱에 새로 고침 이벤트 전송
        val intent = Intent("ACTION_REFRESH_NOTICES").apply {
            putExtra("noticeType", type)
        }
        Log.d("MyFirebaseMessagingService", "Broadcast 전송: $type")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun sendNotification(title: String, messageBody: String, type: String) {
        Log.d("MyFirebaseMessagingService", "sendNotification 호출 - type: $type")

        val channelId = "default_channel"
        val channelName = "Default"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 이미 채널이 존재하면 다시 생성하지 않음
            if (notificationManager.getNotificationChannel(channelId) == null) {
                val channel = NotificationChannel(
                    channelId, channelName, NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    enableVibration(true)
                }
                notificationManager.createNotificationChannel(channel)
            }
        }

        // 알림 클릭 시 특정 화면으로 이동할 Intent
        val intent = Intent(this, MainActivity::class.java).apply {
            Log.d("MyFirebaseMessagingService", "수신한 알림 타입: $type")
            putExtra("noticeType", type)
            putExtra("forceRefresh", true) // 새로고침 강제 옵션 추가
            // singleTask + 아래 Flag로 "앱이 없을 때도" 제대로 인텐트 전달
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        // requestCode를 0으로 통일하여 항상 FLAG_UPDATE_CURRENT로 갱신
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 알림 생성 및 표시
        val notificationBuilder = NotificationCompat.Builder(this, channelId).setContentTitle(title)
            .setContentText(messageBody).setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true).setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0, 500, 500, 500))

        notificationManager.notify(0, notificationBuilder.build())
    }
}
