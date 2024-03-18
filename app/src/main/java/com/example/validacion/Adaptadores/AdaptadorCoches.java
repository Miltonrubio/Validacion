package com.example.validacion.Adaptadores;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.validacion.CheckListFragment;
import com.example.validacion.CheckListSalidaFragment;
import com.example.validacion.CheckListTecnicoFragment;
import com.example.validacion.ChecksFragment;
import com.example.validacion.DetalleFragment;
import com.example.validacion.Objetos.Mecanicos;
import com.example.validacion.Objetos.Refacciones;
import com.example.validacion.SubirFotosUnidadesActivity;
import com.example.validacion.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdaptadorCoches extends RecyclerView.Adapter<AdaptadorCoches.ViewHolder> {
    private Context context;
    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    String url;
    RecyclerView recyclerViewMarcasUnidades;


    AdaptadorNuevoMecanicos adaptadorMecanicos;
    //List<Mecanicos> listaMecanicos = new ArrayList<>();
    List<JSONObject> listaMecanicos = new ArrayList<>();

    ConstraintLayout SinContenidoModalMecanicos;

    AdaptadorAsignarMecanico adaptadorAsignarMecanico;
    List<JSONObject> listaSeleccionMecanicos = new ArrayList<>();

    String ID_mecSelec;
    String nombreMecSelec;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coches, parent, false);
        return new ViewHolder(view);

    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        JSONObject jsonObject2 = filteredData.get(position);
        String id_ref = jsonObject2.optString("id_ser_refacciones", "");
        String id_check_mecanico = jsonObject2.optString("id_check_mecanico", "");
        String id_ser_venta = jsonObject2.optString("id_ser_venta", "");
        String id_ser_cliente = jsonObject2.optString("id_ser_cliente", "");


        String foto = jsonObject2.optString("foto", "");
        String marca = jsonObject2.optString("marcaI", "");
        String modelo = jsonObject2.optString("modeloI", "");
        String kilometraje = jsonObject2.optString("kilometraje");
        String placa = jsonObject2.optString("placasI", "");
        String motivo = jsonObject2.optString("motivoingreso", "");

        String estatus = jsonObject2.optString("estatus", "");
        String fecha_ingreso = jsonObject2.optString("fecha_ingreso", "");
        String hora_ingreso = jsonObject2.optString("hora_ingreso", "");
        String nombre = jsonObject2.optString("NomCliente", "");
        String email = jsonObject2.optString("email", "");
        String telefono = jsonObject2.optString("telefono", "");
        String gasolina = jsonObject2.optString("gasolina", "");
        String tipounidad = jsonObject2.optString("tipounidad", "");
        String motorI = jsonObject2.optString("motorI", "");
        String vinI = jsonObject2.optString("vinI", "");
        String domicilio = jsonObject2.optString("domicilio", "");
        String anioI = jsonObject2.optString("anioI", "");

        String hayfoto = jsonObject2.optString("hayfoto", "");
        String haymecanico = jsonObject2.optString("haymecanico", "");
        String haypago = jsonObject2.optString("haypago", "");
        String haychecklist = jsonObject2.optString("haychecklist", "");
        String haycheckTecnico = jsonObject2.optString("haycheckTecnico", "");
        String haycheckSalida = jsonObject2.optString("haycheckSalida", "");
        String iddoc = jsonObject2.optString("iddoc", "");

        String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/" + foto;

        Bundle bundle = new Bundle();
        bundle.putString("marca", marca);
        bundle.putString("modelo", modelo);
        bundle.putString("motivo", motivo);
        bundle.putString("fecha_ingreso", fecha_ingreso);
        bundle.putString("status", estatus);
        bundle.putString("hora_ingreso", hora_ingreso);
        bundle.putString("idventa", id_ser_venta);
        bundle.putString("gasolina", gasolina);
        bundle.putString("tipounidad", tipounidad);
        bundle.putString("kilometraje", kilometraje);
        bundle.putString("nombre", nombre);
        bundle.putString("email", email);
        bundle.putString("telefono", telefono);
        bundle.putString("anioI", anioI);
        bundle.putString("motorI", motorI);
        bundle.putString("vinI", vinI);
        bundle.putString("domicilio", domicilio);
        bundle.putString("id_ser_cliente", id_ser_cliente);


        setTextViewText(holder.textMarca, marca.toUpperCase() + " - " + modelo.toUpperCase(), "Marca no disponible");
        setTextViewText(holder.textModelo, motivo.toUpperCase(), "Motivo no disponible");
        setTextViewText(holder.textPlaca, placa, "SIN PLACA");
        setTextViewText(holder.textDueño, nombre, "Dueño no disponible");
        setStatusTextView(holder.textStatus, estatus);

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

                setTextViewText(holder.textFecha, fecha_formateada + ". A las " + hora_formateada_inicio, "Fecha no disponible");
            } catch (Exception e) {

                setTextViewText(holder.textFecha, fecha_formateada, "Fecha no disponible");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        loadImage(holder.imageViewCoches, imageUrl);

        /*
        holder.botonDesplegable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mostrarModalOpciones(context, bundle, estatus, id_ser_venta, hayfoto, haymecanico, haychecklist, haycheckTecnico, haycheckSalida, iddoc, haypago);

                //       showPopupMenu(view, bundle);
            }
        });
*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mostrarModalOpciones(context, bundle, estatus, id_ser_venta, hayfoto, haymecanico, haychecklist, haycheckTecnico, haycheckSalida, iddoc, haypago, id_ser_cliente, tipounidad);


                /*

                DetalleFragment detalleFragment = new DetalleFragment();
                detalleFragment.setArguments(bundle);

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layoutCoches, detalleFragment)
                        .addToBackStack(null)
                        .commit();
*/
            }
        });
/*
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                mostrarModalOpciones(context, bundle, estatus, id_ser_venta, hayfoto, haymecanico, haychecklist, haycheckTecnico, haycheckSalida, iddoc, haypago);

                showPopupMenu(view, bundle);
                return false;
            }
        });
*/
    }


    @Override
    public int getItemCount() {

        //return filteredData.size();
        return filteredData.size();

    }


    private void mostrarModalOpciones(Context context, Bundle bundle, String estatus, String id_ser_venta, String hayfoto, String haymecanico, String haychecklist, String haycheckTecnico, String haycheckSalida, String iddoc, String haypago, String id_ser_cliente, String tipounidad) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View customView = LayoutInflater.from(context).inflate(R.layout.modal_nuevos_botones_coches, null);
        builder.setView(ModalRedondeado(context, customView));
        AlertDialog dialogOpcionesCoches = builder.create();
        dialogOpcionesCoches.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogOpcionesCoches.show();

        LinearLayout LayoutDetalles = customView.findViewById(R.id.LayoutDetalles);
        LinearLayout LayoutCheckIngreso = customView.findViewById(R.id.LayoutCheckIngreso);
        LinearLayout LayoutTomarFotos = customView.findViewById(R.id.LayoutTomarFotos);
        LinearLayout LayoutChecksSalida = customView.findViewById(R.id.LayoutChecksSalida);
        LinearLayout LayoutCheckListTecnico = customView.findViewById(R.id.LayoutCheckListTecnico);
        LinearLayout LayoutCambiarStatus = customView.findViewById(R.id.LayoutCambiarStatus);
        LinearLayout LayoutAsignarMecanico = customView.findViewById(R.id.LayoutAsignarMecanico);
        LinearLayout LayoutGestionarInyectores = customView.findViewById(R.id.LayoutGestionarInyectores);
        LinearLayout LayoutConsultarPago = customView.findViewById(R.id.LayoutConsultarPago);
        LinearLayout LayoutRefacciones = customView.findViewById(R.id.LayoutRefacciones);
        LinearLayout LayoutReportes = customView.findViewById(R.id.LayoutReportes);


        TextView TextoPAGO = customView.findViewById(R.id.TextoPAGO);
        ImageView iconoPago = customView.findViewById(R.id.iconoPago);

        TextView TextoASIGNARMECANICOS = customView.findViewById(R.id.TextoASIGNARMECANICOS);
        TextView TextoSUBIRFOTOS = customView.findViewById(R.id.TextoSUBIRFOTOS);
        ImageView iconoCamara = customView.findViewById(R.id.iconoCamara);
        ImageView iconoMecanico = customView.findViewById(R.id.iconoMecanico);
        ImageView iconoCheckIngreso = customView.findViewById(R.id.iconoCheckIngreso);
        TextView tvCheckIngreso = customView.findViewById(R.id.tvCheckIngreso);


        ImageView iconoCheckTecnico = customView.findViewById(R.id.iconoCheckTecnico);
        TextView textCheckTecnico = customView.findViewById(R.id.textCheckTecnico);


        ImageView iconoCheckSalida = customView.findViewById(R.id.iconoCheckSalida);
        TextView textCheckSalida = customView.findViewById(R.id.textCheckSalida);

        int colorIconoCamara = 0;
        int colorIconoMecanico = 0;
        int colorIconoSalida = 0;
        int colorIconoCheck = 0;
        int colorIconoTecnico = 0;
        int colorIconoCerdito = 0;

        LayoutGestionarInyectores.setVisibility(View.GONE);
/*
        if (tipounidad.equalsIgnoreCase("Inyectores") || tipounidad.equalsIgnoreCase("Inyector")) {
            LayoutGestionarInyectores.setVisibility(View.VISIBLE);
            LayoutAsignarMecanico.setVisibility(View.GONE);
        } else {
            LayoutAsignarMecanico.setVisibility(View.VISIBLE);
        }


        LayoutGestionarInyectores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConsultarInyectoresDeUnidad(id_ser_venta);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View customView = LayoutInflater.from(context).inflate(R.layout.modal_inyectores, null);
                builder.setView(ModalRedondeado(context, customView));
                AlertDialog dialogInyectoresPorServicio = builder.create();
                dialogInyectoresPorServicio.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogInyectoresPorServicio.show();

                RecyclerView recyclerViewInyectores = customView.findViewById(R.id.recyclerViewInyectores);

                adaptadorInyectores = new AdaptadorInyectores(listaInyectores, context, dialogInyectoresPorServicio);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                recyclerViewInyectores.setLayoutManager(gridLayoutManager);
                recyclerViewInyectores.setAdapter(adaptadorInyectores);
            }
        });
*/

        //  if (iddoc.equalsIgnoreCase("") || iddoc.isEmpty() || iddoc.equalsIgnoreCase("null") || iddoc.equalsIgnoreCase(null)) {

        if (haypago.equalsIgnoreCase("NO")) {
            TextoPAGO.setTextColor(ContextCompat.getColor(context, R.color.rojo));
            colorIconoCerdito = ContextCompat.getColor(context, R.color.rojo);
            TextoPAGO.setText("ASIGNAR NOTA");

        } else {
            TextoPAGO.setTextColor(ContextCompat.getColor(context, R.color.azulito));
            colorIconoCerdito = ContextCompat.getColor(context, R.color.azulito);
            TextoPAGO.setText("PAGOS");
        }


        if (haycheckSalida.equalsIgnoreCase("SI")) {
            textCheckSalida.setTextColor(ContextCompat.getColor(context, R.color.azulito));
            colorIconoSalida = ContextCompat.getColor(context, R.color.azulito);
        } else {
            textCheckSalida.setTextColor(ContextCompat.getColor(context, R.color.rojo));
            colorIconoSalida = ContextCompat.getColor(context, R.color.rojo);
        }


        if (haycheckTecnico.equalsIgnoreCase("SI")) {
            textCheckTecnico.setTextColor(ContextCompat.getColor(context, R.color.azulito));
            colorIconoTecnico = ContextCompat.getColor(context, R.color.azulito);
        } else {
            textCheckTecnico.setTextColor(ContextCompat.getColor(context, R.color.rojo));
            colorIconoTecnico = ContextCompat.getColor(context, R.color.rojo);
        }


        if (haychecklist.equalsIgnoreCase("SI")) {
            tvCheckIngreso.setTextColor(ContextCompat.getColor(context, R.color.azulito));
            colorIconoCheck = ContextCompat.getColor(context, R.color.azulito);
        } else {
            tvCheckIngreso.setTextColor(ContextCompat.getColor(context, R.color.rojo));
            colorIconoCheck = ContextCompat.getColor(context, R.color.rojo);
        }


        if (hayfoto.equalsIgnoreCase("SI")) {

            TextoSUBIRFOTOS.setTextColor(ContextCompat.getColor(context, R.color.azulito));
            colorIconoCamara = ContextCompat.getColor(context, R.color.azulito);

        } else {
            TextoSUBIRFOTOS.setTextColor(ContextCompat.getColor(context, R.color.rojo));
            colorIconoCamara = ContextCompat.getColor(context, R.color.rojo);
        }


        if (haymecanico.equalsIgnoreCase("SI")) {
            TextoASIGNARMECANICOS.setTextColor(ContextCompat.getColor(context, R.color.azulito));
            colorIconoMecanico = ContextCompat.getColor(context, R.color.azulito);
        } else {
            TextoASIGNARMECANICOS.setTextColor(ContextCompat.getColor(context, R.color.rojo));
            colorIconoMecanico = ContextCompat.getColor(context, R.color.rojo);
        }


        iconoCheckIngreso.setColorFilter(colorIconoCheck);
        iconoMecanico.setColorFilter(colorIconoMecanico);
        iconoCamara.setColorFilter(colorIconoCamara);
        iconoCheckTecnico.setColorFilter(colorIconoTecnico);
        iconoCheckSalida.setColorFilter(colorIconoSalida);
        iconoPago.setColorFilter(colorIconoCerdito);


        /*
        if (estatus.equalsIgnoreCase("ENTREGADO")) {
            LayoutCambiarStatus.setVisibility(View.GONE);
            LayoutTomarFotos.setVisibility(View.GONE);
            LayoutAsignarMecanico.setVisibility(View.GONE);

        } else {

            LayoutCambiarStatus.setVisibility(View.VISIBLE);
            LayoutTomarFotos.setVisibility(View.VISIBLE);
            LayoutAsignarMecanico.setVisibility(View.VISIBLE);
        }
*/

        LayoutReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View customView = LayoutInflater.from(context).inflate(R.layout.modal_nuevo_modal_pdfs, null);
                builder.setView(ModalRedondeado(context, customView));
                AlertDialog dialogConBotones = builder.create();
                dialogConBotones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogConBotones.show();


                LinearLayout LayoutPDFServicio = customView.findViewById(R.id.LayoutPDFServicio);
                //    LinearLayout pdfCheckEntrada = customView.findViewById(R.id.pdfCheckEntrada);
                LinearLayout LayoutPDFSalida = customView.findViewById(R.id.LayoutPDFSalida);
                LinearLayout LayoutPDFTecnico = customView.findViewById(R.id.LayoutPDFTecnico);
                LinearLayout PDFRefacciones = customView.findViewById(R.id.PDFRefacciones);
                LinearLayout LayoutPDFMecanicos = customView.findViewById(R.id.LayoutPDFMecanicos);

                PDFRefacciones.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "82");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", id_ser_venta);

                        new NuevoDownloadFileTask(context, postData).execute(url);

                    }
                });


                LayoutPDFMecanicos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Mostrar diálogo de carga

                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "81");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", id_ser_venta);

                        // Iniciar la tarea de descarga del archivo PDF
                        new NuevoDownloadFileTask(context, postData).execute(url);


/*
                        modalCargando = Utiles.ModalCargando(context, builder);


                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "81");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", id_ser_venta);

                        new DownloadFileTask(context, postData).execute(url);



                        modalCargando.dismiss();

 */
                    }
                });


                LayoutPDFServicio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "83");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", id_ser_venta);

                        new NuevoDownloadFileTask(context, postData).execute(url);
/*
                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "50");
                        postData.put("idventa", idventa);

                        new DownloadFileTask(context, postData).execute(url);
*/


                    }
                });

/*
                pdfCheckEntrada.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        modalCargando = Utiles.ModalCargando(context, builder);

                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "78");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", id_ser_venta);

                        new DownloadFileTask(context, postData).execute(url);
                        modalCargando.dismiss();

                    }
                });

 */

                LayoutPDFSalida.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "79");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", id_ser_venta);

                        new NuevoDownloadFileTask(context, postData).execute(url);

                    }
                });

                LayoutPDFTecnico.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map<String, String> postData = new HashMap<>();
                        postData.put("opcion", "80");
                        postData.put("idcliente", id_ser_cliente);
                        postData.put("idventa", id_ser_venta);

                        new NuevoDownloadFileTask(context, postData).execute(url);

                    }
                });
/*
                Map<String, String> postData = new HashMap<>();
                postData.put("opcion", "50");
                postData.put("idventa", idventa);

                new DownloadFileTask(context, postData).execute(url);


 */
            }
        });


        LayoutRefacciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View customView = LayoutInflater.from(context).inflate(R.layout.modal_refacciones_coches, null);
                builder.setView(ModalRedondeado(context, customView));
                AlertDialog dialogBuscarRefacciones = builder.create();
                dialogBuscarRefacciones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogBuscarRefacciones.show();

                FloatingActionButton botonAgregar = customView.findViewById(R.id.botonAgregar);

                if (estatus.equalsIgnoreCase("ENTREGADO")) {
                    botonAgregar.setVisibility(View.GONE);
                    //  Utiles.crearToastPersonalizado(context, "No puedes actualizar las fotos de una unidad entregada");
                } else {
                    botonAgregar.setVisibility(View.VISIBLE);
                }


                SinContenido = customView.findViewById(R.id.SinContenido);
                recyclerViewRefaccionesUnidades = customView.findViewById(R.id.recyclerViewRefaccionesUnidades);

                TextView textView32 = customView.findViewById(R.id.textView32);
                textView32.setText("REFACCIONES DE LA UNIDAD: " + id_ser_venta);


                ConsultarHerramientasDeUnidad(id_ser_venta);

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
                                            AgregarComentario(idrefaccion, id_ser_venta, comentarioingresado);
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
                                    textView4.setText("¿Seguro deseas eliminar la refaccion: " + clave + " de la unidad #" + id_ser_venta + " ?");

                                    Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                                    Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);


                                    buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialogConfirmacion.dismiss();
                                            dialogOpcionesDeRefaccion.dismiss();
                                            EliminarRefaccionDeUnidad(idrefaccion, id_ser_venta);


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
                                                    RegistrarRefaccion(id_ser_venta, claveIngresada, cantidadIngresada, precioIngresado,
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
                                                    RegistrarRefaccion(id_ser_venta, claveIngresada, cantidadIngresada, precioIngresado,
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
        });


        LayoutConsultarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //     if (iddoc.equalsIgnoreCase("null") || iddoc.isEmpty() || iddoc.equalsIgnoreCase("") || iddoc.equalsIgnoreCase(null)) {
                if (haypago.equalsIgnoreCase("NO")) {
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
                                    dialogOpcionesCoches.dismiss();
                                    actionListener.AsignarFolio(idDocSeleccionado, id_ser_venta);
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
        });


        LayoutAsignarMecanico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarMecanicos(id_ser_venta);

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


                if (estatus.equalsIgnoreCase("ENTREGADO")) {
                    botonAgregar.setVisibility(View.GONE);

                } else {
                    botonAgregar.setVisibility(View.VISIBLE);

                }

                recyclerViewMarcasUnidades = customView.findViewById(R.id.recyclerViewMarcasUnidades);
                adaptadorMecanicos = new AdaptadorNuevoMecanicos(listaMecanicos, context);
                LinearLayoutManager layoutManager2 = new LinearLayoutManager(context);
                recyclerViewMarcasUnidades.setLayoutManager(layoutManager2);

                if (estatus.equalsIgnoreCase("ENTREGADO")) {

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
                                        CorregirActividad(idbitacora, id_ser_venta, actividadCorregida);
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
                                    dialogOpcionesCoches.dismiss();
                                    actionListener.asignarActividadAServicio(id_ser_venta, ID_mecSelec, descripcionActividad);
                                    //     Utiles.crearToastPersonalizado(context, "Seleccionadste a: " + id_ser_venta +" "+ ID_mecSelec+ " "+descripcionActividad );

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


        LayoutCambiarStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


/*
                                dialogConfirmacion.dismiss();
                                dialogEstadoUnidad.dismiss();
                                dialogOpcionesCoches.dismiss();
                                */

                                    ValidarQueEstePagado(iddoc, id_ser_venta, dialogConfirmacion, dialogEstadoUnidad, dialogOpcionesCoches);

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

                            if (haychecklist.equalsIgnoreCase("SI")
                                    && haycheckSalida.equalsIgnoreCase("SI")
                                    && haycheckTecnico.equalsIgnoreCase("SI")
                                    && hayfoto.equalsIgnoreCase("SI")
                                    && haymecanico.equalsIgnoreCase("SI")) {

                   /*         if (iddoc.equalsIgnoreCase("")
                                    || iddoc.isEmpty()
                                    || iddoc.equalsIgnoreCase("null")
                                    || iddoc.equalsIgnoreCase(null)) {
*/
                                if (haypago.equalsIgnoreCase("NO")) {
                                    Utiles.crearToastPersonalizado(context, "Debes asignar una nota");

                                } else {
                                    actionListener.cambiarEstado(id_ser_venta, "LISTO PARA ENTREGA");
                                    dialogEstadoUnidad.dismiss();
                                    dialogOpcionesCoches.dismiss();
                                }
                            } else {

                                Utiles.crearToastPersonalizado(context, "Debes de llenar todos los campos requeridos para marcar como Listo para entrega");


                            }
                        }
                    });


                    LayoutLavadoY.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            actionListener.cambiarEstado(id_ser_venta, "LAVADO Y DETALLADO");
                            dialogEstadoUnidad.dismiss();
                            dialogOpcionesCoches.dismiss();
                        }
                    });

                    LayoutPruebaRuta.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            actionListener.cambiarEstado(id_ser_venta, "PRUEBA DE RUTA");
                            dialogEstadoUnidad.dismiss();
                            dialogOpcionesCoches.dismiss();
                        }
                    });


                    LayoutEnServicio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            actionListener.cambiarEstado(id_ser_venta, "EN SERVICIO");
                            dialogEstadoUnidad.dismiss();
                            dialogOpcionesCoches.dismiss();
                        }
                    });

                    LayoutEsperandoRef.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            actionListener.cambiarEstado(id_ser_venta, "EN ESPERA DE REFACCIONES");
                            dialogEstadoUnidad.dismiss();
                            dialogOpcionesCoches.dismiss();
                        }
                    });

                    LayoutDiagnostico.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            actionListener.cambiarEstado(id_ser_venta, "DIAGNOSTICO");
                            dialogEstadoUnidad.dismiss();
                            dialogOpcionesCoches.dismiss();
                        }
                    });

                }
            }
        });

/*
        LayoutTomarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, SubirFotosUnidadesActivity.class);
                intent.putExtra("marca", bundle.getString("marca"));
                intent.putExtra("id_ser_venta", bundle.getString("idventa"));
                context.startActivity(intent);

                dialogOpcionesCoches.dismiss();


            }
        });
*/
        LayoutTomarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (estatus.equalsIgnoreCase("ENTREGADO")) {

                    Utiles.crearToastPersonalizado(context, "No puedes actualizar las fotos de una unidad entregada");

                } else {

                    Intent intent = new Intent(context, SubirFotosUnidadesActivity.class);
                    intent.putExtra("marca", bundle.getString("marca"));
                    intent.putExtra("id_ser_venta", bundle.getString("idventa"));
                    context.startActivity(intent);

                    // Finalizar la actividad actual
                    if (context instanceof AppCompatActivity) {
                        ((AppCompatActivity) context).finish();
                    }

                    // También puedes usar la siguiente línea si estás seguro de que el contexto es una actividad:
                    // ((Activity) context).finish();

                    dialogOpcionesCoches.dismiss();
                }
            }
        });


        LayoutDetalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogOpcionesCoches.dismiss();

                DetalleFragment detalleFragment = new DetalleFragment();
                detalleFragment.setArguments(bundle);

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layoutCoches, detalleFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        LayoutCheckIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialogOpcionesCoches.dismiss();
                bundle.putString("tipo_check", "Entrada");
                ChecksFragment checksFragment = new ChecksFragment();
                checksFragment.setArguments(bundle);

                FragmentManager fragmentManagerCheck = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManagerCheck.beginTransaction()
                        .replace(R.id.frame_layoutCoches, checksFragment)
                        .addToBackStack(null)
                        .commit();

/*
                CheckListFragment checkListFragment = new CheckListFragment();
                checkListFragment.setArguments(bundle);

                FragmentManager fragmentManagerCheck = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManagerCheck.beginTransaction()
                        .replace(R.id.frame_layoutCoches, checkListFragment)
                        .addToBackStack(null)
                        .commit();
*/


            }
        });


        LayoutChecksSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (haychecklist.equalsIgnoreCase("SI") && haycheckTecnico.equalsIgnoreCase("SI")) {


                    dialogOpcionesCoches.dismiss();
                    bundle.putString("tipo_check", "Salida");
                    ChecksFragment checksFragment = new ChecksFragment();
                    checksFragment.setArguments(bundle);

                    FragmentManager fragmentManagerCheck = ((AppCompatActivity) context).getSupportFragmentManager();
                    fragmentManagerCheck.beginTransaction()
                            .replace(R.id.frame_layoutCoches, checksFragment)
                            .addToBackStack(null)
                            .commit();

                } else {

                    Utiles.crearToastPersonalizado(context, "Primero debes finalizar el check de ingreso y el check tecnico");
                }



                /*
                CheckListSalidaFragment checkListSalidaFragment = new CheckListSalidaFragment();
                checkListSalidaFragment.setArguments(bundle);

                FragmentManager fragmentManagerCheck = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManagerCheck.beginTransaction()
                        .replace(R.id.frame_layoutCoches, checkListSalidaFragment)
                        .addToBackStack(null)
                        .commit();

                 */

            }
        });

        LayoutCheckListTecnico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle.putString("tipo_check", "Tecnico");
                ChecksFragment checksFragment = new ChecksFragment();
                checksFragment.setArguments(bundle);

                FragmentManager fragmentManagerCheck = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManagerCheck.beginTransaction()
                        .replace(R.id.frame_layoutCoches, checksFragment)
                        .addToBackStack(null)
                        .commit();
                /*
                CheckListTecnicoFragment checkListTecnicoFragment = new CheckListTecnicoFragment();
                checkListTecnicoFragment.setArguments(bundle);

                FragmentManager fragmentManagerCheck = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManagerCheck.beginTransaction()
                        .replace(R.id.frame_layoutCoches, checkListTecnicoFragment)
                        .addToBackStack(null)
                        .commit();

                 */
                dialogOpcionesCoches.dismiss();

            }
        });


    }


    EditText editTextClave;
    EditText editCantidad;
    EditText editTipoDeUnidad;
    EditText editTextDescripcion;
    EditText editTextPrecio;
    EditText editTextDescuento;
    EditText editTextImporte;

    /*
        private Double calculateAndSetSum() {
            Double suma = 0.0;

            String cantidadStr = editCantidad.getText().toString();
            String precioStr = editTextPrecio.getText().toString();
            String descuentoStr = editTextDescuento.getText().toString();

            if (!cantidadStr.isEmpty() && !precioStr.isEmpty() && !descuentoStr.isEmpty()) {
                Double cantidades = Double.valueOf(cantidadStr);
                Double precioIngresado = Double.valueOf(precioStr);
                Double descIngresado = Double.valueOf(descuentoStr);

                suma = ((precioIngresado * cantidades) - descIngresado);
            }

            return suma;

        }
    */

/*
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

                // Si el campo de descuento está vacío, se considera como cero
                if (descuentoStr.isEmpty()) {
                    descIngresado = 0.0;
                } else {
                    // Si el campo de descuento no está vacío, se verifica si es un valor decimal válido
                    if (isValidDecimal(descuentoStr)) {
                        descIngresado = Double.parseDouble(descuentoStr);
                    } else {
                        // Si no es un valor decimal válido, se considera como cero
                        descIngresado = 0.0;
                    }
                }

                suma = ((precioIngresado * cantidades) - descIngresado);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // editTextImporte.setText(String.valueOf(suma));
        return suma;
    }
*/


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

                // Si el campo de descuento está vacío, se considera como cero
                if (descuentoStr.isEmpty()) {
                    descIngresado = 0.0;
                } else {
                    // Si el campo de descuento no está vacío, se verifica si es un valor decimal válido
                    if (isValidDecimal(descuentoStr)) {
                        descIngresado = Double.parseDouble(descuentoStr);
                    } else {
                        // Si no es un valor decimal válido, se considera como cero
                        descIngresado = 0.0;
                    }
                }

                suma = ((precioIngresado * cantidades) - descIngresado);

                // Formatear el resultado para que tenga solo dos decimales
                DecimalFormat df = new DecimalFormat("#.##");
                suma = Double.valueOf(df.format(suma));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // editTextImporte.setText(String.valueOf(suma));
        return suma;
    }

    private boolean isValidDecimal(String input) {
        // Verifica si la cadena contiene solo un punto decimal y no está al principio ni al final
        return input.matches("^\\d*\\.?\\d+$") && !input.startsWith(".") && !input.endsWith(".");
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textMarca, textModelo, textPlaca, textDueño, textFecha, textStatus;

        ImageView imageViewCoches, IVNoInternet, botonDesplegable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textMarca = itemView.findViewById(R.id.textMarca);
            textModelo = itemView.findViewById(R.id.textModelo);
            textPlaca = itemView.findViewById(R.id.textPlaca);
            textDueño = itemView.findViewById(R.id.textDueño);
            textFecha = itemView.findViewById(R.id.textFecha);
            textStatus = itemView.findViewById(R.id.textStatus);
            imageViewCoches = itemView.findViewById(R.id.imageViewCoches);
            IVNoInternet = itemView.findViewById(R.id.IVNoInternet);
            botonDesplegable = itemView.findViewById(R.id.botonDesplegable);
            botonDesplegable = itemView.findViewById(R.id.botonDesplegable);
        }
    }

    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(data);
        } else {
            String[] keywords = query.toLowerCase().split(" ");
            for (JSONObject item : data) {
                String marca = item.optString("marcaI", "").toLowerCase();
                String modelo = item.optString("modeloI", "").toLowerCase();
                String nombre = item.optString("nombre", "").toLowerCase();
                String status = item.optString("estatus", "").toLowerCase();
                String placa = item.optString("placasI", "").toLowerCase();
                String fecha = item.optString("fecha_ingreso", "").toLowerCase();
                String hora = item.optString("hora_ingreso", "").toLowerCase();

                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(marca.contains(keyword) || modelo.contains(keyword) || placa.contains(keyword) || fecha.contains(keyword) || hora.contains(keyword) ||
                            nombre.contains(keyword) || status.contains(keyword))) {
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
            actionListener.onFilterData(false); // Indica que no hay resultados
        } else {
            actionListener.onFilterData(true); // Indica que hay resultados
        }
        notifyDataSetChanged();
    }

    public void setFilteredData(List<JSONObject> filteredData) {
        this.filteredData = new ArrayList<>(filteredData);
        notifyDataSetChanged();
    }

    private void CargarMecanicos(String idventa) {
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
/*
                                    String foto = jsonObject.getString("foto");
                                    String nombre = jsonObject.getString("nombre");
                                    String idusuario = jsonObject.getString("idusuario");
                                    String observaciones = jsonObject.getString("observaciones");
                                    String fecha_programada = jsonObject.getString("fecha");
                                    Mecanicos mecanicos = new Mecanicos(foto, nombre, fecha_programada, idusuario, observaciones);
                                    listaMecanicos.add(mecanicos);*/
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
                params.put("opcion", "104");
                params.put("ID_ser_venta", idventa);
                return params;
            }
        };

        RequestQueue requestQueue2 = Volley.newRequestQueue(context);
        requestQueue2.add(stringRequest2);
    }


    private void RegistrarRefaccion(String idventa, String clave, String cantidad, String precio,
                                    String unidad, String descripcion, String importe, String descuento,
                                    String tipo, AlertDialog refaccionesDeUnidades, AlertDialog dialogBuscarRefacciones, AlertDialog dialogBotonRefaccion) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ConsultarHerramientasDeUnidad(idventa);
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
                params.put("opcion", "112");
                params.put("idventa", idventa);
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

/*
    private void asignarActividadAServicio(String ID_ser_venta, String idpersonal, String observaciones) {

     StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CargarMecanicos(ID_ser_venta);

                        Utiles.crearToastPersonalizado(context, "Se asigno el mecanico");
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
                params.put("opcion", "105");
                params.put("ID_ser_venta", ID_ser_venta);
                params.put("observaciones", observaciones);
                params.put("idpersonal", idpersonal);

                return params;
            }
        };

        RequestQueue requestQueue2 = Volley.newRequestQueue(context);
        requestQueue2.add(stringRequest2);
    }
*/

    private void setTextViewText(TextView textView, String text, String defaultText) {
        if (text.equals(null) || text.equals("") || text.equals("null") || text.isEmpty()) {
            textView.setText(defaultText);
        } else {
            textView.setText(text);
        }
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

    private void loadImage(ImageView imageView, String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(imageView.getContext())
                    .load(imageUrl)
                    .error(R.drawable.default_image)
                    .into(imageView);
        } else {
            Glide.with(imageView.getContext())
                    .load(R.drawable.default_image)
                    .into(imageView);
        }
    }

    List<JSONObject> listaFolios = new ArrayList<>();
    String urlPagos;
    AdaptadorFolios adaptadorFolios;

    LottieAnimationView lottieNoFolios;
    TextView textSinNotas;
    RecyclerView recyclerFolios;

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


    List<JSONObject> listaPagos = new ArrayList<>();
    AdaptadorPagos adaptadorPagos;

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


    TextView textSinPago;
    LottieAnimationView lottieNopagos;
    RecyclerView recyclerViewTiposUnidades;

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


    AdaptadorRefaccionesFerrum adaptadorRefaccionesferrum;
    List<JSONObject> listaRefacciones = new ArrayList<>();

    LottieAnimationView lottieSinInternetRef;
    RecyclerView recyclerRefacciones;

    TextView textSinRef;


    AdaptadorInyectores adaptadorInyectores;

    List<JSONObject> listaInyectores = new ArrayList<>();

    private void ConsultarInyectoresDeUnidad(String id_ser_venta) {
        modalCargando = Utiles.ModalCargando(context, builder);
        listaInyectores.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Respuesta de api de inyectores", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listaInyectores.add(jsonObject);
                    }
                    adaptadorInyectores.notifyDataSetChanged();
                    adaptadorInyectores.setFilteredData(listaInyectores);
                    adaptadorInyectores.filter("");
                    modalCargando.dismiss();

                } catch (JSONException e) {

                    Utiles.crearToastPersonalizado(context, "Algo fallo, revisa la conexión");
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
                params.put("opcion", "116");
                params.put("id_ser_venta", id_ser_venta);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
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


    List<JSONObject> listaRefaccionesDeUnidad = new ArrayList<>();
    AdaptadorRefaccionesDeUnidad adaptadorRefaccionesDeUnidad;


    RecyclerView recyclerViewRefaccionesUnidades;
    ConstraintLayout SinContenido;

    private void ConsultarHerramientasDeUnidad(String idventa) {
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
                params.put("opcion", "3");
                params.put("idventa", idventa);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void EliminarRefaccionDeUnidad(String idrefaccion, String idventa) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se eliminó la refacción");
                ConsultarHerramientasDeUnidad(idventa);
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


    private void AgregarComentario(String idrefaccion, String idventa, String observaciones) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se agrego la observación a la refaccion ");
                ConsultarHerramientasDeUnidad(idventa);
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


    private void CorregirActividad(String idbitacora, String idventa, String observaciones) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se corrigio la actividad ");
                CargarMecanicos(idventa);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "Algo fallo, revisa la conexión");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "115");
                params.put("idbitacora", idbitacora);
                params.put("observaciones", observaciones);

                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    public interface OnActivityActionListener {
        void onFilterData(Boolean resultados);

        void cambiarEstado(String ID_ser_venta, String nuevoEstado);

        void asignarActividadAServicio(String ID_ser_venta, String idpersonal, String observaciones);

        void AsignarFolio(String iddoc, String id_ser_venta);
    }

    private AdaptadorCoches.OnActivityActionListener actionListener;


    AlertDialog modalCargando;
    AlertDialog.Builder builder;
    String formattedDate;

    public AdaptadorCoches(List<JSONObject> data, Context context, AdaptadorCoches.OnActivityActionListener actionListener) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.actionListener = actionListener;

        this.url = context.getString(R.string.ApiBack);
        this.builder = new AlertDialog.Builder(context);
        this.builder.setCancelable(false);
        this.urlPagos = context.getString(R.string.urlPagos);


        Date currentDate = new Date();

        // Especificar el formato deseado
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Formatear la fecha
        formattedDate = dateFormat.format(currentDate);

    }

}
