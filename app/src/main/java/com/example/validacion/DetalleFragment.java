package com.example.validacion;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.concurrent.HandlerExecutor;

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

    ViewPager2 viewPager2;

    public DetalleFragment() {
        // Required empty public constructor
    }

    private Handler sliderHandler = new Handler();

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

        TextView textMarca = rootView.findViewById(R.id.tv1);
        TextView textmotivo = rootView.findViewById(R.id.tv3);
        TextView textfecha = rootView.findViewById(R.id.tv4);
        TextView texthora = rootView.findViewById(R.id.tv5);
        TextView textstatus = rootView.findViewById(R.id.tvstatus);
        viewPager2 =rootView.findViewById(R.id.ViewPager);

        List<SlideItem> slideItems = new ArrayList<>();
        slideItems.add(new SlideItem("http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/0dae41abd73c135c15730828328eb56a.jpg"));
        slideItems.add(new SlideItem("http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/54320220de0e1462e914998658cec710.jpg"));
        slideItems.add(new SlideItem("http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/27bea1b4e42ccd5ab66d50d09483eb4a.jpg"));
        slideItems.add(new SlideItem("http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/0dae41abd73c135c15730828328eb56a.jpg"));
        viewPager2.setAdapter(new SlideAdapter(slideItems,viewPager2));


        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(4);

        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);


        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));

        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r =1 - Math.abs(position);
                page.setScaleY(0.85f + 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            public void onPageSelected(int position){
                super.onPageSelected(position);

                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,1500);
            }
        });


        Bundle bundle = getArguments();
        String refaccionesJson = null;
        if (bundle != null) {
            String marca = bundle.getString("marca", "");
            String modelo = bundle.getString("modelo", "");
            String fecha = bundle.getString("fecha", "");

            String hora = bundle.getString("hora", "");
            String status = bundle.getString("status", "");
            String mecanico = bundle.getString("mecanicos");
            String motivo = bundle.getString("motivo", "");
            String foto = bundle.getString("foto", "");
            refaccionesJson = bundle.getString("refacciones");


            RecyclerView recyclerViewRefacciones = rootView.findViewById(R.id.recyclerViewRefacciones);
            List<Refacciones> listaRefacciones = obtenerListaDeRefacciones(refaccionesJson);

            AdaptadorRefacciones adaptadorRefacciones = new AdaptadorRefacciones(listaRefacciones);
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
            recyclerViewRefacciones.setLayoutManager(layoutManager);
            recyclerViewRefacciones.setAdapter(adaptadorRefacciones);



            RecyclerView recyclerViewMecanicos = rootView.findViewById(R.id.recyclerViewMecanicos);
            List<Mecanicos> listaMecanicos= obtenerListaDeMecanicos(mecanico);

            AdaptadorMecanicos adaptadorMecanicos = new AdaptadorMecanicos(listaMecanicos);
            LinearLayoutManager layoutManager2 = new LinearLayoutManager(requireContext());
            recyclerViewMecanicos.setLayoutManager(layoutManager2);
            recyclerViewMecanicos.setAdapter(adaptadorMecanicos);



            textMarca.setText(marca.toUpperCase() + " - " + modelo.toUpperCase());
            textmotivo.setText(motivo);
            textfecha.setText(fecha);
            texthora.setText(hora);

            if (status.equals("pendiente")) {
                textstatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.amarillo));
            } else if (status.equals("entrega")) {
                textstatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.verde));
            } else if (status.equals("en espera")) {
                textstatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.amarillo));
            } else if (status.equals("preparado")) {
                textstatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.verde));
            } else {
                textstatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.rojo));
            }
            textstatus.setText("Estatus: " + status);

            /*
            if (!TextUtils.isEmpty(foto)) {
                String imageUrl = "http://tallergeorgio.hopto.org:5613/verificaciones/imagenes/unidades/" + foto;
                Glide.with(this)
                        .load(imageUrl)  // URL de la foto
                        .error(R.drawable.default_image)  // Aquí se especifica la imagen en caso de error
                        .into(imageViewDetalles);
            } else {
                Glide.with(this)
                        .load(R.drawable.default_image)  // Carga la imagen predeterminada si imageUrl está vacío
                        .into(imageViewDetalles);
            }


             */
        }
        return rootView;
    }

    private List<Refacciones> obtenerListaDeRefacciones(String jsonString) {
        List<Refacciones> listaRefacciones = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String idrefaccion = jsonObject.getString("idrefaccion");
                String descripcion = jsonObject.getString("descripcion");
                String precio = jsonObject.getString("precio");
                String cantidad = jsonObject.getString("cantidad");

                Refacciones refaccion = new Refacciones(cantidad, descripcion, precio, idrefaccion);
                listaRefacciones.add(refaccion);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listaRefacciones;
    }



    private List<Mecanicos> obtenerListaDeMecanicos(String jsonString) {
        List<Mecanicos> listaMecanicos = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);


                String foto = jsonObject.getString("foto");
                String nombre = jsonObject.getString("nombre");
                String motivoingreso = jsonObject.getString("motivoingreso");
                String fecha_programada = jsonObject.getString("fecha_programada");


                Mecanicos mecanicos = new Mecanicos(foto, nombre, motivoingreso, fecha_programada);
                listaMecanicos.add(mecanicos);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listaMecanicos;
    }


    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

}