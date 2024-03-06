package com.example.validacion.Adaptadores;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.validacion.DetallesArrastres;
import com.example.validacion.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdaptadorCheckActividades extends RecyclerView.Adapter<AdaptadorCheckActividades.ViewHolder> {


    private Context context;

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    String url;

    String ID_registro;
    String estado;


    public interface OnActivityActionListener {
        void onActualizarCheck(String ID_check_actividad, String valor_check);
    }

    private AdaptadorCheckActividades.OnActivityActionListener actionListener;


    public AdaptadorCheckActividades(List<JSONObject> data, Context context, String ID_registro, String estado, AdaptadorCheckActividades.OnActivityActionListener actionListener) {
        this.data = data;
        this.estado = estado;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.ID_registro = ID_registro;
        this.actionListener = actionListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_actividades, parent, false);
        return new ViewHolder(view);

    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        url = context.getResources().getString(R.string.ApiBack);


        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String nombre_actividad = jsonObject2.optString("nombre_actividad", "");
            String ID_check_actividad = jsonObject2.optString("ID_check_actividad", "");
            String valor_check = jsonObject2.optString("valor_check", "");


            Bundle bundle = new Bundle();
            bundle.putString("nombre_actividad", nombre_actividad);


            holder.nombreActividad.setText(nombre_actividad);


            if (estado.equalsIgnoreCase("iniciado")) {
                holder.radioButtonNo.setEnabled(true);
                holder.radioButtonSi.setEnabled(true);

            } else {

                holder.radioButtonNo.setEnabled(false);
                holder.radioButtonSi.setEnabled(false);
            }


            if (valor_check.equalsIgnoreCase("Si")) {

                holder.radioButtonNo.setChecked(false);
                holder.radioButtonSi.setChecked(true);
            } else if (valor_check.equalsIgnoreCase("")) {

                holder.radioButtonSi.setChecked(false);
                holder.radioButtonNo.setChecked(false);
            } else {
                holder.radioButtonNo.setChecked(true);
                holder.radioButtonSi.setChecked(false);
            }


            holder.radioButtonNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.radioButtonNo.setChecked(true);
                    holder.radioButtonSi.setChecked(false);
                    actionListener.onActualizarCheck(ID_check_actividad, "No");

                }
            });


            holder.radioButtonSi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.radioButtonSi.setChecked(true);
                    holder.radioButtonNo.setChecked(false);

                   actionListener.onActualizarCheck(ID_check_actividad, "Si");
                }
            });


        } finally {

        }
    }


    @Override
    public int getItemCount() {

        //return filteredData.size();
        return filteredData.size();

    }





    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreActividad;
        RadioButton radioButtonSi;

        RadioButton radioButtonNo;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreActividad = itemView.findViewById(R.id.nombreActividad);
            radioButtonSi = itemView.findViewById(R.id.radioButtonSi);
            radioButtonNo = itemView.findViewById(R.id.radioButtonNo);
        }
    }

    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(data);
        } else {
            String[] keywords = query.toLowerCase().split(" ");

            for (JSONObject item : data) {
                String nombre = item.optString("nombre", "").toLowerCase();
                String empresa = item.optString("empresa", "").toLowerCase();
                String telefono = item.optString("telefono", "").toLowerCase();
                String estatus = item.optString("estatus", "").toLowerCase();
                String placas = item.optString("placas", "").toLowerCase();
                String modelo = item.optString("modelo", "").toLowerCase();

                String direccion = item.optString("direccion", "").toLowerCase();
                String id = item.optString("id", "").toLowerCase();


                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(modelo.contains(keyword) || empresa.contains(keyword) || direccion.contains(keyword) || telefono.contains(keyword) || id.contains(keyword) || placas.contains(keyword) ||
                            nombre.contains(keyword) || estatus.contains(keyword))) {
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

    public void setFilteredData(List<JSONObject> filteredData) {
        this.filteredData = new ArrayList<>(filteredData);
        notifyDataSetChanged();
    }


    private void setTextViewText(TextView textView, String text, String defaultText) {
        if (text.equals(null) || text.equals("") || text.equals(":null") || text.equals("null") || text.isEmpty()) {
            textView.setText(defaultText);
        } else {
            textView.setText(text);
        }
    }

}
