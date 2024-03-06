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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.validacion.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorPagos extends RecyclerView.Adapter<AdaptadorPagos.ViewHolder> {

    private Context context;
    private List<JSONObject> filteredData;
    private List<JSONObject> data;


    String url;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folios, parent, false);
        return new ViewHolder(view);

    }

    public void setFilteredData(List<JSONObject> filteredData) {
        this.filteredData = new ArrayList<>(filteredData);
        notifyDataSetChanged();
    }


    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String DOCID = jsonObject2.optString("DOCID", "");
            String NUMERO = jsonObject2.optString("NUMERO", "");
            String TOTALPAGADO = jsonObject2.optString("TOTALPAGADO", "");
            String ADEUDO = jsonObject2.optString("ADEUDO", "");
            String PGFECHAAPLICADA = jsonObject2.optString("PGFECHAAPLICADA", "");
            String IMPORTECAJA = jsonObject2.optString("IMPORTECAJA", "");


            holder.textTOTAL.setText("Adeudo: " + ADEUDO);
            holder.textNOTA.setText( "Total pagado: " + TOTALPAGADO+ " $");
            holder.textDOCID.setText(DOCID);
            holder.textFecha.setText("\nFecha: "+ PGFECHAAPLICADA);
            holder.textNUMERO.setText("No." + NUMERO);

            holder.imageViewCoches.setVisibility(View.GONE);
        } finally {
        }

    }


    @Override
    public int getItemCount() {

        return filteredData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewCoches;
        TextView textTOTAL;
        TextView textNUMERO;
        TextView textDOCID;
        TextView textFecha;

        TextView textNOTA;
        FrameLayout frameNotas;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCoches = itemView.findViewById(R.id.imageViewCoches);
            textFecha = itemView.findViewById(R.id.textFecha);
            textDOCID = itemView.findViewById(R.id.textDOCID);
            textNUMERO = itemView.findViewById(R.id.textNUMERO);
            textTOTAL = itemView.findViewById(R.id.textTOTAL);
            textNOTA = itemView.findViewById(R.id.textNOTA);
            frameNotas = itemView.findViewById(R.id.frameNotas);
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


    public AdaptadorPagos(List<JSONObject> data, Context context) {

        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        url = context.getResources().getString(R.string.ApiBack);

    }

}

