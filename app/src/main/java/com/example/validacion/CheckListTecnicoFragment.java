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
import com.example.validacion.Adaptadores.AdaptadrCheckListTecnico;
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

public class CheckListTecnicoFragment extends Fragment implements AdaptadrCheckListTecnico.OnActivityActionListener {/* implements AdaptadorChecks.OnCheckUpdatedListener*/

    //   List<Cheks> listaChecks = new ArrayList<>();
    List<JSONObject> listaChecks = new ArrayList<>();
    RecyclerView reciclerViewCheck;

    TextView TVResultadoChecks;

    String url;
    Context context;
    AdaptadrCheckListTecnico adaptadrCheckListTecnico;
    String idventa;

    LinearLayout LayoutConContenido;
    ConstraintLayout LayoutSinInternet;
    ConstraintLayout LayoutSinResultados;

    public CheckListTecnicoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_check_list_tecnico, container, false);
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
            adaptadrCheckListTecnico = new AdaptadrCheckListTecnico(listaChecks, context, this);
            reciclerViewCheck.setLayoutManager(new LinearLayoutManager(context));
            reciclerViewCheck.setAdapter(adaptadrCheckListTecnico);
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
                            adaptadrCheckListTecnico.setFilteredData(listaChecks);
                            adaptadrCheckListTecnico.filter("");

                            int totalVacios = adaptadrCheckListTecnico.obtenerSuma();
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
                params.put("opcion", "38");
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
                params.put("opcion", "39");
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
}