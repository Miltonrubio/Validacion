package com.example.validacion;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;
import static com.example.validacion.Adaptadores.Utiles.crearToastPersonalizado;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.service.controls.Control;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorCoches;
import com.example.validacion.Adaptadores.AdaptadorMarcaDesdeInicio;
import com.example.validacion.Adaptadores.AdaptadorMarcas;
import com.example.validacion.Adaptadores.AdaptadorModeloDesdeInicio;
import com.example.validacion.Adaptadores.AdaptadorModelos;
import com.example.validacion.Adaptadores.AdaptadorSeleccionarCliente;
import com.example.validacion.Adaptadores.AdaptadorSeleccionarUnidad;
import com.example.validacion.Adaptadores.AdaptadorTiposUnidades;
import com.example.validacion.Adaptadores.AdaptadorTiposUnidadesDesdeInicio;
import com.example.validacion.Adaptadores.Utiles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements AdaptadorCoches.OnActivityActionListener, AdaptadorSeleccionarCliente.OnActivityActionListener, AdaptadorSeleccionarUnidad.OnActivityActionListener, AdaptadorTiposUnidadesDesdeInicio.OnActivityActionListener {

    private AdaptadorCoches.OnActivityActionListener cochesActionListener;
    private AdaptadorSeleccionarCliente.OnActivityActionListener clienteActionListener;
    private AdaptadorSeleccionarUnidad.OnActivityActionListener unidadActionListener;
    //  private AdaptadorModeloDesdeInicio.OnActivityActionListener modeloActionListener;
    private AdaptadorTiposUnidadesDesdeInicio.OnActivityActionListener tiposUnidadesActionListener;
    AdaptadorSeleccionarCliente adaptadorSeleccionarCliente;
    String valorGas = null;
    private String selectedIDCliente = "1";
    ArrayList<String> opciones = new ArrayList<>();

    String url;
    Context context;

    private ArrayList<String> nombresClientes = new ArrayList<>();
    String id_serv_unidad = "";
    private ArrayList<String> unidadesVehiculos = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdaptadorCoches adaptadorCoches;
    private List<JSONObject> dataList = new ArrayList<>();
    TextView textSeleccionarCliente;
    private EditText editTextBusqueda;
    FloatingActionButton botonAgregarActividad;
    AlertDialog.Builder builder;
    AlertDialog modalCargando;

    ConstraintLayout LayoutSinResultados;
    ConstraintLayout LayoutRecycler;
    ConstraintLayout LayoutSinInternet;
    Button btn_pendientes;
    Button btn_ENTREGADAS;
    Button btn_ENTREGADAS2;
    Button btn_pendientes2;
    LottieAnimationView lottieNoClientes;
    AlertDialog dialogClientes;
    TextView textSeleccionarUnidad;


    String nombreCliente;
    String id_ser_cliente;

    List<JSONObject> listaUnidades = new ArrayList<>();
    AdaptadorSeleccionarUnidad adaptadorSeleccionarUnidad;
    AlertDialog dialogUnidades;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);

        botonAgregarActividad = view.findViewById(R.id.botonAgregarActividad);
        recyclerView = view.findViewById(R.id.recyclerViewFragment);
        editTextBusqueda = view.findViewById(R.id.searchEditText);
        LayoutSinInternet = view.findViewById(R.id.LayoutSinInternet);
        LayoutSinResultados = view.findViewById(R.id.LayoutSinResultados);
        LayoutRecycler = view.findViewById(R.id.LayoutRecycler);
        lottieNoClientes = view.findViewById(R.id.lottieNoClientes);
        btn_pendientes = view.findViewById(R.id.btn_pendientes);
        btn_ENTREGADAS = view.findViewById(R.id.btn_ENTREGADAS);


        btn_pendientes2 = view.findViewById(R.id.btn_pendientes2);
        btn_ENTREGADAS2 = view.findViewById(R.id.btn_ENTREGADAS2);


        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        cochesActionListener = this;
        unidadActionListener = this;
        clienteActionListener = this;
//        modeloActionListener = this;
        tiposUnidadesActionListener = this;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //  VerNombresClientes();
        ImageView LectorQr = view.findViewById(R.id.LectorQr);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String permisosUsuario = sharedPreferences.getString("permisos", "");
        String idusuario = sharedPreferences.getString("idusuario", "");


        if (permisosUsuario.equals("RECEPCION") || permisosUsuario.equals("SUPERADMIN")) {
            botonAgregarActividad.setVisibility(View.VISIBLE);
            VisualizarServicios();
        } else {
            botonAgregarActividad.setVisibility(View.GONE);
            VisualizarServiciosPorMecanicos(idusuario);
        }


        opciones.add("Lleno");
        opciones.add("3/4");
        opciones.add("1/2");
        opciones.add("1/4");
        opciones.add("Reserva");


        btn_ENTREGADAS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permisosUsuario.equals("RECEPCION") || permisosUsuario.equals("SUPERADMIN")) {
                    botonAgregarActividad.setVisibility(View.VISIBLE);
                    VisualizarServiciosENTREGADOS();
                } else {
                    botonAgregarActividad.setVisibility(View.GONE);
                    //VisualizarServiciosPorMecanicosENTREGADAS(idusuario);

                    Utiles.crearToastPersonalizado(context, "Esta opción es unicamente para administrativos");

                }
            }
        });

        btn_ENTREGADAS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (permisosUsuario.equals("RECEPCION") || permisosUsuario.equals("SUPERADMIN")) {
                    botonAgregarActividad.setVisibility(View.VISIBLE);
                    VisualizarServiciosENTREGADOS();
                } else {
                    botonAgregarActividad.setVisibility(View.GONE);
                    //   VisualizarServiciosPorMecanicosENTREGADAS(idusuario);
                    Utiles.crearToastPersonalizado(context, "Esta opción es unicamente para administrativos");
                }
            }
        });


        btn_pendientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permisosUsuario.equals("RECEPCION") || permisosUsuario.equals("SUPERADMIN")) {
                    botonAgregarActividad.setVisibility(View.VISIBLE);
                    VisualizarServicios();
                } else {
                    botonAgregarActividad.setVisibility(View.GONE);
                    VisualizarServiciosPorMecanicos(idusuario);
                }
            }
        });

        btn_pendientes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permisosUsuario.equals("RECEPCION") || permisosUsuario.equals("SUPERADMIN")) {
                    botonAgregarActividad.setVisibility(View.VISIBLE);

                    VisualizarServicios();
                } else {
                    botonAgregarActividad.setVisibility(View.GONE);
                    VisualizarServiciosPorMecanicos(idusuario);
                }
            }
        });

        LectorQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(HomeFragment.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Lector de còdigos - Taller Georgio");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        dataList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adaptadorCoches = new AdaptadorCoches(dataList, context, this);
        recyclerView.setAdapter(adaptadorCoches);


        editTextBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptadorCoches.filter(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        botonAgregarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_serv_unidad = "";
                id_ser_cliente = "";

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View customView = LayoutInflater.from(context).inflate(R.layout.modal_agregar_nuevo_servicio, null);
                builder.setView(Utiles.ModalRedondeado(context, customView));
                AlertDialog dialogNuevoServicio = builder.create();
                dialogNuevoServicio.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogNuevoServicio.show();


                Button buttonGuardar = customView.findViewById(R.id.buttonGuardar);
                Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                SpinnerGasolina = customView.findViewById(R.id.SpinnerGasolina);
                textSeleccionarUnidad = customView.findViewById(R.id.textSeleccionarUnidad);
                textSeleccionarCliente = customView.findViewById(R.id.textSeleccionarCliente);
                ImageView btnAgregarCliente = customView.findViewById(R.id.btnAgregarCliente);
                TextView textSeleccionarCliente = customView.findViewById(R.id.textSeleccionarCliente);

                gasolinaTV = customView.findViewById(R.id.gasolina);
                KilometrajeTV = customView.findViewById(R.id.Kilometraje);


                editTextKilometraje = customView.findViewById(R.id.editTextKilometraje);
                editTextMotivoIngreso = customView.findViewById(R.id.editTextMotivoIngreso);

/*
                tvNumInyectores = customView.findViewById(R.id.tvNumInyectores);
                EditTextNumInyectores = customView.findViewById(R.id.EditTextNumInyectores);
                tvNumInyectores.setVisibility(View.GONE);
                EditTextNumInyectores.setVisibility(View.GONE);


                */


                ArrayAdapter<String> adaptadorGas = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, opciones);
                adaptadorGas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinnerGasolina.setAdapter(adaptadorGas);
                SpinnerGasolina.setSelection(0);


                textSeleccionarUnidad.setEnabled(false);

                SpinnerGasolina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        valorGas = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }

                });


                buttonGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String motivoIngreso = editTextMotivoIngreso.getText().toString().trim();

                        String km;

                        String numeroInyectores;

                        if (motivoIngreso.isEmpty() || id_serv_unidad.isEmpty() || id_ser_cliente.isEmpty()) {

                            Utiles.crearToastPersonalizado(context, "Debes completar todos los campos");

                        } else {


                            if (editTextKilometraje.getVisibility() == View.VISIBLE) {
                                km = editTextKilometraje.getText().toString().trim();

                            } else {
                                km = "N/A";
                            }


                            if (SpinnerGasolina.getVisibility() == View.VISIBLE) {
                                valorGas = valorGas;
                            } else {
                                valorGas = "N/A";
                            }

/*
                            if (EditTextNumInyectores.getVisibility() == View.VISIBLE) {
                                numeroInyectores = EditTextNumInyectores.getText().toString().trim();

                                try {
                                    int numeroInyect = Integer.parseInt(numeroInyectores);
                                    if (numeroInyect < 1) {
                                        Utiles.crearToastPersonalizado(context, "No puedes ingresar un número menor a 1");

                                    } else {

                                        AgregarServicio(id_ser_cliente, id_serv_unidad, km, valorGas, motivoIngreso, marca, modelo, motor, vin, placas, anio, fotoUnidad, tipoUnidad, numeroInyectores);

                                        dialogNuevoServicio.dismiss();
                                    }
                                } catch (NumberFormatException e) {

                                    Utiles.crearToastPersonalizado(context, "No puedes ingresar decimales u otros digitos");
                                }

                            } else {
                                numeroInyectores = "NoAplica";
                                */

                                AgregarServicio(id_ser_cliente, id_serv_unidad, km, valorGas, motivoIngreso, marca, modelo, motor, vin, placas, anio, fotoUnidad, tipoUnidad /* , numeroInyectores */);

                                dialogNuevoServicio.dismiss();
                            }
                      //  }

                    }
                });


                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogNuevoServicio.dismiss();
                    }
                });

                textSeleccionarUnidad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View customView = LayoutInflater.from(context).inflate(R.layout.modal_opciones_clientes, null);
                        builder.setView(Utiles.ModalRedondeado(context, customView));
                        dialogUnidades = builder.create();
                        dialogUnidades.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogUnidades.show();

                        lottieNoResultados = customView.findViewById(R.id.lottieNoResultados);
                        MostrarUnidadesClientes(id_ser_cliente, view.getContext());


                        Bundle bundleUsuario = new Bundle();
                        bundleUsuario.putString("nombreUsuario", nombreCliente);
                        bundleUsuario.putString("id_ser_cliente", id_ser_cliente);

                        TextView NombreclIENTE = customView.findViewById(R.id.NombreclIENTE);
                        NombreclIENTE.setText("UNIDADES DE " + nombreCliente.toUpperCase());
                        LayoutConContenidoUnidades = customView.findViewById(R.id.LayoutConContenido);
                        LayoutSinInternetUnidades = customView.findViewById(R.id.LayoutSinInternet);
                        LayoutSinContenidoUnidades = customView.findViewById(R.id.LayoutSinContenido);


                        searchEditText = customView.findViewById(R.id.searchEditText);
                        ImageView btnAgregarUnidad = customView.findViewById(R.id.btnAgregarUnidad);
                        ImageView AgregarNuevaUnidad = customView.findViewById(R.id.AgregarNuevaUnidad);

                        btnAgregarUnidad.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //        MostrarModalMarcas(view, bundleUsuario);
                                MostrarModalAgregarNuevaUnidad(view, bundleUsuario);
                            }
                        });
                        AgregarNuevaUnidad.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //      MostrarModalMarcas(view, bundleUsuario);
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
                });


                textSeleccionarCliente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View customView = LayoutInflater.from(context).inflate(R.layout.modal_opciones_clientes, null);
                        builder.setView(Utiles.ModalRedondeado(context, customView));
                        dialogClientes = builder.create();
                        dialogClientes.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogClientes.show();
                        ImageView btnAgregarUnidad = customView.findViewById(R.id.btnAgregarUnidad);
                        btnAgregarUnidad.setVisibility(View.GONE);
                        lottieNoResultados = customView.findViewById(R.id.lottieNoResultados);

                        VerClientes(view.getContext());

                        TextView NombreclIENTE = customView.findViewById(R.id.NombreclIENTE);
                        NombreclIENTE.setText("LISTADO DE CLIENTES");
                        searchEditText = customView.findViewById(R.id.searchEditText);

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
                });


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

            }
        });


    }

    List<JSONObject> listaClientes = new ArrayList<>();

    private void VerClientes(Context context) {
        listaClientes.clear();

        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String scanResult = result.getContents();
            editTextBusqueda.setText(scanResult);
        } else {

            Utiles.crearToastPersonalizado(context, "Escaneo cancelado");
        }
    }

    private void VisualizarServicios() {
        dataList.clear();
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        dataList.add(jsonObject);

                    }
                    adaptadorCoches.notifyDataSetChanged();
                    adaptadorCoches.setFilteredData(dataList);
                    adaptadorCoches.filter("");

                    if (dataList.size() > 0) {

                        mostrarLayout("conContenido");
                    } else {

                        mostrarLayout("SinContenido");
                    }

                    editTextBusqueda.setText("");
                } catch (JSONException e) {
                    mostrarLayout("SinContenido");
                    editTextBusqueda.setText("");
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
                params.put("opcion", "2");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void VisualizarServiciosENTREGADOS() {
        dataList.clear();
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        dataList.add(jsonObject);

                    }
                    adaptadorCoches.notifyDataSetChanged();
                    adaptadorCoches.setFilteredData(dataList);
                    adaptadorCoches.filter("");

                    if (dataList.size() > 0) {

                        mostrarLayout("conContenido");
                    } else {
                        mostrarLayout("SinContenido");
                    }
                    editTextBusqueda.setText("");

                } catch (JSONException e) {

                    mostrarLayout("SinContenido");
                    editTextBusqueda.setText("");
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
                params.put("opcion", "37");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void mostrarLayout(String estado) {
        if (estado.equalsIgnoreCase("SinInternet")) {
            LayoutSinInternet.setVisibility(View.VISIBLE);
            LayoutRecycler.setVisibility(View.GONE);
            LayoutSinResultados.setVisibility(View.GONE);

        } else if (estado.equalsIgnoreCase("SinContenido")) {
            LayoutSinInternet.setVisibility(View.GONE);
            LayoutRecycler.setVisibility(View.GONE);
            LayoutSinResultados.setVisibility(View.VISIBLE);

        } else if (estado.equalsIgnoreCase("conContenido")) {
            LayoutSinInternet.setVisibility(View.GONE);
            LayoutRecycler.setVisibility(View.VISIBLE);
            LayoutSinResultados.setVisibility(View.GONE);
        } else {

            LayoutSinInternet.setVisibility(View.GONE);
            LayoutRecycler.setVisibility(View.GONE);
            LayoutSinResultados.setVisibility(View.VISIBLE);
        }

        onLoadComplete();
    }


    private void onLoadComplete() {
        if (modalCargando.isShowing() && modalCargando != null) {
            modalCargando.dismiss();
        }
    }


    private void VisualizarServiciosPorMecanicos(String idmecanico) {
        dataList.clear();
        modalCargando = Utiles.ModalCargando(context, builder);

        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("\"fallo\"")) {
                    mostrarLayout("SinContenido");
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            dataList.add(jsonObject);
                        }

                        if (dataList.size() > 0) {
                            mostrarLayout("conContenido");
                        } else {
                            mostrarLayout("SinContenido");
                        }


                        adaptadorCoches.notifyDataSetChanged();
                        adaptadorCoches.setFilteredData(dataList);
                        adaptadorCoches.filter("");
                        editTextBusqueda.setText("");

                    } catch (JSONException e) {

                        mostrarLayout("SinContenido");
                        editTextBusqueda.setText("");
                    }
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
                params.put("opcion", "25");
                params.put("idmecanico", idmecanico);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);

    }


    private void VisualizarServiciosPorMecanicosENTREGADAS(String idmecanico) {
        dataList.clear();
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        dataList.add(jsonObject);

                    }
                    adaptadorCoches.notifyDataSetChanged();
                    adaptadorCoches.setFilteredData(dataList);
                    adaptadorCoches.filter("");

                    if (dataList.size() > 0) {

                        mostrarLayout("conContenido");
                    } else {

                        mostrarLayout("SinInternet");
                    }

                } catch (JSONException e) {
                    mostrarLayout("SinInternet");
                }
                editTextBusqueda.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mostrarLayout("SinInternet");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "37");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    @Override
    public void onFilterData(Boolean resultados) {
        if (resultados) {
            mostrarLayout("conContenido");
        } else {
            if ((editTextBusqueda.getText().toString().equals("") || editTextBusqueda.getText().toString().isEmpty())) {

                if (editTextBusqueda.getText().toString().equals("") && dataList.size() > 0) {

                    mostrarLayout("conContenido");
                } else {

                    mostrarLayout("SinContenido");
                }

            } else {
                mostrarLayout("SinContenido");
            }
        }
    }


    ConstraintLayout LayoutConContenidoUnidades;

    ConstraintLayout LayoutSinInternetUnidades;
    ConstraintLayout LayoutSinContenidoUnidades;


    private void MostrarUnidadesClientes(String id_ser_cliente, Context context) {
        listaUnidades.clear();

        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String tipo_unidad = jsonObject.getString("tipo");

                                if (!tipo_unidad.equalsIgnoreCase("Inyector") /* || !tipo_unidad.equalsIgnoreCase("Turbo") */ ) {

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
                },
                new Response.ErrorListener() {
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


    private void AgregarCliente(String nombre, String domicilio, String telefono, String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Utiles.crearToastPersonalizado(context, "Se agrego el cliente " + nombre);
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


    @Override
    public void onTomarCliente(String nombreCliente, String id_ser_cliente) {
        textSeleccionarCliente.setText(nombreCliente.toUpperCase());
        textSeleccionarUnidad.setEnabled(true);
        this.id_ser_cliente = id_ser_cliente;
        this.nombreCliente = nombreCliente;
        id_serv_unidad = "";
        textSeleccionarUnidad.setText("Selecciona la unidad");
    }


    String marca, modelo, anio, placas, motor, vin, fotoUnidad, tipoUnidad;

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
        mostrarFormularios(tipo);

    }


    EditText searchEditText;


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


    LottieAnimationView lottieNoResultados;
    List<JSONObject> listitaTiposUnidades = new ArrayList<>();
    EditText editTextKilometraje;
    EditText editTextMotivoIngreso;
    Spinner SpinnerGasolina;
    TextView gasolinaTV;
    TextView KilometrajeTV;

    /*
    EditText EditTextNumInyectores;
    TextView tvNumInyectores;
*/

    private void mostrarFormularios(String tipoSeleccionado) {

        listitaTiposUnidades.clear();
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    // Buscar el objeto que coincide con el tipoSeleccionado
                    JSONObject selectedObject = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String nombre = jsonObject.getString("nombre");
                        if (nombre.equalsIgnoreCase(tipoSeleccionado)) {
                            selectedObject = jsonObject;
                            break;
                        }
                    }

                    // Verificar si se encontró el objeto
                    if (selectedObject != null) {
                        // Obtener los valores correspondientes
                        String km = selectedObject.getString("km");
                        String gasolina = selectedObject.getString("gasolina");

                        // Aplicar la lógica según los valores obtenidos
                        if (km.equalsIgnoreCase("1")) {
                            editTextKilometraje.setVisibility(View.VISIBLE);
                            KilometrajeTV.setVisibility(View.VISIBLE);
                        } else {
                            editTextKilometraje.setVisibility(View.GONE);
                            KilometrajeTV.setVisibility(View.GONE);
                        }

/*
                        if (tipoSeleccionado.equalsIgnoreCase("Inyector")) {
                            EditTextNumInyectores.setVisibility(View.VISIBLE);
                            tvNumInyectores.setVisibility(View.VISIBLE);
                        } else {

                            EditTextNumInyectores.setVisibility(View.GONE);
                            tvNumInyectores.setVisibility(View.GONE);
                        }
*/
                        if (gasolina.equalsIgnoreCase("1")) {
                            SpinnerGasolina.setVisibility(View.VISIBLE);
                            gasolinaTV.setVisibility(View.VISIBLE);
                        } else {
                            gasolinaTV.setVisibility(View.GONE);
                            SpinnerGasolina.setVisibility(View.GONE);
                        }
                    } else {

                        Utiles.crearToastPersonalizado(context, "No se encontraron coincidencias");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utiles.crearToastPersonalizado(context, "Error al procesar la respuesta JSON: " + e.getMessage());
                }

                modalCargando.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Hubo un error";
                if (error instanceof NetworkError) {
                    errorMessage = "Error de red";
                } else if (error instanceof ServerError) {
                    errorMessage = "Error del servidor";
                } else if (error instanceof AuthFailureError) {
                    errorMessage = "Error de autenticación";
                } else if (error instanceof ParseError) {
                    errorMessage = "Error de análisis";
                } else if (error instanceof NoConnectionError) {
                    errorMessage = "No hay conexión";
                } else if (error instanceof TimeoutError) {
                    errorMessage = "Tiempo de espera agotado";
                }

                Log.e("Error", errorMessage, error);
                Utiles.crearToastPersonalizado(context, errorMessage);
                modalCargando.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "90");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    /*
    private void mostrarFormularios(String tipoSeleccionado) {
        listitaTiposUnidades.clear();
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (km.equalsIgnoreCase("1")) {
                    editTextKilometraje.setVisibility(View.VISIBLE);
                    KilometrajeTV.setVisibility(View.VISIBLE);
                } else {
                    editTextKilometraje.setVisibility(View.GONE);
                    KilometrajeTV.setVisibility(View.GONE);
                }

                if (gasolina.equalsIgnoreCase("1")) {
                    SpinnerGasolina.setVisibility(View.VISIBLE);
                    gasolinaTV.setVisibility(View.VISIBLE);
                } else {
                    gasolinaTV.setVisibility(View.GONE);
                    SpinnerGasolina.setVisibility(View.GONE);
                }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utiles.crearToastPersonalizado(context, "Error al procesar la respuesta JSON: " + e.getMessage());

                }

                modalCargando.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Hubo un error";
                if (error instanceof NetworkError) {
                    errorMessage = "Error de red";
                } else if (error instanceof ServerError) {
                    errorMessage = "Error del servidor";
                } else if (error instanceof AuthFailureError) {
                    errorMessage = "Error de autenticación";
                } else if (error instanceof ParseError) {
                    errorMessage = "Error de análisis";
                } else if (error instanceof NoConnectionError) {
                    errorMessage = "No hay conexión";
                } else if (error instanceof TimeoutError) {
                    errorMessage = "Tiempo de espera agotado";
                }

                Log.e("Error", errorMessage, error);
                Utiles.crearToastPersonalizado(context, errorMessage);
                modalCargando.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "90");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }
*/

    private void AgregarServicio(String id_ser_cliente, String idunidad, String km, String gas, String motivo, String marca, String modelo, String motor, String vin, String placas, String anio, String foto, String tipounidad /*, String numeroInyectores */) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Insertar servicio: ", response);

                VisualizarServicios();
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

                params.put("opcion", "102");
                params.put("id_ser_cliente", id_ser_cliente);
                params.put("idunidad", idunidad);
                params.put("km", km);
                params.put("gas", gas);
                params.put("motivo", motivo);
                params.put("marca", marca);
                params.put("modelo", modelo);
                params.put("motor", motor);
                params.put("vin", vin);
                params.put("placas", placas);
                params.put("anio", anio);
                params.put("tipounidad", tipounidad);
                params.put("foto", foto);
              //  params.put("numeroInyectores", numeroInyectores);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    List<JSONObject> listaMarcas = new ArrayList<>();
    ConstraintLayout LayoutSeleccionarMarca;
    ConstraintLayout LayoutSeleccionarModelo;
    ConstraintLayout LayoutAgregarDatos;

    RecyclerView reciclerViewMarcas;

    AdaptadorMarcaDesdeInicio adaptadorMarcaDesdeInicio;

    LottieAnimationView lottieNoContenido;

    private void MostrarModalAgregarNuevaUnidad(View view, Bundle bundleUsuario) {
        ConsultarTiposUnidades();

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.layout_mostrar_tipos_unidades, null);
        builder.setView(ModalRedondeado(view.getContext(), customView));
        AlertDialog dialogListaUnidades = builder.create();
        dialogListaUnidades.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogListaUnidades.show();
        lottieNoContenido = customView.findViewById(R.id.lottieNoContenido);
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

                        if (!nombreTipoUnidad.equalsIgnoreCase("Inyector") /* && !nombreTipoUnidad.equalsIgnoreCase("Turbo") */ ) {
                            listaTiposUnidades.add(jsonObject);
                        }
                    }
                    if (listaTiposUnidades.size() > 0) {
                        lottieNoContenido.setVisibility(View.GONE);
                    } else {
                        lottieNoContenido.setVisibility(View.VISIBLE);

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


    private void MostrarModalMarcas(View view, Bundle bundleUsuario) {

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_agregar_unidad, null);
        builder.setView(ModalRedondeado(view.getContext(), customView));
        AlertDialog dialogMarcas = builder.create();
        dialogMarcas.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogMarcas.show();

        VerNombresMarcas(view.getContext());

        TextView textView3 = customView.findViewById(R.id.text);
        textView3.setText("SELECCIONA UNA MARCA");

        ConstraintLayout yourConstraintLayoutId = customView.findViewById(R.id.yourConstraintLayoutId);
        yourConstraintLayoutId.setVisibility(View.VISIBLE);
        EditText searchEditText = customView.findViewById(R.id.searchEditText);
        searchEditText.setHint("Buscar la marca");


        LayoutSeleccionarMarca = customView.findViewById(R.id.SeleccionarMarca);
        LayoutSeleccionarModelo = customView.findViewById(R.id.SeleccionarModelo);
        LayoutAgregarDatos = customView.findViewById(R.id.LayoutAgregarDatos);
        reciclerViewMarcas = customView.findViewById(R.id.reciclerViewMarcas);


        LayoutSeleccionarMarca.setVisibility(View.VISIBLE);
        LayoutSeleccionarModelo.setVisibility(View.GONE);
        LayoutAgregarDatos.setVisibility(View.GONE);


        //   adaptadorMarcaDesdeInicio = new AdaptadorMarcaDesdeInicio(listaMarcas, context, bundleUsuario, modeloActionListener, dialogMarcas);
        reciclerViewMarcas.setLayoutManager(new LinearLayoutManager(context));
        reciclerViewMarcas.setAdapter(adaptadorMarcaDesdeInicio);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptadorMarcaDesdeInicio.filter(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private void VerNombresMarcas(Context context) {
        listaMarcas.clear();

        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        listaMarcas.add(jsonObject);
                    }

                    adaptadorMarcaDesdeInicio.setFilteredData(listaMarcas);
                    adaptadorMarcaDesdeInicio.filter("");


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
                params.put("opcion", "32");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    public void onAgregarUnidad(String idcliente, String idmarca, String idmodelo, String anio, String placas, String vin, String motor, String tipo, String foto) {
        dialogUnidades.dismiss();
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Respuesta de volley registrarUsuario", "idcliente" + idcliente + " idmarca" + idmarca + " idmodelo" + idmodelo + " anio" + anio + " placas" + placas + " vin" + vin + " motor" + motor + " tipo" + tipo);
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


    public void cambiarEstado(String ID_ser_venta, String nuevoEstado) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                crearToastPersonalizado(context, "Actualizado correctamente");
                VisualizarServicios();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                crearToastPersonalizado(context, "Hubo un error al actualizar, por favor revisa la conexión");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "103");
                params.put("nuevoEstado", nuevoEstado);
                params.put("ID_ser_venta", ID_ser_venta);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }

    @Override
    public void asignarActividadAServicio(String ID_ser_venta, String idpersonal, String observaciones) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //    CargarMecanicos(ID_ser_venta);
                        VisualizarServicios();
                        Utiles.crearToastPersonalizado(context, "Se asigno el mecanico");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utiles.crearToastPersonalizado(context, "Algo fallo");
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "105");
                params.put("ID_ser_venta", ID_ser_venta);
                params.put("observaciones", observaciones);
                params.put("idpersonal", idpersonal);

                return params;
            }
        };

        RequestQueue requestQueue2 = Volley.newRequestQueue(context);
        requestQueue2.add(stringRequest2);
    }

    @Override
    public void AsignarFolio(String iddoc, String id_ser_venta) {

        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utiles.crearToastPersonalizado(context, "Se asigno la nota correctamente");
                VisualizarServicios();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "Algo fallo, revisa la conexión");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "109");
                params.put("iddoc", iddoc);
                params.put("id_ser_venta", id_ser_venta);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }
}
