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

public class AdaptadorMecanicos extends RecyclerView.Adapter<AdaptadorMecanicos.ViewHolder>{

    private List<Mecanicos> listaMecanicos;

    public AdaptadorMecanicos(List<Mecanicos> listaMecanicos) {
        this.listaMecanicos = listaMecanicos;
    }

    @NonNull
    @Override
    public AdaptadorMecanicos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mecanicos, parent, false);
        return new AdaptadorMecanicos.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mecanicos mecanicos = listaMecanicos.get(position);

        String nombreCompleto= mecanicos.getNombre();
        String[] partes = nombreCompleto.split(" ");
        String nombre = partes[0];
        String primerApellido = partes[1];
        String formatoNombreApellido = nombre + " " + primerApellido;


        holder.nombreMecanico.setText(formatoNombreApellido);

        String fotoMecanico= mecanicos.getFoto();

        String fecha= mecanicos.getFecha_programada();
        if(fecha.equals("null") || fecha.isEmpty()){
            holder.fechaMecanico.setText("AUN SIN FECHA ESTIMADA");

            holder.fechaMecanico.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.rojo));
        }else {
            holder.fechaMecanico.setText(fecha);
        }


        String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/mecanico/" + fotoMecanico;
        if (!TextUtils.isEmpty(fotoMecanico)) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .error(R.drawable.mecanico)
                    .into(holder.imageViewMecanico);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.mecanico)
                    .into(holder.imageViewMecanico);
        }

        holder.reparacionMecanico.setText(mecanicos.getMotivoingreso());


    }


    @Override
    public int getItemCount() {
        return listaMecanicos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView reparacionMecanico, nombreMecanico, fechaMecanico;

        ImageView imageViewMecanico;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMecanico = itemView.findViewById(R.id.imageViewMecanico);
            fechaMecanico=itemView.findViewById(R.id.fechaMecanico);
            nombreMecanico = itemView.findViewById(R.id.nombreMecanico);
            reparacionMecanico=  itemView.findViewById(R.id.reparacionMecanico);
        }
    }


    public void actualizarLista(List<Mecanicos> nuevaLista) {
        listaMecanicos.clear();
        listaMecanicos.addAll(nuevaLista);
        notifyDataSetChanged();
    }
}

