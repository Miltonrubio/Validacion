package com.example.validacion;

import static android.app.Activity.RESULT_OK;
import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorGavetas;
import com.example.validacion.Adaptadores.AdaptadorMaquinas;
import com.example.validacion.Adaptadores.AdaptadorMecanicoAMaquina;
import com.example.validacion.Adaptadores.Utiles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class MaquinasFragment extends Fragment implements AdaptadorMecanicoAMaquina.OnActivityActionListener, AdaptadorMaquinas.OnActivityActionListener {

    public MaquinasFragment() {
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

    List<JSONObject> listaMaquinas = new ArrayList<>();
    AdaptadorMaquinas adaptadorMaquinas;

    ConstraintLayout ContenedorContenido;
    ConstraintLayout ContenedorSinContenido;
    ConstraintLayout ContenedorSinInternet;
    String fechaSeleccionada;
    String rutaImagen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maquinas, container, false);
        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);

        RecyclerView reciclerViewMaquinas = view.findViewById(R.id.reciclerViewMaquinas);
        ContenedorSinContenido = view.findViewById(R.id.ContenedorSinContenido);
        ContenedorContenido = view.findViewById(R.id.ContenedorContenido);
        ContenedorSinInternet = view.findViewById(R.id.ContenedorSinInternet);
        FloatingActionButton botonAgregarMaquina = view.findViewById(R.id.botonAgregarMaquina);


        reciclerViewMaquinas.setLayoutManager(new LinearLayoutManager(context));
        adaptadorMaquinas = new AdaptadorMaquinas(listaMaquinas, context, this, this);
        reciclerViewMaquinas.setAdapter(adaptadorMaquinas);

        MostrarMaquinas();


        botonAgregarMaquina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_agregar_maquina, null);
                builder.setView(ModalRedondeado(view.getContext(), customView));
                AlertDialog dialogAgregarMaquina = builder.create();
                dialogAgregarMaquina.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogAgregarMaquina.show();


                TextView fechaAdqui = customView.findViewById(R.id.fechaAdqui);

                Button botonCancelar = customView.findViewById(R.id.botonCancelar);
                Button botonAgregar = customView.findViewById(R.id.botonAgregarCliente);


                EditText EditTextNombre = customView.findViewById(R.id.EditTextNombre);
                EditText EditTexMarcaMaquina = customView.findViewById(R.id.EditTexMarcaMaquina);
                EditText EditTexModeloMaquina = customView.findViewById(R.id.EditTexModeloMaquina);
                EditText EditTexNoserie = customView.findViewById(R.id.EditTexNoserie);
                EditText EditTexcostoMaquina = customView.findViewById(R.id.EditTexcostoMaquina);
                EditText EditTexArea = customView.findViewById(R.id.EditTexArea);
                EditText EditTexestatus = customView.findViewById(R.id.EditTexestatus);
                EditText EditTexeobserv = customView.findViewById(R.id.EditTexeobserv);
                EditText EditTextcodigoMaquina = customView.findViewById(R.id.EditTextcodigoMaquina);


                fechaAdqui.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.opciones_fecha, null);
                        Button btnCancelar = customView.findViewById(R.id.btnCancelar);
                        Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);
                        DatePicker datePickerFecha = customView.findViewById(R.id.datePickerFecha);
                        builder.setView(ModalRedondeado(context, customView));
                        AlertDialog dialogFecha = builder.create();
                        dialogFecha.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogFecha.show();

                        buttonAceptar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                int day = datePickerFecha.getDayOfMonth();
                                int month = datePickerFecha.getMonth();
                                int year = datePickerFecha.getYear();

                                fechaSeleccionada = year + "-" + (month + 1) + "-" + day;
                                //  fechaSeleccionada = day + "/" + (month + 1) + "/" + year;
                                dialogFecha.dismiss();
                                fechaAdqui.setText(fechaSeleccionada);
                            }
                        });

                        btnCancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogFecha.dismiss();
                            }
                        });
                    }
                });


                botonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogAgregarMaquina.dismiss();
                    }
                });


                botonAgregar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nombreMaquina = EditTextNombre.getText().toString();
                        String marcaMaquina = EditTexMarcaMaquina.getText().toString();
                        String modeloMaquina = EditTexModeloMaquina.getText().toString();
                        String nserieMaquinaion = EditTexNoserie.getText().toString();
                        String costoMaquina = EditTexcostoMaquina.getText().toString();
                        String fadquisicionMaquina = fechaSeleccionada;
                        String area = EditTexArea.getText().toString();
                        String estatus = EditTexestatus.getText().toString();
                        String obs = EditTexeobserv.getText().toString();
                        String codigoMaquina = EditTextcodigoMaquina.getText().toString();


                        if (codigoMaquina.isEmpty() || nombreMaquina.isEmpty() || marcaMaquina.isEmpty() || modeloMaquina.isEmpty() || nserieMaquinaion.isEmpty() || fadquisicionMaquina.isEmpty() || area.isEmpty() ||
                                estatus.isEmpty() || obs.isEmpty()) {

                            Utiles.crearToastPersonalizado(context, "Tienes campos vacios, por favor rellenalos");
                        } else {
                            dialogAgregarMaquina.dismiss();
                            RegistrarMaquina(codigoMaquina, nombreMaquina, marcaMaquina, modeloMaquina, nserieMaquinaion, costoMaquina, fadquisicionMaquina, area, estatus, obs);

                        }
                    }
                });


            }
        });


        return view;
    }


    private void RegistrarMaquina(String codigoMaquina, String nombreMaquina, String marcaMaquina, String modeloMaquina, String nserieMaquina, String costoMaquina, String fadquisicionMaquina, String area, String estatus, String obs) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MostrarMaquinas();
                Utiles.crearToastPersonalizado(context, "Se agregó la maquina correctamente");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Hubo un error, revisa la conexión");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "52");
                params.put("nombreMaquina", nombreMaquina);
                params.put("marcaMaquina", marcaMaquina);
                params.put("modeloMaquina", modeloMaquina);
                params.put("nserieMaquina", nserieMaquina);
                params.put("codigoMaquina", codigoMaquina);
                params.put("costoMaquina", costoMaquina);
                params.put("fadquisicionMaquina", fadquisicionMaquina);
                params.put("area", area);
                params.put("estatus", estatus);
                params.put("obs", obs);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }

    private void MostrarMaquinas() {
        listaMaquinas.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listaMaquinas.add(jsonObject);

                    }
                    adaptadorMaquinas.notifyDataSetChanged();
                    adaptadorMaquinas.setFilteredData(listaMaquinas);
                    adaptadorMaquinas.filter("");

                    if (listaMaquinas.size() > 0) {

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
                params.put("opcion", "51");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void mostrarLayout(String estado) {
        if (estado.equalsIgnoreCase("SinInternet")) {
            ContenedorSinInternet.setVisibility(View.VISIBLE);
            ContenedorContenido.setVisibility(View.GONE);
            ContenedorSinContenido.setVisibility(View.GONE);

        } else if (estado.equalsIgnoreCase("SinContenido")) {
            ContenedorSinInternet.setVisibility(View.GONE);
            ContenedorContenido.setVisibility(View.GONE);
            ContenedorSinContenido.setVisibility(View.VISIBLE);

        } else if (estado.equalsIgnoreCase("conContenido")) {
            ContenedorSinInternet.setVisibility(View.GONE);
            ContenedorContenido.setVisibility(View.VISIBLE);
            ContenedorSinContenido.setVisibility(View.GONE);
        } else {

            ContenedorSinInternet.setVisibility(View.GONE);
            ContenedorContenido.setVisibility(View.GONE);
            ContenedorSinContenido.setVisibility(View.VISIBLE);
        }

//        onLoadComplete();
    }

    @Override
    public void onAsignarMecanicoAMaquina(String idusuario, String idmaquina) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MostrarMaquinas();
                Utiles.crearToastPersonalizado(context, "Se asignó el mecanico correctamente");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Hubo un error, revisa la conexión");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "54");
                params.put("idmaquina",idmaquina);
                params.put("idresponsable",idusuario);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }



    @Override
    public void onEliminarMaquina(String idmaquina) {
            StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    MostrarMaquinas();
                    Utiles.crearToastPersonalizado(context, "Se eliminó la maquina correctamente");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utiles.crearToastPersonalizado(context, "Hubo un error, revisa la conexión");
                }
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("opcion", "53");
                    params.put("idmaquina",idmaquina);
                    return params;
                }
            };

            Volley.newRequestQueue(context).add(postrequest);

    }



    String  idmaquina;

    AlertDialog dialogMaquinas;

    @Override
    public void onActualizarFoto(String idmaquina, AlertDialog dialogMaquinas) {

        this.idmaquina=idmaquina;
        this.dialogMaquinas=dialogMaquinas;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File imagenArchivo = null;
            try {
                imagenArchivo = crearImagen();
            } catch (IOException e) {
                Log.e("Error al obtener la imagen", e.toString());
            }
            if (imagenArchivo != null) {


                Uri fotoUri = FileProvider.getUriForFile(getActivity(), "com.example.validacion.fileprovider", imagenArchivo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(intent, 1);
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            // La imagen seleccionada desde la galería está en 'data.getData()'
            Uri selectedImageUri = data.getData();

            try {
                Bitmap selectedBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);

                // Luego puedes procesar 'selectedBitmap' y enviarlo al servidor
                MandarFoto2(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Llamamos a MandarFoto2 pasando la imagen capturada
            Bitmap imgBitmap = BitmapFactory.decodeFile(rutaImagen);
            imgBitmap = rotarImagen(imgBitmap, rutaImagen); // Rotar la imagen si es necesario
            MandarFoto2(imgBitmap);
        }

    }


    private Bitmap rotarImagen(Bitmap bitmap, String path) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation;
        if (exifInterface != null) {
            orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(270);
                    break;
                default:
                    return bitmap;
            }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }


    private void MandarFoto2(Bitmap imageBitmap) {
   //     modalCargando = Utiles.ModalCargando(contexto, builder);
        new SendImageTask().execute(imageBitmap);
    }


    private File crearImagen() throws IOException {
        String nombreFoto = "image";
        File directorio = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagenTemporal = File.createTempFile(nombreFoto, ".jpg", directorio);
        rutaImagen = imagenTemporal.getAbsolutePath();
        return imagenTemporal;
    }

    private File bitmapToFile(Bitmap bitmap, String fileName) {
        File file = new File(getActivity().getCacheDir(), fileName);
        try {
            file.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
            byte[] bitmapData = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    private class SendImageTask extends AsyncTask<Bitmap, Void, Void> {

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            Bitmap imageBitmap = bitmaps[0];

            OkHttpClient client = new OkHttpClient();

            String nombreArchivo = "imagen" + System.currentTimeMillis() + ".jpg";
            File imageFile = bitmapToFile(imageBitmap, "image.jpg");

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("opcion", "55")
                    .addFormDataPart("idmaquina", idmaquina)
                    .addFormDataPart("fotoMaquina", nombreArchivo,
                            RequestBody.create(MediaType.parse("image/jpeg"), imageFile))
                    .build();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            try {
                okhttp3.Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d("Respuesta del servidor", responseData);

                } else {
                    Log.e("Error en la solicitud", String.valueOf(response.code()));

                }
            } catch (IOException e) {
                Log.e("Error en la solicitud", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Toast.makeText(SubirFotosUnidadesActivity.this, "Imagen " + idSerVenta + " Enviada al servidor", Toast.LENGTH_SHORT).show();
            dialogMaquinas.dismiss();

            Utiles.crearToastPersonalizado(context, "Imagen subida correctamente");

            MostrarMaquinas();

         //   ocultarModales();
        }
    }







}