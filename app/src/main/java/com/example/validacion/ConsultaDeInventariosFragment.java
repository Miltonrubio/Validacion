package com.example.validacion;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorBuscarHerramientas;
import com.example.validacion.Adaptadores.AdaptadorHerramientasDelInventario;
import com.example.validacion.Adaptadores.AdaptadorInventarioPorGaveta;
import com.example.validacion.Adaptadores.Utiles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsultaDeInventariosFragment extends Fragment implements AdaptadorInventarioPorGaveta.OnActivityActionListener, AdaptadorHerramientasDelInventario.OnActivityActionListener {
    Context context;
    String url;

    AdaptadorInventarioPorGaveta adaptadorInventarioPorGaveta;
    String id_gabeta;

    NestedScrollView ContenedorContenido;

    AdaptadorHerramientasDelInventario.OnActivityActionListener actionListenerChecks;

    ConstraintLayout ContenedorSinContenido;

    AlertDialog modalCargando;
    AlertDialog.Builder builder;

    String idusuario;


    public ConsultaDeInventariosFragment() {
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
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        actionListenerChecks= this;
        ContenedorSinContenido = view.findViewById(R.id.ContenedorSinContenido);
        ContenedorContenido = view.findViewById(R.id.ContenedorContenido);


        TextView tvTitulo = view.findViewById(R.id.tvTitulo);
        RecyclerView recyclerViewInventariosPorGaveta = view.findViewById(R.id.recyclerViewInventariosPorGaveta);

        FloatingActionButton botonLevantarInventario = view.findViewById(R.id.botonLevantarInventario);



        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);

        String IDSesionIniciada = sharedPreferences.getString("idusuario", "");


        Bundle bundle = getArguments();
        if (bundle != null) {
            id_gabeta = bundle.getString("id_gabeta", "");
            String nombre = bundle.getString("nombre", "");
             idusuario = bundle.getString("idusuario", "");
            tvTitulo.setText("INVENTARIOS REALIZADOS PARA LA GAVETA  " + nombre.toUpperCase());

            MostrarInventariosPorGaveta(id_gabeta);

            recyclerViewInventariosPorGaveta.setLayoutManager(new LinearLayoutManager(context));
            adaptadorInventarioPorGaveta = new AdaptadorInventarioPorGaveta(listaInventarios, context, this, actionListenerChecks);
            recyclerViewInventariosPorGaveta.setAdapter(adaptadorInventarioPorGaveta);
        }


        botonLevantarInventario.setOnClickListener(new View.OnClickListener() {
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

                textView4.setText("¿Deseas levantar el inventario?");


                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogConfirmacion.dismiss();

                    }
                });


                buttonAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogConfirmacion.dismiss();
                       // dialogOpcionesGaveta.dismiss();

                       onLevantarInventario(id_gabeta, idusuario, IDSesionIniciada);

                    }
                });

            }
        });


        return view;
    }



    public void onLevantarInventario(String idGabeta, String idusuario, String idSesionIniciada){
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                MostrarInventariosPorGaveta(idGabeta);
                Utiles.crearToastPersonalizado(context, "El inventario se levantó correctamente");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "No se pudó levantar el inventario, revisa la conexión");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "56");
                params.put("idgabeta", idGabeta);
                params.put("idencargado", idusuario);
                params.put("idmecanico", idSesionIniciada);

                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }




    List<JSONObject> listaInventarios = new ArrayList<>();

    private void MostrarInventariosPorGaveta(String idgabeta) {
        listaInventarios.clear();
        modalCargando = Utiles.ModalCargando(context, builder);
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


    private void mostrarLayout(String estado) {

        if (estado.equalsIgnoreCase("ConContenido")) {

            ContenedorContenido.setVisibility(View.VISIBLE);
            ContenedorSinContenido.setVisibility(View.GONE);

        } else {

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
                params.put("opcion", "151");
                params.put("iddocga", iddocga);
                params.put("id_gabeta", id_gabeta);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }

    @Override
    public void ActualizarPiezas(String cantidadHerr, String idinv, String nombreherramienta) {

        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //   Utiles.crearToastPersonalizado(context, "Se actualizó la cantidad de " + nombreherramienta);
                //MostrarInventariosPorGaveta(id_gabeta);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "No se pudo actualizar, revisa la conexion por favor");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "69");
                params.put("idinv", idinv);
                params.put("cantidadHerr", cantidadHerr);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }

    @Override
    public void ActualizarCheck(String estadoHerramienta, String idinv, String nombreherramienta) {

        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se actualizó el estado de " + nombreherramienta);
                MostrarInventariosPorGaveta(id_gabeta);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "No se pudo actualizar, revisa la conexion por favor");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "58");
                params.put("idinv", idinv);
                params.put("estadoHerramienta", estadoHerramienta);
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