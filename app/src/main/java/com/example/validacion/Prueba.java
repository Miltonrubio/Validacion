package com.example.validacion;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Prueba extends AppCompatActivity {

    private CameraManager cameraManager;
    Button btnGuardarFoto;
    Button btnGuardarFoto2;
    ImageView imageFoto;
    String rutaImagen;

    Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_prueba);
        imageFoto = findViewById(R.id.imageFoto);

        btnGuardarFoto = findViewById(R.id.guardarFoto);
        btnGuardarFoto2 = findViewById(R.id.guardarFoto2);


        Intent intent = getIntent();
        if (intent != null) {
            int position = intent.getIntExtra("position", -1);

            // Muestra la posición en el TextView
            TextView txtId = findViewById(R.id.txtId); // Asegúrate de que el ID del TextView sea el correcto
            txtId.setText("Posición: " + position);
        }
        btnGuardarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirCamara();
            }
        });

        btnGuardarFoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MandarFoto();
            }
        });

    }


    private void MandarFoto() {
        String url = "http://192.168.1.252/georgioapi/Controllers/Apiback.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {

                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(context, "Respuesta Vacia", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, "Error en volley", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "9");
                params.put("idventa", "60");
                params.put("id_ser_fotos", "7425");
                params.put("tipo", "Frontal"); // Agregar el valor de "tipo"

                // Convertir la imagen a Base64
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_image); // Reemplaza "your_image" con el nombre de tu imagen
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                params.put("image", encodedImage); // Agregar la imagen en formato Base64

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    private void AbrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {

            File imagenArchivo = null;
            try {
                imagenArchivo = crearImagen();
            } catch (IOException e) {
                Log.e("Error al obtener la imaegn", e.toString());

            }
            if (imagenArchivo != null) {
                Uri fotoUri = FileProvider.getUriForFile(this, "com.example.validacion.fileprovider", imagenArchivo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(intent, 1);
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Bundle extras = data.getExtras();


            Bitmap imgBitmap = BitmapFactory.decodeFile(rutaImagen);
            imageFoto.setImageBitmap(imgBitmap);
            Toast.makeText(Prueba.this, "Si jalo", Toast.LENGTH_SHORT).show();
        }
    }

    private File crearImagen() throws IOException {
        String nombreFoto = "foto_taller";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagenTemporal = File.createTempFile(nombreFoto, ".jpg", directorio);
        rutaImagen = imagenTemporal.getAbsolutePath();
        return imagenTemporal;
    }

}