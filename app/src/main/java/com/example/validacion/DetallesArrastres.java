package com.example.validacion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetallesArrastres extends Fragment implements OnMapReadyCallback {

    private boolean isTwoFingerScroll = false;
    private MapView mapView;
    private GoogleMap googleMap;
    private Polyline selectedPolyline;

    private List<Polyline> routePolylines = new ArrayList<>();
    private ArrayList<MarkerInfo> markerList = new ArrayList<>();


    private double LATITUD;
    private double LONGITUD;

    private double DEST_LATITUDE;
    private double DEST_LONGITUDE;

    TextView tvstatus2, tvClienteArrastre, tvFechaInicioArrastre, tvFechaFinalArrastre, TVTotalKilometros, TVPrecio, tvTelefonoCliente;

    LinearLayout LayoutKilometros, LayoutPrecio, LayoutFinalizar, LayouttodasLasRutas;

    Button BotonFinalizarArrastre;
    private String urlApi = "http://tallergeorgio.hopto.org:5611/georgioapp/georgioapi/Controllers/Apiback.php";

    RecyclerView recyclerViewChoferes, recyclerViewRutas;
    ScrollView scrollView;

    String apiKey="AIzaSyCkF9dXkDa3GjKlrLUdLc7BEx5031MELDQ";


    public DetallesArrastres() {
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
        View rootView = inflater.inflate(R.layout.fragment_detalles_arrastres, container, false);
        tvstatus2 = rootView.findViewById(R.id.tvstatus2);
        tvClienteArrastre = rootView.findViewById(R.id.tvClienteArrastre);
        recyclerViewChoferes = rootView.findViewById(R.id.recyclerViewChoferes);
        recyclerViewRutas = rootView.findViewById(R.id.recyclerViewRutas);
        tvFechaFinalArrastre = rootView.findViewById(R.id.tvFechaFinalArrastre);
        tvFechaInicioArrastre = rootView.findViewById(R.id.tvFechaInicioArrastre);
        scrollView = rootView.findViewById(R.id.scrollViewArrastres); // Replace with the actual ID of your ScrollView
        BotonFinalizarArrastre = rootView.findViewById(R.id.BotonFinalizarArrastre);
        LayoutKilometros = rootView.findViewById(R.id.LayoutKilometros);
        TVTotalKilometros = rootView.findViewById(R.id.TVTotalKilometros);
        LayoutPrecio = rootView.findViewById(R.id.LayoutPrecio);
        LayoutFinalizar = rootView.findViewById(R.id.LayoutFinalizar);
        LayouttodasLasRutas= rootView.findViewById(R.id.LayouttodasLasRutas);
        TVPrecio = rootView.findViewById(R.id.TVPrecio);
        tvTelefonoCliente= rootView.findViewById(R.id.tvTelefonoCliente);

        LayouttodasLasRutas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawRoute();
            }
        });

        Bundle bundle = getArguments();

        if (bundle != null) {
            String estatus = bundle.getString("estatus", "");
            String observaciones = bundle.getString("observaciones", "");
            String id = bundle.getString("id", "");
            String telefono = bundle.getString("telefono", "");
            CargarRutas(id);

            tvTelefonoCliente.setText("+52 "+ telefono);
            BotonFinalizarArrastre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FinalizarArrastre(id);
                }
            });

            if (!estatus.equals("")) {
                tvstatus2.setText("Estatus " + estatus);
                if (estatus.equals("pendiente")) {
                    tvstatus2.setTextColor(ContextCompat.getColor(requireContext(), R.color.amarillo));
                } else if (estatus.equals("entrega")) {
                    tvstatus2.setTextColor(ContextCompat.getColor(requireContext(), R.color.verde));
                } else if (estatus.equals("en espera")) {
                    tvstatus2.setTextColor(ContextCompat.getColor(requireContext(), R.color.amarillo));
                } else if (estatus.equals("preparado") || estatus.equals("finalizado")|| estatus.equals("FINALIZADO")) {
                    tvstatus2.setTextColor(ContextCompat.getColor(requireContext(), R.color.verde));
                    LayoutFinalizar.setVisibility(View.GONE);
                } else {
                    tvstatus2.setTextColor(ContextCompat.getColor(requireContext(), R.color.rojo));

                    LayoutFinalizar.setVisibility(View.GONE);
                }
            } else {

                LayoutFinalizar.setVisibility(View.GONE);
                tvstatus2.setText("No hay estatus");
            }

            CargarArrastre(id, observaciones, estatus);
            mapView = rootView.findViewById(R.id.mapViewArrastres);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }
        return rootView;
    }

    @SuppressLint("ResourceAsColor")
    private void ImprimirDatosEnFragment(String cliente_nombre, String fecha_inicio, String hora_inicio, String fecha_final, String hora_final, String kilometros, String costoKilometro) {


        if (kilometros.equals(0) || kilometros.equals("null") || kilometros.isEmpty()) {
            LayoutKilometros.setVisibility(View.INVISIBLE);
            TVPrecio.setText("El costo por kilometro es:" + costoKilometro);
        } else {
            Double costoTotal = (Double.parseDouble(kilometros)) * (Double.parseDouble(costoKilometro));

            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String costoTotalFormateado = decimalFormat.format(costoTotal);

            DecimalFormat decimalFormat2 = new DecimalFormat("#.##");
            String kilometrosFormateado = decimalFormat2.format(Double.parseDouble(kilometros));

            TVTotalKilometros.setText("Total de kilometros: " + kilometrosFormateado + " km");
            TVPrecio.setText("Importe total: " + costoTotalFormateado + "$ MXM");
        }

        if (cliente_nombre.isEmpty() || cliente_nombre.equals("null")) {
            tvClienteArrastre.setText("Cliente desconocido");
            LayoutFinalizar.setVisibility(View.GONE);
        } else {
            tvClienteArrastre.setText(cliente_nombre);
        }

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(fecha_inicio);

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new DateFormatSymbols(new Locale("es", "ES")));
            String fecha_formateada = outputFormat.format(date);

            SimpleDateFormat inputFormatHora = new SimpleDateFormat("HH:mm:ss");
            Date time = inputFormatHora.parse(hora_inicio);

            SimpleDateFormat outputFormatHora = new SimpleDateFormat("hh:mm a");
            String hora_formateada = outputFormatHora.format(time);

            tvFechaInicioArrastre.setText("Comienzo: " + "\nEl " + fecha_formateada + " A las " + hora_formateada);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (fecha_final.isEmpty() || hora_final.isEmpty() || hora_final.equals("null") || fecha_final.equals("null")) {
            tvFechaFinalArrastre.setVisibility(View.VISIBLE);
            tvFechaFinalArrastre.setText("Arrastre sin finalizar");
            tvFechaFinalArrastre.setTextColor(R.color.rojo);
        } else {
            tvFechaFinalArrastre.setVisibility(View.VISIBLE);

            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date_final = inputFormat.parse(fecha_final);

                SimpleDateFormat outputFormatFecha = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new DateFormatSymbols(new Locale("es", "ES")));
                String fecha_formateada_final = outputFormatFecha.format(date_final);

                SimpleDateFormat inputFormatHora = new SimpleDateFormat("HH:mm:ss");
                Date time_final = inputFormatHora.parse(hora_final);

                SimpleDateFormat outputFormatHora = new SimpleDateFormat("hh:mm a");
                String hora_formateada_final = outputFormatHora.format(time_final);

                tvFechaFinalArrastre.setText("Finalizado:\nEl " + fecha_formateada_final + " A las " + hora_formateada_final);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void CargarArrastre(String id_arrastre, String observaciones, String estatus) {
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, urlApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Choferes> listaChoferes = new ArrayList<>();

                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject arrastreObj = jsonArray.getJSONObject(i);
                                    String cliente_telefono = arrastreObj.getString("cliente_telefono");
                                    String cliente_nombre = arrastreObj.getString("cliente_nombre");
                                    String foto_mapa = arrastreObj.getString("foto_mapa");
                                    String fecha_inicio = arrastreObj.getString("fecha_inicio");
                                    String hora_inicio = arrastreObj.getString("hora_inicio");
                                    String fecha_final = arrastreObj.getString("fecha_final");
                                    String hora_final = arrastreObj.getString("hora_final");
                                    String chofer_nombre = arrastreObj.getString("chofer_nombre");
                                    String unidad_marca = arrastreObj.getString("unidad_marca");
                                    String unidad_modelo = arrastreObj.getString("unidad_modelo");
                                    String kilometros = arrastreObj.getString("kilometros");
                                    String importe = arrastreObj.getString("importe");
                                    String costo_kilometro = arrastreObj.getString("costo_kilometro");

                                    listaChoferes.add(new Choferes(id_arrastre, chofer_nombre, unidad_marca, unidad_modelo, cliente_nombre, foto_mapa, fecha_inicio, hora_inicio,
                                            fecha_final, hora_final, kilometros, costo_kilometro, importe, observaciones, estatus));

                                    ImprimirDatosEnFragment(cliente_nombre, fecha_inicio, hora_inicio, fecha_final, hora_final, kilometros, costo_kilometro);

                                }

                                AdaptadorChoferes adaptadorChoferes = new AdaptadorChoferes(listaChoferes);
                                LinearLayoutManager layoutManager2 = new LinearLayoutManager(requireContext());
                                recyclerViewChoferes.setLayoutManager(layoutManager2);
                                recyclerViewChoferes.setAdapter(adaptadorChoferes);

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
                params.put("opcion", "13");
                params.put("id", id_arrastre);
                return params;
            }
        };

        RequestQueue requestQueue3 = Volley.newRequestQueue(requireContext());
        requestQueue3.add(stringRequest3);

    }


    private void CargarRutas(String id_arrastre) {
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, urlApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                String latitud_origen;
                                String longitud_origen;
                                String latitud_destino;
                                String longitud_destino;
                                String kilometros;

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject rutasObj = jsonArray.getJSONObject(i);
                                    latitud_origen = rutasObj.getString("latitud_origen");
                                    longitud_origen = rutasObj.getString("longitud_origen");
                                    latitud_destino = rutasObj.getString("latitud_destino");
                                    longitud_destino = rutasObj.getString("longitud_destino");
                                    kilometros = rutasObj.getString("kilometros");
                                    String id = rutasObj.getString("id");

                                    LATITUD = Double.parseDouble(latitud_origen);
                                    LONGITUD = Double.parseDouble(longitud_origen);
                                    DEST_LATITUDE = Double.parseDouble(latitud_destino);
                                    DEST_LONGITUDE = Double.parseDouble(longitud_destino);

                                    markerList.add(new MarkerInfo(LATITUD, LONGITUD, DEST_LATITUDE, DEST_LONGITUDE, "Ruta " + id));
                                }

                                AdaptadorRutas adaptadorRutas = new AdaptadorRutas(markerList);

                                adaptadorRutas.setOnItemClickListener(new AdaptadorRutas.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        MarkerInfo markerInfo = markerList.get(position);
                                        drawSelectedRoute(position);
                                    }
                                });

                                LinearLayoutManager layoutManager3 = new LinearLayoutManager(requireContext());
                                recyclerViewRutas.setLayoutManager(layoutManager3);
                                recyclerViewRutas.setAdapter(adaptadorRutas);


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
                params.put("opcion", "14");
                params.put("id_arrastre", id_arrastre);
                return params;
            }
        };

        RequestQueue requestQueue3 = Volley.newRequestQueue(requireContext());
        requestQueue3.add(stringRequest3);
    }


    private void clearRoutePolylines() {
        for (Polyline polyline : routePolylines) {
            polyline.remove();
        }
        routePolylines.clear();
    }


    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        getView().post(() -> {
            drawRoute();
        });
    }

    private void drawSelectedRoute(int selectedRouteIndex) {
        clearRoutePolylines();

        if (selectedRouteIndex < 0 || selectedRouteIndex >= markerList.size()) {
            // Verificar si el índice seleccionado es válido
            drawRoute();
            return;
        }

        MarkerInfo selectedMarkerInfo = markerList.get(selectedRouteIndex);
        LatLng markerStart = new LatLng(selectedMarkerInfo.getLatitud_inicio(), selectedMarkerInfo.getLongitud_inicio());
        LatLng markerDest = new LatLng(selectedMarkerInfo.getLatitud_destino(), selectedMarkerInfo.getLongitud_destino());

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            List<Address> startAddresses = geocoder.getFromLocation(selectedMarkerInfo.getLatitud_inicio(), selectedMarkerInfo.getLongitud_inicio(), 1);
            List<Address> destAddresses = geocoder.getFromLocation(selectedMarkerInfo.getLatitud_destino(), selectedMarkerInfo.getLongitud_destino(), 1);

            if (!startAddresses.isEmpty() && !destAddresses.isEmpty()) {
                Address startAddressObj = startAddresses.get(0);
                Address destAddressObj = destAddresses.get(0);

                String startAddress = startAddressObj.getThoroughfare() + ", #" + startAddressObj.getSubThoroughfare() + ", " + startAddressObj.getSubLocality() + " (Inicio)";
                String destAddress = destAddressObj.getThoroughfare() + ", #" + destAddressObj.getSubThoroughfare() + ", " + destAddressObj.getSubLocality() + " (Destino)";

                googleMap.addMarker(new MarkerOptions().position(markerStart).title(startAddress));
                googleMap.addMarker(new MarkerOptions().position(markerDest).title(destAddress));

                GeoApiContext geoApiContext = new GeoApiContext.Builder()
                        .apiKey(apiKey)
                        .build();

                DirectionsApiRequest request2 = DirectionsApi.newRequest(geoApiContext)
                        .mode(TravelMode.DRIVING)
                        .origin(new com.google.maps.model.LatLng(selectedMarkerInfo.getLatitud_inicio(), selectedMarkerInfo.getLongitud_inicio()))
                        .destination(new com.google.maps.model.LatLng(selectedMarkerInfo.getLatitud_destino(), selectedMarkerInfo.getLongitud_destino()));

                request2.setCallback(new PendingResult.Callback<DirectionsResult>() {
                    @Override
                    public void onResult(DirectionsResult result) {
                        if (result.routes != null && result.routes.length > 0) {
                            DirectionsRoute route = result.routes[0];
                            String encodedPolyline = route.overviewPolyline.getEncodedPath();
                            List<LatLng> decodedPath = PolyUtil.decode(encodedPolyline);

                            PolylineOptions polylineOptions = new PolylineOptions()
                                    .addAll(decodedPath)
                                    .color(Color.RED)
                                    .width(5);

                            if (googleMap != null) {
                                getActivity().runOnUiThread(() -> {
                                    Polyline polyline = googleMap.addPolyline(polylineOptions);
                                    routePolylines.add(polyline);

                                    // Crear un nuevo builder para incluir todos los puntos de la ruta
                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    for (LatLng point : decodedPath) {
                                        builder.include(point);
                                    }
                                    LatLngBounds bounds = builder.build();

                                    int padding = 100;
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                    googleMap.animateCamera(cameraUpdate);
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void drawRoute() {
        clearRoutePolylines();

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        LatLngBounds.Builder builder = new LatLngBounds.Builder(); // Paso 1: Crear un builder para las coordenadas

        for (MarkerInfo markerInfo : markerList) {
            LatLng markerStart = new LatLng(markerInfo.getLatitud_inicio(), markerInfo.getLongitud_inicio());
            LatLng markerDest = new LatLng(markerInfo.getLatitud_destino(), markerInfo.getLongitud_destino());

            try {
                List<Address> startAddresses = geocoder.getFromLocation(markerInfo.getLatitud_inicio(), markerInfo.getLongitud_inicio(), 1);
                List<Address> destAddresses = geocoder.getFromLocation(markerInfo.getLatitud_destino(), markerInfo.getLongitud_destino(), 1);

                if (!startAddresses.isEmpty() && !destAddresses.isEmpty()) {
                    Address startAddressObj = startAddresses.get(0);
                    Address destAddressObj = destAddresses.get(0);

                    String startAddress = startAddressObj.getThoroughfare() + ", #" + startAddressObj.getSubThoroughfare() + ", " + startAddressObj.getSubLocality() + " (Inicio)";
                    String destAddress = destAddressObj.getThoroughfare() + ", #" + destAddressObj.getSubThoroughfare() + ", " + destAddressObj.getSubLocality() + " (Destino)";

                    googleMap.addMarker(new MarkerOptions().position(markerStart).title(startAddress));
                    googleMap.addMarker(new MarkerOptions().position(markerDest).title(destAddress));

                    // Paso 2: Agregar las coordenadas al builder
                    builder.include(markerStart);
                    builder.include(markerDest);

                    GeoApiContext geoApiContext = new GeoApiContext.Builder()
                            .apiKey(apiKey)
                            .build();

                    DirectionsApiRequest request2 = DirectionsApi.newRequest(geoApiContext)
                            .mode(TravelMode.DRIVING)
                            .origin(new com.google.maps.model.LatLng(markerInfo.getLatitud_inicio(), markerInfo.getLongitud_inicio()))
                            .destination(new com.google.maps.model.LatLng(markerInfo.getLatitud_destino(), markerInfo.getLongitud_destino()));

                    request2.setCallback(new PendingResult.Callback<DirectionsResult>() {
                        @Override
                        public void onResult(DirectionsResult result) {
                            if (result.routes != null && result.routes.length > 0) {
                                DirectionsRoute route = result.routes[0];
                                String encodedPolyline = route.overviewPolyline.getEncodedPath();
                                List<LatLng> decodedPath = PolyUtil.decode(encodedPolyline);

                                PolylineOptions polylineOptions = new PolylineOptions()
                                        .addAll(decodedPath)
                                        .color(Color.RED)
                                        .width(5);

                                if (googleMap != null) {
                                    getActivity().runOnUiThread(() -> {
                                        Polyline polyline = googleMap.addPolyline(polylineOptions);
                                        routePolylines.add(polyline);

                                        // Paso 4: Ajustar la cámara para que se centre en las coordenadas
                                        LatLngBounds bounds = builder.build();
                                        int padding = 180;
                                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                        googleMap.animateCamera(cameraUpdate);
                                    });
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    private void FinalizarArrastre(String id_arrastre) {
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, urlApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            Toast.makeText(requireContext(), "Se finalizo el arrastre", Toast.LENGTH_SHORT).show();
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
                params.put("opcion", "17");
                params.put("id", id_arrastre);
                return params;
            }
        };

        RequestQueue requestQueue3 = Volley.newRequestQueue(requireContext());
        requestQueue3.add(stringRequest3);
    }


    private void showDrivingRoute(MarkerInfo markerInfo) {
        // Limpia cualquier Polyline previamente seleccionada
        if (selectedPolyline != null) {
            // Si ya hay una Polyline seleccionada, quítala
            selectedPolyline.remove();
        }

        // Obtén los puntos de inicio y destino
        LatLng markerStart = new LatLng(markerInfo.getLatitud_inicio(), markerInfo.getLongitud_inicio());
        LatLng markerDest = new LatLng(markerInfo.getLatitud_destino(), markerInfo.getLongitud_destino());

        // Agrega marcadores para el inicio y el destino
        googleMap.addMarker(new MarkerOptions().position(markerStart).title("Inicio"));
        googleMap.addMarker(new MarkerOptions().position(markerDest).title("Destino"));

        // Crea una nueva Polyline para resaltar la ruta seleccionada
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(markerStart)
                .add(markerDest)
                .color(Color.BLUE)  // Cambia el color como desees
                .width(7);           // Cambia el ancho como desees

        selectedPolyline = googleMap.addPolyline(polylineOptions);

        // Calcula la cámara para centrar y hacer zoom en la ruta seleccionada
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(markerStart);
        builder.include(markerDest);
        LatLngBounds bounds = builder.build();

        int padding = 80;  // Ajusta el valor del relleno según tus preferencias
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cameraUpdate);

        // Traza la ruta como si fuera conduciendo
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyCkF9dXkDa3GjKlrLUdLc7BEx5031MELDQ")  // Reemplaza con tu clave de API de Google Maps
                .build();

        DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext)
                .mode(TravelMode.DRIVING)  // Cambia el modo de transporte a "DRIVING"
                .origin(new com.google.maps.model.LatLng(markerInfo.getLatitud_inicio(), markerInfo.getLongitud_inicio()))
                .destination(new com.google.maps.model.LatLng(markerInfo.getLatitud_destino(), markerInfo.getLongitud_destino()));

        request.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                if (result.routes != null && result.routes.length > 0) {
                    DirectionsRoute route = result.routes[0];
                    String encodedPolyline = route.overviewPolyline.getEncodedPath();
                    List<LatLng> decodedPath = PolyUtil.decode(encodedPolyline);

                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(decodedPath)
                            .color(Color.RED)
                            .width(5);

                    if (googleMap != null) {
                        getActivity().runOnUiThread(() -> {
                            Polyline polyline = googleMap.addPolyline(polylineOptions);
                            routePolylines.add(polyline);
                        });
                    }
                }
            }

            @Override
            public void onFailure(Throwable e) {
                e.printStackTrace();
            }
        });
    }


    public void onItemClick(int position) {

        MarkerInfo markerInfo = markerList.get(position);
    }
}