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
}

