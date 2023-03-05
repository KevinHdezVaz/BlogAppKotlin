package com.kevin.courseApp.utils


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kevin.courseApp.MainActivity
import com.kevin.courseApp.R
import java.io.IOException
import java.net.URL

class NotificacionFirebase : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Verificar si la notificación contiene datos
        if (remoteMessage.data.isNotEmpty()) {
            // Manejar la notificación
            val title = remoteMessage.data["title"]
            val message = remoteMessage.data["message"]
            val imageUrl = remoteMessage.data["image"]
            sendNotification(title, message, imageUrl)
        }

        // Verificar si la notificación contiene una notificación push
        remoteMessage.notification?.let {
            // Manejar la notificación push
            val title = it.title
            val message = it.body
            sendNotification(title, message, null)
        }
    }

    private fun sendNotification(title: String?, message: String?, imageUrl: String?) {
        // Crear un intent para abrir la aplicación cuando se toque la notificación
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        // Crear el estilo de la notificación
        val style = NotificationCompat.BigTextStyle()
        style.setBigContentTitle(title)
        style.bigText(message)

        // Crear la notificación
        val notificationBuilder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setStyle(style)
            .setContentIntent(pendingIntent)

        // Agregar una imagen si está disponible
        if (!imageUrl.isNullOrEmpty()) {
            val bitmap = getBitmapFromUrl(imageUrl)
            if (bitmap != null) {
                val bigPictureStyle = NotificationCompat.BigPictureStyle()
                bigPictureStyle.bigPicture(bitmap)
                notificationBuilder.setStyle(bigPictureStyle)
            }
        }

        // Mostrar la notificación
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID",
                "CHANNEL_NAME",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun getBitmapFromUrl(imageUrl: String): Bitmap? {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection()
            connection.doInput = true
            connection.connect()
            val input = connection.getInputStream()
            return BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

}
