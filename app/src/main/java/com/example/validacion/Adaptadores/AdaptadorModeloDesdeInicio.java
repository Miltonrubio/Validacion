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
import com.example.validacion.R;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

import okhttp3.internal.Util;


public class AdaptadorModeloDesdeInicio extends RecyclerView.Adapter<AdaptadorModeloDesdeInicio.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String id_modelo, String nombreModelo);
    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    Context context;

    AlertDialog.Builder builder;

    String url;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_unidades_cliente, parent, false);

        return new ViewHolder(view);

    }
/*
    ConstraintLayout LayoutSeleccionarMarca;
    ConstraintLayout LayoutAgregarDatos;
    ConstraintLayout LayoutSeleccionarModelo;
    RecyclerView reciclerViewModelos;
    AdaptadorModeloDesdeInicio adaptadorModeloDesdeInicio;
*/

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.LayoutAgregarServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    String id_modelo = filteredData.get(position).optString("id_car_model", "");
                    String nombreModelo = filteredData.get(position).optString("name", "");
                    onItemClickListener.onItemClick(id_modelo, nombreModelo);
                }
            }
        });

        try {
            JSONObject jsonObject2 = filteredData.get(position);

            String modelo = jsonObject2.optString("name", "");
            String id_car_model = jsonObject2.optString("id_car_model", "");

            setTextViewText(holder.NombreUnidad, modelo, "No se encontro el modelo");
            holder.imagenCarrito.setVisibility(View.GONE);


        } finally {
        }
    }

    /*
        private void VerModelos(String id, Context context) {
            listaModelos.clear();
            modalCargando = Utiles.ModalCargando(context, builder);
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
                    onLoadComplete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    crearToastPersonalizado(context, "Error al cargar los datos, revisa la conexion");
                    onLoadComplete();
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

    */



    Bundle bundleUsuario;

    public AdaptadorModeloDesdeInicio(List<JSONObject> data, Context context, Bundle bundleUsuario) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        url = context.getResources().getString(R.string.ApiBack);
        this.bundleUsuario = bundleUsuario;
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
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


        /*
        TextView NombreUnidad;
        ImageView imagenCarrito;
        LinearLayout LayoutAgregarServicio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            NombreUnidad = itemView.findViewById(R.id.NombreUnidad);
            LayoutAgregarServicio = itemView.findViewById(R.id.LayoutAgregarServicio);
            imagenCarrito = itemView.findViewById(R.id.imagenCarrito);
        }


         */
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
    Bundle bundleUsuario;

    AdaptadorModeloDesdeInicio.OnActivityActionListener actionListenerModelo;

    AlertDialog dialogMarcas;


    private void onLoadComplete() {
        if (modalCargando.isShowing() && modalCargando != null) {
            modalCargando.dismiss();
        }
    }

    public AdaptadorMarcaDesdeInicio(List<JSONObject> data, Context context, Bundle bundleUsuario, AdaptadorModeloDesdeInicio.OnActivityActionListener actionListenerModelo, AlertDialog dialogMarcas) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        url = context.getResources().getString(R.string.ApiBack);
        this.bundleUsuario = bundleUsuario;
        this.actionListenerModelo = actionListenerModelo;
        this.dialogMarcas = dialogMarcas;

        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
    }

*/
}



/*
public class AdaptadorModeloDesdeInicio extends RecyclerView.Adapter<AdaptadorModeloDesdeInicio.ViewHolder> {


    public interface OnActivityActionListener {
        void onAgregarUnidad(String idcliente, String idmarca, String idmodelo, String anio, String placas, String vin, String motor, String tipo);
    }

    private AdaptadorModeloDesdeInicio.OnActivityActionListener actionListener;

    AlertDialog dialogMarcas;
    AlertDialog dialogMModelos;

    public AdaptadorModeloDesdeInicio(List<JSONObject> data, Context context, Bundle bundle, AdaptadorModeloDesdeInicio.OnActivityActionListener actionListener, AlertDialog dialogMarcas, AlertDialog dialogMModelos) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.bundle = bundle;
        url = context.getResources().getString(R.string.ApiBack);
        this.actionListener = actionListener;
        this.dialogMarcas = dialogMarcas;
        this.dialogMModelos = dialogMModelos;

        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
    }

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    Context context;

    String url;
    String tipoVehiculo;

    private ArrayList<String> listaModelos = new ArrayList<>();


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

    AdaptadorTiposUnidadesDesdeInicio adaptadorTiposUnidades;

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


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


            Bundle bundleModelo = new Bundle();
            bundleModelo.putString("id_marca", id_marca);
            bundleModelo.putString("marca", marca);
            bundleModelo.putString("id_ser_cliente", id_ser_cliente);
            bundleModelo.putString("nombreUsuario", nombreUsuario);
            bundleModelo.putString("id_car_model", id_car_model);
            bundleModelo.putString("name", name);


            holder.LayoutAgregarServicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.layout_mostrar_tipos_unidades, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogModelos = builder.create();
                    dialogModelos.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogModelos.show();

                    RecyclerView recyclerViewTiposUnidades = customView.findViewById(R.id.recyclerViewTiposUnidades);

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                    recyclerViewTiposUnidades.setLayoutManager(gridLayoutManager);
                    adaptadorTiposUnidades = new AdaptadorTiposUnidadesDesdeInicio(listaTiposUnidades, context);
                    recyclerViewTiposUnidades.setAdapter(adaptadorTiposUnidades);
                    VerTipoUnidades();


                    */
/*
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_agregar_unidad, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogModelos = builder.create();
                    dialogModelos.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogModelos.show();

                    TextView textView3 = customView.findViewById(R.id.text);
                    textView3.setText("REGISTRAR " + marca.toUpperCase() + " " + name.toUpperCase() + " PARA " + nombreUsuario.toUpperCase());


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

                    radioButton2.setChecked(true);

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

                                dialogModelos.dismiss();
                                dialogMModelos.dismiss();
                                dialogMarcas.dismiss();


                                if (radioButton2.isChecked()) {
                                    actionListener.onAgregarUnidad(id_ser_cliente, id_marca, id_car_model, anio, placas, vin, motor, "vehiculo");

                                } else if (radioButton3.isChecked()) {

                                    actionListener.onAgregarUnidad(id_ser_cliente, id_marca, id_car_model, anio, placas, vin, motor, "Motocicleta");

                                } else if (radioButton4.isChecked()) {
                                    actionListener.onAgregarUnidad(id_ser_cliente, id_marca, id_car_model, anio, placas, vin, motor, "Plataforma");

                                } else if (radioButton5.isChecked()) {
                                    actionListener.onAgregarUnidad(id_ser_cliente, id_marca, id_car_model, anio, placas, vin, motor, "Grua");

                                } else if (radioButton7.isChecked()) {
                                    actionListener.onAgregarUnidad(id_ser_cliente, id_marca, id_car_model, anio, placas, vin, motor, "Montacargas");

                                } else if (radioButton6.isChecked()) {
                                    actionListener.onAgregarUnidad(id_ser_cliente, id_marca, id_car_model, anio, placas, vin, motor, "Patin");

                                }
                            }
                        }
                    });
//  */
/*
                }
            });


        } finally {
        }
    }


    List<JSONObject> listaTiposUnidades = new ArrayList<>();
    AlertDialog modalCargando;

    AlertDialog.Builder builder;

    private void VerTipoUnidades() {
        listaTiposUnidades.clear();
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        listaTiposUnidades.add(jsonObject);
                    }

                    adaptadorTiposUnidades.setFilteredData(listaTiposUnidades);
                    adaptadorTiposUnidades.filter("");


                } catch (JSONException e) {
                    crearToastPersonalizado(context, "Error al cargar los datos");
                }
                onLoadComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                crearToastPersonalizado(context, "Error al cargar los datos");
                onLoadComplete();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "90");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void onLoadComplete() {
        if (modalCargando.isShowing() && modalCargando != null) {
            modalCargando.dismiss();
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


}

*/