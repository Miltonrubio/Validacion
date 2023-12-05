package com.example.validacion;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorClientes;
import com.example.validacion.Adaptadores.AdaptadorMecanicos;
import com.example.validacion.Adaptadores.AdaptadorModelos;
import com.example.validacion.Adaptadores.AdaptadorProductividadMecanicos;
import com.example.validacion.Adaptadores.Utiles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrudMecanicosFragment extends Fragment implements AdaptadorProductividadMecanicos.OnActivityActionListener {


    RecyclerView recyclerViewMecanicos;

    // FloatingActionButton botonAgregarClientes;

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
    String url;

    AdaptadorProductividadMecanicos adaptadorProductividadMecanicos;

    ConstraintLayout LayoutSinInternet;
    ConstraintLayout LayoutSinContenido;
    RelativeLayout LayoutConContenido;

    List<JSONObject> listaMecanicos = new ArrayList<>();


    AlertDialog modalCargando;
    AlertDialog.Builder builder;
    LottieAnimationView lottieNoClientes;
    TextView TextSinResultados;
    EditText searchEditText;

    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crud_mecanicos, container, false);
        recyclerViewMecanicos = view.findViewById(R.id.recyclerViewMecanicos);
        //      botonAgregarClientes=view.findViewById(R.id.botonAgregarClientes);
        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);

        LayoutSinInternet = view.findViewById(R.id.LayoutSinInternet);
        LayoutSinContenido = view.findViewById(R.id.LayoutSinContenido);
        LayoutConContenido = view.findViewById(R.id.LayoutConContenido);
        searchEditText = view.findViewById(R.id.searchEditText);
        lottieNoClientes = view.findViewById(R.id.lottieNoClientes);
        TextSinResultados = view.findViewById(R.id.TextSinResultados);

        swipeRefreshLayout=view.findViewById(R.id.swipeRefresh);


        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        MostrarMecanicos();


        adaptadorProductividadMecanicos = new AdaptadorProductividadMecanicos(listaMecanicos, context, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerViewMecanicos.setLayoutManager(gridLayoutManager);
        recyclerViewMecanicos.setAdapter(adaptadorProductividadMecanicos);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                MostrarMecanicos();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptadorProductividadMecanicos.filter(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        return view;
    }


    private void animacionLupe(String estado) {
        if (estado.equals("Oculto")) {
            lottieNoClientes.setVisibility(View.GONE);
            TextSinResultados.setVisibility(View.GONE);
        } else {
            lottieNoClientes.setVisibility(View.VISIBLE);
            TextSinResultados.setVisibility(View.VISIBLE);
        }
    }


    private void MostrarMecanicos() {
        listaMecanicos.clear();
        modalCargando = Utiles.ModalCargando(context, builder);
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
                params.put("opcion", "60");
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

        onLoadComplete();
    }


    private void onLoadComplete() {
        if (modalCargando.isShowing() && modalCargando != null) {
            modalCargando.dismiss();
        }
    }


    @Override
    public void onFilterData(Boolean resultados) {
        if (resultados) {
            animacionLupe("Oculto");
        } else {
            if ((searchEditText.getText().toString().equals("") || searchEditText.getText().toString().isEmpty())) {
                animacionLupe("Oculto");
            } else {
                animacionLupe("Visible");
            }
        }
    }
}