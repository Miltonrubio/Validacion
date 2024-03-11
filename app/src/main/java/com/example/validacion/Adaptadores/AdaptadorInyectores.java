package com.example.validacion.Adaptadores;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
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
import com.example.validacion.ChecksFragment;
import com.example.validacion.ChecksInyectoresFragment;
import com.example.validacion.FotosInyectoresFragment;
import com.example.validacion.Objetos.SlideItem;
import com.example.validacion.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdaptadorInyectores extends RecyclerView.Adapter<AdaptadorInyectores.ViewHolder> {

    private Context context;
    private List<JSONObject> filteredData;
    private List<JSONObject> data;


    String url;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inyectores, parent, false);
        return new ViewHolder(view);

    }

    public void setFilteredData(List<JSONObject> filteredData) {
        this.filteredData = new ArrayList<>(filteredData);
        notifyDataSetChanged();
    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String ID_inyector = jsonObject2.optString("ID_inyector", "");
            String nombre_inyector = jsonObject2.optString("nombre_inyector", "");
            String ID_servicio = jsonObject2.optString("ID_servicio", "");
            String status_inyector = jsonObject2.optString("status_inyector", "");
            String foto_inyector = jsonObject2.optString("foto_inyector", "");
            String hay_check = jsonObject2.optString("hay_check", "");
            String hay_mecanico = jsonObject2.optString("hay_mecanico", "");


            Bundle bundle = new Bundle();
            bundle.putString("ID_inyector", ID_inyector);
            bundle.putString("foto_inyector", foto_inyector);


            holder.nombreinyector.setText(nombre_inyector.toUpperCase());


            String image = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/" + foto_inyector;
            Glide.with(context)
                    .load(image)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.inyectorpng)
                    .error(R.drawable.inyectorpng)
                    .into(holder.imageView2);


            holder.contenedorInyector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View customView = LayoutInflater.from(context).inflate(R.layout.modal_opciones_inyectores, null);
                    builder.setView(ModalRedondeado(context, customView));
                    AlertDialog dialogOpcionesInyectores = builder.create();
                    dialogOpcionesInyectores.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogOpcionesInyectores.show();


                    LinearLayout LayoutCheckSalida = customView.findViewById(R.id.LayoutCheckSalida);
                    LinearLayout LayoutAsigMec = customView.findViewById(R.id.LayoutAsigMec);
                    LinearLayout LayoutFotoInyector = customView.findViewById(R.id.LayoutFotoInyector);


                    ImageView iconoFoto = customView.findViewById(R.id.iconoFoto);
                    ImageView iconoCheck = customView.findViewById(R.id.iconoCheck);
                    ImageView iconoMecanico = customView.findViewById(R.id.iconoMecanico);


                    TextView textFoto = customView.findViewById(R.id.textFoto);
                    TextView textCheck = customView.findViewById(R.id.textCheck);
                    TextView textMecanico = customView.findViewById(R.id.textMecanico);


                    int colorIconoCheck = 0;
                    int colorIconoMecanico = 0;
                    int colorIconoFoto = 0;

                    if (foto_inyector.equalsIgnoreCase("Default,jpg") || foto_inyector.equalsIgnoreCase("") || foto_inyector.isEmpty() || foto_inyector == null) {

                        colorIconoFoto = ContextCompat.getColor(context, R.color.rojo);
                        textFoto.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                    } else {
                        colorIconoFoto = ContextCompat.getColor(context, R.color.azulito);
                        textFoto.setTextColor(ContextCompat.getColor(context, R.color.azulito));
                    }

                    if (hay_check.equalsIgnoreCase("SI")) {

                        textCheck.setTextColor(ContextCompat.getColor(context, R.color.azulito));
                        colorIconoCheck = ContextCompat.getColor(context, R.color.azulito);

                    } else {

                        textCheck.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                        colorIconoCheck = ContextCompat.getColor(context, R.color.rojo);
                    }

                    if (hay_mecanico.equalsIgnoreCase("SI")) {


                        textMecanico.setTextColor(ContextCompat.getColor(context, R.color.azulito));
                        colorIconoMecanico = ContextCompat.getColor(context, R.color.azulito);
                    } else {

                        textMecanico.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                        colorIconoMecanico = ContextCompat.getColor(context, R.color.rojo);
                    }


                    iconoCheck.setColorFilter(colorIconoCheck);
                    iconoMecanico.setColorFilter(colorIconoMecanico);
                    iconoFoto.setColorFilter(colorIconoFoto);


                    LayoutCheckSalida.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialogOpcionesInyectores.dismiss();
                            dialogInyectoresPorServicio.dismiss();
                            ChecksInyectoresFragment checksFragment = new ChecksInyectoresFragment();
                            checksFragment.setArguments(bundle);

                            FragmentManager fragmentManagerCheck = ((AppCompatActivity) context).getSupportFragmentManager();
                            fragmentManagerCheck.beginTransaction()
                                    .replace(R.id.frame_layoutCoches, checksFragment)
                                    .addToBackStack(null)
                                    .commit();

                        }
                    });


                    LayoutFotoInyector.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View customView = LayoutInflater.from(context).inflate(R.layout.modal_fotos_inyectores, null);
                            builder.setView(ModalRedondeado(context, customView));
                            AlertDialog dialogFotoInyector = builder.create();
                            dialogFotoInyector.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogFotoInyector.show();

                            TextView textView41 = customView.findViewById(R.id.textView41);

                            ImageView ImageViewInyector = customView.findViewById(R.id.ImageViewInyector);
                            //      ViewPagerImagenes = customView.findViewById(R.id.ViewPagerImagenes);

                            Button buttonCamara = customView.findViewById(R.id.buttonCamara);


                            String image = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/" + foto_inyector;
                            Glide.with(context)
                                    .load(image)
                                    .skipMemoryCache(false)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .placeholder(R.drawable.inyectorpng)
                                    .error(R.drawable.inyectorpng)
                                    .into(ImageViewInyector);

                                    /*
                                    .placeholder(R.drawable.imagendefault)
                                    .error(R.drawable.imagendefault)
                                     */


                            textView41.setText("Fotos del " + nombre_inyector + " #ID: " + ID_inyector);

                            buttonCamara.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    actionListener.onActualizarFoto(ID_inyector, dialogFotoInyector, dialogOpcionesInyectores);
                                }
                            });


                        }
                    });


                    LayoutAsigMec.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CargarMecanicos(ID_inyector);

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View customView = LayoutInflater.from(context).inflate(R.layout.modal_marca_modelo, null);
                            builder.setView(ModalRedondeado(context, customView));
                            AlertDialog dialogMostrarMecanicosAsignados = builder.create();
                            dialogMostrarMecanicosAsignados.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogMostrarMecanicosAsignados.show();

                            TextView textView32 = customView.findViewById(R.id.textView32);
                            textView32.setText("Actividades De Mecanicos");


                            SinContenidoModalMecanicos = customView.findViewById(R.id.SinContenido);

                            ConstraintLayout yourConstraintLayoutId = customView.findViewById(R.id.yourConstraintLayoutId);
                            yourConstraintLayoutId.setVisibility(View.GONE);
                            FloatingActionButton botonAgregar = customView.findViewById(R.id.botonAgregar);


                            if (status_inyector.equalsIgnoreCase("ENTREGADO")) {
                                botonAgregar.setVisibility(View.GONE);

                            } else {
                                botonAgregar.setVisibility(View.VISIBLE);

                            }


                            recyclerViewMarcasUnidades = customView.findViewById(R.id.recyclerViewMarcasUnidades);
                            adaptadorMecanicos = new AdaptadorNuevoMecanicos(listaMecanicos, context);
                            LinearLayoutManager layoutManager2 = new LinearLayoutManager(context);
                            recyclerViewMarcasUnidades.setLayoutManager(layoutManager2);

                            if (status_inyector.equalsIgnoreCase("ENTREGADO")) {

                            } else {

                                adaptadorMecanicos.setOnItemClickListener(new AdaptadorNuevoMecanicos.OnItemClickListener() {
                                    @Override
                                    public void onMecanicoClick(String idusuario, String nombre, String observaciones, String idbitacora) {


                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        View customView = LayoutInflater.from(context).inflate(R.layout.modal_confirmacion, null);
                                        builder.setView(ModalRedondeado(context, customView));
                                        AlertDialog dialogEditarActividad = builder.create();
                                        dialogEditarActividad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        dialogEditarActividad.show();


                                        TextView textView4 = customView.findViewById(R.id.textView4);
                                        Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                                        Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);
                                        EditText Motivo = customView.findViewById(R.id.Motivo);
                                        Motivo.setVisibility(View.VISIBLE);
                                        Motivo.setText(observaciones);


                                        buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                String actividadCorregida = Motivo.getText().toString();

                                                if (actividadCorregida.isEmpty()) {
                                                    Utiles.crearToastPersonalizado(context, "Debes ingresar la descripcion de la actividad");
                                                } else {


                                                    dialogEditarActividad.dismiss();
                                                    //         CorregirActividad(idbitacora, id_ser_venta, actividadCorregida);
                                                }

                                            }
                                        });

                                        buttonCancelar.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogEditarActividad.dismiss();
                                            }
                                        });

                                    }
                                });
                            }
                            recyclerViewMarcasUnidades.setAdapter(adaptadorMecanicos);

                            botonAgregar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    View customView = LayoutInflater.from(context).inflate(R.layout.modal_asignacion_mecanico, null);
                                    builder.setView(ModalRedondeado(context, customView));
                                    AlertDialog dialogAgregarMecanico = builder.create();
                                    dialogAgregarMecanico.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialogAgregarMecanico.show();

                                    Button btnCancelar = customView.findViewById(R.id.btnCancelar);
                                    TextView textView33 = customView.findViewById(R.id.textView33);
                                    EditText editTextDescripcion = customView.findViewById(R.id.editTextDescripcion);


                                    Button btnAceptar = customView.findViewById(R.id.btnAceptar);
                                    btnAceptar.setEnabled(false);

                                    btnAceptar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            String descripcionActividad = editTextDescripcion.getText().toString();


                                            if (descripcionActividad.equalsIgnoreCase("") || descripcionActividad.equalsIgnoreCase("null") ||
                                                    descripcionActividad.equalsIgnoreCase(null) || descripcionActividad.isEmpty()) {
                                                Utiles.crearToastPersonalizado(context, "Debes ingresar una descripcion de la actividad");
                                            } else {

                                                dialogAgregarMecanico.dismiss();
                                                dialogMostrarMecanicosAsignados.dismiss();
                                                //   dialogOpcionesCoches.dismiss();

                                              actionListener.onAsignarManoDeObraInyector( ID_inyector,  ID_mecSelec, descripcionActividad);
                                              //  Utiles.crearToastPersonalizado(context, ID_inyector + " " + ID_mecSelec + " " + descripcionActividad);
                                            }
                                        }
                                    });


                                    btnCancelar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialogAgregarMecanico.dismiss();
                                        }
                                    });


                                    textView33.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            CargarTodosLosMecanicos();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            View customView = LayoutInflater.from(context).inflate(R.layout.layout_mostrar_tipos_unidades, null);
                                            builder.setView(ModalRedondeado(context, customView));
                                            AlertDialog dialogMecanicos = builder.create();
                                            dialogMecanicos.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            dialogMecanicos.show();

                                            TextView textView30 = customView.findViewById(R.id.textView30);
                                            textView30.setText("Selecciona el mecanico que deseas asignar");

                                            RecyclerView recyclerViewTiposUnidades = customView.findViewById(R.id.recyclerViewTiposUnidades);


                                            adaptadorAsignarMecanico = new AdaptadorAsignarMecanico(listaSeleccionMecanicos, context);
                                            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                                            recyclerViewTiposUnidades.setLayoutManager(gridLayoutManager);


                                            adaptadorAsignarMecanico.setOnItemClickListener(new AdaptadorAsignarMecanico.OnItemClickListener() {
                                                @Override
                                                public void onMecanicoSeleccionado(String idusuario, String nombre) {
                                                    ID_mecSelec = idusuario;
                                                    nombreMecSelec = nombre;


                                                    btnAceptar.setEnabled(true);
                                                    dialogMecanicos.dismiss();
                                                    textView33.setText(nombreMecSelec);
                                                }
                                            });


                                            recyclerViewTiposUnidades.setAdapter(adaptadorAsignarMecanico);


                                        }
                                    });


                                }
                            });


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
        TextView nombreinyector;
        ImageView imageView2;
        ConstraintLayout contenedorInyector;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreinyector = itemView.findViewById(R.id.nombreinyector);
            contenedorInyector = itemView.findViewById(R.id.contenedorInyector);
            imageView2 = itemView.findViewById(R.id.imageView2);
        }
    }


    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(data);
        } else {
            String[] keywords = query.toLowerCase().split(" ");

            for (JSONObject item : data) {
                String saldo_inicial = item.optString("saldo_inicial", "").toLowerCase();
                String ID_saldo = item.optString("ID_saldo", "").toLowerCase();
                String nuevo_saldo = item.optString("nuevo_saldo", "").toLowerCase();


                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(saldo_inicial.contains(keyword) || ID_saldo.contains(keyword)
                            || nuevo_saldo.contains(keyword)
                    )) {
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

    AlertDialog dialogInyectoresPorServicio;


    public interface OnActivityActionListener {
        void onActualizarFoto(String ID_inyector, AlertDialog dialogFotoInyector, AlertDialog dialogOpcionesInyectores);




        void onAsignarManoDeObraInyector(String ID_inyector, String ID_mecanico, String observaciones);
    }

    String ID_mecSelec = "";
    String nombreMecSelec = "";
    AdaptadorInyectores.OnActivityActionListener actionListener;

    RecyclerView recyclerViewMarcasUnidades;
    ConstraintLayout SinContenidoModalMecanicos;

    List<JSONObject> listaMecanicos = new ArrayList<>();

    AdaptadorNuevoMecanicos adaptadorMecanicos;


    private void CargarMecanicos(String id_inyector) {
        listaMecanicos.clear();
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    listaMecanicos.add(jsonObject);
                                }


                                if (listaMecanicos.size() > 0) {
                                    SinContenidoModalMecanicos.setVisibility(View.GONE);
                                    recyclerViewMarcasUnidades.setVisibility(View.VISIBLE);


                                } else {
                                    SinContenidoModalMecanicos.setVisibility(View.VISIBLE);
                                    recyclerViewMarcasUnidades.setVisibility(View.GONE);
                                }

                                adaptadorMecanicos.notifyDataSetChanged();
                                adaptadorMecanicos.setFilteredData(listaMecanicos);
                                adaptadorMecanicos.filter("");


                            } catch (JSONException e) {
                                SinContenidoModalMecanicos.setVisibility(View.VISIBLE);
                                recyclerViewMarcasUnidades.setVisibility(View.GONE);
                            }
                        } else {

                            SinContenidoModalMecanicos.setVisibility(View.VISIBLE);
                            recyclerViewMarcasUnidades.setVisibility(View.GONE);
                            Utiles.crearToastPersonalizado(context, "Algo fallo");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utiles.crearToastPersonalizado(context, "Algo fallo");
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "124");
                params.put("ID_inyector", id_inyector);
                return params;
            }
        };

        RequestQueue requestQueue2 = Volley.newRequestQueue(context);
        requestQueue2.add(stringRequest2);
    }

    List<JSONObject> listaSeleccionMecanicos = new ArrayList<>();

    AdaptadorAsignarMecanico adaptadorAsignarMecanico;

    private void CargarTodosLosMecanicos() {
        listaSeleccionMecanicos.clear();
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                listaSeleccionMecanicos.add(jsonObject);
                            }


                            adaptadorAsignarMecanico.notifyDataSetChanged();
                            adaptadorAsignarMecanico.setFilteredData(listaSeleccionMecanicos);
                            adaptadorAsignarMecanico.filter("");


                        } catch (JSONException e) {

                            Utiles.crearToastPersonalizado(context, "Revisa la conexion");

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utiles.crearToastPersonalizado(context, "Algo fallo");
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "48");
                return params;
            }
        };

        RequestQueue requestQueue2 = Volley.newRequestQueue(context);
        requestQueue2.add(stringRequest2);
    }


    public AdaptadorInyectores(List<JSONObject> data, Context context, AlertDialog dialogInyectoresPorServicio, AdaptadorInyectores.OnActivityActionListener actionListener) {

        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        url = context.getResources().getString(R.string.ApiBack);
        this.dialogInyectoresPorServicio = dialogInyectoresPorServicio;
        this.actionListener = actionListener;
    }

}

