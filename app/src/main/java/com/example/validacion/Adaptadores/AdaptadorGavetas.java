package com.example.validacion.Adaptadores;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.validacion.ConsultaDeInventariosFragment;
import com.example.validacion.DetallesArrastres;
import com.example.validacion.R;
import com.itextpdf.text.pdf.parser.Line;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AdaptadorGavetas extends RecyclerView.Adapter<AdaptadorGavetas.ViewHolder> {


    private Context context;

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    String url;

    AdaptadorCajones adaptadorCajones;

/*
    List<JSONObject> listaCajones = new ArrayList<>();
    RecyclerView reciclerViewCajones;
    LottieAnimationView lottieNoCajones;
    TextView textViewSinContenido;
    LottieAnimationView lottieNoInternet;
*/

    AdaptadorHerramientas.OnActivityActionListener actionListener;
    AdaptadorMecanicosNuevo.OnActivityActionListener actionListenerMecanicos;

    String permisosUsuario, nombresesioniniciada, IDSesionIniciada;


    public AdaptadorGavetas(List<JSONObject> data, Context context, AdaptadorHerramientas.OnActivityActionListener actionListener, AdaptadorGavetas.OnActivityActionListener actionListenerGaveta, AdaptadorMecanicosNuevo.OnActivityActionListener actionListenerMecanicos) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.actionListener = actionListener;
        this.actionListenerGaveta = actionListenerGaveta;
        this.actionListenerMecanicos = actionListenerMecanicos;

        SharedPreferences sharedPreferences = context.getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        permisosUsuario = sharedPreferences.getString("permisos", "");
        nombresesioniniciada = sharedPreferences.getString("nombre", "");
        IDSesionIniciada = sharedPreferences.getString("idusuario", "");


        Calendar calendarHoy = Calendar.getInstance();
        fechaHoy = calendarHoy.getTime();

    }

    Date fechaHoy;

    AdaptadorMecanicosNuevo adaptadorMecanicosNuevo;

    private AdaptadorGavetas.OnActivityActionListener actionListenerGaveta;

    public interface OnActivityActionListener {


        void onEliminarGaveta(String id_gabeta, String nombre, AlertDialog dialogOpcionesGaveta, AlertDialog dialogConfirmacion);

        void AsignarAlarma(String id_gabeta, String frecuenciaDias);


        void EditarGaveta(String idgabeta, String EditdescripcionGaveta, String EditnombreGaveta);

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gabetas, parent, false);
        return new ViewHolder(view);

    }


    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        List<JSONObject> listaDeCajones = new ArrayList<>();
        url = context.getResources().getString(R.string.ApiBack);


        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String id_gabeta = jsonObject2.optString("id_gabeta", "");
            String nombre_gaveta = jsonObject2.optString("nombre_gaveta", "");
            String nombre_mecanico = jsonObject2.optString("nombre_mecanico", "");
            String cajones = jsonObject2.optString("cajones", "");
            String idusuario = jsonObject2.optString("idusuario", "");
            String foto_mecanico = jsonObject2.optString("foto_mecanico", "");
            String descripcion = jsonObject2.optString("descripcion", "");

            String fecha_ultimo_inv = jsonObject2.optString("fecha_ultimo_inv", "");
            int frecuencia_dias = jsonObject2.optInt("frecuencia_dias", 0);


            if (fecha_ultimo_inv.isEmpty() || fecha_ultimo_inv.equalsIgnoreCase("null") || fecha_ultimo_inv.equalsIgnoreCase("")) {
                holder.textView43.setText("No se ha realizado ninguna revisión de inventario");
                holder.progressBar.setProgress(100);
                holder.progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(context, R.color.rojo), PorterDuff.Mode.SRC_IN);
            } else {
                Calendar cal = Calendar.getInstance();
                Date fechaActual = cal.getTime();

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date fechaUltimoInvDate = null;
                try {
                    fechaUltimoInvDate = format.parse(fecha_ultimo_inv);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                cal.setTime(fechaUltimoInvDate);
                cal.add(Calendar.DAY_OF_MONTH, frecuencia_dias);
                Date fechaSiguienteInv = cal.getTime();

                long diferenciaMillis = fechaSiguienteInv.getTime() - fechaActual.getTime();
                long diferenciaDias = diferenciaMillis / (1000 * 60 * 60 * 24);

                int porcentajeProgreso = 0;
                if (diferenciaDias <= 0) {
                    porcentajeProgreso = 100;
                } else {
                    porcentajeProgreso = (int) (diferenciaDias * 100 / frecuencia_dias);
                }

                if (diferenciaDias == 0) {
                    holder.textView43.setText("El inventario se debe realizar hoy");
                    holder.progressBar.setProgress(10);
                    holder.progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(context, R.color.rojo), PorterDuff.Mode.SRC_IN);

                } else if (diferenciaDias == 1) {
                    holder.textView43.setText("El inventario se debe realizar mañana");
                    holder.progressBar.setProgress(10);
                    holder.progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(context, R.color.amarillo), PorterDuff.Mode.SRC_IN);

                } else if (diferenciaDias < 0) {
                    holder.textView43.setText("No se ha levantando el inventario para la gaveta");
                    holder.progressBar.setProgress(100);
                    holder.progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(context, R.color.rojo), PorterDuff.Mode.SRC_IN);

                } else {
                    holder.progressBar.setProgress(porcentajeProgreso);
                    holder.textView43.setText("Faltan: " + String.valueOf(diferenciaDias) + " Dias hasta la proxima revisión");
                    holder.progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(context, R.color.verde), PorterDuff.Mode.SRC_IN);

                }


            }


            holder.textDescripcion.setText(descripcion);
            if (nombre_mecanico.equalsIgnoreCase("null") || nombre_mecanico.equalsIgnoreCase("") || nombre_mecanico.isEmpty()) {

                holder.DueñoGaveta.setTextColor(ContextCompat.getColor(context, R.color.rojo));
            } else {

                holder.DueñoGaveta.setTextColor(ContextCompat.getColor(context, R.color.black));
            }


            String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/usuarios/" + foto_mecanico;
            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.usuarios)
                    .error(R.drawable.usuarios)
                    .into(holder.fotoMecanico);


            listaDeCajones.clear();

            try {
                JSONArray listaCajones = new JSONArray(cajones);

                for (int i = 0; i < listaCajones.length(); i++) {
                    JSONObject jsonObject = listaCajones.getJSONObject(i);


                    listaDeCajones.add(jsonObject);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


            holder.reciclerViewCajonesInterno.setLayoutManager(new GridLayoutManager(context, 2));
            adaptadorCajones = new AdaptadorCajones(listaDeCajones, context, id_gabeta, actionListener);
            holder.reciclerViewCajonesInterno.setAdapter(adaptadorCajones);

            adaptadorCajones.notifyDataSetChanged();
            adaptadorCajones.setFilteredData(listaDeCajones);
            adaptadorCajones.filter("");


            Bundle bundle = new Bundle();
            bundle.putString("id_gabeta", id_gabeta);
            bundle.putString("nombre", nombre_gaveta);
            bundle.putString("idusuario", idusuario);


            setTextViewText(holder.tituloGaveta, nombre_gaveta, "No se encontro el nombre de gaveta");
            setTextViewText(holder.DueñoGaveta, nombre_mecanico, "Aun no se asigna un mecanico");
            holder.txtId.setText("id " + id_gabeta);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_opciones_gavetas_nuevo, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogOpcionesGaveta = builder.create();
                    dialogOpcionesGaveta.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogOpcionesGaveta.show();

                    LinearLayout LayoutDescargarPDF = customView.findViewById(R.id.LayoutDescargarPDF);
                    LinearLayout LayoutAsignarMecanico = customView.findViewById(R.id.LayoutAsignarMecanico);
                    LinearLayout LayoutEliminarGaveta = customView.findViewById(R.id.LayoutEliminarGaveta);
                    LinearLayout LayoutLevantarInventario = customView.findViewById(R.id.LayoutLevantarInventario);
                    LinearLayout LayoutConsultarInventarios = customView.findViewById(R.id.LayoutConsultarInventarios);
                    LinearLayout layoutAsignarAlarma = customView.findViewById(R.id.layoutAsignarAlarma);
                    LinearLayout EditarGaveta = customView.findViewById(R.id.EditarGaveta);


                    if (nombre_mecanico.isEmpty() || nombre_mecanico.equals("null") || nombre_mecanico.equals(null)) {
                        LayoutAsignarMecanico.setVisibility(View.VISIBLE);
                    } else {
                        LayoutAsignarMecanico.setVisibility(View.GONE);
                    }


                    EditarGaveta.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_editar_gaveta, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogEditarGaveta = builder.create();
                            dialogEditarGaveta.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogEditarGaveta.show();

                            TextView DueñoGaveta = customView.findViewById(R.id.DueñoGaveta);
                            ImageView fotoEditMecanico = customView.findViewById(R.id.fotoEditMecanico);

                            EditText editTextNombreGaveta = customView.findViewById(R.id.editTextNombreGaveta);
                            EditText editTextDescripcionGaveta = customView.findViewById(R.id.editTextDescripcionGaveta);
                            Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                            Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);

                            DueñoGaveta.setText(nombre_mecanico);
                            editTextNombreGaveta.setText(nombre_gaveta);
                            editTextDescripcionGaveta.setText(descripcion);

                            String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/usuarios/" + foto_mecanico;
                            Glide.with(context)
                                    .load(imageUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .placeholder(R.drawable.usuarios)
                                    .error(R.drawable.usuarios)
                                    .into(fotoEditMecanico);

                            buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String nuevoNombre = editTextNombreGaveta.getText().toString();
                                    String descripGaveta = editTextDescripcionGaveta.getText().toString();

                                    if (nuevoNombre.isEmpty() || descripGaveta.isEmpty()) {
                                        Utiles.crearToastPersonalizado(context, "Debes llenar todos los campos");
                                    } else {
                                        dialogEditarGaveta.dismiss();
                                        dialogOpcionesGaveta.dismiss();
                                        actionListenerGaveta.EditarGaveta(id_gabeta, descripGaveta, nuevoNombre);
                                    }


                                }
                            });


                            buttonCancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogEditarGaveta.dismiss();
                                }
                            });


                        }
                    });


                    layoutAsignarAlarma.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_asignar_alarma, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogAsignarAlarma = builder.create();
                            dialogAsignarAlarma.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogAsignarAlarma.show();


                            EditText editTextFrecuencia = customView.findViewById(R.id.editTextFrecuencia);
                            Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                            Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);


                            buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String frecuenciaDias = editTextFrecuencia.getText().toString();

                                    if (frecuenciaDias.isEmpty() || frecuenciaDias.equalsIgnoreCase("0")) {
                                        Utiles.crearToastPersonalizado(context, "Debes ingresar una frecuencia de revisión valida");
                                    } else {

                                        dialogAsignarAlarma.dismiss();

                                        actionListenerGaveta.AsignarAlarma(id_gabeta, frecuenciaDias);

                                    }


                                }
                            });

                            buttonCancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogAsignarAlarma.dismiss();
                                }
                            });


                        }
                    });


                    LayoutConsultarInventarios.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            dialogOpcionesGaveta.dismiss();
                            Utiles.RedirigirAFragment(fragmentManager, new ConsultaDeInventariosFragment(), bundle);


                        }
                    });


                    LayoutLevantarInventario.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogConfirmacion = builder.create();
                            dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogConfirmacion.show();

                            TextView textView4 = customView.findViewById(R.id.textView4);
                            Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                            Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);

                            textView4.setText("¿Deseas levantar el inventario?");


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
                                    dialogOpcionesGaveta.dismiss();

                                    actionListener.onLevantarInventario(id_gabeta, idusuario, IDSesionIniciada);

                                }
                            });
                        }
                    });


                    LayoutDescargarPDF.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_opciones_pdf_gavetas, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogConfirmacion = builder.create();
                            dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogConfirmacion.show();

                            LinearLayout LayoutPDFContenidoGaveta = customView.findViewById(R.id.LayoutPDFContenidoGaveta);
                            LinearLayout LayoutPDFInventarios = customView.findViewById(R.id.LayoutPDFInventarios);

                            LayoutPDFContenidoGaveta.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Map<String, String> postData = new HashMap<>();
                                    postData.put("opcion", "153");
                                    postData.put("id_gabeta", id_gabeta);

                                    new NuevoDownloadFileTask(context, postData).execute(url);
                                }
                            });

                            LayoutPDFInventarios.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Map<String, String> postData = new HashMap<>();
                                    postData.put("opcion", "154");
                                    postData.put("id_gabeta", id_gabeta);

                                    new NuevoDownloadFileTask(context, postData).execute(url);
                                }
                            });

                            /*
                            Map<String, String> postData = new HashMap<>();
                            postData.put("opcion", "50");

                            new DownloadFileTask(context, postData).execute(url);

                            */

                        }
                    });


                    LayoutEliminarGaveta.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogConfirmacion = builder.create();
                            dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogConfirmacion.show();

                            TextView textView4 = customView.findViewById(R.id.textView4);
                            textView4.setText("¿ Estas seguro que deseas eliminar la gaveta " + nombre_gaveta + " ?");


                            Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                            Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);


                            buttonCancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogConfirmacion.dismiss();
                                }
                            });


                            buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    actionListenerGaveta.onEliminarGaveta(id_gabeta, nombre_gaveta, dialogOpcionesGaveta, dialogConfirmacion);
                                }
                            });


                        }
                    });


                    LayoutAsignarMecanico.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
/*
                            Map<String, String> postData = new HashMap<>();
                            postData.put("opcion", "48");

                            new DownloadFileTask(context, postData).execute(url);
*/


                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_ver_mecanicos, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogMecanicos = builder.create();
                            dialogMecanicos.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogMecanicos.show();

                            RecyclerView RecyclerViewMecanicos = customView.findViewById(R.id.RecyclerViewMecanicos);


                            RecyclerViewMecanicos.setLayoutManager(new LinearLayoutManager(context));
                            adaptadorMecanicosNuevo = new AdaptadorMecanicosNuevo(listaMecanicos, context, id_gabeta, actionListenerMecanicos, dialogMecanicos, dialogOpcionesGaveta);
                            RecyclerViewMecanicos.setAdapter(adaptadorMecanicosNuevo);

                            MostrarMecanicos();


                        }
                    });


                }
            });


        } finally {

        }
    }

    List<JSONObject> listaMecanicos = new ArrayList<>();

    private void MostrarMecanicos() {
        listaMecanicos.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listaMecanicos.add(jsonObject);

                    }
                    adaptadorMecanicosNuevo.notifyDataSetChanged();
                    adaptadorMecanicosNuevo.setFilteredData(listaMecanicos);
                    adaptadorMecanicosNuevo.filter("");


                    if (listaMecanicos.size() > 0) {

                        //      mostrarLayout("conContenido");
                    } else {

                        //           mostrarLayout("SinContenido");
                    }

                } catch (JSONException e) {
                    //       mostrarLayout("SinContenido");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  mostrarLayout("SinInternet");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "48");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }







/*
    private void mostrarLayout(String estado) {


        if (estado.equalsIgnoreCase("conContenido")) {
            reciclerViewCajones.setVisibility(View.VISIBLE);
            lottieNoCajones.setVisibility(View.GONE);
            textViewSinContenido.setVisibility(View.GONE);
            lottieNoInternet.setVisibility(View.GONE);

        } else if (estado.equalsIgnoreCase("SinInternet")) {

            reciclerViewCajones.setVisibility(View.GONE);
            lottieNoCajones.setVisibility(View.GONE);
            textViewSinContenido.setVisibility(View.VISIBLE);
            lottieNoInternet.setVisibility(View.VISIBLE);
            textViewSinContenido.setText("No hay conexión a internet");


        } else {

            lottieNoInternet.setVisibility(View.GONE);
            reciclerViewCajones.setVisibility(View.GONE);
            lottieNoCajones.setVisibility(View.VISIBLE);
            textViewSinContenido.setVisibility(View.VISIBLE);
            textViewSinContenido.setText("No hay cajones en esta gaveta");

        }

    }
*/

    @Override
    public int getItemCount() {

        //return filteredData.size();
        return filteredData.size();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tituloGaveta;
        TextView DueñoGaveta;
        ConstraintLayout LayoutGaveta;
        TextView txtId;
        RecyclerView reciclerViewCajonesInterno;

        ImageView fotoMecanico;

        TextView textDescripcion;
        ProgressBar progressBar;

        TextView textView43;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            DueñoGaveta = itemView.findViewById(R.id.DueñoGaveta);
            tituloGaveta = itemView.findViewById(R.id.tituloGaveta);
            reciclerViewCajonesInterno = itemView.findViewById(R.id.reciclerViewCajones);
            LayoutGaveta = itemView.findViewById(R.id.LayoutGaveta);
            txtId = itemView.findViewById(R.id.txtId);
            fotoMecanico = itemView.findViewById(R.id.fotoMecanico);
            textDescripcion = itemView.findViewById(R.id.textDescripcion);
            progressBar = itemView.findViewById(R.id.progressBar);
            textView43 = itemView.findViewById(R.id.textView43);
        }
    }

    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(data);
        } else {
            String[] keywords = query.toLowerCase().split(" ");

            for (JSONObject item : data) {
                String nombre_gaveta = item.optString("nombre_gaveta", "").toLowerCase();
                String nombre_mecanico = item.optString("nombre_mecanico", "").toLowerCase();
                String id_gabeta = item.optString("id_gabeta", "").toLowerCase();


                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(nombre_gaveta.contains(keyword) || nombre_mecanico.contains(keyword) || id_gabeta.contains(keyword))) {
                        matchesAllKeywords = false;
                        break;
                    }
                }

                if (matchesAllKeywords) {
                    filteredData.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }

    public void setFilteredData(List<JSONObject> filteredData) {
        this.filteredData = new ArrayList<>(filteredData);
        notifyDataSetChanged();
    }


    private void setTextViewText(TextView textView, String text, String defaultText) {
        if (text.equals(null) || text.equals("") || text.equals(":null") || text.equals("null") || text.isEmpty()) {
            textView.setText(defaultText);
        } else {
            textView.setText(text);
        }
    }

}
