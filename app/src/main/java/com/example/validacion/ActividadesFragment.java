package com.example.validacion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorActividades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActividadesFragment extends Fragment implements AdaptadorActividades.OnActivityActionListener {


    private List<JSONObject> dataList = new ArrayList<>();

Context context;
    private List<JSONObject> datosFiltrados = new ArrayList<>();
    private AdaptadorActividades adaptadorActividades;
    RecyclerView recyclerViewActividades;
    String url;

    EditText searchEditTextActividades;
    String idusuario;

    public ActividadesFragment() {
        // Required empty public constructor
    }

    public static ActividadesFragment newInstance(String param1, String param2) {
        ActividadesFragment fragment = new ActividadesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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

        View view = inflater.inflate(R.layout.fragment_actividades, container, false);

        url = context.getResources().getString(R.string.ApiBack);
        context= requireContext();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        idusuario = sharedPreferences.getString("idusuario", "");


        recyclerViewActividades = view.findViewById(R.id.recyclerViewActividades);

        searchEditTextActividades = view.findViewById(R.id.searchEditTextActividades);
        recyclerViewActividades.setLayoutManager(new LinearLayoutManager(getContext()));

        if (isAdded()) {
            adaptadorActividades = new AdaptadorActividades(dataList, context, this);
        }
        recyclerViewActividades.setAdapter(adaptadorActividades);


        TomarActividadesDesdeApi(idusuario);
        return view;
    }


    private void mostrarDatosFiltrados() {
        datosFiltrados.clear();
        for (JSONObject jsonObject : dataList) {
            try {
                String estatus = jsonObject.getString("estatus");

                if (estatus.equals("activo") || estatus.equals("pendiente") || estatus.equals("pausada")) {
                    datosFiltrados.add(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adaptadorActividades.setFilteredData(datosFiltrados);
        adaptadorActividades.notifyDataSetChanged();

    }


    public void TomarActividadesDesdeApi(String idmecanico) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    dataList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        dataList.add(jsonObject);
                    }
                    adaptadorActividades.notifyDataSetChanged();
                    adaptadorActividades.setFilteredData(dataList);
                    adaptadorActividades.filter("");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                searchEditTextActividades.setText("");
                mostrarDatosFiltrados();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "23");
                params.put("idmecanico", idmecanico);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    @Override
    public void onActualizarEstadoActividadesActivity(String idbitacora, String estatus) {
        //   ActualizarEstadoActividades(idbitacora, estatus);
        ActualizarEstadoActividades(idbitacora, estatus);
    }


    public void ActualizarEstadoActividades(String idbitacora, String estatus) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                TomarActividadesDesdeApi(idusuario);
                if (response.equals("\"esta activo\"")) {

                    Toast.makeText(context, "Ya tienes una actividad iniciada", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(context, "Se actualizo la actividad", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context, "Error al actualizar el estado de la actividad", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "27");
                params.put("idbitacora", idbitacora);
                params.put("estatus", estatus);
                params.put("idmecanico", idusuario);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


}
