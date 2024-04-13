package com.example.validacion;

import static android.app.Activity.RESULT_OK;
import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;
import static com.example.validacion.Adaptadores.Utiles.crearToastPersonalizado;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorInyectores;
import com.example.validacion.Adaptadores.AdaptadorSeleccionarCliente;
import com.example.validacion.Adaptadores.AdaptadorSeleccionarUnidad;
import com.example.validacion.Adaptadores.AdaptadorServiciosInyectores;
import com.example.validacion.Adaptadores.AdaptadorTiposUnidadesDesdeInicio;
import com.example.validacion.Adaptadores.Utiles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class InyectoresFragment extends Fragment implements AdaptadorSeleccionarCliente.OnActivityActionListener, AdaptadorTiposUnidadesDesdeInicio.OnActivityActionListener, AdaptadorSeleccionarUnidad.OnActivityActionListener, AdaptadorInyectores.OnActivityActionListener, AdaptadorServiciosInyectores.OnActivityActionListener {
    String url;
    Context context;
    AdaptadorInyectores.OnActivityActionListener actionListenerInyectores;
    AlertDialog.Builder builder;
    AlertDialog modalCargando;
    AdaptadorTiposUnidadesDesdeInicio.OnActivityActionListener tiposUnidadesActionListener;
    AdaptadorSeleccionarUnidad.OnActivityActionListener unidadActionListener;
    TextView textSinContenido;

    ConstraintLayout contenedorNoInternet;
    ConstraintLayout contenedorContenido;
    ConstraintLayout contenedorNoContenido;
    AdaptadorServiciosInyectores.OnActivityActionListener actionListenerServiciosInyectores;

    EditText searchEditTextInicio;

    private void MostrarContenido(String estado) {

        if (estado.equalsIgnoreCase("CONTENIDO")) {
            contenedorNoInternet.setVisibility(View.GONE);
            contenedorContenido.setVisibility(View.VISIBLE);
            contenedorNoContenido.setVisibility(View.GONE);

        } else if (estado.equalsIgnoreCase("SINCONTENIDO")) {
            contenedorNoInternet.setVisibility(View.GONE);
            contenedorContenido.setVisibility(View.GONE);
            contenedorNoContenido.setVisibility(View.VISIBLE);
        } else {
            contenedorNoInternet.setVisibility(View.VISIBLE);
            contenedorContenido.setVisibility(View.GONE);
            contenedorNoContenido.setVisibility(View.GONE);
        }

        modalCargando.dismiss();
    }


    public InyectoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inyectores, container, false);
        context = requireContext();
        url = context.getString(R.string.ApiBack);
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        MostrarInyectores();


        clienteActionListener = this;
        unidadActionListener = this;
        tiposUnidadesActionListener = this;
        actionListenerInyectores = this;
        actionListenerServiciosInyectores = this;

        RecyclerView reciclerViewInyectores = view.findViewById(R.id.reciclerViewInyectores);
        FloatingActionButton botonAgregarInyector = view.findViewById(R.id.botonAgregarInyector);
        contenedorNoContenido = view.findViewById(R.id.contenedorNoContenido);
        textSinContenido= view.findViewById(R.id.textSinContenido);
        contenedorNoInternet = view.findViewById(R.id.contenedorNoInternet);
        contenedorContenido = view.findViewById(R.id.contenedorContenido);
        searchEditTextInicio = view.findViewById(R.id.searchEditTextInicio);

        adaptadorServiciosInyectores = new AdaptadorServiciosInyectores(listaServicios, context, actionListenerInyectores, actionListenerServiciosInyectores);
        reciclerViewInyectores.setLayoutManager(new LinearLayoutManager(context));
        reciclerViewInyectores.setAdapter(adaptadorServiciosInyectores);


        Button buttonPendientes = view.findViewById(R.id.buttonPendientes);
        Button buttonEntregados = view.findViewById(R.id.buttonEntregados);


        buttonPendientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MostrarInyectores();


            }
        });


        buttonEntregados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MostrarInyectoresEntregados();

            }
        });


        botonAgregarInyector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                id_serv_unidad = "";
                id_ser_cliente = "";

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View customView = LayoutInflater.from(context).inflate(R.layout.modal_agregar_inyector, null);
                builder.setView(Utiles.ModalRedondeado(context, customView));
                AlertDialog dialogNuevoServicio = builder.create();
                dialogNuevoServicio.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogNuevoServicio.show();


                textSeleccionarCliente = customView.findViewById(R.id.textSeleccionarCliente);
                textSeleccionarUnidad = customView.findViewById(R.id.textSeleccionarUnidad);
                EditTextNumInyectores = customView.findViewById(R.id.EditTextNumInyectores);
                tvNumInyectores = customView.findViewById(R.id.tvNumInyectores);

                EditText editTextMotivoIngreso = customView.findViewById(R.id.editTextMotivoIngreso);
                ImageView btnAgregarCliente = customView.findViewById(R.id.btnAgregarCliente);

                Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                Button buttonGuardar = customView.findViewById(R.id.buttonGuardar);


                btnAgregarCliente.setOnClickListener(new View.OnClickListener() {
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


                buttonGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String motivoIngresado = editTextMotivoIngreso.getText().toString();
                        String numInyectoresIngresado;

                        if (!tipoUnidad.equalsIgnoreCase("Turbo")){
                            numInyectoresIngresado= EditTextNumInyectores.getText().toString();
                        }else {
                            numInyectoresIngresado = "1";
                        }



                        if (numInyectoresIngresado.isEmpty() || motivoIngresado.isEmpty() || id_serv_unidad.isEmpty() || id_ser_cliente.isEmpty()) {
                            Utiles.crearToastPersonalizado(context, "Debes ingresar todos los campos");
                        } else {
                            try {
                                int numeroInyect = Integer.parseInt(numInyectoresIngresado);
                                if (numeroInyect < 1) {


                                    Utiles.crearToastPersonalizado(context, "No puedes ingresar un número menor a 1");

                                } else {

                                    AgregarServicio(id_serv_unidad, motivoIngresado, numInyectoresIngresado, tipoUnidad);

                                    dialogNuevoServicio.dismiss();
                                }
                            } catch (NumberFormatException e) {

                                Utiles.crearToastPersonalizado(context, "No puedes ingresar decimales u otros digitos");
                            }


                        }
                    }
                });


                textSeleccionarUnidad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (nombreCliente.isEmpty() || nombreCliente.equalsIgnoreCase("") || nombreCliente == null) {
                            Utiles.crearToastPersonalizado(context, "Debes seleccionar un cliente");
                        } else {

                            AbrirModalUnidades();
                        }

                    }
                });


                textSeleccionarCliente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AbrirModalClientes();
                    }
                });

                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogNuevoServicio.dismiss();
                    }
                });
            }
        });


        searchEditTextInicio.setHint("Buscar inyector por ID");


        searchEditTextInicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptadorServiciosInyectores.filter(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        return view;

    }


    private void AgregarServicio(String idunidad, String motivo, String numeroInyectores, String tipo) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                MostrarInyectores();
                Utiles.crearToastPersonalizado(context, "Servicio Agregado");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Error " + error.getMessage());

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("opcion", "118");
                params.put("ID_unidad", idunidad);
                params.put("motivo_ingreso", motivo);
                params.put("tipo", tipo);
                params.put("numeroInyectores", numeroInyectores);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }



    private void AbrirModalClientes() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View customView = LayoutInflater.from(context).inflate(R.layout.modal_opciones_clientes, null);
        builder.setView(Utiles.ModalRedondeado(context, customView));
        AlertDialog dialogClientes = builder.create();
        dialogClientes.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogClientes.show();
        ImageView btnAgregarUnidad = customView.findViewById(R.id.btnAgregarUnidad);
        btnAgregarUnidad.setVisibility(View.GONE);
        VerClientes();
        searchEditText = customView.findViewById(R.id.searchEditText);
        lottieNoResultados = customView.findViewById(R.id.lottieNoResultados);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptadorSeleccionarCliente.filter(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        RecyclerView RecyclerViewUnidadesUsuario = customView.findViewById(R.id.RecyclerViewUnidadesUsuario);
        RecyclerViewUnidadesUsuario.setLayoutManager(new LinearLayoutManager(context));
        adaptadorSeleccionarCliente = new AdaptadorSeleccionarCliente(listaClientes, context, clienteActionListener, dialogClientes);
        RecyclerViewUnidadesUsuario.setAdapter(adaptadorSeleccionarCliente);

    }


    List<JSONObject> listaServicios = new ArrayList<>();
    AdaptadorServiciosInyectores adaptadorServiciosInyectores;

    private void MostrarInyectores() {
        listaServicios.clear();
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                listaServicios.add(jsonObject);
                            }

                            adaptadorServiciosInyectores.notifyDataSetChanged();
                            adaptadorServiciosInyectores.setFilteredData(listaServicios);
                            adaptadorServiciosInyectores.filter("");

                            if (listaServicios.size() > 0) {
                                MostrarContenido("CONTENIDO");

                            } else {
                                MostrarContenido("SINCONTENIDO");
                            }


                        } catch (JSONException e) {
                            MostrarContenido("SINCONTENIDO");

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        MostrarContenido("SININTERNET");
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "117");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }


    private void MostrarInyectoresEntregados() {
        listaServicios.clear();
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                listaServicios.add(jsonObject);
                            }

                            adaptadorServiciosInyectores.notifyDataSetChanged();
                            adaptadorServiciosInyectores.setFilteredData(listaServicios);
                            adaptadorServiciosInyectores.filter("");

                            if (listaServicios.size() > 0) {
                                MostrarContenido("CONTENIDO");

                            } else {
                                MostrarContenido("SINCONTENIDO");
                            }


                        } catch (JSONException e) {
                            MostrarContenido("SINCONTENIDO");

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        MostrarContenido("SININTERNET");
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "135");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }


    AdaptadorSeleccionarCliente.OnActivityActionListener clienteActionListener;
    AdaptadorSeleccionarCliente adaptadorSeleccionarCliente;
    List<JSONObject> listaClientes = new ArrayList<>();

    private void VerClientes() {
        listaClientes.clear();
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listaClientes.add(jsonObject);
                    }

                    adaptadorSeleccionarCliente.setFilteredData(listaClientes);
                    adaptadorSeleccionarCliente.filter("");


                } catch (JSONException e) {
                    Utiles.crearToastPersonalizado(context, "Error al cargar los datos");
                }
                onLoadComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Error al cargar los datos");
                onLoadComplete();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "19");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void onLoadComplete() {
        if (modalCargando.isShowing() && modalCargando != null) {
            modalCargando.dismiss();
        }
    }

    TextView textSeleccionarCliente;
    TextView textSeleccionarUnidad;
    String id_ser_cliente = "";
    String nombreCliente = "";
    String id_serv_unidad = "";

    @Override
    public void onTomarCliente(String nombreCliente, String id_ser_cliente) {

        textSeleccionarCliente.setText(nombreCliente.toUpperCase());
        textSeleccionarUnidad.setEnabled(true);


        this.id_ser_cliente = id_ser_cliente;
        this.nombreCliente = nombreCliente;
        id_serv_unidad = "";
        textSeleccionarUnidad.setText("Selecciona la unidad");


    }

    @Override
    public void onResultadosClientes(boolean result) {

        if (result) {
            animacionSinResultadosClientes("Oculto");
        } else {
            if ((searchEditText.getText().toString().equals("") || searchEditText.getText().toString().isEmpty())) {
                animacionSinResultadosClientes("Oculto");
            } else {
                animacionSinResultadosClientes("Visible");
            }
        }
    }


    private void animacionSinResultadosClientes(String estado) {
        if (estado.equals("Oculto")) {
            lottieNoResultados.setVisibility(View.GONE);
        } else {
            lottieNoResultados.setVisibility(View.VISIBLE);
        }
    }


    String marca, modelo, anio, placas, motor, vin, fotoUnidad, tipoUnidad;

    EditText EditTextNumInyectores;
    TextView tvNumInyectores;

    @Override
    public void onTomarUnidad(String id_serv_unidad, String marca, String modelo, String vin, String motor, String anio, String placas, String tipo, String foto) {
        this.id_serv_unidad = id_serv_unidad;
        this.marca = marca;
        this.modelo = modelo;
        this.vin = vin;
        this.motor = motor;
        this.anio = anio;
        this.placas = placas;
        this.fotoUnidad = foto;
        this.tipoUnidad = tipo;

        textSeleccionarUnidad.setText(marca.toUpperCase() + " " + modelo.toUpperCase());

        //  Utiles.crearToastPersonalizado(context, "Foto: " + foto);
        //   mostrarFormularios(tipo);


        if (tipo.equalsIgnoreCase("Turbo")) {
            EditTextNumInyectores.setVisibility(View.GONE);
            tvNumInyectores.setVisibility(View.GONE);
        } else {
            EditTextNumInyectores.setVisibility(View.VISIBLE);
            tvNumInyectores.setVisibility(View.VISIBLE);

        }
    }

    EditText searchEditText;

    LottieAnimationView lottieNoResultados;

    @Override
    public void onResultadosUnidad(boolean result) {
        if (result) {
            animacionSinResultadosUnidad("Oculto");
        } else {
            if ((searchEditText.getText().toString().equals("") || searchEditText.getText().toString().isEmpty())) {
                animacionSinResultadosUnidad("Oculto");
            } else {
                animacionSinResultadosUnidad("Visible");
            }
        }
    }

    private void animacionSinResultadosUnidad(String estado) {
        if (estado.equals("Oculto")) {
            lottieNoResultados.setVisibility(View.GONE);
        } else {
            lottieNoResultados.setVisibility(View.VISIBLE);
        }
    }


    private void AbrirModalUnidades() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View customView = LayoutInflater.from(context).inflate(R.layout.modal_opciones_clientes, null);
        builder.setView(Utiles.ModalRedondeado(context, customView));
        AlertDialog dialogUnidades = builder.create();
        dialogUnidades.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogUnidades.show();
        MostrarUnidadesClientes(id_ser_cliente, context);
        TextView textSinContenido = customView.findViewById(R.id.textSinContenido);
        textSinContenido.setText("NO HAY INYECTORES O TURBOS REGISTRADOS PARA " + nombreCliente.toUpperCase());
        lottieNoResultados = customView.findViewById(R.id.lottieNoResultados);

        Bundle bundleUsuario = new Bundle();
        bundleUsuario.putString("nombreUsuario", nombreCliente);
        bundleUsuario.putString("id_ser_cliente", id_ser_cliente);

        TextView NombreclIENTE = customView.findViewById(R.id.NombreclIENTE);
        NombreclIENTE.setText("INYECTORES DE " + nombreCliente.toUpperCase());
        LayoutConContenidoUnidades = customView.findViewById(R.id.LayoutConContenido);
        LayoutSinInternetUnidades = customView.findViewById(R.id.LayoutSinInternet);
        LayoutSinContenidoUnidades = customView.findViewById(R.id.LayoutSinContenido);


        searchEditText = customView.findViewById(R.id.searchEditText);
        ImageView btnAgregarUnidad = customView.findViewById(R.id.btnAgregarUnidad);
        ImageView AgregarNuevaUnidad = customView.findViewById(R.id.AgregarNuevaUnidad);

        btnAgregarUnidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostrarModalAgregarNuevaUnidad(view, bundleUsuario);
            }
        });
        AgregarNuevaUnidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostrarModalAgregarNuevaUnidad(view, bundleUsuario);

            }
        });


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptadorSeleccionarUnidad.filter(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        RecyclerView RecyclerViewUnidadesUsuario = customView.findViewById(R.id.RecyclerViewUnidadesUsuario);
        RecyclerViewUnidadesUsuario.setLayoutManager(new LinearLayoutManager(context));
        adaptadorSeleccionarUnidad = new AdaptadorSeleccionarUnidad(listaUnidades, context, unidadActionListener, dialogUnidades);
        RecyclerViewUnidadesUsuario.setAdapter(adaptadorSeleccionarUnidad);

    }


    AdaptadorSeleccionarUnidad adaptadorSeleccionarUnidad;


    private void MostrarModalAgregarNuevaUnidad(View view, Bundle bundleUsuario) {
        ConsultarTiposUnidades();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View customView = LayoutInflater.from(context).inflate(R.layout.layout_mostrar_tipos_unidades, null);
        builder.setView(ModalRedondeado(context, customView));
        AlertDialog dialogListaUnidades = builder.create();
        dialogListaUnidades.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogListaUnidades.show();

        RecyclerView recyclerViewTiposUnidades = customView.findViewById(R.id.recyclerViewTiposUnidades);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerViewTiposUnidades.setLayoutManager(gridLayoutManager);
        adaptadorTiposUnidadesDesdeInicio = new AdaptadorTiposUnidadesDesdeInicio(listaTiposUnidades, context, tiposUnidadesActionListener, bundleUsuario, dialogListaUnidades);
        recyclerViewTiposUnidades.setAdapter(adaptadorTiposUnidadesDesdeInicio);


    }

    AdaptadorTiposUnidadesDesdeInicio adaptadorTiposUnidadesDesdeInicio;

    List<JSONObject> listaTiposUnidades = new ArrayList<>();

    private void ConsultarTiposUnidades() {

        listaTiposUnidades.clear();
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String nombreTipoUnidad = jsonObject.getString("nombre");

                        if (nombreTipoUnidad.equalsIgnoreCase("Inyector") /* || nombreTipoUnidad.equalsIgnoreCase("Turbo") */) {
                            listaTiposUnidades.add(jsonObject);
                        }
                    }

                    adaptadorTiposUnidadesDesdeInicio.setFilteredData(listaTiposUnidades);
                    adaptadorTiposUnidadesDesdeInicio.filter("");


                } catch (JSONException e) {
                    crearToastPersonalizado(context, "Error al cargar los datos");
                }
                onLoadComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                crearToastPersonalizado(context, "Error al cargar los datos");
                onLoadComplete();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "90");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);

    }


    ConstraintLayout LayoutConContenidoUnidades;
    ConstraintLayout LayoutSinInternetUnidades;
    ConstraintLayout LayoutSinContenidoUnidades;
    List<JSONObject> listaUnidades = new ArrayList<>();

    private void MostrarUnidadesClientes(String id_ser_cliente, Context context) {
        listaUnidades.clear();
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String tipo = jsonObject.getString("tipo");
                        if (tipo.equalsIgnoreCase("Inyector") /* || tipo.equalsIgnoreCase("Turbo") */) {
                            listaUnidades.add(jsonObject);
                        }

                    }

                    adaptadorSeleccionarUnidad.notifyDataSetChanged();
                    adaptadorSeleccionarUnidad.setFilteredData(listaUnidades);
                    adaptadorSeleccionarUnidad.filter("");

                    if (listaUnidades.size() > 0) {

                        OcultarLayoutsUnidades("ConContenido");
                    } else {
                        OcultarLayoutsUnidades("SinContenido");
                    }

                } catch (JSONException e) {
                    OcultarLayoutsUnidades("SinContenido");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                OcultarLayoutsUnidades("SinInternet");

            }
        }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "101");
                params.put("id_ser_cliente", id_ser_cliente);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void OcultarLayoutsUnidades(String estado) {
        if (estado.equalsIgnoreCase("SinInternet")) {
            LayoutConContenidoUnidades.setVisibility(View.GONE);
            LayoutSinInternetUnidades.setVisibility(View.VISIBLE);
            LayoutSinContenidoUnidades.setVisibility(View.GONE);
        } else if (estado.equalsIgnoreCase("SinContenido")) {

            LayoutConContenidoUnidades.setVisibility(View.GONE);
            LayoutSinInternetUnidades.setVisibility(View.GONE);
            LayoutSinContenidoUnidades.setVisibility(View.VISIBLE);
        } else {

            LayoutConContenidoUnidades.setVisibility(View.VISIBLE);
            LayoutSinInternetUnidades.setVisibility(View.GONE);
            LayoutSinContenidoUnidades.setVisibility(View.GONE);
        }
        onLoadComplete();
    }


    @Override
    public void onAgregarUnidad(String idcliente, String idmarca, String idmodelo, String anio, String placas, String vin, String motor, String tipo, String foto) {

        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Respuesta de volley registrarUsuario", "idcliente" + idcliente + " idmarca" + idmarca + " idmodelo" + idmodelo + " anio" + anio + " placas" + placas + " vin" + vin + " motor" + motor + " tipo" + tipo);

                MostrarUnidadesClientes(id_ser_cliente, context);


                crearToastPersonalizado(context, "Agregado correctamente");
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
                params.put("foto", foto);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void AgregarCliente(String nombre, String domicilio, String telefono, String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se agrego el cliente " + nombre);
                MostrarInyectores();

            }
        }, new Response.ErrorListener() {
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

    String ID_inyector = "";

    @Override
    public void onActualizarFoto(String ID_inyector, AlertDialog dialogFotoInyector, AlertDialog dialogOpcionesInyectores) {

        dialogFotoInyector.dismiss();
        dialogOpcionesInyectores.dismiss();
        this.ID_inyector = ID_inyector;
        AbrirCamara();
    }

    private void MandarFoto2(Bitmap imageBitmap) {
        modalCargando = Utiles.ModalCargando(context, builder);
        new InyectoresFragment.SendImageTask().execute(imageBitmap);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap imgBitmap = BitmapFactory.decodeFile(rutaImagen);
            MandarFoto2(imgBitmap);
        }

    }


    private void AbrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File imagenArchivo = null;
            try {
                imagenArchivo = crearImagen();
            } catch (IOException e) {
                Log.e("Error al obtener la imagen", e.toString());
            }
            if (imagenArchivo != null) {
                Uri fotoUri = FileProvider.getUriForFile(getActivity(), "com.example.validacion.fileprovider", imagenArchivo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(intent, 1);
            }
        }
    }

    String rutaImagen = "";

    private File crearImagen() throws IOException {
        String nombreFoto = "image";
        File directorio = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagenTemporal = File.createTempFile(nombreFoto, ".jpg", directorio);
        rutaImagen = imagenTemporal.getAbsolutePath();
        return imagenTemporal;
    }

    private File bitmapToFile(Bitmap bitmap, String fileName) {
        File file = new File(getActivity().getCacheDir(), fileName);
        try {
            file.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
            byte[] bitmapData = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    public void cambiarEstado(String ID_serv_inyector, String nuevoStatus) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se actualizó el servicio");
                MostrarInyectores();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "No se pudo actualizar el servicio, revisa la conexión ");
            }
        }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "126");
                params.put("ID_serv_inyector", ID_serv_inyector);
                params.put("nuevoStatus", nuevoStatus);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public void AsignarFolio(String iddocSelec, String ID_serv_inyector) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se asigno el folio");
                MostrarInyectores();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "No se pudo asignar el folio, revisa la conexión ");
            }
        }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "127");
                params.put("ID_serv_inyector", ID_serv_inyector);
                params.put("iddocSelec", iddocSelec);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private class SendImageTask extends AsyncTask<Bitmap, Void, Void> {
        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            Bitmap imageBitmap = bitmaps[0];

            OkHttpClient client = new OkHttpClient();

            String nombreArchivo = "imagen" + System.currentTimeMillis() + ".jpg";
            File imageFile = bitmapToFile(imageBitmap, "image.jpg");

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("opcion", "123")
                    .addFormDataPart("ID_inyector", ID_inyector)
                    .addFormDataPart("foto", nombreArchivo,
                            RequestBody.create(MediaType.parse("image/jpeg"), imageFile))
                    .build();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            try {
                okhttp3.Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d("Respuesta del servidor", responseData);
                } else {
                    Log.e("Error en la solicitud", String.valueOf(response.code()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Utiles.crearToastPersonalizado(context, "Se actualizó la imagen");

            modalCargando.dismiss();
            MostrarInyectores();
        }
    }


    @Override
    public void onAsignarManoDeObraInyector(String ID_inyector, String ID_mecanico, String observaciones, String costoActiv) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se asigno la actividad");
                MostrarInyectores();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "No se pudo asignar la actividad, revisa la conexión ");
            }
        }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "125");
                params.put("ID_inyector", ID_inyector);
                params.put("ID_mecanico", ID_mecanico);
                params.put("observaciones", observaciones);
                params.put("costo", costoActiv);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    @Override
    public void FinalizarRevisionInyector(String ID_inyector) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        MostrarInyectores();
                        Utiles.crearToastPersonalizado(context, "Se finalizo la revision");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utiles.crearToastPersonalizado(context, "Algo fallo, revisa la conexion");
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "133");
                params.put("ID_inyector", ID_inyector);
                return params;
            }
        };

        RequestQueue requestQueue2 = Volley.newRequestQueue(context);
        requestQueue2.add(stringRequest2);
    }





    @Override
    public void onFilterData(boolean result) {
        if (result) {
            animacionLupe("Oculto");
        } else {
            if ((searchEditTextInicio.getText().toString().equals("") || searchEditTextInicio.getText().toString().isEmpty())) {
                animacionLupe("Oculto");
            } else {
                animacionLupe("Visible");
            }
        }
    }

    private void animacionLupe(String estado) {
        if (estado.equals("Oculto")) {
            contenedorNoContenido.setVisibility(View.GONE);
            textSinContenido.setVisibility(View.GONE);
            textSinContenido.setText("No hay servicios activos en este momento");

        } else {
            contenedorNoContenido.setVisibility(View.VISIBLE);
            textSinContenido.setVisibility(View.VISIBLE);
            textSinContenido.setText("No hay resultados que coincidan con la busqueda");
        }
    }



}