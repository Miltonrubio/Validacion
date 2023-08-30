package com.example.validacion;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetalleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetallesArrastres extends Fragment implements OnMapReadyCallback {

    private boolean isTwoFingerScroll = false;
    private MapView mapView;
    private GoogleMap googleMap;

    private ArrayList<MarkerInfo> markerList = new ArrayList<>();


    private double LATITUD;
    private double LONGITUD;

    private double DEST_LATITUDE;
    private double DEST_LONGITUDE;

    TextView tvstatus2, tvClienteArrastre, tvFechaInicioArrastre, tvFechaFinalArrastre;

    ImageView imageViewDetallesArrastres;

    private String urlApi = "http://192.168.1.252/georgioapi/Controllers/Apiback.php";

    RecyclerView recyclerViewChoferes;
    ScrollView scrollView;


    public DetallesArrastres() {
    }

    private Handler sliderHandler = new Handler();


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
        tvFechaFinalArrastre = rootView.findViewById(R.id.tvFechaFinalArrastre);
        tvFechaInicioArrastre = rootView.findViewById(R.id.tvFechaInicioArrastre);
        scrollView = rootView.findViewById(R.id.scrollViewArrastres); // Replace with the actual ID of your ScrollView

        Bundle bundle = getArguments();
        if (bundle != null) {

            String fecha_inicio = bundle.getString("fecha_inicio", "");
            String hora_inicio = bundle.getString("hora_inicio", "");
            String estatus = bundle.getString("estatus", "");
            String kilometros = bundle.getString("kilometros", "");
            String observaciones = bundle.getString("observaciones", "");

            String id = bundle.getString("id", "");
            CargarRutas(id);

            //Texto del fragment con validaciones
            if (!estatus.equals("")) {
                tvstatus2.setText("Estatus " + estatus);
                if (estatus.equals("pendiente")) {
                    tvstatus2.setTextColor(ContextCompat.getColor(requireContext(), R.color.amarillo));
                } else if (estatus.equals("entrega")) {
                    tvstatus2.setTextColor(ContextCompat.getColor(requireContext(), R.color.verde));
                } else if (estatus.equals("en espera")) {
                    tvstatus2.setTextColor(ContextCompat.getColor(requireContext(), R.color.amarillo));
                } else if (estatus.equals("preparado") || estatus.equals("finalizado")) {
                    tvstatus2.setTextColor(ContextCompat.getColor(requireContext(), R.color.verde));
                } else {
                    tvstatus2.setTextColor(ContextCompat.getColor(requireContext(), R.color.rojo));
                }
            } else {

                tvstatus2.setTextColor(ContextCompat.getColor(requireContext(), R.color.rojo));
                tvstatus2.setText("No hay estatus");
            }

            CargarArrastre(id, observaciones, estatus);
            mapView = rootView.findViewById(R.id.mapViewArrastres);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
            mapView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    int pointerCount = event.getPointerCount();

                    // Permitir el desplazamiento hacia arriba en el ScrollView si estás al principio de la vista
                    if (scrollView.getScrollY() == 0 && event.getY() < rootView.getHeight() / 2) {
                        scrollView.requestDisallowInterceptTouchEvent(false);
                    } else {
                        scrollView.requestDisallowInterceptTouchEvent(true);
                    }

                    if (pointerCount == 2) {
                        isTwoFingerScroll = true;
                    } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                        isTwoFingerScroll = false;
                    }

                    return false;
                }
            });
        }
        return rootView;
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

                                    tvClienteArrastre.setText("" + cliente_nombre);
                                    tvFechaInicioArrastre.setText("Comienzo: " + fecha_inicio + " - " + hora_inicio);
                                    tvFechaFinalArrastre.setText("Finalizado: " + fecha_final + "- " + hora_final);

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
                                  String  id = rutasObj.getString("id");

                                    LATITUD = Double.parseDouble(latitud_origen);
                                    LONGITUD = Double.parseDouble(longitud_origen);
                                    DEST_LATITUDE = Double.parseDouble(latitud_destino);
                                    DEST_LONGITUDE = Double.parseDouble(longitud_destino);

                                 markerList.add(new MarkerInfo(LATITUD, LONGITUD, DEST_LATITUDE, DEST_LONGITUDE, "Ruta "+ id));
                                }

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


    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        drawRoute();
        LatLng startLocation = new LatLng(LATITUD, LONGITUD);
        LatLng destLocation = new LatLng(DEST_LATITUDE, DEST_LONGITUDE);

        googleMap.addMarker(new MarkerOptions().position(startLocation).title("Salida"));
        googleMap.addMarker(new MarkerOptions().position(destLocation).title("Destino"));

        LatLng center = new LatLng((LATITUD + DEST_LATITUDE) / 2, (LONGITUD + DEST_LONGITUDE) / 2);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 15f));
    }

/*
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        drawRoute();

        getView().post(() -> {
            for (MarkerInfo markerInfo : markerList) {
                LatLng markerLocation = new LatLng(markerInfo.getLatitud_inicio(), markerInfo.getLongitud_inicio());
                googleMap.addMarker(new MarkerOptions().position(markerLocation).title(markerInfo.getTitulo()));
            }

            // Ajustar la cámara para mostrar todos los marcadores
            if (!markerList.isEmpty()) {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (MarkerInfo markerInfo : markerList) {
                    builder.include(new LatLng(markerInfo.getLatitud_inicio(), markerInfo.getLongitud_inicio()));
                }
                LatLngBounds bounds = builder.build();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100)); // 100 es el padding
            }
        });
    }
*/

    private void drawRoute() {
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyCkF9dXkDa3GjKlrLUdLc7BEx5031MELDQ")
                .build();

        DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext)
                .mode(TravelMode.DRIVING)
                .origin(new com.google.maps.model.LatLng(LATITUD, LONGITUD))
                .destination(new com.google.maps.model.LatLng(DEST_LATITUDE, DEST_LONGITUDE));

        try {
            DirectionsResult result = request.await();
            DirectionsRoute route = result.routes[0];
            String encodedPolyline = route.overviewPolyline.getEncodedPath();
            List<LatLng> decodedPath = PolyUtil.decode(encodedPolyline);

            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(decodedPath)
                    .color(Color.RED)
                    .width(5);

            Polyline polyline = googleMap.addPolyline(polylineOptions);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*
private void drawRoute() {
    GeoApiContext geoApiContext = new GeoApiContext.Builder()
            .apiKey("AIzaSyCkF9dXkDa3GjKlrLUdLc7BEx5031MELDQ")
            .build();

    DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext)
            .mode(TravelMode.DRIVING)
            .origin(new com.google.maps.model.LatLng(LATITUD, LONGITUD))
            .destination(new com.google.maps.model.LatLng(DEST_LATITUDE, DEST_LONGITUDE));

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

                // Asegurarse de que googleMap esté inicializado antes de agregar la polilínea
                if (googleMap != null) {
                    getActivity().runOnUiThread(() -> {
                        Polyline polyline = googleMap.addPolyline(polylineOptions);

                        // Ajustar la cámara para mostrar la ruta y los marcadores
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (MarkerInfo markerInfo : markerList) {
                            builder.include(new LatLng(markerInfo.getLatitud_inicio(), markerInfo.getLongitud_inicio()));
                        }
                        LatLngBounds bounds = builder.build();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
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
*/

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

}