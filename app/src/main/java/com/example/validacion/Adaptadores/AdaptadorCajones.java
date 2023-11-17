package com.example.validacion.Adaptadores;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.validacion.DetallesArrastres;
import com.example.validacion.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdaptadorCajones extends RecyclerView.Adapter<AdaptadorCajones.ViewHolder> {


    private Context context;

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    String url;

    List<JSONObject> listaHerramientas = new ArrayList<>();
    AdaptadorHerramientas adaptadorHerramientas;

    String id_gabeta;


    ConstraintLayout ContenedorContenido;
    ConstraintLayout ContenedorSinContenido;
    ConstraintLayout ContenedorSinInternet;


    AdaptadorHerramientas.OnActivityActionListener actionListener;

    public AdaptadorCajones(List<JSONObject> data, Context context, String id_gabeta, AdaptadorHerramientas.OnActivityActionListener actionListener) {
        this.data = data;
        this.context = context;
        this.id_gabeta = id_gabeta;
        this.filteredData = new ArrayList<>(data);
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cajones, parent, false);
        return new ViewHolder(view);

    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        url = context.getResources().getString(R.string.ApiBack);


        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String nombre_cajon = jsonObject2.optString("nombre_cajon", "");
            String id_cajon = jsonObject2.optString("id_cajon", "");

            Bundle bundle = new Bundle();
            bundle.putString("nombre", nombre_cajon);
            bundle.putString("id_cajon", id_cajon);
            holder.numCajon.setText(nombre_cajon);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_mostrar_herramientas, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogAgregarCliente = builder.create();
                    dialogAgregarCliente.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogAgregarCliente.show();


                    ContenedorContenido = customView.findViewById(R.id.ContenedorContenido);
                    ContenedorSinContenido = customView.findViewById(R.id.ContenedorSinContenido);
                    ContenedorSinInternet = customView.findViewById(R.id.ContenedorSinInternet);


                    RecyclerView reciclerViewHerramientas = customView.findViewById(R.id.reciclerViewHerramientas);
                    FloatingActionButton botonAgregarHerramienta = customView.findViewById(R.id.botonAgregarHerramienta);


                    reciclerViewHerramientas.setLayoutManager(new LinearLayoutManager(context));
                    adaptadorHerramientas = new AdaptadorHerramientas(listaHerramientas, context, id_gabeta, actionListener, dialogAgregarCliente);
                    reciclerViewHerramientas.setAdapter(adaptadorHerramientas);


                    listaHerramientas.clear();
                    MostrarHerramientas(id_gabeta, id_cajon);


                    botonAgregarHerramienta.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.agregar_gaveta, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogAgregarHerramienta = builder.create();
                            dialogAgregarHerramienta.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogAgregarHerramienta.show();

                            TextView textView7 = customView.findViewById(R.id.textView7);
                            textView7.setText("REGISTRAR HERRAMIENTAS");

                            TextView textView = customView.findViewById(R.id.textView);
                            textView.setText("Nombre de la herramienta");

                            TextView textView6 = customView.findViewById(R.id.textView6);
                            textView6.setText("Descripcion de la herramienta");

                            TextView textView8 = customView.findViewById(R.id.textView8);
                            textView8.setText("Cantidad de piezas");

                            EditText EditTextNombre = customView.findViewById(R.id.EditTextNombre);
                            EditText EditTexDescripcionGaveta = customView.findViewById(R.id.EditTexDescripcionGaveta);
                            EditText EditTexCantidadCajones = customView.findViewById(R.id.EditTexCantidadCajones);
                            Button botonCancelar = customView.findViewById(R.id.botonCancelar);
                            Button botonAgregarCliente = customView.findViewById(R.id.botonAgregarCliente);


                            botonCancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogAgregarHerramienta.dismiss();
                                }
                            });


                            botonAgregarCliente.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    String nombreHerramienta = EditTextNombre.getText().toString();
                                    String descripHerramienta = EditTexDescripcionGaveta.getText().toString();
                                    String cantidadHerramientas = EditTexCantidadCajones.getText().toString();


                                    if (nombreHerramienta.isEmpty() || descripHerramienta.isEmpty() || cantidadHerramientas.isEmpty()) {
                                        Utiles.crearToastPersonalizado(context, "Tienes campos vacios, por favor llenalos");
                                    } else {
                                        RegistrarHerramienta(nombreHerramienta, descripHerramienta, cantidadHerramientas, id_cajon);
                                        dialogAgregarHerramienta.dismiss();
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


    private void RegistrarHerramienta(String nombre, String descrip, String cantidad, String id_cajon) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Utiles.crearToastPersonalizado(context, response);

                MostrarHerramientas(id_gabeta, id_cajon);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   mostrarLayout("SinInternet");
                Utiles.crearToastPersonalizado(context, "Algo fallo");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "43");
                params.put("nombreHerramienta", nombre);
                params.put("descripHerramienta", descrip);
                params.put("cantidadHerramientas", cantidad);
                params.put("id_cajon", id_cajon);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void MostrarHerramientas(String id_gabeta, String id_cajon) {
        listaHerramientas.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String estado = jsonObject.getString("inventario");

                        if (!estado.equalsIgnoreCase("eliminado")) {
                            listaHerramientas.add(jsonObject);
                        }

                    }
                    adaptadorHerramientas.notifyDataSetChanged();
                    adaptadorHerramientas.setFilteredData(listaHerramientas);
                    adaptadorHerramientas.filter("");

                    if (listaHerramientas.size() > 0) {

                        mostrarLayout("conContenido");
                    } else {

                        mostrarLayout("SinContenido");
                    }

                } catch (JSONException e) {
                    mostrarLayout("SinContenido");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mostrarLayout("SinInternet");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "42");
                params.put("id_gabeta", id_gabeta);
                params.put("id_cajon", id_cajon);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void mostrarLayout(String estado) {


        if (estado.equalsIgnoreCase("conContenido")) {
            ContenedorContenido.setVisibility(View.VISIBLE);
            ContenedorSinContenido.setVisibility(View.GONE);
            ContenedorSinInternet.setVisibility(View.GONE);

        } else if (estado.equalsIgnoreCase("SinInternet")) {
            ContenedorContenido.setVisibility(View.GONE);
            ContenedorSinContenido.setVisibility(View.GONE);
            ContenedorSinInternet.setVisibility(View.VISIBLE);

        } else {
            ContenedorContenido.setVisibility(View.GONE);
            ContenedorSinContenido.setVisibility(View.VISIBLE);
            ContenedorSinInternet.setVisibility(View.GONE);

        }

    }


    @Override
    public int getItemCount() {

        //return filteredData.size();
        return filteredData.size();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView numCajon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            numCajon = itemView.findViewById(R.id.numCajon);
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
                    if (!(modelo.contains(keyword) || empresa.contains(keyword) || direccion.contains(keyword) || telefono.contains(keyword) || id.contains(keyword) || placas.contains(keyword) ||
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


    private void setTextViewText(TextView textView, String text, String defaultText) {
        if (text.equals(null) || text.equals("") || text.equals(":null") || text.equals("null") || text.isEmpty()) {
            textView.setText(defaultText);
        } else {
            textView.setText(text);
        }
    }

}
