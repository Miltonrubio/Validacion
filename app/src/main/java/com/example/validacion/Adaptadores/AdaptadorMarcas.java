package com.example.validacion.Adaptadores;


import static android.app.PendingIntent.getActivity;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;
import static com.example.validacion.Adaptadores.Utiles.crearToastPersonalizado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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


public class AdaptadorMarcas extends RecyclerView.Adapter<AdaptadorMarcas.ViewHolder> {

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    Context context;

    String url;

    List<JSONObject> listaModelos = new ArrayList<>();

    Bundle bundleUsuario;
    AdaptadorModelos.OnActivityActionListener actionListener;

    AlertDialog dialogUnidadesDeCliente;
    AlertDialog dialogAgregarUnidad;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unidades_cliente, parent, false);
        return new ViewHolder(view);

    }


    ConstraintLayout LayoutSeleccionarMarca;
    ConstraintLayout LayoutAgregarDatos;
    ConstraintLayout LayoutSeleccionarModelo;
    RecyclerView reciclerViewModelos;
    AdaptadorModelos adaptadorModelos;


    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        url = context.getResources().getString(R.string.ApiBack);

        String nombreUsuario = bundleUsuario.getString("nombre");
        String id_ser_cliente = bundleUsuario.getString("id_ser_cliente");

        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String marca = jsonObject2.optString("name", "");
            String id_car_make = jsonObject2.optString("id_car_make", "");
            setTextViewText(holder.NombreUnidad, marca, "No se encontro el modelo");


            Bundle bundle = new Bundle();
            bundle.putString("nombreUsuario", nombreUsuario);
            bundle.putString("id_car_make", id_car_make);
            bundle.putString("marca", marca);
            bundle.putString("id_ser_cliente", id_ser_cliente);


            holder.imagenCarrito.setVisibility(View.GONE);

            holder.LayoutAgregarServicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_agregar_unidad, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogMarcas = builder.create();
                    dialogMarcas.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogMarcas.show();

                    VerModelos(id_car_make);

                    TextView textView3 = customView.findViewById(R.id.text);
                    textView3.setText("SELECCIONA EL MODELO PARA " + marca.toUpperCase());


                    LayoutSeleccionarMarca = customView.findViewById(R.id.SeleccionarMarca);
                    LayoutSeleccionarModelo = customView.findViewById(R.id.SeleccionarModelo);
                    ConstraintLayout yourConstraintLayoutId = customView.findViewById(R.id.yourConstraintLayoutId);
                    yourConstraintLayoutId.setVisibility(View.VISIBLE);

                    EditText searchEditText = customView.findViewById(R.id.searchEditText);
                    searchEditText.setHint("Buscar el modelo");


                    LayoutAgregarDatos = customView.findViewById(R.id.LayoutAgregarDatos);
                    LayoutSeleccionarModelo.setVisibility(View.VISIBLE);
                    LayoutSeleccionarMarca.setVisibility(View.GONE);
                    LayoutAgregarDatos.setVisibility(View.GONE);
                    reciclerViewModelos = customView.findViewById(R.id.reciclerViewModelos);


                    adaptadorModelos = new AdaptadorModelos(listaModelos, customView.getContext(), bundle, actionListener, dialogUnidadesDeCliente, dialogAgregarUnidad, dialogMarcas);
                    reciclerViewModelos.setLayoutManager(new LinearLayoutManager(customView.getContext()));
                    reciclerViewModelos.setAdapter(adaptadorModelos);

                    searchEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adaptadorModelos.filter(s.toString().toLowerCase());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                }
            });

        } finally {
        }
    }


    private void VerModelos(String id) {
        listaModelos.clear(); // Limpia la lista antes de agregar los nuevos
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id_car_make = jsonObject.getString("id_car_make");
                        if (id_car_make.equalsIgnoreCase(id)) {
                            listaModelos.add(jsonObject);
                        }


                        adaptadorModelos.setFilteredData(listaModelos);
                        adaptadorModelos.filter("");

                    }
                } catch (JSONException e) {
                    crearToastPersonalizado(context, "Error al cargar los datos");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                crearToastPersonalizado(context, "Error al cargar los datos");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "33");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    @Override
    public int getItemCount() {

        return filteredData.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView NombreUnidad;
        ImageView imagenCarrito;
        LinearLayout LayoutAgregarServicio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            NombreUnidad = itemView.findViewById(R.id.NombreUnidad);
            LayoutAgregarServicio = itemView.findViewById(R.id.LayoutAgregarServicio);
            imagenCarrito = itemView.findViewById(R.id.imagenCarrito);
        }
    }

    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(data);
        } else {
            String[] keywords = query.toLowerCase().split(" ");

            for (JSONObject item : data) {
                String nombre = item.optString("name", "").toLowerCase();
                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(nombre.contains(keyword))) {
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


    public AdaptadorMarcas(List<JSONObject> data, Context context, Bundle bundle, AdaptadorModelos.OnActivityActionListener actionListener, AlertDialog dialogUnidadesDeCliente, AlertDialog dialogAgregarUnidad) {
        this.data = data;
        this.bundleUsuario = bundle;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.actionListener = actionListener;
        this.dialogUnidadesDeCliente = dialogUnidadesDeCliente;
        this.dialogAgregarUnidad = dialogAgregarUnidad;
    }


}

