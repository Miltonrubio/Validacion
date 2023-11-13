package com.example.validacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

//import com.android.volley.Request;
// import com.android.volley.Response;

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
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.SlideAdapter;
import com.example.validacion.Adaptadores.Utiles;
import com.example.validacion.Objetos.SlideItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubirFotosUnidadesActivity extends AppCompatActivity {

    private Handler sliderHandler = new Handler();
    String url;

    ViewPager2 viewPager2;
    Button btnGuardarFoto, fotoDesdeGaleria;
    String rutaImagen;
    String idSerVenta;
    Context context;

    ImageView imagenDesdeGaleriaIM;
    LottieAnimationView animacionSinImagenes;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = this;
        url = context.getResources().getString(R.string.ApiBack);
        setContentView(R.layout.activity_subirfotosunidades);
        btnGuardarFoto = findViewById(R.id.guardarFoto);
        TextView txtId = findViewById(R.id.txtId);
        fotoDesdeGaleria = findViewById(R.id.fotoDesdeGaleria);
        viewPager2 = findViewById(R.id.ViewPagerImagenes);
        imagenDesdeGaleriaIM = findViewById(R.id.imagenDesdeGaleriaIM);
        animacionSinImagenes = findViewById(R.id.animacionSinImagenes);

        Intent intent = getIntent();
        idSerVenta = intent.getStringExtra("id_ser_venta");
        txtId.setText("Id de venta: " + idSerVenta);
        CargarImagenes(idSerVenta);


        SharedPreferences sharedPreferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);

        String permisosUsuario = sharedPreferences.getString("permisos", "");

        if (permisosUsuario.equals("SUPERADMIN") || permisosUsuario.equals("RECEPCIONISTA")) {
            fotoDesdeGaleria.setVisibility(View.VISIBLE);
        } else {
            fotoDesdeGaleria.setVisibility(View.GONE);
        }

        fotoDesdeGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirGaleria();
            }
        });

        btnGuardarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AbrirCamara();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            // La imagen seleccionada desde la galería está en 'data.getData()'
            Uri selectedImageUri = data.getData();

            try {
                Bitmap selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

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


    private void AbrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File imagenArchivo = null;
            try {
                imagenArchivo = crearImagen();
            } catch (IOException e) {
                Log.e("Error al obtener la imagen", e.toString());
            }
            if (imagenArchivo != null) {
                Uri fotoUri = FileProvider.getUriForFile(this, "com.example.validacion.fileprovider", imagenArchivo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(intent, 1);
            }
        }
    }


    private void AbrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }


    private void MandarFoto2(Bitmap imageBitmap) {
        new SendImageTask().execute(imageBitmap);
    }


    private File crearImagen() throws IOException {
        String nombreFoto = "image";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagenTemporal = File.createTempFile(nombreFoto, ".jpg", directorio);
        rutaImagen = imagenTemporal.getAbsolutePath();
        return imagenTemporal;
    }

    private File bitmapToFile(Bitmap bitmap, String fileName) {
        File file = new File(getCacheDir(), fileName);
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
                    .addFormDataPart("opcion", "9")
                    .addFormDataPart("idventa", idSerVenta)
                    .addFormDataPart("image", nombreArchivo,
                            RequestBody.create(MediaType.parse("image/jpeg"), imageFile))
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d("Respuesta del servidor", responseData);
                } else {
                    Log.e("Error en la solicitud", String.valueOf(response.code()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Toast.makeText(SubirFotosUnidadesActivity.this, "Imagen " + idSerVenta + " Enviada al servidor", Toast.LENGTH_SHORT).show();
            Utiles.crearToastPersonalizado(context, "Imagen enviada al servidor");
            Intent intent = new Intent(SubirFotosUnidadesActivity.this, Activity_Binding.class);
            startActivity(intent);
        }
    }


    private void CargarImagenes(String idventa) {

        List<SlideItem> slideItems = new ArrayList<>();
        slideItems.clear();
        StringRequest stringRequest3 = new StringRequest(com.android.volley.Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject fotoObj = jsonArray.getJSONObject(i);
                                String foto = fotoObj.getString("foto");

                                String id_ser_venta = fotoObj.getString("id_ser_venta");
                                String fotoUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/";

                                slideItems.add(new SlideItem(fotoUrl + foto, id_ser_venta));
                            }


                            if (slideItems.size() > 0) {
                                animacionSinImagenes.setVisibility(View.GONE);
                                viewPager2.setVisibility(View.VISIBLE);

                            } else {

                                animacionSinImagenes.setVisibility(View.VISIBLE);
                                viewPager2.setVisibility(View.GONE);

                            }

                            viewPager2.setAdapter(new SlideAdapter(slideItems, viewPager2));
                            viewPager2.setClipToPadding(false);
                            viewPager2.setClipChildren(false);
                            viewPager2.setOffscreenPageLimit(4);
                            viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                            compositePageTransformer.addTransformer(new MarginPageTransformer(10));
                            compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                                @Override
                                public void transformPage(@NonNull View page, float position) {
                                    float r = 1 - Math.abs(position);
                                    page.setScaleY(0.85f + 0.15f);
                                }
                            });

                            viewPager2.setPageTransformer(compositePageTransformer);
                            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                public void onPageSelected(int position) {
                                    super.onPageSelected(position);
                                    sliderHandler.removeCallbacks(sliderRunnable);
                                    sliderHandler.postDelayed(sliderRunnable, 3000);
                                }
                            });


                        } catch (JSONException e) {
                            animacionSinImagenes.setVisibility(View.VISIBLE);
                            viewPager2.setVisibility(View.GONE);
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        animacionSinImagenes.setVisibility(View.VISIBLE);
                        viewPager2.setVisibility(View.GONE);
                        Utiles.crearToastPersonalizado(context, "No hay internet");
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "8");
                params.put("idventa", idSerVenta);
                return params;
            }
        };

        RequestQueue requestQueue3 = Volley.newRequestQueue(context);
        requestQueue3.add(stringRequest3);

    }


    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };


    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

}