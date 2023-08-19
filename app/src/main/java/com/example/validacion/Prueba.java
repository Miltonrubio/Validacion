package com.example.validacion;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
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

import java.io.File;
import java.io.IOException;

public class Prueba extends AppCompatActivity {

    private CameraManager cameraManager;
    Button btnGuardarFoto;
    ImageView imageFoto;
    String rutaImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);
        imageFoto = findViewById(R.id.imageFoto);

        btnGuardarFoto = findViewById(R.id.guardarFoto);

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
    }


    private void AbrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {

            File imagenArchivo = null;
            try{
                imagenArchivo= crearImagen();
            }catch (IOException e){
                Log.e("Error al obtener la imaegn", e.toString());

            }
            if (imagenArchivo!=null){
                Uri fotoUri= FileProvider.getUriForFile(this, "com.example.validacion.fileprovider", imagenArchivo);
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
        String nombreFoto="foto_taller";
        File directorio=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagenTemporal=File.createTempFile(nombreFoto, ".jpg", directorio);
        rutaImagen= imagenTemporal.getAbsolutePath();
        return imagenTemporal;
    }

}