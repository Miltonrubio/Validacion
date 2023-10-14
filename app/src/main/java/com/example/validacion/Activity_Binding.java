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
import android.widget.Toast;

import com.example.validacion.databinding.ActivityBindingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class Activity_Binding extends AppCompatActivity {


    ActivityBindingBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);

        binding = ActivityBindingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);

        String permisosUsuario = sharedPreferences.getString("permisos", "");

        setupMenu(permisosUsuario);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case (R.id.menu_home):
                    replaceFragment(new HomeFragment());
                    break;
                case (R.id.menu_actividades):
                    replaceFragment(new ActividadesFragment());
                    break;
                case (R.id.menu_usuario):
                    replaceFragment(new UsuariosFragment());
                    break;
            }
            return true;
        });
        replaceFragment(new HomeFragment());

        if ("SUPERADMIN".equals(permisosUsuario)) {
            binding.bottomNavigationView.setSelectedItemId(R.id.menu_home);
        } else {
            binding.bottomNavigationView.setSelectedItemId(R.id.menu_home);
        }

    }


    private void setupMenu(String permisosUsuario) {
        if ("SUPERADMIN".equals(permisosUsuario)) {
            binding.bottomNavigationView.getMenu().findItem(R.id.menu_actividades).setVisible(false);

        } else {

        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layoutCoches, fragment);
        fragmentTransaction.commit();

    }

/*
        SharedPreferences sharedPreferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String permisosUsuario = sharedPreferences.getString("permisos", "");


        binding = ActivityBindingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (permisosUsuario.equals("MECANICO") || permisosUsuario.equals("MECANICOS")) {

            replaceFragment(new HomeFragment());
            getMenuInflater().inflate(R.menu.menu_mecanicos, binding.bottomNavigationView.getMenu());
            binding.bottomNavigationView.setOnItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case (R.id.menu_home):
                        replaceFragment(new HomeFragment());
                        break;
                    case (R.id.menu_actividades):
                        replaceFragment(new ActividadesFragment());
                        break;
                    case (R.id.menu_usuario):
                        replaceFragment(new UsuariosFragment());
                        break;
                }
                return true;
            });

        } else if (permisosUsuario.equals("RECEPCION") || permisosUsuario.equals("SUPERADMIN")){
            replaceFragment(new HomeFragment());
            getMenuInflater().inflate(R.menu.menu, binding.bottomNavigationView.getMenu());

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
        }else{

            replaceFragment(new ArrastresFragment());
            getMenuInflater().inflate(R.menu.menu, binding.bottomNavigationView.getMenu());

            binding.bottomNavigationView.setOnItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case (R.id.menu_home):
                        replaceFragment(new ArrastresFragment());
                        break;
                    case (R.id.menu_usuario):
                        replaceFragment(new UsuariosFragment());
                        break;
                }
                return true;
            });
        }
    }


    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layoutCoches, fragment);
        fragmentTransaction.commit();


    }
*/

}