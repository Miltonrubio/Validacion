package com.example.validacion.Adaptadores;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Objetos.Cheks;
import com.example.validacion.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdaptadorChecks extends RecyclerView.Adapter<AdaptadorChecks.ViewHolder> {


    private List<Cheks> listaChecks;

    public int valoresVaciosChecks = 0;
    String url = "http://tallergeorgio.hopto.org:5611/georgioapp/georgioapi/Controllers/Apiback.php";


    public AdaptadorChecks(List<Cheks> listaChecks) {

        this.listaChecks = listaChecks;

        valoresVaciosChecks = calcularValoresVacios();
    }

    private int calcularValoresVacios() {
        int count = 0;
        for (Cheks checks : listaChecks) {
            String valorCheck = checks.getValorcheck();
            if (TextUtils.isEmpty(valorCheck) || valorCheck.equals("PENDIENTE")) {
                count++;
            }
        }
        return count;
    }

    @NonNull
    @Override
    public AdaptadorChecks.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checklist, parent, false);
        return new ViewHolder(view); // Crear instancia de ViewHolder, no de AdaptadorChecks
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorChecks.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cheks checks = listaChecks.get(position);
        holder.descripcionCheck.setText(checks.getDescripcion());
        String valorCheck = checks.getValorcheck();
        String IDCheck = checks.getIdcheck();
        String descripcion = checks.getDescripcion();


        holder.textPendiente.setVisibility(View.INVISIBLE);
        if ("R".equals(valorCheck)) {
            holder.regularRadioButton.setChecked(true);
            holder.naRadioButton.setChecked(false);
            holder.maloRadioButton.setChecked(false);
            holder.buenoRadioButton.setChecked(false);
            // valoresVaciosChecks--;
        } else if ("B".equals(valorCheck)) {
            holder.buenoRadioButton.setChecked(true);
            holder.regularRadioButton.setChecked(false);
            holder.naRadioButton.setChecked(false);
            holder.maloRadioButton.setChecked(false);
            //    valoresVaciosChecks--;
        } else if ("M".equals(valorCheck)) {
            holder.maloRadioButton.setChecked(true);
            holder.regularRadioButton.setChecked(false);
            holder.naRadioButton.setChecked(false);
            holder.buenoRadioButton.setChecked(false);
            //   valoresVaciosChecks--;
        } else if ("NA".equals(valorCheck)) {
            holder.naRadioButton.setChecked(true);
            holder.regularRadioButton.setChecked(false);
            holder.maloRadioButton.setChecked(false);
            holder.buenoRadioButton.setChecked(false);
            //  valoresVaciosChecks--;
        } else {

            holder.regularRadioButton.setChecked(false);
            holder.naRadioButton.setChecked(false);
            holder.maloRadioButton.setChecked(false);
            holder.buenoRadioButton.setChecked(false);
            holder.textPendiente.setVisibility(View.VISIBLE);

            //  valoresVaciosChecks++;

        }

        holder.regularRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.regularRadioButton.setChecked(true);
                holder.buenoRadioButton.setChecked(false);
                holder.maloRadioButton.setChecked(false);
                holder.naRadioButton.setChecked(false);

                holder.textPendiente.setVisibility(View.INVISIBLE);
                ActualizarChecks("R", IDCheck, holder.itemView.getContext(), descripcion, position);
            }
        });

        holder.buenoRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.regularRadioButton.setChecked(false);
                holder.buenoRadioButton.setChecked(true);
                holder.maloRadioButton.setChecked(false);
                holder.naRadioButton.setChecked(false);

                holder.textPendiente.setVisibility(View.INVISIBLE);
                ActualizarChecks("B", IDCheck, holder.itemView.getContext(), descripcion, position);
            }
        });

        holder.maloRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.regularRadioButton.setChecked(false);
                holder.buenoRadioButton.setChecked(false);
                holder.maloRadioButton.setChecked(true);
                holder.naRadioButton.setChecked(false);

                holder.textPendiente.setVisibility(View.INVISIBLE);
                ActualizarChecks("M", IDCheck, holder.itemView.getContext(), descripcion, position);
            }
        });

        holder.naRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.regularRadioButton.setChecked(false);
                holder.buenoRadioButton.setChecked(false);
                holder.maloRadioButton.setChecked(false);
                holder.naRadioButton.setChecked(true);

                holder.textPendiente.setVisibility(View.INVISIBLE);
                ActualizarChecks("NA", IDCheck, holder.itemView.getContext(), descripcion, position);
            }
        });


    }

    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return listaChecks.size();
    }


    private void ActualizarChecks(String valorCheck, String idcheck, Context context, String descripcion, int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            showToast(context, descripcion + " Revisado");
                            listaChecks.get(position).setValorcheck(valorCheck);
                            notifyItemChanged(position);
                            int valoresVacios = calcularValoresVacios();
                            if (adaptadorListener != null) {
                                adaptadorListener.onCheckUpdated(position, valorCheck, valoresVacios, listaChecks.size());
                            }

                        } else {
                            Toast.makeText(context, "La respuesta está vacía", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Error", "Error en la solicitud: " + error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "12");
                params.put("idcheck", idcheck);
                params.put("valorcheck", valorCheck);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView descripcionCheck, textPendiente;
        RadioButton regularRadioButton, buenoRadioButton, maloRadioButton, naRadioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descripcionCheck = itemView.findViewById(R.id.descripcionCheck);
            regularRadioButton = itemView.findViewById(R.id.regularRadioButton);
            buenoRadioButton = itemView.findViewById(R.id.buenoRadioButton);
            maloRadioButton = itemView.findViewById(R.id.maloRadioButton);
            naRadioButton = itemView.findViewById(R.id.naRadioButton);

            textPendiente = itemView.findViewById(R.id.textPendiente);
        }
    }

    private AdaptadorListener adaptadorListener;

    public void setAdaptadorListener(AdaptadorListener listener) {
        this.adaptadorListener = listener;
    }

    public interface AdaptadorListener {
        void onCheckUpdated(int position, String valorCheck, int valoresVaciosChecks, int TotalValores);
    }

}