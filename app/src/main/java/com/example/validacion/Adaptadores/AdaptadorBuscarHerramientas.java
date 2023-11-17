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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class AdaptadorBuscarHerramientas extends RecyclerView.Adapter<AdaptadorBuscarHerramientas.ViewHolder> {


    private Context context;

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    String url;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_herramientas, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        url = context.getResources().getString(R.string.ApiBack);

        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String foto = jsonObject2.optString("foto", "");
            String id_cajon = jsonObject2.optString("id_cajon", "");
            String nombre = jsonObject2.optString("nombre", "");
            String piezas = jsonObject2.optString("piezas", "");
            String idHerramienta = jsonObject2.optString("idHerramienta");
            String descripcion = jsonObject2.optString("descripcion");
            String nombre_cajon = jsonObject2.optString("nombre_cajon");
            String nombre_mecanico = jsonObject2.optString("nombre_mecanico");
            String nombre_gaveta = jsonObject2.optString("nombre_gaveta");


            Bundle bundle = new Bundle();
            bundle.putString("nombre", nombre);
            bundle.putString("id_cajon", id_cajon);
            bundle.putString("foto", foto);
            bundle.putString("piezas", piezas);
            bundle.putString("idHerramienta", idHerramienta);

            holder.nombreHerramienta.setText(nombre);
            holder.piezas.setText(piezas + " Pzs");


            if (nombre_mecanico.isEmpty() || nombre_mecanico.equalsIgnoreCase("null") || nombre_mecanico.equals(null)) {

                holder.mecanicoACargo.setText("Aun no tiene mecanico asignado");

            } else {
                holder.mecanicoACargo.setText("A cargo de " + nombre_mecanico);

            }


            if (nombre_gaveta.isEmpty() || nombre_gaveta.equalsIgnoreCase("null") || nombre_gaveta.equals(null)) {
                holder.NoGaveta.setText("No tiene gaveta asignada");
            } else {
                holder.NoGaveta.setText("Nombre de gaveta " + nombre_gaveta);
            }


            if (nombre_cajon.isEmpty() || nombre_cajon.equalsIgnoreCase("null") || nombre_cajon.equals(null)) {
                holder.NumCajon.setText("No tiene cajòn asignado");
            } else {
                holder.NumCajon.setText(nombre_cajon);
            }


            String image = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/herramienta/" + foto;


            Glide.with(holder.itemView.getContext())
                    .load(image)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(R.drawable.baseline_construction_24)
                    .error(R.drawable.baseline_construction_24)
                    .into(holder.imageViewherramientas);


            holder.descripcion.setText(descripcion);


        } finally {

        }
    }

    @Override
    public int getItemCount() {

        //return filteredData.size();
        return filteredData.size();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreHerramienta, piezas;
        TextView descripcion;
        ImageView imageViewherramientas;
        TextView mecanicoACargo;
        TextView NumCajon;
        TextView NoGaveta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreHerramienta = itemView.findViewById(R.id.nombreHerramienta);
            piezas = itemView.findViewById(R.id.piezas);
            descripcion = itemView.findViewById(R.id.descripcion);
            imageViewherramientas = itemView.findViewById(R.id.imageViewherramientas);
            mecanicoACargo = itemView.findViewById(R.id.mecanicoACargo);
            NumCajon = itemView.findViewById(R.id.NumCajon);
            NoGaveta = itemView.findViewById(R.id.NoGaveta);
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


    public AdaptadorBuscarHerramientas(List<JSONObject> data, Context context) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);


    }


}
