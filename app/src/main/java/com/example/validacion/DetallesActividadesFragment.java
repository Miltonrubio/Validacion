package com.example.validacion;

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
import android.widget.DatePicker;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorCajones;
import com.example.validacion.Adaptadores.AdaptadorCoches;
import com.example.validacion.Adaptadores.AdaptadorReporteActividades;
import com.example.validacion.Adaptadores.DownloadFileTask;
import com.example.validacion.Adaptadores.Utiles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.internal.Util;

public class DetallesActividadesFragment extends Fragment {


    public DetallesActividadesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    ConstraintLayout ContenedorSinContenido;
    ConstraintLayout ContenedorContenido;
    ConstraintLayout ContenedorSinInternet;
    Context context;
    String url;


    AlertDialog.Builder builder;
    AlertDialog modalCargando;
    List<JSONObject> listaActividades = new ArrayList<>();
    AdaptadorReporteActividades adaptadorReporteActividades;


    String fechaInicialSeleccionada;

    String fechaFinalSeleccionada;
    String ID_usuario;
    String nombre;

    FloatingActionButton botonPDF;

    LottieAnimationView lottieFecha;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_actividades, container, false);

        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        ContenedorSinContenido = view.findViewById(R.id.ContenedorSinContenido);
        ContenedorContenido = view.findViewById(R.id.ContenedorContenido);
        ContenedorSinInternet = view.findViewById(R.id.ContenedorSinInternet);
        botonPDF = view.findViewById(R.id.botonPDF);
        ContenedorSinInternet.setVisibility(View.GONE);
        ContenedorContenido.setVisibility(View.GONE);
        ContenedorSinContenido.setVisibility(View.VISIBLE);
        lottieFecha = view.findViewById(R.id.lottieFecha);
        Bundle bundle = getArguments();
        if (bundle != null) {
            nombre = bundle.getString("nombre", "");
            ID_usuario = bundle.getString("idusuario", "");
        }


        TextView textView3 = view.findViewById(R.id.textView3);
        textView3.setText("REPORTE DE ACTIVIDADES DE: " + nombre.toUpperCase());
        Button buttonFiltrarPorFecha = view.findViewById(R.id.buttonFiltrarPorFecha);

        RecyclerView recyclerViewActividades = view.findViewById(R.id.recyclerViewActividades);

        adaptadorReporteActividades = new AdaptadorReporteActividades(listaActividades, context);
        recyclerViewActividades.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewActividades.setAdapter(adaptadorReporteActividades);

        lottieFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirModalFechas();
            }
        });


        buttonFiltrarPorFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirModalFechas();
            }
        });


        botonPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fechaInicialSeleccionada.isEmpty() || fechaFinalSeleccionada.isEmpty()) {
                    Utiles.crearToastPersonalizado(context, "Debes seleccionar una fecha");
                } else {

                    Map<String, String> postData = new HashMap<>();
                    String urlPDFActividades = context.getString(R.string.urlPDFActividades);

                    postData.put("idPersonal", ID_usuario);
                    postData.put("fechaInicio", fechaInicialSeleccionada);
                    postData.put("fechaFin", fechaFinalSeleccionada);

/*
                    postData.put("opcion", "85");
                    postData.put("ID_usuario", ID_usuario);
                    postData.put("fechaInicio", fechaInicialSeleccionada);
                    postData.put("fechaFin", fechaFinalSeleccionada);

                    new DownloadFileTask(context, postData).execute(url);

 */
                    new DownloadFileTask(context, postData).execute(urlPDFActividades);
                }
            }
        });

        return view;
    }


    private void abrirModalFechas() {

        fechaFinalSeleccionada = "";
        fechaInicialSeleccionada = "";


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View customView = LayoutInflater.from(context).inflate(R.layout.modal_fecha, null);
        builder.setView(Utiles.ModalRedondeado(context, customView));
        AlertDialog dialogSeleccionarFecha = builder.create();
        dialogSeleccionarFecha.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSeleccionarFecha.show();

        TextView fechaInicial = customView.findViewById(R.id.fechaInicial);
        TextView fechaFinal = customView.findViewById(R.id.fechaFinal);
        Button btnBuscar = customView.findViewById(R.id.btnBuscar);


        fechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View customView = LayoutInflater.from(context).inflate(R.layout.modal_seleccionar_fecha, null);
                builder.setView(Utiles.ModalRedondeado(context, customView));
                AlertDialog dialogCalendario = builder.create();
                dialogCalendario.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogCalendario.show();

                Button btnCancelar = customView.findViewById(R.id.btnCancelar);
                Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);
                DatePicker datePickerFecha = customView.findViewById(R.id.datePickerFecha);

                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogCalendario.dismiss();
                    }
                });


                buttonAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int day = datePickerFecha.getDayOfMonth();
                        int month = datePickerFecha.getMonth(); // El mes comienza desde 0 (enero = 0, febrero = 1, etc.)
                        int year = datePickerFecha.getYear();

                        fechaFinalSeleccionada = year + "-" + (month + 1) + "-" + day; // El mes se incrementa en 1 para ajustar a la convención (enero = 1, febrero = 2, etc.)


                        try {
                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            Date date = inputFormat.parse(fechaFinalSeleccionada);
                            SimpleDateFormat outputDayOfWeek = new SimpleDateFormat("EEEE", new Locale("es", "ES"));
                            String dayOfWeek = outputDayOfWeek.format(date);
                            SimpleDateFormat outputFormat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
                            String formattedDate = outputFormat.format(date);

                            fechaFinal.setText(dayOfWeek.toLowerCase() + " " + formattedDate.toLowerCase());

                        } catch (ParseException e) {
                        }

                        dialogCalendario.dismiss();
                    }
                });


            }
        });


        fechaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View customView = LayoutInflater.from(context).inflate(R.layout.modal_seleccionar_fecha, null);
                builder.setView(Utiles.ModalRedondeado(context, customView));
                AlertDialog dialogCalendario = builder.create();
                dialogCalendario.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogCalendario.show();

                Button btnCancelar = customView.findViewById(R.id.btnCancelar);
                Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);
                DatePicker datePickerFecha = customView.findViewById(R.id.datePickerFecha);

                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogCalendario.dismiss();
                    }
                });


                buttonAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int day = datePickerFecha.getDayOfMonth();
                        int month = datePickerFecha.getMonth(); // El mes comienza desde 0 (enero = 0, febrero = 1, etc.)
                        int year = datePickerFecha.getYear();

                        fechaInicialSeleccionada = year + "-" + (month + 1) + "-" + day; // El mes se incrementa en 1 para ajustar a la convención (enero = 1, febrero = 2, etc.)


                        try {
                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            Date date = inputFormat.parse(fechaInicialSeleccionada);
                            SimpleDateFormat outputDayOfWeek = new SimpleDateFormat("EEEE", new Locale("es", "ES"));
                            String dayOfWeek = outputDayOfWeek.format(date);
                            SimpleDateFormat outputFormat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
                            String formattedDate = outputFormat.format(date);

                            fechaInicial.setText(dayOfWeek.toLowerCase() + " " + formattedDate.toLowerCase());

                        } catch (ParseException e) {
                        }

                        dialogCalendario.dismiss();
                    }
                });


            }
        });


        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (fechaInicialSeleccionada.equals("") || fechaFinalSeleccionada.equals("")) {
                    Utiles.crearToastPersonalizado(context, "Debes seleccionar la fecha inicial y la fecha final");
                } else {

                    dialogSeleccionarFecha.dismiss();
                    ActividadesPorFecha(ID_usuario, fechaInicialSeleccionada, fechaFinalSeleccionada);
                }


            }
        });

    }


    private void ActividadesPorFecha(String ID_usuario, String fechaInicio, String fechaFin) {
        listaActividades.clear();
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
                            listaActividades.add(jsonObject);
                        }

                        if (listaActividades.size() > 0) {
                            mostrarLayout("conContenido");
                        } else {
                            mostrarLayout("SinContenido");
                        }


                        adaptadorReporteActividades.notifyDataSetChanged();
                        adaptadorReporteActividades.setFilteredData(listaActividades);
                        adaptadorReporteActividades.filter("");

                    } catch (JSONException e) {

                        mostrarLayout("SinContenido");
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
                params.put("opcion", "84");
                params.put("ID_usuario", ID_usuario);
                params.put("fechaInicio", fechaInicio);
                params.put("fechaFin", fechaFin);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);

    }


    private void mostrarLayout(String estado) {
        if (estado.equalsIgnoreCase("SinInternet")) {
            ContenedorContenido.setVisibility(View.GONE);
            ContenedorSinContenido.setVisibility(View.GONE);
            ContenedorSinInternet.setVisibility(View.VISIBLE);

        } else if (estado.equalsIgnoreCase("conContenido")) {
            ContenedorContenido.setVisibility(View.VISIBLE);
            ContenedorSinContenido.setVisibility(View.GONE);
            ContenedorSinInternet.setVisibility(View.GONE);
        } else {

            ContenedorContenido.setVisibility(View.GONE);
            ContenedorSinContenido.setVisibility(View.VISIBLE);
            ContenedorSinInternet.setVisibility(View.GONE);
        }
        modalCargando.dismiss();
    }

}