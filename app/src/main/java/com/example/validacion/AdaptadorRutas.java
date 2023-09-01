package com.example.validacion;

import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

public class AdaptadorRutas extends RecyclerView.Adapter<AdaptadorRutas.ViewHolder>{

    private OnItemClickListener onItemClickListener;

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


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

        Geocoder geocoder = new Geocoder(holder.itemView.getContext());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitud_destino, longitud_destino, 1);

            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);

                String calle = address.getThoroughfare();
                String numero = address.getSubThoroughfare();
                String colonia = address.getSubLocality();

                StringBuilder direccionBuilder = new StringBuilder();
                if (calle != null) {
                    direccionBuilder.append(calle);
                    if (numero != null) {
                        direccionBuilder.append(" #").append(numero);
                    }
                }
                if (colonia != null) {
                    direccionBuilder.append(", ").append(colonia);
                }

                String direccion = direccionBuilder.toString();

                holder.direccionRuta.setText(direccion);
            } else {
                holder.direccionRuta.setText("No disponible");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return listaRutas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView direccionRuta;

        ImageView imageViewRuta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewRuta = itemView.findViewById(R.id.imageViewRuta);

            direccionRuta = itemView.findViewById(R.id.direccionRuta);
        }
    }


    public void actualizarLista(List<MarkerInfo> nuevaLista) {
        listaRutas.clear();
        listaRutas.addAll(nuevaLista);
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


}

