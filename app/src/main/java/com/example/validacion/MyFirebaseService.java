package com.example.validacion;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Random;



public class MyFirebaseService extends FirebaseMessagingService {


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(Utils.TAG, token);

    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        String title = message.getNotification().getTitle();
        String content = message.getNotification().getBody();
        String data = new Gson().toJson(message.getData());
        Utils.showNotification(this, title, content);
        Log.d(Utils.TAG, data);

    }


}




/*
public class MyFirebaseService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        Log.d(Utils.TAG, token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        String title = message.getNotification().getTitle();
        String content = message.getNotification().getBody();
        String data = new Gson().toJson(message.getData());

        Log.d(Utils.TAG, data);

        // Validar el título y redirigir a actividades correspondientes
        PendingIntent pendingIntent = null;
        int notificationId = new Random().nextInt(); // Generar un ID de notificación único
        if ("Hay un coche que necesita servicio".equals(title)) {
            Intent intent = new Intent(this, SubirFotosUnidadesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(
                    this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
        } else if ("Hay un coche que necesita servicio2".equals(title)) {
            Intent intent = new Intent(this, Prueba2.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(
                    this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
        }else{
            Intent intent = new Intent(this, Activity_Binding.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(
                    this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
        }

        Utils.showNotifications(this, title, content, pendingIntent, notificationId);
    }
}
*/