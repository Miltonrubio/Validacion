package com.example.validacion;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetalleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetalleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetalleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetalleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetalleFragment newInstance(String param1, String param2) {
        DetalleFragment fragment = new DetalleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detalle, container, false);

        // Obtener los datos del Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String marca = bundle.getString("marca", "");
            String modelo = bundle.getString("modelo", "");
            String fecha = bundle.getString("fecha", "");

            String hora = bundle.getString("hora", "");
            String motivo = bundle.getString("motivo", "");
            String foto = bundle.getString("foto", "");


            // Mostrar los datos en las vistas correspondientes
            TextView textMarca = rootView.findViewById(R.id.tv1);
            TextView textModelo = rootView.findViewById(R.id.tv2);
            TextView textmotivo = rootView.findViewById(R.id.tv3);
            TextView textfecha = rootView.findViewById(R.id.tv4);
            TextView texthora = rootView.findViewById(R.id.tv5);
            ImageView imageViewDetalles = rootView.findViewById(R.id.imageViewDetalles); // Asegúrate de tener el ID correcto para el ImageView


            textMarca.setText(marca);
            textModelo.setText(modelo);
            textmotivo.setText(motivo);
            textfecha.setText(fecha);
            texthora.setText(hora);



            if (!TextUtils.isEmpty(foto)) {
                String imageUrl = "http://tallergeorgio.hopto.org:5613/verificaciones/imagenes/unidades/" + foto;
                Glide.with(this)
                        .load(imageUrl)  // URL de la foto
                        .into(imageViewDetalles);
            }else{
                String imageUrl = "http://tallergeorgio.hopto.org:5613/verificaciones/imagenes/unidades/AbHidalgo/3e4c701286a6847f64ae7d89423002ab.jpg";
                Glide.with(this)
                        .load(imageUrl)  // URL de la foto
                        .into(imageViewDetalles);
            }

        }

        return rootView;
    }
}