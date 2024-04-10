package com.example.validacion.Adaptadores;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.cache.RemovalCause;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdaptadorServiciosInyectores extends RecyclerView.Adapter<AdaptadorServiciosInyectores.ViewHolder> {

    private Context context;
    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    AdaptadorInyectores adaptadorInyectores;
    List<JSONObject> listaInyectores = new ArrayList<>();


    String url;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servicios_inyectores, parent, false);
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
            String ID_serv_inyector = jsonObject2.optString("ID_serv_inyector", "");
            String fecha_ingreso = jsonObject2.optString("fecha_ingreso", "");
            String hora_ingreso = jsonObject2.optString("hora_ingreso", "");
            String MarcaCoche = jsonObject2.optString("MarcaCoche", "");
            String ModeloCoche = jsonObject2.optString("ModeloCoche", "");
            String iddoc = jsonObject2.optString("iddoc", "");
            String foto_inyector = jsonObject2.optString("foto_inyector", "");
            String status_servicio = jsonObject2.optString("status_servicio", "");

            String nombre = jsonObject2.optString("nombre", "");
            String motivo_ingreso = jsonObject2.optString("motivo_ingreso", "");
            String listado_inyectores = jsonObject2.optString("listado_inyectores", "");


            adaptadorInyectores = new AdaptadorInyectores(listaInyectores, context, actionListenerInyectores);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
            holder.recyclerViewRecInyectores.setLayoutManager(gridLayoutManager);
            holder.recyclerViewRecInyectores.setAdapter(adaptadorInyectores);

            listaInyectores.clear();


            try {
                JSONObject jsonListadoInyectores = new JSONObject(listado_inyectores);
                JSONArray jsonArrayInyectores = jsonListadoInyectores.getJSONArray("inyectores");
                int total_registros = jsonListadoInyectores.getInt("total_registros");
                int finalizados = jsonListadoInyectores.getInt("finalizados");

                holder.textTotales.setText("FINALIZADOS: " + finalizados + " / " + total_registros);

                if (finalizados < total_registros) {
                    holder.textTotales.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                } else {
                    holder.textTotales.setTextColor(ContextCompat.getColor(context, R.color.black));

                }

                for (int i = 0; i < jsonArrayInyectores.length(); i++) {
                    JSONObject jsonObject = jsonArrayInyectores.getJSONObject(i);
                    listaInyectores.add(jsonObject);
                }

                adaptadorInyectores.notifyDataSetChanged();
                adaptadorInyectores.setFilteredData(listaInyectores);
                adaptadorInyectores.filter("");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            setStatusTextView(holder.textStatus, status_servicio);

            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date_inicio = inputFormat.parse(fecha_ingreso);

                SimpleDateFormat outputFormatFecha = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new DateFormatSymbols(new Locale("es", "ES")));
                String fecha_formateada = outputFormatFecha.format(date_inicio);

                try {
                    SimpleDateFormat inputFormatHora = new SimpleDateFormat("HH:mm:ss");
                    Date time = inputFormatHora.parse(hora_ingreso);

                    SimpleDateFormat outputFormatHora = new SimpleDateFormat("hh:mm a");
                    String hora_formateada_inicio = outputFormatHora.format(time);

                    holder.fechaServicio.setText(fecha_formateada + ". A las " + hora_formateada_inicio);
                } catch (Exception e) {
                    holder.fechaServicio.setText("Fecha no disponible");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            holder.MarcaModelo.setText(MarcaCoche.toUpperCase() + " - " + ModeloCoche.toUpperCase());
            holder.nombrecliente.setText(nombre.toUpperCase());

            holder.motivoIngreso.setText(motivo_ingreso);

            holder.contenedorServicioInyector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View customView = LayoutInflater.from(context).inflate(R.layout.modal_opciones_servicios_inyectores, null);
                    builder.setView(ModalRedondeado(context, customView));
                    AlertDialog dialogOpcionesInyectores = builder.create();
                    dialogOpcionesInyectores.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogOpcionesInyectores.show();

                    LinearLayout LayoutCambiarStatus = customView.findViewById(R.id.LayoutCambiarStatus);
                    LinearLayout LayoutConsultarPago = customView.findViewById(R.id.LayoutConsultarPago);
                    LinearLayout LayoutInsumos = customView.findViewById(R.id.LayoutInsumos);
                    LinearLayout LayoutReportes = customView.findViewById(R.id.LayoutReportes);

                    ImageView iconoPago = customView.findViewById(R.id.iconoPago);
                    TextView TextoPAGO = customView.findViewById(R.id.TextoPAGO);


                    int colorIconopagos = 0;

                    if (iddoc.equalsIgnoreCase("") ||
                            iddoc.equalsIgnoreCase("null") ||
                            iddoc.isEmpty() ||
                            iddoc.equalsIgnoreCase(null) ||
                            iddoc == null) {
                        TextoPAGO.setTextColor(ContextCompat.getColor(context, R.color.rojo));
                        colorIconopagos = ContextCompat.getColor(context, R.color.rojo);
                    } else {
                        TextoPAGO.setTextColor(ContextCompat.getColor(context, R.color.azulito));
                        colorIconopagos = ContextCompat.getColor(context, R.color.azulito);
                    }
                    iconoPago.setColorFilter(colorIconopagos);


                    LayoutConsultarPago.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ModalPagos(formattedDate, ID_serv_inyector, dialogOpcionesInyectores, iddoc);
                        }
                    });


                    LayoutCambiarStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ModalCambiarStatus(status_servicio, ID_serv_inyector, dialogOpcionesInyectores, iddoc);
                        }
                    });

                    LayoutInsumos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            ModalAgregarInsumos(status_servicio, ID_serv_inyector, dialogOpcionesInyectores, MarcaCoche, ModeloCoche);
                        }


                    });

                    LayoutReportes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ModalReportes(ID_serv_inyector);
                        }
                    });

                }
            });


        } finally {
        }

    }

    ConstraintLayout SinContenido;
    RecyclerView recyclerViewRefaccionesUnidades;

    EditText editTextClave;
    EditText editCantidad;
    EditText editTipoDeUnidad;
    EditText editTextDescripcion;
    EditText editTextPrecio;
    EditText editTextDescuento;
    EditText editTextImporte;
    RecyclerView recyclerRefacciones;
    LottieAnimationView lottieSinInternetRef;
    TextView textSinRef;
    AdaptadorRefaccionesFerrum adaptadorRefaccionesferrum;


    private void ModalAgregarInsumos(String estatus, String idServInyector, AlertDialog dialogOpcionesInyectores, String marca, String modelo) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View customView = LayoutInflater.from(context).inflate(R.layout.modal_refacciones_coches, null);
        builder.setView(ModalRedondeado(context, customView));
        AlertDialog dialogBuscarRefacciones = builder.create();
        dialogBuscarRefacciones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBuscarRefacciones.show();

        FloatingActionButton botonAgregar = customView.findViewById(R.id.botonAgregar);

        if (estatus.equalsIgnoreCase("ENTREGADO")) {
            botonAgregar.setVisibility(View.GONE);
        } else {
            botonAgregar.setVisibility(View.VISIBLE);
        }
        Button buttonTraspasosDeUnidad = customView.findViewById(R.id.buttonTraspasosDeUnidad);


        buttonTraspasosDeUnidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AbrirModalConsultaTraspasos(idServInyector, marca, modelo);
            }
        });


        TextView sinContenidoInsumos = customView.findViewById(R.id.sinContenidoInsumos);
        sinContenidoInsumos.setText("No hay insumos para este servicio");
        SinContenido = customView.findViewById(R.id.SinContenidoInsumos);
        recyclerViewRefaccionesUnidades = customView.findViewById(R.id.recyclerViewRefaccionesUnidades);

        TextView textView32 = customView.findViewById(R.id.textView32);
        textView32.setText("INSUMOS DEL SERVICIO: #" + idServInyector);


        ConsultarHerramientasDeUnidad(idServInyector);

        adaptadorRefaccionesDeUnidad = new AdaptadorInsumosDeServicio(listaRefaccionesDeUnidad, context);
        recyclerViewRefaccionesUnidades.setLayoutManager(new LinearLayoutManager(context));


        adaptadorRefaccionesDeUnidad.setOnItemClickListener(new AdaptadorInsumosDeServicio.OnItemClickListener() {
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
                                    AgregarComentarioInsumo(idrefaccion, idServInyector, comentarioingresado);
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
                            textView4.setText("¿Seguro deseas eliminar la refaccion: " + clave + " de la unidad #" + idServInyector + " ?");

                            Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                            Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);


                            buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogConfirmacion.dismiss();
                                    dialogOpcionesDeRefaccion.dismiss();
                                    EliminarInsumoDeUnidad(idrefaccion, idServInyector);


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
                        AbrirModalTraspasos(idServInyector, dialogBotonRefaccion);
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
                                            RegistrarRefaccion(idServInyector, claveIngresada, cantidadIngresada, precioIngresado,
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
                                            RegistrarRefaccion(idServInyector, claveIngresada, cantidadIngresada, precioIngresado,
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

    AdaptadorInsumosDeServicio adaptadorRefaccionesDeUnidad;
    List<JSONObject> listaRefaccionesDeUnidad = new ArrayList<>();

    private void ConsultarHerramientasDeUnidad(String ID_serv_inyector) {
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
                params.put("opcion", "132");
                params.put("ID_serv_inyector", ID_serv_inyector);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void AgregarComentarioInsumo(String idrefaccion, String ID_serv_inyector, String observaciones) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se agregó la observación a la refacción ");
                ConsultarHerramientasDeUnidad(ID_serv_inyector);
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


    private void EliminarInsumoDeUnidad(String idrefaccion, String ID_serv_inyector) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se eliminó la refacción");
                ConsultarHerramientasDeUnidad(ID_serv_inyector);
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


    RecyclerView recyclerFolios;
    LottieAnimationView lottieNoFolios;
    TextView textSinNotas;
    RecyclerView recyclerViewTiposUnidades;

    List<JSONObject> listaFolios = new ArrayList<>();
    AdaptadorFolios adaptadorFolios;


    RecyclerView recyclerTraspasosUnidad;

    LottieAnimationView lottieNoTraspasosUnidad;
    TextView textSinTraspasosUnidad;

    AdaptadorTraspasosUnidad adaptadorTraspasosUnidad;

    List<JSONObject> listaTraspasosDeUnidad = new ArrayList<>();


    private void AbrirModalConsultaTraspasos(String id_serv_inyector, String marca, String modelo) {
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


        TraspasosDeServicio(id_serv_inyector);

        adaptadorTraspasosUnidad = new AdaptadorTraspasosUnidad(listaTraspasosDeUnidad, context);
        recyclerTraspasosUnidad.setLayoutManager(new LinearLayoutManager(context));
        recyclerTraspasosUnidad.setAdapter(adaptadorTraspasosUnidad);
        tituloTraspasoUnidad.setText("TRASPASOS PARA " + marca.toUpperCase() + " " + modelo.toUpperCase());


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

                        DesvincularTraspasoUnidad(ID_traspaso, DOCID, id_serv_inyector);
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
                params.put("opcion", "142");
                params.put("id_serv_inyector", id);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void DesvincularTraspasoUnidad(String ID_traspaso, String DOCID, String id_serv_inyector) {

        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utiles.crearToastPersonalizado(context, "Se desvinculo el traspaso");
                ConsultarHerramientasDeUnidad(id_serv_inyector);

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


    private void ModalReportes(String ID_serv_inyector) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View customView = LayoutInflater.from(context).inflate(R.layout.modal_nuevo_modal_pdfs, null);
        builder.setView(ModalRedondeado(context, customView));
        AlertDialog dialogConBotones = builder.create();
        dialogConBotones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogConBotones.show();

        LinearLayout linearLayout5 = customView.findViewById(R.id.linearLayout5);
        LinearLayout LayoutPDFServicio = customView.findViewById(R.id.LayoutPDFServicio);
        LinearLayout LayoutPDFSalida = customView.findViewById(R.id.LayoutPDFSalida);
        LinearLayout LayoutPDFTecnico = customView.findViewById(R.id.LayoutPDFTecnico);
        LinearLayout PDFRefacciones = customView.findViewById(R.id.PDFRefacciones);
        LinearLayout LayoutPDFMecanicos = customView.findViewById(R.id.LayoutPDFMecanicos);

        linearLayout5.setVisibility(View.GONE);

        PDFRefacciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Map<String, String> postData = new HashMap<>();
                postData.put("opcion", "147");
                postData.put("ID_serv_inyector", ID_serv_inyector);

                new NuevoDownloadFileTask(context, postData).execute(url);
            }
        });


        LayoutPDFMecanicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, String> postData = new HashMap<>();
                postData.put("opcion", "148");
                postData.put("ID_serv_inyector", ID_serv_inyector);

                new NuevoDownloadFileTask(context, postData).execute(url);
            }
        });


        LayoutPDFServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, String> postData = new HashMap<>();
                postData.put("opcion", "145");
                postData.put("ID_serv_inyector", ID_serv_inyector);

                new NuevoDownloadFileTask(context, postData).execute(url);

            }
        });

        LayoutPDFSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, String> postData = new HashMap<>();
                postData.put("opcion", "146");
                postData.put("ID_serv_inyector", ID_serv_inyector);

                new NuevoDownloadFileTask(context, postData).execute(url);

            }
        });
/*
        LayoutPDFTecnico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, String> postData = new HashMap<>();
                postData.put("opcion", "80");
                postData.put("ID_serv_inyector", ID_serv_inyector);

                new NuevoDownloadFileTask(context, postData).execute(url);

            }
        });
*/
    }

    private void ModalPagos(String formattedDate, String ID_serv_inyector, AlertDialog ModalOpcionesInyectores, String iddoc) {

        if (iddoc.equalsIgnoreCase("") ||
                iddoc.equalsIgnoreCase("null") ||
                iddoc.isEmpty() ||
                iddoc.equalsIgnoreCase(null) ||
                iddoc == null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View customView = LayoutInflater.from(context).inflate(R.layout.modal_buscar_folios, null);
            builder.setView(ModalRedondeado(context, customView));
            AlertDialog dialogBuscarFolios = builder.create();
            dialogBuscarFolios.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogBuscarFolios.show();
            recyclerFolios = customView.findViewById(R.id.recyclerFolios);

            lottieNoFolios = customView.findViewById(R.id.lottieNoFolios);
            textSinNotas = customView.findViewById(R.id.textSinNotas);

            ImageView btnFiltrarPorFecha = customView.findViewById(R.id.btnFiltrarPorFecha);
            TextView searchEditText = customView.findViewById(R.id.searchEditText);


            BuscarFolios(formattedDate);
            adaptadorFolios = new AdaptadorFolios(listaFolios, context);
            recyclerFolios.setLayoutManager(new LinearLayoutManager(context));
            recyclerFolios.setAdapter(adaptadorFolios);

            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adaptadorFolios.filter(s.toString().toLowerCase());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });


            adaptadorFolios.setOnItemClickListener(new AdaptadorFolios.OnItemClickListener() {
                @Override
                public void onItemClick(String DOCID, String NUMERO) {
                    String idDocSeleccionado = DOCID;
                    String numSeleccionado = NUMERO;


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View customView = LayoutInflater.from(context).inflate(R.layout.modal_confirmacion, null);
                    builder.setView(ModalRedondeado(context, customView));
                    AlertDialog dialogConfirmacion = builder.create();
                    dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogConfirmacion.show();


                    TextView textView4 = customView.findViewById(R.id.textView4);
                    Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                    Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);

                    textView4.setText("¿Deseas vincular la nota:  #" + numSeleccionado + " a este vehiculo?");


                    buttonAceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialogConfirmacion.dismiss();
                            dialogBuscarFolios.dismiss();
                            ModalOpcionesInyectores.dismiss();
                            actionListener.AsignarFolio(idDocSeleccionado, ID_serv_inyector);
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

                            int day = datePickerFecha.getDayOfMonth();
                            int month = datePickerFecha.getMonth() + 1;
                            int year = datePickerFecha.getYear();

                            String fechaSeleccionada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);

                            dialogFecha.dismiss();


                            BuscarFolios(fechaSeleccionada);
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

        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View customView = LayoutInflater.from(context).inflate(R.layout.layout_mostrar_tipos_unidades, null);
            builder.setView(ModalRedondeado(context, customView));
            AlertDialog dialogBuscarFolios = builder.create();
            dialogBuscarFolios.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogBuscarFolios.show();
            recyclerViewTiposUnidades = customView.findViewById(R.id.recyclerViewTiposUnidades);

            TextView textView30 = customView.findViewById(R.id.textView30);
            textView30.setText("Registro de pagos para la nota " + iddoc);


            textSinPago = customView.findViewById(R.id.textSinPago);
            lottieNopagos = customView.findViewById(R.id.lottieNopagos);


            ConsultarPagosDeUnaNota(iddoc);
            adaptadorPagos = new AdaptadorPagos(listaPagos, context);
            recyclerViewTiposUnidades.setLayoutManager(new LinearLayoutManager(context));
            recyclerViewTiposUnidades.setAdapter(adaptadorPagos);

        }

    }

    TextView textSinPago;
    LottieAnimationView lottieNopagos;

    private void ConsultarPagosDeUnaNota(String iddoc) {
        modalCargando = Utiles.ModalCargando(context, builder);
        listaPagos.clear();
        listaFolios.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, urlPagos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("fallo")) {
                    textSinPago.setVisibility(View.VISIBLE);
                    lottieNopagos.setVisibility(View.VISIBLE);
                    recyclerViewTiposUnidades.setVisibility(View.GONE);


                    modalCargando.dismiss();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            listaPagos.add(jsonObject);
                        }


                        textSinPago.setVisibility(View.GONE);
                        lottieNopagos.setVisibility(View.GONE);
                        recyclerViewTiposUnidades.setVisibility(View.VISIBLE);

                        adaptadorPagos.setFilteredData(listaPagos);
                        adaptadorPagos.filter("");
                        modalCargando.dismiss();

                    } catch (JSONException e) {

                        textSinPago.setVisibility(View.VISIBLE);
                        lottieNopagos.setVisibility(View.VISIBLE);
                        recyclerViewTiposUnidades.setVisibility(View.GONE);
                        modalCargando.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                textSinPago.setVisibility(View.VISIBLE);
                lottieNopagos.setVisibility(View.VISIBLE);
                recyclerViewTiposUnidades.setVisibility(View.GONE);
                Utiles.crearToastPersonalizado(context, "Algo fallo, revisa la conexión");
                modalCargando.dismiss();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "71");
                params.put("iddoc", iddoc);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void RegistrarRefaccion(String ID_servicio_inyector, String clave, String cantidad, String precio,
                                    String unidad, String descripcion, String importe, String descuento,
                                    String tipo, AlertDialog refaccionesDeUnidades, AlertDialog dialogBuscarRefacciones, AlertDialog dialogBotonRefaccion) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ConsultarHerramientasDeUnidad(ID_servicio_inyector);
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
                params.put("opcion", "130");
                params.put("ID_servicio_inyector", ID_servicio_inyector);
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


    private void BuscarFolios(String fechaSeleccionada) {
        modalCargando = Utiles.ModalCargando(context, builder);
        listaPagos.clear();
        listaFolios.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, urlPagos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("fallo")) {
                    lottieNoFolios.setVisibility(View.VISIBLE);
                    textSinNotas.setVisibility(View.VISIBLE);
                    recyclerFolios.setVisibility(View.GONE);

                    modalCargando.dismiss();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            listaFolios.add(jsonObject);
                        }


                        lottieNoFolios.setVisibility(View.GONE);
                        textSinNotas.setVisibility(View.GONE);
                        recyclerFolios.setVisibility(View.VISIBLE);
                        adaptadorFolios.setFilteredData(listaFolios);
                        adaptadorFolios.filter("");
                        modalCargando.dismiss();

                    } catch (JSONException e) {

                        lottieNoFolios.setVisibility(View.VISIBLE);
                        textSinNotas.setVisibility(View.VISIBLE);
                        recyclerFolios.setVisibility(View.GONE);

                        modalCargando.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lottieNoFolios.setVisibility(View.VISIBLE);
                textSinNotas.setVisibility(View.VISIBLE);
                recyclerFolios.setVisibility(View.GONE);

                Utiles.crearToastPersonalizado(context, "Algo fallo, revisa la conexión");
                modalCargando.dismiss();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "5");
                params.put("fecconsulta", fechaSeleccionada);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }

    List<JSONObject> listaRefacciones = new ArrayList<>();

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


    private void ModalCambiarStatus(String estatus, String ID_serv_inyector, AlertDialog dialogOpcionesCoches, String iddoc) {

        if (estatus.equalsIgnoreCase("ENTREGADO")) {

            Utiles.crearToastPersonalizado(context, "No puedes cambiar el estado de una unidad entregada");

        } else {


            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View customView = LayoutInflater.from(context).inflate(R.layout.cambiarestadounidad, null);
            builder.setView(ModalRedondeado(context, customView));
            AlertDialog dialogEstadoUnidad = builder.create();
            dialogEstadoUnidad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogEstadoUnidad.show();


            LinearLayout LayoutDiagnostico = customView.findViewById(R.id.LayoutDiagnostico);
            LinearLayout LayoutEsperandoRef = customView.findViewById(R.id.LayoutEsperandoRef);
            LinearLayout LayoutEnServicio = customView.findViewById(R.id.LayoutEnServicio);
            LinearLayout LayoutPruebaRuta = customView.findViewById(R.id.LayoutPruebaRuta);
            LinearLayout LayoutLavadoY = customView.findViewById(R.id.LayoutLavadoY);
            LinearLayout LayoutListoEntregar = customView.findViewById(R.id.LayoutListoEntregar);
            LinearLayout LayoutENTREGADO = customView.findViewById(R.id.LayoutENTREGADO);

            if (estatus.equalsIgnoreCase("LISTO PARA ENTREGA")) {
                LayoutENTREGADO.setVisibility(View.VISIBLE);
                LayoutListoEntregar.setVisibility(View.GONE);
            } else {
                LayoutListoEntregar.setVisibility(View.VISIBLE);
                LayoutENTREGADO.setVisibility(View.GONE);
            }


            LayoutENTREGADO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View customView = LayoutInflater.from(context).inflate(R.layout.modal_confirmacion, null);
                    builder.setView(ModalRedondeado(context, customView));
                    AlertDialog dialogConfirmacion = builder.create();
                    dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogConfirmacion.show();

                    TextView textView4 = customView.findViewById(R.id.textView4);
                    textView4.setText("¿Deseas marcar como entregada esta unidad? \n\nUna vez entregada no podras editarla");


                    Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                    Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);

                    buttonAceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            ValidarQueEstePagado(iddoc, ID_serv_inyector, dialogConfirmacion, dialogEstadoUnidad, dialogOpcionesCoches);

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
            LayoutListoEntregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (iddoc.equalsIgnoreCase("") || iddoc.equalsIgnoreCase("null") || iddoc.isEmpty() || iddoc.equalsIgnoreCase(null) || iddoc == null) {
                        Utiles.crearToastPersonalizado(context, "Debes asignar una nota");


                    } else {

                        ValidarListos(ID_serv_inyector, dialogEstadoUnidad, dialogOpcionesCoches);

                        /*
                        dialogEstadoUnidad.dismiss();
                        dialogOpcionesCoches.dismiss();

                        */
                    }
                }
            });


            LayoutLavadoY.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionListener.cambiarEstado(ID_serv_inyector, "LAVADO Y DETALLADO");
                    dialogEstadoUnidad.dismiss();
                    dialogOpcionesCoches.dismiss();
                }
            });

            LayoutPruebaRuta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionListener.cambiarEstado(ID_serv_inyector, "PRUEBA DE RUTA");
                    dialogEstadoUnidad.dismiss();
                    dialogOpcionesCoches.dismiss();
                }
            });


            LayoutEnServicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionListener.cambiarEstado(ID_serv_inyector, "EN SERVICIO");
                    dialogEstadoUnidad.dismiss();
                    dialogOpcionesCoches.dismiss();
                }
            });

            LayoutEsperandoRef.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionListener.cambiarEstado(ID_serv_inyector, "EN ESPERA DE REFACCIONES");
                    dialogEstadoUnidad.dismiss();
                    dialogOpcionesCoches.dismiss();
                }
            });

            LayoutDiagnostico.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    actionListener.cambiarEstado(ID_serv_inyector, "DIAGNOSTICO");
                    dialogEstadoUnidad.dismiss();
                    dialogOpcionesCoches.dismiss();
                }
            });

        }
    }


    List<JSONObject> listaPagos = new ArrayList<>();
    AdaptadorPagos adaptadorPagos;

    String urlPagos;

    private void ValidarQueEstePagado(String iddoc, String id_ser_venta, AlertDialog dialogConfirmacion, AlertDialog dialogEstadoUnidad, AlertDialog dialogOpcionesCoches) {
        modalCargando = Utiles.ModalCargando(context, builder);
        listaPagos.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, urlPagos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("fallo")) {
                    Utiles.crearToastPersonalizado(context, "Aun no hay pagos para esta nota");
                    modalCargando.dismiss();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            listaPagos.add(jsonObject);
                        }
                        if (listaPagos.size() > 0) {
                            JSONObject ultimoPago = listaPagos.get(listaPagos.size() - 1);
                            double adeudo = ultimoPago.optDouble("ADEUDO", 0.0);

                            if (adeudo == 0.0) {
                                dialogConfirmacion.dismiss();
                                dialogEstadoUnidad.dismiss();
                                dialogOpcionesCoches.dismiss();
                                actionListener.cambiarEstado(id_ser_venta, "ENTREGADO");
                            } else {

                                Utiles.crearToastPersonalizado(context, "Aun no se ha liquidado esta nota");

                            }
                        }
                        modalCargando.dismiss();
                    } catch (JSONException e) {
                        Utiles.crearToastPersonalizado(context, "Algo fallo, revisa la conexión");
                        modalCargando.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Algo fallo, revisa la conexión");
                modalCargando.dismiss();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "71");
                params.put("iddoc", iddoc);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void ValidarListos(String ID_serv_inyector, AlertDialog dialogEstadoUnidad, AlertDialog dialogOpcionesCoches) {
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    int totalRegistros = jsonResponse.getInt("total_registros");
                    int finalizados = jsonResponse.getInt("finalizados");


                    int faltan = totalRegistros - finalizados;


                    if (finalizados < totalRegistros) {
                        Utiles.crearToastPersonalizado(context, "Te falta revisar " + faltan + " inyector");
                    } else {
                        dialogEstadoUnidad.dismiss();
                        dialogOpcionesCoches.dismiss();
                        actionListener.cambiarEstado(ID_serv_inyector, "LISTO PARA ENTREGA");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utiles.crearToastPersonalizado(context, "Error al procesar la respuesta");
                } finally {
                    modalCargando.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Algo fallo, revisa la conexión");
                modalCargando.dismiss();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "134");
                params.put("ID_serv_inyector", ID_serv_inyector);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void AbrirModalTraspasos(String ID_ser_venta, AlertDialog dialogOpcionesCoches) {
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

                            AsignarTraspaso(ID_ser_venta, DOCID, NUMERO, TOTAL, NOTA, FECHA, FECCAN, EMISOR, NOMBRE, ESTADO);

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


    RecyclerView recyclerTraspasos;
    AdaptadorSelectorTraspasos adaptadorSelectorTraspasos;
    List<JSONObject> listaTraspasos = new ArrayList<>();


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


    public void AsignarTraspaso(String ID_serv_inyector, String DOCID, String NUMERO, String TOTAL, String NOTA, String FECHA, String FECCAN, String EMISOR, String NOMBRE, String ESTADO) {
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                modalCargando.dismiss();
                ConsultarHerramientasDeUnidad(ID_serv_inyector);
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
                params.put("opcion", "141");
                params.put("ID_serv_inyector", ID_serv_inyector);
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


    private void setStatusTextView(TextView textView, String status) {
        if (!status.equals("null")) {
            textView.setText(status.toUpperCase());

            if (status.equalsIgnoreCase("pendiente")) {
                textView.setBackgroundResource(R.drawable.textview_outline3);
            } else if (status.equalsIgnoreCase("Finalizado")) {

                textView.setBackgroundResource(R.drawable.textview_outline4);
            } else if (status.equalsIgnoreCase("Servicios programado")) {

                textView.setBackgroundResource(R.drawable.textview_outline4);
            } else if (status.equalsIgnoreCase("Cita programada")) {

                textView.setBackgroundResource(R.drawable.textview_outline2);
            } else if (status.equalsIgnoreCase("Diagnostico")) {

                textView.setBackgroundResource(R.drawable.textview_outline5);
            } else if (status.equalsIgnoreCase("En espera")) {

                textView.setBackgroundResource(R.drawable.textview_outlinegris);
            } else if (status.equalsIgnoreCase("En servicio")) {
                textView.setBackgroundResource(R.drawable.textview_outline3);
            } else if (status.equalsIgnoreCase("SubirFotosUnidadesActivity de ruta")) {

                textView.setBackgroundResource(R.drawable.textview_outlinenegro);
                int colorBlanco = ContextCompat.getColor(context, R.color.white);
                textView.setTextColor(colorBlanco);

            } else if (status.equalsIgnoreCase("Lavado y detallado")) {

                textView.setBackgroundResource(R.drawable.textview_outline2);
            } else if (status.equalsIgnoreCase("Listo para entrega")) {

                textView.setBackgroundResource(R.drawable.textview_outline4);
            } else if (status.equalsIgnoreCase("Entregado")) {

                textView.setBackgroundResource(R.drawable.textview_outline4);
            } else {
                textView.setBackgroundResource(R.drawable.textview_outline2);
            }
        } else {
            textView.setText("Status no disponible");
            textView.setBackgroundResource(R.drawable.textview_outline5);
        }
    }


    @Override
    public int getItemCount() {

        return filteredData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombrecliente;
        TextView MarcaModelo;
        TextView fechaServicio;
        ConstraintLayout contenedorServicioInyector;

        TextView motivoIngreso;

        TextView textStatus;
        RecyclerView recyclerViewRecInyectores;

        TextView textTotales;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombrecliente = itemView.findViewById(R.id.nombrecliente);
            MarcaModelo = itemView.findViewById(R.id.MarcaModelo);
            nombrecliente = itemView.findViewById(R.id.nombrecliente);
            fechaServicio = itemView.findViewById(R.id.fechaServicio);
            contenedorServicioInyector = itemView.findViewById(R.id.contenedorServicioInyector);
            motivoIngreso = itemView.findViewById(R.id.motivoIngreso);
            recyclerViewRecInyectores = itemView.findViewById(R.id.recyclerViewRecInyectores);
            textStatus = itemView.findViewById(R.id.textStatus);
            textTotales = itemView.findViewById(R.id.textTotales);
        }
    }


    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(data);
        } else {
            String[] keywords = query.toLowerCase().split(" ");

            for (JSONObject item : data) {
                String ID_serv_inyector = item.optString("ID_serv_inyector", "").toLowerCase();
                String ID_unidad = item.optString("ID_unidad", "").toLowerCase();
                String status_servicio = item.optString("status_servicio", "").toLowerCase();
                String motivo_ingreso = item.optString("motivo_ingreso", "").toLowerCase();
                String MarcaCoche = item.optString("MarcaCoche", "").toLowerCase();
                String ModeloCoche = item.optString("ModeloCoche", "").toLowerCase();
                String nombre = item.optString("nombre", "").toLowerCase();


                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(ID_serv_inyector.contains(keyword) || status_servicio.contains(keyword) || ModeloCoche.contains(keyword) || nombre.contains(keyword)
                            || ID_unidad.contains(keyword) || motivo_ingreso.contains(keyword) || MarcaCoche.contains(keyword)
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


    AlertDialog.Builder builder;
    AlertDialog modalCargando;


    AdaptadorInyectores.OnActivityActionListener actionListenerInyectores;


    public interface OnActivityActionListener {
        void cambiarEstado(String ID_serv_inyector, String nuevoStatus);

        void AsignarFolio(String iddocSelec, String ID_serv_inyector);
    }

    AdaptadorServiciosInyectores.OnActivityActionListener actionListener;
    String formattedDate;


    public AdaptadorServiciosInyectores(List<JSONObject> data, Context context, AdaptadorInyectores.OnActivityActionListener actionListenerInyectores, AdaptadorServiciosInyectores.OnActivityActionListener actionListener) {

        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        url = context.getResources().getString(R.string.ApiBack);

        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        this.actionListenerInyectores = actionListenerInyectores;
        this.urlPagos = context.getString(R.string.urlPagos);
        this.actionListener = actionListener;


        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        formattedDate = dateFormat.format(currentDate);
    }

}

