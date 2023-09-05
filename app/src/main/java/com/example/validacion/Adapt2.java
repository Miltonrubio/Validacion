package com.example.validacion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

public class Adapt2 extends RecyclerView.Adapter<Adapt2.ViewHolder> {
    private static final int VIEW_TYPE_ERROR = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private Context context;
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
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coches, parent, false);
            return new ViewHolder(view);
        } else {

            View errorView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_error, parent, false);
            return new ViewHolder(errorView);
        }

    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            JSONObject jsonObject2 = filteredData.get(position);
            String id_ref = jsonObject2.optString("id_ser_refacciones", "");
            String id_check_mecanico = jsonObject2.optString("id_check_mecanico", "");
            String foto = jsonObject2.optString("foto", "");
            String marca = jsonObject2.optString("marcaI", "");
            String modelo = jsonObject2.optString("modeloI", "");
            String placa = jsonObject2.optString("placasI", "");
            String dueño = jsonObject2.optString("nombre", "");
            String motivo = jsonObject2.optString("motivoingreso", "");
            String estatus = jsonObject2.optString("estatus", "");
            String fecha_ingreso = jsonObject2.optString("fecha_ingreso", "");
            String hora_ingreso = jsonObject2.optString("hora_ingreso", "");
            String id_ser_venta = jsonObject2.optString("id_ser_venta", "");

            String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/" + foto;

            Bundle bundle = new Bundle();
            bundle.putString("marca", marca);
            bundle.putString("modelo", modelo);
            bundle.putString("motivo", motivo);
            bundle.putString("fecha", fecha_ingreso);
            bundle.putString("status", estatus);
            bundle.putString("hora", hora_ingreso);
            bundle.putString("idventa", id_ser_venta);

/*
            if (TextUtils.isEmpty(foto) || foto.equals("null")) {
                holder.IconoCamara.setVisibility(View.VISIBLE);
            } else {
               holder.IconoCamara.setVisibility(View.GONE);
            }

            if (TextUtils.isEmpty(id_check_mecanico) || id_check_mecanico.equals("null") || id_check_mecanico.equals("0")) {

                holder.IconoMecanico.setVisibility(View.VISIBLE);
            } else {
                holder.IconoMecanico.setVisibility(View.GONE);
            }
*/
            setTextViewText(holder.textMarca, marca.toUpperCase() + " - " + modelo.toUpperCase(), "Marca no disponible");
            setTextViewText(holder.textModelo, motivo.toUpperCase(), "Motivo no disponible");
            setTextViewText(holder.textPlaca, placa, "Placa no disponible");
            setTextViewText(holder.textDueño, dueño, "Dueño no disponible");
            setStatusTextView(holder.textStatus, estatus);

            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date_inicio = inputFormat.parse(fecha_ingreso);

                SimpleDateFormat outputFormatFecha = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new DateFormatSymbols(new Locale("es", "ES")));
                String fecha_formateada = outputFormatFecha.format(date_inicio);

                SimpleDateFormat inputFormatHora = new SimpleDateFormat("HH:mm:ss");
                Date time = inputFormatHora.parse(hora_ingreso);

                SimpleDateFormat outputFormatHora = new SimpleDateFormat("hh:mm a");
                String hora_formateada_inicio = outputFormatHora.format(time);
                setTextViewText(holder.textFecha, fecha_formateada + ". A las " + hora_formateada_inicio, "Fecha no disponible");
            } catch (Exception e) {
                e.printStackTrace();
            }

            loadImage(holder.imageViewCoches, imageUrl);

            holder.botonDesplegable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view, bundle);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AdaptadorRefacciones adaptadorRefacciones = new AdaptadorRefacciones(new ArrayList<>());

                    DetalleFragment detalleFragment = new DetalleFragment();
                    detalleFragment.setArguments(bundle);

                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layoutCoches, detalleFragment)
                            .addToBackStack(null)
                            .commit();

                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showPopupMenu(view, bundle);
                    return true;
                }
            });
        } else if (getItemViewType(position) == VIEW_TYPE_ERROR && filteredData.isEmpty()) {

            if (filteredData.isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(R.drawable.nocoches)
                        .into(holder.IVNoInternet);
            }
        }
    }

    @Override
    public int getItemCount() {

        //return filteredData.size();
        return filteredData.isEmpty() ? 1 : filteredData.size();

    }

    @Override
    public int getItemViewType(int position) {
        return filteredData.isEmpty() ? VIEW_TYPE_ERROR : VIEW_TYPE_ITEM;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textMarca, textModelo, textPlaca, textDueño, textFecha, textStatus;

        ImageView imageViewCoches, IVNoInternet, botonDesplegable, IconoCamara, IconoMecanico;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textMarca = itemView.findViewById(R.id.textMarca);
            textModelo = itemView.findViewById(R.id.textModelo);
            textPlaca = itemView.findViewById(R.id.textPlaca);
            textDueño = itemView.findViewById(R.id.textDueño);
            textFecha = itemView.findViewById(R.id.textFecha);
            textStatus = itemView.findViewById(R.id.textStatus);
            imageViewCoches = itemView.findViewById(R.id.imageViewCoches);
            IVNoInternet = itemView.findViewById(R.id.IVNoInternet);
         //   IconoMecanico = itemView.findViewById(R.id.IconoMecanico);
        //    IconoCamara = itemView.findViewById(R.id.IconoCamara);
            botonDesplegable = itemView.findViewById(R.id.botonDesplegable);
        }
    }

    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(data);
        } else {
            String[] keywords = query.toLowerCase().split(" ");

            for (JSONObject item : data) {
                String marca = item.optString("marcaI", "").toLowerCase();
                String modelo = item.optString("modeloI", "").toLowerCase();
                String nombre = item.optString("nombre", "").toLowerCase();
                String status = item.optString("estatus", "").toLowerCase();
                String placa = item.optString("placasI", "").toLowerCase();
                String fecha = item.optString("fecha_ingreso", "").toLowerCase();

                String hora = item.optString("hora_ingreso", "").toLowerCase();

                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(marca.contains(keyword) || modelo.contains(keyword) || placa.contains(keyword) || fecha.contains(keyword) || hora.contains(keyword) ||
                            nombre.contains(keyword) || status.contains(keyword))) {
                        matchesAllKeywords = false;
                        break; // Si alguna palabra clave no coincide, no es necesario verificar más
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


    private void showPopupMenu(View view, Bundle bundle) {
        PopupMenu popupMenu = new PopupMenu(context, view, Gravity.CENTER);
        popupMenu.getMenuInflater().inflate(R.menu.menu_opciones, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.opcionDetalles:
                        DetalleFragment detalleFragment = new DetalleFragment();
                        detalleFragment.setArguments(bundle);

                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_layoutCoches, detalleFragment)
                                .addToBackStack(null)
                                .commit();
                        return true;

                    case R.id.opcionFotos:
                        Intent intent = new Intent(context, Prueba.class);
                        intent.putExtra("marca", bundle.getString("marca"));
                        intent.putExtra("id_ser_venta", bundle.getString("idventa"));
                        context.startActivity(intent);
                        return true;

                    case R.id.opcionCheck:
                        CheckListFragment checkListFragment = new CheckListFragment();
                        checkListFragment.setArguments(bundle);

                        FragmentManager fragmentManagerCheck = ((AppCompatActivity) context).getSupportFragmentManager();
                        fragmentManagerCheck.beginTransaction()
                                .replace(R.id.frame_layoutCoches, checkListFragment)
                                .addToBackStack(null)
                                .commit();
                        return true;

                    case R.id.opcionCancelar:
                        return true;

                    default:
                        return false;
                }
            }
        });

        popupMenu.show();

        try {
            Field mPopup = PopupMenu.class.getDeclaredField("mPopup");
            mPopup.setAccessible(true);
            Object menuPopupHelper = mPopup.get(popupMenu);
            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
            setForceIcons.invoke(menuPopupHelper, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTextViewText(TextView textView, String text, String defaultText) {
        if (text.equals(null) || text.equals("") || text.equals("null") || text.isEmpty()) {
            textView.setText(defaultText);
        } else {
            textView.setText(text);
        }
    }

    private void setStatusTextView(TextView textView, String status) {
        if (!status.equals("null")) {
            textView.setText(status.toUpperCase());

            if (status.equalsIgnoreCase("pendiente")) {
                textView.setBackgroundResource(R.drawable.textview_outline3);
            } else if (status.equalsIgnoreCase("Finalizado")) {

                textView.setBackgroundResource(R.drawable.textview_outline4);
            } else if (status.equalsIgnoreCase("Servicios programado")) {

                textView.setBackgroundResource(R.drawable.textview_outline4);
            } else if (status.equalsIgnoreCase("Cita programada")) {

                textView.setBackgroundResource(R.drawable.textview_outline2);
            } else if (status.equalsIgnoreCase("Diagnostico")) {

                textView.setBackgroundResource(R.drawable.textview_outline5);
            } else if (status.equalsIgnoreCase("En espera")) {

                textView.setBackgroundResource(R.drawable.textview_outlinegris);
            } else if (status.equalsIgnoreCase("En servicio")) {
                textView.setBackgroundResource(R.drawable.textview_outline3);
            } else if (status.equalsIgnoreCase("Prueba de ruta")) {

                textView.setBackgroundResource(R.drawable.textview_outlinenegro);
                int colorBlanco = ContextCompat.getColor(context, R.color.white);
                textView.setTextColor(colorBlanco);

            } else if (status.equalsIgnoreCase("Lavado y detallado")) {

                textView.setBackgroundResource(R.drawable.textview_outline2);
            } else if (status.equalsIgnoreCase("Listo para entrega")) {

                textView.setBackgroundResource(R.drawable.textview_outline4);
            } else if (status.equalsIgnoreCase("Entregado")) {

                textView.setBackgroundResource(R.drawable.textview_outline4);
            } else {
                textView.setBackgroundResource(R.drawable.textview_outline2);
            }
        } else {
            textView.setText("Status no disponible");
            textView.setBackgroundResource(R.drawable.textview_outline5);
        }
    }

    private void loadImage(ImageView imageView, String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(imageView.getContext())
                    .load(imageUrl)
                    .error(R.drawable.default_image)
                    .into(imageView);
        } else {
            Glide.with(imageView.getContext())
                    .load(R.drawable.default_image)
                    .into(imageView);
        }
    }

}
