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
import android.text.TextUtils;
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


public class AdaptadorModelos extends RecyclerView.Adapter<AdaptadorModelos.ViewHolder> {

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    Context context;

    String url;
    String tipoVehiculo;

    private ArrayList<String> listaModelos = new ArrayList<>();

    AlertDialog dialogUnidadesDeCliente, dialogAgregarUnidad, dialogMarcas;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unidades_cliente, parent, false);
        return new ViewHolder(view);

    }


    Bundle bundle;
    ConstraintLayout LayoutSeleccionarMarca;
    ConstraintLayout LayoutAgregarDatos;
    ConstraintLayout LayoutSeleccionarModelo;
    RecyclerView reciclerViewModelos;


    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        url = context.getResources().getString(R.string.ApiBack);
        String id_marca = bundle.getString("id_car_make");
        String nombreUsuario = bundle.getString("nombreUsuario");
        String marca = bundle.getString("marca");
        String id_ser_cliente = bundle.getString("id_ser_cliente");


        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String name = jsonObject2.optString("name", "");
            String id_car_model = jsonObject2.optString("id_car_model", "");
            setTextViewText(holder.NombreUnidad, name, "No se encontro la marca");
            holder.imagenCarrito.setVisibility(View.GONE);

            holder.LayoutAgregarServicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_agregar_unidad, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogModelos = builder.create();
                    dialogModelos.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogModelos.show();

                    TextView textView3 = customView.findViewById(R.id.text);
                    textView3.setText("REGISTRAR " +marca.toUpperCase() + " " +name.toUpperCase() + " PARA " + nombreUsuario.toUpperCase());


                    LayoutSeleccionarMarca = customView.findViewById(R.id.SeleccionarMarca);
                    LayoutSeleccionarModelo = customView.findViewById(R.id.SeleccionarModelo);
                    LayoutAgregarDatos = customView.findViewById(R.id.LayoutAgregarDatos);
                    Button botonAgregarCliente = customView.findViewById(R.id.botonAgregarCliente);
                    Button botonCancelar = customView.findViewById(R.id.botonCancelar);
                    EditText EditTextAnio = customView.findViewById(R.id.EditTextAnio);
                    EditText EditTextPlacas = customView.findViewById(R.id.EditTextPlacas);
                    EditText EditTextVIN = customView.findViewById(R.id.EditTextVIN);
                    EditText EditTextMotor = customView.findViewById(R.id.EditTextMotor);
                    ConstraintLayout yourConstraintLayoutId = customView.findViewById(R.id.yourConstraintLayoutId);
                    yourConstraintLayoutId.setVisibility(View.GONE);


                    RadioButton radioButton2 = customView.findViewById(R.id.radioButton2);
                    RadioButton radioButton3 = customView.findViewById(R.id.radioButton3);
                    RadioButton radioButton5 = customView.findViewById(R.id.radioButton5);
                    RadioButton radioButton4 = customView.findViewById(R.id.radioButton4);
                    RadioButton radioButton7 = customView.findViewById(R.id.radioButton7);
                    RadioButton radioButton6 = customView.findViewById(R.id.radioButton6);

                    LayoutSeleccionarModelo.setVisibility(View.GONE);
                    LayoutSeleccionarMarca.setVisibility(View.GONE);
                    LayoutAgregarDatos.setVisibility(View.VISIBLE);

                    radioButton2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            tipoVehiculo = radioButton2.getText().toString();
                            radioButton2.setChecked(true);
                            radioButton3.setChecked(false);
                            radioButton5.setChecked(false);
                            radioButton4.setChecked(false);
                            radioButton7.setChecked(false);
                            radioButton6.setChecked(false);
                        }
                    });


                    radioButton3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tipoVehiculo = radioButton3.getText().toString();
                            radioButton2.setChecked(false);
                            radioButton3.setChecked(true);
                            radioButton5.setChecked(false);
                            radioButton4.setChecked(false);
                            radioButton7.setChecked(false);
                            radioButton6.setChecked(false);
                        }
                    });


                    radioButton4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tipoVehiculo = radioButton6.getText().toString();
                            radioButton2.setChecked(false);
                            radioButton3.setChecked(false);
                            radioButton5.setChecked(false);
                            radioButton4.setChecked(true);
                            radioButton7.setChecked(false);
                            radioButton6.setChecked(false);
                        }
                    });

                    radioButton5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tipoVehiculo = radioButton5.getText().toString();
                            radioButton2.setChecked(false);
                            radioButton3.setChecked(false);
                            radioButton5.setChecked(true);
                            radioButton4.setChecked(false);
                            radioButton7.setChecked(false);
                            radioButton6.setChecked(false);
                        }
                    });


                    radioButton7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tipoVehiculo = radioButton7.getText().toString();
                            radioButton2.setChecked(false);
                            radioButton3.setChecked(false);
                            radioButton5.setChecked(false);
                            radioButton4.setChecked(false);
                            radioButton7.setChecked(true);
                            radioButton6.setChecked(false);
                        }
                    });

                    radioButton6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tipoVehiculo = radioButton6.getText().toString();
                            radioButton2.setChecked(false);
                            radioButton3.setChecked(false);
                            radioButton5.setChecked(false);
                            radioButton4.setChecked(false);
                            radioButton7.setChecked(false);
                            radioButton6.setChecked(true);
                        }
                    });

                    botonCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogModelos.dismiss();
                        }
                    });

                    botonAgregarCliente.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            String anio = EditTextAnio.getText().toString();
                            String placas = EditTextPlacas.getText().toString();
                            String vin = EditTextVIN.getText().toString();
                            String motor = EditTextMotor.getText().toString();

                            if (anio.isEmpty() || placas.isEmpty() || vin.isEmpty() || motor.isEmpty()) {
                                crearToastPersonalizado(context, "Tienes campos vacios, por favor rellenalos");
                            } else {
                                //    crearToastPersonalizado(context, "id usuario " + id_ser_cliente + "id marca" + id_marca + anio + placas + vin + motor + tipoVehiculo);

                                dialogUnidadesDeCliente.dismiss();
                                dialogAgregarUnidad.dismiss();
                                dialogMarcas.dismiss();
                                dialogModelos.dismiss();
                                actionListener.onAgregarUnidad(id_ser_cliente, id_marca, id_car_model, anio, placas, vin, motor, tipoVehiculo);

                            }
                        }
                    });


                }
            });

        } finally {
        }
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
            imagenCarrito = itemView.findViewById(R.id.imagenCarrito);
            LayoutAgregarServicio = itemView.findViewById(R.id.LayoutAgregarServicio);
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

/*
    public AdaptadorModelos(List<JSONObject> data, Context context, Bundle bundle) {
        this.data = data;
        this.context = context;
        this.bundle = bundle;
        this.filteredData = new ArrayList<>(data);
    }
*/

    public interface OnActivityActionListener {
        void onAgregarUnidad(String idcliente, String idmarca, String idmodelo, String anio, String placas, String vin, String motor, String tipo);
    }

    private AdaptadorModelos.OnActivityActionListener actionListener;


    public AdaptadorModelos(List<JSONObject> data, Context context, Bundle bundle, AdaptadorModelos.OnActivityActionListener actionListener, AlertDialog dialogUnidadesDeCliente, AlertDialog dialogAgregarUnidad, AlertDialog dialogMarcas) {
        this.data = data;
        this.context = context;
        this.bundle = bundle;
        this.filteredData = new ArrayList<>(data);
        this.actionListener = actionListener;
        this.dialogUnidadesDeCliente = dialogUnidadesDeCliente;
        this.dialogAgregarUnidad = dialogAgregarUnidad;
        this.dialogMarcas = dialogMarcas;
    }


}

