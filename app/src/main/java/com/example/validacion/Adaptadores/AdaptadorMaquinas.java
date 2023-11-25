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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class AdaptadorMaquinas extends RecyclerView.Adapter<AdaptadorMaquinas.ViewHolder> {


    private Context context;

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    String url;
    AdaptadorMecanicoAMaquina adaptadorMecanicoAMaquina;

    ConstraintLayout ContenedorContenido;
    ConstraintLayout ContenedorSinInternet;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_maquinas, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        url = context.getResources().getString(R.string.ApiBack);

        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String nombre = jsonObject2.optString("nombre", "");
            String idmaquina = jsonObject2.optString("idmaquina", "");
            String marca = jsonObject2.optString("marca", "");
            String foto = jsonObject2.optString("foto", "");
            String modelo = jsonObject2.optString("modelo", "");
            String nserie = jsonObject2.optString("nserie", "");
            String area = jsonObject2.optString("area", "");
            String idusuario = jsonObject2.optString("idusuario", "");
            String nombreresponsable = jsonObject2.optString("nombreresponsable", "");

            holder.nombreMaquina.setText(nombre);

            holder.tvMarcaModelo.setText(marca.toUpperCase() + " " + modelo.toUpperCase());
            holder.idmaquina.setText("ID:" + idmaquina);


            if (nombreresponsable.isEmpty() || nombreresponsable.equals(null) || nombreresponsable.equals("null")) {

                holder.tvACargo.setText("No tiene usuario asignado");
            } else {

                holder.tvACargo.setText(nombreresponsable);
            }

            String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/maquinas/" + foto;
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.baseline_precision_manufacturing_24)
                    .error(R.drawable.baseline_precision_manufacturing_24)
                    .into(holder.imageViewMaquina);


            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if (nombreresponsable.isEmpty() || nombreresponsable.equals(null) || nombreresponsable.equals("null")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_ver_mecanicos, null);
                        builder.setView(ModalRedondeado(view.getContext(), customView));
                        AlertDialog dialogMaquinas = builder.create();
                        dialogMaquinas.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogMaquinas.show();


                        ContenedorSinInternet= customView.findViewById(R.id.ContenedorSinInternet);
                        ContenedorContenido= customView.findViewById(R.id.ContenedorContenido);

                        RecyclerView RecyclerViewMecanicos = customView.findViewById(R.id.RecyclerViewMecanicos);


                        RecyclerViewMecanicos.setLayoutManager(new LinearLayoutManager(context));
                        adaptadorMecanicoAMaquina = new AdaptadorMecanicoAMaquina(listaMecanicos, context, actionListener, idmaquina, dialogMaquinas);
                        RecyclerViewMecanicos.setAdapter(adaptadorMecanicoAMaquina);

                        MostrarMecanicos();



                    } else {


                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.opciones_herramienta, null);
                        builder.setView(ModalRedondeado(view.getContext(), customView));
                        AlertDialog dialogMaquinas = builder.create();
                        dialogMaquinas.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogMaquinas.show();

                        ImageView imagenHerramienta = customView.findViewById(R.id.imagenHerramienta);
                        TextView textViewTitulo = customView.findViewById(R.id.textViewTitulo);


                        String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/maquinas/" + foto;
                        Glide.with(holder.itemView.getContext())
                                .load(imageUrl)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .placeholder(R.drawable.baseline_precision_manufacturing_24)
                                .error(R.drawable.baseline_precision_manufacturing_24)
                                .into(imagenHerramienta);

                        textViewTitulo.setText(nombre);


                        LinearLayout LayoutEliminar = customView.findViewById(R.id.LayoutEliminar);
                        LinearLayout LayoutCambiarFoto = customView.findViewById(R.id.LayoutCambiarFoto);


                        LayoutCambiarFoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                actionListenerMaquinas.onActualizarFoto(idmaquina,dialogMaquinas );
                            }
                        });


                        LayoutEliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion, null);
                                builder.setView(ModalRedondeado(view.getContext(), customView));
                                AlertDialog dialogConfirmacion = builder.create();
                                dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogConfirmacion.show();

                                TextView textView4 = customView.findViewById(R.id.textView4);
                                textView4.setText("¿ Estas seguro que deseas eliminar " + nombre + " ?");


                                Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                                Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);


                                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialogConfirmacion.dismiss();
                                    }
                                });


                                buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialogMaquinas.dismiss();
                                        dialogConfirmacion.dismiss();
                                        actionListenerMaquinas.onEliminarMaquina(idmaquina);
                                    }
                                });

                            }
                        });


                    }


                    return false;
                }
            });


        } finally {

        }
    }


    List<JSONObject> listaMecanicos = new ArrayList<>();

    private void MostrarMecanicos() {
        listaMecanicos.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listaMecanicos.add(jsonObject);

                    }
                    adaptadorMecanicoAMaquina.notifyDataSetChanged();
                    adaptadorMecanicoAMaquina.setFilteredData(listaMecanicos);
                    adaptadorMecanicoAMaquina.filter("");


                    if (listaMecanicos.size() > 0) {

                           mostrarLayout("conContenido");
                    } else {
                        mostrarLayout("SinInternet");
                    }

                } catch (JSONException e) {
                        mostrarLayout("SinInternet");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                  mostrarLayout("SinInternet");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "48");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void mostrarLayout(String  estado){
        if (estado.equalsIgnoreCase("SinInternet")){
            ContenedorContenido.setVisibility(View.GONE);
            ContenedorSinInternet.setVisibility(View.VISIBLE);
        }else {

            ContenedorContenido.setVisibility(View.VISIBLE);
            ContenedorSinInternet.setVisibility(View.GONE);
        }

    }



    @Override
    public int getItemCount() {

        //return filteredData.size();
        return filteredData.size();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreMaquina;
        ImageView imageViewMaquina;


        TextView idmaquina;
        TextView tvMarcaModelo;
        TextView tvACargo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreMaquina = itemView.findViewById(R.id.nombreMaquina);
            imageViewMaquina = itemView.findViewById(R.id.imageViewMaquina);
            idmaquina = itemView.findViewById(R.id.idmaquina);
            tvMarcaModelo = itemView.findViewById(R.id.tvMarcaModelo);
            tvACargo = itemView.findViewById(R.id.tvACargo);
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


    public interface OnActivityActionListener {
        void onEliminarMaquina(String idmaquina);


        void onActualizarFoto(String idmaquina, AlertDialog dialogMaquinas);
    }

    AdaptadorMaquinas.OnActivityActionListener actionListenerMaquinas;
    AdaptadorMecanicoAMaquina.OnActivityActionListener actionListener;

    public AdaptadorMaquinas(List<JSONObject> data, Context context, AdaptadorMecanicoAMaquina.OnActivityActionListener actionListener, AdaptadorMaquinas.OnActivityActionListener actionListenerMaquinas) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.actionListener = actionListener;
        this.actionListenerMaquinas = actionListenerMaquinas;


    }


}
