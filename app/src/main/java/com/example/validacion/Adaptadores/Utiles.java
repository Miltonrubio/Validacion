package com.example.validacion.Adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.cardview.widget.CardView;

import com.example.validacion.R;

public class Utiles {

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

}
