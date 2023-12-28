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

public class AdaptadorDesgloseActividades extends RecyclerView.Adapter<AdaptadorDesgloseActividades.ViewHolder> {


    private Context context;
    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    String url;

    public AdaptadorDesgloseActividades(List<JSONObject> data, Context context) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        url = context.getResources().getString(R.string.ApiBack);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_desglose_actividades, parent, false);
        return new ViewHolder(view);

    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String ID_check_actividad = jsonObject2.optString("ID_check_actividad", "");
            String valor_check = jsonObject2.optString("valor_check", "");
            String nombre_actividad = jsonObject2.optString("nombre_actividad", "");

            Bundle bundle = new Bundle();
            bundle.putString("ID_check_actividad", ID_check_actividad);
            bundle.putString("valor_check", valor_check);

            holder.radioButtonNo.setEnabled(false);
            holder.radioButtonSi.setEnabled(false);

            if (valor_check.equalsIgnoreCase("Si")) {

                holder.radioButtonNo.setChecked(false);
                holder.radioButtonSi.setChecked(true);


            } else if (valor_check.equalsIgnoreCase("No")) {

                holder.radioButtonNo.setChecked(true);
                holder.radioButtonSi.setChecked(false);
            } else {
                holder.radioButtonNo.setChecked(false);
                holder.radioButtonSi.setChecked(false);

            }

            holder.Descripcion.setText(nombre_actividad);


        } finally {

        }
    }


    @Override
    public int getItemCount() {

        //return filteredData.size();
        return filteredData.size();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButtonNo;
        RadioButton radioButtonSi;

        TextView Descripcion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButtonSi = itemView.findViewById(R.id.radioButtonSi);
            radioButtonNo = itemView.findViewById(R.id.radioButtonNo);
            Descripcion = itemView.findViewById(R.id.Descripcion);
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


                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(nombre.contains(keyword) || empresa.contains(keyword))) {
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

}
