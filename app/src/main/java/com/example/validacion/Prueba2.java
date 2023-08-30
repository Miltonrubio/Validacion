package com.example.validacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import java.util.List;

public class Prueba2 extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;

    private double LATITUD = 18.467;
    private double LONGITUD = -97.4181;

    private double DEST_LATITUDE = 18.477;
    private double DEST_LONGITUDE = -97.4191;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba2);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        // Draw a route between the two points
        drawRoute();

        // Add markers for the starting and destination points
        LatLng startLocation = new LatLng(LATITUD, LONGITUD);
        LatLng destLocation = new LatLng(DEST_LATITUDE, DEST_LONGITUDE);

        googleMap.addMarker(new MarkerOptions().position(startLocation).title("Starting Location"));
        googleMap.addMarker(new MarkerOptions().position(destLocation).title("Destination"));

        // Move camera to the center of the locations
        LatLng center = new LatLng((LATITUD + DEST_LATITUDE) / 2, (LONGITUD + DEST_LONGITUDE) / 2);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 15f));
    }


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
