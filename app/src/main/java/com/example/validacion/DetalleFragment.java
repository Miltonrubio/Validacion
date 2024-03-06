package com.example.validacion;


import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.validacion.Adaptadores.AdaptadorActividadesUnidad;
import com.example.validacion.Adaptadores.AdaptadorMecanicos;
import com.example.validacion.Adaptadores.AdaptadorRefacciones;
import com.example.validacion.Adaptadores.DownloadFileTask;
import com.example.validacion.Adaptadores.NuevoDownloadFileTask;
import com.example.validacion.Adaptadores.SlideAdapter;
import com.example.validacion.Adaptadores.Utiles;
import com.example.validacion.Objetos.Mecanicos;
import com.example.validacion.Objetos.Refacciones;
import com.example.validacion.Objetos.SlideItem;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BadPdfFormatException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.Line;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class DetalleFragment extends Fragment {

    private String url;

    List<SlideItem> slideItems = new ArrayList<>();
    List<Mecanicos> listaMecanicos = new ArrayList<>();
    List<Refacciones> listaRefacciones = new ArrayList<>();


    List<ActividadadesUnidad> listaActividadesUnidad = new ArrayList<>();


    private File pdfFileImagenes;


    ViewPager2 viewPager2;
    RecyclerView recyclerViewMecanicos, recyclerViewRefacciones, recyclerViewBitacora;

    TextView tvRefacciones, tvBitacora;


    String marca, modelo, fecha, hora, status, motivo, idventa, telefonousuario, emailusuario, nombreusuario;

    String motorI, vinI, placasI, domicilio, gasolina, anioI, kilometraje;

    Context context;

    LottieAnimationView animacionSinImagenes;

    public DetalleFragment() {
        // Required empty public constructor
    }

    private Handler sliderHandler = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    String id_ser_cliente;


    AlertDialog.Builder builder;
    AlertDialog modalCargando;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detalle, container, false);

        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);

        FloatingActionButton btnImprimirPdf = rootView.findViewById(R.id.btnImprimirPdf);
        TextView textMarca = rootView.findViewById(R.id.tv1);
        TextView textmotivo = rootView.findViewById(R.id.tv3);
        TextView textfecha = rootView.findViewById(R.id.tv4);
        TextView textstatus = rootView.findViewById(R.id.tvstatus);
        tvBitacora = rootView.findViewById(R.id.tvBitacora);
        recyclerViewRefacciones = rootView.findViewById(R.id.recyclerViewRefacciones);
        recyclerViewMecanicos = rootView.findViewById(R.id.recyclerViewMecanicos);
        recyclerViewBitacora = rootView.findViewById(R.id.recyclerViewBitacora);
        recyclerViewBitacora.setVisibility(View.GONE);
        viewPager2 = rootView.findViewById(R.id.ViewPager);
        animacionSinImagenes = rootView.findViewById(R.id.animacionSinImagenes);
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            marca = bundle.getString("marca", "");
            modelo = bundle.getString("modelo", "");
            fecha = bundle.getString("fecha_ingreso", "");
            hora = bundle.getString("hora_ingreso", "");
            status = bundle.getString("status", "");
            motivo = bundle.getString("motivo", "");
            idventa = bundle.getString("idventa", "");
            motorI = bundle.getString("motorI", "");
            vinI = bundle.getString("vinI", "");
            placasI = bundle.getString("placasI", "");
            gasolina = bundle.getString("gasolina", "");
            anioI = bundle.getString("anioI", "");
            kilometraje = bundle.getString("kilometraje", "");
            nombreusuario = bundle.getString("nombre", "");
            telefonousuario = bundle.getString("telefono", "");
            emailusuario = bundle.getString("email", "");
            domicilio = bundle.getString("domicilio", "");
            id_ser_cliente = bundle.getString("id_ser_cliente", "");


            CargarRefacciones(idventa);
            CargarMecanicos(idventa);
            CargarImagenes(idventa);
            //    CargarBitacora(idventa);
            //   CargarChecks(idventa);

            textMarca.setText(marca.toUpperCase() + " - " + modelo.toUpperCase());
            textmotivo.setText(motivo);


            if (fecha.equals("null") || fecha.isEmpty()) {

                textfecha.setText("No hay fecha estimada");
            } else {
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = inputFormat.parse(fecha);

                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new DateFormatSymbols(new Locale("es", "ES")));
                    String fecha_formateada = outputFormat.format(date);


                    try {

                        SimpleDateFormat inputFormatHora = new SimpleDateFormat("HH:mm:ss");
                        Date time = inputFormatHora.parse(hora);

                        SimpleDateFormat outputFormatHora = new SimpleDateFormat("hh:mm a");
                        String hora_formateada = outputFormatHora.format(time);

                        textfecha.setText("Ingresado el dia: \n" + fecha_formateada + " a las " + hora_formateada);

                    } catch (Exception e) {

                        textfecha.setText("Ingresado: " + "el " + fecha_formateada);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int colorBlanco = ContextCompat.getColor(requireContext(), R.color.white);
            int colorAmarillo = ContextCompat.getColor(requireContext(), R.color.amarillo);
            int colorVerde = ContextCompat.getColor(requireContext(), R.color.verde);
            int colorRojo = ContextCompat.getColor(requireContext(), R.color.rojo);
            int colorAzulito = ContextCompat.getColor(requireContext(), R.color.azulitoSuave);
            int colorNegro = ContextCompat.getColor(requireContext(), R.color.black);
            int colorGris = ContextCompat.getColor(requireContext(), R.color.gris);

            if (!status.equals("null") || status.isEmpty()) {
                textstatus.setText("Estatus: " + status.toUpperCase());

                if (status.equalsIgnoreCase("pendiente")) {
                    textstatus.setTextColor(colorAmarillo);
                } else if (status.equalsIgnoreCase("Finalizado")) {
                    textstatus.setTextColor(colorVerde);
                } else if (status.equalsIgnoreCase("Servicios programado")) {
                    textstatus.setTextColor(colorVerde);
                } else if (status.equalsIgnoreCase("Cita programada")) {
                    textstatus.setTextColor(colorAzulito);
                } else if (status.equalsIgnoreCase("Diagnostico")) {
                    textstatus.setTextColor(colorRojo);
                } else if (status.equalsIgnoreCase("En espera")) {
                    textstatus.setTextColor(colorGris);
                } else if (status.equalsIgnoreCase("En servicio")) {
                    textstatus.setTextColor(colorAmarillo);
                } else if (status.equalsIgnoreCase("SubirFotosUnidadesActivity de ruta")) {
                    textstatus.setTextColor(colorNegro);
                } else if (status.equalsIgnoreCase("Lavado y detallado")) {
                    textstatus.setTextColor(colorAzulito);
                } else if (status.equalsIgnoreCase("Listo para entrega")) {
                    textstatus.setTextColor(colorVerde);
                } else if (status.equalsIgnoreCase("Entregado")) {
                    textstatus.setTextColor(colorVerde);
                } else {
                    textstatus.setTextColor(colorAzulito);
                }
            } else {
                textstatus.setText("Status no disponible");
                textstatus.setTextColor(colorRojo);
            }
        }

        btnImprimirPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View customView = LayoutInflater.from(context).inflate(R.layout.modal_nuevo_modal_pdfs, null);
                builder.setView(ModalRedondeado(context, customView));
                AlertDialog dialogConBotones = builder.create();
                dialogConBotones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogConBotones.show();


                LinearLayout LayoutPDFServicio = customView.findViewById(R.id.LayoutPDFServicio);
                //    LinearLayout pdfCheckEntrada = customView.findViewById(R.id.pdfCheckEntrada);
                LinearLayout LayoutPDFSalida = customView.findViewById(R.id.LayoutPDFSalida);
                LinearLayout LayoutPDFTecnico = customView.findViewById(R.id.LayoutPDFTecnico);
                LinearLayout PDFRefacciones = customView.findViewById(R.id.PDFRefacciones);
                LinearLayout LayoutPDFMecanicos = customView.findViewById(R.id.LayoutPDFMecanicos);

                PDFRefacciones.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "82");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", idventa);

                        new NuevoDownloadFileTask(context, postData).execute(url);

                    }
                });


                LayoutPDFMecanicos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Mostrar diálogo de carga

                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "81");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", idventa);

                        // Iniciar la tarea de descarga del archivo PDF
                        new NuevoDownloadFileTask(context, postData).execute(url);


/*
                        modalCargando = Utiles.ModalCargando(context, builder);


                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "81");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", id_ser_venta);

                        new DownloadFileTask(context, postData).execute(url);



                        modalCargando.dismiss();

 */
                    }
                });


                LayoutPDFServicio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "83");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", idventa);

                        new NuevoDownloadFileTask(context, postData).execute(url);
/*
                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "50");
                        postData.put("idventa", idventa);

                        new DownloadFileTask(context, postData).execute(url);
*/


                    }
                });

/*
                pdfCheckEntrada.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modalCargando = Utiles.ModalCargando(context, builder);

                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "78");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", id_ser_venta);

                        new DownloadFileTask(context, postData).execute(url);
                        modalCargando.dismiss();

                    }
                });

 */

                LayoutPDFSalida.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "79");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", idventa);

                        new NuevoDownloadFileTask(context, postData).execute(url);

                    }
                });

                LayoutPDFTecnico.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "80");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", idventa);

                        new NuevoDownloadFileTask(context, postData).execute(url);

                    }
                });
/*
                Map<String, String> postData = new HashMap<>();
                postData.put("opcion", "50");
                postData.put("idventa", idventa);

                new DownloadFileTask(context, postData).execute(url);


 */

                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.opciones_pdfs, null);
                builder.setView(ModalRedondeado(view.getContext(), customView));
                AlertDialog dialogConBotones = builder.create();
                dialogConBotones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogConBotones.show();


                LinearLayout pdfServicio = customView.findViewById(R.id.pdfServicio);
                LinearLayout pdfCheckEntrada = customView.findViewById(R.id.pdfCheckEntrada);
                LinearLayout checkSalida = customView.findViewById(R.id.checkSalida);
                LinearLayout checkTecnico = customView.findViewById(R.id.checkTecnico);
                LinearLayout REFACCIONES = customView.findViewById(R.id.REFACCIONES);
                LinearLayout mecanicos = customView.findViewById(R.id.mecanicos);

                REFACCIONES.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        modalCargando = Utiles.ModalCargando(context, builder);

                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "82");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", idventa);

                        new DownloadFileTask(context, postData).execute(url);
                        modalCargando.dismiss();

                    }
                });


                mecanicos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        modalCargando = Utiles.ModalCargando(context, builder);
                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "81");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", idventa);

                        new DownloadFileTask(context, postData).execute(url);
                        modalCargando.dismiss();
                    }
                });


                pdfServicio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        modalCargando = Utiles.ModalCargando(context, builder);

                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "83");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", idventa);

                        new DownloadFileTask(context, postData).execute(url);
                        modalCargando.dismiss();

                    }
                });


                pdfCheckEntrada.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modalCargando = Utiles.ModalCargando(context, builder);

                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "78");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", idventa);

                        new DownloadFileTask(context, postData).execute(url);
                        modalCargando.dismiss();

                    }
                });

                checkSalida.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        modalCargando = Utiles.ModalCargando(context, builder);
                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "79");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", idventa);

                        new DownloadFileTask(context, postData).execute(url);
                        modalCargando.dismiss();

                    }
                });

                checkTecnico.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        modalCargando = Utiles.ModalCargando(context, builder);
                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "80");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", idventa);

                        new DownloadFileTask(context, postData).execute(url);
                        modalCargando.dismiss();

                    }
                });

                */
            }
        });


        return rootView;
    }


    private void CargarImagenes(String idventa) {
        slideItems.clear();

        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject fotoObj = jsonArray.getJSONObject(i);
                                String foto = fotoObj.getString("foto");
                                String id_ser_venta = fotoObj.getString("id_ser_venta");
                                String fotoUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/";

                                slideItems.add(new SlideItem(fotoUrl + foto, id_ser_venta));
                            }

                            SlideAdapter slideAdapter = new SlideAdapter(slideItems, viewPager2);

                            viewPager2.setAdapter(slideAdapter);
                            viewPager2.setClipToPadding(false);
                            viewPager2.setClipChildren(false);
                            viewPager2.setOffscreenPageLimit(4);
                            viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                            compositePageTransformer.addTransformer(new MarginPageTransformer(10));
                            compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                                @Override
                                public void transformPage(@NonNull View page, float position) {
                                    float r = 1 - Math.abs(position);
                                    page.setScaleY(0.85f + 0.15f);
                                }
                            });

                            viewPager2.setPageTransformer(compositePageTransformer);
                            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                public void onPageSelected(int position) {
                                    super.onPageSelected(position);
                                    sliderHandler.removeCallbacks(sliderRunnable);
                                    sliderHandler.postDelayed(sliderRunnable, 3000);
                                }
                            });


                            if (slideItems.size() > 0) {
                                animacionSinImagenes.setVisibility(View.GONE);
                                viewPager2.setVisibility(View.VISIBLE);
                            } else {

                                animacionSinImagenes.setVisibility(View.VISIBLE);
                                viewPager2.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            animacionSinImagenes.setVisibility(View.VISIBLE);
                            viewPager2.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        animacionSinImagenes.setVisibility(View.VISIBLE);
                        viewPager2.setVisibility(View.GONE);
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "8");
                params.put("idventa", idventa);
                return params;
            }
        };

        RequestQueue requestQueue3 = Volley.newRequestQueue(context);
        requestQueue3.add(stringRequest3);

    }

    private void CargarMecanicos(String idventa) {

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String foto = jsonObject.getString("foto");
                                    String nombre = jsonObject.getString("nombre");
                                    String idusuario = jsonObject.getString("idusuario");
                                    String observaciones = jsonObject.getString("observaciones");
                                    String fecha_programada = jsonObject.getString("fecha");
                                    Mecanicos mecanicos = new Mecanicos(foto, nombre, fecha_programada, idusuario, observaciones);
                                    listaMecanicos.add(mecanicos);
                                }


                                if (listaMecanicos.isEmpty()) {
                                    recyclerViewMecanicos.setVisibility(View.GONE);
                                    LinearLayout LayoutNoMecanicos = requireView().findViewById(R.id.LayoutNoMecanicos);
                                    LayoutNoMecanicos.setVisibility(View.VISIBLE);
                                } else {
                                    recyclerViewMecanicos.setVisibility(View.VISIBLE);
                                    LinearLayout LayoutNoMecanicos = requireView().findViewById(R.id.LayoutNoMecanicos);
                                    LayoutNoMecanicos.setVisibility(View.GONE);
                                }

                                AdaptadorMecanicos adaptadorMecanicos = new AdaptadorMecanicos(listaMecanicos);
                                LinearLayoutManager layoutManager2 = new LinearLayoutManager(context);
                                recyclerViewMecanicos.setLayoutManager(layoutManager2);
                                recyclerViewMecanicos.setAdapter(adaptadorMecanicos);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("API Response", "Respuesta vacía");
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
                params.put("opcion", "104");
                params.put("ID_ser_venta", idventa);
                return params;
            }
        };

        RequestQueue requestQueue2 = Volley.newRequestQueue(context);
        requestQueue2.add(stringRequest2);

    }

    private void CargarRefacciones(String idventa) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String idrefaccion = jsonObject.getString("idrefaccion");
                                    String descripcion = jsonObject.getString("descripcion");
                                    String precio = jsonObject.getString("precio");
                                    String cantidad = jsonObject.getString("cantidad");

                                    Refacciones refaccion = new Refacciones(cantidad, descripcion, precio, idrefaccion);
                                    listaRefacciones.add(refaccion);
                                }

                                if (listaRefacciones.isEmpty()) {
                                    recyclerViewRefacciones.setVisibility(View.GONE);
                                    LinearLayout LayoutNoRefacciones = requireView().findViewById(R.id.LayoutNoRefacciones);
                                    LayoutNoRefacciones.setVisibility(View.VISIBLE);
                                } else {
                                    recyclerViewRefacciones.setVisibility(View.VISIBLE);
                                    LinearLayout LayoutNoRefacciones = requireView().findViewById(R.id.LayoutNoRefacciones);
                                    LayoutNoRefacciones.setVisibility(View.GONE);

                                    AdaptadorRefacciones adaptadorRefacciones = new AdaptadorRefacciones(listaRefacciones);
                                    recyclerViewRefacciones.setLayoutManager(new LinearLayoutManager(context));
                                    recyclerViewRefacciones.setAdapter(adaptadorRefacciones);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            tvRefacciones.setText("No hay Refacciones para mostrar");
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
                params.put("opcion", "3");
                params.put("idventa", idventa);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

/*
    private void CargarBitacora(String idservicio) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String horainicio = jsonObject.getString("horainicio");
                                    String horafin = jsonObject.getString("horafin");
                                    String fecha = jsonObject.getString("fecha");
                                    String id_servicio = jsonObject.getString("id_servicio");
                                    String idpersonal = jsonObject.getString("idpersonal");
                                    String observaciones = jsonObject.getString("observaciones");
                                    String estatus = jsonObject.getString("estatus");
                                    String nombre = jsonObject.getString("nombre");
                                    String telefono = jsonObject.getString("telefono");
                                    String area = jsonObject.getString("area");
                                    String foto = jsonObject.getString("foto");

                                    ActividadadesUnidad actividadadesUnidad = new ActividadadesUnidad(horainicio, horafin, fecha, id_servicio, idpersonal, observaciones, estatus, nombre, telefono, area, foto);
                                    listaActividadesUnidad.add(actividadadesUnidad);
                                }

                                if (listaActividadesUnidad.isEmpty()) {
                                    recyclerViewBitacora.setVisibility(View.GONE);
                                    LinearLayout LayoutNoActividades = requireView().findViewById(R.id.LayoutNoActividades);
                                    LayoutNoActividades.setVisibility(View.VISIBLE);
                                } else {
                                    recyclerViewBitacora.setVisibility(View.VISIBLE);
                                    LinearLayout LayoutNoActividades = requireView().findViewById(R.id.LayoutNoActividades);
                                    LayoutNoActividades.setVisibility(View.GONE);

                                    AdaptadorActividadesUnidad adaptadorActividadesUnidad = new AdaptadorActividadesUnidad(listaActividadesUnidad);
                                    recyclerViewBitacora.setLayoutManager(new LinearLayoutManager(requireContext()));
                                    recyclerViewBitacora.setAdapter(adaptadorActividadesUnidad);
                                }

                            } catch (JSONException e) {

                                Log.e("JSON Error", "Error al analizar la respuesta JSON: " + e.getMessage());
                            }
                        } else {
                            tvBitacora.setText("No hay Actividades para esta unidad");
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
                params.put("opcion", "26");
                params.put("idservicio", idservicio);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
*/

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };


    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

}