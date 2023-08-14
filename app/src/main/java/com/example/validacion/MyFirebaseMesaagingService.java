package com.example.validacion;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMesaagingService extends  FirebaseMessagingService{

    public static final String TAG="MyFirebaseMessagingService";


    public void onMessageReceived(@NonNull RemoteMessage message){


        super.onMessageReceived(message);
    }

/*
public void onNewToken(NonNull String token){

}*/

}
