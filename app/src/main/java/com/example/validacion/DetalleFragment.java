package com.example.validacion;

import android.app.Activity;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // Mostrar los datos en las vistas correspondientes
        TextView textMarca = rootView.findViewById(R.id.tv1);
        TextView textmotivo = rootView.findViewById(R.id.tv3);
        TextView textfecha = rootView.findViewById(R.id.tv4);
        TextView texthora = rootView.findViewById(R.id.tv5);
        TextView textmecanico = rootView.findViewById(R.id.tvmecanico);
        TextView textstatus = rootView.findViewById(R.id.tvstatus);
        ImageView imageViewDetalles = rootView.findViewById(R.id.imageViewDetalles); // Asegúrate de tener el ID correcto para el ImageView


        // Obtener los datos del Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String marca = bundle.getString("marca", "");
            String modelo = bundle.getString("modelo", "");
            String fecha = bundle.getString("fecha", "");

            String hora = bundle.getString("hora", "");
            String status = bundle.getString("status", "");
            String mecanico = bundle.getString("mecanico", "");
            String motivo = bundle.getString("motivo", "");
            String foto = bundle.getString("foto", "");

// Obtener el campo de refacciones del bundle
            String refaccionesJson = bundle.getString("refacciones", "");

            if (!TextUtils.isEmpty(refaccionesJson)) {
                try {
                    JSONArray refaccionesArray = new JSONArray(refaccionesJson);

                    StringBuilder refaccionesText = new StringBuilder();

                    // Iterar a través de cada objeto en el array de refacciones
                    for (int i = 0; i < refaccionesArray.length(); i++) {
                        JSONObject refaccionObj = refaccionesArray.getJSONObject(i);

                        String idRefaccion = refaccionObj.optString("idrefaccion", "");
                        String clave = refaccionObj.optString("clave", "");
                        String cantidad = refaccionObj.optString("cantidad", "");
                        String descripcion = refaccionObj.optString("descripcion", "");

                        // Construir una cadena con la información de la refacción
                        String refaccionInfo = "ID Refacción: " + idRefaccion + "\n" +
                                "Clave: " + clave + "\n" +
                                "Cantidad: " + cantidad + "\n" +
                                "Descripción: " + descripcion + "\n\n";

                        refaccionesText.append(refaccionInfo);
                    }

                    // Mostrar la información en la vista correspondiente
                    TextView textRefacciones = rootView.findViewById(R.id.tvrefacciones);
                    textRefacciones.setText(refaccionesText.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            textMarca.setText(marca.toUpperCase() + " - " + modelo.toUpperCase());
            textmotivo.setText(motivo);
            textfecha.setText(fecha);
            texthora.setText(hora);


            if(status.equals("pendiente")) {
                textstatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.amarillo));
            }else if(status.equals("entrega")){
                textstatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.verde));
            }else if(status.equals("en espera")){
                textstatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.amarillo));
            }else if(status.equals("preparado")){
                textstatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.verde));
            }else {
                textstatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.rojo));
            }


            textstatus.setText("Estatus: "+status);


            textmecanico.setText("Id de mecanico: "+ mecanico);

            if (!TextUtils.isEmpty(foto)) {
                String imageUrl = "http://tallergeorgio.hopto.org:5613/verificaciones/imagenes/unidades/" + foto;
                Glide.with(this)
                        .load(imageUrl)  // URL de la foto
                        .error(R.drawable.default_image)  // Aquí se especifica la imagen en caso de error
                        .into(imageViewDetalles);
            }else{

                Glide.with(this)
                        .load(R.drawable.default_image)  // Carga la imagen predeterminada si imageUrl está vacío
                        .into(imageViewDetalles);
            }

        }

        return rootView;
    }



}