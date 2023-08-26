package com.example.validacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class Prueba2 extends AppCompatActivity {


    Button log_out;

    ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba2);

        viewPager2 = findViewById(R.id.ViewPager425);


        List<SlideItem> slideItems = new ArrayList<>();
        slideItems.add(new SlideItem("http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/0dae41abd73c135c15730828328eb56a.jpg", "1"));
        slideItems.add(new SlideItem("http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/54320220de0e1462e914998658cec710.jpg","2"));
        slideItems.add(new SlideItem("http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/27bea1b4e42ccd5ab66d50d09483eb4a.jpg","3"));
        slideItems.add(new SlideItem("http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/0dae41abd73c135c15730828328eb56a.jpg","4"));


        viewPager2.setAdapter(new SlideAdapter(slideItems, viewPager2));


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