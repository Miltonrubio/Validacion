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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.validacion.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;


public class AdaptadorAsignarMecanico extends RecyclerView.Adapter<AdaptadorAsignarMecanico.ViewHolder> {


    public AdaptadorAsignarMecanico(List<JSONObject> data, Context context) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        url = context.getResources().getString(R.string.ApiBack);
    }


    public interface OnItemClickListener {
        void onMecanicoSeleccionado(String idusuario, String nombre);
    }


    private OnItemClickListener onMecanicoSeleccionado;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onMecanicoSeleccionado = listener;
    }


    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    Context context;
    String url;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seleccionar_mecanicos, parent, false);

        return new ViewHolder(view);

    }


    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.ContenedorUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMecanicoSeleccionado != null) {
                    String idusuario = filteredData.get(position).optString("idusuario", "");
                    String nombre = filteredData.get(position).optString("nombre", "");
                    onMecanicoSeleccionado.onMecanicoSeleccionado(idusuario, nombre);
                }
            }
        });


        try {
            JSONObject jsonObject2 = filteredData.get(position);

            String nombre = jsonObject2.optString("nombre", "");
            String foto = jsonObject2.optString("foto", "");
            String idusuario = jsonObject2.optString("idusuario", "");

            setTextViewText(holder.NombreMecanico, nombre, "No se encontro el modelo");



            String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/usuarios/" + foto;

            Glide.with(context)
                    .load(imageUrl)
                    .error(R.drawable.baseline_person_24)
                    .into(holder.ImagenMecanico);




        } finally {
        }
    }


    @Override
    public int getItemCount() {

        return filteredData.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView NombreMecanico;
        ImageView ImagenMecanico;

        ConstraintLayout ContenedorUsuario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            NombreMecanico = itemView.findViewById(R.id.NombreMecanico);
            ImagenMecanico = itemView.findViewById(R.id.ImagenMecanico);
            ContenedorUsuario = itemView.findViewById(R.id.ContenedorUsuario);

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

}

