package com.example.validacion.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.validacion.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorNuevosChecks extends RecyclerView.Adapter<AdaptadorNuevosChecks.ViewHolder> {

    private Context context;
    private List<JSONObject> filteredData;
    private List<JSONObject> data;


    String url;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nuevochecklist, parent, false);
        return new ViewHolder(view);

    }

    public void setFilteredData(List<JSONObject> filteredData) {
        this.filteredData = new ArrayList<>(filteredData);
        notifyDataSetChanged();
    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        int drawableResId = 0;
        int colorIcono = 0;

        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String ID_valor_check = jsonObject2.optString("ID_valor_check", "");
            String valor_check = jsonObject2.optString("valor_check", "");
            String nombre_check = jsonObject2.optString("nombre_check", "");
            String status_revision= jsonObject2.optString("status_revision","");


            if (status_revision.equalsIgnoreCase("Finalizado")){
                holder.maloRadioButton.setEnabled(false);
                holder.buenoRadioButton.setEnabled(false);
                holder.regularRadioButton.setEnabled(false);
            }else {

                holder.maloRadioButton.setEnabled(true);
                holder.buenoRadioButton.setEnabled(true);
                holder.regularRadioButton.setEnabled(true);
            }

            holder.descripcionCheck.setText(nombre_check);

            if (valor_check.equalsIgnoreCase("Bueno")) {

                holder.maloRadioButton.setChecked(false);
                holder.buenoRadioButton.setChecked(true);
                holder.regularRadioButton.setChecked(false);
                holder.textPendiente.setVisibility(View.GONE);
         //       holder.contenedorChecks.setBackgroundResource(R.drawable.contorno_verdesito);

                holder.textPendiente.setBackgroundResource(0);
            } else if (valor_check.equalsIgnoreCase("Malo")) {
                holder.maloRadioButton.setChecked(true);
                holder.buenoRadioButton.setChecked(false);
                holder.regularRadioButton.setChecked(false);
                holder.textPendiente.setVisibility(View.GONE);

                holder.textPendiente.setBackgroundResource(0);
         //       holder.contenedorChecks.setBackgroundResource(R.drawable.contorno_verdesito);

            } else if (valor_check.equalsIgnoreCase("Regular")) {

                holder.maloRadioButton.setChecked(false);
                holder.buenoRadioButton.setChecked(false);
                holder.regularRadioButton.setChecked(true);
                holder.textPendiente.setVisibility(View.GONE);

                holder.textPendiente.setBackgroundResource(0);
         //       holder.contenedorChecks.setBackgroundResource(R.drawable.contorno_verdesito);

            } else {


                holder.textPendiente.setBackgroundResource(R.drawable.textview_outline3);
          //      holder.contenedorChecks.setBackgroundResource(R.drawable.contorno_amarillo);

                holder.maloRadioButton.setChecked(false);
                holder.buenoRadioButton.setChecked(false);
                holder.textPendiente.setVisibility(View.VISIBLE);
                holder.regularRadioButton.setChecked(false);
            }

            holder.buenoRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.maloRadioButton.setChecked(false);
                    holder.buenoRadioButton.setChecked(true);
                    holder.regularRadioButton.setChecked(false);
                    actionListener.onActualizarCheck(ID_valor_check, "Bueno");

                }
            });

            holder.maloRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.maloRadioButton.setChecked(true);
                    holder.buenoRadioButton.setChecked(false);
                    holder.regularRadioButton.setChecked(false);

                    actionListener.onActualizarCheck(ID_valor_check, "Malo");

                }
            });



            holder.regularRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.maloRadioButton.setChecked(false);
                    holder.buenoRadioButton.setChecked(false);
                    holder.regularRadioButton.setChecked(true);

                    actionListener.onActualizarCheck(ID_valor_check, "Regular");

                }
            });



        } finally {
        }

    }


    @Override
    public int getItemCount() {

        return filteredData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView descripcionCheck;
        RadioButton regularRadioButton;
        RadioButton maloRadioButton;
        RadioButton buenoRadioButton;
        TextView textPendiente;
        FrameLayout contenedorChecks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textPendiente = itemView.findViewById(R.id.textPendiente);
            descripcionCheck = itemView.findViewById(R.id.descripcionCheck);
            regularRadioButton = itemView.findViewById(R.id.regularRadioButton);
            maloRadioButton = itemView.findViewById(R.id.maloRadioButton);
            buenoRadioButton = itemView.findViewById(R.id.buenoRadioButton);
            contenedorChecks = itemView.findViewById(R.id.contenedorChecks);



        }
    }


    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(data);
        } else {
            String[] keywords = query.toLowerCase().split(" ");

            for (JSONObject item : data) {
                String saldo_inicial = item.optString("saldo_inicial", "").toLowerCase();
                String ID_saldo = item.optString("ID_saldo", "").toLowerCase();
                String nuevo_saldo = item.optString("nuevo_saldo", "").toLowerCase();


                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(saldo_inicial.contains(keyword) || ID_saldo.contains(keyword)
                            || nuevo_saldo.contains(keyword)
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


    public interface OnActivityActionListener {
        void onActualizarCheck(String ID_valor_check, String valorCheck);

    }

    private AdaptadorNuevosChecks.OnActivityActionListener actionListener;


    public AdaptadorNuevosChecks(List<JSONObject> data, Context context, AdaptadorNuevosChecks.OnActivityActionListener actionListener) {

        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        url = context.getResources().getString(R.string.ApiBack);
        this.actionListener = actionListener;

    }

}

