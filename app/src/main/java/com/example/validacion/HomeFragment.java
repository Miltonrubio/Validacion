package com.example.validacion;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Activity_Binding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

 public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private Adapt adapter;


    private List<JSONObject> dataList = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;


    }
     @Override
     public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
         super.onViewCreated(view, savedInstanceState);

         recyclerView = view.findViewById(R.id.recyclerViewFragment);
         recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

         adapter = new Adapt(dataList, requireContext());
         recyclerView.setAdapter(adapter);

         // Llama a EnviarWS solo una vez al iniciar la vista del fragmento
         EnviarWS();
     }

     private void EnviarWS() {
         String url = "http://192.168.1.252/georgioapi/Controllers/Apiback.php/";

         StringRequest postrequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 try {
                     JSONArray jsonArray = new JSONArray(response);

                     // Limpia la lista de datos antes de agregar nuevos elementos
                     dataList.clear();

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

         // Utiliza getContext() o requireContext() para obtener el contexto del Fragment
         Volley.newRequestQueue(requireContext()).add(postrequest);
     }

}

