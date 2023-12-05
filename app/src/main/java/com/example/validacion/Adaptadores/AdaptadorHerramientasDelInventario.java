package com.example.validacion.Adaptadores;

import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;
import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.validacion.Activity_Binding;
import com.example.validacion.Objetos.Cheks;
import com.example.validacion.R;
import com.example.validacion.SubirFotosUnidadesActivity;
import com.itextpdf.text.pdf.parser.Line;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.internal.Util;

public class AdaptadorHerramientasDelInventario extends RecyclerView.Adapter<AdaptadorHerramientasDelInventario.ViewHolder> {


    AdaptadorInventarioPorGaveta adaptadorInventarioPorGaveta;
    private Context context;

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    String url;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_herramientas_en_inventario, parent, false);
        return new ViewHolder(view);

    }


    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        url = context.getResources().getString(R.string.ApiBack);


        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String idinv = jsonObject2.optString("idinv", "");
            String estadoHerr = jsonObject2.optString("estadoHerr", "");
            String herramienta = jsonObject2.optString("herramienta", "");
            String estatus = jsonObject2.optString("estatus", "");
            String foto = jsonObject2.optString("foto", "");
            String cantidad = jsonObject2.optString("cantidad", "");




/*
            if (estadoHerr.isEmpty() || estadoHerr.equalsIgnoreCase("NA")|| estadoHerr.equalsIgnoreCase("0")) {
                sumaVacios=sumaVacios+1;
                Log.d("Entro a la validacion ", "Si si entro");
            }

            Log.d("vacios", "total de vacios: " + sumaVacios);
*/
            Bundle bundle = new Bundle();
            bundle.putString("idinv", idinv);
            bundle.putString("estadoHerr", estadoHerr);

            holder.estadoHerramienta.setText(estatus);
            String image = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/herramienta/" + foto;


            Glide.with(holder.itemView.getContext())
                    .load(image)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(R.drawable.baseline_construction_24)
                    .error(R.drawable.baseline_construction_24)
                    .into(holder.imageViewherramientas);


            holder.nombreHerramienta.setText(herramienta);

            holder.piezas.setText(cantidad);


            try {
                double numero = Double.parseDouble(cantidad);
                String cadenaFormateada = String.valueOf((int) numero);
                holder.piezas.setText(cadenaFormateada);
            } catch (NumberFormatException e) {
                holder.piezas.setText(0);
            }


            if (estadoinv.equalsIgnoreCase("pendiente")) {

                holder.radioButtonBueno.setEnabled(true);
                holder.radioButtonMalo.setEnabled(true);
                holder.radioButtonRegular.setEnabled(true);
                holder.radioButtonNA.setEnabled(true);
                holder.piezas.setEnabled(true);

            } else {

                holder.radioButtonBueno.setEnabled(false);
                holder.radioButtonMalo.setEnabled(false);
                holder.radioButtonRegular.setEnabled(false);
                holder.radioButtonNA.setEnabled(false);
                holder.piezas.setEnabled(false);

            }


            if (estadoHerr.equalsIgnoreCase("B")) {

                holder.radioButtonBueno.setChecked(true);
                holder.radioButtonMalo.setChecked(false);
                holder.radioButtonRegular.setChecked(false);
                holder.radioButtonNA.setChecked(false);


            } else if (estadoHerr.equalsIgnoreCase("M")) {

                holder.radioButtonBueno.setChecked(false);
                holder.radioButtonMalo.setChecked(true);
                holder.radioButtonRegular.setChecked(false);
                holder.radioButtonNA.setChecked(false);

            } else if (estadoHerr.equalsIgnoreCase("R")) {


                holder.radioButtonBueno.setChecked(false);
                holder.radioButtonMalo.setChecked(false);
                holder.radioButtonRegular.setChecked(true);
                holder.radioButtonNA.setChecked(false);
            } else if (estadoHerr.equalsIgnoreCase("NA")) {

                holder.radioButtonBueno.setChecked(false);
                holder.radioButtonMalo.setChecked(false);
                holder.radioButtonRegular.setChecked(false);
                holder.radioButtonNA.setChecked(true);
            } else {

                holder.radioButtonBueno.setChecked(false);
                holder.radioButtonMalo.setChecked(false);
                holder.radioButtonRegular.setChecked(false);
                holder.radioButtonNA.setChecked(false);
            }

            holder.radioButtonNA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.radioButtonBueno.setChecked(false);
                    holder.radioButtonMalo.setChecked(false);
                    holder.radioButtonRegular.setChecked(false);
                    holder.radioButtonNA.setChecked(true);

                    ActualizarCheck("NA", idinv, herramienta);

                    if (estadoHerr.equalsIgnoreCase("0") || estadoHerr.isEmpty()) {
                        contador--;
                    }
                }
            });


            holder.radioButtonRegular.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.radioButtonBueno.setChecked(false);
                    holder.radioButtonMalo.setChecked(false);
                    holder.radioButtonRegular.setChecked(true);
                    holder.radioButtonNA.setChecked(false);


                    ActualizarCheck("R", idinv, herramienta);
                    if (estadoHerr.equalsIgnoreCase("0") || estadoHerr.isEmpty()) {
                        contador--;
                    }
                }
            });


            holder.radioButtonMalo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.radioButtonBueno.setChecked(false);
                    holder.radioButtonMalo.setChecked(true);
                    holder.radioButtonRegular.setChecked(false);
                    holder.radioButtonNA.setChecked(false);

                    ActualizarCheck("M", idinv, herramienta);
                    if (estadoHerr.equalsIgnoreCase("0") || estadoHerr.isEmpty()) {
                        contador--;
                    }

                }
            });


            holder.radioButtonBueno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.radioButtonBueno.setChecked(true);
                    holder.radioButtonMalo.setChecked(false);
                    holder.radioButtonRegular.setChecked(false);
                    holder.radioButtonNA.setChecked(false);
                    ActualizarCheck("B", idinv, herramienta);
                    if (estadoHerr.equalsIgnoreCase("0") || estadoHerr.isEmpty()) {
                        contador--;
                    }
                }
            });


            holder.piezas.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                        String cantidadHerr = holder.piezas.getText().toString();

                        ActualizarPiezas(cantidadHerr, idinv, herramienta);
                        return true; // Se maneja el evento
                    }
                    return false; // No se maneja el evento
                }
            });


        } finally {

        }
    }

    private void ActualizarPiezas(String cantidadHerr, String idinv, String nombreherramienta) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se actualizó la cantidad de " + nombreherramienta);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "No se pudo actualizar, revisa la conexion por favor");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "69");
                params.put("idinv", idinv);
                params.put("cantidadHerr", cantidadHerr);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void ActualizarCheck(String estadoHerramienta, String idinv, String nombreherramienta) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se actualizó el estado de " + nombreherramienta);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "No se pudo actualizar, revisa la conexion por favor");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "58");
                params.put("idinv", idinv);
                params.put("estadoHerramienta", estadoHerramienta);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    @Override
    public int getItemCount() {

        //return filteredData.size();
        return filteredData.size();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView estadoHerramienta;
        TextView nombreHerramienta;

        ImageView imageViewherramientas;

        TextView descripcion;

        RadioButton radioButtonBueno;
        RadioButton radioButtonMalo;
        RadioButton radioButtonRegular;
        RadioButton radioButtonNA;

        EditText piezas;
        TextView NumCajon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            estadoHerramienta = itemView.findViewById(R.id.estadoHerramienta);

            imageViewherramientas = itemView.findViewById(R.id.imageViewherramientas);
            NumCajon = itemView.findViewById(R.id.NumCajon);

            nombreHerramienta = itemView.findViewById(R.id.nombreHerramienta);
            radioButtonBueno = itemView.findViewById(R.id.radioButtonBueno);
            radioButtonMalo = itemView.findViewById(R.id.radioButtonMalo);
            radioButtonRegular = itemView.findViewById(R.id.radioButtonRegular);
            radioButtonNA = itemView.findViewById(R.id.radioButtonNA);

            piezas = itemView.findViewById(R.id.piezas);

            descripcion = itemView.findViewById(R.id.descripcion);

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

    String estadoinv;


    public AdaptadorHerramientasDelInventario(List<JSONObject> data, Context context, String estadoinv) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.estadoinv = estadoinv;
        contador = obtenerSuma();
    }


    int contador = 0;

    public int obtenerSuma() {
        contador = 0;
        for (JSONObject jsonObject : filteredData) {
            String valorCheck = jsonObject.optString("estadoHerr", "");

            if (TextUtils.isEmpty(valorCheck) || valorCheck.equalsIgnoreCase("0")) {
                contador++;
            }
        }

        return contador;
    }

    public int obtenerContador() {
        return contador;
    }


}
