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

public class AdaptadorHerramientas extends RecyclerView.Adapter<AdaptadorHerramientas.ViewHolder> {


    private Context context;

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    String url;
    String id_gabeta;


    AlertDialog DialogHerramientas;

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

            Bundle bundle = new Bundle();
            bundle.putString("nombre", nombre);
            bundle.putString("id_cajon", id_cajon);
            bundle.putString("foto", foto);
            bundle.putString("piezas", piezas);
            bundle.putString("idHerramienta", idHerramienta);

            holder.nombreHerramienta.setText(nombre);
            holder.piezas.setText(piezas + " Pzs");
            holder.descripcion.setText(descripcion);


            holder.mecanicoACargo.setVisibility(View.GONE);
            holder.NumCajon.setVisibility(View.GONE);
            holder.NoGaveta.setVisibility(View.GONE);


            String image = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/herramienta/" + foto;


            Glide.with(holder.itemView.getContext())
                    .load(image)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(R.drawable.baseline_construction_24)
                    .error(R.drawable.baseline_construction_24)
                    .into(holder.imageViewherramientas);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.opciones_herramienta, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogMostrarHerramientas = builder.create();
                    dialogMostrarHerramientas.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogMostrarHerramientas.show();


                    TextView textViewTitulo = customView.findViewById(R.id.textViewTitulo);
                    textViewTitulo.setText(nombre);

                    ImageView imagenHerramienta = customView.findViewById(R.id.imagenHerramienta);


                    String image = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/herramienta/" + foto;


                    Glide.with(view.getContext())
                            .load(image)
                            .skipMemoryCache(false)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .placeholder(R.drawable.baseline_construction_24)
                            .error(R.drawable.baseline_construction_24)
                            .into(imagenHerramienta);


                    LinearLayout LayoutEliminar = customView.findViewById(R.id.LayoutEliminar);
                    LinearLayout LayoutCambiarFoto = customView.findViewById(R.id.LayoutCambiarFoto);

                    LayoutCambiarFoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            actionListener.onAbrirCamera(idHerramienta, dialogMostrarHerramientas, DialogHerramientas, context);
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
                            textView4.setText("¿Estas seguro que deseas eliminar " + nombre + "?");

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

                                    dialogConfirmacion.dismiss();
                                    dialogMostrarHerramientas.dismiss();
                                    EliminarHerramienta(idHerramienta, nombre, id_cajon, id_gabeta);

                                }
                            });


                        }
                    });


                }
            });

        } finally {

        }
    }


    private void MostrarHerramientas(String id_gabeta, String id_cajon) {
        filteredData.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String estado = jsonObject.getString("inventario");

                        if (!estado.equalsIgnoreCase("eliminado")) {
                            filteredData.add(jsonObject);
                        }

                    }
                    notifyDataSetChanged();


                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   mostrarLayout("SinInternet");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "42");
                params.put("id_gabeta", id_gabeta);
                params.put("id_cajon", id_cajon);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void EliminarHerramienta(String idHerramienta, String nombre, String id_cajon, String id_gabeta) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se eliminó la herramienta " + nombre);
                notifyDataSetChanged();
                MostrarHerramientas(id_gabeta, id_cajon);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Hubo un error, revisa la conexión");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "44");
                params.put("idHerramienta", idHerramienta);
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
        TextView nombreHerramienta, piezas;

        ImageView imageViewherramientas;
        TextView descripcion;


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


    public interface OnActivityActionListener {
        void onAbrirCamera(String idHerramienta, AlertDialog dialogMostrarHerramientas, AlertDialog DialogHerramientas, Context context);
    }

    private AdaptadorHerramientas.OnActivityActionListener actionListener;


    public AdaptadorHerramientas(List<JSONObject> data, Context context, String id_gabeta, AdaptadorHerramientas.OnActivityActionListener actionListener, AlertDialog DialogHerramientas) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.id_gabeta = id_gabeta;
        this.actionListener = actionListener;
        this.DialogHerramientas = DialogHerramientas;


    }


}
