package com.example.validacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.validacion.databinding.ActivityBindingBinding;

public class Activity_Binding extends AppCompatActivity {


    ActivityBindingBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);

        binding = ActivityBindingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case (R.id.menu_home):
                    replaceFragment(new HomeFragment());
                    break;
                case (R.id.menu_usuario):
                    replaceFragment(new UsuariosFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_layoutCoches, fragment);

        fragmentTransaction.commit();
    }

    public void cerrarSesion() {

        SharedPreferences sharedPreferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.remove("password");
        editor.putBoolean("rememberMe", false);
        editor.apply();
        Log.d("CERRAR_SESION", "Credenciales borradas");
        Log.d("CERRAR_SESION", "rememberMe actualizado a false");

        Intent intent = new Intent(Activity_Binding.this, MainActivity.class);
        startActivity(intent);
        finish();


    }
}