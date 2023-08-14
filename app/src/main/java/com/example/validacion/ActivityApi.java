package com.example.validacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityApi extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adapt adapter;
    private List<JSONObject> dataList = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

/*
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new Adapt(dataList);
        recyclerView.setAdapter(adapter);
*/
     //   EnviarWS();
    }

    /*
        private void LeerWs() {

            String url = "https://jsonplaceholder.typicode.com/todos/1";

            StringRequest postrequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        etmarca.setText(jsonObject.getString("userId"));
                        etmodelo.setText(jsonObject.getString("title"));
                        etanio.setText(jsonObject.getString("completed"));


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            Volley.newRequestQueue(this).add(postrequest);

        }
    */

    /*
    private void EnviarWS() {
        String url = "http://192.168.1.252/georgioapi/Controllers/Apiback.php";

        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        dataList.add(jsonObject); // Agrega cada objeto JSON a la lista
                    }

                    adapter.notifyDataSetChanged(); // Notifica al adaptador sobre los nuevos datos

                } catch (JSONException e) {
                    Log.e("Error", "JSONException: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "VolleyError: " + error.getMessage());
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "2");

                return params;
            }
        };

        Volley.newRequestQueue(this).add(postrequest);
    }*/

}