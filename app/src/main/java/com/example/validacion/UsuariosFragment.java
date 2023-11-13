package com.example.validacion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class UsuariosFragment extends Fragment {


    public UsuariosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usuarios, container, false);

        TextView tvNombreMecanico = view.findViewById(R.id.textNombreUsuario);
        TextView tvCorreoMecanico = view.findViewById(R.id.textTelefonoUsuario);
        TextView tvEstadoMecanico = view.findViewById(R.id.textRol);

         Button cerrarSesion= view.findViewById(R.id.cerrarSesion);

        /*
        Button customButton = view.findViewById(R.id.customButton);
        Button mandaraPrueba = view.findViewById(R.id.mandaraPrueba);
*/


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);

        String idusuario = sharedPreferences.getString("idusuario", "");
        String nombre = sharedPreferences.getString("nombre", "");
        String estado = sharedPreferences.getString("estado", "");
        String correo = sharedPreferences.getString("email", "");
        String telefono = sharedPreferences.getString("telefono", "");
        String permisos = sharedPreferences.getString("permisos", "");


        tvEstadoMecanico.setText(permisos);
        tvCorreoMecanico.setText(telefono);
        tvNombreMecanico.setText(idusuario + " - " + nombre);

/*
        mandaraPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrASlider();
            }
        });


        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
                //  IrASlider();
            }
        });
*/

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
            }
        });
        return view;
    }

    private void cerrarSesion() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.remove("password");
        editor.remove("rememberMe");
        editor.apply();

        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }



}