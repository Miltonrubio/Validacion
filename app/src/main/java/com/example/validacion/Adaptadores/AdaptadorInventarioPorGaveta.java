package com.example.validacion.Adaptadores;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.validacion.ConsultaDeInventariosFragment;
import com.example.validacion.DetallesArrastres;
import com.example.validacion.R;
import com.itextpdf.text.pdf.parser.Line;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdaptadorInventarioPorGaveta extends RecyclerView.Adapter<AdaptadorInventarioPorGaveta.ViewHolder> {


    private Context context;

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    String url;


    public AdaptadorInventarioPorGaveta(List<JSONObject> data, Context context) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventarios_gavetas, parent, false);
        return new ViewHolder(view);

    }


    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        List<JSONObject> listaDeCajones = new ArrayList<>();
        url = context.getResources().getString(R.string.ApiBack);

        /*
        SharedPreferences sharedPreferences = context.getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String permisosUsuario = sharedPreferences.getString("permisos", "");
        String nombresesioniniciada = sharedPreferences.getString("nombre", "");
        String IDSesionIniciada = sharedPreferences.getString("idusuario", "");

        */

        try {
            JSONObject jsonObject = filteredData.get(position);
            String iddocga = jsonObject.optString("iddocga", "");
            String NombreMecanico = jsonObject.optString("NombreMecanico", "");
            String fecha = jsonObject.optString("fecha", "");
            String listaHerramientas= jsonObject.optString("listaHerramientas","");

            holder.tvEncargado.setText("A cargo de "+ NombreMecanico);
            holder.tvFechaInventario.setText(fecha);







        } finally {

        }
    }


    @Override
    public int getItemCount() {

        //return filteredData.size();
        return filteredData.size();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEncargado;
        TextView tvFechaInventario;
        RecyclerView reciclerViewInventarios;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFechaInventario = itemView.findViewById(R.id.tvFechaInventario);
            reciclerViewInventarios = itemView.findViewById(R.id.reciclerViewInventarios);
            tvEncargado = itemView.findViewById(R.id.tvEncargado);
        }
    }

    public void filter(String query) {
        filteredData.clear();

        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(data);
        } else {
            String[] keywords = query.toLowerCase().split(" ");

            for (JSONObject item : data) {
                String nombre_gaveta = item.optString("nombre_gaveta", "").toLowerCase();
                String nombre_mecanico = item.optString("nombre_mecanico", "").toLowerCase();
                String id_gabeta = item.optString("id_gabeta", "").toLowerCase();


                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(nombre_gaveta.contains(keyword) || nombre_mecanico.contains(keyword) || id_gabeta.contains(keyword))) {
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
