package com.example.validacion;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;
import static com.example.validacion.Adaptadores.Utiles.crearToastPersonalizado;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorChecks;
import com.example.validacion.Adaptadores.AdaptadorClientes;
import com.example.validacion.Adaptadores.AdaptadorModelos;
import com.example.validacion.Adaptadores.Utiles;
import com.example.validacion.Objetos.Cheks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClientesFragment extends Fragment implements AdaptadorModelos.OnActivityActionListener, AdaptadorClientes.OnActivityActionListener {


    public ClientesFragment() {
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

    RecyclerView recyclerViewCliente;

    AdaptadorClientes adaptadorClientes;
    List<JSONObject> listaClientes = new ArrayList<>();
    ConstraintLayout LayoutSinInternet;
    RelativeLayout LayoutConContenido;
    EditText searchEditText;
    ConstraintLayout LayoutSinContenido;

    LottieAnimationView lottieNoClientes;
    TextView TextSinResultados;


    AlertDialog.Builder builder;
    AlertDialog modalCargando;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);
        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);

        builder = new AlertDialog.Builder(context);

        searchEditText = view.findViewById(R.id.searchEditText);
        recyclerViewCliente = view.findViewById(R.id.recyclerViewClientes);
        LayoutSinInternet = view.findViewById(R.id.LayoutSinInternet);
        LayoutConContenido = view.findViewById(R.id.LayoutConContenido);
        lottieNoClientes = view.findViewById(R.id.lottieNoClientes);
        LayoutSinContenido = view.findViewById(R.id.LayoutSinContenido);
        TextSinResultados = view.findViewById(R.id.TextSinResultados);
        FloatingActionButton botonAgregarClientes = view.findViewById(R.id.botonAgregarClientes);


        CargarClientes();


        List<JSONObject> listaModelos = new ArrayList<>();
        Bundle bundle = new Bundle();
        AdaptadorModelos adaptadorModelos = new AdaptadorModelos(listaModelos, context, bundle, this, null, null, null);
        adaptadorClientes = new AdaptadorClientes(listaClientes, context, this, this);
        recyclerViewCliente.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewCliente.setAdapter(adaptadorClientes);


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptadorClientes.filter(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        botonAgregarClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_agregar_clientes, null);
                builder.setView(ModalRedondeado(view.getContext(), customView));
                AlertDialog dialogAgregarCliente = builder.create();
                dialogAgregarCliente.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogAgregarCliente.show();

                Button botonAgregarCliente = customView.findViewById(R.id.botonAgregarCliente);
                Button botonCancelar = customView.findViewById(R.id.botonCancelar);
                EditText EditTextNombre = customView.findViewById(R.id.EditTextNombre);
                EditText EditTextDomicilio = customView.findViewById(R.id.EditTextDomicilio);
                EditText EditTextTelefono = customView.findViewById(R.id.EditTextTelefono);
                EditText EditTextCorreo = customView.findViewById(R.id.EditTextCorreo);


                botonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogAgregarCliente.dismiss();
                    }
                });

                botonAgregarCliente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nombre = EditTextNombre.getText().toString();
                        String correo = EditTextCorreo.getText().toString();
                        String domicilio = EditTextDomicilio.getText().toString();
                        String telefono = EditTextTelefono.getText().toString();


                        if (nombre.isEmpty() || correo.isEmpty() || domicilio.isEmpty() || telefono.isEmpty()) {
                            crearToastPersonalizado(context, "Tienes campos vacios, por favor rellenalos");
                        } else {
                            dialogAgregarCliente.dismiss();
                            AgregarCliente(nombre, domicilio, telefono, correo);

                        }

                    }
                });


            }
        });

        return view;

    }


    private void AgregarCliente(String nombre, String domicilio, String telefono, String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Utiles.crearToastPersonalizado(context, "Se agrego el cliente " + nombre);
                        CargarClientes();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Utiles.crearToastPersonalizado(context, "No se pudo agregar al cliente, revisa la conexión ");
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "31");
                params.put("nombre", nombre);
                params.put("domicilio", domicilio);
                params.put("telefono", telefono);
                params.put("email", email);
                params.put("obs", "Nuevo Usuario");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }


    public void CargarClientes() {
        listaClientes.clear();
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                listaClientes.add(jsonObject);
                            }
                            adaptadorClientes.notifyDataSetChanged();
                            adaptadorClientes.setFilteredData(listaClientes);
                            adaptadorClientes.filter("");

                            if (listaClientes.size() > 0) {
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
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "19");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void mostrarDatos(String estado) {

        if (estado.equalsIgnoreCase("SinContenido")) {

            LayoutSinInternet.setVisibility(View.GONE);
            LayoutConContenido.setVisibility(View.GONE);
            LayoutSinContenido.setVisibility(View.VISIBLE);

        } else if (estado.equalsIgnoreCase("SinInternet")) {

            LayoutSinInternet.setVisibility(View.VISIBLE);
            LayoutConContenido.setVisibility(View.GONE);
            LayoutSinContenido.setVisibility(View.GONE);
        } else {

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
    public void onAgregarUnidad(String idcliente, String idmarca, String idmodelo, String anio, String placas, String vin, String motor, String tipo) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Respuesta de volley registrarUsuario", "idcliente" + idcliente + " idmarca" + idmarca + " idmodelo" + idmodelo + " anio" + anio + " placas" + placas + " vin" + vin + " motor" + motor + " tipo" + tipo);
                crearToastPersonalizado(context, "Se registro correctamente la unidad del cliente");
                CargarClientes();
                searchEditText.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                crearToastPersonalizado(context, "Hubo un error al registrar al usuario, por favor revisa la conexión");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "34");
                params.put("idcliente", idcliente);
                params.put("idmarca", idmarca);
                params.put("idmodelo", idmodelo);
                params.put("anio", anio);
                params.put("placas", placas);
                params.put("vin", vin);
                params.put("motor", motor);
                params.put("tipo", tipo);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }

    @Override
    public void onFilterData(boolean result) {
        if (result) {
            animacionLupe("Oculto");
        } else {
            if ((searchEditText.getText().toString().equals("") || searchEditText.getText().toString().isEmpty())) {
                animacionLupe("Oculto");
            } else {
                animacionLupe("Visible");
            }
        }
    }


    private void animacionLupe(String estado) {
        if (estado.equals("Oculto")) {
            LayoutSinContenido.setVisibility(View.GONE);
            TextSinResultados.setVisibility(View.GONE);
        } else {
            LayoutSinContenido.setVisibility(View.VISIBLE);
            TextSinResultados.setVisibility(View.VISIBLE);
        }
    }
}