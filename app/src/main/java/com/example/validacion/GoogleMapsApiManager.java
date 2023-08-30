package com.example.validacion;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleMapsApiManager {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    private static final String GEOCODE_ENDPOINT = "geocode/json";
    private static final String API_KEY = "AIzaSyCkF9dXkDa3GjKlrLUdLc7BEx5031MELDQ";

    private Context context;
    private RequestQueue requestQueue;

    public GoogleMapsApiManager(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void geocodeAddress(String address, final GeocodeCallback callback) {
        String url = BASE_URL + GEOCODE_ENDPOINT + "?address=" + address + "&key=" + API_KEY;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                });

        requestQueue.add(request);
    }

    public interface GeocodeCallback {
        void onSuccess(JSONObject response);
        void onError(VolleyError error);
    }

}
