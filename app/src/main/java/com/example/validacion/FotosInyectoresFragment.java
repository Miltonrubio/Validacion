package com.example.validacion;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.validacion.Adaptadores.SlideAdapter;
import com.example.validacion.Objetos.SlideItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FotosInyectoresFragment extends Fragment {

    public FotosInyectoresFragment() {
        // Required empty public constructor
    }

    private String url;

    List<SlideItem> slideItems = new ArrayList<>();
    private Handler sliderHandler = new Handler();
    Context context;


    AlertDialog.Builder builder;
    AlertDialog modalCargando;

    String ID_inyector;
    ViewPager2 ViewPagerImagenes;
    LottieAnimationView animacionSinImagenes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fotos_inyectores, container, false);

        animacionSinImagenes = view.findViewById(R.id.animacionSinImagenes);
        ViewPagerImagenes = view.findViewById(R.id.ViewPagerImagenes);
        TextView txtId = view.findViewById(R.id.txtId);
        ImageView imagenDesdeGaleriaIM = view.findViewById(R.id.imagenDesdeGaleriaIM);
        Button FotoCamara = view.findViewById(R.id.FotoCamara);
        Button fotoDesdeGaleria = view.findViewById(R.id.fotoDesdeGaleria);


        context = requireContext();
        url = context.getResources().getString(R.string.ApiBack);


        builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);


        Bundle bundle = getArguments();
        if (bundle != null) {
            ID_inyector = bundle.getString("ID_inyector", "");
        }
        txtId.setText("Estas editando el inyector: #" + ID_inyector);

        CargarImagenes();

        FotoCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        return view;
    }






    private void CargarImagenes() {
        slideItems.clear();
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject fotoObj = jsonArray.getJSONObject(i);
                                String ID_foto_inyector = fotoObj.getString("ID_foto_inyector");
                                String url = fotoObj.getString("url");
                                String fotoUrl = "http://tallergeorgio.hopto.org:5613/tallergeorgio/imagenes/unidades/";

                                slideItems.add(new SlideItem(fotoUrl + url, ID_foto_inyector));
                            }

                            SlideAdapter slideAdapter = new SlideAdapter(slideItems, ViewPagerImagenes);

                            ViewPagerImagenes.setAdapter(slideAdapter);
                            ViewPagerImagenes.setClipToPadding(false);
                            ViewPagerImagenes.setClipChildren(false);
                            ViewPagerImagenes.setOffscreenPageLimit(4);
                            ViewPagerImagenes.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                            compositePageTransformer.addTransformer(new MarginPageTransformer(10));
                            compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                                @Override
                                public void transformPage(@NonNull View page, float position) {
                                    float r = 1 - Math.abs(position);
                                    page.setScaleY(0.85f + 0.15f);
                                }
                            });

                            ViewPagerImagenes.setPageTransformer(compositePageTransformer);
                            ViewPagerImagenes.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                public void onPageSelected(int position) {
                                    super.onPageSelected(position);
                                    sliderHandler.removeCallbacks(sliderRunnable);
                                    sliderHandler.postDelayed(sliderRunnable, 3000);
                                }
                            });


                            if (slideItems.size() > 0) {
                                animacionSinImagenes.setVisibility(View.GONE);
                                ViewPagerImagenes.setVisibility(View.VISIBLE);
                            } else {

                                animacionSinImagenes.setVisibility(View.VISIBLE);
                                ViewPagerImagenes.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            animacionSinImagenes.setVisibility(View.VISIBLE);
                            ViewPagerImagenes.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        animacionSinImagenes.setVisibility(View.VISIBLE);
                        ViewPagerImagenes.setVisibility(View.GONE);
                    }
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opcion", "122");
                params.put("ID_inyector", "36");
                return params;
            }
        };

        RequestQueue requestQueue3 = Volley.newRequestQueue(context);
        requestQueue3.add(stringRequest3);

    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            ViewPagerImagenes.setCurrentItem(ViewPagerImagenes.getCurrentItem() + 1);
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