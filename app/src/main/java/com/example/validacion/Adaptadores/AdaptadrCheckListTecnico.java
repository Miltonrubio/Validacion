package com.example.validacion.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.validacion.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdaptadrCheckListTecnico extends RecyclerView.Adapter<AdaptadrCheckListTecnico.ViewHolder> {
    private Context context;
    private List<JSONObject> producciones;
    private List<JSONObject> filteredData;
    private AdaptadrCheckListTecnico.OnActivityActionListener actionListener;

    int contador = 0;


    public int obtenerSuma() {
        contador = 0;
        for (JSONObject jsonObject : filteredData) {
            String valorCheck = jsonObject.optString("valorcheck", "");

            if (TextUtils.isEmpty(valorCheck) || valorCheck.equalsIgnoreCase("PENDIENTE")) {
                contador++;
            }
        }
        return contador;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_checklist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject jsonDatos = filteredData.get(position);
            String idcheck = jsonDatos.optString("idcheck", "");
            String iditem = jsonDatos.optString("iditem", "");
            String descripcion = jsonDatos.optString("descripcion", "");
            String categoria = jsonDatos.optString("categoria", "");
            String valorcheck = jsonDatos.optString("valorcheck", "");
            String estado = jsonDatos.optString("estado", "");
            String id_ser_venta = jsonDatos.optString("id_ser_venta", "");


            holder.descripcionCheck.setText(descripcion);

/*
            if (estado.equalsIgnoreCase("pendiente")) {
                contadorPendientes++;
            }
*/

            holder.textPendiente.setVisibility(View.INVISIBLE);
            if (valorcheck.equalsIgnoreCase("R")) {
                holder.regularRadioButton.setChecked(true);
                holder.naRadioButton.setChecked(false);
                holder.maloRadioButton.setChecked(false);
                holder.buenoRadioButton.setChecked(false);
            } else if (valorcheck.equalsIgnoreCase("B")) {
                holder.buenoRadioButton.setChecked(true);
                holder.regularRadioButton.setChecked(false);
                holder.naRadioButton.setChecked(false);
                holder.maloRadioButton.setChecked(false);
            } else if (valorcheck.equalsIgnoreCase("M")) {
                holder.maloRadioButton.setChecked(true);
                holder.regularRadioButton.setChecked(false);
                holder.naRadioButton.setChecked(false);
                holder.buenoRadioButton.setChecked(false);
            } else if (valorcheck.equalsIgnoreCase("NA")) {
                holder.naRadioButton.setChecked(true);
                holder.regularRadioButton.setChecked(false);
                holder.maloRadioButton.setChecked(false);
                holder.buenoRadioButton.setChecked(false);
            } else {
                holder.regularRadioButton.setChecked(false);
                holder.naRadioButton.setChecked(false);
                holder.maloRadioButton.setChecked(false);
                holder.buenoRadioButton.setChecked(false);
                holder.textPendiente.setVisibility(View.VISIBLE);
            }


            holder.regularRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.regularRadioButton.setChecked(true);
                    holder.buenoRadioButton.setChecked(false);
                    holder.maloRadioButton.setChecked(false);
                    holder.naRadioButton.setChecked(false);

                    holder.textPendiente.setVisibility(View.INVISIBLE);
                    actionListener.onActualizarCheck("R", idcheck, descripcion);

                    if (valorcheck.equalsIgnoreCase("PENDIENTE")) {
                        contador--;
                    }
                }
            });

            holder.buenoRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.regularRadioButton.setChecked(false);
                    holder.buenoRadioButton.setChecked(true);
                    holder.maloRadioButton.setChecked(false);
                    holder.naRadioButton.setChecked(false);

                    holder.textPendiente.setVisibility(View.INVISIBLE);
                    actionListener.onActualizarCheck("B", idcheck, descripcion);
                    if (valorcheck.equalsIgnoreCase("PENDIENTE")) {
                        contador--;
                    }
                }
            });

            holder.maloRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.regularRadioButton.setChecked(false);
                    holder.buenoRadioButton.setChecked(false);
                    holder.maloRadioButton.setChecked(true);
                    holder.naRadioButton.setChecked(false);

                    holder.textPendiente.setVisibility(View.INVISIBLE);
                    actionListener.onActualizarCheck("M", idcheck, descripcion);
                    if (valorcheck.equalsIgnoreCase("PENDIENTE")) {
                        contador--;
                    }
                }
            });

            holder.naRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.regularRadioButton.setChecked(false);
                    holder.buenoRadioButton.setChecked(false);
                    holder.maloRadioButton.setChecked(false);
                    holder.naRadioButton.setChecked(true);

                    holder.textPendiente.setVisibility(View.INVISIBLE);
                    actionListener.onActualizarCheck("NA", idcheck, descripcion);
                    if (valorcheck.equalsIgnoreCase("PENDIENTE")) {
                        contador--;
                    }
                }
            });

        } finally {
        }


    }


    @Override
    public int getItemCount() {
        return producciones.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView descripcionCheck, textPendiente;
        RadioButton regularRadioButton, buenoRadioButton, maloRadioButton, naRadioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descripcionCheck = itemView.findViewById(R.id.descripcionCheck);
            regularRadioButton = itemView.findViewById(R.id.regularRadioButton);
            buenoRadioButton = itemView.findViewById(R.id.buenoRadioButton);
            maloRadioButton = itemView.findViewById(R.id.maloRadioButton);
            naRadioButton = itemView.findViewById(R.id.naRadioButton);
            textPendiente = itemView.findViewById(R.id.textPendiente);

        }
    }

    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(producciones);
        } else {
            String[] keywords = query.toLowerCase().split(" ");

            for (JSONObject item : producciones) {

                String titulo = item.optString("titulo", "").toLowerCase();
                String subtitulo = item.optString("subtitulo", "").toLowerCase();
                String precio = item.optString("precio", "").toLowerCase();

                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(titulo.contains(keyword) || subtitulo.contains(keyword) || precio.contains(keyword))) {
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

    public interface OnActivityActionListener {

        void onChecksPendientes(String vacios);


        void onActualizarCheck(String valorCheck, String idcheck, String descripcion);

    }


    public AdaptadrCheckListTecnico(List<JSONObject> data, Context context, AdaptadrCheckListTecnico.OnActivityActionListener actionListener) {
        this.producciones = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.actionListener = actionListener;
    }
}
