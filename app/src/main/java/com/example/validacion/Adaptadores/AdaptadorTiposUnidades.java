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
import com.itextpdf.text.pdf.parser.Line;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;


public class AdaptadorTiposUnidades extends RecyclerView.Adapter<AdaptadorTiposUnidades.ViewHolder> {

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    Context context;

    String url;

    List<JSONObject> listaModelos = new ArrayList<>();

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


            holder.LayoutUnidad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.opciones_listado_actividades, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogOpcionesTipoUnidad = builder.create();
                    dialogOpcionesTipoUnidad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogOpcionesTipoUnidad.show();


                    LinearLayout LayoutVerChecks = customView.findViewById(R.id.LayoutVerChecks);

                    LinearLayout layoutEliminar = customView.findViewById(R.id.layoutEliminar);
                    LinearLayout layoutEditar = customView.findViewById(R.id.layoutEditar);
                    TextView textEdi = customView.findViewById(R.id.textEdi);
                    textEdi.setText("Editar nombre");

                    LinearLayout layoutActualizarFoto = customView.findViewById(R.id.layoutActualizarFoto);
                    layoutActualizarFoto.setVisibility(View.VISIBLE);


                    LayoutVerChecks.setVisibility(View.VISIBLE);

                    LayoutVerChecks.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_ver_checks_tipo_unidad, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogCheckUnidades = builder.create();
                            dialogCheckUnidades.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogCheckUnidades.show();

                            RadioButton radioBtnSiPlacas = customView.findViewById(R.id.radioBtnSiPlacas);
                            RadioButton radioBtnNoPlacas = customView.findViewById(R.id.radioBtnNoPlacas);
                            RadioButton radioBtnSiKm = customView.findViewById(R.id.radioBtnSiKm);
                            RadioButton radioBtnNoKm = customView.findViewById(R.id.radioBtnNoKm);
                            RadioButton radioBtnSiGas = customView.findViewById(R.id.radioBtnSiGas);
                            RadioButton radioBtnNoGas = customView.findViewById(R.id.radioBtnNoGas);
                            RadioButton radioBtnSiVin = customView.findViewById(R.id.radioBtnSiVin);
                            RadioButton radioBtnNoVin = customView.findViewById(R.id.radioBtnNoVin);
                            RadioButton radioBtnSiMotor = customView.findViewById(R.id.radioBtnSiMotor);
                            RadioButton radioBtnNoMotor = customView.findViewById(R.id.radioBtnNoMotor);


                            Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                            Button btnGuardar = customView.findViewById(R.id.btnGuardar);


                            btnGuardar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogOpcionesTipoUnidad.dismiss();
                                    dialogCheckUnidades.dismiss();
                                }
                            });

                            buttonCancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogCheckUnidades.dismiss();
                                }
                            });


                            if (gasolina.equalsIgnoreCase("1")) {
                                radioBtnSiGas.setChecked(true);
                                radioBtnNoGas.setChecked(false);
                            } else {
                                radioBtnSiGas.setChecked(false);
                                radioBtnNoGas.setChecked(true);
                            }


                            if (motor.equalsIgnoreCase("1")) {
                                radioBtnSiMotor.setChecked(true);
                                radioBtnNoMotor.setChecked(false);
                            } else {
                                radioBtnSiMotor.setChecked(false);
                                radioBtnNoMotor.setChecked(true);
                            }


                            if (vin.equalsIgnoreCase("1")) {
                                radioBtnSiVin.setChecked(true);
                                radioBtnNoVin.setChecked(false);
                            } else {

                                radioBtnSiVin.setChecked(false);
                                radioBtnNoVin.setChecked(true);
                            }


                            if (km.equalsIgnoreCase("1")) {
                                radioBtnSiKm.setChecked(true);
                                radioBtnNoKm.setChecked(false);
                            } else {
                                radioBtnSiKm.setChecked(false);
                                radioBtnNoKm.setChecked(true);
                            }


                            if (placas.equalsIgnoreCase("1")) {
                                radioBtnSiPlacas.setChecked(true);
                                radioBtnNoPlacas.setChecked(false);
                            } else {
                                radioBtnSiPlacas.setChecked(false);
                                radioBtnNoPlacas.setChecked(true);
                            }


                            radioBtnSiPlacas.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    radioBtnSiPlacas.setChecked(true);
                                    radioBtnNoPlacas.setChecked(false);

                                    actionListener.onActualizarCheck(ID_tipo_unidad, "placas", "1");
                                }
                            });

                            radioBtnNoPlacas.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    radioBtnSiPlacas.setChecked(false);
                                    radioBtnNoPlacas.setChecked(true);

                                    actionListener.onActualizarCheck(ID_tipo_unidad, "placas", "0");
                                }
                            });


                            radioBtnSiKm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    radioBtnNoKm.setChecked(false);
                                    radioBtnSiKm.setChecked(true);

                                    actionListener.onActualizarCheck(ID_tipo_unidad, "km", "1");
                                }
                            });


                            radioBtnNoKm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    radioBtnNoKm.setChecked(true);
                                    radioBtnSiKm.setChecked(false);

                                    actionListener.onActualizarCheck(ID_tipo_unidad, "km", "0");
                                }
                            });


                            radioBtnSiGas.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    radioBtnSiGas.setChecked(true);
                                    radioBtnNoGas.setChecked(false);

                                    actionListener.onActualizarCheck(ID_tipo_unidad, "gasolina", "1");
                                }
                            });
                            radioBtnNoGas.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    radioBtnSiGas.setChecked(false);
                                    radioBtnNoGas.setChecked(true);

                                    actionListener.onActualizarCheck(ID_tipo_unidad, "gasolina", "0");

                                }
                            });

                            radioBtnSiVin.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    radioBtnNoVin.setChecked(false);
                                    radioBtnSiVin.setChecked(true);
                                    actionListener.onActualizarCheck(ID_tipo_unidad, "vin", "1");

                                }
                            });


                            radioBtnNoVin.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    radioBtnNoVin.setChecked(true);
                                    radioBtnSiVin.setChecked(false);
                                    actionListener.onActualizarCheck(ID_tipo_unidad, "vin", "0");

                                }
                            });

                            radioBtnSiMotor.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    radioBtnSiMotor.setChecked(true);
                                    radioBtnNoMotor.setChecked(false);
                                    actionListener.onActualizarCheck(ID_tipo_unidad, "motor", "1");

                                }
                            });


                            radioBtnNoMotor.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    radioBtnSiMotor.setChecked(false);
                                    radioBtnNoMotor.setChecked(true);

                                    actionListener.onActualizarCheck(ID_tipo_unidad, "motor", "0");

                                }
                            });


                        }
                    });


                    layoutActualizarFoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            actionListener.onEditarTipoUnidad(ID_tipo_unidad, foto, nombre, dialogOpcionesTipoUnidad);
                        }
                    });


                    layoutEditar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            actionListener.onActualizarNombre(ID_tipo_unidad, nombre, dialogOpcionesTipoUnidad);

                        }
                    });

                    layoutEliminar.setOnClickListener(new View.OnClickListener() {
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

                            textView4.setText("Seguro deseas eliminar el tipo de unidad " + nombre + " ?");


                            buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogConfirmacion.dismiss();
                                    actionListener.onEliminarTipo(ID_tipo_unidad, dialogOpcionesTipoUnidad);
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
            });


        } finally {
        }
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
        void onEliminarTipo(String ID_tipo_unidad, AlertDialog dialogOpcionesTipoUnidad);

        void onEditarTipoUnidad(String ID_tipo_unidad, String foto, String nombre, AlertDialog dialogOpcionesTipoUnidad);


        void onActualizarNombre(String ID_tipo_unidad, String nombre, AlertDialog dialogOpcionesTipoUnidad);

        void onActualizarCheck(String ID_tipo_unidad, String tipo_check, String valor_check);

    }

    private AdaptadorTiposUnidades.OnActivityActionListener actionListener;


    public AdaptadorTiposUnidades(List<JSONObject> data, Context context, AdaptadorTiposUnidades.OnActivityActionListener actionListener) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        url = context.getResources().getString(R.string.ApiBack);
        this.actionListener = actionListener;
    }


}

