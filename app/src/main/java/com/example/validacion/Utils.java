package com.example.validacion;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Random;

public class Utils {

    public static final String TAG = "El Token es: ";


    public static void showNotifications(Context context, String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "com.example.validacion");

        builder.setSmallIcon(R.drawable.baseline_notifications_active_24);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(title);
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.setSummaryText(title);

        builder.setStyle(bigTextStyle);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "com.example.validacion.id";
            NotificationChannel channel = new NotificationChannel(channelId, "Taller Georgio",
                    NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channelId);

        }
        manager.notify(new Random().nextInt(), builder.build());
    }




}
