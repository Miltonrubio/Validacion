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

import okhttp3.internal.Util;


public class AdaptadorMarcaDesdeInicio extends RecyclerView.Adapter<AdaptadorMarcaDesdeInicio.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String id_marca, String nombreMarca);
    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    Context context;
    AlertDialog modalCargando;
    AlertDialog.Builder builder;

    String url;

    List<JSONObject> listaModelos = new ArrayList<>();

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
                    String id_marca = filteredData.get(position).optString("id_car_make", "");
                    String nombreMarca = filteredData.get(position).optString("name", "");
                    onItemClickListener.onItemClick(id_marca, nombreMarca);
                }
            }
        });


        try {
            JSONObject jsonObject2 = filteredData.get(position);

            String marca = jsonObject2.optString("name", "");
            String id_car_make = jsonObject2.optString("id_car_make", "");

            setTextViewText(holder.NombreUnidad, marca, "No se encontro el modelo");
            holder.imagenCarrito.setVisibility(View.GONE);

        /*
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
                    AlertDialog dialogMModelos = builder.create();
                    dialogMModelos.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogMModelos.show();

                    VerModelos(id_car_make, view.getContext());

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


                    adaptadorModeloDesdeInicio = new AdaptadorModeloDesdeInicio(listaModelos, context, bundle, actionListenerModelo, dialogMarcas, dialogMModelos);
                    reciclerViewModelos.setLayoutManager(new LinearLayoutManager(context));
                    reciclerViewModelos.setAdapter(adaptadorModeloDesdeInicio);

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
*/

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

    public AdaptadorMarcaDesdeInicio(List<JSONObject> data, Context context, Bundle bundleUsuario) {
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

