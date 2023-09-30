package com.example.validacion;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdaptadorActividadesUnidad extends RecyclerView.Adapter<AdaptadorActividadesUnidad.ViewHolder>{

    private List<ActividadadesUnidad> listaActividades;

    public AdaptadorActividadesUnidad(List<ActividadadesUnidad> listaActividades) {
        this.listaActividades = listaActividades;
    }

    @NonNull
    @Override
    public AdaptadorActividadesUnidad.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividades, parent, false);
        return new AdaptadorActividadesUnidad.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActividadadesUnidad actividadadesUnidad = listaActividades.get(position);

        String observaciones= actividadadesUnidad.getObservaciones();
        String estatus= actividadadesUnidad.getEstatus();


        holder.nombreActividad.setText(observaciones);
        holder.fechaInicio.setText(actividadadesUnidad.getFecha());
        holder.estadoActividad.setText(estatus);


    }


    @Override
    public int getItemCount() {
        return listaActividades.size();
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


    public void actualizarLista(List<ActividadadesUnidad> nuevaLista) {
        listaActividades.clear();
        listaActividades.addAll(nuevaLista);
        notifyDataSetChanged();
    }
}

