package com.example.validacion;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorBuscarHerramientas;
import com.example.validacion.Adaptadores.AdaptadorInventarioPorGaveta;
import com.example.validacion.Adaptadores.Utiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsultaDeInventariosFragment extends Fragment implements AdaptadorInventarioPorGaveta.OnActivityActionListener {
    Context context;
    String url;

    AdaptadorInventarioPorGaveta adaptadorInventarioPorGaveta;
    String id_gabeta;

    NestedScrollView ContenedorContenido;
    ConstraintLayout ContenedorSinContenido;

    AlertDialog modalCargando;
    AlertDialog.Builder builder;


    public ConsultaDeInventariosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consulta_de_inventarios, container, false);

        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);
        builder= new AlertDialog.Builder(context);
        builder.setCancelable(false);


        ContenedorSinContenido=view.findViewById(R.id.ContenedorSinContenido);
        ContenedorContenido=view.findViewById(R.id.ContenedorContenido);


        TextView tvTitulo = view.findViewById(R.id.tvTitulo);
        RecyclerView recyclerViewInventariosPorGaveta = view.findViewById(R.id.recyclerViewInventariosPorGaveta);

        Bundle bundle = getArguments();
        if (bundle != null) {
            id_gabeta = bundle.getString("id_gabeta", "");
            String nombre = bundle.getString("nombre", "");
            tvTitulo.setText("INVENTARIOS REALIZADOS PARA LA GAVETA  " + nombre.toUpperCase());

            MostrarInventariosPorGaveta(id_gabeta);

            recyclerViewInventariosPorGaveta.setLayoutManager(new LinearLayoutManager(context));
            adaptadorInventarioPorGaveta = new AdaptadorInventarioPorGaveta(listaInventarios, context, this);
            recyclerViewInventariosPorGaveta.setAdapter(adaptadorInventarioPorGaveta);


        }

        return view;
    }


    List<JSONObject> listaInventarios = new ArrayList<>();

    private void MostrarInventariosPorGaveta(String idgabeta) {
        listaInventarios.clear();
        modalCargando =   Utiles.ModalCargando(context, builder);
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listaInventarios.add(jsonObject);

                    }
                    adaptadorInventarioPorGaveta.notifyDataSetChanged();
                    adaptadorInventarioPorGaveta.setFilteredData(listaInventarios);
                    adaptadorInventarioPorGaveta.filter("");


                    if (listaInventarios.size() > 0) {

                        mostrarLayout("ConContenido");
                    } else {

                        mostrarLayout("SinInternet");
                    }



                } catch (JSONException e) {
                    mostrarLayout("SinInternet");
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
                params.put("opcion", "57");
                params.put("idgabeta", idgabeta);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private  void mostrarLayout(String estado){

        if (estado.equalsIgnoreCase("ConContenido")){

            ContenedorContenido.setVisibility(View.VISIBLE);
            ContenedorSinContenido.setVisibility(View.GONE);

        }else {

            ContenedorContenido.setVisibility(View.GONE);
            ContenedorSinContenido.setVisibility(View.VISIBLE);
        }
        onLoadComplete();

    }


    private void onLoadComplete() {
        if (modalCargando.isShowing() && modalCargando != null) {
            modalCargando.dismiss();
        }
    }
    @Override
    public void onFinalizarInventario(String iddocga) {

        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se actualizó el inventario");
                MostrarInventariosPorGaveta(id_gabeta);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "No se pudo actualizar el inventario , revisa la conexion por favor");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "59");
                params.put("iddocga", iddocga);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


/*

    private void mostrarLayoutHerramientas(String estado) {
        if (estado.equalsIgnoreCase("conContenido")) {

            lottieNoHerramientas.setVisibility(View.GONE);
            RecyclerViewHerramientas.setVisibility(View.VISIBLE);
        } else {
            lottieNoHerramientas.setVisibility(View.VISIBLE);
            RecyclerViewHerramientas.setVisibility(View.GONE);

        }
    }
 */

}