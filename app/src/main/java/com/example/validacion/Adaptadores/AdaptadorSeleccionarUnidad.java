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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.validacion.R;
import com.itextpdf.text.pdf.parser.Line;

import org.checkerframework.common.returnsreceiver.qual.This;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdaptadorSeleccionarUnidad extends RecyclerView.Adapter<AdaptadorSeleccionarUnidad.ViewHolder> {
    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    Context context;

    String url;
    AlertDialog dialogUnidades;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unidades_cliente, parent, false);
        return new ViewHolder(view);

    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String placas = jsonObject2.optString("placas", "");
            String modelo = jsonObject2.optString("modelo", "");
            String marca = jsonObject2.optString("marca", "");
            String id_serv_unidad = jsonObject2.optString("id_serv_unidad", "");
            String foto = jsonObject2.optString("foto", "");
            String vin = jsonObject2.optString("vin", "");
            String motor = jsonObject2.optString("motor", "");
            String anio = jsonObject2.optString("anio", "");
            String tipo = jsonObject2.optString("tipo", "");

            holder.LayoutAgregarServicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialogUnidades.dismiss();
                    actionListener.onTomarUnidad(id_serv_unidad, marca, modelo, vin, motor, anio, placas, tipo, foto);

                }
            });


            setTextViewText(holder.NombreUnidad, marca.toUpperCase() + " " + modelo.toUpperCase(), "No se encontro el nombre");
            setTextViewText(holder.PlacasUnidad, placas.toUpperCase(), "No se encontro el telefono");
            holder.PlacasUnidad.setVisibility(View.VISIBLE);

            String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/" + foto;


            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.baseline_directions_car_filled_24)
                    .error(R.drawable.baseline_directions_car_filled_24)
                    .into(holder.imagenCarrito);


        } finally {
        }
    }


    /*
    private void MostrarModalMarcas(View view, Bundle bundle, AlertDialog dialogUnidadesDeCliente) {

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_agregar_unidad, null);
        builder.setView(ModalRedondeado(view.getContext(), customView));
        AlertDialog dialogMarcas = builder.create();
        dialogMarcas.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogMarcas.show();

        VerNombresMarcas();

        TextView textView3 = customView.findViewById(R.id.text);
        textView3.setText("SELECCIONA UNA MARCA");

        ConstraintLayout yourConstraintLayoutId = customView.findViewById(R.id.yourConstraintLayoutId);
        yourConstraintLayoutId.setVisibility(View.VISIBLE);
        EditText searchEditText = customView.findViewById(R.id.searchEditText);
        searchEditText.setHint("Buscar la marca");


        LayoutSeleccionarMarca = customView.findViewById(R.id.SeleccionarMarca);
        LayoutSeleccionarModelo = customView.findViewById(R.id.SeleccionarModelo);
        LayoutAgregarDatos = customView.findViewById(R.id.LayoutAgregarDatos);
        reciclerViewMarcas = customView.findViewById(R.id.reciclerViewMarcas);


        LayoutSeleccionarMarca.setVisibility(View.VISIBLE);
        LayoutSeleccionarModelo.setVisibility(View.GONE);
        LayoutAgregarDatos.setVisibility(View.GONE);


        adaptadorMarcas = new AdaptadorMarcas(listaMarcas, customView.getContext(), bundle, actionListener, dialogUnidadesDeCliente, dialogMarcas);
        reciclerViewMarcas.setLayoutManager(new LinearLayoutManager(customView.getContext()));
        reciclerViewMarcas.setAdapter(adaptadorMarcas);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptadorMarcas.filter(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private void VerNombresMarcas() {
        listaMarcas.clear(); // Limpia la lista antes de agregar los nuevos
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        listaMarcas.add(jsonObject);
                    }

                    adaptadorMarcas.setFilteredData(listaMarcas);
                    adaptadorMarcas.filter("");


                } catch (JSONException e) {
                    crearToastPersonalizado(context, "Error al cargar los datos");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                crearToastPersonalizado(context, "Error al cargar los datos");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "32");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }



*/

    @Override
    public int getItemCount() {

        return filteredData.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView NombreUnidad;
        TextView PlacasUnidad;

        ImageView imagenCarrito;
        LinearLayout LayoutAgregarServicio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            NombreUnidad = itemView.findViewById(R.id.NombreUnidad);
            PlacasUnidad = itemView.findViewById(R.id.PlacasUnidad);
            imagenCarrito = itemView.findViewById(R.id.imagenCarrito);
            LayoutAgregarServicio = itemView.findViewById(R.id.LayoutAgregarServicio);
        }
    }

    /*

            String placas = jsonObject2.optString("placas", "");
            String Modelo = jsonObject2.optString("Modelo", "");
            String Marca = jsonObject2.optString("Marca", "");
            String id_serv_unidad = jsonObject2.optString("id_serv_unidad", "");
     */

    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(data);
        } else {
            String[] keywords = query.toLowerCase().split(" ");

            for (JSONObject item : data) {
                String placas = item.optString("placas", "").toLowerCase();
                String Modelo = item.optString("Modelo", "").toLowerCase();
                String Marca = item.optString("Marca", "").toLowerCase();
                String MarcaCoche = item.optString("MarcaCoche", "").toLowerCase();
                String ModeloCoche = item.optString("ModeloCoche", "").toLowerCase();
                String id_serv_unidad = item.optString("id_serv_unidad", "").toLowerCase();


                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(placas.contains(keyword) || MarcaCoche.contains(keyword) || ModeloCoche.contains(keyword) || Modelo.contains(keyword) || Marca.contains(keyword) || id_serv_unidad.contains(keyword))) {
                        matchesAllKeywords = false;
                        break;
                    }
                }

                if (matchesAllKeywords) {
                    filteredData.add(item);
                }
            }
        }

        if (filteredData.isEmpty()) {
            actionListener.onResultadosUnidad(false);
        } else {
            actionListener.onResultadosUnidad(true);
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
        void onTomarUnidad(String id_serv_unidad, String marca, String modelo, String vin, String motor, String anio, String placas, String tipo, String foto);


        void onResultadosUnidad(boolean result);



    }

    private AdaptadorSeleccionarUnidad.OnActivityActionListener actionListener;


    public AdaptadorSeleccionarUnidad(List<JSONObject> data, Context context, AdaptadorSeleccionarUnidad.OnActivityActionListener actionListener, AlertDialog dialogUnidades) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.actionListener = actionListener;
        this.dialogUnidades = dialogUnidades;
        url = context.getResources().getString(R.string.ApiBack);
    }


}

