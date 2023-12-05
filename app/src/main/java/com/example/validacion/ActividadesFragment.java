package com.example.validacion;

import static android.app.Activity.RESULT_OK;

import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorActividades;
import com.example.validacion.Adaptadores.AdaptadorGavetas;
import com.example.validacion.Adaptadores.AdaptadorNombresActividades;
import com.example.validacion.Adaptadores.Utiles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.pdf.parser.Line;

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

public class ActividadesFragment extends Fragment implements AdaptadorActividades.OnActivityActionListener, AdaptadorNombresActividades.OnActivityActionListener {


    private List<JSONObject> dataList = new ArrayList<>();

    Context context;
    private List<JSONObject> datosFiltrados = new ArrayList<>();
    private AdaptadorActividades adaptadorActividades;
    RecyclerView recyclerViewActividades;
    String url;

    EditText searchEditTextActividades;
    String idusuario;

    LinearLayout ContenedorContenido;
    ConstraintLayout LayoutSinResultados;
    ConstraintLayout LayoutSinInternet;

    AlertDialog modalCargando;
    AlertDialog.Builder builder;


    String rutaImagen;
    List<JSONObject> listaNombreActividades = new ArrayList<>();

    AdaptadorNombresActividades adaptadorNombresActividades;
    AlertDialog dialogSeleccionarActividad;


    public ActividadesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_actividades, container, false);
        recyclerViewActividades = view.findViewById(R.id.recyclerViewActividades);
        searchEditTextActividades = view.findViewById(R.id.searchEditTextActividades);
        ContenedorContenido = view.findViewById(R.id.ContenedorContenido);
        LayoutSinResultados = view.findViewById(R.id.LayoutSinResultados);
        LayoutSinInternet = view.findViewById(R.id.LayoutSinInternet);
        FloatingActionButton botonAgregarActividad = view.findViewById(R.id.botonAgregarActividad);
        context = requireContext();
        SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.swipeRefresh);

        url = context.getResources().getString(R.string.ApiBack);
        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            idusuario = bundle.getString("idusuario", "");
        } else {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
            idusuario = sharedPreferences.getString("idusuario", "");
        }


        adaptadorNombresActividades = new AdaptadorNombresActividades(listaNombreActividades, context, this);


        recyclerViewActividades.setLayoutManager(new LinearLayoutManager(context));
        adaptadorActividades = new AdaptadorActividades(dataList, context, this);
        recyclerViewActividades.setAdapter(adaptadorActividades);
        TomarActividadesDesdeApi(idusuario);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                TomarActividadesDesdeApi(idusuario);
                swipeRefresh.setRefreshing(false);

            }
        });


        botonAgregarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidarDisponibilidad(idusuario, new AdaptadorActividades.DisponibilidadCallback() {
                    @Override
                    public void onDisponibilidadValidada(String disponibilidad) {

                        String dispon = disponibilidad;
                        if (dispon.equals("libre")) {
                            MostrarNombresActividades();
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.opciones_asignar_actividad, null);
                            builder.setView(ModalRedondeado(view.getContext(), customView));
                             dialogSeleccionarActividad = builder.create();
                            dialogSeleccionarActividad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogSeleccionarActividad.show();
                            RecyclerView recyclerViewSeleccionActividades = customView.findViewById(R.id.recyclerViewSeleccionActividades);


                            recyclerViewSeleccionActividades.setLayoutManager(new LinearLayoutManager(view.getContext()));
                            recyclerViewSeleccionActividades.setAdapter(adaptadorNombresActividades);


                        } else {

                            Utiles.crearToastPersonalizado(context, "Esta ocupao");

                        }
                    }
                });
            }
        });


        return view;
    }

    public void MostrarNombresActividades() {
        listaNombreActividades.clear();

        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        listaNombreActividades.add(jsonObject);

                    }
                    adaptadorNombresActividades.notifyDataSetChanged();
                    adaptadorNombresActividades.setFilteredData(listaNombreActividades);
                    adaptadorNombresActividades.filter("");

                    /*
                    if (dataList.size() > 0) {
                        mostrarLayout("conContenido");
                    } else {
                        mostrarLayout("SinContenido");
                    }

                    adaptadorActividades.notifyDataSetChanged();
                    adaptadorActividades.setFilteredData(dataList);
                    adaptadorActividades.filter("");

                } catch (JSONException e) {
                    mostrarLayout("SinContenido");
                }

                searchEditTextActividades.setText("");
                //   mostrarDatosFiltrados();
                */
                } catch (JSONException e) {
                    //mostrarLayout("SinContenido");

                    Utiles.crearToastPersonalizado(context, "No hay conexion");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //     mostrarLayout("SinInternet");
                Utiles.crearToastPersonalizado(context, "No hay conexion");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "64");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    public void ValidarDisponibilidad(String idpersonal, AdaptadorActividades.DisponibilidadCallback callback) {

        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("esta libre")) {
                    callback.onDisponibilidadValidada("libre");
                } else {
                    callback.onDisponibilidadValidada("ocupado");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onDisponibilidadValidada("No hay internet");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "63");
                params.put("idpersonal", idpersonal);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    public void TomarActividadesDesdeApi(String idmecanico) {
        dataList.clear();
        modalCargando = Utiles.ModalCargando(context, builder);

        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String estatus = jsonObject.getString("estatus");

                        if (estatus.equalsIgnoreCase("activo") || estatus.equalsIgnoreCase("activa") || estatus.equalsIgnoreCase("pendiente") || estatus.equalsIgnoreCase("pausada")) {
                            dataList.add(jsonObject);
                        }
                    }

                    if (dataList.size() > 0) {
                        mostrarLayout("conContenido");
                    } else {
                        mostrarLayout("SinContenido");
                    }

                    adaptadorActividades.notifyDataSetChanged();
                    adaptadorActividades.setFilteredData(dataList);
                    adaptadorActividades.filter("");

                } catch (JSONException e) {
                    mostrarLayout("SinContenido");
                }

                searchEditTextActividades.setText("");
                //   mostrarDatosFiltrados();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mostrarLayout("SinInternet");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "23");
                params.put("idmecanico", idmecanico);
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


    private void mostrarLayout(String estado) {
        if (estado.equalsIgnoreCase("SinInternet")) {
            ContenedorContenido.setVisibility(View.GONE);
            LayoutSinInternet.setVisibility(View.VISIBLE);
            LayoutSinResultados.setVisibility(View.GONE);

        } else if (estado.equalsIgnoreCase("SinContenido")) {
            LayoutSinResultados.setVisibility(View.VISIBLE);
            ContenedorContenido.setVisibility(View.GONE);
            LayoutSinInternet.setVisibility(View.GONE);

        } else {
            ContenedorContenido.setVisibility(View.VISIBLE);
            LayoutSinInternet.setVisibility(View.GONE);
            LayoutSinResultados.setVisibility(View.GONE);
        }
        onLoadComplete();
    }


    public void ActualizarEstadoActividades(String idbitacora, String estatus, String observacion) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("esta ocupado")) {


                    Utiles.crearToastPersonalizado(context, "Ya tienes una actividad iniciada");

                } else {
                    Utiles.crearToastPersonalizado(context, "Se actualizo la actividad");
                }
                TomarActividadesDesdeApi(idusuario);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Error al actualizar el estado de la actividad, Revisa la conexión");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "27");
                params.put("idbitacora", idbitacora);
                params.put("estatus", estatus);
                params.put("idmecanico", idusuario);
                params.put("observacion", observacion);

                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    @Override
    public void onActualizarEstadoActividadesActivity(String idbitacora, String estatus, String observacion) {
        ActualizarEstadoActividades(idbitacora, estatus, observacion);
    }


    String idbitacoraSeleccionado = "";
    String nuevoStatus = "";
    String tipo_evidencia = "";

    String observacion = "";

    String descripcion = "";

    String resultadoImagen = "";

    @Override
    public void onRegistrarNuevaActividad(String idbitacora, String descripcion) {
        this.idbitacoraSeleccionado = idbitacora;
        this.descripcion = descripcion;
        dialogSeleccionarActividad.dismiss();

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
                startActivityForResult(intent, 5);
            }
        }

    }


    @Override
    public void onMandarEvidencia(String idbitacora, String estatus, String tipo_evidencia, String observacion) {

        this.idbitacoraSeleccionado = idbitacora;
        this.nuevoStatus = estatus;
        this.tipo_evidencia = tipo_evidencia;
        this.observacion = observacion;


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
            Uri selectedImageUri = data.getData();

            try {
                Bitmap selectedBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);

                MandarFoto2(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap imgBitmap = BitmapFactory.decodeFile(rutaImagen);
            imgBitmap = rotarImagen(imgBitmap, rutaImagen);
            MandarFoto2(imgBitmap);
        }


        if (requestCode == 5 && resultCode == RESULT_OK) {
            Bitmap imgBitmap = BitmapFactory.decodeFile(rutaImagen);
            imgBitmap = rotarImagen(imgBitmap, rutaImagen);
            MandarFoto(imgBitmap);
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
        modalCargando = Utiles.ModalCargando(context, builder);
        new SendImageTask().execute(imageBitmap);
    }


    private void MandarFoto(Bitmap imageBitmap) {
        modalCargando = Utiles.ModalCargando(context, builder);
        new NewSendImageTask().execute(imageBitmap);
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
                    .addFormDataPart("opcion", "61")
                    .addFormDataPart("tipo", tipo_evidencia)
                    .addFormDataPart("id_bitacora", idbitacoraSeleccionado)
                    .addFormDataPart("fotoActividad", nombreArchivo,
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
                    resultadoImagen = "Exitoso";

                } else {
                    Log.e("Error en la solicitud", String.valueOf(response.code()));
                    resultadoImagen = "Fallo";

                }
            } catch (IOException e) {
                Log.e("Error en la solicitud", e.getMessage());
                resultadoImagen = "Fallo";

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (resultadoImagen.equalsIgnoreCase("Fallo")) {
                Utiles.crearToastPersonalizado(context, "No se pudo mandar la foto");
                modalCargando.dismiss();

            } else {

                Utiles.crearToastPersonalizado(context, "Imagen subida correctamente");
                modalCargando.dismiss();
                ActualizarEstadoActividades(idbitacoraSeleccionado, nuevoStatus, observacion);
            }
        }
    }






    private class NewSendImageTask extends AsyncTask<Bitmap, Void, Void> {

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            Bitmap imageBitmap = bitmaps[0];

            OkHttpClient client = new OkHttpClient();

            String nombreArchivo = "imagen" + System.currentTimeMillis() + ".jpg";
            File imageFile = bitmapToFile(imageBitmap, "image.jpg");

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("opcion", "65")
                    .addFormDataPart("idpersonal", idusuario)
                    .addFormDataPart("idactividad", idbitacoraSeleccionado)
                    .addFormDataPart("observaciones", descripcion)
                    .addFormDataPart("fotoActividad", nombreArchivo,
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
                    resultadoImagen = "Exitoso";

                } else {
                    Log.e("Error en la solicitud", String.valueOf(response.code()));
                    resultadoImagen = "Fallo";

                }
            } catch (IOException e) {
                Log.e("Error en la solicitud", e.getMessage());
                resultadoImagen = "Fallo";

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (resultadoImagen.equalsIgnoreCase("Fallo")) {
                Utiles.crearToastPersonalizado(context, "No se pudo mandar la foto");
                modalCargando.dismiss();

            } else {

                Utiles.crearToastPersonalizado(context, "Imagen subida correctamente");
                modalCargando.dismiss();
                ActualizarEstadoActividades(idbitacoraSeleccionado, nuevoStatus, observacion);
            }
        }
    }


}
