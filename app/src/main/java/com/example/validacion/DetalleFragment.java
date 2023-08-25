package com.example.validacion;

import android.app.Activity;
import android.content.Intent;
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

    private String urlApi = "http://192.168.1.252/georgioapi/Controllers/Apiback.php";

    ViewPager2 viewPager2;
    RecyclerView recyclerViewRefacciones;
    RecyclerView recyclerViewMecanicos;

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

        recyclerViewRefacciones = rootView.findViewById(R.id.recyclerViewRefacciones);

        recyclerViewMecanicos = rootView.findViewById(R.id.recyclerViewMecanicos);

        viewPager2 = rootView.findViewById(R.id.ViewPager);


        Bundle bundle = getArguments();
        if (bundle != null) {
            String marca = bundle.getString("marca", "");
            String modelo = bundle.getString("modelo", "");
            String fecha = bundle.getString("fecha", "");
            String hora = bundle.getString("hora", "");
            String status = bundle.getString("status", "");
            String motivo = bundle.getString("motivo", "");
            String idventa = bundle.getString("idventa", "");

            CargarRefacciones(idventa);
            CargarMecanicos(idventa);
            CargarImagenes(idventa);

            //Texto del fragment con validaciones

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

            textMarca.setText(marca.toUpperCase() + " - " + modelo.toUpperCase());
            textstatus.setText("Estatus: " + status);
            textmotivo.setText(motivo);
            textfecha.setText(fecha);
            texthora.setText(hora);
        }
        return rootView;
    }

    private void CargarImagenes(String idventa) {

        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, urlApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<SlideItem> slideItems = new ArrayList<>();

                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject fotoObj = jsonArray.getJSONObject(i);
                                    String foto = fotoObj.getString("foto");
                                    String fotoUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/";

                                   slideItems.add(new SlideItem(fotoUrl + foto));


                                }
                                viewPager2.setAdapter(new SlideAdapter(slideItems, viewPager2));
                                viewPager2.setClipToPadding(false);



                                viewPager2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int currentItem = viewPager2.getCurrentItem();
                                        SlideItem selectedSlide = slideItems.get(currentItem);

                                        Toast.makeText(requireContext(), "Elemento seleccionado"+ selectedSlide, Toast.LENGTH_SHORT).show();
                                    }
                                });



                                viewPager2.setClipChildren(false);
                                viewPager2.setOffscreenPageLimit(4);
                                viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                                CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                                compositePageTransformer.addTransformer(new MarginPageTransformer(10));
                                compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                                    @Override
                                    public void transformPage(@NonNull View page, float position) {
                                        float r = 1 - Math.abs(position);
                                        page.setScaleY(0.85f + 0.15f);
                                    }
                                });

                                viewPager2.setPageTransformer(compositePageTransformer);
                                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                    public void onPageSelected(int position) {
                                        super.onPageSelected(position);
                                        sliderHandler.removeCallbacks(sliderRunnable);
                                        sliderHandler.postDelayed(sliderRunnable, 3000);
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("API Response", "Respuesta vacía");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Error", "Error en la solicitud: " + error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "8");
                params.put("idventa", idventa);
                return params;
            }
        };

        RequestQueue requestQueue3 = Volley.newRequestQueue(requireContext());
        requestQueue3.add(stringRequest3);

    }

    private void CargarMecanicos(String idventa) {

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, urlApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        List<Mecanicos> listaMecanicos = new ArrayList<>();

                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String foto = jsonObject.getString("foto");
                                    String nombre = jsonObject.getString("nombre");
                                    String motivoingreso = jsonObject.getString("motivoingreso");
                                    String fecha_programada = jsonObject.getString("fecha_programada");

                                    Mecanicos mecanicos = new Mecanicos(foto, nombre, motivoingreso, fecha_programada);
                                    listaMecanicos.add(mecanicos);
                                }

                                AdaptadorMecanicos adaptadorMecanicos = new AdaptadorMecanicos(listaMecanicos);
                                LinearLayoutManager layoutManager2 = new LinearLayoutManager(requireContext());
                                recyclerViewMecanicos.setLayoutManager(layoutManager2);
                                recyclerViewMecanicos.setAdapter(adaptadorMecanicos);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("API Response", "Respuesta vacía");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Error", "Error en la solicitud: " + error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "6");
                params.put("idventa", idventa);
                return params;
            }
        };

        RequestQueue requestQueue2 = Volley.newRequestQueue(requireContext());
        requestQueue2.add(stringRequest2);

    }

    private void CargarRefacciones(String idventa) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        List<Refacciones> listaRefacciones = new ArrayList<>();

                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String idrefaccion = jsonObject.getString("idrefaccion");
                                    String descripcion = jsonObject.getString("descripcion");
                                    String precio = jsonObject.getString("precio");
                                    String cantidad = jsonObject.getString("cantidad");

                                    Refacciones refaccion = new Refacciones(cantidad, descripcion, precio, idrefaccion);
                                    listaRefacciones.add(refaccion);
                                }

                                AdaptadorRefacciones adaptadorRefacciones = new AdaptadorRefacciones(listaRefacciones);
                                recyclerViewRefacciones.setLayoutManager(new LinearLayoutManager(requireContext()));
                                recyclerViewRefacciones.setAdapter(adaptadorRefacciones);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("API Response", "Respuesta vacía");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Error", "Error en la solicitud: " + error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "3");
                params.put("idventa", idventa);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };


    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

}