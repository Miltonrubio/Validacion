package com.example.validacion.Adaptadores;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.validacion.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorActividades extends RecyclerView.Adapter<AdaptadorActividades.ViewHolder> {

    private static final int VIEW_TYPE_ERROR = 0;
    private static final int VIEW_TYPE_ITEM = 1;


    String nuevoEstado = "";
    private Context context;
    private List<JSONObject> filteredData;
    private List<JSONObject> data;


    public AdaptadorActividades(List<JSONObject> data, Context context) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividades, parent, false);
            return new ViewHolder(view);
        } else {

            View errorView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_noactividades, parent, false);
            return new ViewHolder(errorView);
        }
    }
    private boolean actividadIniciada = false;

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            try {
                JSONObject jsonObject2 = filteredData.get(position);
                String observaciones = jsonObject2.optString("observaciones", "");
                String idbitacora = jsonObject2.optString("idbitacora", "");
                String estatus = jsonObject2.optString("estatus", "");
                String horainicio = jsonObject2.optString("horainicio", "");


                if (estatus.equalsIgnoreCase("Iniciado")) {
                    actividadIniciada = true;
                }


                setTextViewText(holder.nombreActividad, observaciones, "Esta actividad NO tiene observaciones");
                setTextViewText(holder.estadoActividad, estatus , "Estatus NO disponible");
                setTextViewText(holder.fechaInicio, horainicio, "Hora NO disponible");


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Que desea hacer con:  " + observaciones + " ?");
                        View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.opciones_acitividad, null);
                        LinearLayout LayoutIniciar = customView.findViewById(R.id.LayoutIniciar);
                        LinearLayout LayoutPausar = customView.findViewById(R.id.LayoutPausar);
                        LinearLayout LayoutFinalizarActividad = customView.findViewById(R.id.LayoutFinalizarActividad);
                        LinearLayout LayoutCancelar= customView.findViewById(R.id.LayoutCancelar);


                        if(estatus.equalsIgnoreCase("Pendiente")){
                            if (actividadIniciada) {
                                LayoutIniciar.setVisibility(View.GONE); // Si hay una actividad iniciada, oculta el botón "Iniciar".
                            } else {
                                LayoutIniciar.setVisibility(View.VISIBLE);
                            }
                        }else if (estatus.equalsIgnoreCase("Activo")|| estatus.equalsIgnoreCase("Activa")|| estatus.equalsIgnoreCase("Iniciado") ){
                            LayoutPausar.setVisibility(View.VISIBLE);
                            LayoutFinalizarActividad.setVisibility(View.VISIBLE);
                        }else if (estatus.equalsIgnoreCase("Pausado") || estatus.equalsIgnoreCase("Pausada")){
                            if (actividadIniciada) {
                                LayoutIniciar.setVisibility(View.GONE); // Si hay una actividad iniciada, oculta el botón "Iniciar".
                            } else {
                                LayoutIniciar.setVisibility(View.VISIBLE);
                            }
                        }else if (estatus.equalsIgnoreCase("Cancelada")){
                            LayoutFinalizarActividad.setVisibility(View.VISIBLE);
                        }else{
                            LayoutCancelar.setVisibility(View.VISIBLE);
                        }

                        builder.setView(customView);

                        final AlertDialog dialogConBotones = builder.create();

                        LayoutPausar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                AlertDialog.Builder aceptarBuilder = new AlertDialog.Builder(view.getContext());
                                aceptarBuilder.setTitle("Deseas pausar la actividad: " + observaciones);
                                aceptarBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        nuevoEstado = "pausada";
                                        actionListener.onActualizarEstadoActividadesActivity(idbitacora, nuevoEstado);
                                        dialog.dismiss();
                                        dialogConBotones.dismiss();
                                    }
                                });

                                aceptarBuilder.setNegativeButton("Cancelar", null);
                                aceptarBuilder.show();
                            }
                        });

                        LayoutIniciar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                AlertDialog.Builder aceptarBuilder = new AlertDialog.Builder(view.getContext());
                                aceptarBuilder.setTitle("Deseas iniciar la actividad: " + observaciones);
                                aceptarBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        nuevoEstado = "activo";
                                        actionListener.onActualizarEstadoActividadesActivity(idbitacora, nuevoEstado);
                                        dialog.dismiss();
                                        dialogConBotones.dismiss();
                                    }
                                });

                                aceptarBuilder.setNegativeButton("Cancelar", null);
                                aceptarBuilder.show();
                            }
                        });


                        LayoutFinalizarActividad.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                AlertDialog.Builder aceptarBuilder = new AlertDialog.Builder(view.getContext());
                                aceptarBuilder.setTitle("Deseas finalizar la actividad: " + observaciones);
                                aceptarBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        nuevoEstado = "finalizada";
                                        actionListener.onActualizarEstadoActividadesActivity(idbitacora, nuevoEstado);
                                        dialog.dismiss();
                                        dialogConBotones.dismiss();
                                    }
                                });

                                aceptarBuilder.setNegativeButton("Cancelar", null);
                                aceptarBuilder.show();
                            }
                        });

                        LayoutCancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                AlertDialog.Builder aceptarBuilder = new AlertDialog.Builder(view.getContext());
                                aceptarBuilder.setTitle("Deseas cancelar la actividad: " + observaciones);
                                aceptarBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        nuevoEstado = "cancelado";
                                        actionListener.onActualizarEstadoActividadesActivity(idbitacora, nuevoEstado);
                                        dialog.dismiss();
                                        dialogConBotones.dismiss();
                                    }
                                });

                                aceptarBuilder.setNegativeButton("Cancelar", null);
                                aceptarBuilder.show();
                            }
                        });

                        dialogConBotones.show();
                    }
                });

            } finally {
            }
        }
    }

    @Override
    public int getItemCount() {

        return filteredData.isEmpty() ? 1 : filteredData.size();

    }

    @Override
    public int getItemViewType(int position) {
        return filteredData.isEmpty() ? VIEW_TYPE_ERROR : VIEW_TYPE_ITEM;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreActividad, fechaInicio, estadoActividad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreActividad = itemView.findViewById(R.id.nombreActividad);
            estadoActividad= itemView.findViewById(R.id.estadoActividad);
                    fechaInicio= itemView.findViewById(R.id.fechaInicio);
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
        void onActualizarEstadoActividadesActivity(String idbitacora, String estatus);
    }

    private OnActivityActionListener actionListener;

    public AdaptadorActividades(List<JSONObject> data, Context context, OnActivityActionListener actionListener) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.actionListener = actionListener;
    }


}
