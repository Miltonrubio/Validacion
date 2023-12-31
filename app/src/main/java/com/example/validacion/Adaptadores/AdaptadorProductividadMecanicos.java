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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.validacion.ActividadesFragment;
import com.example.validacion.Activity_Binding;
import com.example.validacion.DetalleFragment;
import com.example.validacion.DetallesActividadesFragment;
import com.example.validacion.R;
import com.example.validacion.SubirFotosUnidadesActivity;
import com.itextpdf.text.pdf.parser.Line;

import org.checkerframework.common.returnsreceiver.qual.This;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class AdaptadorProductividadMecanicos extends RecyclerView.Adapter<AdaptadorProductividadMecanicos.ViewHolder> {

    private Context context;

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    String url;
    FragmentManager fragmentManager;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuarios, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String foto = jsonObject2.optString("foto", "");
            String idusuario = jsonObject2.optString("idusuario", "");
            String telefono = jsonObject2.optString("telefono", "");
            String nombre = jsonObject2.optString("nombre", "");
            String permisos = jsonObject2.optString("permisos", "");


            Bundle bundle = new Bundle();
            bundle.putString("idusuario", idusuario);
            bundle.putString("nombre", nombre);

            String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/usuarios/" + foto;


            holder.cargoMec.setText(permisos.toUpperCase());
            holder.telefonoMec.setText(telefono);


            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.mecanico)
                    .error(R.drawable.mecanico)
                    .into(holder.ImagenMecanico);

            holder.NombreMecanico.setText(nombre.toUpperCase());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_opciones_actividades, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogOpcionesActividades = builder.create();
                    dialogOpcionesActividades.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogOpcionesActividades.show();

                    LinearLayout LyoutConsultarReportes = customView.findViewById(R.id.LyoutConsultarReportes);
                    LinearLayout LayoutGenerarRegistro = customView.findViewById(R.id.LayoutGenerarRegistro);


                    LyoutConsultarReportes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            DetallesActividadesFragment detallesActividadesFragment = new DetallesActividadesFragment();
                            detallesActividadesFragment.setArguments(bundle);

                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_layoutCoches, detallesActividadesFragment)
                                    .addToBackStack(null)
                                    .commit();
                            dialogOpcionesActividades.dismiss();
                        }
                    });

                    LayoutGenerarRegistro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            actionListener.onValidarCheckDiario(idusuario, view, nombre);

                        }
                    });


                }
            });


            /*

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Utiles.RedirigirAFragment(fragmentManager, new ActividadesFragment(), bundle);
                }
            });
*/

        } finally {

        }
    }

/*

    public void ValidarCheckDiario(String ID_usuario, View view, String nombre) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equalsIgnoreCase("\"Agregado\"")) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_check_actividades, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogConBotones = builder.create();
                    dialogConBotones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogConBotones.show();


                    RecyclerView reciclerViewActividades = customView.findViewById(R.id.reciclerViewActividades);
                    TextView textView20 = customView.findViewById(R.id.textView20);
                    Button button = customView.findViewById(R.id.button);
                    textView20.setText("Listado de actividades de " + nombre);

                    adaptadorCheckActividades = new AdaptadorCheckActividades(listadoActividades, context, response);

                    LinearLayoutManager linearLayout = new LinearLayoutManager(context);
                    reciclerViewActividades.setLayoutManager(linearLayout);
                    reciclerViewActividades.setAdapter(adaptadorCheckActividades);
                    CheckActividades();


                } else {

                    Utiles.crearToastPersonalizado(context, "Se creo el registro de actividades");

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Error al cargar");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "74");
                params.put("fecha", fechaActual);
                params.put("ID_usuario", ID_usuario);


                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }
 */


    @Override
    public int getItemCount() {

        //return filteredData.size();
        return filteredData.size();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView NombreMecanico;

        ImageView ImagenMecanico;

        TextView cargoMec;
        TextView telefonoMec;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            NombreMecanico = itemView.findViewById(R.id.NombreMecanico);
            ImagenMecanico = itemView.findViewById(R.id.ImagenMecanico);
            cargoMec = itemView.findViewById(R.id.cargoMec);
            telefonoMec = itemView.findViewById(R.id.telefonoMec);
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
        if (filteredData.isEmpty()) {
            actionListener.onFilterData(false); // Indica que no hay resultados
        } else {
            actionListener.onFilterData(true); // Indica que hay resultados
        }

        notifyDataSetChanged();
    }

    public void setFilteredData(List<JSONObject> filteredData) {
        this.filteredData = new ArrayList<>(filteredData);
        notifyDataSetChanged();
    }


    public interface OnActivityActionListener {
        void onFilterData(Boolean resultados);

        void onValidarCheckDiario(String ID_usuario, View view, String nombre);

    }

    private AdaptadorProductividadMecanicos.OnActivityActionListener actionListener;


    public AdaptadorProductividadMecanicos(List<JSONObject> data, Context context, AdaptadorProductividadMecanicos.OnActivityActionListener actionListener) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.actionListener = actionListener;
        url = context.getResources().getString(R.string.ApiBack);
    }


}
