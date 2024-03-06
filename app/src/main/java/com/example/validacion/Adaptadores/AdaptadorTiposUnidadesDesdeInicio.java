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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.validacion.R;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdaptadorTiposUnidadesDesdeInicio extends RecyclerView.Adapter<AdaptadorTiposUnidadesDesdeInicio.ViewHolder> {

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    Context context;

    String url;

    String id_marcaSeleccionada = "";
    String marcaSeleccionada = "";

    AdaptadorMarcaDesdeInicio adaptadorMarcaDesdeInicio;

    List<JSONObject> listaMarcas = new ArrayList<>();
    List<JSONObject> listaModelos = new ArrayList<>();
    AdaptadorModeloDesdeInicio adaptadorModeloDesdeInicio;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tipos_unidades, parent, false);
        return new ViewHolder(view);

    }


    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String nombre = jsonObject2.optString("nombre", "");
            String estado = jsonObject2.optString("estado", "");
            String foto = jsonObject2.optString("foto", "");
            String ID_tipo_unidad = jsonObject2.optString("ID_tipo_unidad", "");
            String placas = jsonObject2.optString("placas", "");
            String km = jsonObject2.optString("km", "");
            String gasolina = jsonObject2.optString("gasolina", "");
            String vin = jsonObject2.optString("vin", "");
            String motor = jsonObject2.optString("motor", "");


            setTextViewText(holder.TipoUnidad, nombre, "No se encontro el modelo");

            String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/" + foto;

            Glide.with(context)
                    .load(imageUrl)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.baseline_directions_car_filled_24)
                    .into(holder.imagenCarrito);

            Bundle bundle = new Bundle();
            bundle.putString("ID_tipo_unidad", ID_tipo_unidad);
            bundle.putString("checkPlacas", placas);
            bundle.putString("checkKm", km);
            bundle.putString("checkVin", vin);
            bundle.putString("checkMotor", motor);
            bundle.putString("checkGasolina", gasolina);

            holder.LayoutUnidad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_registrar_unidad, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogAgregarUnidad = builder.create();
                    dialogAgregarUnidad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogAgregarUnidad.show();
                    TextView textView24 = customView.findViewById(R.id.textView24);
                    LinearLayout LayoutCliente = customView.findViewById(R.id.LayoutCliente);
                    TextView textSeleccionarMarca = customView.findViewById(R.id.textSeleccionarMarca);
                    TextView textSeleccionarUnidad = customView.findViewById(R.id.textSeleccionarUnidad);
                    EditText anioUnidad = customView.findViewById(R.id.anioUnidad);
                    Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                    Button buttonGuardar = customView.findViewById(R.id.buttonGuardar);

                    if (marcaSeleccionada.equalsIgnoreCase("") || marcaSeleccionada.isEmpty()) {
                        textSeleccionarUnidad.setEnabled(false);
                        textSeleccionarUnidad.setClickable(false);

                    } else {
                        textSeleccionarUnidad.setEnabled(true);
                        textSeleccionarUnidad.setClickable(true);
                    }

                    textView24.setText("Registra los datos solicitados para " + nombre);


                    TextView AgregaPlaca = customView.findViewById(R.id.AgregaPlaca);
                    EditText PlacasUnidad = customView.findViewById(R.id.PlacasUnidad);

                    if (placas.equals("1")) {
                        AgregaPlaca.setVisibility(View.VISIBLE);
                        PlacasUnidad.setVisibility(View.VISIBLE);
                    } else {
                        AgregaPlaca.setVisibility(View.GONE);
                        PlacasUnidad.setVisibility(View.GONE);
                    }


                    TextView AgregaKm = customView.findViewById(R.id.AgregaKm);
                    EditText kmUnidad = customView.findViewById(R.id.kmUnidad);

                    AgregaKm.setVisibility(View.GONE);
                    kmUnidad.setVisibility(View.GONE);
/*
                    if (km.equals("1")) {
                        AgregaKm.setVisibility(View.VISIBLE);
                        kmUnidad.setVisibility(View.VISIBLE);
                    } else {
                        AgregaKm.setVisibility(View.GONE);
                        kmUnidad.setVisibility(View.GONE);

                    }
*/


                    TextView AgregaMotor = customView.findViewById(R.id.AgregaMotor);
                    EditText MotorUnidad = customView.findViewById(R.id.MotorUnidad);

                    if (motor.equals("1")) {
                        AgregaMotor.setVisibility(View.VISIBLE);
                        MotorUnidad.setVisibility(View.VISIBLE);
                    } else {
                        AgregaMotor.setVisibility(View.GONE);
                        MotorUnidad.setVisibility(View.GONE);
                    }


                    TextView AgregaVin = customView.findViewById(R.id.AgregaVin);
                    EditText VinUnidad = customView.findViewById(R.id.VinUnidad);

                    if (vin.equals("1")) {
                        AgregaVin.setVisibility(View.VISIBLE);
                        VinUnidad.setVisibility(View.VISIBLE);

                    } else {
                        AgregaVin.setVisibility(View.GONE);
                        VinUnidad.setVisibility(View.GONE);
                    }



                    buttonGuardar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String VinIngresado;
                            String MotorIngresado;
                            //      String KmIngresado;
                            String PlacasIngresadas;
                            String anioIngresado;
                            String cantidadInyectoresIngresada;

                            if (VinUnidad.getVisibility() == View.VISIBLE) {
                                VinIngresado = VinUnidad.getText().toString();
                            } else {
                                VinIngresado = "N/A";
                            }

                            if (MotorUnidad.getVisibility() == View.VISIBLE) {
                                MotorIngresado = MotorUnidad.getText().toString();
                            } else {
                                MotorIngresado = "N/A";
                            }


                            /*
                            if (kmUnidad.getVisibility() == View.VISIBLE) {
                                KmIngresado = kmUnidad.getText().toString();
                            } else {
                                KmIngresado = "0";
                            }
*/
                            if (PlacasUnidad.getVisibility() == View.VISIBLE) {
                                String placasRaw = PlacasUnidad.getText().toString();
                                // Eliminar guiones ("-") de la cadena
                                PlacasIngresadas = placasRaw.replace("-", "");
                            } else {
                                PlacasIngresadas = "N/A";
                            }

                            anioIngresado = anioUnidad.getText().toString();

/*
                            Utiles.crearToastPersonalizado(context, "marcaSeleccionada " + marcaSeleccionada + "modeloSeleccionado " + modeloSeleccionado + "VinIngresado " +
                                    VinIngresado + "MotorIngresado " + MotorIngresado + "KmIngresado " + KmIngresado + "PlacasIngresadas " + PlacasIngresadas
                                    + " anioIngresado " + anioIngresado);
*/

                            if (PlacasIngresadas.equalsIgnoreCase("") || PlacasIngresadas.isEmpty() || PlacasIngresadas.equalsIgnoreCase("null") || PlacasIngresadas.equalsIgnoreCase(null)) {
                                Utiles.crearToastPersonalizado(context, "Debes ingresar las placas");
                            } else {
                                dialogAgregarUnidad.dismiss();
                                dialogListaUnidades.dismiss();
                                actionListener.onAgregarUnidad(id_ser_cliente, id_marcaSeleccionada, id_modeloSeleccionado, anioIngresado, PlacasIngresadas.toUpperCase(), VinIngresado, MotorIngresado, nombre, foto);
                            }

                        }
                    });


                    textSeleccionarUnidad.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            VerModelos(id_marcaSeleccionada);
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_marca_modelo, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogModelos = builder.create();
                            dialogModelos.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogModelos.show();
                            EditText searchEditText = customView.findViewById(R.id.searchEditText);
                            TextView textView32 = customView.findViewById(R.id.textView32);
                            textView32.setText("SELECCIONA UN MODELO");
                            RecyclerView recyclerViewMarcasUnidades = customView.findViewById(R.id.recyclerViewMarcasUnidades);
                            adaptadorModeloDesdeInicio = new AdaptadorModeloDesdeInicio(listaModelos, context, bundle);
                            recyclerViewMarcasUnidades.setLayoutManager(new LinearLayoutManager(context));

                            adaptadorModeloDesdeInicio.setOnItemClickListener(new AdaptadorModeloDesdeInicio.OnItemClickListener() {
                                @Override
                                public void onItemClick(String id_modelo, String nombreModelo) {
                                    id_modeloSeleccionado = id_modelo;
                                    modeloSeleccionado = nombreModelo;
                                    dialogModelos.dismiss();

                                    textSeleccionarUnidad.setText(modeloSeleccionado.toUpperCase());
                                }
                            });
                            recyclerViewMarcasUnidades.setAdapter(adaptadorModeloDesdeInicio);

                            searchEditText.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    adaptadorModeloDesdeInicio.filter(s.toString().toLowerCase());
                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                }
                            });

                        }
                    });


                    LayoutCliente.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            VerMarcas();
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_marca_modelo, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogMarca = builder.create();
                            dialogMarca.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogMarca.show();
                            TextView textView32 = customView.findViewById(R.id.textView32);
                            textView32.setText("SELECCIONA UNA MARCA");
                            RecyclerView recyclerViewMarcasUnidades = customView.findViewById(R.id.recyclerViewMarcasUnidades);
                            EditText searchEditText = customView.findViewById(R.id.searchEditText);
                            adaptadorMarcaDesdeInicio = new AdaptadorMarcaDesdeInicio(listaMarcas, context, bundle);
                            recyclerViewMarcasUnidades.setLayoutManager(new LinearLayoutManager(context));
                            adaptadorMarcaDesdeInicio.setOnItemClickListener(new AdaptadorMarcaDesdeInicio.OnItemClickListener() {
                                @Override
                                public void onItemClick(String id_marca, String nombreMarca) {
                                    id_marcaSeleccionada = id_marca;
                                    marcaSeleccionada = nombreMarca;
                                    dialogMarca.dismiss();

                                    textSeleccionarMarca.setText(marcaSeleccionada.toUpperCase());
                                    textSeleccionarUnidad.setText("Selecciona un modelo");
                                    modeloSeleccionado = "";
                                    id_modeloSeleccionado = "";
                                    textSeleccionarUnidad.setEnabled(true);
                                    textSeleccionarUnidad.setClickable(true);
                                }
                            });
                            recyclerViewMarcasUnidades.setAdapter(adaptadorMarcaDesdeInicio);


                            searchEditText.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    adaptadorMarcaDesdeInicio.filter(s.toString().toLowerCase());
                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                }
                            });
                        }
                    });


                    buttonCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogAgregarUnidad.dismiss();
                        }
                    });


                }
            });


        } finally {
        }
    }

    String id_modeloSeleccionado = "";
    String modeloSeleccionado = "";

    private void VerMarcas() {
        listaMarcas.clear();
        //   modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.d("Ola", "onResponse: " + response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listaMarcas.add(jsonObject);
                    }

                    adaptadorMarcaDesdeInicio.setFilteredData(listaMarcas);
                    adaptadorMarcaDesdeInicio.filter("");
                    adaptadorMarcaDesdeInicio.notifyDataSetChanged();


                } catch (JSONException e) {
                    crearToastPersonalizado(context, "No hay datos para mostrar");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                crearToastPersonalizado(context, "Error al cargar los datos, revisa la conexion");

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

    private void VerModelos(String id) {
        listaModelos.clear();
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
                        adaptadorModeloDesdeInicio.setFilteredData(listaModelos);
                        adaptadorModeloDesdeInicio.filter("");

                    }
                } catch (JSONException e) {
                    crearToastPersonalizado(context, "No hay datos para mostrar");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                crearToastPersonalizado(context, "Error al cargar los datos, revisa la conexion");
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
        TextView TipoUnidad;
        ImageView imagenCarrito;
        ConstraintLayout LayoutUnidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TipoUnidad = itemView.findViewById(R.id.TipoUnidad);
            LayoutUnidad = itemView.findViewById(R.id.LayoutUnidad);
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


    public interface OnActivityActionListener {
        void onAgregarUnidad(String idcliente, String idmarca, String idmodelo, String anio, String placas, String vin, String motor, String tipo, String foto);
    }

    private AdaptadorTiposUnidadesDesdeInicio.OnActivityActionListener actionListener;

    String id_ser_cliente;
    String nombreUsuario;

    AlertDialog dialogListaUnidades;

    public AdaptadorTiposUnidadesDesdeInicio(List<JSONObject> data, Context context, AdaptadorTiposUnidadesDesdeInicio.OnActivityActionListener actionListener, Bundle bundleUsuario, AlertDialog dialogListaUnidades) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.actionListener = actionListener;
        url = context.getResources().getString(R.string.ApiBack);

        this.nombreUsuario = bundleUsuario.getString("nombreUsuario");
        this.id_ser_cliente = bundleUsuario.getString("id_ser_cliente");
        this.dialogListaUnidades = dialogListaUnidades;

    }


}

