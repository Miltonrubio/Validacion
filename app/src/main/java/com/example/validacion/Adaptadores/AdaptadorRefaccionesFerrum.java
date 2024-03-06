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
import androidx.recyclerview.widget.RecyclerView;

import com.example.validacion.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorRefaccionesFerrum extends RecyclerView.Adapter<AdaptadorRefaccionesFerrum.ViewHolder> {

    private Context context;
    private List<JSONObject> filteredData;
    private List<JSONObject> data;


    String url;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_refacciones_ferrum, parent, false);
        return new ViewHolder(view);

    }

    public void setFilteredData(List<JSONObject> filteredData) {
        this.filteredData = new ArrayList<>(filteredData);
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        void onItemClick(String precio, String DESCRIPCIO, String existencia, String CLAVE);
    }


    private AdaptadorRefaccionesFerrum.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AdaptadorRefaccionesFerrum.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.contenedorRefaccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    String CLAVE = filteredData.get(position).optString("CLAVE", "");
                    String existencia = filteredData.get(position).optString("existencia", "");
                    String DESCRIPCIO = filteredData.get(position).optString("DESCRIPCIO", "");
                    String precio = filteredData.get(position).optString("precio", "");
                    onItemClickListener.onItemClick(precio, DESCRIPCIO, existencia, CLAVE);
                }
            }
        });


        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String CLAVE = jsonObject2.optString("CLAVE", "");
            String DESCRIPCIO = jsonObject2.optString("DESCRIPCIO", "");
            String ARTICULOID = jsonObject2.optString("ARTICULOID", "");
            String precio = jsonObject2.optString("precio", "");
            String existencia = jsonObject2.optString("existencia", "");


            holder.nombreRefaccion.setText(DESCRIPCIO.toUpperCase());
            holder.precioRefaccion.setText("Precio: " + precio + " $");

            String cantidad = existencia;

            String[] partes = cantidad.split("\\.");
            String parteEntera = partes[0];
            String parteDecimal = partes[1];
            parteDecimal = parteDecimal.replaceAll("0*$", "");
            String cantidadFormateada = parteEntera;
            if (!parteDecimal.isEmpty()) {
                cantidadFormateada += "." + parteDecimal;
            }

            holder.cantidadRefacciones.setText("Cantidad: " + cantidadFormateada);


            holder.ARTICULOID.setText("ID: "+ ARTICULOID);
            holder.TVCLAVE.setText("Clave: "+ CLAVE);






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
        FrameLayout contenedorRefaccion;

        TextView ARTICULOID;

        TextView TVCLAVE;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreRefaccion = itemView.findViewById(R.id.nombreRefaccion);
            precioRefaccion = itemView.findViewById(R.id.precioRefaccion);
            cantidadRefacciones = itemView.findViewById(R.id.cantidadRefacciones);
            contenedorRefaccion = itemView.findViewById(R.id.contenedorRefaccion);
            TVCLAVE = itemView.findViewById(R.id.TVCLAVE);
            ARTICULOID = itemView.findViewById(R.id.ARTICULOID);

        }
    }


    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(data);
        } else {
            String[] keywords = query.toLowerCase().split(" ");

            for (JSONObject item : data) {


                String CLAVE = item.optString("CLAVE", "");
                String DESCRIPCIO = item.optString("DESCRIPCIO", "");
                String ARTICULOID = item.optString("ARTICULOID", "");
                String precio = item.optString("precio", "");
                String existencia = item.optString("existencia", "");

                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(CLAVE.contains(keyword) || DESCRIPCIO.contains(keyword)
                            || ARTICULOID.contains(keyword) || precio.contains(keyword) || existencia.contains(keyword)
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


    public AdaptadorRefaccionesFerrum(List<JSONObject> data, Context context) {

        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        url = context.getResources().getString(R.string.ApiBack);

    }

}

