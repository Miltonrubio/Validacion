package com.example.validacion;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Random;


public class Utils {
    public static final String TAG = "BITACORA";

    public static void showNotification(Context context, String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "com.example.validacion");

        builder.setSmallIcon(R.drawable.tallergeorgiopng);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "com.example.validacion";
            NotificationChannel channel = new NotificationChannel(channelId, "Taller Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        manager.notify(new Random().nextInt(), builder.build());
    }

}


/*
public class Utils {

    public static final String TAG = "El Token es: ";

    public static void showNotifications(Context context, String title, String body, PendingIntent pendingIntent, int notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "com.example.validacion");

        builder.setSmallIcon(R.drawable.tallergeorgiopng);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(title);
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.setSummaryText(title);

        builder.setStyle(bigTextStyle);

        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        }

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "com.example.validacion.id";
            NotificationChannel channel = new NotificationChannel(channelId, "Taller Georgio",
                    NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        manager.notify(notificationId, builder.build());
    }
}
*/