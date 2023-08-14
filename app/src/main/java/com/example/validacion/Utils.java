package com.example.validacion;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class Utils {

    public static final String TAG = "EDMIDEV";


    public static void showNotifications(Context context, String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "edmt,dev.channel");

        builder.setSmallIcon(R.drawable.baseline_notifications_active_24);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);


        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(title);
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.setSummaryText(title);

        builder.setStyle(bigTextStyle);
/*
        NotificationManager manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.0){
            NotificationChannel channel= new NotificationChannel("edmt.dev.channel", "EDMTDEV CHANNEL",
                    NotificationManager.IMPORTANCE_HIGH);

        }*/


    }

}
