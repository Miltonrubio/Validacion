package com.example.validacion;

import static android.app.Activity.RESULT_OK;
import static com.example.validacion.Adaptadores.Utiles.crearToastPersonalizado;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.validacion.Adaptadores.AdaptadorCoches;
import com.example.validacion.Adaptadores.AdaptadorTiposUnidades;
import com.example.validacion.Adaptadores.Utiles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class CrudTiposUnidades extends Fragment implements AdaptadorTiposUnidades.OnActivityActionListener {


    public CrudTiposUnidades() {


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    Context context;
    String url;

    AlertDialog modalCargando;

    AlertDialog.Builder builder;
    AdaptadorTiposUnidades adaptadorTiposUnidades;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int IMAGEN_PARA_EDITAR = 4;

    ImageView imageViewFotoSeleccionada;
    String nombre_tipo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crud_tipos_unidades, container, false);

        RecyclerView recylcerViewTiposUnidades = view.findViewById(R.id.recylcerViewTiposUnidades);

        FloatingActionButton botonAgregarActividad = view.findViewById(R.id.botonAgregarActividad);


        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);


        GridLayoutManager gridLayoutManager= new GridLayoutManager(context, 2);
        recylcerViewTiposUnidades.setLayoutManager(gridLayoutManager);

        adaptadorTiposUnidades = new AdaptadorTiposUnidades(listaTiposUnidades, context, this);
        recylcerViewTiposUnidades.setAdapter(adaptadorTiposUnidades);

        VerTipoUnidades();


        botonAgregarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View customView = LayoutInflater.from(context).inflate(R.layout.modal_agregar_tipo_unidad, null);
                builder.setView(Utiles.ModalRedondeado(context, customView));
                AlertDialog dialogNuevoTipoUnidad = builder.create();
                dialogNuevoTipoUnidad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogNuevoTipoUnidad.show();

                Button btnAceptar = customView.findViewById(R.id.btnAceptar);
                imageViewFotoSeleccionada = customView.findViewById(R.id.imageViewFotoSeleccionada);


                EditText editText2 = customView.findViewById(R.id.editText2);
                Button buttonAbrirGaleria = customView.findViewById(R.id.buttonAbrirGaleria);
                Button button2 = customView.findViewById(R.id.button2);


                imageViewFotoSeleccionada.setVisibility(View.GONE);


                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogNuevoTipoUnidad.dismiss();
                    }
                });

                buttonAbrirGaleria.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openGallery("Agregar");
                    }
                });


                btnAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        nombre_tipo = editText2.getText().toString();
                        if (nombre_tipo.equals("") || nombre_tipo.isEmpty()) {
                            Utiles.crearToastPersonalizado(context, "Debes ingresar un nombre");
                        } else {
                            if (selectedImageUri != null) {
                                try {
                                    Bitmap selectedBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                                    MandarFoto2(selectedBitmap);
                                    dialogNuevoTipoUnidad.dismiss();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {

                                Utiles.crearToastPersonalizado(context, "Debes subir una foto");

                            }

                        }


                    }
                });


            }
        });


        return view;
    }


    private void openGallery(String estado) {

        if (estado.equalsIgnoreCase("Agregar")) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } else {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGEN_PARA_EDITAR);
        }


    }


    private void MandarFoto2(Bitmap imageBitmap) {
        modalCargando = Utiles.ModalCargando(context, builder);
        new SendImageTask().execute(imageBitmap);
    }


    private void MandarFotoEditar(Bitmap imageBitmap) {
        modalCargando = Utiles.ModalCargando(context, builder);
        new SendImageTaskEditar().execute(imageBitmap);
    }

    Uri selectedImageUri;

    ImageView imageViewFotoSeleccionadaEditar;

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

            selectedImageUri = data.getData();
            imageViewFotoSeleccionada.setImageURI(selectedImageUri);
            imageViewFotoSeleccionada.setVisibility(View.VISIBLE);
        }

        if (requestCode == IMAGEN_PARA_EDITAR && resultCode == RESULT_OK && data != null) {

            selectedImageUri = data.getData();
            imageViewFotoSeleccionadaEditar.setImageURI(selectedImageUri);
            imageViewFotoSeleccionadaEditar.setVisibility(View.VISIBLE);
        }


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
                    .addFormDataPart("opcion", "91")
                    .addFormDataPart("nombre_tipo", nombre_tipo)
                    .addFormDataPart("foto", nombreArchivo,
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

            Utiles.crearToastPersonalizado(context, "Imagen subida correctamente");
            modalCargando.dismiss();
            VerTipoUnidades();

        }
    }


    private class SendImageTaskEditar extends AsyncTask<Bitmap, Void, Void> {

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            Bitmap imageBitmap = bitmaps[0];
            OkHttpClient client = new OkHttpClient();
            String nombreArchivo = "imagen" + System.currentTimeMillis() + ".jpg";
            File imageFile = bitmapToFile(imageBitmap, "image.jpg");

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("opcion", "95")
                    .addFormDataPart("nombre_tipo", editarNombreTipo)
                    .addFormDataPart("ID_tipo_unidad", ID_tipo_unidad)
                    .addFormDataPart("foto", nombreArchivo,
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

            Utiles.crearToastPersonalizado(context, "Imagen subida correctamente");
            modalCargando.dismiss();
            dialogOpcionesTipoUnidad.dismiss();
            VerTipoUnidades();

        }
    }


    List<JSONObject> listaTiposUnidades = new ArrayList<>();


    private void VerTipoUnidades() {
        listaTiposUnidades.clear();
        modalCargando = Utiles.ModalCargando(context, builder);
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        listaTiposUnidades.add(jsonObject);
                    }

                    adaptadorTiposUnidades.setFilteredData(listaTiposUnidades);
                    adaptadorTiposUnidades.filter("");


                } catch (JSONException e) {
                    crearToastPersonalizado(context, "Error al cargar los datos");
                }
                onLoadComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                crearToastPersonalizado(context, "Error al cargar los datos");
                onLoadComplete();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "90");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void onLoadComplete() {
        if (modalCargando.isShowing() && modalCargando != null) {
            modalCargando.dismiss();
        }
    }


    @Override
    public void onEliminarTipo(String ID_tipo_unidad, AlertDialog dialogOpcionesTipoUnidad) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                crearToastPersonalizado(context, "Se eliminó correctamente");
                dialogOpcionesTipoUnidad.dismiss();
                VerTipoUnidades();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                crearToastPersonalizado(context, "Error al cargar los datos");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "93");
                params.put("ID_tipo_unidad", ID_tipo_unidad);


                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    String editarNombreTipo;

    String ID_tipo_unidad;
    AlertDialog dialogOpcionesTipoUnidad;

    @Override
    public void onEditarTipoUnidad(String ID_tipo_unidad, String foto, String nombre, AlertDialog dialogOpcionesTipoUnidad) {

        this.ID_tipo_unidad = ID_tipo_unidad;
        this.dialogOpcionesTipoUnidad = dialogOpcionesTipoUnidad;


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View customView = LayoutInflater.from(context).inflate(R.layout.modal_agregar_tipo_unidad, null);
        builder.setView(Utiles.ModalRedondeado(context, customView));
        AlertDialog dialogEditarTipoUnidad = builder.create();
        dialogEditarTipoUnidad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEditarTipoUnidad.show();

        TextView textView28 = customView.findViewById(R.id.textView28);
        textView28.setText("Editar tipo de unidad");
        EditText editText2 = customView.findViewById(R.id.editText2);


        imageViewFotoSeleccionadaEditar = customView.findViewById(R.id.imageViewFotoSeleccionada);
        Button buttonAbrirGaleria = customView.findViewById(R.id.buttonAbrirGaleria);
        Button button2 = customView.findViewById(R.id.button2);
        Button btnAceptar = customView.findViewById(R.id.btnAceptar);

        editText2.setText(nombre);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEditarTipoUnidad.dismiss();
            }
        });

        String imageUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/" + foto;

        if (foto.equalsIgnoreCase("") || foto.isEmpty()) {

            imageViewFotoSeleccionadaEditar.setVisibility(View.GONE);

        } else {

            Glide.with(context)
                    .load(imageUrl)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageViewFotoSeleccionadaEditar);


            imageViewFotoSeleccionadaEditar.setVisibility(View.VISIBLE);
        }


        buttonAbrirGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery("Editar");
            }
        });


        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                editarNombreTipo = editText2.getText().toString();
                if (editarNombreTipo.equals("") || editarNombreTipo.isEmpty()) {
                    Utiles.crearToastPersonalizado(context, "Debes ingresar un nombre");
                } else {
                    if (selectedImageUri != null) {
                        try {
                            Bitmap selectedBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                            MandarFotoEditar(selectedBitmap);
                            dialogEditarTipoUnidad.dismiss();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {

                        Utiles.crearToastPersonalizado(context, "Debes subir una foto");

                    }

                }


            }
        });

    }


    public void onActualizarNombre(String ID_tipo_unidad, String nombre, AlertDialog dialogOpcionesTipoUnidad) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View customView = LayoutInflater.from(context).inflate(R.layout.modal_agregar_tipo_unidad, null);
        builder.setView(Utiles.ModalRedondeado(context, customView));
        AlertDialog dialogEditarTipoUnidad = builder.create();
        dialogEditarTipoUnidad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEditarTipoUnidad.show();

        TextView textView28 = customView.findViewById(R.id.textView28);
        textView28.setText("Editar tipo de unidad");
        EditText editText2 = customView.findViewById(R.id.editText2);
        editText2.setText(nombre);

        TextView textView26 = customView.findViewById(R.id.textView26);
        textView26.setVisibility(View.GONE);

        ImageView imageViewFotoSeleccionada = customView.findViewById(R.id.imageViewFotoSeleccionada);
        imageViewFotoSeleccionada.setVisibility(View.GONE);

        Button buttonAbrirGaleria = customView.findViewById(R.id.buttonAbrirGaleria);
        buttonAbrirGaleria.setVisibility(View.GONE);

        Button button2 = customView.findViewById(R.id.button2);
        Button btnAceptar = customView.findViewById(R.id.btnAceptar);


        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nuevoNombreTipo = editText2.getText().toString();

                if (nuevoNombreTipo.isEmpty()) {
                    Utiles.crearToastPersonalizado(context, "Debes ingresar un nombre ");
                } else {

                    dialogEditarTipoUnidad.dismiss();
                    dialogOpcionesTipoUnidad.dismiss();
                    ActualizarNombre(ID_tipo_unidad, nuevoNombreTipo);

                }
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEditarTipoUnidad.dismiss();

            }
        });


    }


    private void ActualizarNombre(String ID_tipo_unidad, String nuevoNombre) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                crearToastPersonalizado(context, "Se editó correctamente");
                VerTipoUnidades();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                crearToastPersonalizado(context, "Error al cargar los datos");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "92");
                params.put("ID_tipo_unidad", ID_tipo_unidad);
                params.put("nombre_tipo", nuevoNombre);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    public void onActualizarCheck(String ID_tipo_unidad, String tipo_check, String valor_check) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                VerTipoUnidades();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                crearToastPersonalizado(context, "Error al cargar los datos");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "96");
                params.put("ID_tipo_unidad", ID_tipo_unidad);
                params.put("tipo_check", tipo_check);
                params.put("valor_check", valor_check);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


}