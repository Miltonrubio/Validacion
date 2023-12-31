package com.example.validacion.Adaptadores;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.validacion.Objetos.Choferes;
import com.example.validacion.R;

import java.util.List;

public class AdaptadorChoferes extends RecyclerView.Adapter<AdaptadorChoferes.ViewHolder>{

    private List<Choferes> listaChoferes;

    public AdaptadorChoferes(List<Choferes> listaChoferes) {
        this.listaChoferes = listaChoferes;
    }

    @NonNull
    @Override
    public AdaptadorChoferes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choferes, parent, false);
        return new AdaptadorChoferes.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Choferes choferes = listaChoferes.get(position);



        holder.nombreChofer.setText(choferes.getChofer());

        String fotoMecanico= choferes.getFoto_mapa();

        String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/usuarios/"+fotoMecanico;
        if (!TextUtils.isEmpty(fotoMecanico)) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .error(R.drawable.chofer)
                    .into(holder.imageViewChofer);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.chofer)
                    .into(holder.imageViewChofer);
        }

        holder.unidadChofer.setText(choferes.getMarca() + " " +choferes.getModelo());


    }


    @Override
    public int getItemCount() {
        return listaChoferes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView unidadChofer, nombreChofer;

        ImageView imageViewChofer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewChofer = itemView.findViewById(R.id.imageViewChofer);

            nombreChofer = itemView.findViewById(R.id.nombreChofer);
            unidadChofer=  itemView.findViewById(R.id.unidadChofer);
        }
    }


    public void actualizarLista(List<Choferes> nuevaLista) {
        listaChoferes.clear();
        listaChoferes.addAll(nuevaLista);
        notifyDataSetChanged();
    }
}

