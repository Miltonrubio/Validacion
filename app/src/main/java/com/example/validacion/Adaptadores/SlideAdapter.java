package com.example.validacion.Adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.validacion.R;
import com.example.validacion.Objetos.SlideItem;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {


    private List<SlideItem> slideItems;
    private ViewPager2 viewPager2;

    public SlideAdapter(List<SlideItem> slideItems, ViewPager2 viewPager2) {
        this.slideItems = slideItems;
        this.viewPager2 = viewPager2;
    }

    Context context;
    String url;

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // return new SlideViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coches_container, parent, false));
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coches_container, parent, false);
        SlideViewHolder viewHolder = new SlideViewHolder(view);
        context = view.getContext();
        url = context.getResources().getString(R.string.ApiBack);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                SlideItem clickedItem = slideItems.get(position);


                if (position != RecyclerView.NO_POSITION) {
                    showAlertDialog(parent.getContext(), position, clickedItem);
                    // Toast.makeText(parent.getContext(), clickedItem.getImage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        holder.setImage(slideItems.get(position));

        if (position == slideItems.size() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return slideItems.size();
    }

    class SlideViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

        void setImage(SlideItem slideItem) {
            String fotoSlide = slideItem.getImage();

            if (!TextUtils.isEmpty(fotoSlide)) {
                Glide.with(itemView.getContext())
                        .load(fotoSlide)
                        .error(R.drawable.default_image)
                        .into(imageView);
            } else {
                Glide.with(itemView.getContext())
                        .load(R.drawable.default_image)
                        .into(imageView);
            }
        }

    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            slideItems.addAll(slideItems);
            notifyDataSetChanged();
        }
    };


    private void showAlertDialog(Context context, int position, SlideItem item) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View customView = LayoutInflater.from(context).inflate(R.layout.modal_confirmacion, null);
        builder.setView(Utiles.ModalRedondeado(context, customView));
        AlertDialog dialogConfirmacion = builder.create();
        dialogConfirmacion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogConfirmacion.show();


        String imageUri = item.getImage(); // URL de la imagen
        String idSerVenta = item.getId_ser_venta(); // ID de la venta

        TextView textView4 = customView.findViewById(R.id.textView4);
        textView4.setText("¿Quieres elegir esta imagen como principal?");

        Button buttonCancelar = customView.findViewById(R.id.buttonCancelar);
        Button buttonAceptar = customView.findViewById(R.id.buttonAceptar);

        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse(imageUri);
                String fileName = uri.getLastPathSegment();
                cambiarImagenPrincipal(context, idSerVenta, fileName);

                dialogConfirmacion.dismiss();
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogConfirmacion.dismiss();
            }
        });



        /*

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        String imageUri = item.getImage(); // URL de la imagen
        String idSerVenta = item.getId_ser_venta(); // ID de la venta


        builder.setTitle("Confirmación");
        builder.setMessage("¿Quieres elegir esta imagen como principal?");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse(imageUri);
                String fileName = uri.getLastPathSegment();
                cambiarImagenPrincipal(context, idSerVenta, fileName);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


         */
    }


    private void cambiarImagenPrincipal(Context context, String id_ser_Venta, String foto) {

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utiles.crearToastPersonalizado(context, "La foto se asignó como principal");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Utiles.crearToastPersonalizado(context, "Hubo un error, revisa la conexión");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "10");
                params.put("idventa", id_ser_Venta);
                params.put("foto", foto);
                return params;
            }
        };
        queue.add(request);

    }


}
