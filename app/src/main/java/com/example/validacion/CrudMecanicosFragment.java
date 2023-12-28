package com.example.validacion;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorCheckActividades;
import com.example.validacion.Adaptadores.AdaptadorClientes;
import com.example.validacion.Adaptadores.AdaptadorMecanicos;
import com.example.validacion.Adaptadores.AdaptadorModelos;
import com.example.validacion.Adaptadores.AdaptadorProductividadMecanicos;
import com.example.validacion.Adaptadores.Utiles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrudMecanicosFragment extends Fragment implements AdaptadorProductividadMecanicos.OnActivityActionListener {


    RecyclerView recyclerViewMecanicos;

    // FloatingActionButton botonAgregarClientes;

    List<JSONObject> listadoActividades = new ArrayList<>();

    public CrudMecanicosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    Context context;
    String url;

    AdaptadorProductividadMecanicos adaptadorProductividadMecanicos;

    ConstraintLayout LayoutSinInternet;
    ConstraintLayout LayoutSinContenido;
    RelativeLayout LayoutConContenido;

    List<JSONObject> listaMecanicos = new ArrayList<>();


    AlertDialog modalCargando;
    AlertDialog.Builder builder;
    LottieAnimationView lottieNoClientes;
    TextView TextSinResultados;
    EditText searchEditText;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crud_mecanicos, container, false);
        recyclerViewMecanicos = view.findViewById(R.id.recyclerViewMecanicos);
        //      botonAgregarClientes=view.findViewById(R.id.botonAgregarClientes);
        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);

        LayoutSinInternet = view.findViewById(R.id.LayoutSinInternet);
        LayoutSinContenido = view.findViewById(R.id.LayoutSinContenido);
        LayoutConContenido = view.findViewById(R.id.LayoutConContenido);
        searchEditText = view.findViewById(R.id.searchEditText);
        lottieNoClientes = view.findViewById(R.id.lottieNoClientes);
        TextSinResultados = view.findViewById(R.id.TextSinResultados);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);


        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        MostrarMecanicos();


        adaptadorProductividadMecanicos = new AdaptadorProductividadMecanicos(listaMecanicos, context, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerViewMecanicos.setLayoutManager(gridLayoutManager);
        recyclerViewMecanicos.setAdapter(adaptadorProductividadMecanicos);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                MostrarMecanicos();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptadorProductividadMecanicos.filter(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        return view;
    }


    private void animacionLupe(String estado) {
        if (estado.equals("Oculto")) {
            lottieNoClientes.setVisibility(View.GONE);
            TextSinResultados.setVisibility(View.GONE);
        } else {
            lottieNoClientes.setVisibility(View.VISIBLE);
            TextSinResultados.setVisibility(View.VISIBLE);
        }
    }


    private void MostrarMecanicos() {
        listaMecanicos.clear();
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listaMecanicos.add(jsonObject);

                    }
                    adaptadorProductividadMecanicos.notifyDataSetChanged();
                    adaptadorProductividadMecanicos.setFilteredData(listaMecanicos);
                    adaptadorProductividadMecanicos.filter("");

                    if (listaMecanicos.size() > 0) {

                        mostrarLayout("conContenido");
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
                params.put("opcion", "60");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void mostrarLayout(String estado) {
        if (estado.equalsIgnoreCase("SinInternet")) {


            LayoutSinInternet.setVisibility(View.VISIBLE);
            LayoutConContenido.setVisibility(View.GONE);
            LayoutSinContenido.setVisibility(View.GONE);

        } else if (estado.equalsIgnoreCase("SinContenido")) {
            LayoutSinInternet.setVisibility(View.GONE);
            LayoutConContenido.setVisibility(View.GONE);
            LayoutSinContenido.setVisibility(View.VISIBLE);

        } else if (estado.equalsIgnoreCase("conContenido")) {
            LayoutSinInternet.setVisibility(View.GONE);
            LayoutConContenido.setVisibility(View.VISIBLE);
            LayoutSinContenido.setVisibility(View.GONE);
        }

        onLoadComplete();
    }


    private void onLoadComplete() {
        if (modalCargando.isShowing() && modalCargando != null) {
            modalCargando.dismiss();
        }
    }


    @Override
    public void onFilterData(Boolean resultados) {
        if (resultados) {
            animacionLupe("Oculto");
        } else {
            if ((searchEditText.getText().toString().equals("") || searchEditText.getText().toString().isEmpty())) {
                animacionLupe("Oculto");
            } else {
                animacionLupe("Visible");
            }
        }
    }


    AdaptadorCheckActividades adaptadorCheckActividades;

    Calendar calendar = Calendar.getInstance();

    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    String fechaActual = year + "-" + month + "-" + day;


    @Override
    public void onValidarCheckDiario(String ID_usuario, View view, String nombre) {
        modalCargando = Utiles.ModalCargando(context, builder);

        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equalsIgnoreCase("\"Esta libre\"")) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_check_actividades, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogConBotones = builder.create();
                    dialogConBotones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogConBotones.show();


                    RecyclerView reciclerViewActividades = customView.findViewById(R.id.reciclerViewActividades);
                    TextView textView20 = customView.findViewById(R.id.textView20);
                    Button button = customView.findViewById(R.id.button);
                    Button buttonCerrar = customView.findViewById(R.id.buttonCerrar);
                    textView20.setText("Revisión de actividades de " + nombre);


                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        JSONObject actividadJson = jsonArray.getJSONObject(0);

                        String idRegistroActividades = actividadJson.getString("ID_registro_actividades");
                        String estado = actividadJson.getString("estado");
                        String ID_registro_actividades = actividadJson.getString("ID_registro_actividades");


                        adaptadorCheckActividades = new AdaptadorCheckActividades(listadoActividades, context, idRegistroActividades, estado);

                        LinearLayoutManager linearLayout = new LinearLayoutManager(context);
                        reciclerViewActividades.setLayoutManager(linearLayout);
                        reciclerViewActividades.setAdapter(adaptadorCheckActividades);


                        CheckActividades(idRegistroActividades);

                        if (estado.equalsIgnoreCase("iniciado")) {
                            button.setEnabled(true);
                            button.setText("Guardar");
                        } else {
                            button.setText("Guardado");
                            button.setEnabled(false);

                        }


                        buttonCerrar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogConBotones.dismiss();
                            }
                        });


                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion, null);
                                builder.setView(ModalRedondeado(view.getContext(), customView));
                                AlertDialog dialogConfirmacion = builder.create();
                                dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialogConfirmacion.show();

                                TextView textView4 = customView.findViewById(R.id.textView4);


                                textView4.setText("¿Seguro deseas finalizar esta revision de actividades?");
                                Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
                                Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);


                                buttonCancelar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialogConfirmacion.dismiss();
                                    }
                                });

                                buttonAceptar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialogConBotones.dismiss();
                                        dialogConfirmacion.dismiss();
                                        onFinalizarRegistro(ID_registro_actividades);

                                    }
                                });


                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {


                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_confirmacion, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogConfirmacion = builder.create();
                    dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogConfirmacion.show();

                    TextView textView4 = customView.findViewById(R.id.textView4);
                    Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);
                    Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);

                    textView4.setText("¿Deseas iniciar un registro de actividades del dia de hoy para " + nombre + " ?");


                    buttonAceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onRegistrarRegistro(ID_usuario);
                            dialogConfirmacion.dismiss();

                        }
                    });

                    buttonCancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialogConfirmacion.dismiss();
                        }
                    });


                }

                modalCargando.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Error al cargar");
                modalCargando.dismiss();

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "75");
                params.put("fecha", fechaActual);
                params.put("ID_usuario", ID_usuario);


                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    public void onFinalizarRegistro(String ID_registro_actividades) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utiles.crearToastPersonalizado(context, "Se realizo la revisión correctamente");
                MostrarMecanicos();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Error al finalizar la revisión");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "77");
                params.put("ID_registro_actividades", ID_registro_actividades);


                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);

    }


    private void onRegistrarRegistro(String ID_usuario) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utiles.crearToastPersonalizado(context, "Se registro correctamente");
                MostrarMecanicos();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Error al cargar");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "74");
                params.put("fecha", fechaActual);
                params.put("ID_usuario", ID_usuario);


                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);

    }


    public void CheckActividades(String ID_registro_actividades) {
        listadoActividades.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listadoActividades.add(jsonObject);

                    }

                    adaptadorCheckActividades.setFilteredData(listadoActividades);
                    adaptadorCheckActividades.filter("");
                    adaptadorCheckActividades.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Error al cargar");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "73");
                params.put("registro_actividades", ID_registro_actividades);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }

}