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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

public class CrudMecanicosFragment extends Fragment implements AdaptadorProductividadMecanicos.OnActivityActionListener, AdaptadorCheckActividades.OnActivityActionListener {


    RecyclerView recyclerViewMecanicos;

    // FloatingActionButton botonAgregarClientes;

    List<JSONObject> listadoActividades = new ArrayList<>();

    AdaptadorCheckActividades.OnActivityActionListener actionListenerChecks;

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


    ImageView imageViewEncargado;
    TextView NombreEncargado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crud_mecanicos, container, false);
        recyclerViewMecanicos = view.findViewById(R.id.recyclerViewMecanicos);
        //      botonAgregarClientes=view.findViewById(R.id.botonAgregarClientes);
        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);
        actionListenerChecks = this;
        LayoutSinInternet = view.findViewById(R.id.LayoutSinInternet);
        LayoutSinContenido = view.findViewById(R.id.LayoutSinContenido);
        LayoutConContenido = view.findViewById(R.id.LayoutConContenido);
        searchEditText = view.findViewById(R.id.searchEditText);
        lottieNoClientes = view.findViewById(R.id.lottieNoClientes);
        TextSinResultados = view.findViewById(R.id.TextSinResultados);

        NombreEncargado = view.findViewById(R.id.NombreEncargado);
        imageViewEncargado = view.findViewById(R.id.imageViewEncargado);


        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);


        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        ConsultarEncargado();
        MostrarMecanicos();

        adaptadorProductividadMecanicos = new AdaptadorProductividadMecanicos(listaMecanicos, context, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerViewMecanicos.setLayoutManager(gridLayoutManager);
        recyclerViewMecanicos.setAdapter(adaptadorProductividadMecanicos);


        FloatingActionButton botonAgregarUsuarios = view.findViewById(R.id.botonAgregarUsuarios);

        botonAgregarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View customView = LayoutInflater.from(context).inflate(R.layout.modal_agregar_usuarios, null);
                builder.setView(Utiles.ModalRedondeado(context, customView));
                AlertDialog dialogNuevoEmpleado = builder.create();
                dialogNuevoEmpleado.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogNuevoEmpleado.show();


                Button botonCancelar = customView.findViewById(R.id.botonCancelar);
                Button botonAceptar = customView.findViewById(R.id.botonAceptar);

                EditText EditTextNombre = customView.findViewById(R.id.EditTextNombre);
                EditText EditTextCorreo = customView.findViewById(R.id.EditTextCorreo);
                EditText EditTextTelefono = customView.findViewById(R.id.EditTextTelefono);
                EditText EditTextClave = customView.findViewById(R.id.EditTextClave);

                TextView textViewSeleccionaArea = customView.findViewById(R.id.textViewSeleccionaArea);
                TextView textViewTipoEmpresa = customView.findViewById(R.id.textViewTipoEmpresa);
                TextView textView37 = customView.findViewById(R.id.textView37);


                textViewSeleccionaArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View customView = LayoutInflater.from(context).inflate(R.layout.modal_seleccionar_area, null);
                        builder.setView(Utiles.ModalRedondeado(context, customView));
                        AlertDialog dialogArea = builder.create();
                        dialogArea.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogArea.show();

                        LinearLayout LayoutALMACEN = customView.findViewById(R.id.LayoutALMACEN);
                        LinearLayout LayoutAdministrativo = customView.findViewById(R.id.LayoutAdministrativo);
                        LinearLayout LayoutCajas = customView.findViewById(R.id.LayoutCajas);
                        LinearLayout LayoutChofer = customView.findViewById(R.id.LayoutChofer);
                        LinearLayout LayoutGerencia = customView.findViewById(R.id.LayoutGerencia);
                        LinearLayout LayoutHojalateria = customView.findViewById(R.id.LayoutHojalateria);
                        LinearLayout LayoutJefeDeTaller = customView.findViewById(R.id.LayoutJefeDeTaller);
                        LinearLayout LayoutMecanico = customView.findViewById(R.id.LayoutMecanico);
                        LinearLayout LayoutRecepcion = customView.findViewById(R.id.LayoutRecepcion);
                        LinearLayout LayoutRepartidor = customView.findViewById(R.id.LayoutRepartidor);
                        LinearLayout LayoutVendedor = customView.findViewById(R.id.LayoutVendedor);
                        LinearLayout LayoutOtros = customView.findViewById(R.id.LayoutOtros);


                        LayoutOtros.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                textViewSeleccionaArea.setText("OTROS");
                                dialogArea.dismiss();
                            }
                        });

                        LayoutVendedor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                textViewSeleccionaArea.setText("VENDEDOR");
                                dialogArea.dismiss();
                            }
                        });
                        LayoutRepartidor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                textViewSeleccionaArea.setText("REPARTIDOR");
                                dialogArea.dismiss();
                            }
                        });


                        LayoutRecepcion.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                textViewSeleccionaArea.setText("RECEPCION");
                                dialogArea.dismiss();
                            }
                        });

                        LayoutMecanico.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                textViewSeleccionaArea.setText("MECANICO");
                                dialogArea.dismiss();
                            }
                        });

                        LayoutJefeDeTaller.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                textViewSeleccionaArea.setText("JEFE DE TALLER");
                                dialogArea.dismiss();
                            }
                        });
                        LayoutHojalateria.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                textViewSeleccionaArea.setText("HOJALATERIA");
                                dialogArea.dismiss();
                            }
                        });

                        LayoutGerencia.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                textViewSeleccionaArea.setText("GERENCIA");
                                dialogArea.dismiss();
                            }
                        });


                        LayoutChofer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                textViewSeleccionaArea.setText("CHOFER");
                                dialogArea.dismiss();
                            }
                        });

                        LayoutCajas.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                textViewSeleccionaArea.setText("CAJAS");
                                dialogArea.dismiss();
                            }
                        });
                        LayoutAdministrativo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                textViewSeleccionaArea.setText("ADMINISTRATIVO");
                                dialogArea.dismiss();
                            }
                        });


                        LayoutALMACEN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                textViewSeleccionaArea.setText("ALMACEN");
                                dialogArea.dismiss();
                            }
                        });


                    }
                });


                textView37.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View customView = LayoutInflater.from(context).inflate(R.layout.modal_seleccoion_tipo_usuario, null);
                        builder.setView(Utiles.ModalRedondeado(context, customView));
                        AlertDialog dialogTipoUsuario = builder.create();
                        dialogTipoUsuario.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogTipoUsuario.show();

                        LinearLayout LayoutSuperAdmin = customView.findViewById(R.id.LayoutSuperAdmin);
                        LinearLayout LayoutChofer = customView.findViewById(R.id.LayoutChofer);
                        LinearLayout LayoutMecanico = customView.findViewById(R.id.LayoutMecanico);
                        LinearLayout LayoutCortes = customView.findViewById(R.id.LayoutCortes);
                        LinearLayout LayoutRecepcion = customView.findViewById(R.id.LayoutRecepcion);
                        LinearLayout LayoutJefeDeTaller = customView.findViewById(R.id.LayoutJefeDeTaller);
                        LinearLayout LayoutAlmacenista = customView.findViewById(R.id.LayoutAlmacenista);
                        LinearLayout LayoutPreventa = customView.findViewById(R.id.LayoutPreventa);
                        LinearLayout LayoutRepartidor = customView.findViewById(R.id.LayoutRepartidor);

                        LayoutRepartidor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogTipoUsuario.dismiss();
                                textView37.setText("REPARTIDOR");
                            }
                        });

                        LayoutPreventa.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogTipoUsuario.dismiss();
                                textView37.setText("PREVENTA");
                            }
                        });


                        LayoutAlmacenista.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogTipoUsuario.dismiss();
                                textView37.setText("ALMACENISTA");
                            }
                        });
                        LayoutJefeDeTaller.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogTipoUsuario.dismiss();
                                textView37.setText("JEFE DE TALLER");
                            }
                        });
                        LayoutRecepcion.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogTipoUsuario.dismiss();
                                textView37.setText("RECEPCION");
                            }
                        });
                        LayoutCortes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogTipoUsuario.dismiss();
                                textView37.setText("CORTES");

                            }
                        });
                        LayoutMecanico.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogTipoUsuario.dismiss();
                                textView37.setText("MECANICO");
                            }
                        });

                        LayoutChofer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogTipoUsuario.dismiss();
                                textView37.setText("CHOFER");
                            }
                        });

                        LayoutSuperAdmin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogTipoUsuario.dismiss();
                                textView37.setText("SUPERADMIN");
                            }
                        });


                    }
                });

                textViewTipoEmpresa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View customView = LayoutInflater.from(context).inflate(R.layout.modal_seleccionar_empresa, null);
                        builder.setView(Utiles.ModalRedondeado(context, customView));
                        AlertDialog dialogEmpresa = builder.create();
                        dialogEmpresa.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogEmpresa.show();

                        LinearLayout LayoutAbarroteraHidalgo = customView.findViewById(R.id.LayoutAbarroteraHidalgo);
                        LinearLayout LayoutBitala = customView.findViewById(R.id.LayoutBitala);
                        LinearLayout LayoutAlcobo = customView.findViewById(R.id.LayoutAlcobo);
                        LinearLayout LayoutLicJorge = customView.findViewById(R.id.LayoutLicJorge);
                        LinearLayout LayoutTallerGeorgio = customView.findViewById(R.id.LayoutTallerGeorgio);

                        LayoutAbarroteraHidalgo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogEmpresa.dismiss();
                                textViewTipoEmpresa.setText("ABARROTERA HIDALGO");
                            }
                        });

                        LayoutBitala.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogEmpresa.dismiss();
                                textViewTipoEmpresa.setText("BITALA");
                            }
                        });

                        LayoutAlcobo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogEmpresa.dismiss();
                                textViewTipoEmpresa.setText("ALCOBO");
                            }
                        });

                        LayoutLicJorge.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogEmpresa.dismiss();
                                textViewTipoEmpresa.setText("LIC JORGE");
                            }
                        });

                        LayoutTallerGeorgio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogEmpresa.dismiss();
                                textViewTipoEmpresa.setText("TALLER GEORGIO");
                            }
                        });


                    }
                });


                botonAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String nombreUsuario = EditTextNombre.getText().toString();
                        String correoUsuario = EditTextCorreo.getText().toString();
                        String telefonoUsuario = EditTextTelefono.getText().toString().trim();
                        String password = EditTextClave.getText().toString();

                        String area = textViewSeleccionaArea.getText().toString().trim();
                        String empresa = textViewTipoEmpresa.getText().toString();
                        String tipoEmpleado = textView37.getText().toString().trim();


                        if (nombreUsuario.isEmpty() || correoUsuario.isEmpty() || telefonoUsuario.isEmpty() || password.isEmpty() || area.equalsIgnoreCase("Selecciona el area de empleado")
                                || empresa.equalsIgnoreCase("Selecciona una empresa") || tipoEmpleado.equalsIgnoreCase("Selecciona un tipo de empleado")) {
                            Utiles.crearToastPersonalizado(context, "Debes llenar todos los campos");
                        } else {
                            dialogNuevoEmpleado.dismiss();
                            AgregarUsuario(nombreUsuario, correoUsuario, telefonoUsuario, password, tipoEmpleado, empresa, area);
                        }
                    }
                });

                botonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogNuevoEmpleado.dismiss();
                    }
                });


            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ConsultarEncargado();
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


    private void ConsultarEncargado() {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);


                        String nombre = jsonObject.getString("nombre");
                        String email = jsonObject.getString("email");
                        String telefono = jsonObject.getString("telefono");
                        String fotoEncargado = jsonObject.getString("foto");

                        NombreEncargado.setText(nombre.toUpperCase());


                        String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/usuarios/" + fotoEncargado;


                        Glide.with(context)
                                .load(imageUrl)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .placeholder(R.drawable.usuarios)
                                .error(R.drawable.usuarios)
                                .into(imageViewEncargado);


                    } else {

                        NombreEncargado.setText("No se encontro encargado");


                    }
                } catch (JSONException e) {
                    NombreEncargado.setText("No se encontro encargado");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NombreEncargado.setText("No se encontro encargado");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "107");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
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
    TextView tvFaltantes;
    String idRegistroActividades;
    Button buttonGuardarChecks;

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
                    tvFaltantes = customView.findViewById(R.id.tvFaltantes);
                    buttonGuardarChecks = customView.findViewById(R.id.buttonGuardarChecks);
                    Button buttonCerrar = customView.findViewById(R.id.buttonCerrar);
                    textView20.setText("Revisión de actividades de: \n" + nombre);


                    try {


                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject actividadJson = jsonArray.getJSONObject(0);
                        idRegistroActividades = actividadJson.getString("ID_registro_actividades");
                        String estado = actividadJson.getString("estado");
                        String ID_registro_actividades = actividadJson.getString("ID_registro_actividades");


                        adaptadorCheckActividades = new AdaptadorCheckActividades(listadoActividades, context, idRegistroActividades, estado, actionListenerChecks);

                        LinearLayoutManager linearLayout = new LinearLayoutManager(context);
                        reciclerViewActividades.setLayoutManager(linearLayout);
                        reciclerViewActividades.setAdapter(adaptadorCheckActividades);


                        CheckActividades(idRegistroActividades);

                        if (estado.equalsIgnoreCase("iniciado")) {
                            buttonGuardarChecks.setEnabled(true);
                            buttonGuardarChecks.setText("Guardar");
                        } else {
                            buttonGuardarChecks.setText("Guardado");
                            buttonGuardarChecks.setEnabled(false);
                        }


                        buttonGuardarChecks.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (faltantes > 0) {
                                    Utiles.crearToastPersonalizado(context, "Debes completar la revision para terminar");
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    View customView = LayoutInflater.from(context).inflate(R.layout.modal_confirmacion, null);
                                    builder.setView(ModalRedondeado(context, customView));
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
                            }
                        });


                        buttonCerrar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogConBotones.dismiss();
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

    @Override
    public void onActualizarEncargado(String idusuario) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ConsultarEncargado();
                MostrarMecanicos();
                Utiles.crearToastPersonalizado(context, "Se actualizo al encargado");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Error al finalizar la revisión");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "108");
                params.put("idusuario", idusuario);
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
                ConsultarEncargado();

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

                ConsultarEncargado();
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

    int faltantes;

    public void CheckActividades(String ID_registro_actividades) {
        listadoActividades.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    faltantes = jsonResponse.getInt("faltantes");

                    if (faltantes != 0) {

                        tvFaltantes.setText("Revisiones faltantes: " + faltantes);
                    } else {

                        tvFaltantes.setText("Terminaste la revision");
                    }


                    JSONArray registrosArray = jsonResponse.getJSONArray("registros");

                    for (int i = 0; i < registrosArray.length(); i++) {
                        JSONObject actividad = registrosArray.getJSONObject(i);

                        int ID_check_actividad = actividad.getInt("ID_check_actividad");
                        String valor_check = actividad.getString("valor_check");
                        int ID_listado_actividades = actividad.getInt("ID_listado_actividades");
                        int ID_registro_actividades = actividad.getInt("ID_registro_actividades");
                        String nombre_actividad = actividad.getString("nombre_actividad");

                        // Hacer algo con los datos, por ejemplo, agregarlos a tu lista
                        listadoActividades.add(actividad);
                    }

                    // Actualiza tu adaptador u realiza otras acciones según tus necesidades
                    adaptadorCheckActividades.setFilteredData(listadoActividades);
                    adaptadorCheckActividades.filter("");
                    adaptadorCheckActividades.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


/*
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
*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Error al cargar");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                /*
                params.put("opcion", "73");
                params.put("registro_actividades", ID_registro_actividades);
                */

                params.put("opcion", "106");
                params.put("id_registro_actividades", ID_registro_actividades);

                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }

    @Override
    public void onActualizarCheck(String ID_check_actividad, String valor_check) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CheckActividades(idRegistroActividades);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Error al cargar, revisa la conexión");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "76");
                params.put("ID_check_actividad", ID_check_actividad);
                params.put("valor_check", valor_check);


                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    public void AgregarUsuario(String nombre, String correo, String telefono, String clave, String permisos, String empresa, String area) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, "Se agrego el usuario: " + nombre);
                MostrarMecanicos();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Error al cargar, revisa la conexión");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "110");
                params.put("nombre", nombre);
                params.put("password", clave);
                params.put("email", correo);
                params.put("telefono", telefono);
                params.put("permisos", permisos);
                params.put("empresa", empresa);
                params.put("area", area);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    public void EditarUsuario(String nombre, String correo, String telefono, String clave, String permisos, String empresa, String area, String id, AlertDialog dialogOpcionesUsuarios) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                dialogOpcionesUsuarios.dismiss();
                Utiles.crearToastPersonalizado(context, "Se actualizó al usuario: " + nombre);
                MostrarMecanicos();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Error al cargar, revisa la conexión");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "111");
                params.put("nombre", nombre);
                params.put("password", clave);
                params.put("email", correo);
                params.put("telefono", telefono);
                params.put("permisos", permisos);
                params.put("empresa", empresa);
                params.put("area", area);
                params.put("idusuario", id);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


}