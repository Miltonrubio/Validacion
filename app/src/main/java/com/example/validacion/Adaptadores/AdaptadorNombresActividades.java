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
import com.example.validacion.R;

import org.checkerframework.common.returnsreceiver.qual.This;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;


public class AdaptadorNombresActividades extends RecyclerView.Adapter<AdaptadorNombresActividades.ViewHolder> {

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    Context context;
    String url;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seleccion_actividades, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String nombre = jsonObject2.optString("nombre", "");
            String idactividad = jsonObject2.optString("idactividad", "");


            Bundle bundle = new Bundle();
            bundle.putString("nombre", nombre);

            setTextViewText(holder.nombreActividad, nombre.toUpperCase(), "No se encontro el nombre");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_asignar_actividad, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogAsignarActividad = builder.create();
                    dialogAsignarActividad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogAsignarActividad.show();


                    EditText editTextDescripcion = customView.findViewById(R.id.editTextDescripcion);
                    Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                    Button buttonSubirFoto = customView.findViewById(R.id.buttonSubirFoto);

                    TextView textViewDescripcion = customView.findViewById(R.id.textViewDescripcion);
                    textViewDescripcion.setText(nombre);


                    buttonCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogAsignarActividad.dismiss();
                        }
                    });


                    buttonSubirFoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String descripcion = editTextDescripcion.getText().toString();

                            if (descripcion.isEmpty()) {

                                Utiles.crearToastPersonalizado(context, "Agrega una descripción para poder asignar la actividad");

                            } else {


                                dialogAsignarActividad.dismiss();
                                actionListener.onRegistrarNuevaActividad(idactividad, descripcion);


                            }

                        }
                    });

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
        TextView nombreActividad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreActividad = itemView.findViewById(R.id.nombreActividad);
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
                String telefono = item.optString("telefono", "").toLowerCase();
                String domicilio = item.optString("domicilio", "").toLowerCase();
                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(nombre.contains(keyword) || telefono.contains(keyword) || domicilio.contains(keyword))) {
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


    public interface OnActivityActionListener {
        void onRegistrarNuevaActividad(String idbitacora, String descripcion);
    }

    private AdaptadorNombresActividades.OnActivityActionListener actionListener;


    public AdaptadorNombresActividades(List<JSONObject> data, Context context, AdaptadorNombresActividades.OnActivityActionListener actionListener) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        url = context.getResources().getString(R.string.ApiBack);
        this.actionListener = actionListener;
    }


}

