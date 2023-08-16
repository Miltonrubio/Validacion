package com.example.validacion;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapt extends RecyclerView.Adapter<Adapt.ViewHolder> {
    private List<JSONObject> data;

    private Context context;

    private String UrlApiRefacciones = "http://192.168.1.252/georgioapi/Controllers/Apiback.php";

    public String refacciones;

    public Adapt(List<JSONObject> data, Context context) {
        this.data = data;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coches, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject jsonObject = data.get(position);

            String id_ref = jsonObject.optString("id_ser_refacciones", "");
            String marca = jsonObject.optString("marcaI", "");

            if (!marca.equals("null")) {
                holder.textMarca.setText(marca);
            } else {
                holder.textMarca.setText("Marca no disponible");
            }

            String modelo = jsonObject.optString("modeloI", "");
            if (!modelo.equals("null")) {
                holder.textModelo.setText("Modelo: " + modelo);
            } else {
                holder.textModelo.setText("Modelo no disponible");
            }

            String urlOriginal = jsonObject.getString("foto");
            String urlFormateada = "AbHidalgo/91c9318a8e649f0a357ed81bb0b867bc.jpg";

            if (!TextUtils.isEmpty(urlOriginal) && !urlOriginal.equals("null")) {
                urlFormateada = urlOriginal.replace("-", "/");
            }

            String imageUrl = "http://tallergeorgio.hopto.org:5613/verificaciones/imagenes/unidades/" + urlFormateada;
            if (!TextUtils.isEmpty(imageUrl)) {
                Glide.with(holder.itemView.getContext())
                        .load(imageUrl)
                        .into(holder.imageViewCoches);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlApiRefacciones,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (!TextUtils.isEmpty(response)) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);

                                            // Crear un Bundle para enviar los datos al Fragment de Detalle
                                            Bundle bundle = new Bundle();
                                            bundle.putString("marca", marca);
                                            bundle.putString("modelo", modelo);
                                            bundle.putString("refacciones", jsonArray.toString()); // Aquí se incluye el JSONArray
                                            bundle.putString("motivo", jsonObject.optString("motivoingreso", ""));
                                            bundle.putString("fecha", jsonObject.optString("fecha_ingreso", ""));
                                            bundle.putString("status", jsonObject.optString("estatus", ""));
                                            bundle.putString("mecanico", jsonObject.optString("id_check_mecanico", ""));
                                            bundle.putString("foto", jsonObject.optString("foto", ""));
                                            bundle.putString("hora", jsonObject.optString("hora_ingreso", ""));

                                            // Instanciar el Fragment de Detalle y configurar el Bundle
                                            DetalleFragment detalleFragment = new DetalleFragment();
                                            detalleFragment.setArguments(bundle);

                                            // Abrir el Fragment de Detalle
                                            FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();

                                            fragmentManager.beginTransaction()
                                                    .replace(R.id.frame_layoutCoches, detalleFragment)
                                                    .addToBackStack(null)
                                                    .commit();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Log.d("API Response", "Respuesta vacía");
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Manejar errores de la solicitud aquí
                                    Log.e("API Error", "Error en la solicitud: " + error.getMessage());
                                }
                            }
                    ) {
                        @Override
                        public Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("opcion", "3");
                            params.put("idventa", "60");
                            return params;
                        }
                    };

                    // Agregar la solicitud a la cola de solicitudes
                    RequestQueue requestQueue2 = Volley.newRequestQueue(context);
                    requestQueue2.add(stringRequest);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textMarca, textModelo;

        ImageView imageViewCoches;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textMarca = itemView.findViewById(R.id.textMarca);
            textModelo = itemView.findViewById(R.id.textModelo);
            imageViewCoches = itemView.findViewById(R.id.imageViewCoches);
        }
    }
}
