package com.example.validacion;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdaptadorRutas /* extends RecyclerView.Adapter<AdaptadorRutas.ViewHolder>*/{
/*
    private List<MarkerInfo> listaRutas;

    public AdaptadorRutas(List<MarkerInfo> listaRutas) {
        this.listaRutas = listaRutas;
    }

    @NonNull
    @Override
    public AdaptadorRutas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rutas, parent, false);
        return new AdaptadorRutas.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MarkerInfo markerInfo = listaRutas.get(position);

        Double latitud_destino = markerInfo.getLatitud_destino();
        Double longitud_destino = markerInfo.getLongitud_destino();


        holder.nombreMecanico.setText(formatoNombreApellido);

        String fotoMecanico= mecanicos.getFoto();

        String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/mecanico/0ff922ddee3e92d91b1e95b25a51e61c.jpg";
        if (!TextUtils.isEmpty(fotoMecanico)) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .error(R.drawable.default_image)
                    .into(holder.imageViewMecanico);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.default_image)
                    .into(holder.imageViewMecanico);
        }

        holder.reparacionMecanico.setText(mecanicos.getMotivoingreso());


    }


    @Override
    public int getItemCount() {
        return listaMecanicos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView reparacionMecanico, nombreMecanico;

        ImageView imageViewMecanico;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMecanico = itemView.findViewById(R.id.imageViewMecanico);

            nombreMecanico = itemView.findViewById(R.id.nombreMecanico);
            reparacionMecanico=  itemView.findViewById(R.id.reparacionMecanico);
        }
    }


    public void actualizarLista(List<Mecanicos> nuevaLista) {
        listaMecanicos.clear();
        listaMecanicos.addAll(nuevaLista);
        notifyDataSetChanged();
    }

 */
}

