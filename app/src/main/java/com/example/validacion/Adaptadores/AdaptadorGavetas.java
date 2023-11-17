package com.example.validacion.Adaptadores;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.validacion.DetallesArrastres;
import com.example.validacion.R;
import com.itextpdf.text.pdf.parser.Line;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdaptadorGavetas extends RecyclerView.Adapter<AdaptadorGavetas.ViewHolder> {


    private Context context;

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    String url;

    AdaptadorCajones adaptadorCajones;

/*
    List<JSONObject> listaCajones = new ArrayList<>();
    RecyclerView reciclerViewCajones;
    LottieAnimationView lottieNoCajones;
    TextView textViewSinContenido;
    LottieAnimationView lottieNoInternet;
*/

    AdaptadorHerramientas.OnActivityActionListener actionListener;
    AdaptadorMecanicosNuevo.OnActivityActionListener actionListenerMecanicos;

    public AdaptadorGavetas(List<JSONObject> data, Context context, AdaptadorHerramientas.OnActivityActionListener actionListener, AdaptadorGavetas.OnActivityActionListener actionListenerGaveta, AdaptadorMecanicosNuevo.OnActivityActionListener actionListenerMecanicos) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.actionListener = actionListener;
        this.actionListenerGaveta = actionListenerGaveta;
        this.actionListenerMecanicos = actionListenerMecanicos;
    }

    AdaptadorMecanicosNuevo adaptadorMecanicosNuevo;

    private AdaptadorGavetas.OnActivityActionListener actionListenerGaveta;

    public interface OnActivityActionListener {
        void onEliminarGaveta(String id_gabeta, String nombre);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gabetas, parent, false);
        return new ViewHolder(view);

    }


    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        List<JSONObject> listaDeCajones = new ArrayList<>();
        url = context.getResources().getString(R.string.ApiBack);


        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String id_gabeta = jsonObject2.optString("id_gabeta", "");
            String nombre_gaveta = jsonObject2.optString("nombre_gaveta", "");
            String nombre_mecanico = jsonObject2.optString("nombre_mecanico", "");
            String cajones = jsonObject2.optString("cajones", "");


            listaDeCajones.clear();

            try {
                JSONArray listaCajones = new JSONArray(cajones);

                for (int i = 0; i < listaCajones.length(); i++) {
                    JSONObject jsonObject = listaCajones.getJSONObject(i);
                    listaDeCajones.add(jsonObject);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }


            holder.reciclerViewCajonesInterno.setLayoutManager(new LinearLayoutManager(context));
            adaptadorCajones = new AdaptadorCajones(listaDeCajones, context, id_gabeta, actionListener);
            holder.reciclerViewCajonesInterno.setAdapter(adaptadorCajones);

            adaptadorCajones.notifyDataSetChanged();
            adaptadorCajones.setFilteredData(listaDeCajones);
            adaptadorCajones.filter("");


            Bundle bundle = new Bundle();
            bundle.putString("id_gabeta", id_gabeta);
            bundle.putString("nombre", nombre_gaveta);


            setTextViewText(holder.tituloGaveta, nombre_gaveta, "No se encontro el nombre de gaveta");
            setTextViewText(holder.DueñoGaveta, nombre_mecanico, "Aun no se asigna un mecanico");
            holder.txtId.setText("id " + id_gabeta);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_opciones_gavetas, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogOpcionesGaveta = builder.create();
                    dialogOpcionesGaveta.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogOpcionesGaveta.show();


                    LinearLayout LayoutAsignarMecanico = customView.findViewById(R.id.LayoutAsignarMecanico);
                    LinearLayout LayoutEliminarGaveta = customView.findViewById(R.id.LayoutEliminarGaveta);

                    if (nombre_mecanico.isEmpty() || nombre_mecanico.equals("null") || nombre_mecanico.equals(null)) {
                        LayoutAsignarMecanico.setVisibility(View.VISIBLE);
                    } else {
                        LayoutAsignarMecanico.setVisibility(View.GONE);
                    }


                    LayoutEliminarGaveta.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogConfirmacion = builder.create();
                            dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogConfirmacion.show();

                            TextView textView4 = customView.findViewById(R.id.textView4);
                            textView4.setText("¿ Estas seguro que deseas eliminar la gaveta " + nombre_gaveta + " ?");


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
                                    dialogOpcionesGaveta.dismiss();
                                    dialogConfirmacion.dismiss();
                                    actionListenerGaveta.onEliminarGaveta(id_gabeta, nombre_gaveta);
                                }
                            });


                        }
                    });


                    LayoutAsignarMecanico.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_ver_mecanicos, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogMecanicos = builder.create();
                            dialogMecanicos.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogMecanicos.show();

                            RecyclerView RecyclerViewMecanicos = customView.findViewById(R.id.RecyclerViewMecanicos);


                            RecyclerViewMecanicos.setLayoutManager(new LinearLayoutManager(context));
                            adaptadorMecanicosNuevo = new AdaptadorMecanicosNuevo(listaMecanicos, context, id_gabeta, actionListenerMecanicos, dialogMecanicos, dialogOpcionesGaveta);
                            RecyclerViewMecanicos.setAdapter(adaptadorMecanicosNuevo);

                            MostrarMecanicos();
                        }
                    });


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
                    adaptadorMecanicosNuevo.notifyDataSetChanged();
                    adaptadorMecanicosNuevo.setFilteredData(listaMecanicos);
                    adaptadorMecanicosNuevo.filter("");


                    if (listaMecanicos.size() > 0) {

                        //      mostrarLayout("conContenido");
                    } else {

                        //           mostrarLayout("SinContenido");
                    }

                } catch (JSONException e) {
                    //       mostrarLayout("SinContenido");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  mostrarLayout("SinInternet");
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

/*
    private void mostrarLayout(String estado) {


        if (estado.equalsIgnoreCase("conContenido")) {
            reciclerViewCajones.setVisibility(View.VISIBLE);
            lottieNoCajones.setVisibility(View.GONE);
            textViewSinContenido.setVisibility(View.GONE);
            lottieNoInternet.setVisibility(View.GONE);

        } else if (estado.equalsIgnoreCase("SinInternet")) {

            reciclerViewCajones.setVisibility(View.GONE);
            lottieNoCajones.setVisibility(View.GONE);
            textViewSinContenido.setVisibility(View.VISIBLE);
            lottieNoInternet.setVisibility(View.VISIBLE);
            textViewSinContenido.setText("No hay conexión a internet");


        } else {

            lottieNoInternet.setVisibility(View.GONE);
            reciclerViewCajones.setVisibility(View.GONE);
            lottieNoCajones.setVisibility(View.VISIBLE);
            textViewSinContenido.setVisibility(View.VISIBLE);
            textViewSinContenido.setText("No hay cajones en esta gaveta");

        }

    }
*/

    @Override
    public int getItemCount() {

        //return filteredData.size();
        return filteredData.size();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tituloGaveta;
        TextView DueñoGaveta;
        ConstraintLayout LayoutGaveta;
        TextView txtId;
        RecyclerView reciclerViewCajonesInterno;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            DueñoGaveta = itemView.findViewById(R.id.DueñoGaveta);
            tituloGaveta = itemView.findViewById(R.id.tituloGaveta);
            reciclerViewCajonesInterno = itemView.findViewById(R.id.reciclerViewCajones);
            LayoutGaveta = itemView.findViewById(R.id.LayoutGaveta);
            txtId = itemView.findViewById(R.id.txtId);
        }
    }

    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(data);
        } else {
            String[] keywords = query.toLowerCase().split(" ");

            for (JSONObject item : data) {
                String nombre_gaveta = item.optString("nombre_gaveta", "").toLowerCase();
                String nombre_mecanico = item.optString("nombre_mecanico", "").toLowerCase();
                String id_gabeta = item.optString("id_gabeta", "").toLowerCase();


                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(nombre_gaveta.contains(keyword) || nombre_mecanico.contains(keyword) || id_gabeta.contains(keyword))) {
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


    private void setTextViewText(TextView textView, String text, String defaultText) {
        if (text.equals(null) || text.equals("") || text.equals(":null") || text.equals("null") || text.isEmpty()) {
            textView.setText(defaultText);
        } else {
            textView.setText(text);
        }
    }

}
