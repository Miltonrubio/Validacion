package com.example.validacion.Adaptadores;


import static android.app.PendingIntent.getActivity;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdaptadorClientes extends RecyclerView.Adapter<AdaptadorClientes.ViewHolder> {

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    Context context;

    String url;

    RecyclerView RecyclerViewUnidadesUsuario;
    AdaptadorUnidadesClientes AdaptadorUnidadesClientes;

    List<JSONObject> listaUnidades = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clientes, parent, false);
        return new ViewHolder(view);

    }

    ConstraintLayout LayoutSinInternet;
    LinearLayout LayoutConContenido;
    ConstraintLayout  LayoutSinContenido;


    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        url = context.getResources().getString(R.string.ApiBack);
        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String nombre = jsonObject2.optString("nombre", "");
            String telefono = jsonObject2.optString("telefono", "");
            String domicilio = jsonObject2.optString("domicilio", "");
            String id_ser_cliente = jsonObject2.optString("id_ser_cliente", "");

            setTextViewText(holder.textNombreUsuario, nombre, "No se encontro el nombre");
            setTextViewText(holder.textTelefonoUsuario, telefono, "No se encontro el telefono");
            setTextViewText(holder.textDireccionUsuario, domicilio, "No se encontro el domicilio");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_opciones_clientes, null);
                    LayoutSinInternet = customView.findViewById(R.id.LayoutSinInternet);
                    LayoutConContenido = customView.findViewById(R.id.LayoutConContenido);
                    LayoutSinContenido=customView.findViewById(R.id.LayoutSinContenido);

                    RecyclerViewUnidadesUsuario = customView.findViewById(R.id.RecyclerViewUnidadesUsuario);
                    TextView NombreclIENTE = customView.findViewById(R.id.NombreclIENTE);
                    ImageView btnAgregarUnidad = customView.findViewById(R.id.btnAgregarUnidad);

                    NombreclIENTE.setText("Unidades de " + nombre);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogConBotones = builder.create();
                    dialogConBotones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogConBotones.show();


                    RecyclerViewUnidadesUsuario.setLayoutManager(new LinearLayoutManager(context));
                    AdaptadorUnidadesClientes = new AdaptadorUnidadesClientes(listaUnidades, context);
                    RecyclerViewUnidadesUsuario.setAdapter(AdaptadorUnidadesClientes);

                    MostrarUnidadesClientes(id_ser_cliente);


                    btnAgregarUnidad.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.insertar_actividad, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogConBotones = builder.create();
                            dialogConBotones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogConBotones.show();


                        }
                    });

                }
            });

        } finally {
        }
    }


    private void MostrarUnidadesClientes(String id_ser_cliente) {
        listaUnidades.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                listaUnidades.add(jsonObject);
                            }

                            AdaptadorUnidadesClientes.notifyDataSetChanged();
                            AdaptadorUnidadesClientes.setFilteredData(listaUnidades);
                            AdaptadorUnidadesClientes.filter("");


                            if (listaUnidades.size() > 0) {
                                LayoutConContenido.setVisibility(View.VISIBLE);
                                LayoutSinInternet.setVisibility(View.GONE);
                                LayoutSinContenido.setVisibility(View.GONE);
                            } else {

                                LayoutSinContenido.setVisibility(View.VISIBLE);
                                LayoutConContenido.setVisibility(View.GONE);
                                LayoutSinInternet.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            LayoutSinContenido.setVisibility(View.VISIBLE);
                            LayoutConContenido.setVisibility(View.GONE);
                            LayoutSinInternet.setVisibility(View.GONE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LayoutConContenido.setVisibility(View.GONE);
                        LayoutSinContenido.setVisibility(View.GONE);
                        LayoutSinInternet.setVisibility(View.VISIBLE);
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "20");
                params.put("id_ser_cliente", id_ser_cliente);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    @Override
    public int getItemCount() {

        return filteredData.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNombreUsuario;
        TextView textDireccionUsuario;

        TextView textTelefonoUsuario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombreUsuario = itemView.findViewById(R.id.textNombreUsuario);
            textDireccionUsuario = itemView.findViewById(R.id.textDireccionUsuario);
            textTelefonoUsuario = itemView.findViewById(R.id.textTelefonoUsuario);
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
                String telefono = item.optString("telefono", "").toLowerCase();
                String domicilio = item.optString("domicilio", "").toLowerCase();
                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(nombre.contains(keyword) || telefono.contains(keyword) || domicilio.contains(keyword))) {
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


    public AdaptadorClientes(List<JSONObject> data, Context context) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
    }


}

