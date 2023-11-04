package com.example.validacion.Adaptadores;


import static android.app.PendingIntent.getActivity;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.validacion.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AdaptadorUnidadesClientes extends RecyclerView.Adapter<AdaptadorUnidadesClientes.ViewHolder> {
    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    Context context;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unidades_cliente, parent, false);
        return new ViewHolder(view);

    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            JSONObject jsonObject = filteredData.get(position);
            String Modelo = jsonObject.optString("Modelo", "");
            String Marca = jsonObject.optString("Marca", "");

            holder.NombreUnidad.setText(Marca + " " + Modelo);

            holder.NombreUnidad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.opciones_agregar_servicio, null);
                    TextView vehiculoAIngesar = customView.findViewById(R.id.vehiculoAIngesar);
                    vehiculoAIngesar.setText(Marca + " " + Modelo);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogConBotones = builder.create();
                    dialogConBotones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogConBotones.show();

                }
            });


        } finally {
        }
    }

    @Override
    public int getItemCount() {

        return filteredData.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView NombreUnidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            NombreUnidad = itemView.findViewById(R.id.NombreUnidad);
        }
    }

    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(data);
        } else {

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


    public AdaptadorUnidadesClientes(List<JSONObject> data, Context context) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
    }


}

