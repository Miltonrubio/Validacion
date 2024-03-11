package com.example.validacion.Adaptadores;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.R;

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

public class AdaptadorServiciosInyectores extends RecyclerView.Adapter<AdaptadorServiciosInyectores.ViewHolder> {

    private Context context;
    private List<JSONObject> filteredData;
    private List<JSONObject> data;


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
            String nombre = jsonObject2.optString("nombre", "");
            String motivo_ingreso = jsonObject2.optString("motivo_ingreso", "");


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
            holder.nombrecliente.setText(nombre);

            holder.motivoIngreso.setText(motivo_ingreso);

            holder.contenedorServicioInyector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View customView = LayoutInflater.from(context).inflate(R.layout.modal_inyectores, null);
                    builder.setView(ModalRedondeado(context, customView));
                    AlertDialog dialogInyectoresPorServicio = builder.create();
                    dialogInyectoresPorServicio.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogInyectoresPorServicio.show();

                    RecyclerView recyclerViewInyectores = customView.findViewById(R.id.recyclerViewInyectores);

                    ConsultarInyectoresDeUnidad(ID_serv_inyector);
                    adaptadorInyectores = new AdaptadorInyectores(listaInyectores, context, dialogInyectoresPorServicio,actionListenerInyectores );
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                    recyclerViewInyectores.setLayoutManager(gridLayoutManager);
                    recyclerViewInyectores.setAdapter(adaptadorInyectores);


                }
            });


        } finally {
        }

    }


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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombrecliente = itemView.findViewById(R.id.nombrecliente);
            MarcaModelo = itemView.findViewById(R.id.MarcaModelo);
            nombrecliente = itemView.findViewById(R.id.nombrecliente);
            fechaServicio = itemView.findViewById(R.id.fechaServicio);
            contenedorServicioInyector = itemView.findViewById(R.id.contenedorServicioInyector);
            motivoIngreso = itemView.findViewById(R.id.motivoIngreso);
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


    AlertDialog.Builder builder;
    AlertDialog modalCargando;


    AdaptadorInyectores.OnActivityActionListener actionListenerInyectores;

    public AdaptadorServiciosInyectores(List<JSONObject> data, Context context, AdaptadorInyectores.OnActivityActionListener actionListenerInyectores ) {

        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        url = context.getResources().getString(R.string.ApiBack);

        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        this.actionListenerInyectores=actionListenerInyectores;
    }

}
