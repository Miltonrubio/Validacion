package com.example.validacion.Adaptadores;

import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;
import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.validacion.ActividadesFragment;
import com.example.validacion.Activity_Binding;
import com.example.validacion.DetalleFragment;
import com.example.validacion.DetallesActividadesFragment;
import com.example.validacion.R;
import com.example.validacion.SubirFotosUnidadesActivity;
import com.itextpdf.text.pdf.parser.Line;

import org.checkerframework.common.returnsreceiver.qual.This;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class AdaptadorProductividadMecanicos extends RecyclerView.Adapter<AdaptadorProductividadMecanicos.ViewHolder> {

    private Context context;

    private List<JSONObject> filteredData;
    private List<JSONObject> data;
    String url;
    FragmentManager fragmentManager;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuarios, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        try {
            JSONObject jsonObject2 = filteredData.get(position);
            String foto = jsonObject2.optString("foto", "");
            String idusuario = jsonObject2.optString("idusuario", "");
            String telefono = jsonObject2.optString("telefono", "");
            String nombre = jsonObject2.optString("nombre", "");
            String empresa = jsonObject2.optString("empresa", "");
            String area = jsonObject2.optString("area", "");
            String email = jsonObject2.optString("email", "");
            String password = jsonObject2.optString("password", "");
            String tipo = jsonObject2.optString("tipo", "");
            String permisos = jsonObject2.optString("permisos", "");


            Bundle bundle = new Bundle();
            bundle.putString("idusuario", idusuario);
            bundle.putString("nombre", nombre);

            String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/usuarios/" + foto;


            holder.cargoMec.setText(permisos.toUpperCase());
            holder.telefonoMec.setText(telefono);


            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.mecanico)
                    .error(R.drawable.mecanico)
                    .into(holder.ImagenMecanico);

            holder.NombreMecanico.setText(nombre.toUpperCase());


            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_opciones_actividades, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogOpcionesUsuarios = builder.create();
                    dialogOpcionesUsuarios.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogOpcionesUsuarios.show();

                    LinearLayout LyoutConsultarReportes = customView.findViewById(R.id.LyoutConsultarReportes);
                    LinearLayout LayoutGenerarRegistro = customView.findViewById(R.id.LayoutGenerarRegistro);
                    LinearLayout LayoutEncargado = customView.findViewById(R.id.LayoutEncargado);
                    LinearLayout LayoutEditar = customView.findViewById(R.id.LayoutEditar);


                    LayoutEncargado.setVisibility(View.VISIBLE);

                    LayoutEditar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AbrirModalEditar(idusuario, nombre, email, telefono, password, dialogOpcionesUsuarios);
                        }
                    });


                    LayoutEncargado.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            ConsultarEncargado(idusuario, dialogOpcionesUsuarios);

                        }
                    });


                    LyoutConsultarReportes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            DetallesActividadesFragment detallesActividadesFragment = new DetallesActividadesFragment();
                            detallesActividadesFragment.setArguments(bundle);

                            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_layoutCoches, detallesActividadesFragment)
                                    .addToBackStack(null)
                                    .commit();
                            dialogOpcionesUsuarios.dismiss();
                        }
                    });

                    LayoutGenerarRegistro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            actionListener.onValidarCheckDiario(idusuario, view, nombre);

                        }
                    });


                    return false;
                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (!permisos.equalsIgnoreCase("SUPERADMIN")) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_opciones_actividades, null);
                        builder.setView(ModalRedondeado(view.getContext(), customView));
                        AlertDialog dialogOpcionesUsuarios = builder.create();
                        dialogOpcionesUsuarios.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogOpcionesUsuarios.show();

                        LinearLayout LyoutConsultarReportes = customView.findViewById(R.id.LyoutConsultarReportes);
                        LinearLayout LayoutGenerarRegistro = customView.findViewById(R.id.LayoutGenerarRegistro);
                        LinearLayout LayoutEditar = customView.findViewById(R.id.LayoutEditar);


                        LayoutEditar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AbrirModalEditar(idusuario, nombre, email, telefono, password, dialogOpcionesUsuarios);
                            }
                        });


                        LyoutConsultarReportes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                DetallesActividadesFragment detallesActividadesFragment = new DetallesActividadesFragment();
                                detallesActividadesFragment.setArguments(bundle);

                                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.frame_layoutCoches, detallesActividadesFragment)
                                        .addToBackStack(null)
                                        .commit();
                                dialogOpcionesUsuarios.dismiss();
                            }
                        });

                        LayoutGenerarRegistro.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                actionListener.onValidarCheckDiario(idusuario, view, nombre);

                            }
                        });


                    } else {


                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_opciones_actividades, null);
                        builder.setView(ModalRedondeado(view.getContext(), customView));
                        AlertDialog dialogOpcionesUsuarios = builder.create();
                        dialogOpcionesUsuarios.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogOpcionesUsuarios.show();

                        LinearLayout LyoutConsultarReportes = customView.findViewById(R.id.LyoutConsultarReportes);
                        LinearLayout LayoutGenerarRegistro = customView.findViewById(R.id.LayoutGenerarRegistro);
                        LinearLayout LayoutEncargado = customView.findViewById(R.id.LayoutEncargado);

                        LayoutEncargado.setVisibility(View.VISIBLE);
                        LayoutGenerarRegistro.setVisibility(View.GONE);
                        LyoutConsultarReportes.setVisibility(View.GONE);

                        LinearLayout LayoutEditar = customView.findViewById(R.id.LayoutEditar);


                        LayoutEditar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AbrirModalEditar(idusuario, nombre, email, telefono, password, dialogOpcionesUsuarios);
                            }
                        });


                        LayoutEncargado.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                ConsultarEncargado(idusuario, dialogOpcionesUsuarios);

                            }
                        });


                    }

                }

            });


            /*

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Utiles.RedirigirAFragment(fragmentManager, new ActividadesFragment(), bundle);
                }
            });
*/

        } finally {

        }
    }

/*

    public void ValidarCheckDiario(String ID_usuario, View view, String nombre) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equalsIgnoreCase("\"Agregado\"")) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_check_actividades, null);
                    builder.setView(ModalRedondeado(view.getContext(), customView));
                    AlertDialog dialogConBotones = builder.create();
                    dialogConBotones.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogConBotones.show();


                    RecyclerView reciclerViewActividades = customView.findViewById(R.id.reciclerViewActividades);
                    TextView textView20 = customView.findViewById(R.id.textView20);
                    Button button = customView.findViewById(R.id.button);
                    textView20.setText("Listado de actividades de " + nombre);

                    adaptadorCheckActividades = new AdaptadorCheckActividades(listadoActividades, context, response);

                    LinearLayoutManager linearLayout = new LinearLayoutManager(context);
                    reciclerViewActividades.setLayoutManager(linearLayout);
                    reciclerViewActividades.setAdapter(adaptadorCheckActividades);
                    CheckActividades();


                } else {

                    Utiles.crearToastPersonalizado(context, "Se creo el registro de actividades");

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
                params.put("opcion", "74");
                params.put("fecha", fechaActual);
                params.put("ID_usuario", ID_usuario);


                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }
 */


    public void ConsultarEncargado(String idusuario, AlertDialog dialogOpcionesActividades) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);


                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        View customView = LayoutInflater.from(context).inflate(R.layout.modal_encargado, null);
                        builder.setView(ModalRedondeado(context, customView));
                        AlertDialog dialogEncargado = builder.create();
                        dialogEncargado.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogEncargado.show();

                        TextView NombreEncargado = customView.findViewById(R.id.NombreEncargado);
                        ImageView ImagenEncargado = customView.findViewById(R.id.ImagenEncargado);

                        Button btnCancelar = customView.findViewById(R.id.btnCancelar);
                        Button btnAceptar = customView.findViewById(R.id.btnAceptar);


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
                                .into(ImagenEncargado);


                        //  String mensaje = "Nombre: " + nombre + "\nEmail: " + email + "\nTeléfono: " + telefono;


                        btnAceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                dialogEncargado.dismiss();
                                dialogOpcionesActividades.dismiss();
                                actionListener.onActualizarEncargado(idusuario);

                            }
                        });

                        btnCancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogEncargado.dismiss();
                            }
                        });


                    } else {
                        Utiles.crearToastPersonalizado(context, "No se encontraron datos en la respuesta");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utiles.crearToastPersonalizado(context, "Error al parsear la respuesta JSON");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Error al cargar, revisa la conexión");
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


    @Override
    public int getItemCount() {

        //return filteredData.size();
        return filteredData.size();

    }


    private void AbrirModalEditar(String id, String nombre, String email, String telefono, String password, AlertDialog dialogOpcionesUsuarios) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View customView = LayoutInflater.from(context).inflate(R.layout.modal_agregar_usuarios, null);
        builder.setView(Utiles.ModalRedondeado(context, customView));
        AlertDialog dialogNuevoEmpleado = builder.create();
        dialogNuevoEmpleado.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogNuevoEmpleado.show();


        Button botonCancelar = customView.findViewById(R.id.botonCancelar);
        Button botonAceptar = customView.findViewById(R.id.botonAceptar);
        botonAceptar.setText("Actualizar");


        EditText EditTextNombre = customView.findViewById(R.id.EditTextNombre);
        EditText EditTextCorreo = customView.findViewById(R.id.EditTextCorreo);
        EditText EditTextTelefono = customView.findViewById(R.id.EditTextTelefono);
        EditText EditTextClave = customView.findViewById(R.id.EditTextClave);

        TextView textViewSeleccionaArea = customView.findViewById(R.id.textViewSeleccionaArea);
        TextView textViewTipoEmpresa = customView.findViewById(R.id.textViewTipoEmpresa);
        TextView textView37 = customView.findViewById(R.id.textView37);

        EditTextNombre.setText(nombre);
        EditTextCorreo.setText(email);
        EditTextTelefono.setText(telefono);
        EditTextClave.setText(password);

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
                    actionListener.EditarUsuario(nombreUsuario, correoUsuario, telefonoUsuario, password, tipoEmpleado, empresa, area, id, dialogOpcionesUsuarios);

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


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView NombreMecanico;

        ImageView ImagenMecanico;

        TextView cargoMec;
        TextView telefonoMec;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            NombreMecanico = itemView.findViewById(R.id.NombreMecanico);
            ImagenMecanico = itemView.findViewById(R.id.ImagenMecanico);
            cargoMec = itemView.findViewById(R.id.cargoMec);
            telefonoMec = itemView.findViewById(R.id.telefonoMec);
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
                String telefono = item.optString("telefono", "").toLowerCase();
                String estatus = item.optString("estatus", "").toLowerCase();
                String placas = item.optString("placas", "").toLowerCase();
                String modelo = item.optString("modelo", "").toLowerCase();

                String direccion = item.optString("direccion", "").toLowerCase();
                String id = item.optString("id", "").toLowerCase();


                boolean matchesAllKeywords = true;

                for (String keyword : keywords) {
                    if (!(modelo.contains(keyword) || empresa.contains(keyword) || direccion.contains(keyword) || telefono.contains(keyword) || id.contains(keyword) || placas.contains(keyword) ||
                            nombre.contains(keyword) || estatus.contains(keyword))) {
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


    public interface OnActivityActionListener {
        void onFilterData(Boolean resultados);

        void onValidarCheckDiario(String ID_usuario, View view, String nombre);

        void onActualizarEncargado(String idusuario);

        void AgregarUsuario(String nombre, String correo, String telefono, String clave, String permisos, String empresa, String area);

        void EditarUsuario(String nombre, String correo, String telefono, String clave, String permisos, String empresa, String area, String id, AlertDialog dialogOpcionesUsuarios);
    }

    private AdaptadorProductividadMecanicos.OnActivityActionListener actionListener;


    public AdaptadorProductividadMecanicos(List<JSONObject> data, Context context, AdaptadorProductividadMecanicos.OnActivityActionListener actionListener) {
        this.data = data;
        this.context = context;
        this.filteredData = new ArrayList<>(data);
        this.actionListener = actionListener;
        url = context.getResources().getString(R.string.ApiBack);
    }


}
