package com.example.validacion.Adaptadores;


import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;
import static com.example.validacion.Adaptadores.Utiles.crearToastPersonalizado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Objetos.SlideItem;
import com.example.validacion.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class AdaptadorActividades extends RecyclerView.Adapter<AdaptadorActividades.ViewHolder> {

    ViewPager2 viewPager2;

    String nuevoEstado = "";
    private Context context;
    private List<JSONObject> filteredData;
    private List<JSONObject> data;

    //  private boolean actividadIniciada = false;

    List<SlideItem> slideItems = new ArrayList<>();
    private Handler sliderHandler = new Handler();


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividades, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String observaciones = jsonObject2.optString("observaciones", "");
            String idbitacora = jsonObject2.optString("idbitacora", "");
            String estatus = jsonObject2.optString("estatus", "");
            String horainicio = jsonObject2.optString("horainicio", "");


            String idpersonal = jsonObject2.optString("idpersonal");
            String id_servicio = jsonObject2.optString("id_servicio");


/*
            if (estatus.equalsIgnoreCase("Iniciado")) {
                actividadIniciada = true;
            }

 */


            setTextViewText(holder.nombreActividad, observaciones, "Esta actividad NO tiene observaciones");
            setTextViewText(holder.estadoActividad, estatus, "Estatus NO disponible");
            setTextViewText(holder.fechaInicio, horainicio, "Hora NO disponible");


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Que desea hacer con:  " + observaciones + " ?");
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.opciones_acitividad, null);

                    builder.setView(customView);

                    final AlertDialog dialogConBotones = builder.create();

                    dialogConBotones.show();
*/

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.opciones_acitividad, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogConBotones = builder.create();
                    dialogConBotones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogConBotones.show();


                    LinearLayout LayoutIniciar = customView.findViewById(R.id.LayoutIniciar);
                    LinearLayout LayoutPausar = customView.findViewById(R.id.LayoutPausar);
                    LinearLayout LayoutFinalizarActividad = customView.findViewById(R.id.LayoutFinalizarActividad);
                    LinearLayout LayoutCancelar = customView.findViewById(R.id.LayoutCancelar);
                    LinearLayout LayoutReanudarActividades = customView.findViewById(R.id.LayoutReanudarActividades);
                    LinearLayout LayoutSubirEvidencias = customView.findViewById(R.id.LayoutSubirEvidencias);


                    if (estatus.equalsIgnoreCase("Pendiente")) {

                        LayoutIniciar.setVisibility(View.VISIBLE);
                        LayoutPausar.setVisibility(View.GONE);
                        LayoutFinalizarActividad.setVisibility(View.GONE);
                        LayoutCancelar.setVisibility(View.VISIBLE);
                        LayoutReanudarActividades.setVisibility(View.GONE);
                        LayoutSubirEvidencias.setVisibility(View.GONE);


                    } else if (  estatus.equalsIgnoreCase("Activo") ||  estatus.equalsIgnoreCase("") || estatus.equalsIgnoreCase("Activa") || estatus.equalsIgnoreCase("Iniciado")) {


                        LayoutIniciar.setVisibility(View.GONE);
                        LayoutPausar.setVisibility(View.VISIBLE);
                        LayoutFinalizarActividad.setVisibility(View.VISIBLE);
                        LayoutCancelar.setVisibility(View.VISIBLE);
                        LayoutReanudarActividades.setVisibility(View.GONE);
                        LayoutSubirEvidencias.setVisibility(View.VISIBLE);

                    } else if (estatus.equalsIgnoreCase("Pausado") || estatus.equalsIgnoreCase("Pausada")) {

                        LayoutIniciar.setVisibility(View.GONE);
                        LayoutPausar.setVisibility(View.GONE);
                        LayoutFinalizarActividad.setVisibility(View.GONE);
                        LayoutCancelar.setVisibility(View.VISIBLE);
                        LayoutReanudarActividades.setVisibility(View.VISIBLE);
                        LayoutSubirEvidencias.setVisibility(View.GONE);


                    } else if (estatus.equalsIgnoreCase("Cancelada")) {


                        LayoutIniciar.setVisibility(View.GONE);
                        LayoutPausar.setVisibility(View.GONE);
                        LayoutFinalizarActividad.setVisibility(View.GONE);
                        LayoutCancelar.setVisibility(View.VISIBLE);
                        LayoutReanudarActividades.setVisibility(View.GONE);
                        LayoutSubirEvidencias.setVisibility(View.GONE);


                    } else {

                        LayoutIniciar.setVisibility(View.GONE);
                        LayoutPausar.setVisibility(View.GONE);
                        LayoutFinalizarActividad.setVisibility(View.GONE);
                        LayoutCancelar.setVisibility(View.GONE);
                        LayoutReanudarActividades.setVisibility(View.GONE);
                        LayoutSubirEvidencias.setVisibility(View.VISIBLE);

                    }


                    LayoutSubirEvidencias.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_evidencias, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogConfirmacion = builder.create();
                            dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogConfirmacion.show();

                            viewPager2 = customView.findViewById(R.id.ViewPagerImagenes);
                            VerFotosActividad(idbitacora);

                            TextView txtDesc = customView.findViewById(R.id.txtDesc);
                            if (observaciones.isEmpty() || observaciones.equals("null") || observaciones.equals("") || observaciones.equals(null)) {

                                txtDesc.setText("Esta actividad no tiene descripcion");
                            } else {

                                txtDesc.setText(observaciones);
                            }

                            Button btnCancelar = customView.findViewById(R.id.btnCancelar);
                            Button tomarFoto = customView.findViewById(R.id.tomarFoto);

                            if (estatus.equalsIgnoreCase("finalizada")) {

                                btnCancelar.setVisibility(View.VISIBLE);
                                tomarFoto.setVisibility(View.GONE);
                            } else {
                                btnCancelar.setVisibility(View.VISIBLE);
                                tomarFoto.setVisibility(View.VISIBLE);
                            }


                            btnCancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogConfirmacion.dismiss();
                                //    return false;
                                }
                            });

                            tomarFoto.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    dialogConfirmacion.dismiss();
                                    dialogConBotones.dismiss();
                                    actionListener.onMandarEvidencia(idbitacora, nuevoEstado, "Fin", "", "NO");
                                 //   return false;
                                }
                            });

// return false;
                        }
                    });


                    LayoutReanudarActividades.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ValidarDisponibilidad(idpersonal, new DisponibilidadCallback() {
                                @Override
                                public void onDisponibilidadValidada(String disponibilidad) {

                                    String dispon = disponibilidad;
                                    if (dispon.equals("libre")) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                        View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion, null);
                                        builder.setView(ModalRedondeado(view.getContext(), customView));
                                        AlertDialog dialogConfirmacion = builder.create();
                                        dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        dialogConfirmacion.show();


                                        TextView textView4 = customView.findViewById(R.id.textView4);
                                        textView4.setText("¿Estas seguro de que deseas ranudar la actividad " + observaciones + " ?");

                                        Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                                        Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);

                                        buttonCancelar.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogConfirmacion.dismiss();
                                            //    return false;
                                            }
                                        });

                                        buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                dialogConfirmacion.dismiss();
                                                dialogConBotones.dismiss();
                                                actionListener.onActualizarEstadoActividadesActivity(idbitacora, "activo", "");
// return false;
                                            }
                                        });


                                    } else if (dispon.equalsIgnoreCase("No hay internet")) {

                                        crearToastPersonalizado(context, "No se pudo validar la disponibilidad, revisa la conexión");
                                    } else {

                                        crearToastPersonalizado(context, "Ya tienes una actividad activa");
                                    }

                                }
                            });

// return false;
                        }
                    });


                    LayoutPausar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogConfirmacion = builder.create();
                            dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogConfirmacion.show();

                            TextView textView4 = customView.findViewById(R.id.textView4);
                            textView4.setText("¿Estas seguro de pausar la actividad " + observaciones + " ?");
                            Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                            Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);


                            buttonCancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    dialogConfirmacion.dismiss();// return false;
                                }
                            });

                            buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    nuevoEstado = "pausada";
                                    actionListener.onActualizarEstadoActividadesActivity(idbitacora, nuevoEstado, "");
                                    dialogConBotones.dismiss();
                                    dialogConfirmacion.dismiss();
                                 //   return false;
                                }
                            });
                         //   return false;
                        }
                    });


                    LayoutIniciar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            ValidarDisponibilidad(idpersonal, new DisponibilidadCallback() {
                                @Override
                                public void onDisponibilidadValidada(String disponibilidad) {

                                    String dispon = disponibilidad;
                                    if (dispon.equals("libre")) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                        View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion_actividades, null);
                                        builder.setView(ModalRedondeado(view.getContext(), customView));
                                        AlertDialog dialogConfirmacion = builder.create();
                                        dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        dialogConfirmacion.show();

                                        TextView textView4 = customView.findViewById(R.id.textView4);
                                        LinearLayout LayoutCancelar = customView.findViewById(R.id.LayoutCancelar);
                                        LinearLayout LayoutTomarFoto = customView.findViewById(R.id.LayoutTomarFoto);

                                        textView4.setText("Para Iniciar una actividad debes subir tu evidencia");


                                        LayoutCancelar.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogConfirmacion.dismiss();
                                           //     return false;
                                            }
                                        });

                                        LayoutTomarFoto.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                nuevoEstado = "activo";

                                                actionListener.onMandarEvidencia(idbitacora, nuevoEstado, "Inicio", "", "SI");
                                                dialogConfirmacion.dismiss();
                                                dialogConBotones.dismiss();


                                            //    return false;
                                            }
                                        });

                                    } else if (dispon.equalsIgnoreCase("No hay internet")) {

                                        crearToastPersonalizado(context, "Ya tienes una actividad activa");
                                    } else {

                                        crearToastPersonalizado(context, "Ya tienes una actividad activa");
                                    }

                                }
                            });


                           // return false;
                        }
                    });

                    LayoutFinalizarActividad.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            ValidarEvidencias(idbitacora, view, dialogConBotones);
/*
                            if (respuestaImagenes.equals("Sin evidencias")) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion_actividades, null);
                                builder.setView(ModalRedondeado(view.getContext(), customView));
                                AlertDialog dialogConfirmacion = builder.create();
                                dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogConfirmacion.show();

                                TextView textView4 = customView.findViewById(R.id.textView4);
                                LinearLayout LayoutCancelar = customView.findViewById(R.id.LayoutCancelar);
                                LinearLayout LayoutTomarFoto = customView.findViewById(R.id.LayoutTomarFoto);
                                EditText observaciones = customView.findViewById(R.id.observaciones);
                                observaciones.setVisibility(View.VISIBLE);

                                textView4.setText("Para Finalizar una actividad debes subir tu evidencia");


                                LayoutCancelar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialogConfirmacion.dismiss();
                                    }
                                });

                                LayoutTomarFoto.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        String observacionesFinales = observaciones.getText().toString();

                                        if (observacionesFinales.isEmpty()) {
                                            crearToastPersonalizado(context, "Debes ingresar una observación para finalizar la actividad");
                                        } else {

                                            dialogConfirmacion.dismiss();
                                            nuevoEstado = "finalizada";
                                            actionListener.onMandarEvidencia(idbitacora, nuevoEstado, "Fin", observacionesFinales);
                                            dialogConBotones.dismiss();
                                        }
                                    }
                                });
                            } else if (respuestaImagenes.equalsIgnoreCase("Con evidencias")) {
                                Utiles.crearToastPersonalizado(context, "Tienes evidencias");
                            } else {

                                Utiles.crearToastPersonalizado(context, respuestaImagenes+ " No se pudieron cargar las evidencias");
                            }
*/
                        //    return false;
                        }
                    });

                    LayoutCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogConfirmacion = builder.create();
                            dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogConfirmacion.show();

                            TextView textView4 = customView.findViewById(R.id.textView4);
                            textView4.setText("¿Estas seguro de cancelar la actividad " + observaciones + " ?");
                            Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                            Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);
                            EditText Motivo = customView.findViewById(R.id.Motivo);
                            Motivo.setVisibility(View.VISIBLE);

                            buttonCancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    dialogConfirmacion.dismiss();
                                 //   return false;
                                }
                            });

                            buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String motivoCancelacion = Motivo.getText().toString();
                                    if (motivoCancelacion.isEmpty()) {

                                        Utiles.crearToastPersonalizado(context, "Debes ingresar un motivo de cancelacion");

                                    } else {

                                        nuevoEstado = "cancelado";
                                        actionListener.onActualizarEstadoActividadesActivity(idbitacora, nuevoEstado, motivoCancelacion);
                                        dialogConBotones.dismiss();
                                        dialogConfirmacion.dismiss();
                                    }


                               //     return false;
                                }
                            });

                         //   return false;
                        }
                    });

                 //   return false;
                }
            });

        } finally {
        }
    }

    String respuestaImagenes;

    List<JSONObject> listaFotos = new ArrayList<>();

    private void ValidarEvidencias(String id_bitacora, View view, AlertDialog dialogConBotones) {
        listaFotos.clear();

        respuestaImagenes = "";
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject fotoObj = jsonArray.getJSONObject(i);

                        listaFotos.add(fotoObj);

                    }

                    if (listaFotos.size() > 1) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion, null);
                        builder.setView(ModalRedondeado(view.getContext(), customView));
                        AlertDialog dialogConfirmacion = builder.create();
                        dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogConfirmacion.show();


                        TextView textView4 = customView.findViewById(R.id.textView4);
                        Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                        Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);

                        textView4.setText("¿Estas seguro que deseas finalizar esta actividad?");

                        buttonCancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogConfirmacion.dismiss();
                                // return false;
                            }
                        });

                        buttonAceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialogConfirmacion.dismiss();
                                dialogConBotones.dismiss();
                                actionListener.onActualizarEstadoActividadesActivity(id_bitacora, "finalizada", "");
// return false;
                            }
                        });

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion_actividades, null);
                        builder.setView(ModalRedondeado(view.getContext(), customView));
                        AlertDialog dialogConfirmacion = builder.create();
                        dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogConfirmacion.show();

                        TextView textView4 = customView.findViewById(R.id.textView4);
                        LinearLayout LayoutCancelar = customView.findViewById(R.id.LayoutCancelar);
                        LinearLayout LayoutTomarFoto = customView.findViewById(R.id.LayoutTomarFoto);
                        EditText observaciones = customView.findViewById(R.id.observaciones);
                        observaciones.setVisibility(View.VISIBLE);

                        textView4.setText("Para Finalizar una actividad debes subir tu evidencia");


                        LayoutCancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogConfirmacion.dismiss();// return false;
                            }
                        });

                        LayoutTomarFoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String observacionesFinales = observaciones.getText().toString();

                                if (observacionesFinales.isEmpty()) {
                                    crearToastPersonalizado(context, "Debes ingresar una observación para finalizar la actividad");
                                } else {

                                    dialogConfirmacion.dismiss();
                                    nuevoEstado = "finalizada";
                                    actionListener.onMandarEvidencia(id_bitacora, nuevoEstado, "Fin", observacionesFinales, "SI");
                                    dialogConBotones.dismiss();
                                }// return false;
                            }
                        });


                    }

                } catch (JSONException e) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion_actividades, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogConfirmacion = builder.create();
                    dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogConfirmacion.show();

                    TextView textView4 = customView.findViewById(R.id.textView4);
                    LinearLayout LayoutCancelar = customView.findViewById(R.id.LayoutCancelar);
                    LinearLayout LayoutTomarFoto = customView.findViewById(R.id.LayoutTomarFoto);
                    EditText observaciones = customView.findViewById(R.id.observaciones);
                    observaciones.setVisibility(View.VISIBLE);

                    textView4.setText("Para Finalizar una actividad debes subir tu evidencia");


                    LayoutCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogConfirmacion.dismiss();// return false;
                        }
                    });

                    LayoutTomarFoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String observacionesFinales = observaciones.getText().toString();

                            if (observacionesFinales.isEmpty()) {
                                crearToastPersonalizado(context, "Debes ingresar una observación para finalizar la actividad");
                            } else {

                                dialogConfirmacion.dismiss();
                                nuevoEstado = "finalizada";
                                actionListener.onMandarEvidencia(id_bitacora, nuevoEstado, "Fin", observacionesFinales, "SI");
                                dialogConBotones.dismiss();
                            }// return false;
                        }
                    });

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "No se pudo cargar, revisa la conexion ");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "71");
                params.put("id_bitacora", id_bitacora);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);

    }


    private void VerFotosActividad(String id_bitacora) {
        slideItems.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject fotoObj = jsonArray.getJSONObject(i);
                        String nombre = fotoObj.getString("nombre");
                        String id_foto = fotoObj.getString("id_foto");
                        String fotoUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/actividades/";

                        slideItems.add(new SlideItem(fotoUrl + nombre, id_foto));

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
                        viewPager2.setVisibility(View.VISIBLE);
                    } else {
                        viewPager2.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    viewPager2.setVisibility(View.GONE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                viewPager2.setVisibility(View.GONE);
                Utiles.crearToastPersonalizado(context, "Hubo un error al cargar las imagenes");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "71");
                params.put("id_bitacora", id_bitacora);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };


    public interface DisponibilidadCallback {
        void onDisponibilidadValidada(String disponibilidad);
    }


    public void ValidarDisponibilidad(String idpersonal, DisponibilidadCallback callback) {

        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("esta libre")) {
                    callback.onDisponibilidadValidada("libre");
                } else {
                    callback.onDisponibilidadValidada("ocupado");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onDisponibilidadValidada("No hay internet");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "63");
                params.put("idpersonal", idpersonal);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    @Override
    public int getItemCount() {

        return filteredData.size();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreActividad, fechaInicio, estadoActividad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreActividad = itemView.findViewById(R.id.nombreActividad);
            estadoActividad = itemView.findViewById(R.id.estadoActividad);
            fechaInicio = itemView.findViewById(R.id.fechaInicio);
        }
    }


    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(data);
        } else {
            String[] keywords = query.toLowerCase().split(" ");

            for (JSONObject item : data) {
                String ID_actividad = item.optString("idbitacora", "").toLowerCase();
                String nombre_actividad = item.optString("observaciones", "").toLowerCase();
                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(nombre_actividad.contains(keyword) || ID_actividad.contains(keyword))) {
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

    public interface OnActivityActionListener {
        void onActualizarEstadoActividadesActivity(String idbitacora, String estatus, String observacion);


        void onMandarEvidencia(String idbitacora, String estatus, String tipo_evidencia, String observacion, String actualizarEstado);


        //  void onDisponibilidadValidada(String disponibilidad);
    }

    private OnActivityActionListener actionListener;
    String url;

    public AdaptadorActividades(List<JSONObject> data, Context context, OnActivityActionListener actionListener) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.actionListener = actionListener;
        this.url = context.getResources().getString(R.string.ApiBack);

    }


}
