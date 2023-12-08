package com.example.validacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {


    private static final int SPLASH_DURATION = 1000; //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Creamos un Intent para iniciar la siguiente actividad
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                // Cerramos la actividad actual para evitar que el usuario regrese a la pantalla de inicio
                finish();
            }
        }, SPLASH_DURATION);

    }
}