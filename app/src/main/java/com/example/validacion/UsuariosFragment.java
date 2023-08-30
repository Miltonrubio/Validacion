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
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsuariosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsuariosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UsuariosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsuariosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsuariosFragment newInstance(String param1, String param2) {
        UsuariosFragment fragment = new UsuariosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usuarios, container, false);

        TextView cerrarSesion = view.findViewById(R.id.cerrarSesion);

        ImageView coronaImg = view.findViewById(R.id.corona);

        TextView tvNombreMecanico = view.findViewById(R.id.tvNombreMecanico);
        TextView tvCorreoMecanico = view.findViewById(R.id.tvCorreoMecanico);
        TextView tvEstadoMecanico = view.findViewById(R.id.tvEstadoMecanico);
        TextInputLayout ContenedorCerrarSesion = view.findViewById(R.id.ContenedorCerrarSesion);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);

        String idusuario = sharedPreferences.getString("idusuario", "");
        String nombre = sharedPreferences.getString("nombre", "");
        String estado = sharedPreferences.getString("estado", "");
        String correo = sharedPreferences.getString("email", "");
        String permisos = sharedPreferences.getString("permisos", "");

        if (permisos.equals("SUPERADMIN")) {
            coronaImg.setVisibility(View.VISIBLE);
        }

        tvEstadoMecanico.setText(estado);
        tvCorreoMecanico.setText(correo);
        tvNombreMecanico.setText( idusuario + " - " +nombre);


        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           //  cerrarSesion();

            IrASlider();
            }
        });

        ContenedorCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  cerrarSesion();

                IrASlider();
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

    private void IrASlider() {

        Intent intent = new Intent(requireContext(), Prueba2.class);
        startActivity(intent);
        requireActivity().finish();
    }
}