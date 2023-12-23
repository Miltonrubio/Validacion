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

import org.checkerframework.common.returnsreceiver.qual.This;
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


    List<JSONObject> listaMarcas = new ArrayList<>();
    List<JSONObject> listaModelos = new ArrayList<>();

    AdaptadorModelos.OnActivityActionListener actionListener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clientes, parent, false);
        return new ViewHolder(view);

    }

    ConstraintLayout LayoutSinInternet;
    ConstraintLayout LayoutConContenido;
    ConstraintLayout LayoutSinContenido;


    RecyclerView reciclerViewMarcas;
    ConstraintLayout LayoutSeleccionarMarca;
    ConstraintLayout LayoutSeleccionarModelo;
    ConstraintLayout LayoutAgregarDatos;
    AdaptadorMarcas adaptadorMarcas;

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        url = context.getResources().getString(R.string.ApiBack);
        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String nombre = jsonObject2.optString("nombre", "");
            String telefono = jsonObject2.optString("telefono", "");
            String domicilio = jsonObject2.optString("domicilio", "");
            String id_ser_cliente = jsonObject2.optString("id_ser_cliente", "");


            Bundle bundle = new Bundle();
            bundle.putString("nombre", nombre);
            bundle.putString("id_ser_cliente", id_ser_cliente);

            setTextViewText(holder.textNombreUsuario, nombre.toUpperCase(), "No se encontro el nombre");
            setTextViewText(holder.textTelefonoUsuario, telefono, "No se encontro el telefono");
            setTextViewText(holder.textDireccionUsuario, domicilio, "No se encontro el domicilio");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_opciones_clientes, null);
                    LayoutSinInternet = customView.findViewById(R.id.LayoutSinInternet);
                    LayoutConContenido = customView.findViewById(R.id.LayoutConContenido);
                    LayoutSinContenido = customView.findViewById(R.id.LayoutSinContenido);

                    ImageView btnAgregarUnidad = customView.findViewById(R.id.btnAgregarUnidad);
                    ImageView AgregarNuevaUnidad = customView.findViewById(R.id.AgregarNuevaUnidad);

                    EditText searchEditText = customView.findViewById(R.id.searchEditText);





                    RecyclerViewUnidadesUsuario = customView.findViewById(R.id.RecyclerViewUnidadesUsuario);
                    TextView NombreclIENTE = customView.findViewById(R.id.NombreclIENTE);

                    NombreclIENTE.setText("Unidades de " + nombre);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogUnidadesDeCliente = builder.create();
                    dialogUnidadesDeCliente.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogUnidadesDeCliente.show();


                    MostrarUnidadesClientes(id_ser_cliente);
                    RecyclerViewUnidadesUsuario.setLayoutManager(new LinearLayoutManager(context));
                    AdaptadorUnidadesClientes = new AdaptadorUnidadesClientes(listaUnidades, context, dialogUnidadesDeCliente, id_ser_cliente);
                    RecyclerViewUnidadesUsuario.setAdapter(AdaptadorUnidadesClientes);

                    searchEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            AdaptadorUnidadesClientes.filter(s.toString().toLowerCase());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    AgregarNuevaUnidad.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            MostrarModalMarcas(view, bundle, dialogUnidadesDeCliente);
/*
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_agregar_unidad, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogMarcas = builder.create();
                            dialogMarcas.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogMarcas.show();

                            VerNombresMarcas();

                            TextView textView3 = customView.findViewById(R.id.text);
                            textView3.setText("SELECCIONA UNA MARCA");

                            ConstraintLayout yourConstraintLayoutId = customView.findViewById(R.id.yourConstraintLayoutId);
                            yourConstraintLayoutId.setVisibility(View.VISIBLE);
                            EditText searchEditText = customView.findViewById(R.id.searchEditText);
                            searchEditText.setHint("Buscar la marca");


                            LayoutSeleccionarMarca = customView.findViewById(R.id.SeleccionarMarca);
                            LayoutSeleccionarModelo = customView.findViewById(R.id.SeleccionarModelo);
                            LayoutAgregarDatos = customView.findViewById(R.id.LayoutAgregarDatos);
                            reciclerViewMarcas = customView.findViewById(R.id.reciclerViewMarcas);


                            LayoutSeleccionarMarca.setVisibility(View.VISIBLE);
                            LayoutSeleccionarModelo.setVisibility(View.GONE);
                            LayoutAgregarDatos.setVisibility(View.GONE);


                            adaptadorMarcas = new AdaptadorMarcas(listaMarcas, customView.getContext(), bundle, actionListener, dialogUnidadesDeCliente, dialogMarcas);
                            reciclerViewMarcas.setLayoutManager(new LinearLayoutManager(customView.getContext()));
                            reciclerViewMarcas.setAdapter(adaptadorMarcas);


                            */
                        }
                    });


                    btnAgregarUnidad.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            MostrarModalMarcas(view, bundle, dialogUnidadesDeCliente);

                            /*



                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_agregar_unidad, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogAgregarUnidad = builder.create();
                            dialogAgregarUnidad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogAgregarUnidad.show();

                            VerNombresMarcas();

                            TextView textView3 = customView.findViewById(R.id.text);
                            textView3.setText("REGISTRAR NUEVA UNIDAD PARA " + nombre.toUpperCase());


                            LayoutSeleccionarMarca = customView.findViewById(R.id.SeleccionarMarca);
                            LayoutSeleccionarModelo = customView.findViewById(R.id.SeleccionarModelo);
                            LayoutAgregarDatos = customView.findViewById(R.id.LayoutAgregarDatos);
                            reciclerViewMarcas = customView.findViewById(R.id.reciclerViewMarcas);

                            LayoutSeleccionarMarca.setVisibility(View.VISIBLE);
                            LayoutSeleccionarModelo.setVisibility(View.GONE);
                            LayoutAgregarDatos.setVisibility(View.GONE);


                            adaptadorMarcas = new AdaptadorMarcas(listaMarcas, customView.getContext(), bundle, actionListener, dialogUnidadesDeCliente, dialogAgregarUnidad);
                            reciclerViewMarcas.setLayoutManager(new LinearLayoutManager(customView.getContext()));
                            reciclerViewMarcas.setAdapter(adaptadorMarcas);


 */
                        }
                    });

                }
            });

        } finally {
        }
    }

    private void MostrarModalMarcas(View view, Bundle bundle, AlertDialog dialogUnidadesDeCliente) {

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_agregar_unidad, null);
        builder.setView(ModalRedondeado(view.getContext(), customView));
        AlertDialog dialogMarcas = builder.create();
        dialogMarcas.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogMarcas.show();

        VerNombresMarcas();

        TextView textView3 = customView.findViewById(R.id.text);
        textView3.setText("SELECCIONA UNA MARCA");

        ConstraintLayout yourConstraintLayoutId = customView.findViewById(R.id.yourConstraintLayoutId);
        yourConstraintLayoutId.setVisibility(View.VISIBLE);
        EditText searchEditText = customView.findViewById(R.id.searchEditText);
        searchEditText.setHint("Buscar la marca");


        LayoutSeleccionarMarca = customView.findViewById(R.id.SeleccionarMarca);
        LayoutSeleccionarModelo = customView.findViewById(R.id.SeleccionarModelo);
        LayoutAgregarDatos = customView.findViewById(R.id.LayoutAgregarDatos);
        reciclerViewMarcas = customView.findViewById(R.id.reciclerViewMarcas);


        LayoutSeleccionarMarca.setVisibility(View.VISIBLE);
        LayoutSeleccionarModelo.setVisibility(View.GONE);
        LayoutAgregarDatos.setVisibility(View.GONE);


        adaptadorMarcas = new AdaptadorMarcas(listaMarcas, customView.getContext(), bundle, actionListener, dialogUnidadesDeCliente, dialogMarcas);
        reciclerViewMarcas.setLayoutManager(new LinearLayoutManager(customView.getContext()));
        reciclerViewMarcas.setAdapter(adaptadorMarcas);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptadorMarcas.filter(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private void VerNombresMarcas() {
        listaMarcas.clear(); // Limpia la lista antes de agregar los nuevos
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        listaMarcas.add(jsonObject);
                    }

                    adaptadorMarcas.setFilteredData(listaMarcas);
                    adaptadorMarcas.filter("");


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
                params.put("opcion", "32");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
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

        if (filteredData.isEmpty()) {
            actionListenerClientes.onFilterData(false); // Indica que no hay resultados
        } else {
            actionListenerClientes.onFilterData(true); // Indica que hay resultados
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


    public interface OnActivityActionListener {
        void onFilterData(boolean result);
    }

    private AdaptadorClientes.OnActivityActionListener actionListenerClientes;


    public AdaptadorClientes(List<JSONObject> data, Context context, AdaptadorModelos.OnActivityActionListener actionListener, AdaptadorClientes.OnActivityActionListener actionListenerClientes) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.actionListener = actionListener;
        this.actionListenerClientes = actionListenerClientes;
    }


}

