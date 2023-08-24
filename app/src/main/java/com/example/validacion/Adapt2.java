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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class Adapt2 extends RecyclerView.Adapter<Adapt2.ViewHolder> {

    private Context context;
    private String Url = "http://192.168.1.252/georgioapi/Controllers/Apiback.php";
    private String UrlApiRefacciones = "http://192.168.1.252/georgioapi/Controllers/Apiback.php";

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
            JSONObject jsonObject2 = filteredData.get(position);
            String id_ref = jsonObject2.optString("id_ser_refacciones", "");
            String marca = jsonObject2.optString("marcaI", "");
            String modelo = jsonObject2.optString("modeloI", "");
            String placa = jsonObject2.optString("placasI", "");
            String dueño = jsonObject2.optString("nombre", "");
            String motivo = jsonObject2.optString("motivoingreso", "");
            String estatus = jsonObject2.optString("estatus", "");
            String fecha_ingreso = jsonObject2.optString("fecha_ingreso", "");
            String hora_ingreso = jsonObject2.optString("hora_ingreso", "");

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
                } else if (estatus.equals("prueba")) {
                    holder.textStatus.setBackgroundResource(R.drawable.textview_outline2);
                } else {
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

            String urlOriginal = jsonObject2.getString("foto");
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

                    AdaptadorRefacciones adaptadorRefacciones = new AdaptadorRefacciones(new ArrayList<>());
                    obtenerDatosVolley(position, marca, modelo, motivo, fecha_ingreso, estatus, hora_ingreso /*, adaptadorRefacciones*/);

                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Seleccionaste el elemento: " + marca  + " " +modelo);

//                    (position, marca, modelo, motivo, fecha_ingreso, estatus, hora_ingreso);
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

                            AdaptadorRefacciones adaptadorRefacciones = new AdaptadorRefacciones(new ArrayList<>());
                            obtenerDatosVolley(position, marca, modelo, motivo, fecha_ingreso, estatus, hora_ingreso/* , adaptadorRefacciones*/);
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
                if (marca.contains(query) || modelo.contains(query) || placa.contains(query) || nombre.contains(query) || status.contains(query)) {
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


    private void obtenerDatosVolley(int position, String marca, String modelo, String motivo, String fecha_ingreso, String estatus, String hora_ingreso/* , AdaptadorRefacciones adaptadorRefacciones*/) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlApiRefacciones,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                              //  List<Refacciones> listaRefacciones = new ArrayList<>();

                                Bundle bundle = new Bundle();
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String descripcion = jsonObject.optString("descripcion", "");
                                    String cantidad = jsonObject.optString("cantidad", "");
                                    String precio = jsonObject.optString("precio", "");
                                    String importe = jsonObject.optString("importe", "");
                                    String idventa = jsonObject.optString("idventa", "");


                                    /*
                                    stringBuilder.append("descripción").append(descripcion);
                                    stringBuilder.append("cantidad").append(cantidad);
                                    stringBuilder.append("precio").append(precio);
                                    stringBuilder.append("importe").append(importe);
                                    stringBuilder.append("idventa").append(importe);
*/

                                /*    Refacciones refaccion = new Refacciones(cantidad, descripcion, precio, idventa);
                                    listaRefacciones.add(refaccion);

                                    */
                                }

                                bundle.putString("marca", marca);
                                bundle.putString("modelo", modelo);
                                bundle.putString("refacciones", response.toString());
                                bundle.putString("motivo", motivo);
                                bundle.putString("fecha", fecha_ingreso);
                                bundle.putString("status", estatus);
                                bundle.putString("hora", hora_ingreso);

                           //    adaptadorRefacciones.actualizarLista(listaRefacciones);
                                realizarSegundoRequest(bundle);
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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    private void realizarSegundoRequest(Bundle bundle) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                           //     StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                     /*   JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String descripcion = jsonObject.optString("nombre", "");
                                    String cantidad = jsonObject.optString("motivoingreso", "");
                                    stringBuilder.append(descripcion).append("\n");
                                    stringBuilder.append("Encargado de: ").append(cantidad).append("\n");
                                    stringBuilder.append("\n");*/
                                }

                                bundle.putString("mecanicos", response.toString());

                                DetalleFragment detalleFragment = new DetalleFragment();
                                detalleFragment.setArguments(bundle);

                                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
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
                        // Manejar el error aquí
                        Log.e("VolleyError", "Error en la solicitud: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "6");
                params.put("idventa", "60");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


}
