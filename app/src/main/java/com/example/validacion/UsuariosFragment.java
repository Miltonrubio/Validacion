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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
        ImageView iconImageView = view.findViewById(R.id.iconImageView);
        Button cerrarSesion = view.findViewById(R.id.cerrarSesion);

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
        String foto = sharedPreferences.getString("foto", "");

        String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/usuarios/" + foto;
        Glide.with(requireContext())
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.usuarios)
                .error(R.drawable.usuarios)
                .into(iconImageView);

        tvEstadoMecanico.setText(permisos);
        tvCorreoMecanico.setText(telefono);
        tvNombreMecanico.setText(nombre.toUpperCase());

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