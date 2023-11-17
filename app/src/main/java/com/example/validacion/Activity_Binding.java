package com.example.validacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.validacion.Adaptadores.Utiles;
import com.example.validacion.databinding.ActivityBindingBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class Activity_Binding extends AppCompatActivity {


    ActivityBindingBinding binding;


    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);

        binding = ActivityBindingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);

        String permisosUsuario = sharedPreferences.getString("permisos", "");


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
                case (R.id.menu_clientes):
                    replaceFragment(new ClientesFragment());
                    break;
                case (R.id.herramientas):
                    replaceFragment(new InventariosFragment());
                    break;

            }
            return true;
        });

        setupMenu(permisosUsuario);

    }

    private void setupMenu(String permisosUsuario) {
        if ("SUPERADMIN".equals(permisosUsuario)) {
            binding.bottomNavigationView.getMenu().findItem(R.id.menu_actividades).setVisible(false);
            binding.bottomNavigationView.setSelectedItemId(R.id.menu_home);
        } else {
            binding.bottomNavigationView.getMenu().findItem(R.id.menu_clientes).setVisible(false);
            binding.bottomNavigationView.setSelectedItemId(R.id.menu_home);
        }
    }

    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame_layoutCoches);

        if (currentFragment instanceof UsuariosFragment || currentFragment instanceof ClientesFragment) {
            binding.bottomNavigationView.setSelectedItemId(R.id.menu_home);
        } else if (currentFragment instanceof HomeFragment) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
            } else {
                this.doubleBackToExitPressedOnce = true;
                Utiles.crearToastPersonalizado(Activity_Binding.this, "Presiona atrás otra vez para salir");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        } else {
            super.onBackPressed();
        }
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layoutCoches, fragment);
        fragmentTransaction.commit();
    }

}