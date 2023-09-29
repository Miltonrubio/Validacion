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
import android.widget.LinearLayout;
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

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class DetalleFragment extends Fragment {


    private String urlApi = "http://192.168.1.252/georgioapi/Controllers/Apiback.php";

    ViewPager2 viewPager2;
    RecyclerView recyclerViewMecanicos, recyclerViewRefacciones, recyclerViewBitacora;

    TextView tvRefacciones, tvBitacora;

    public DetalleFragment() {
        // Required empty public constructor
    }

    private Handler sliderHandler = new Handler();


    public static DetalleFragment newInstance(String param1, String param2) {
        DetalleFragment fragment = new DetalleFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detalle, container, false);

        TextView textMarca = rootView.findViewById(R.id.tv1);
        TextView textmotivo = rootView.findViewById(R.id.tv3);
        TextView textfecha = rootView.findViewById(R.id.tv4);
        TextView textstatus = rootView.findViewById(R.id.tvstatus);

        tvBitacora = rootView.findViewById(R.id.tvBitacora);

        recyclerViewRefacciones = rootView.findViewById(R.id.recyclerViewRefacciones);
        recyclerViewMecanicos = rootView.findViewById(R.id.recyclerViewMecanicos);
        recyclerViewBitacora = rootView.findViewById(R.id.recyclerViewBitacora);


        viewPager2 = rootView.findViewById(R.id.ViewPager);


        Bundle bundle = getArguments();
        if (bundle != null) {
            String marca = bundle.getString("marca", "");
            String modelo = bundle.getString("modelo", "");
            String fecha = bundle.getString("fecha_ingreso", "");
            String hora = bundle.getString("hora_ingreso", "");
            String status = bundle.getString("status", "");
            String motivo = bundle.getString("motivo", "");
            String idventa = bundle.getString("idventa", "");

            CargarRefacciones(idventa);
            CargarMecanicos(idventa);
            CargarImagenes(idventa);
            CargarBitacora(idventa);

            textMarca.setText(marca.toUpperCase() + " - " + modelo.toUpperCase());
            // textstatus.setText("Estatus: " + status);
            textmotivo.setText(motivo);


            if (fecha.equals("null") || fecha.isEmpty()) {

                textfecha.setText("No hay fecha estimada");
            } else {
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = inputFormat.parse(fecha);

                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new DateFormatSymbols(new Locale("es", "ES")));
                    String fecha_formateada = outputFormat.format(date);


                    try {

                        SimpleDateFormat inputFormatHora = new SimpleDateFormat("HH:mm:ss");
                        Date time = inputFormatHora.parse(hora);

                        SimpleDateFormat outputFormatHora = new SimpleDateFormat("hh:mm a");
                        String hora_formateada = outputFormatHora.format(time);

                        textfecha.setText("Ingresado: " + "el " + fecha_formateada + " a las " + hora_formateada);

                    } catch (Exception e) {

                        textfecha.setText("Ingresado: " + "el " + fecha_formateada);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int colorBlanco = ContextCompat.getColor(requireContext(), R.color.white);
            int colorAmarillo = ContextCompat.getColor(requireContext(), R.color.amarillo);
            int colorVerde = ContextCompat.getColor(requireContext(), R.color.verde);
            int colorRojo = ContextCompat.getColor(requireContext(), R.color.rojo);
            int colorAzulito = ContextCompat.getColor(requireContext(), R.color.azulitoSuave);
            int colorNegro = ContextCompat.getColor(requireContext(), R.color.black);
            int colorGris = ContextCompat.getColor(requireContext(), R.color.gris);


            if (!status.equals("null") || status.isEmpty()) {
                textstatus.setText("Estatus: " + status.toUpperCase());


                if (status.equalsIgnoreCase("pendiente")) {
                    textstatus.setTextColor(colorAmarillo);

                } else if (status.equalsIgnoreCase("Finalizado")) {

                    textstatus.setTextColor(colorVerde);
                } else if (status.equalsIgnoreCase("Servicios programado")) {

                    textstatus.setTextColor(colorVerde);
                } else if (status.equalsIgnoreCase("Cita programada")) {

                    textstatus.setTextColor(colorAzulito);
                } else if (status.equalsIgnoreCase("Diagnostico")) {

                    textstatus.setTextColor(colorRojo);
                } else if (status.equalsIgnoreCase("En espera")) {

                    textstatus.setTextColor(colorGris);
                } else if (status.equalsIgnoreCase("En servicio")) {

                    textstatus.setTextColor(colorAmarillo);
                } else if (status.equalsIgnoreCase("Prueba de ruta")) {


                    textstatus.setTextColor(colorNegro);

                } else if (status.equalsIgnoreCase("Lavado y detallado")) {

                    textstatus.setTextColor(colorAzulito);
                } else if (status.equalsIgnoreCase("Listo para entrega")) {

                    textstatus.setTextColor(colorVerde);
                } else if (status.equalsIgnoreCase("Entregado")) {

                    textstatus.setTextColor(colorVerde);
                } else {

                    textstatus.setTextColor(colorAzulito);
                }
            } else {
                textstatus.setText("Status no disponible");
                textstatus.setTextColor(colorRojo);
            }


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
                                    String id_ser_venta = fotoObj.getString("id_ser_venta");
                                    String fotoUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/";

                                    slideItems.add(new SlideItem(fotoUrl + foto, id_ser_venta));
                                }

                                SlideAdapter slideAdapter = new SlideAdapter(slideItems, viewPager2);

                                viewPager2.setAdapter(slideAdapter);
                                viewPager2.setClipToPadding(false);
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
                                    String fecha_programada = jsonObject.getString("fecha");

                                    Mecanicos mecanicos = new Mecanicos(foto, nombre, motivoingreso, fecha_programada);
                                    listaMecanicos.add(mecanicos);
                                }


                                if (listaMecanicos.isEmpty()) {
                                    recyclerViewMecanicos.setVisibility(View.GONE);
                                    LinearLayout LayoutNoMecanicos = requireView().findViewById(R.id.LayoutNoMecanicos);
                                    LayoutNoMecanicos.setVisibility(View.VISIBLE);
                                } else {
                                    recyclerViewMecanicos.setVisibility(View.VISIBLE);
                                    LinearLayout LayoutNoMecanicos = requireView().findViewById(R.id.LayoutNoMecanicos);
                                    LayoutNoMecanicos.setVisibility(View.GONE);
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

                                if (listaRefacciones.isEmpty()) {
                                    recyclerViewRefacciones.setVisibility(View.GONE);
                                    LinearLayout LayoutNoRefacciones = requireView().findViewById(R.id.LayoutNoRefacciones);
                                    LayoutNoRefacciones.setVisibility(View.VISIBLE);
                                } else {
                                    recyclerViewRefacciones.setVisibility(View.VISIBLE);
                                    LinearLayout LayoutNoRefacciones = requireView().findViewById(R.id.LayoutNoRefacciones);
                                    LayoutNoRefacciones.setVisibility(View.GONE);

                                    AdaptadorRefacciones adaptadorRefacciones = new AdaptadorRefacciones(listaRefacciones);
                                    recyclerViewRefacciones.setLayoutManager(new LinearLayoutManager(requireContext()));
                                    recyclerViewRefacciones.setAdapter(adaptadorRefacciones);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            tvRefacciones.setText("No hay Refacciones para mostrar");
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


    private void CargarBitacora(String idservicio) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<ActividadadesUnidad> listaActividadesUnidad = new ArrayList<>();
                        if (!TextUtils.isEmpty(response)) {
                            Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show();
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String horainicio = jsonObject.getString("horainicio");
                                    String horafin = jsonObject.getString("horafin");
                                    String fecha = jsonObject.getString("fecha");
                                    String id_servicio = jsonObject.getString("id_servicio");
                                    String id_personal = jsonObject.getString("id_personal");
                                    String obsservaciones = jsonObject.getString("obsservaciones");
                                    String estatus = jsonObject.getString("estatus");
                                    ActividadadesUnidad actividadadesUnidad = new ActividadadesUnidad(horainicio, horafin, fecha, id_servicio, id_personal, obsservaciones, estatus);
                                    listaActividadesUnidad.add(actividadadesUnidad);
                                }

                                if (listaActividadesUnidad.isEmpty()) {
                                    recyclerViewBitacora.setVisibility(View.GONE);
                                    LinearLayout LayoutNoActividades = requireView().findViewById(R.id.LayoutNoActividades);
                                    LayoutNoActividades.setVisibility(View.VISIBLE);
                                } else {
                                    recyclerViewBitacora.setVisibility(View.VISIBLE);
                                    LinearLayout LayoutNoActividades = requireView().findViewById(R.id.LayoutNoActividades);
                                    LayoutNoActividades.setVisibility(View.GONE);

                                    AdaptadorActividadesUnidad adaptadorActividadesUnidad = new AdaptadorActividadesUnidad(listaActividadesUnidad);
                                    recyclerViewBitacora.setLayoutManager(new LinearLayoutManager(requireContext()));
                                    recyclerViewBitacora.setAdapter(adaptadorActividadesUnidad);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            tvBitacora.setText("No hay Actividades para esta unidad");
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
                params.put("opcion", "26");
                params.put("idservicio", idservicio);
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