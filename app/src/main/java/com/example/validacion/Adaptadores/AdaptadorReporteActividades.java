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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdaptadorReporteActividades extends RecyclerView.Adapter<AdaptadorReporteActividades.ViewHolder> {


    private Context context;
    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    String url;

    List<JSONObject> listadoActividades = new ArrayList<>();

    public AdaptadorReporteActividades(List<JSONObject> data, Context context) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        url = context.getResources().getString(R.string.ApiBack);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reporte_actividades, parent, false);
        return new ViewHolder(view);

    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String fecha = jsonObject2.optString("fecha", "");
            String ID_registro_actividades = jsonObject2.optString("ID_registro_actividades", "");
            String desglose_actividades = jsonObject2.optString("desglose_actividades", "");

            Bundle bundle = new Bundle();
            bundle.putString("ID_registro_actividades", ID_registro_actividades);
            bundle.putString("fecha", fecha);


            listadoActividades.clear();
            try {
                JSONArray jsonArray = new JSONArray(desglose_actividades);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    listadoActividades.add(jsonObject);
                }

                AdaptadorDesgloseActividades adaptadorDesgloseActividades = new AdaptadorDesgloseActividades(listadoActividades, context);
                holder.recyclerViewReporteActividades.setLayoutManager(new LinearLayoutManager(context));
                holder.recyclerViewReporteActividades.setAdapter(adaptadorDesgloseActividades);
                adaptadorDesgloseActividades.notifyDataSetChanged();
                adaptadorDesgloseActividades.setFilteredData(listadoActividades);
                adaptadorDesgloseActividades.filter("");

            } catch (JSONException e) {

            }

            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date date = inputFormat.parse(fecha);
                SimpleDateFormat outputDayOfWeek = new SimpleDateFormat("EEEE", new Locale("es", "ES"));
                String dayOfWeek = outputDayOfWeek.format(date);
                SimpleDateFormat outputFormat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
                String formattedDate = outputFormat.format(date);

                holder.textView21.setText("Actividades del: " + dayOfWeek.toLowerCase() + " " + formattedDate.toLowerCase());

            } catch (ParseException e) {
                holder.textView21.setText("No se encontro la fecha");
            }


        } finally {

        }
    }


    @Override
    public int getItemCount() {

        //return filteredData.size();
        return filteredData.size();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerViewReporteActividades;

        TextView textView21;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView21 = itemView.findViewById(R.id.textView21);
            recyclerViewReporteActividades = itemView.findViewById(R.id.recyclerViewReporteActividades);
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
