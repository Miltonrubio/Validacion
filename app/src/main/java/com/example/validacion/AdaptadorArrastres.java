package com.example.validacion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
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
import java.io.IOException;
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

public class AdaptadorArrastres extends RecyclerView.Adapter<AdaptadorArrastres.ViewHolder> {


    private static final int VIEW_TYPE_ERROR = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private Context context;

    private List<JSONObject> filteredData;
    private List<JSONObject> data;

    public AdaptadorArrastres(List<JSONObject> data, Context context) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_arrastres, parent, false);
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
                String id = jsonObject2.optString("id", "");
                String id_cliente = jsonObject2.optString("id_cliente", "");
                String foto_mapa = jsonObject2.optString("foto_mapa", "");
                String fecha_inicio = jsonObject2.optString("fecha_inicio", "");
                String hora_inicio = jsonObject2.optString("hora_inicio", "");
                String fecha_final = jsonObject2.optString("fecha_final", "");
                String hora_final = jsonObject2.optString("hora_final", "");
                String kilometros = jsonObject2.optString("kilometros", "");
                String costo_kilometro = jsonObject2.optString("costo_kilometro", "");
                String importe = jsonObject2.optString("id_ser_venta", "");
                String observaciones = jsonObject2.optString("observaciones", "");
                String estatus = jsonObject2.optString("estatus", "");
                String direccion = jsonObject2.optString("direccion", "");
                String telefono = jsonObject2.optString("telefono", "");
                String cliente = jsonObject2.optString("cliente", "");

                Bundle bundle = new Bundle();

                bundle.putString("id", id);
                bundle.putString("id_cliente", id_cliente);
                bundle.putString("foto_mapa", foto_mapa);
                bundle.putString("fecha_inicio", fecha_inicio);
                bundle.putString("estatus", estatus);
                bundle.putString("hora_inicio", hora_inicio);
                bundle.putString("fecha_final", fecha_final);
                bundle.putString("hora_final", hora_final);
                bundle.putString("kilometros", kilometros);
                bundle.putString("costo_kilometro", costo_kilometro);
                bundle.putString("importe", importe);
                bundle.putString("observaciones", observaciones);
                bundle.putString("telefono", telefono);

             setTextViewText(holder.textDireccion, direccion, "Direccion no disponible");
                setTextViewText(holder.textDueño2, cliente, "Dueño no disponible");

                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date_inicio = inputFormat.parse(fecha_inicio);

                    SimpleDateFormat outputFormatFecha = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new DateFormatSymbols(new Locale("es", "ES")));
                    String fecha_formateada = outputFormatFecha.format(date_inicio);

                    SimpleDateFormat inputFormatHora = new SimpleDateFormat("HH:mm:ss");
                    Date time = inputFormatHora.parse(hora_inicio);

                    SimpleDateFormat outputFormatHora = new SimpleDateFormat("hh:mm a");
                    String hora_formateada_inicio = outputFormatHora.format(time);

                    setTextViewText(holder.textFecha2, fecha_formateada + ". A las " + hora_formateada_inicio, "Fecha no disponible");


                } catch (Exception e) {
                    e.printStackTrace();
                }

                setTextViewText(holder.textStatus, estatus, "Dueño no disponible");
                setTextViewText(holder.textTelefono, telefono, "Telefono no disponible");

                holder.botonDesplegable2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopupMenu(view, bundle);
                    }
                });


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DetallesArrastres detallesArrastres = new DetallesArrastres();
                        detallesArrastres.setArguments(bundle);

                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_layoutCoches, detallesArrastres)
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
            } finally {

            }
        } else if (getItemViewType(position) == VIEW_TYPE_ERROR && filteredData.isEmpty()) {

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
        TextView textFecha2, textStatus, textTelefono, textDueño2, textDireccion;

        ImageView IVNoInternet, botonDesplegable2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textFecha2 = itemView.findViewById(R.id.textFecha2);
            textStatus = itemView.findViewById(R.id.textStatus);
            IVNoInternet = itemView.findViewById(R.id.IVNoInternet);
            textTelefono = itemView.findViewById(R.id.textTelefono);
            textDueño2 = itemView.findViewById(R.id.textDueño2);
            textDireccion = itemView.findViewById(R.id.textDireccionDestino);
            botonDesplegable2 = itemView.findViewById(R.id.botonDesplegable2);
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
                    if (!(modelo.contains(keyword) || empresa.contains(keyword)|| direccion.contains(keyword) || telefono.contains(keyword) || id.contains(keyword) || placas.contains(keyword) ||
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


    private void showPopupMenu(View view, Bundle bundle) {
        PopupMenu popupMenu = new PopupMenu(context, view, Gravity.CENTER);
        popupMenu.getMenuInflater().inflate(R.menu.menu_opciones, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.opcionDetalles:
                        DetallesArrastres detallesArrastres = new DetallesArrastres();
                        detallesArrastres.setArguments(bundle);

                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_layoutCoches, detallesArrastres)
                                .addToBackStack(null)
                                .commit();
                        return true;
/*
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
*/
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
        if (text.equals(null) || text.equals("") || text.equals("null")) {
            textView.setText(defaultText);
        } else {
            textView.setText(text);
        }
    }

}
