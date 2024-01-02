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

public class AdaptadorListadoActividades extends RecyclerView.Adapter<AdaptadorListadoActividades.ViewHolder> {


    private Context context;
    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    String url;


    public interface OnActivityActionListener {
        void onEliminarListado(String ID_listado_actividad);

        void onEditarListado(String ID_listado_actividad, String nombre_actividad);

        void onReactivarListado(String ID_listado_actividad);

    }

    private AdaptadorListadoActividades.OnActivityActionListener actionListener;

    public AdaptadorListadoActividades(List<JSONObject> data, Context context, AdaptadorListadoActividades.OnActivityActionListener actionListener) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        url = context.getResources().getString(R.string.ApiBack);

        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listado_actividades, parent, false);
        return new ViewHolder(view);

    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String ID_listado_actividad = jsonObject2.optString("ID_listado_actividad", "");
            String nombre_actividad = jsonObject2.optString("nombre_actividad", "");
            String estatus = jsonObject2.optString("estatus", "");


            if (estatus.equalsIgnoreCase("alta")) {

                holder.nombreActividad.setBackgroundResource(R.drawable.redondeado_verde);
                int colorIcono = ContextCompat.getColor(context, R.color.white);
                holder.nombreActividad.setTextColor(colorIcono);
            } else {

                holder.nombreActividad.setBackgroundResource(R.drawable.redondeado_gris);
                int colorIcono = ContextCompat.getColor(context, R.color.white);
                holder.nombreActividad.setTextColor(colorIcono);

            }


            Bundle bundle = new Bundle();
            bundle.putString("ID_listado_actividad", ID_listado_actividad);
            bundle.putString("nombre_actividad", nombre_actividad);

            holder.nombreActividad.setText(nombre_actividad);

            holder.nombreActividad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.opciones_listado_actividades, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogOpcionesActividades = builder.create();
                    dialogOpcionesActividades.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogOpcionesActividades.show();


                    LinearLayout LayoutReactivar = customView.findViewById(R.id.LayoutReactivar);
                    TextView textEliminar = customView.findViewById(R.id.textEliminar);
                    textEliminar.setText("Desactivar");

                    LinearLayout layoutEditar = customView.findViewById(R.id.layoutEditar);
                    LinearLayout layoutEliminar = customView.findViewById(R.id.layoutEliminar);

                    if (estatus.equalsIgnoreCase("alta")) {

                        layoutEliminar.setVisibility(View.VISIBLE);
                        LayoutReactivar.setVisibility(View.GONE);
                    } else {

                        layoutEliminar.setVisibility(View.GONE);
                        LayoutReactivar.setVisibility(View.VISIBLE);
                    }


                    LayoutReactivar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialogOpcionesActividades.dismiss();
                            actionListener.onReactivarListado(ID_listado_actividad);
                        }
                    });


                    layoutEditar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_agregar_listado_actividades, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogConfirmacipn = builder.create();
                            dialogConfirmacipn.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogConfirmacipn.show();

                            TextView textView23 = customView.findViewById(R.id.textView23);
                            Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                            Button buttonGuardar = customView.findViewById(R.id.buttonGuardar);
                            EditText editText = customView.findViewById(R.id.editText);
                            editText.setText(nombre_actividad);
                            textView23.setText("Inserta el nuevo nombre");


                            buttonGuardar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String nuevoNombreActividad = editText.getText().toString();

                                    dialogConfirmacipn.dismiss();
                                    dialogOpcionesActividades.dismiss();

                                    actionListener.onEditarListado(ID_listado_actividad, nuevoNombreActividad);

                                }
                            });


                            buttonCancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogConfirmacipn.dismiss();
                                }
                            });

                        }
                    });


                    layoutEliminar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                            AlertDialog dialogConfirmacipn = builder.create();
                            dialogConfirmacipn.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogConfirmacipn.show();


                            TextView textView4 = customView.findViewById(R.id.textView4);
                            Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);
                            Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);


                            textView4.setText("Seguro deseas desactivar la actividad " + nombre_actividad);


                            buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    dialogConfirmacipn.dismiss();
                                    dialogOpcionesActividades.dismiss();
                                    actionListener.onEliminarListado(ID_listado_actividad);

                                }
                            });


                            buttonCancelar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogConfirmacipn.dismiss();
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

        //return filteredData.size();
        return filteredData.size();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombreActividad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreActividad = itemView.findViewById(R.id.nombreActividad);
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


                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(nombre.contains(keyword) || empresa.contains(keyword))) {
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

}
