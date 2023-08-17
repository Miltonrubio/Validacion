package com.example.validacion;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

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

        Utils.showNotifications(this, title, content);


    }


    private void openMainActivity() {
        // Aquí abres tu MainActivity
        Intent intent = new Intent(this, Prueba.class);
        startActivity(intent);
    }

    private void openActivity2() {
        // Aquí abres tu Activity2
        Intent intent = new Intent(this, Prueba2.class);
        startActivity(intent);
    }
}
