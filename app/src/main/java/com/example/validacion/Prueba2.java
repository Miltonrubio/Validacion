package com.example.validacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.validacion.Adaptadores.SlideAdapter2;
import com.example.validacion.Adaptadores.Utiles;
import com.example.validacion.Objetos.SlideItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Prueba2 extends AppCompatActivity {

    List<SlideItem> slideItems = new ArrayList<>();
    private Button generatePdfBtn;
    private File pdfFileImagenes;
    ViewPager2 viewPager2;

    private Handler sliderHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba2);

        generatePdfBtn = findViewById(R.id.generatePdfBtn);
        viewPager2 = findViewById(R.id.viewPager2);

        slideItems.clear();
        slideItems.add(new SlideItem("http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/010517b32ac5b640cd134d6c4503ff27.jpg", "1"));
        slideItems.add(new SlideItem("http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/010aca7e324f9966ab767c0128d63a96.jpg", "1"));
        slideItems.add(new SlideItem("http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/bf8a73d0b93408399677aac957b77ada.jpg", "1"));
        slideItems.add(new SlideItem("http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/6a59df1b0be0b163a706ee0b752cc78d.jpg", "1"));


        SlideAdapter2 slideAdapter = new SlideAdapter2(slideItems, viewPager2);

        viewPager2.setAdapter(slideAdapter);
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

        generatePdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                generatePdfFromUrls(slideItems);

            }
        });
    }




    private void generatePdfFromUrls(List<SlideItem> slideItems) {
        PdfDocument pdfDocument = new PdfDocument();

        for (int i = 0; i < slideItems.size(); i++) {
            final int position = i;
            SlideItem slideItem = slideItems.get(i);
            String imageUrl = slideItem.getImage();

            Picasso.get().load(imageUrl).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), position + 2).create();
                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                    Canvas canvas = page.getCanvas();
                    Paint paint = new Paint();
                    paint.setColor(Color.BLACK);
                    canvas.drawBitmap(bitmap, 0, 0, paint);

                    pdfDocument.finishPage(page);

                    if (position == slideItems.size() - 1) {
                        pdfFileImagenes = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "images_to_pdf.pdf");
                        try {
                            pdfDocument.writeTo(new FileOutputStream(pdfFileImagenes));
                            pdfDocument.close();

                            if (pdfFileImagenes != null && pdfFileImagenes.exists()) {
                                compartirPDFDeImagnes();
                            } else {
                           //     Toast.makeText(Prueba2.this, "PDF file not found.", Toast.LENGTH_SHORT).show();
                                Utiles.crearToastPersonalizado(Prueba2.this, "PDF file not found.");
                            }
                        } catch (IOException e) {
                            Utiles.crearToastPersonalizado(Prueba2.this, "Error: " + e.getMessage());
                        }
                    }
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    Utiles.crearToastPersonalizado(Prueba2.this, "No se pudo cargar la imagen desde el url ");

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
        }
    }

    private void compartirPDFDeImagnes() {

        Uri pdfUri = FileProvider.getUriForFile(Prueba2.this,
                "com.example.validacion.fileprovider", pdfFileImagenes);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share PDF"));
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


