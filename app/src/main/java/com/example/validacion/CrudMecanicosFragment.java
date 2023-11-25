package com.example.validacion;

import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorClientes;
import com.example.validacion.Adaptadores.AdaptadorMecanicos;
import com.example.validacion.Adaptadores.AdaptadorModelos;
import com.example.validacion.Adaptadores.AdaptadorProductividadMecanicos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrudMecanicosFragment extends Fragment {


    RecyclerView recyclerViewMecanicos;

    FloatingActionButton botonAgregarClientes;

    public CrudMecanicosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    Context context;
    String url ;

    AdaptadorProductividadMecanicos adaptadorProductividadMecanicos;

    ConstraintLayout LayoutSinInternet;
    ConstraintLayout LayoutSinContenido;
    RelativeLayout LayoutConContenido;

    List<JSONObject>listaMecanicos=new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_crud_mecanicos, container, false);
        recyclerViewMecanicos= view.findViewById(R.id.recyclerViewMecanicos);
        botonAgregarClientes=view.findViewById(R.id.botonAgregarClientes);
        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);

        LayoutSinInternet=view.findViewById(R.id.LayoutSinInternet);
        LayoutSinContenido=view.findViewById(R.id.LayoutSinContenido);
        LayoutConContenido=view.findViewById(R.id.LayoutConContenido);


        MostrarMecanicos();


        adaptadorProductividadMecanicos  = new AdaptadorProductividadMecanicos(listaMecanicos, context);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerViewMecanicos.setLayoutManager(gridLayoutManager);
        recyclerViewMecanicos.setAdapter(adaptadorProductividadMecanicos);

        return view;
    }


    private void MostrarMecanicos() {
        listaMecanicos.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listaMecanicos.add(jsonObject);

                    }
                    adaptadorProductividadMecanicos.notifyDataSetChanged();
                    adaptadorProductividadMecanicos.setFilteredData(listaMecanicos);
                    adaptadorProductividadMecanicos.filter("");

                    if (listaMecanicos.size() > 0) {

                        mostrarLayout("conContenido");
                    } else {

                        mostrarLayout("SinInternet");
                    }

                } catch (JSONException e) {
                    mostrarLayout("SinInternet");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mostrarLayout("SinInternet");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "48");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }



    private void mostrarLayout(String estado) {
        if (estado.equalsIgnoreCase("SinInternet")) {


            LayoutSinInternet.setVisibility(View.VISIBLE);
            LayoutConContenido.setVisibility(View.GONE);
            LayoutSinContenido.setVisibility(View.GONE);

        } else if (estado.equalsIgnoreCase("SinContenido")) {
            LayoutSinInternet.setVisibility(View.GONE);
            LayoutConContenido.setVisibility(View.GONE);
            LayoutSinContenido.setVisibility(View.VISIBLE);

        } else if (estado.equalsIgnoreCase("conContenido")) {
            LayoutSinInternet.setVisibility(View.GONE);
            LayoutConContenido.setVisibility(View.VISIBLE);
            LayoutSinContenido.setVisibility(View.GONE);
        }

//        onLoadComplete();
    }


}