package com.example.validacion.Adaptadores;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.validacion.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorInsumosDeServicio extends RecyclerView.Adapter<AdaptadorInsumosDeServicio.ViewHolder> {

    private AdaptadorInsumosDeServicio.OnItemClickListener onItemClickListener;
    private Context context;
    private List<JSONObject> filteredData;
    private List<JSONObject> data;


    String url;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insumos, parent, false);
        return new ViewHolder(view);

    }

    public void setFilteredData(List<JSONObject> filteredData) {
        this.filteredData = new ArrayList<>(filteredData);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(String idrefaccion, String descripcion, String clave, String observaciones);
    }



    /*
    public void setOnItemClickListener(AdaptadorRefaccionesFerrum.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
*/

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.contenedorRefaccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    String idrefaccion = filteredData.get(position).optString("idrefaccion", "");
                    String descripcion = filteredData.get(position).optString("descripcion", "");
                    String observaciones = filteredData.get(position).optString("observaciones", "");
                    String clave = filteredData.get(position).optString("clave", "");
                    onItemClickListener.onItemClick(idrefaccion, descripcion, clave, observaciones);
                }
            }
        });


        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String clave = jsonObject2.optString("clave", "");
            String cantidad = jsonObject2.optString("cantidad", "");
            String descripcion = jsonObject2.optString("descripcion", "");
            String precio = jsonObject2.optString("precio", "");
            String importe = jsonObject2.optString("importe", "");
            String idrefaccion = jsonObject2.optString("idrefaccion", "");
            String tipo = jsonObject2.optString("tipo", "");
            String descuento = jsonObject2.optString("descuento", "");


            holder.nombreRefaccion.setText(descripcion.toUpperCase());
            holder.precioRefaccion.setText("Precio: " + precio + " $");
            holder.cantidadRefacciones.setText("Cantidad: " + cantidad);

            holder.importeTotal.setVisibility(View.VISIBLE);

            if (descuento.equalsIgnoreCase("0.00")) {

                holder.descuento.setVisibility(View.GONE);
            } else {
                holder.descuento.setVisibility(View.VISIBLE);

            }

            holder.descuento.setText("Descuento: -" + descuento + " $");

            holder.importeTotal.setText("Importe total: " + importe + " $");

        } finally {
        }

    }


    @Override
    public int getItemCount() {

        return filteredData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView precioRefaccion;
        TextView nombreRefaccion;
        TextView cantidadRefacciones;
        ConstraintLayout contenedorRefaccion;
        TextView importeTotal;
        TextView descuento;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreRefaccion = itemView.findViewById(R.id.nombreRefaccion);
            precioRefaccion = itemView.findViewById(R.id.precioRefaccion);
            cantidadRefacciones = itemView.findViewById(R.id.cantidadRefacciones);
            contenedorRefaccion = itemView.findViewById(R.id.contenedorRefaccion);
            importeTotal = itemView.findViewById(R.id.importeTotal);
            descuento = itemView.findViewById(R.id.descuento);
        }
    }


    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(data);
        } else {
            String[] keywords = query.toLowerCase().split(" ");

            for (JSONObject item : data) {

                String DOCID = item.optString("DOCID", "");
                String NUMERO = item.optString("NUMERO", "");
                String TOTALPAGADO = item.optString("TOTALPAGADO", "");
                String NOTA = item.optString("NOTA", "");
                String FECHA = item.optString("FECHA", "");

                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(NUMERO.contains(keyword) || DOCID.contains(keyword)
                            || TOTALPAGADO.contains(keyword) || NOTA.contains(keyword) || FECHA.contains(keyword)
                    )) {
                        matchesAllKeywords = false;
                        break;
                    }
                }

                if (matchesAllKeywords) {
                    filteredData.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }


    public AdaptadorInsumosDeServicio(List<JSONObject> data, Context context) {

        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        url = context.getResources().getString(R.string.ApiBack);

    }

}

