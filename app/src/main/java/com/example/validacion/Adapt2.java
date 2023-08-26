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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class Adapt2 extends RecyclerView.Adapter<Adapt2.ViewHolder> {


    private static final int VIEW_TYPE_ERROR = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private Context context;
    private String Url = "http://192.168.1.252/georgioapi/Controllers/Apiback.php";

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

                String id_ser_venta= jsonObject2.optString("id_ser_venta", "");
                String id_ser_unidad= jsonObject2.optString("id_ser_unidad", "");
                String id_ser_cliente= jsonObject2.optString("id_ser_cliente", "");

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

                String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/" + jsonObject2.getString("foto");
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

                        Bundle bundle = new Bundle();

                        bundle.putString("marca", marca);
                        bundle.putString("modelo", modelo);
                        bundle.putString("motivo", motivo);
                        bundle.putString("fecha", fecha_ingreso);
                        bundle.putString("status", estatus);
                        bundle.putString("hora", hora_ingreso);

                        bundle.putString("idventa", id_ser_venta);

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
                        int[] location = new int[2];
                        view.getLocationOnScreen(location);

                        int centerX = location[0] + view.getWidth() / 2;
                        int centerY = location[1] + view.getHeight() / 2;

                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

                        int screenWidth = displayMetrics.widthPixels;
                        int screenHeight = displayMetrics.heightPixels;

                        centerX = screenWidth / 2;
                        centerY = screenHeight / 2;

                        PopupMenu popupMenu = new PopupMenu(context, view, Gravity.CENTER); // Set gravity to center
                        popupMenu.getMenuInflater().inflate(R.menu.menu_opciones, popupMenu.getMenu());

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {

                                Bundle bundle = new Bundle();
                                bundle.putString("marca", marca);
                                bundle.putString("modelo", modelo);
                                bundle.putString("motivo", motivo);
                                bundle.putString("fecha", fecha_ingreso);
                                bundle.putString("status", estatus);
                                bundle.putString("hora", hora_ingreso);
                                bundle.putString("idventa", id_ser_venta);

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
                                        intent.putExtra("marca", marca);
                                        intent.putExtra("id_ser_venta", id_ser_venta);
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
                                        // Acción para la opción 4
                                        return true;

                                    default:
                                        return false;
                                }
                            }
                        });

                        popupMenu.show();

                        // Adjusting the icon visibility
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

                        return true;
                    }
                });




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (getItemViewType(position) == VIEW_TYPE_ERROR && filteredData.isEmpty()){

            if (filteredData.isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(R.drawable.nointernet)
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
        TextView textMarca, textModelo, textPlaca, textDueño, textFecha, textStatus, TVNoInternet;

        ImageView imageViewCoches,IVNoInternet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textMarca = itemView.findViewById(R.id.textMarca);
            textModelo = itemView.findViewById(R.id.textModelo);
            textPlaca = itemView.findViewById(R.id.textPlaca);
            textDueño = itemView.findViewById(R.id.textDueño);
            textFecha = itemView.findViewById(R.id.textFecha);
            textStatus = itemView.findViewById(R.id.textStatus);
            imageViewCoches = itemView.findViewById(R.id.imageViewCoches);
            IVNoInternet= itemView.findViewById(R.id.IVNoInternet);
            TVNoInternet= itemView.findViewById(R.id.TVNoInternet);
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

                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(marca.contains(keyword) || modelo.contains(keyword) || placa.contains(keyword) ||
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


}
