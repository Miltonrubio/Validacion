package com.example.validacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Prueba2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba2);

        Button btnScann = findViewById(R.id.btnScann);

        btnScann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                IntentIntegrator integrador = new IntentIntegrator(Prueba2.this);
                integrador.setDesiredBarcodeFormats(integrador.ALL_CODE_TYPES);
                integrador.setPrompt("Lector - DCP");

                integrador.setCameraId(0);
                integrador.setBeepEnabled(true);
                integrador.setBarcodeImageEnabled(true);

                integrador.initiateScan();
*/
            }
        });
    }

    /*
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);


        TextView txtResultado = findViewById(R.id.txtResultado);
        if(!result.equals(null)){
            if (result.getContents()== null){
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
            }else {

                Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                txtResultado.setText(result.getContents());
            }
        }else {

            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

}