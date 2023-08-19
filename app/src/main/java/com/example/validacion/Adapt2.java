package com.example.validacion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class Adapt2 extends RecyclerView.Adapter<Adapt2.ViewHolder> {

    private Context context;
    private String UrlApiRefacciones = "http://192.168.1.252/georgioapi/Controllers/Apiback.php";

    public String refacciones;


    private List<JSONObject> filteredData;
    private List<JSONObject> data;

    public Adapt2(List<JSONObject> data, Context context) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coches, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            JSONObject jsonObject = filteredData.get(position);

            String id_ref = jsonObject.optString("id_ser_refacciones", "");
            String marca = jsonObject.optString("marcaI", "");

            String modelo = jsonObject.optString("modeloI", "");
            String placa = jsonObject.optString("placasI", "");
            String dueño = jsonObject.optString("nombre", "");
            String motivo = jsonObject.optString("motivoingreso", "");


            String estatus = jsonObject.optString("estatus", "");

            String fecha_ingreso = jsonObject.optString("fecha_ingreso", "");

            String hora_ingreso = jsonObject.optString("hora_ingreso", "");

            if (!marca.equals("null") && !modelo.equals("null")) {
                holder.textMarca.setText(marca.toUpperCase() + " - " + modelo.toUpperCase());
            } else {
                holder.textMarca.setText("Marca no disponible");
            }

            if (!motivo.equals("null")) {
                holder.textModelo.setText(motivo.toUpperCase());
            } else {
                holder.textModelo.setText("Motivo no disponible");
            }

            if (!placa.equals("null")) {
                holder.textPlaca.setText(placa);
            } else {
                holder.textPlaca.setText("Placa no disponible");
            }

            if (!dueño.equals("null")) {
                holder.textDueño.setText(dueño);
            } else {
                holder.textDueño.setText("Placa no disponible");
            }

            if (!estatus.equals("null")) {
                holder.textStatus.setText(estatus);

                if (estatus.equals("pendiente")) {
                    holder.textStatus.setBackgroundResource(R.drawable.textview_outline3);
                } else if (estatus.equals("prueba")){
                    holder.textStatus.setBackgroundResource(R.drawable.textview_outline2);
                } else{
                    holder.textStatus.setBackgroundResource(R.drawable.textview_outline4);

                }

            } else {
                holder.textStatus.setText("Status no disponible");
                holder.textStatus.setBackgroundResource(R.drawable.textview_outline5);

            }

            if (!fecha_ingreso.equals("null") && !hora_ingreso.equals("null")) {
                holder.textFecha.setText(fecha_ingreso + ".   " + hora_ingreso);
            } else {
                holder.textFecha.setText("Fecha no disponible");
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
                        .error(R.drawable.default_image)
                        .into(holder.imageViewCoches);
            } else {
                Glide.with(holder.itemView.getContext())
                        .load(R.drawable.default_image)
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
                                        JSONObject jsonObject = filteredData.get(position);

                                        Bundle bundle = new Bundle();
                                        bundle.putString("marca", marca);
                                        bundle.putString("modelo", modelo);


                                   //     JSONArray refaccionesArray = new JSONArray();
                                        bundle.putString("refacciones", jsonObject.toString());
                                        Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_SHORT).show();

                                        bundle.putString("motivo", jsonObject.optString("motivoingreso", ""));
                                        bundle.putString("fecha", jsonObject.optString("fecha_ingreso", ""));
                                        bundle.putString("status", jsonObject.optString("estatus", ""));
                                        bundle.putString("mecanico", jsonObject.optString("id_check_mecanico", ""));
                                        bundle.putString("foto", jsonObject.optString("foto", ""));
                                        bundle.putString("hora", jsonObject.optString("hora_ingreso", ""));



                                        DetalleFragment detalleFragment = new DetalleFragment();
                                        detalleFragment.setArguments(bundle);


                                        FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();

                                        fragmentManager.beginTransaction()
                                                .replace(R.id.frame_layoutCoches, detalleFragment)
                                                .addToBackStack(null)
                                                .commit();
                                    } else {
                                        Log.d("API Response", "Respuesta vacía");
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

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

                    RequestQueue requestQueue2 = Volley.newRequestQueue(context);
                    requestQueue2.add(stringRequest);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Clic Largo");
                    builder.setMessage("Has dejado presionado el elemento en la posición: " + position);

                    builder.setPositiveButton("Subir Foto", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(context, Prueba.class);
                            intent.putExtra("position", position); // Puedes pasar datos adicionales si es necesario
                            context.startActivity(intent);
                        }
                    });

                    builder.setNegativeButton("Detalles", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlApiRefacciones,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if (!TextUtils.isEmpty(response)) {
                                                JSONObject jsonObject = filteredData.get(position);

                                                Bundle bundle = new Bundle();
                                                bundle.putString("marca", marca);
                                                bundle.putString("modelo", modelo);
                                                bundle.putString("refacciones", jsonObject.toString()); // Aquí se incluye el JSONArray
                                                bundle.putString("motivo", jsonObject.optString("motivoingreso", ""));
                                                bundle.putString("fecha", jsonObject.optString("fecha_ingreso", ""));
                                                bundle.putString("status", jsonObject.optString("estatus", ""));
                                                bundle.putString("mecanico", jsonObject.optString("id_check_mecanico", ""));
                                                bundle.putString("foto", jsonObject.optString("foto", ""));
                                                bundle.putString("hora", jsonObject.optString("hora_ingreso", ""));

                                                DetalleFragment detalleFragment = new DetalleFragment();
                                                detalleFragment.setArguments(bundle);

                                                FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();

                                                fragmentManager.beginTransaction()
                                                        .replace(R.id.frame_layoutCoches, detalleFragment)
                                                        .addToBackStack(null)
                                                        .commit();
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

                    builder.show();

                    return true; // Indica que el evento ha sido manejado
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return filteredData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textMarca, textModelo, textPlaca, textDueño, textFecha, textStatus;

        ImageView imageViewCoches;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textMarca = itemView.findViewById(R.id.textMarca);
            textModelo = itemView.findViewById(R.id.textModelo);
            textPlaca = itemView.findViewById(R.id.textPlaca);
            textDueño = itemView.findViewById(R.id.textDueño);
            textFecha = itemView.findViewById(R.id.textFecha);
            textStatus = itemView.findViewById(R.id.textStatus);
            imageViewCoches = itemView.findViewById(R.id.imageViewCoches);


        }
    }

    public void filter(String query) {
        if (TextUtils.isEmpty(query)) {
            filteredData = new ArrayList<>(data); // Restaura la lista filtrada a la lista original
        } else {
            filteredData.clear();
            for (JSONObject item : data) {
                String marca = item.optString("marcaI", "").toLowerCase();
                String modelo = item.optString("modeloI", "").toLowerCase();
                String nombre = item.optString("nombre", "").toLowerCase();
                String status = item.optString("estatus", "").toLowerCase();
                String placa = item.optString("placasI", "").toLowerCase();
                if (marca.contains(query) || modelo.contains(query) || placa.contains(query)|| nombre.contains(query)|| status.contains(query)) {
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
