package com.example.validacion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorActividades;
import com.example.validacion.Adaptadores.Utiles;
import com.itextpdf.text.pdf.parser.Line;

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

    LinearLayout ContenedorContenido;
    ConstraintLayout LayoutSinResultados;
    ConstraintLayout LayoutSinInternet;


    public ActividadesFragment() {
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
        recyclerViewActividades = view.findViewById(R.id.recyclerViewActividades);
        searchEditTextActividades = view.findViewById(R.id.searchEditTextActividades);
        ContenedorContenido = view.findViewById(R.id.ContenedorContenido);
        LayoutSinResultados = view.findViewById(R.id.LayoutSinResultados);
        LayoutSinInternet = view.findViewById(R.id.LayoutSinInternet);
        context = requireContext();

        url = context.getResources().getString(R.string.ApiBack);


        Bundle bundle = getArguments();
        if (bundle != null) {
            idusuario = bundle.getString("idusuario", "");
        } else {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
            idusuario = sharedPreferences.getString("idusuario", "");
        }


        recyclerViewActividades.setLayoutManager(new LinearLayoutManager(context));
        adaptadorActividades = new AdaptadorActividades(dataList, context, this);
        recyclerViewActividades.setAdapter(adaptadorActividades);
        TomarActividadesDesdeApi(idusuario);
        return view;
    }


    public void TomarActividadesDesdeApi(String idmecanico) {
        dataList.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String estatus = jsonObject.getString("estatus");

                        if (estatus.equals("activo") || estatus.equals("pendiente") || estatus.equals("pausada")) {
                            dataList.add(jsonObject);
                        }
                    }

                    if (dataList.size() > 0) {
                        mostrarLayout("conContenido");
                    } else {
                        mostrarLayout("SinContenido");
                    }

                    adaptadorActividades.notifyDataSetChanged();
                    adaptadorActividades.setFilteredData(dataList);
                    adaptadorActividades.filter("");

                } catch (JSONException e) {
                    mostrarLayout("SinContenido");
                }

                searchEditTextActividades.setText("");
                //   mostrarDatosFiltrados();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mostrarLayout("SinInternet");
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

/*
    private void mostrarDatosFiltrados() {
        datosFiltrados.clear();
        for (JSONObject jsonObject : dataList) {
            try {
                String estatus = jsonObject.getString("estatus");

                if (estatus.equals("activo") || estatus.equals("pendiente") || estatus.equals("pausada")) {
                    datosFiltrados.add(jsonObject);
                }

                if (datosFiltrados.size() > 0) {
                    mostrarLayout("conContenido");
                } else {
                    mostrarLayout("SinContenido");
                }

            } catch (JSONException e) {
                mostrarLayout("SinContenido");
            }
        }
        adaptadorActividades.setFilteredData(datosFiltrados);
        adaptadorActividades.notifyDataSetChanged();
    }
*/

    private void mostrarLayout(String estado) {
        if (estado.equalsIgnoreCase("SinInternet")) {
            ContenedorContenido.setVisibility(View.GONE);
            LayoutSinInternet.setVisibility(View.VISIBLE);
            LayoutSinResultados.setVisibility(View.GONE);

        } else if (estado.equalsIgnoreCase("SinContenido")) {
            LayoutSinResultados.setVisibility(View.VISIBLE);
            ContenedorContenido.setVisibility(View.GONE);
            LayoutSinInternet.setVisibility(View.GONE);

        } else {
            ContenedorContenido.setVisibility(View.VISIBLE);
            LayoutSinInternet.setVisibility(View.GONE);
            LayoutSinResultados.setVisibility(View.GONE);
        }
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


                    Utiles.crearToastPersonalizado(context, "Ya tienes una actividad iniciada");

                } else {
                    Utiles.crearToastPersonalizado(context, "Se actualizo la actividad");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Error al actualizar el estado de la actividad, Revisa la conexión");
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
