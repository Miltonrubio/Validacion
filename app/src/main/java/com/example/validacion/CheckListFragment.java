package com.example.validacion;

import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorCheckListEntrada;
import com.example.validacion.Adaptadores.AdaptadorChecks;
import com.example.validacion.Adaptadores.AdaptadorClientes;
import com.example.validacion.Adaptadores.Utiles;
import com.example.validacion.Objetos.Cheks;
import com.google.android.gms.dynamic.IFragmentWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckListFragment extends Fragment implements AdaptadorCheckListEntrada.OnActivityActionListener {/* implements AdaptadorChecks.OnCheckUpdatedListener*/

    List<JSONObject> listaChecks = new ArrayList<>();
    RecyclerView reciclerViewCheck;

    TextView TVResultadoChecks;

    String url;
    Context context;
    AdaptadorCheckListEntrada adaptadorCheckListEntrada;
    String idventa;

    LinearLayout LayoutConContenido;
    ConstraintLayout LayoutSinInternet;
    ConstraintLayout LayoutSinResultados;

    public CheckListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_check_list, container, false);
        reciclerViewCheck = rootView.findViewById(R.id.reciclerViewCheck);
        TVResultadoChecks = rootView.findViewById(R.id.TVResultadoChecks);
        TVResultadoChecks.setVisibility(View.VISIBLE);
        LayoutConContenido = rootView.findViewById(R.id.LayoutConContenido);
        LayoutSinInternet = rootView.findViewById(R.id.LayoutSinInternet);
        LayoutSinResultados = rootView.findViewById(R.id.LayoutSinResultados);

        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);
        Bundle bundle = getArguments();
        if (bundle != null) {
            idventa = bundle.getString("idventa", "");

            CargarChecks(idventa);
            adaptadorCheckListEntrada = new AdaptadorCheckListEntrada(listaChecks, context, this);
            reciclerViewCheck.setLayoutManager(new LinearLayoutManager(context));
            reciclerViewCheck.setAdapter(adaptadorCheckListEntrada);
        }


        return rootView;

    }

    public void CargarChecks(String idventa) {
        listaChecks.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                listaChecks.add(jsonObject);
                            }
                            adaptadorCheckListEntrada.setFilteredData(listaChecks);
                            adaptadorCheckListEntrada.filter("");

                            int totalVacios = adaptadorCheckListEntrada.obtenerSuma();
                            if (totalVacios <= 0) {
                                TVResultadoChecks.setText("Terminaste la revisión");
                            } else {
                                TVResultadoChecks.setText("Faltantes: " + String.valueOf(totalVacios));
                            }

                            if (listaChecks.size() > 0) {
                                mostrarDatos("ConContenido");
                            } else {
                                mostrarDatos("SinContenido");
                            }

                        } catch (JSONException e) {
                            mostrarDatos("SinContenido");

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mostrarDatos("SinInternet");
                        Utiles.crearToastPersonalizado(context, "Hubo un error al cargar los datos, revisa la conexión");
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


    private void mostrarDatos(String estado) {
        if (estado.equalsIgnoreCase("SinContenido")) {
            LayoutConContenido.setVisibility(View.GONE);
            LayoutSinInternet.setVisibility(View.GONE);
            LayoutSinResultados.setVisibility(View.VISIBLE);
        } else if (estado.equalsIgnoreCase("SinInternet")) {
            LayoutConContenido.setVisibility(View.GONE);
            LayoutSinInternet.setVisibility(View.VISIBLE);
            LayoutSinResultados.setVisibility(View.GONE);
        } else {
            LayoutConContenido.setVisibility(View.VISIBLE);
            LayoutSinInternet.setVisibility(View.GONE);
            LayoutSinResultados.setVisibility(View.GONE);
        }
    }


    public void onActualizarCheck(String valorCheck, String idcheck, String descripcion) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utiles.crearToastPersonalizado(context, descripcion + " Revisado");
                        CargarChecks(idventa);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utiles.crearToastPersonalizado(context, "No se puedo actualizar el check, revisa la conexión");
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "12");
                params.put("idcheck", idcheck);
                params.put("valorcheck", valorCheck);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onChecksPendientes(String vacios) {

    }






/*
    public void CargarChecks(String idventa) {
        listaChecks.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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


                                // Cheks checks = new Cheks(descripcion, categoria, comentarios, id_ser_venta, urlfoto, valorcheck, fechacheck, horacheck, estado, idcheck);
                                //listaChecks.add(checks);
                                listaChecks.add(jsonObject);
/*
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


                            } catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                    },
                            new Response.ErrorListener()

                    {
                        @Override
                        public void onErrorResponse (VolleyError error){
                        Log.e("API Error", "Error en la solicitud: " + error.getMessage());
                    }
                    }
        )

                    {
                        @Override
                        public Map<String, String> getParams () throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("opcion", "11");
                        params.put("idventa", idventa);
                        return params;
                    }
                    }

                    ;
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
                }


        public void onCheckUpdated ( int position, String valorCheck,int valoresVaciosChecks,
        int totalValores){
            int calculo = totalValores - valoresVaciosChecks;

            if (calculo != 0) {
                TVResultadoChecks.setText("Revisados: " + calculo + " / " + totalValores);
            } else {
                TVResultadoChecks.setVisibility(View.INVISIBLE);
            }
        }

*/
}