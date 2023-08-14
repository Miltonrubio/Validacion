package com.example.validacion;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;

/*
public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder> {

    private ArrayList<Coches> dataset;

    private Context context;

    public Adaptador(Context context) {
        this.context = context;
        dataset = new ArrayList<>();
    }


    @NonNull
    @Override
    public Adaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coches, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptador.ViewHolder holder, int position) {

        Coches p = dataset.get(position);

        holder.Marca.setText(p.getMarca().toString());

        holder.Modelo.setText(p.getModelo().toString());


        String urlOrginal = p.getFoto_unidad();
        String urlFormateada;

        String foto = p.getFoto_unidad();
        if (foto.contains("foto_default.jpg")) {

            urlFormateada = "AbHidalgo/9c205e0cc4bceb0100b32e1ae2995c93.jpg";
        } else {
            urlFormateada = urlOrginal.replace("-", "/");
        }

        String imageUrl = "http://tallergeorgio.hopto.org:5613/verificaciones/imagenes/unidades/" + urlFormateada;
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(context)
                    .load(imageUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.Imagen);
        }

      /*  Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + p.getNumber() + ".png")
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.Imagen);
*/

    /*
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView Imagen;
        private TextView Marca, Modelo;


        public ViewHolder(View itemView) {
            super(itemView);
            Imagen = itemView.findViewById(R.id.imageViewCoches);
            Marca = itemView.findViewById(R.id.textMarca);

            Modelo = itemView.findViewById(R.id.textModelo);

        }
    }


    public void adicionarListaCoches(ArrayList<Coches> listaCoches) {
        dataset.addAll(listaCoches);
        notifyDataSetChanged();
    }
}
*/