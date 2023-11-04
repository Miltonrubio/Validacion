package com.example.validacion;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorChecks;
import com.example.validacion.Objetos.Cheks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckListFragment extends Fragment implements AdaptadorChecks.AdaptadorListener /* implements AdaptadorChecks.OnCheckUpdatedListener*/ {

    List<Cheks> listaChecks = new ArrayList<>();
    int valoresVacios = 0;

    RecyclerView reciclerViewCheck;

    private TextView TVResultadoChecks; // Declarar el TextView

    private String url;
    Context context;

    public CheckListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_check_list, container, false);
        reciclerViewCheck = rootView.findViewById(R.id.reciclerViewCheck);

        TVResultadoChecks = rootView.findViewById(R.id.TVResultadoChecks); // Obtener referencia del TextView
        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String idventa = bundle.getString("idventa", "");

            CargarChecks(idventa);
        }
        return rootView;

    }


    public void CargarChecks(String idventa) {

        listaChecks.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String idcheck = jsonObject.getString("idcheck");
                                    String id_ser_venta = jsonObject.getString("id_ser_venta");
                                    String descripcion = jsonObject.getString("descripcion");
                                    String comentarios = jsonObject.getString("comentarios");
                                    String urlfoto = jsonObject.getString("urlfoto");
                                    String categoria = jsonObject.getString("categoria");
                                    String valorcheck = jsonObject.getString("valorcheck");
                                    String fechacheck = jsonObject.getString("fechacheck");
                                    String horacheck = jsonObject.getString("horacheck");
                                    String estado = jsonObject.getString("estado");


                                    Cheks checks = new Cheks(descripcion, categoria, comentarios, id_ser_venta, urlfoto, valorcheck, fechacheck, horacheck, estado, idcheck);
                                    listaChecks.add(checks);

                                    if (TextUtils.isEmpty(valorcheck) || valorcheck.equals("PENDIENTE")) {
                                        valoresVacios++; // Incrementa el contador de valores vacíos
                                    }

                                }

                                AdaptadorChecks adaptadorChecks = new AdaptadorChecks(listaChecks);
                                adaptadorChecks.setAdaptadorListener(CheckListFragment.this); // Llamando al método en la instancia del adaptador
                                reciclerViewCheck.setLayoutManager(new LinearLayoutManager(context));
                                reciclerViewCheck.setAdapter(adaptadorChecks);


                                int totalResultados = listaChecks.size();
                                int totalNoFaltantes = totalResultados - valoresVacios;


                                if (totalNoFaltantes == totalResultados) {
                                    TVResultadoChecks.setVisibility(View.INVISIBLE);
                                } else {
                                    TVResultadoChecks.setText("Revisados: " + totalNoFaltantes + " / " + totalResultados);
                                }


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
                params.put("opcion", "11");
                params.put("idventa", idventa);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    public void onCheckUpdated(int position, String valorCheck, int valoresVaciosChecks, int totalValores) {
        int calculo = totalValores - valoresVaciosChecks;

        if (calculo != 0) {
            TVResultadoChecks.setText("Revisados: " + calculo + " / " + totalValores);
        } else {
            TVResultadoChecks.setVisibility(View.INVISIBLE);
        }
    }

}