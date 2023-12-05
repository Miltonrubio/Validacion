package com.example.validacion.Adaptadores;


import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;
import static com.example.validacion.Adaptadores.Utiles.crearToastPersonalizado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdaptadorActividades extends RecyclerView.Adapter<AdaptadorActividades.ViewHolder> {


    String nuevoEstado = "";
    private Context context;
    private List<JSONObject> filteredData;
    private List<JSONObject> data;

    //  private boolean actividadIniciada = false;


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


                    if (estatus.equalsIgnoreCase("Pendiente")) {

                        LayoutIniciar.setVisibility(View.VISIBLE);
                        LayoutPausar.setVisibility(View.GONE);
                        LayoutFinalizarActividad.setVisibility(View.GONE);
                        LayoutCancelar.setVisibility(View.VISIBLE);
                        LayoutReanudarActividades.setVisibility(View.GONE);


                    } else if (estatus.equalsIgnoreCase("Activo") || estatus.equalsIgnoreCase("Activa") || estatus.equalsIgnoreCase("Iniciado")) {


                        LayoutIniciar.setVisibility(View.GONE);
                        LayoutPausar.setVisibility(View.VISIBLE);
                        LayoutFinalizarActividad.setVisibility(View.VISIBLE);
                        LayoutCancelar.setVisibility(View.VISIBLE);
                        LayoutReanudarActividades.setVisibility(View.GONE);

                    } else if (estatus.equalsIgnoreCase("Pausado") || estatus.equalsIgnoreCase("Pausada")) {

                        LayoutIniciar.setVisibility(View.GONE);
                        LayoutPausar.setVisibility(View.GONE);
                        LayoutFinalizarActividad.setVisibility(View.GONE);
                        LayoutCancelar.setVisibility(View.VISIBLE);
                        LayoutReanudarActividades.setVisibility(View.VISIBLE);

                    } else if (estatus.equalsIgnoreCase("Cancelada")) {


                        LayoutIniciar.setVisibility(View.GONE);
                        LayoutPausar.setVisibility(View.GONE);
                        LayoutFinalizarActividad.setVisibility(View.GONE);
                        LayoutCancelar.setVisibility(View.VISIBLE);
                        LayoutReanudarActividades.setVisibility(View.GONE);

                    } else {
                        LayoutIniciar.setVisibility(View.GONE);
                        LayoutPausar.setVisibility(View.GONE);
                        LayoutFinalizarActividad.setVisibility(View.GONE);
                        LayoutCancelar.setVisibility(View.VISIBLE);
                        LayoutReanudarActividades.setVisibility(View.GONE);
                    }


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
                                        textView4.setText("¿Estas deseas ranudar la actividad " + observaciones + " ?");

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

                                                dialogConfirmacion.dismiss();
                                                dialogConBotones.dismiss();
                                                actionListener.onActualizarEstadoActividadesActivity(idbitacora, "activo", "");

                                            }
                                        });


                                    } else if (dispon.equalsIgnoreCase("No hay internet")) {

                                        crearToastPersonalizado(context, "No se pudo validar la disponibilidad, revisa la conexión");
                                    } else {

                                        crearToastPersonalizado(context, "Ya tienes una actividad activa");
                                    }

                                }
                            });


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

                                    dialogConfirmacion.dismiss();
                                }
                            });

                            buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    nuevoEstado = "pausada";
                                    actionListener.onActualizarEstadoActividadesActivity(idbitacora, nuevoEstado, "");
                                    dialogConBotones.dismiss();
                                    dialogConfirmacion.dismiss();
                                }
                            });
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
                                            }
                                        });

                                        LayoutTomarFoto.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                nuevoEstado = "activo";

                                                actionListener.onMandarEvidencia(idbitacora, nuevoEstado, "Inicio", "");
                                                dialogConfirmacion.dismiss();
                                                dialogConBotones.dismiss();


                                            }
                                        });

                                    } else if (dispon.equalsIgnoreCase("No hay internet")) {

                                        crearToastPersonalizado(context, "Ya tienes una actividad activa");
                                    } else {

                                        crearToastPersonalizado(context, "Ya tienes una actividad activa");
                                    }

                                }
                            });


                        }
                    });

                    LayoutFinalizarActividad.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


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


                                }
                            });

                        }
                    });

                }
            });

        } finally {
        }
    }

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


        void onMandarEvidencia(String idbitacora, String estatus, String tipo_evidencia, String observacion);


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
