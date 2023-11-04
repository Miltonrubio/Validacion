package com.example.validacion;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorChecks;
import com.example.validacion.Adaptadores.AdaptadorClientes;
import com.example.validacion.Objetos.Cheks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClientesFragment extends Fragment {


    public ClientesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    Context context;
    String url;

    RecyclerView recyclerViewCliente;

    AdaptadorClientes adaptadorClientes;
    List<JSONObject> listaClientes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);
        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);

        EditText searchEditText = view.findViewById(R.id.searchEditText);
        recyclerViewCliente = view.findViewById(R.id.recyclerViewClientes);
        CargarClientes();


        adaptadorClientes = new AdaptadorClientes(listaClientes, context);
        recyclerViewCliente.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewCliente.setAdapter(adaptadorClientes);


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptadorClientes.filter(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }


    public void CargarClientes() {

        listaClientes.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    listaClientes.add(jsonObject);
                                }

                                adaptadorClientes.notifyDataSetChanged();
                                adaptadorClientes.setFilteredData(listaClientes);
                                adaptadorClientes.filter("");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
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
                params.put("opcion", "19");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


}