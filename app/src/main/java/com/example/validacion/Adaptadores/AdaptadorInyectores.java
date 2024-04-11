package com.example.validacion.Adaptadores;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

            String tipo = jsonObject2.optString("tipo", "");

            Bundle bundle = new Bundle();
            bundle.putString("ID_inyector", ID_inyector);
            bundle.putString("foto_inyector", foto_inyector);


            holder.nombreinyector.setText(nombre_inyector.toUpperCase());


            String image = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/" + foto_inyector;

/*
            Glide.with(context)
                    .load(image)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.inyectorpng)
                    .error(R.drawable.inyectorpng)
                    .into(holder.imageView2);
*/


            Glide.with(context)
                    .load(image)
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
                    LinearLayout LayoutRefacciones = customView.findViewById(R.id.LayoutRefacciones);
                    LinearLayout LayoutFotoInyector = customView.findViewById(R.id.LayoutFotoInyector);
                    LinearLayout finalizarInyector = customView.findViewById(R.id.finalizarInyector);
                    LinearLayout ContendorBotonFinalizar = customView.findViewById(R.id.ContendorBotonFinalizar);


                    ImageView iconoFoto = customView.findViewById(R.id.iconoFoto);
                    ImageView iconoCheck = customView.findViewById(R.id.iconoCheck);
                    ImageView iconoMecanico = customView.findViewById(R.id.iconoMecanico);


                    TextView textFoto = customView.findViewById(R.id.textFoto);
                    TextView textCheck = customView.findViewById(R.id.textCheck);
                    TextView textMecanico = customView.findViewById(R.id.textMecanico);


                    int colorIconoCheck = 0;
                    int colorIconoMecanico = 0;
                    int colorIconoFoto = 0;


                    if (foto_inyector.equalsIgnoreCase("Default.jpg") || foto_inyector.equalsIgnoreCase("") || foto_inyector.isEmpty() || foto_inyector == null || hay_check.equalsIgnoreCase("NO") || hay_mecanico.equalsIgnoreCase("NO")) {

                        ContendorBotonFinalizar.setVisibility(View.GONE);

                    } else if (status_inyector.equalsIgnoreCase("Finalizado")) {

                        ContendorBotonFinalizar.setVisibility(View.GONE);
                    } else {
                        ContendorBotonFinalizar.setVisibility(View.VISIBLE);

                    }


                    finalizarInyector.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View customView = LayoutInflater.from(context).inflate(R.layout.modal_confirmacion, null);
                            builder.setView(ModalRedondeado(context, customView));
                            AlertDialog dialogConfirmacion = builder.create();
                            dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogConfirmacion.show();


                            TextView textView4 = customView.findViewById(R.id.textView4);
                            textView4.setText("¿Seguro que deseas finalizar la revision de este inyector? \n\nRecuerda que no podras modificarlo despues");

                            Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                            Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);


                            buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogConfirmacion.dismiss();
                                    dialogOpcionesInyectores.dismiss();
                                    actionListener.FinalizarRevisionInyector(ID_inyector);
                                }
                            });


                            buttonCancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogConfirmacion.dismiss();
                                }
                            });
                        }
                    });


                    if (foto_inyector.equalsIgnoreCase("Default.jpg") || foto_inyector.equalsIgnoreCase("") || foto_inyector.isEmpty() || foto_inyector == null) {

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


                    LayoutRefacciones.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ModalRefacciones(status_inyector, ID_inyector, nombre_inyector);
                        }
                    });

                    LayoutCheckSalida.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialogOpcionesInyectores.dismiss();
                            //   dialogInyectoresPorServicio.dismiss();
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
                            if (status_inyector.equalsIgnoreCase("ENTREGADO") || status_inyector.equalsIgnoreCase("Finalizado") ) {
                                buttonCamara.setVisibility(View.GONE);

                            }else{
                                buttonCamara.setVisibility(View.VISIBLE);

                            }


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

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View customView = LayoutInflater.from(context).inflate(R.layout.modal_marca_modelo, null);
                            builder.setView(ModalRedondeado(context, customView));
                            AlertDialog dialogMostrarMecanicosAsignados = builder.create();
                            dialogMostrarMecanicosAsignados.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogMostrarMecanicosAsignados.show();

                            CargarMecanicos(ID_inyector, view);


                            TextView textView32 = customView.findViewById(R.id.textView32);
                            textView32.setText("Actividades De Mecanicos");


                            SinContenidoModalMecanicos = customView.findViewById(R.id.SinContenido);

                            ConstraintLayout yourConstraintLayoutId = customView.findViewById(R.id.yourConstraintLayoutId);
                            yourConstraintLayoutId.setVisibility(View.GONE);
                            FloatingActionButton botonAgregar = customView.findViewById(R.id.botonAgregar);


                            if (status_inyector.equalsIgnoreCase("ENTREGADO") || status_inyector.equalsIgnoreCase("Finalizado") ) {
                                botonAgregar.setVisibility(View.GONE);

                            } else {
                                botonAgregar.setVisibility(View.VISIBLE);

                            }


                            recyclerViewMarcasUnidades = customView.findViewById(R.id.recyclerViewMarcasUnidades);
                            adaptadorMecanicos = new AdaptadorNuevoMecanicos(listaMecanicos, context);
                            LinearLayoutManager layoutManager2 = new LinearLayoutManager(context);
                            recyclerViewMarcasUnidades.setLayoutManager(layoutManager2);

                            if (status_inyector.equalsIgnoreCase("ENTREGADO") || status_inyector.equalsIgnoreCase("Finalizado") ) {

                            } else {

                                adaptadorMecanicos.setOnItemClickListener(new AdaptadorNuevoMecanicos.OnItemClickListener() {
                                    @Override
                                    public void onMecanicoClick(String idusuario, String nombre, String observaciones, String idbitacora, String costo) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        View customView = LayoutInflater.from(context).inflate(R.layout.modal_editar_actividad, null);
                                        builder.setView(ModalRedondeado(context, customView));
                                        AlertDialog dialogEditarActividad = builder.create();
                                        dialogEditarActividad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        dialogEditarActividad.show();

                                        TextView textView34 = customView.findViewById(R.id.textView34);
                                        textView34.setText("EDITANDO ACTIVIDAD DE " + nombre.toUpperCase());


                                        Button btnCancelar = customView.findViewById(R.id.btnCancelar);
                                        Button btnAceptar = customView.findViewById(R.id.btnAceptar);

                                        EditText editTextDescripcion = customView.findViewById(R.id.editTextDescripcion);
                                        EditText editTextCosto = customView.findViewById(R.id.editTextCosto);

                                        editTextDescripcion.setText(observaciones);
                                        editTextCosto.setText(costo);

                                        btnAceptar.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                String actividadCorregida = editTextDescripcion.getText().toString();
                                                String costoEditado = editTextCosto.getText().toString();

                                                if (actividadCorregida.isEmpty() || costoEditado.isEmpty()) {
                                                    Utiles.crearToastPersonalizado(context, "Debes ingresar la descripcion de la actividad");
                                                } else {


                                                    dialogEditarActividad.dismiss();
                                                            CorregirActividad(idbitacora, ID_inyector, actividadCorregida, costoEditado, view);
                                                }

                                            }
                                        });

                                        btnCancelar.setOnClickListener(new View.OnClickListener() {
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

                                    EditText editTextCosto = customView.findViewById(R.id.editTextCosto);
                                    TextView textViewgasqwr = customView.findViewById(R.id.textViewgasqwr);


                                    editTextCosto.setVisibility(View.VISIBLE);
                                    editTextCosto.setText("0");
                                    textViewgasqwr.setVisibility(View.VISIBLE);


                                    Button btnCancelar = customView.findViewById(R.id.btnCancelar);
                                    TextView textView33 = customView.findViewById(R.id.textView33);
                                    EditText editTextDescripcion = customView.findViewById(R.id.editTextDescripcion);


                                    Button btnAceptar = customView.findViewById(R.id.btnAceptar);
                                    btnAceptar.setEnabled(false);


                                    btnAceptar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            String descripcionActividad = editTextDescripcion.getText().toString();
                                            String costoActiv = editTextCosto.getText().toString();

                                            if (descripcionActividad.equalsIgnoreCase("") || descripcionActividad.equalsIgnoreCase("null") ||
                                                    descripcionActividad.equalsIgnoreCase(null) || descripcionActividad.isEmpty() ||
                                                    costoActiv.equalsIgnoreCase("") || costoActiv.equalsIgnoreCase("null") ||
                                                    costoActiv.equalsIgnoreCase(null) || costoActiv.isEmpty() || ID_mecSelec.isEmpty()

                                            ) {
                                                Utiles.crearToastPersonalizado(context, "Debes llenar todos los campos");
                                            } else {

                                                dialogAgregarMecanico.dismiss();
                                                dialogMostrarMecanicosAsignados.dismiss();
                                                //   dialogOpcionesCoches.dismiss();
                                                dialogOpcionesInyectores.dismiss();
                                                actionListener.onAsignarManoDeObraInyector(ID_inyector, ID_mecSelec, descripcionActividad, costoActiv);
                                                // Utiles.crearToastPersonalizado(context, ID_inyector + " " + ID_mecSelec + " " + descripcionActividad + " " +costoActiv  );
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


    ConstraintLayout SinContenido;
    RecyclerView recyclerViewRefaccionesUnidades;
    AdaptadorRefaccionesDeUnidad adaptadorRefaccionesDeUnidad;

    List<JSONObject> listaRefaccionesDeUnidad = new ArrayList<>();

    private void ModalRefacciones(String estatus, String id_inyector, String nombre_inyector) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View customView = LayoutInflater.from(context).inflate(R.layout.modal_refacciones_coches, null);
        builder.setView(ModalRedondeado(context, customView));
        AlertDialog dialogBuscarRefacciones = builder.create();
        dialogBuscarRefacciones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBuscarRefacciones.show();

        FloatingActionButton botonAgregar = customView.findViewById(R.id.botonAgregar);
        Button buttonTraspasosDeUnidad = customView.findViewById(R.id.buttonTraspasosDeUnidad);

        if (estatus.equalsIgnoreCase("ENTREGADO")) {
            botonAgregar.setVisibility(View.GONE);
        } else {
            botonAgregar.setVisibility(View.VISIBLE);
        }


        buttonTraspasosDeUnidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AbrirModalConsultaTraspasos(id_inyector, nombre_inyector);
            }
        });


        SinContenido = customView.findViewById(R.id.SinContenido);
        recyclerViewRefaccionesUnidades = customView.findViewById(R.id.recyclerViewRefaccionesUnidades);

        TextView textView32 = customView.findViewById(R.id.textView32);
        textView32.setText("REFACCIONES DEL INYECTOR: #" + id_inyector);


        ConsultarHerramientasDeUnidad(id_inyector);

        adaptadorRefaccionesDeUnidad = new AdaptadorRefaccionesDeUnidad(listaRefaccionesDeUnidad, context);
        recyclerViewRefaccionesUnidades.setLayoutManager(new LinearLayoutManager(context));


        adaptadorRefaccionesDeUnidad.setOnItemClickListener(new AdaptadorRefaccionesDeUnidad.OnItemClickListener() {
            @Override
            public void onItemClick(String idrefaccion, String descripcion, String clave, String observaciones) {
                      /*  id_modeloSeleccionado = id_modelo;
                        modeloSeleccionado = nombreModelo;
                        dialogModelos.dismiss();
*/


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View customView = LayoutInflater.from(context).inflate(R.layout.modal_opciones_refacciones, null);
                builder.setView(ModalRedondeado(context, customView));
                AlertDialog dialogOpcionesDeRefaccion = builder.create();
                dialogOpcionesDeRefaccion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogOpcionesDeRefaccion.show();


                LinearLayout btnComentario = customView.findViewById(R.id.btnComentario);
                LinearLayout btnEliminar = customView.findViewById(R.id.btnEliminar);


                btnComentario.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View customView = LayoutInflater.from(context).inflate(R.layout.modal_confirmacion, null);
                        builder.setView(ModalRedondeado(context, customView));
                        AlertDialog dialogConfirmacion = builder.create();
                        dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogConfirmacion.show();


                        EditText Motivo = customView.findViewById(R.id.Motivo);
                        Motivo.setVisibility(View.VISIBLE);

                        if (observaciones.isEmpty() || observaciones == null || observaciones.equalsIgnoreCase("null") || observaciones.equalsIgnoreCase("")) {

                            Motivo.setHint("Ingresa un comentario");
                        } else {

                            Motivo.setText(observaciones);
                        }


                        TextView textView4 = customView.findViewById(R.id.textView4);
                        textView4.setText("Agrega un comentario a esta refacciòn");
                        Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                        Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);


                        if (estatus.equalsIgnoreCase("ENTREGADO")) {
                            buttonAceptar.setEnabled(false);
                            Motivo.setEnabled(false);
                        } else {
                            buttonAceptar.setEnabled(true);
                            Motivo.setEnabled(true);
                        }

                        buttonAceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String comentarioingresado = Motivo.getText().toString();


                                if (comentarioingresado.isEmpty()) {
                                    Utiles.crearToastPersonalizado(context, "Debes ingresar un comentario");
                                } else {
                                    dialogConfirmacion.dismiss();
                                    dialogOpcionesDeRefaccion.dismiss();
                                    AgregarComentario(idrefaccion, id_inyector, comentarioingresado);
                                }

                            }
                        });


                        buttonCancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogConfirmacion.dismiss();
                            }
                        });


                    }
                });


                btnEliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (estatus.equalsIgnoreCase("ENTREGADO")) {
                            Utiles.crearToastPersonalizado(context, "No puedes editar una unidad ya entregada");
                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View customView = LayoutInflater.from(context).inflate(R.layout.modal_confirmacion, null);
                            builder.setView(ModalRedondeado(context, customView));
                            AlertDialog dialogConfirmacion = builder.create();
                            dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogConfirmacion.show();

                            TextView textView4 = customView.findViewById(R.id.textView4);
                            textView4.setText("¿Seguro deseas eliminar la refaccion: " + clave + " de la unidad #" + id_inyector + " ?");

                            Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                            Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);


                            buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogConfirmacion.dismiss();
                                    dialogOpcionesDeRefaccion.dismiss();
                                    EliminarRefaccionDeUnidad(idrefaccion, id_inyector);


                                }
                            });


                            buttonCancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogConfirmacion.dismiss();
                                }
                            });
                        }
                    }
                });


            }
        });


        recyclerViewRefaccionesUnidades.setAdapter(adaptadorRefaccionesDeUnidad);

        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View customView = LayoutInflater.from(context).inflate(R.layout.modal_botones_refacciones, null);
                builder.setView(ModalRedondeado(context, customView));
                AlertDialog dialogBotonRefaccion = builder.create();
                dialogBotonRefaccion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogBotonRefaccion.show();

                LinearLayout btnRefaccionesExternas = customView.findViewById(R.id.btnRefaccionesExternas);
                LinearLayout btnFerrum = customView.findViewById(R.id.btnFerrum);
                LinearLayout AsignarTraspaso = customView.findViewById(R.id.AsignarTraspaso);

                AsignarTraspaso.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AbrirModalTraspasos(id_inyector, dialogBotonRefaccion);
                    }
                });


                btnRefaccionesExternas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View customView = LayoutInflater.from(context).inflate(R.layout.modal_agregar_refacciones, null);
                        builder.setView(ModalRedondeado(context, customView));
                        AlertDialog dialogAgregarRefacciones = builder.create();
                        dialogAgregarRefacciones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogAgregarRefacciones.show();

                        Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                        Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);


                        editTextClave = customView.findViewById(R.id.editTextClave);
                        editCantidad = customView.findViewById(R.id.editCantidad);
                        editCantidad.setText("1");
                        editTipoDeUnidad = customView.findViewById(R.id.editTipoDeUnidad);
                        editTipoDeUnidad.setText("Unidad");
                        editTextDescripcion = customView.findViewById(R.id.editTextDescripcion);
                        editTextPrecio = customView.findViewById(R.id.editTextPrecio);
                        editTextPrecio.setText("0");
                        editTextDescuento = customView.findViewById(R.id.editTextDescuento);
                        editTextDescuento.setText("0");
                        editTextImporte = customView.findViewById(R.id.editTextImporte);


                        Double suma = 0.0;
                        Double cantidades = Double.valueOf(editCantidad.getText().toString());
                        Double precioIngresado = Double.valueOf(editTextPrecio.getText().toString());
                        Double descIngresado = Double.valueOf(editTextDescuento.getText().toString());

                        suma = ((precioIngresado * cantidades) - descIngresado);

                        editTextImporte.setText("" + suma);


                        editCantidad.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                Double sumita = calculateAndSetSum();

                                editTextImporte.setText(String.valueOf(sumita));
                                //     editTextImporte.setText("" + finalSuma);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        editTextPrecio.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                Double sumita = calculateAndSetSum();

                                editTextImporte.setText(String.valueOf(sumita));
                                //     editTextImporte.setText("" + finalSuma);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        editTextDescuento.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                Double sumita = calculateAndSetSum();

                                editTextImporte.setText(String.valueOf(sumita));
                                //     editTextImporte.setText("" + finalSuma);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        buttonAceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String claveIngresada = editTextClave.getText().toString();
                                String cantidadIngresada = editCantidad.getText().toString();
                                String tipounidadIngresada = editTipoDeUnidad.getText().toString();
                                String descripcionIngresada = editTextDescripcion.getText().toString();
                                String precioIngresado = editTextPrecio.getText().toString();
                                String descuentoIngresado = editTextDescuento.getText().toString();
                                String importeTotal = editTextImporte.getText().toString();

                                if (claveIngresada.isEmpty() || precioIngresado.isEmpty() || tipounidadIngresada.isEmpty()
                                        || cantidadIngresada.isEmpty() || descripcionIngresada.isEmpty()) {
                                    Utiles.crearToastPersonalizado(context, "Debes llenar todos los campos");
                                } else {
                                    if (precioIngresado.equalsIgnoreCase("0") || precioIngresado.equalsIgnoreCase("0.0")) {
                                        Utiles.crearToastPersonalizado(context, "Debes ingresar el precio");
                                    } else {
                                        double descuentoIngresadoD = Double.parseDouble(descuentoIngresado);
                                        double importeTotalD = Double.parseDouble(importeTotal);

                                        if (descuentoIngresadoD > importeTotalD) {

                                            // Realiza la acción correspondiente si el descuento es mayor que el importe total
                                            Utiles.crearToastPersonalizado(context, "No puedes ingresar un descuento mayor al total");

                                        } else {
                                            RegistrarRefaccion(id_inyector, claveIngresada, cantidadIngresada, precioIngresado,
                                                    tipounidadIngresada, descripcionIngresada, importeTotal, descuentoIngresado,
                                                    "externo", dialogAgregarRefacciones, null, dialogBotonRefaccion);

                                        }

                                    }
                                }
                            }
                        });


                        buttonCancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogAgregarRefacciones.dismiss();
                            }
                        });
                    }
                });


                btnFerrum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View customView = LayoutInflater.from(context).inflate(R.layout.modal_buscar_refacciones, null);
                        builder.setView(ModalRedondeado(context, customView));
                        AlertDialog dialogBuscarRefacciones = builder.create();
                        dialogBuscarRefacciones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogBuscarRefacciones.show();

                        recyclerRefacciones = customView.findViewById(R.id.recyclerRefacciones);


                        EditText searchEditText = customView.findViewById(R.id.searchEditText);


                        lottieSinInternetRef = customView.findViewById(R.id.lottieSinInternetRef);

                        textSinRef = customView.findViewById(R.id.textSinRef);


                        ConsultarHerramientasFerrum();
                        adaptadorRefaccionesferrum = new AdaptadorRefaccionesFerrum(listaRefacciones, context);
                        recyclerRefacciones.setLayoutManager(new LinearLayoutManager(context));

                        adaptadorRefaccionesferrum.setOnItemClickListener(new AdaptadorRefaccionesFerrum.OnItemClickListener() {
                            @Override
                            public void onItemClick(String precio, String DESCRIPCIO, String existencia, String CLAVE) {
                            /*    id_modeloSeleccionado = id_modelo;
                                modeloSeleccionado = nombreModelo;
                                dialogModelos.dismiss();
*/


                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                View customView = LayoutInflater.from(context).inflate(R.layout.modal_agregar_refacciones, null);
                                builder.setView(ModalRedondeado(context, customView));
                                AlertDialog dialogAgregarRefacciones = builder.create();
                                dialogAgregarRefacciones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogAgregarRefacciones.show();

                                Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                                Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);

                                editTextClave = customView.findViewById(R.id.editTextClave);
                                editCantidad = customView.findViewById(R.id.editCantidad);
                                editTipoDeUnidad = customView.findViewById(R.id.editTipoDeUnidad);
                                editTextDescripcion = customView.findViewById(R.id.editTextDescripcion);
                                editTextPrecio = customView.findViewById(R.id.editTextPrecio);
                                editTextDescuento = customView.findViewById(R.id.editTextDescuento);
                                editTextImporte = customView.findViewById(R.id.editTextImporte);


                                editCantidad.setText("1");
                                editTextPrecio.setEnabled(false);
                                editTextClave.setEnabled(false);

                                editTipoDeUnidad.setText("Unidad");
                                editTextClave.setText(CLAVE);
                                editTextDescripcion.setText(DESCRIPCIO);
                                editTextPrecio.setText(precio);
                                editTextDescuento.setText("0");


                                Double suma = 0.0;
                                Double cantidades = Double.valueOf(editCantidad.getText().toString());
                                Double precioIngresado = Double.valueOf(editTextPrecio.getText().toString());
                                Double descIngresado = Double.valueOf(editTextDescuento.getText().toString());

                                suma = ((precioIngresado * cantidades) - descIngresado);

                                editTextImporte.setText("" + suma);

                                // Double finalSuma = suma;

                                editTextDescuento.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        Double sumita = calculateAndSetSum();

                                        editTextImporte.setText(String.valueOf(sumita));
                                        //     editTextImporte.setText("" + finalSuma);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                    }
                                });


                                editCantidad.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        Double sumita = calculateAndSetSum();
                                        editTextImporte.setText(String.valueOf(sumita));
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                    }
                                });


                                buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        editTextClave = customView.findViewById(R.id.editTextClave);
                                        editCantidad = customView.findViewById(R.id.editCantidad);
                                        editTipoDeUnidad = customView.findViewById(R.id.editTipoDeUnidad);
                                        editTextDescripcion = customView.findViewById(R.id.editTextDescripcion);
                                        editTextPrecio = customView.findViewById(R.id.editTextPrecio);
                                        editTextDescuento = customView.findViewById(R.id.editTextDescuento);
                                        editTextImporte = customView.findViewById(R.id.editTextImporte);


                                        String claveIngresada = editTextClave.getText().toString();
                                        String cantidadIngresada = editCantidad.getText().toString();
                                        String tipoUnidadIngresado = editTipoDeUnidad.getText().toString();
                                        String descripcionIngresada = editTextDescripcion.getText().toString();
                                        String precioIngresado = editTextPrecio.getText().toString();
                                        String descuentoIngresado = editTextDescuento.getText().toString();
                                        String importeTotal = editTextImporte.getText().toString();


                                        double descuentoIngresadoD = Double.parseDouble(descuentoIngresado);
                                        double importeTotalD = Double.parseDouble(importeTotal);

                                        if (descuentoIngresadoD > importeTotalD) {

                                            // Realiza la acción correspondiente si el descuento es mayor que el importe total
                                            Utiles.crearToastPersonalizado(context, "No puedes ingresar un descuento mayor al total");


                                        } else {
                                            // Realiza la acción correspondiente si el descuento no es mayor que el importe total
                                            RegistrarRefaccion(id_inyector, claveIngresada, cantidadIngresada, precioIngresado,
                                                    tipoUnidadIngresado, descripcionIngresada, importeTotal, descuentoIngresado,
                                                    "ferrum", dialogAgregarRefacciones, dialogBuscarRefacciones, dialogBotonRefaccion);
                                        }

                                    }
                                });


                                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialogAgregarRefacciones.dismiss();
                                    }
                                });

                            }
                        });


                        searchEditText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                adaptadorRefaccionesferrum.filter(s.toString().toLowerCase());
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });


                        recyclerRefacciones.setAdapter(adaptadorRefaccionesferrum);


                        //      ConsultarHerramientasDeUnidad(String idventa)
                        searchEditText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                adaptadorRefaccionesferrum.filter(s.toString().toLowerCase());
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });
                    }
                });

            }
        });


    }


    RecyclerView recyclerTraspasosUnidad;

    LottieAnimationView lottieNoTraspasosUnidad;
    TextView textSinTraspasosUnidad;

    AdaptadorTraspasosUnidad adaptadorTraspasosUnidad;

    List<JSONObject> listaTraspasosDeUnidad = new ArrayList<>();


    private void AbrirModalConsultaTraspasos(String id_inyector, String nombre_inyector) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View customView = LayoutInflater.from(context).inflate(R.layout.modal_consultar_traspasos_unidad, null);
        builder.setView(ModalRedondeado(context, customView));
        AlertDialog dialogTraspasosUnidad = builder.create();
        dialogTraspasosUnidad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogTraspasosUnidad.show();
        recyclerTraspasosUnidad = customView.findViewById(R.id.recyclerTraspasosUnidad);
        TextView tituloTraspasoUnidad = customView.findViewById(R.id.tituloTraspasoUnidad);

        lottieNoTraspasosUnidad = customView.findViewById(R.id.lottieNoTraspasosUnidad);
        textSinTraspasosUnidad = customView.findViewById(R.id.textSinTraspasosUnidad);


        TraspasosDeServicio(id_inyector);

        adaptadorTraspasosUnidad = new AdaptadorTraspasosUnidad(listaTraspasosDeUnidad, context);
        recyclerTraspasosUnidad.setLayoutManager(new LinearLayoutManager(context));
        recyclerTraspasosUnidad.setAdapter(adaptadorTraspasosUnidad);
        tituloTraspasoUnidad.setText("TRASPASOS PARA " + nombre_inyector.toUpperCase());


        adaptadorTraspasosUnidad.setOnItemClickListener(new AdaptadorTraspasosUnidad.OnItemClickListener() {
            @Override
            public void onItemClick(String ID_traspaso, String DOCID) {

                //   Utiles.crearToastPersonalizado(context, "Selecci " +ID_traspaso);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View customView = LayoutInflater.from(context).inflate(R.layout.modal_confirmacion, null);
                builder.setView(ModalRedondeado(context, customView));
                AlertDialog dialogConfirmacion = builder.create();
                dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogConfirmacion.show();


                TextView textView4 = customView.findViewById(R.id.textView4);
                Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);

                textView4.setText("¿ Deseas desvincular la asignación de este traspaso ?");
                buttonAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogConfirmacion.dismiss();
                        dialogTraspasosUnidad.dismiss();

                        DesvincularTraspasoUnidad(ID_traspaso, DOCID, id_inyector);
                        // Utiles.crearToastPersonalizado(context, "Jeje " + ID_traspaso + " " + DOCID);
                    }
                });


                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogConfirmacion.dismiss();
                    }
                });


            }
        });

    }


    private void TraspasosDeServicio(String id) {
        modalCargando = Utiles.ModalCargando(context, builder);

        listaTraspasosDeUnidad.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listaTraspasosDeUnidad.add(jsonObject);
                    }


                    lottieNoTraspasosUnidad.setVisibility(View.GONE);
                    textSinTraspasosUnidad.setVisibility(View.GONE);
                    recyclerTraspasosUnidad.setVisibility(View.VISIBLE);
                    adaptadorTraspasosUnidad.setFilteredData(listaTraspasosDeUnidad);
                    adaptadorTraspasosUnidad.filter("");
                    modalCargando.dismiss();

                } catch (JSONException e) {

                    lottieNoTraspasosUnidad.setVisibility(View.VISIBLE);
                    textSinTraspasosUnidad.setVisibility(View.VISIBLE);
                    recyclerTraspasosUnidad.setVisibility(View.GONE);

                    modalCargando.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lottieNoTraspasosUnidad.setVisibility(View.VISIBLE);
                textSinTraspasosUnidad.setVisibility(View.VISIBLE);
                recyclerTraspasosUnidad.setVisibility(View.GONE);
                Utiles.crearToastPersonalizado(context, "Algo fallo, revisa la conexión");
                modalCargando.dismiss();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "144");
                params.put("ID_inyector", id);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void ConsultarHerramientasDeUnidad(String id_inyector) {
        modalCargando = Utiles.ModalCargando(context, builder);
        listaRefaccionesDeUnidad.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listaRefaccionesDeUnidad.add(jsonObject);
                    }

                    if (listaRefaccionesDeUnidad.size() > 0) {

                        recyclerViewRefaccionesUnidades.setVisibility(View.VISIBLE);
                        SinContenido.setVisibility(View.GONE);

                    } else {

                        recyclerViewRefaccionesUnidades.setVisibility(View.GONE);
                        SinContenido.setVisibility(View.VISIBLE);
                    }

                    adaptadorRefaccionesDeUnidad.setFilteredData(listaRefaccionesDeUnidad);
                    adaptadorRefaccionesDeUnidad.filter("");
                    modalCargando.dismiss();

                } catch (JSONException e) {

                    modalCargando.dismiss();

                    recyclerViewRefaccionesUnidades.setVisibility(View.GONE);
                    SinContenido.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "Algo fallo, revisa la conexión");
                modalCargando.dismiss();

                recyclerViewRefaccionesUnidades.setVisibility(View.GONE);
                SinContenido.setVisibility(View.VISIBLE);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "128");
                params.put("id_inyector", id_inyector);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void CorregirActividad( String  idbitacora,String ID_inyector,String actividadCorregida,String costoEditado, View view){

        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se corrigió la actividad");
                CargarMecanicos(ID_inyector, view);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "Algo fallo, revisa la conexión");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "149");
                params.put("idbitacora", idbitacora);
                params.put("nuevoCosto", costoEditado);
                params.put("actividadCorregida", actividadCorregida);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    RecyclerView recyclerTraspasos;
    AdaptadorSelectorTraspasos adaptadorSelectorTraspasos;
    List<JSONObject> listaTraspasos = new ArrayList<>();
    LottieAnimationView lottieNoFolios;

    TextView textSinNotas;


    private void AbrirModalTraspasos(String ID_inyector, AlertDialog dialogOpcionesCoches) {
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View customView = LayoutInflater.from(context).inflate(R.layout.modal_buscar_traspaso, null);
            builder.setView(ModalRedondeado(context, customView));
            AlertDialog dialogAsignarTraspaso = builder.create();
            dialogAsignarTraspaso.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogAsignarTraspaso.show();
            recyclerTraspasos = customView.findViewById(R.id.recyclerTraspasos);

            lottieNoFolios = customView.findViewById(R.id.lottieNoFolios);
            textSinNotas = customView.findViewById(R.id.textSinNotas);

            ImageView btnFiltrarPorFecha = customView.findViewById(R.id.btnFiltrarTraspasoPorFecha);
            TextView searchEditText = customView.findViewById(R.id.searchTrapaso);


            //  BuscarFolios(formattedDate);
            BuscarTraspaso(formattedDate);


            adaptadorSelectorTraspasos = new AdaptadorSelectorTraspasos(listaTraspasos, context);
            recyclerTraspasos.setLayoutManager(new LinearLayoutManager(context));
            recyclerTraspasos.setAdapter(adaptadorSelectorTraspasos);

            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adaptadorSelectorTraspasos.filter(s.toString().toLowerCase());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });


            adaptadorSelectorTraspasos.setOnItemClickListener(new AdaptadorSelectorTraspasos.OnItemClickListener() {
                @Override
                public void onItemClick(String DOCID, String NUMERO, String TOTAL, String NOTA, String FECHA, String FECCAN, String EMISOR, String NOMBRE, String ESTADO) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View customView = LayoutInflater.from(context).inflate(R.layout.modal_confirmacion, null);
                    builder.setView(ModalRedondeado(context, customView));
                    AlertDialog dialogConfirmacion = builder.create();
                    dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogConfirmacion.show();


                    TextView textView4 = customView.findViewById(R.id.textView4);
                    Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                    Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);

                    textView4.setText("¿Deseas vincular el traspaso:  #" + NUMERO + " a este vehiculo?");


                    buttonAceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialogConfirmacion.dismiss();
                            dialogAsignarTraspaso.dismiss();
                            dialogOpcionesCoches.dismiss();
                            //  actionListener.AsignarFolio(idDocSeleccionado, id_ser_venta);

                            //   Utiles.crearToastPersonalizado(context, "Logica para asignar");

                            AsignarTraspaso(ID_inyector, DOCID, NUMERO, TOTAL, NOTA, FECHA, FECCAN, EMISOR, NOMBRE, ESTADO);

                        }
                    });


                    buttonCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogConfirmacion.dismiss();
                        }
                    });


                }
            });


            btnFiltrarPorFecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View customView = LayoutInflater.from(context).inflate(R.layout.modal_seleccionar_fecha, null);
                    builder.setView(ModalRedondeado(context, customView));
                    AlertDialog dialogFecha = builder.create();
                    dialogFecha.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogFecha.show();


                    Button btnCancelar = customView.findViewById(R.id.btnCancelar);
                    Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);
                    DatePicker datePickerFecha = customView.findViewById(R.id.datePickerFecha);


                    buttonAceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

//Falta agregar to do lo de los folios.
                            int day = datePickerFecha.getDayOfMonth();
                            int month = datePickerFecha.getMonth() + 1; // Se suma 1 ya que los meses en Android se cuentan desde 0 (enero) hasta 11 (diciembre)
                            int year = datePickerFecha.getYear();

                            //       String fechaSeleccionada = day + "/" + month + "/" + year;

                            // Formatear la fecha en el formato deseado (YYYY-MM-DD)
                            String fechaSeleccionada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);

                            dialogFecha.dismiss();


                            BuscarTraspaso(fechaSeleccionada);
                        }
                    });

                    btnCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogFecha.dismiss();
                        }
                    });


                }
            });


        }
    }


    public void AsignarTraspaso(String ID_inyector, String DOCID, String NUMERO, String TOTAL, String NOTA, String FECHA, String FECCAN, String EMISOR, String NOMBRE, String ESTADO) {
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                modalCargando.dismiss();
                ConsultarHerramientasDeUnidad(ID_inyector);
                Utiles.crearToastPersonalizado(context, "Se asignó el traspaso a este servicio");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                modalCargando.dismiss();
                Utiles.crearToastPersonalizado(context, "Algo falló, revisa la conexión");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "143");
                params.put("ID_inyector", ID_inyector);
                params.put("DOCID", DOCID);
                params.put("NOMBRE", NOMBRE);
                params.put("EMISOR", EMISOR);
                params.put("NUMERO", NUMERO);
                params.put("ESTADO", ESTADO);
                params.put("FECHA", FECHA);
                params.put("FECCAN", FECCAN);
                params.put("TOTAL", TOTAL);
                params.put("NOTA", NOTA);

                return params;
            }
        };


        Volley.newRequestQueue(context).add(postrequest);
    }


    private void DesvincularTraspasoUnidad(String ID_traspaso, String DOCID, String id_inyector) {

        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utiles.crearToastPersonalizado(context, "Se desvinculo el traspaso");
                ConsultarHerramientasDeUnidad(id_inyector);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Algo fallo, revisa la conexión");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "140");
                params.put("ID_traspaso", ID_traspaso);
                params.put("DOCID", DOCID);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void BuscarTraspaso(String fechaSeleccionada) {
        modalCargando = Utiles.ModalCargando(context, builder);
        listaTraspasos.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, urlPagos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("fallo")) {
                    lottieNoFolios.setVisibility(View.VISIBLE);
                    textSinNotas.setVisibility(View.VISIBLE);
                    recyclerTraspasos.setVisibility(View.GONE);

                    modalCargando.dismiss();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            listaTraspasos.add(jsonObject);
                        }


                        lottieNoFolios.setVisibility(View.GONE);
                        textSinNotas.setVisibility(View.GONE);
                        recyclerTraspasos.setVisibility(View.VISIBLE);
                        adaptadorSelectorTraspasos.setFilteredData(listaTraspasos);
                        adaptadorSelectorTraspasos.filter("");
                        modalCargando.dismiss();

                    } catch (JSONException e) {

                        lottieNoFolios.setVisibility(View.VISIBLE);
                        textSinNotas.setVisibility(View.VISIBLE);
                        recyclerTraspasos.setVisibility(View.GONE);

                        modalCargando.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lottieNoFolios.setVisibility(View.VISIBLE);
                textSinNotas.setVisibility(View.VISIBLE);
                recyclerTraspasos.setVisibility(View.GONE);
                Utiles.crearToastPersonalizado(context, "Algo fallo, revisa la conexión");
                modalCargando.dismiss();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "72");
                params.put("fecha", fechaSeleccionada);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void EliminarRefaccionDeUnidad(String idrefaccion, String id_inyector) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se eliminó la refacción");
                ConsultarHerramientasDeUnidad(id_inyector);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "Algo fallo, revisa la conexión");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "113");
                params.put("idrefaccion", idrefaccion);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void AgregarComentario(String idrefaccion, String id_inyector, String observaciones) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se agregó la observación a la refacción ");
                ConsultarHerramientasDeUnidad(id_inyector);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "Algo fallo, revisa la conexión");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "114");
                params.put("idrefaccion", idrefaccion);
                params.put("observaciones", observaciones);

                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    EditText editTextClave;
    EditText editCantidad;
    EditText editTipoDeUnidad;
    EditText editTextDescripcion;
    EditText editTextPrecio;
    EditText editTextDescuento;
    EditText editTextImporte;


    public interface OnActivityActionListener {
        void onActualizarFoto(String ID_inyector, AlertDialog dialogFotoInyector, AlertDialog dialogOpcionesInyectores);

        void onAsignarManoDeObraInyector(String ID_inyector, String ID_mecanico, String observaciones, String costoActiv);

        void FinalizarRevisionInyector(String ID_inyector);
    }

    String ID_mecSelec = "";
    String nombreMecSelec = "";
    AdaptadorInyectores.OnActivityActionListener actionListener;

    RecyclerView recyclerViewMarcasUnidades;
    ConstraintLayout SinContenidoModalMecanicos;

    List<JSONObject> listaMecanicos = new ArrayList<>();

    AdaptadorNuevoMecanicos adaptadorMecanicos;


    private void CargarMecanicos(String id_inyector, View view) {

        modalCargando = Utiles.ModalCargando(view.getContext(), builder);
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
                                    modalCargando.dismiss();

                                } else {
                                    SinContenidoModalMecanicos.setVisibility(View.VISIBLE);
                                    recyclerViewMarcasUnidades.setVisibility(View.GONE);
                                    modalCargando.dismiss();

                                }

                                adaptadorMecanicos.notifyDataSetChanged();
                                adaptadorMecanicos.setFilteredData(listaMecanicos);
                                adaptadorMecanicos.filter("");


                            } catch (JSONException e) {
                                SinContenidoModalMecanicos.setVisibility(View.VISIBLE);
                                modalCargando.dismiss();
                                recyclerViewMarcasUnidades.setVisibility(View.GONE);
                            }
                        } else {

                            SinContenidoModalMecanicos.setVisibility(View.VISIBLE);
                            recyclerViewMarcasUnidades.setVisibility(View.GONE);
                            Utiles.crearToastPersonalizado(context, "Algo fallo");
                            modalCargando.dismiss();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        modalCargando.dismiss();
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
        modalCargando = Utiles.ModalCargando(context, builder);
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
                            modalCargando.dismiss();

                        } catch (JSONException e) {

                            Utiles.crearToastPersonalizado(context, "Revisa la conexion");
                            modalCargando.dismiss();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        modalCargando.dismiss();
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


    private Double calculateAndSetSum() {
        Double suma = 0.0;

        String cantidadStr = editCantidad.getText().toString().trim();
        String precioStr = editTextPrecio.getText().toString().trim();
        String descuentoStr = editTextDescuento.getText().toString().trim();

        if (!cantidadStr.isEmpty() && !precioStr.isEmpty()) {
            try {
                Double cantidades = Double.parseDouble(cantidadStr);
                Double precioIngresado = Double.parseDouble(precioStr);
                Double descIngresado;

                if (descuentoStr.isEmpty()) {
                    descIngresado = 0.0;
                } else {
                    if (isValidDecimal(descuentoStr)) {
                        descIngresado = Double.parseDouble(descuentoStr);
                    } else {
                        descIngresado = 0.0;
                    }
                }

                suma = ((precioIngresado * cantidades) - descIngresado);
                DecimalFormat df = new DecimalFormat("#.##");
                suma = Double.valueOf(df.format(suma));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return suma;
    }

    private boolean isValidDecimal(String input) {
        return input.matches("^\\d*\\.?\\d+$") && !input.startsWith(".") && !input.endsWith(".");
    }


    AlertDialog modalCargando;
    AlertDialog.Builder builder;
    RecyclerView recyclerRefacciones;
    LottieAnimationView lottieSinInternetRef;
    TextView textSinRef;
    AdaptadorRefaccionesFerrum adaptadorRefaccionesferrum;
    List<JSONObject> listaRefacciones = new ArrayList<>();


    private void RegistrarRefaccion(String id_inyector, String clave, String cantidad, String precio,
                                    String unidad, String descripcion, String importe, String descuento,
                                    String tipo, AlertDialog refaccionesDeUnidades, AlertDialog dialogBuscarRefacciones, AlertDialog dialogBotonRefaccion) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ConsultarHerramientasDeUnidad(id_inyector);
                        refaccionesDeUnidades.dismiss();
                        if (dialogBuscarRefacciones != null) {
                            dialogBuscarRefacciones.dismiss();
                        }
                        dialogBotonRefaccion.dismiss();
                        Utiles.crearToastPersonalizado(context, "Se registro la refacción correctamente");

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
                params.put("opcion", "129");
                params.put("id_inyector", id_inyector);
                params.put("clave", clave);
                params.put("cantidad", cantidad);
                params.put("precio", precio);
                params.put("unidad", unidad);
                params.put("descripcion", descripcion);
                params.put("importe", importe);
                params.put("descuento", descuento);
                params.put("tipo", tipo);
                return params;
            }
        };

        RequestQueue requestQueue2 = Volley.newRequestQueue(context);
        requestQueue2.add(stringRequest2);
    }


    private void ConsultarHerramientasFerrum() {
        modalCargando = Utiles.ModalCargando(context, builder);
        listaRefacciones.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, urlPagos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("fallo")) {

                    modalCargando.dismiss();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            listaRefacciones.add(jsonObject);
                        }

                        lottieSinInternetRef.setVisibility(View.GONE);
                        textSinRef.setVisibility(View.GONE);
                        recyclerRefacciones.setVisibility(View.VISIBLE);


                        adaptadorRefaccionesferrum.setFilteredData(listaRefacciones);
                        adaptadorRefaccionesferrum.filter("");
                        modalCargando.dismiss();

                    } catch (JSONException e) {

                        modalCargando.dismiss();


                        lottieSinInternetRef.setVisibility(View.VISIBLE);
                        textSinRef.setVisibility(View.VISIBLE);
                        recyclerRefacciones.setVisibility(View.GONE);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "Algo fallo, revisa la conexión");
                modalCargando.dismiss();

                lottieSinInternetRef.setVisibility(View.VISIBLE);
                textSinRef.setVisibility(View.VISIBLE);
                recyclerRefacciones.setVisibility(View.GONE);
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "1");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    String urlPagos;
    String formattedDate;

    public AdaptadorInyectores(List<JSONObject> data, Context context, AdaptadorInyectores.OnActivityActionListener actionListener) {

        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        url = context.getResources().getString(R.string.ApiBack);
        //    this.dialogInyectoresPorServicio = dialogInyectoresPorServicio;
        this.actionListener = actionListener;

        this.builder = new AlertDialog.Builder(context);
        this.builder.setCancelable(false);
        urlPagos = context.getString(R.string.urlPagos);

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        formattedDate = dateFormat.format(currentDate);

    }

}

