package com.example.validacion.Adaptadores;


import static android.app.PendingIntent.getActivity;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;
import static com.example.validacion.Adaptadores.Utiles.RedirigirAFragment;
import static com.example.validacion.Adaptadores.Utiles.crearToastPersonalizado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
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
import com.bumptech.glide.signature.ObjectKey;
import com.example.validacion.HomeFragment;
import com.example.validacion.R;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdaptadorUnidadesClientes extends RecyclerView.Adapter<AdaptadorUnidadesClientes.ViewHolder> {
    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    Context context;

    AlertDialog dialogUnidadesDeCliente;

    ArrayList<String> opciones = new ArrayList<>();

    String valorGas;

    String url;
    String id_ser_cliente;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unidades_cliente, parent, false);
        return new ViewHolder(view);

    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        url = context.getResources().getString(R.string.ApiBack);
        try {
            JSONObject jsonObject = filteredData.get(position);
            String id_serv_unidad = jsonObject.optString("id_serv_unidad", "");
            String Marca = jsonObject.optString("Marca", "");
            String Modelo = jsonObject.optString("Modelo", "");
            String anio = jsonObject.optString("anio", "");
            String placas = jsonObject.optString("placas", "");
            String motor = jsonObject.optString("motor", "");
            String foto = jsonObject.optString("foto", "");
            String vin = jsonObject.optString("vin", "");
            holder.NombreUnidad.setText(Marca + " " + Modelo);

            holder.PlacasUnidad.setVisibility(View.VISIBLE);

            holder.PlacasUnidad.setText(placas.toUpperCase());

            String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/" + foto;


            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.baseline_directions_car_filled_24)
                    .error(R.drawable.baseline_directions_car_filled_24)
                    .into(holder.imagenCarrito);


            holder.LayoutAgregarServicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.opciones_agregar_servicio, null);

                    Spinner spinnerGas = customView.findViewById(R.id.spinnerGas);
                    EditText editTextkm = customView.findViewById(R.id.editTextkm);
                    EditText editTextmotivo = customView.findViewById(R.id.editTextmotivo);

                    opciones.add("Lleno");
                    opciones.add("3/4");
                    opciones.add("1/2");
                    opciones.add("1/4");
                    opciones.add("Reserva");

                    Button btn_InsertarServicio = customView.findViewById(R.id.btn_InsertarServicio);

                    TextView vehiculoAIngesar = customView.findViewById(R.id.vehiculoAIngesar);
                    vehiculoAIngesar.setText("Ingresar al taller el " + Marca + " " + Modelo);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogAgregarServicio = builder.create();
                    dialogAgregarServicio.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogAgregarServicio.show();


                    ArrayAdapter<String> adaptadorGas = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, opciones);
                    adaptadorGas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerGas.setAdapter(adaptadorGas);
                    spinnerGas.setSelection(0);


                    spinnerGas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            valorGas = parent.getItemAtPosition(position).toString();
                        }


                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }

                    });


                    btn_InsertarServicio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogConfirmacion = builder.create();
                            dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogConfirmacion.show();


                            TextView textView4 = customView.findViewById(R.id.textView4);
                            Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                            Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);

                            textView4.setText("¿Estas seguro de que deseas insertar este servicio?");

                            buttonCancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogConfirmacion.dismiss();
                                }
                            });

                            buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String motivoIngreso = editTextmotivo.getText().toString();
                                    String kilometraje = editTextkm.getText().toString();

                                    if (motivoIngreso.isEmpty() || kilometraje.isEmpty() || valorGas.isEmpty()) {
                                        dialogConfirmacion.dismiss();
                                        crearToastPersonalizado(context, "Tienes campos vacios, por favor llenalos");
                                    } else {

                                        dialogConfirmacion.dismiss();
                                        dialogAgregarServicio.dismiss();
                                        dialogUnidadesDeCliente.dismiss();
                                        RegistrarServicio(id_ser_cliente, id_serv_unidad, kilometraje, valorGas, motivoIngreso, Marca, Modelo, motor, vin, placas, anio);

                                    }
                                }
                            });


                        }
                    });

                }
            });


        } finally {
        }
    }


    private void RegistrarServicio(String id_ser_cliente, String idunidad, String km, String gas, String motivo, String marca, String modelo, String motor, String vin, String placas, String anio) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        crearToastPersonalizado(context, "Se registro correctamente el servicio");

                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        Utiles.RedirigirAFragment(fragmentManager, new HomeFragment(), null);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        crearToastPersonalizado(context, "Error al registrar el servicio, revisa la conexión");
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "21");
                params.put("id_ser_cliente", id_ser_cliente);
                params.put("idunidad", idunidad);
                params.put("km", km);
                params.put("gas", gas);
                params.put("motivo", motivo);
                params.put("marca", marca);
                params.put("modelo", modelo);
                params.put("motor", motor);
                params.put("vin", vin);
                params.put("placas", placas);
                params.put("anio", anio);
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

        TextView NombreUnidad;
        LinearLayout LayoutAgregarServicio;
        ImageView imagenCarrito;
        TextView PlacasUnidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            NombreUnidad = itemView.findViewById(R.id.NombreUnidad);
            LayoutAgregarServicio = itemView.findViewById(R.id.LayoutAgregarServicio);
            imagenCarrito = itemView.findViewById(R.id.imagenCarrito);

            PlacasUnidad = itemView.findViewById(R.id.PlacasUnidad);
        }
    }

    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(data);
        } else {


            String[] keywords = query.toLowerCase().split(" ");
            for (JSONObject item : data) {
                String id_serv_unidad = item.optString("id_serv_unidad", "").toLowerCase();
                String Marca = item.optString("Marca", "").toLowerCase();
                String Modelo = item.optString("Modelo", "").toLowerCase();
                String anio = item.optString("anio", "").toLowerCase();
                String placas = item.optString("placas", "").toLowerCase();
                String vin = item.optString("vin", "").toLowerCase();

                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(Marca.contains(keyword) || Modelo.contains(keyword) || anio.contains(keyword) || placas.contains(keyword) || vin.contains(keyword) ||
                            id_serv_unidad.contains(keyword))) {
                        matchesAllKeywords = false;
                        break;
                    }
                }

                if (matchesAllKeywords) {
                    filteredData.add(item);
                }
            }
            notifyDataSetChanged();
        }
    }

    public void setFilteredData(List<JSONObject> filteredData) {
        this.filteredData = new ArrayList<>(filteredData);
        notifyDataSetChanged();
    }

    public AdaptadorUnidadesClientes(List<JSONObject> data, Context context, AlertDialog dialogUnidadesDeCliente, String id_ser_cliente) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.dialogUnidadesDeCliente = dialogUnidadesDeCliente;
        this.id_ser_cliente = id_ser_cliente;
    }


}

