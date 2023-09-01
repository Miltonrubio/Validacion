package com.example.validacion;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ImageDialogFragment extends DialogFragment {

    public ImageDialogFragment() {
        // Required empty public constructor
    }

    private String imageUrl;

      private Drawable imageDrawable;

    public ImageDialogFragment(Drawable imageDrawable) {
        this.imageDrawable = imageDrawable;
    }
    public ImageDialogFragment(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static ImageDialogFragment newInstance(String param1, String param2) {
        ImageDialogFragment fragment = new ImageDialogFragment();
        return fragment;
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
        View rootView = inflater.inflate(R.layout.fragment_image_dialog, container, false);

/*
        ImageView imageView = rootView.findViewById(R.id.dialogImageView);

        // Carga la imagen en el ImageView usando Glide u otra biblioteca de carga de imágenes
        Glide.with(this)
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Toast.makeText(requireContext(), "No hay nada", Toast.LENGTH_SHORT).show();
                        Log.d("No recibido", "No recibido");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Toast.makeText(requireContext(), "Imagen recibida "+ imageUrl, Toast.LENGTH_SHORT).show();
                        Log.d("Recibido", imageUrl);
                        return false;
                    }
                })
                .into(imageView);

*/

        ImageView imageView = rootView.findViewById(R.id.dialogImageView);
        imageView.setImageDrawable(imageDrawable);

        return rootView;
}

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Crear un diálogo personalizado sin bordes y fondo transparente
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        return dialog;
    }
}
