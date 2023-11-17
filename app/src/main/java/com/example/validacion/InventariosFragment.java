package com.example.validacion;

import static android.app.Activity.RESULT_OK;
import static com.example.validacion.Adaptadores.Utiles.ModalRedondeado;

import android.app.Activity;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.AdaptadorBuscarHerramientas;
import com.example.validacion.Adaptadores.AdaptadorCoches;
import com.example.validacion.Adaptadores.AdaptadorGavetas;
import com.example.validacion.Adaptadores.AdaptadorHerramientas;
import com.example.validacion.Adaptadores.AdaptadorMecanicosNuevo;
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

public class InventariosFragment extends Fragment implements AdaptadorHerramientas.OnActivityActionListener, AdaptadorGavetas.OnActivityActionListener, AdaptadorMecanicosNuevo.OnActivityActionListener {


    String rutaImagen;


    public InventariosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Context context;
    String url;

    List<JSONObject> listaGavetas = new ArrayList<>();
    AdaptadorGavetas adaptadorGavetas;

    ConstraintLayout ContenedorSinContenido;

    ConstraintLayout ContenedorContenido;

    ConstraintLayout ContenedorSinInternet;

    String idHerramienta;

    private boolean isSearching = false;


    AdaptadorBuscarHerramientas adaptadorBuscarHerramientas;
    RecyclerView RecyclerViewHerramientas;
    LottieAnimationView lottieNoHerramientas;


    List<JSONObject> listaHerramientas = new ArrayList<>();
    List<JSONObject> listaHerramientasFiltrada = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inventarios, container, false);

        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);

        RecyclerView reciclerViewGavetas = view.findViewById(R.id.reciclerViewGavetas);

        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);

        ContenedorSinContenido = view.findViewById(R.id.ContenedorSinContenido);
        ContenedorContenido = view.findViewById(R.id.ContenedorContenido);
        ContenedorSinInternet = view.findViewById(R.id.ContenedorSinInternet);
        FloatingActionButton botonAgregarGaveta = view.findViewById(R.id.botonAgregarGaveta);
        EditText searchEditText = view.findViewById(R.id.searchEditText);
        ImageView btnFiltro = view.findViewById(R.id.btnFiltro);


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptadorGavetas.filter(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        reciclerViewGavetas.setLayoutManager(new LinearLayoutManager(context));
        adaptadorGavetas = new AdaptadorGavetas(listaGavetas, context, this, this, this);
        reciclerViewGavetas.setAdapter(adaptadorGavetas);

        MostrarGavetas();


        botonAgregarGaveta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.agregar_gaveta, null);
                builder.setView(ModalRedondeado(view.getContext(), customView));
                AlertDialog dialogAgregarGaveta = builder.create();
                dialogAgregarGaveta.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogAgregarGaveta.show();


                EditText EditTextNombre = customView.findViewById(R.id.EditTextNombre);
                EditText EditTexDescripcionGaveta = customView.findViewById(R.id.EditTexDescripcionGaveta);
                EditText EditTexCantidadCajones = customView.findViewById(R.id.EditTexCantidadCajones);
                Button botonCancelar = customView.findViewById(R.id.botonCancelar);
                Button botonAgregarCliente = customView.findViewById(R.id.botonAgregarCliente);

                botonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogAgregarGaveta.dismiss();
                    }
                });


                botonAgregarCliente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String nombreGaveta = EditTextNombre.getText().toString();
                        String descripcionGaveta = EditTexDescripcionGaveta.getText().toString();
                        String numCajones = EditTexCantidadCajones.getText().toString();


                        if (nombreGaveta.isEmpty() || descripcionGaveta.isEmpty() || numCajones.isEmpty()) {
                            Utiles.crearToastPersonalizado(context, "Tienes campos vacios, por favor llenalos");
                        } else {

                            dialogAgregarGaveta.dismiss();
                            AgregarGaveta(nombreGaveta, descripcionGaveta, numCajones);
                        }
                    }
                });
            }
        });


        btnFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.modal_buscar_herramienta, null);
                builder.setView(ModalRedondeado(view.getContext(), customView));
                AlertDialog dialogAgregarGaveta = builder.create();
                dialogAgregarGaveta.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogAgregarGaveta.show();

                BuscarHerramienta();

                SearchView searchView = customView.findViewById(R.id.searchView);
                RecyclerViewHerramientas = customView.findViewById(R.id.RecyclerViewHerramientas);
                lottieNoHerramientas = customView.findViewById(R.id.lottieNoHerramientas);

                RecyclerViewHerramientas.setLayoutManager(new LinearLayoutManager(view.getContext()));
                adaptadorBuscarHerramientas = new AdaptadorBuscarHerramientas(listaHerramientasFiltrada, view.getContext());
                RecyclerViewHerramientas.setAdapter(adaptadorBuscarHerramientas);


                searchView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchView.setIconified(false);
                    }
                });
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return true;
                    }

                    @Override

                    public boolean onQueryTextChange(String newText) {
                        busqueda(newText);
                        return false;
                    }
                });
            }
        });


        return view;
    }


    private void BuscarHerramienta() {

        modalCargando = Utiles.ModalCargando(context, builder);
        listaHerramientas.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listaHerramientas.add(jsonObject);
                    }

                    onLoadComplete();

                } catch (JSONException e) {
                    mostrarLayoutHerramientas("SinInternet");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mostrarLayoutHerramientas("SinInternet");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "47");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void busqueda(String query) {
        listaHerramientasFiltrada.clear();
        String[] keywords = query.toLowerCase().split(" ");

        for (JSONObject item : listaHerramientas) {
            String nombre = item.optString("nombre", "").toLowerCase();
            String descripcion = item.optString("descripcion", "").toLowerCase();

            boolean matchesAllKeywords = true;

            for (String keyword : keywords) {
                if (!(nombre.contains(keyword) || descripcion.contains(keyword))) {
                    matchesAllKeywords = false;
                    break;
                }
            }

            if (matchesAllKeywords) {
                listaHerramientasFiltrada.add(item);
            }

            adaptadorBuscarHerramientas.notifyDataSetChanged();
            adaptadorBuscarHerramientas.setFilteredData(listaHerramientasFiltrada);
            adaptadorBuscarHerramientas.filter("");

            if (listaHerramientasFiltrada.size() > 0) {

                mostrarLayoutHerramientas("conContenido");
            } else {

                mostrarLayoutHerramientas("SinInternet");
            }
        }
    }

    private void mostrarLayoutHerramientas(String estado) {
        if (estado.equalsIgnoreCase("conContenido")) {

            lottieNoHerramientas.setVisibility(View.GONE);
            RecyclerViewHerramientas.setVisibility(View.VISIBLE);
        } else {
            lottieNoHerramientas.setVisibility(View.VISIBLE);
            RecyclerViewHerramientas.setVisibility(View.GONE);

        }
    }


    private void AgregarGaveta(String nombreGaveta, String descripcionGaveta, String numCajones) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utiles.crearToastPersonalizado(context, response);
                MostrarGavetas();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utiles.crearToastPersonalizado(context, "Algo falló, revisa la conexión");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "45");
                params.put("nombreGaveta", nombreGaveta);
                params.put("descripcionGaveta", descripcionGaveta);
                params.put("numCajones", numCajones);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void MostrarGavetas() {
        listaGavetas.clear();
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        listaGavetas.add(jsonObject);

                    }
                    adaptadorGavetas.notifyDataSetChanged();
                    adaptadorGavetas.setFilteredData(listaGavetas);
                    adaptadorGavetas.filter("");

                    if (listaGavetas.size() > 0) {

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
                params.put("opcion", "40");
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


    private void onLoadComplete() {
        if (modalCargando.isShowing() && modalCargando != null) {
            modalCargando.dismiss();
        }
    }


    AlertDialog dialogMostrarHerramientas;

    AlertDialog DialogHerramientas;


    AlertDialog.Builder builder;
    AlertDialog modalCargando;

    Context contexto;

    @Override
    public void onAbrirCamera(String idHerramienta, AlertDialog
            dialogMostrarHerramientas, AlertDialog DialogHerramientas, Context contexto) {
        this.idHerramienta = idHerramienta;
        this.dialogMostrarHerramientas = dialogMostrarHerramientas;
        this.DialogHerramientas = DialogHerramientas;
        this.contexto = contexto;

        builder = new AlertDialog.Builder(contexto);
        builder.setCancelable(false);

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


    public void onEliminarGaveta(String id_gabeta, String nombre) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                MostrarGavetas();
                Utiles.crearToastPersonalizado(context, "Se elimino la gaveta " + nombre);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "No se puedo eliminar la gaveta, revisa la conexión");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "46");
                params.put("id_gabeta", id_gabeta);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
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
        modalCargando = Utiles.ModalCargando(contexto, builder);
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
                    .addFormDataPart("opcion", "41")
                    .addFormDataPart("idHerramienta", idHerramienta)
                    .addFormDataPart("fotoHerramienta", nombreArchivo,
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
            Utiles.crearToastPersonalizado(context, "Imagen subida correctamente");


            ocultarModales();
        }
    }


    private void ocultarModales() {


        if ((dialogMostrarHerramientas.isShowing() && dialogMostrarHerramientas != null) && (DialogHerramientas.isShowing() && DialogHerramientas != null)) {

            dialogMostrarHerramientas.dismiss();
            DialogHerramientas.dismiss();
        }

        if (modalCargando.isShowing() && modalCargando != null) {
            modalCargando.dismiss();
        }
    }


    @Override
    public void onAsignarMecanico(String id_mecanico, String id_gabeta, String nombreMecanico) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                MostrarGavetas();
                Utiles.crearToastPersonalizado(context, "Se asignó el mecanico " + nombreMecanico + " a esta gaveta");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Utiles.crearToastPersonalizado(context, "No se puedo asignar la gaveta, revisa la conexión");

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "49");
                params.put("id_gabeta", id_gabeta);
                params.put("id_mecanico", id_mecanico);

                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


}