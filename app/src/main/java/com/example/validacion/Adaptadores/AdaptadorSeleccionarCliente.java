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


public class AdaptadorSeleccionarCliente extends RecyclerView.Adapter<AdaptadorSeleccionarCliente.ViewHolder> {
    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    Context context;

    String url;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clientes, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String nombre = jsonObject2.optString("nombre", "");
            String telefono = jsonObject2.optString("telefono", "");
            String domicilio = jsonObject2.optString("domicilio", "");
            String id_ser_cliente = jsonObject2.optString("id_ser_cliente", "");


            Bundle bundle = new Bundle();
            bundle.putString("nombre", nombre);
            bundle.putString("id_ser_cliente", id_ser_cliente);

            setTextViewText(holder.textNombreUsuario, nombre.toUpperCase(), "No se encontro el nombre");
            setTextViewText(holder.textTelefonoUsuario, telefono, "No se encontro el telefono");
            setTextViewText(holder.textDireccionUsuario, domicilio, "No se encontro el domicilio");


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialogClientes.dismiss();
                    actionListener.onTomarCliente(nombre, id_ser_cliente);

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
        TextView textNombreUsuario;
        TextView textDireccionUsuario;

        TextView textTelefonoUsuario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombreUsuario = itemView.findViewById(R.id.textNombreUsuario);
            textDireccionUsuario = itemView.findViewById(R.id.textDireccionUsuario);
            textTelefonoUsuario = itemView.findViewById(R.id.textTelefonoUsuario);
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
/*
        if (filteredData.isEmpty()) {
            actionListener.onFilterData(false); // Indica que no hay resultados
        } else {
            actionListener.onFilterData(true); // Indica que hay resultados
        }
  */
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
        void onTomarCliente(String nombreCliente, String id_ser_cliente);
    }

    private AdaptadorSeleccionarCliente.OnActivityActionListener actionListener;


    AlertDialog dialogClientes;

    public AdaptadorSeleccionarCliente(List<JSONObject> data, Context context, AdaptadorSeleccionarCliente.OnActivityActionListener actionListener, AlertDialog dialogClientes) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.actionListener = actionListener;

        this.dialogClientes = dialogClientes;
        url = context.getResources().getString(R.string.ApiBack);
    }


}

