package com.example.validacion;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Adapt extends RecyclerView.Adapter<Adapt.ViewHolder>{
    private List<JSONObject> data;
    private Context context;

    public Adapt(List<JSONObject> data, Context context) {
        this.data = data;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coches, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject jsonObject = data.get(position);

            String marca = jsonObject.optString("marcaI", "");
            if (!marca.equals("null")) {
                holder.textMarca.setText(marca);
            } else {
                holder.textMarca.setText("Marca no disponible");
            }

            String modelo = jsonObject.optString("modeloI", "");
            if (!modelo.equals("null")) {
                holder.textModelo.setText(modelo);
            } else {
                holder.textModelo.setText("Modelo no disponible");
            }

            String urlOriginal = jsonObject.getString("foto");
            String urlFormateada = "AbHidalgo/91c9318a8e649f0a357ed81bb0b867bc.jpg";

            if (!TextUtils.isEmpty(urlOriginal) && !urlOriginal.equals("null")) {
                urlFormateada = urlOriginal.replace("-", "/");
            }

            String imageUrl = "http://tallergeorgio.hopto.org:5613/verificaciones/imagenes/unidades/" + urlFormateada;
            if (!TextUtils.isEmpty(imageUrl)) {
                Glide.with(holder.itemView.getContext())
                        .load(imageUrl)
                        .into(holder.imageViewCoches);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Crear un Bundle para enviar los datos al Fragment de Detalle
                    Bundle bundle = new Bundle();
                    bundle.putString("marca", marca);
                    bundle.putString("modelo", modelo);
                    bundle.putString("motivo", jsonObject.optString("motivoingreso", ""));
                    bundle.putString("fecha", jsonObject.optString("fecha_ingreso", ""));

                    bundle.putString("foto", jsonObject.optString("foto", ""));
                    bundle.putString("hora", jsonObject.optString("hora_ingreso", ""));

                    // Instanciar el Fragment de Detalle y configurar el Bundle
                    DetalleFragment detalleFragment = new DetalleFragment();
                    detalleFragment.setArguments(bundle);

                    // Abrir el Fragment de Detalle
                    FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();

                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layoutCoches, detalleFragment) // Reemplazar el contenido del fragment_container
                            .addToBackStack(null) // Agregar a la pila de retroceso
                            .commit();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textMarca, textModelo;

        ImageView imageViewCoches;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textMarca = itemView.findViewById(R.id.textMarca);
            textModelo = itemView.findViewById(R.id.textModelo);
            imageViewCoches= itemView.findViewById(R.id.imageViewCoches);
        }
    }
}
