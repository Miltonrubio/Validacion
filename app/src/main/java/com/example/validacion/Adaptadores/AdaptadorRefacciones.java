package com.example.validacion.Adaptadores;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.validacion.R;
import com.example.validacion.Objetos.Refacciones;

import java.util.List;

public class AdaptadorRefacciones extends RecyclerView.Adapter<AdaptadorRefacciones.ViewHolder> {
    private List<Refacciones> listaRefacciones;

    public AdaptadorRefacciones(List<Refacciones> listaRefacciones) {
        this.listaRefacciones = listaRefacciones;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_refacciones, parent, false);
            return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (listaRefacciones.isEmpty()) {
            // No hay elementos para mostrar, no hagas nada especial aquí
        } else {
            Refacciones refaccion = listaRefacciones.get(position);
            holder.nombreRefaccion.setText(refaccion.getDescripcion());

            holder.precioRefaccion.setText("Precio: " + String.valueOf(refaccion.getPrecio()) + " $");

            String cantidad = refaccion.getCantidad();

            String[] partes = cantidad.split("\\.");
            String parteEntera = partes[0];
            String parteDecimal = partes[1];
            parteDecimal = parteDecimal.replaceAll("0*$", "");
            String cantidadFormateada = parteEntera;
            if (!parteDecimal.isEmpty()) {
                cantidadFormateada += "." + parteDecimal;
            }

            holder.cantidadRefacciones.setText("Cantidad: " + cantidadFormateada);
        }
    }


    @Override
    public int getItemCount() {
        return listaRefacciones.isEmpty() ? 1 : listaRefacciones.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreRefaccion, precioRefaccion, cantidadRefacciones;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreRefaccion = itemView.findViewById(R.id.nombreRefaccion);
            precioRefaccion = itemView.findViewById(R.id.precioRefaccion);
            cantidadRefacciones = itemView.findViewById(R.id.cantidadRefacciones);

        }
    }


    public void actualizarLista(List<Refacciones> nuevaLista) {
        listaRefacciones.clear();
        listaRefacciones.addAll(nuevaLista);
        notifyDataSetChanged();
    }
}
