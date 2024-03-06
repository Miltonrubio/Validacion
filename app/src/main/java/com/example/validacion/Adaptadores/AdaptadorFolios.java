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

public class AdaptadorFolios extends RecyclerView.Adapter<AdaptadorFolios.ViewHolder> {

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


    public interface OnItemClickListener {
        void onItemClick(String DOCID, String NUMERO);
    }


    private AdaptadorFolios.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AdaptadorFolios.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.frameNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    String DOCID = filteredData.get(position).optString("DOCID", "");
                    String NUMERO = filteredData.get(position).optString("NUMERO", "");
                    onItemClickListener.onItemClick(DOCID, NUMERO);
                }
            }
        });


        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String DOCID = jsonObject2.optString("DOCID", "");
            String NUMERO = jsonObject2.optString("NUMERO", "");
            String TOTALPAGADO = jsonObject2.optString("TOTALPAGADO", "");
            String NOTA = jsonObject2.optString("NOTA", "");
            String FECHA = jsonObject2.optString("FECHA", "");


            holder.textTOTAL.setText("Total pagado: "+TOTALPAGADO +" $");
            holder.textNOTA.setText(NOTA);
            holder.textDOCID.setText("ID nota: " + DOCID);
            holder.textFecha.setText(FECHA);
            holder.textNUMERO.setText("No. " +NUMERO);


        } finally {
        }

    }


    @Override
    public int getItemCount() {

        return filteredData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTOTAL;
        TextView textNUMERO;
        TextView textDOCID;
        TextView textFecha;

        TextView textNOTA;
        FrameLayout frameNotas;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
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
                            || TOTALPAGADO.contains(keyword)  || NOTA.contains(keyword)  || FECHA.contains(keyword)
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


    public AdaptadorFolios(List<JSONObject> data, Context context) {

        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        url = context.getResources().getString(R.string.ApiBack);

    }

}

