package com.example.validacion.Adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.validacion.R;

public class Utiles {

    public static void crearToastPersonalizado(Context context, String mensaje) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_con_logo, null);

        ImageView logoImage = layout.findViewById(R.id.logo_image);
        TextView toastText = layout.findViewById(R.id.toast_text);
        logoImage.setImageResource(R.drawable.tallergeorgiopng);
        toastText.setText(mensaje);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }


    public static androidx.appcompat.app.AlertDialog ModalCargando(Context contextDialog, androidx.appcompat.app.AlertDialog.Builder builder) {

        View customView = LayoutInflater.from(contextDialog).inflate(R.layout.cargando, null);
        CardView cardView = new CardView(contextDialog);
        cardView.setBackgroundResource(R.drawable.modal_transparente);
        cardView.addView(customView);
        builder.setView(cardView);
        androidx.appcompat.app.AlertDialog dialogCargando = builder.create();
        dialogCargando.setCanceledOnTouchOutside(false);
        dialogCargando.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogCargando.show();

        return dialogCargando;
    }


    public static CardView ModalRedondeado(Context contextDialog, View viewModal) {

        CardView cardView = new CardView(contextDialog);
        cardView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        cardView.setBackgroundResource(R.drawable.rounded_background);
        cardView.addView(viewModal);
        return cardView;
    }





    public static void RedirigirAFragment(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layoutCoches, fragment)
                .addToBackStack(null)
                .commit();
    }


}
