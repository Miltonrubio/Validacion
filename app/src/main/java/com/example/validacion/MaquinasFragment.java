package com.example.validacion;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorGavetas;
import com.example.validacion.Adaptadores.AdaptadorMaquinas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaquinasFragment extends Fragment {

    public MaquinasFragment() {
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

    List<JSONObject>listaMaquinas=new ArrayList<>();
    AdaptadorMaquinas adaptadorMaquinas;

    ConstraintLayout ContenedorContenido;
    ConstraintLayout ContenedorSinContenido;
    ConstraintLayout ContenedorSinInternet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_maquinas, container, false);
        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);

        RecyclerView reciclerViewMaquinas = view.findViewById(R.id.reciclerViewMaquinas);
        ContenedorSinContenido = view.findViewById(R.id.ContenedorSinContenido);
        ContenedorContenido = view.findViewById(R.id.ContenedorContenido);
        ContenedorSinInternet = view.findViewById(R.id.ContenedorSinInternet);
        FloatingActionButton botonAgregarMaquina=view.findViewById(R.id.botonAgregarMaquina);


        reciclerViewMaquinas.setLayoutManager(new LinearLayoutManager(context));
        adaptadorMaquinas = new AdaptadorMaquinas(listaMaquinas, context);
        reciclerViewMaquinas.setAdapter(adaptadorMaquinas);

        MostrarMaquinas();


        botonAgregarMaquina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_agregar_maquina, null);
                builder.setView(ModalRedondeado(view.getContext(), customView));
                AlertDialog dialogAgregarMaquina = builder.create();
                dialogAgregarMaquina.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogAgregarMaquina.show();


                TextInputEditText fechaAdqui= customView.findViewById(R.id.fechaAdqui);

                fechaAdqui.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            }
        });



        return view;
    }



    private void MostrarMaquinas() {
        listaMaquinas.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listaMaquinas.add(jsonObject);

                    }
                    adaptadorMaquinas.notifyDataSetChanged();
                    adaptadorMaquinas.setFilteredData(listaMaquinas);
                    adaptadorMaquinas.filter("");

                    if (listaMaquinas.size() > 0) {

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
                params.put("opcion", "51");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }




    private void mostrarLayout(String estado) {
        if (estado.equalsIgnoreCase("SinInternet")) {
            ContenedorSinInternet.setVisibility(View.VISIBLE);
            ContenedorContenido.setVisibility(View.GONE);
            ContenedorSinContenido.setVisibility(View.GONE);

        } else if (estado.equalsIgnoreCase("SinContenido")) {
            ContenedorSinInternet.setVisibility(View.GONE);
            ContenedorContenido.setVisibility(View.GONE);
            ContenedorSinContenido.setVisibility(View.VISIBLE);

        } else if (estado.equalsIgnoreCase("conContenido")) {
            ContenedorSinInternet.setVisibility(View.GONE);
            ContenedorContenido.setVisibility(View.VISIBLE);
            ContenedorSinContenido.setVisibility(View.GONE);
        } else {

            ContenedorSinInternet.setVisibility(View.GONE);
            ContenedorContenido.setVisibility(View.GONE);
            ContenedorSinContenido.setVisibility(View.VISIBLE);
        }

//        onLoadComplete();
    }

}