package com.example.validacion.Adaptadores;


import static android.app.PendingIntent.getActivity;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;
import static com.example.validacion.Adaptadores.Utiles.crearToastPersonalizado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.validacion.R;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdaptadorTiposUnidadesDesdeInicio extends RecyclerView.Adapter<AdaptadorTiposUnidadesDesdeInicio.ViewHolder> {

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    Context context;

    String url;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tipos_unidades, parent, false);
        return new ViewHolder(view);

    }


    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String nombre = jsonObject2.optString("nombre", "");
            String estado = jsonObject2.optString("estado", "");
            String foto = jsonObject2.optString("foto", "");
            String ID_tipo_unidad = jsonObject2.optString("ID_tipo_unidad", "");
            String placas = jsonObject2.optString("placas", "");
            String km = jsonObject2.optString("km", "");
            String gasolina = jsonObject2.optString("gasolina", "");
            String vin = jsonObject2.optString("vin", "");
            String motor = jsonObject2.optString("motor", "");


            setTextViewText(holder.TipoUnidad, nombre, "No se encontro el modelo");

            String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/" + foto;

            Glide.with(context)
                    .load(imageUrl)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.baseline_directions_car_filled_24)
                    .into(holder.imagenCarrito);

            Bundle bundle = new Bundle();
            bundle.putString("ID_tipo_unidad", ID_tipo_unidad);
            bundle.putString("checkPlacas", placas);
            bundle.putString("checkKm", km);
            bundle.putString("checkVin", vin);
            bundle.putString("checkMotor", motor);
            bundle.putString("checkGasolina", gasolina);

            holder.LayoutUnidad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_registrar_unidad, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogMarcas = builder.create();
                    dialogMarcas.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogMarcas.show();

                    TextView textView24 = customView.findViewById(R.id.textView24);

                    textView24.setText("Registra los datos solicitados para " + nombre);








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
        TextView TipoUnidad;
        ImageView imagenCarrito;
        ConstraintLayout LayoutUnidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TipoUnidad = itemView.findViewById(R.id.TipoUnidad);
            LayoutUnidad = itemView.findViewById(R.id.LayoutUnidad);
            imagenCarrito = itemView.findViewById(R.id.imagenCarrito);
        }
    }


    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(data);
        } else {
            String[] keywords = query.toLowerCase().split(" ");

            for (JSONObject item : data) {
                String nombre = item.optString("name", "").toLowerCase();
                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(nombre.contains(keyword))) {
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


    public AdaptadorTiposUnidadesDesdeInicio(List<JSONObject> data, Context context) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);

    }


}

