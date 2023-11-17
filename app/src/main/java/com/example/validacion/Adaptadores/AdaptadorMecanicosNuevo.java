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

public class AdaptadorMecanicosNuevo extends RecyclerView.Adapter<AdaptadorMecanicosNuevo.ViewHolder> {


    private Context context;

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    String url;
    private AdaptadorMecanicosNuevo.OnActivityActionListener actionListener;

    AlertDialog dialogMecanicos;
    AlertDialog dialogOpcionesGaveta;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mecanicos, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        url = context.getResources().getString(R.string.ApiBack);

        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String foto = jsonObject2.optString("foto", "");
            String idusuario = jsonObject2.optString("idusuario", "");
            String nombre = jsonObject2.optString("nombre", "");


            String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/usuarios/" + foto;


            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.mecanico)
                    .error(R.drawable.mecanico)
                    .into(holder.imageViewMecanico);

            holder.fechaMecanico.setVisibility(View.GONE);

            holder.nombreMecanico.setText(nombre);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogConfirmacion = builder.create();
                    dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogConfirmacion.show();

                    TextView textView4 = customView.findViewById(R.id.textView4);
                    textView4.setText("¿ Estas seguro que deseas asignar a " + nombre + " esta gaveta ?");


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
                            dialogMecanicos.dismiss();
                            dialogOpcionesGaveta.dismiss();
                            actionListener.onAsignarMecanico(idusuario, id_gabeta, nombre);


                        }
                    });

                }
            });

        } finally {

        }
    }


    @Override
    public int getItemCount() {

        //return filteredData.size();
        return filteredData.size();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fechaMecanico, nombreMecanico;

        ImageView imageViewMecanico;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreMecanico = itemView.findViewById(R.id.nombreMecanico);
            fechaMecanico = itemView.findViewById(R.id.fechaMecanico);
            imageViewMecanico = itemView.findViewById(R.id.imageViewMecanico);
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


    String id_gabeta;


    public interface OnActivityActionListener {
        void onAsignarMecanico(String id_mecanico, String id_gabeta, String nombreMecanico);
    }


    public AdaptadorMecanicosNuevo(List<JSONObject> data, Context context, String id_gabeta, AdaptadorMecanicosNuevo.OnActivityActionListener actionListener, AlertDialog dialogMecanicos, AlertDialog dialogOpcionesGaveta) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.id_gabeta = id_gabeta;
        this.dialogOpcionesGaveta = dialogOpcionesGaveta;
        this.dialogMecanicos = dialogMecanicos;
        this.actionListener = actionListener;
    }


}
