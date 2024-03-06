package com.example.validacion;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorCoches;
import com.example.validacion.Adaptadores.AdaptadorNuevosChecks;
import com.example.validacion.Adaptadores.Utiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChecksFragment extends Fragment implements AdaptadorNuevosChecks.OnActivityActionListener {


    public ChecksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    Context context;
    List<JSONObject> listaChecks = new ArrayList<>();
    AdaptadorNuevosChecks adaptadorNuevosChecks;
    String url;

    String id_ser_venta;
    String tipo_check;
    TextView textViewFaltantes;
    ImageView botonFinalizarRevision;

    AlertDialog modalCargando;
    AlertDialog.Builder builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checks, container, false);

        TextView Titulo = view.findViewById(R.id.Titulo);
        RecyclerView recyclerViewChecks = view.findViewById(R.id.recyclerViewChecks);
        textViewFaltantes = view.findViewById(R.id.textViewFaltantes);

        botonFinalizarRevision = view.findViewById(R.id.botonFinalizarRevision);

        context = requireContext();
        url = context.getString(R.string.ApiBack);


        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);


        Bundle bundle = getArguments();
        if (bundle != null) {

            tipo_check = bundle.getString("tipo_check", "");
            id_ser_venta = bundle.getString("idventa", "");

            Titulo.setText("REVISION DE " + tipo_check.toUpperCase() + " " + id_ser_venta);


            mostrarChecks();


            adaptadorNuevosChecks = new AdaptadorNuevosChecks(listaChecks, context, this);
            recyclerViewChecks.setLayoutManager(new LinearLayoutManager(context));
            recyclerViewChecks.setAdapter(adaptadorNuevosChecks);


        }
        botonFinalizarRevision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion, null);
                builder.setView(ModalRedondeado(view.getContext(), customView));
                AlertDialog dialogConfirmacion = builder.create();
                dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogConfirmacion.show();

                TextView textView4 = customView.findViewById(R.id.textView4);

                Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);
                Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                EditText Motivo = customView.findViewById(R.id.Motivo);
                Motivo.setHint("Agrega una recomendación");

                if (!tipo_check.equalsIgnoreCase("Entrada") ) {
                    textView4.setText("Antes de terminar la revisión, ¿deseas agregar alguna recomendacion de servicio?");
                    Motivo.setVisibility(View.VISIBLE);
                } else {
                    textView4.setText("¿Estas seguro que deseas finalizar esta revisión? \n\nRecuerda que no podras editarla despues");
                    Motivo.setVisibility(View.GONE);
                }


                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogConfirmacion.dismiss();
                    }
                });

                buttonAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogConfirmacion.dismiss();
                        String Observaciones = Motivo.getText().toString();

                        if (Motivo.getVisibility() == View.VISIBLE && (!Observaciones.equalsIgnoreCase("") || !Observaciones.equalsIgnoreCase("null") || !Observaciones.equalsIgnoreCase(null)  || !Observaciones.isEmpty() )) {
                            FinalizarRevision(Observaciones);
                        } else {
                            FinalizarRevision("Vacio");
                        }

                    }
                });


            }
        });


        return view;
    }


    private void mostrarChecks() {
        listaChecks.clear();
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    int faltantes = jsonResponse.getInt("faltantes");
                    String finalizacion = jsonResponse.getString("mensaje");

                    if (faltantes > 0) {
                        textViewFaltantes.setText("Faltantes: " + faltantes);
                        botonFinalizarRevision.setVisibility(View.GONE);
                    } else {
                        textViewFaltantes.setText("Terminaste la revisión.");
                        botonFinalizarRevision.setVisibility(View.VISIBLE);
                        if (faltantes == 0 && finalizacion.equalsIgnoreCase("Finalizados")) {
                            botonFinalizarRevision.setVisibility(View.GONE);
                        }
                    }

                    JSONArray jsonArray = jsonResponse.getJSONArray("registros");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listaChecks.add(jsonObject);
                    }

                    adaptadorNuevosChecks.notifyDataSetChanged();
                    adaptadorNuevosChecks.setFilteredData(listaChecks);
                    adaptadorNuevosChecks.filter("");


                } catch (JSONException e) {
                    Utiles.crearToastPersonalizado(context, "Error al procesar la respuesta del servidor");
                }
                modalCargando.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "No se pudo obtener los checks");
                modalCargando.dismiss();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "99");
                params.put("tipo_check", tipo_check);
                params.put("id_ser_venta", id_ser_venta);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    @Override
    public void onActualizarCheck(String ID_valor_check, String valorCheck) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mostrarChecks();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "No se pudo obtener los checks");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "98");
                params.put("valor_check", valorCheck);
                params.put("ID_valor_check", ID_valor_check);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }

    private void FinalizarRevision(String observaciones) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mostrarChecks();
                Utiles.crearToastPersonalizado(context, "Se finalizó la revisión");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "No se pudó finalizar la revision");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                if (observaciones.equalsIgnoreCase("Vacio")){

                    params.put("opcion", "100");
                    params.put("tipo_check", tipo_check);
                    params.put("id_ser_venta", id_ser_venta);

                }else {

                params.put("opcion", "100");
                params.put("tipo_check", tipo_check);
                params.put("id_ser_venta", id_ser_venta);
                params.put("observaciones", observaciones);
                }
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }

}