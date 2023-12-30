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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorListadoActividades;
import com.example.validacion.Adaptadores.AdaptadorReporteActividades;
import com.example.validacion.Adaptadores.Utiles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListadoActividadesFragment extends Fragment implements AdaptadorListadoActividades.OnActivityActionListener {
    public ListadoActividadesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    String url;
    Context context;

    AlertDialog.Builder builder;
    AlertDialog modalCargando;
    List<JSONObject> listadoActividades = new ArrayList<>();
    ConstraintLayout ContenedorContenido;
    ConstraintLayout ContenedorSinInternet;
    AdaptadorListadoActividades adaptadorListadoActividades;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listado_actividades, container, false);
        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        ContenedorContenido = view.findViewById(R.id.ContenedorContenido);
        ContenedorSinInternet = view.findViewById(R.id.ContenedorSinInternet);


        FloatingActionButton botonAgregarActividad = view.findViewById(R.id.botonAgregarActividad);
        RecyclerView recyclerViewListaActividades = view.findViewById(R.id.recyclerViewListaActividades);


        adaptadorListadoActividades = new AdaptadorListadoActividades(listadoActividades, context, this);
        recyclerViewListaActividades.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewListaActividades.setAdapter(adaptadorListadoActividades);


        ConsultarActividades();

        botonAgregarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_agregar_listado_actividades, null);
                builder.setView(ModalRedondeado(view.getContext(), customView));
                AlertDialog dialogOpcionesActividades = builder.create();
                dialogOpcionesActividades.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogOpcionesActividades.show();

                EditText editText = customView.findViewById(R.id.editText);
                Button buttonGuardar = customView.findViewById(R.id.buttonGuardar);

                Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);

                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogOpcionesActividades.dismiss();
                    }
                });


                buttonGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String nombre_actividad = editText.getText().toString().trim();

                        if (nombre_actividad.isEmpty() || nombre_actividad.equals("")) {
                            Utiles.crearToastPersonalizado(context, "Debes ingresar un nombre");
                        } else {
                            dialogOpcionesActividades.dismiss();
                            AgregarListadoActividad(nombre_actividad);

                        }


                    }
                });

            }
        });


        return view;
    }


    private void AgregarListadoActividad(String nombre_actividad) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utiles.crearToastPersonalizado(context, "Se agrego la actividad correctamente");
                ConsultarActividades();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "No se pudo ingresar la actividad, revisa la conexión ");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "87");
                params.put("nombre_actividad", nombre_actividad);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);

    }


    private void ConsultarActividades() {
        listadoActividades.clear();
        modalCargando = Utiles.ModalCargando(context, builder);

        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String estatus = jsonObject.getString("estatus");
                        //  if (estatus.equalsIgnoreCase("alta")) {
                        listadoActividades.add(jsonObject);

                    }

                    if (listadoActividades.size() > 0) {
                        mostrarLayout("conContenido");
                    } else {
                        mostrarLayout("SinContenido");
                    }


                    adaptadorListadoActividades.notifyDataSetChanged();
                    adaptadorListadoActividades.setFilteredData(listadoActividades);
                    adaptadorListadoActividades.filter("");

                } catch (JSONException e) {

                    mostrarLayout("SinContenido");
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
                params.put("opcion", "86");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);

    }


    private void mostrarLayout(String estado) {
        if (estado.equalsIgnoreCase("SinInternet")) {
            ContenedorContenido.setVisibility(View.GONE);
            ContenedorSinInternet.setVisibility(View.VISIBLE);

        } else if (estado.equalsIgnoreCase("conContenido")) {
            ContenedorContenido.setVisibility(View.VISIBLE);
            ContenedorSinInternet.setVisibility(View.GONE);
        } else {

            ContenedorContenido.setVisibility(View.GONE);
            ContenedorSinInternet.setVisibility(View.GONE);
        }
        modalCargando.dismiss();
    }


    @Override
    public void onEliminarListado(String ID_listado_actividad) {

        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utiles.crearToastPersonalizado(context, "Se eliminó la actividad correctamente");
                ConsultarActividades();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "No se pudo eliminar, revisa la conexión ");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "88");
                params.put("ID_listado_actividad", ID_listado_actividad);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);


    }


    @Override
    public void onEditarListado(String ID_listado_actividad, String nombre_actividad) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utiles.crearToastPersonalizado(context, "Se editó la actividad correctamente");
                ConsultarActividades();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "No se pudo editar, revisa la conexión ");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "89");
                params.put("ID_listado_actividad", ID_listado_actividad);
                params.put("nombre_actividad", nombre_actividad);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


}