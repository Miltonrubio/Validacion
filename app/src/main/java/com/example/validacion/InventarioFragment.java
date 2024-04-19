package com.example.validacion;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.validacion.Adaptadores.Utiles;
import com.itextpdf.text.pdf.parser.Line;

public class InventarioFragment extends Fragment {

    public InventarioFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventario, container, false);
        context = requireContext();
        LinearLayout verGavetas = view.findViewById(R.id.verGavetas);
        LinearLayout VerMaquinas = view.findViewById(R.id.VerMaquinas);
        LinearLayout verProductividad = view.findViewById(R.id.verProductividad);
        LinearLayout TiposDeUnidades = view.findViewById(R.id.TiposDeUnidades);

        LinearLayout ListaActividades = view.findViewById(R.id.ListaActividades);

/*
        verGavetas.setVisibility(View.GONE);
        VerMaquinas.setVisibility(View.GONE);
*/
        VerMaquinas.setVisibility(View.GONE);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


        verProductividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utiles.RedirigirAFragment(fragmentManager, new CrudMecanicosFragment(), null);
            }
        });


        verGavetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utiles.RedirigirAFragment(fragmentManager, new GavetasFragment(), null);
            }
        });


        VerMaquinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utiles.RedirigirAFragment(fragmentManager, new MaquinasFragment(), null);
            }
        });


        ListaActividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utiles.RedirigirAFragment(fragmentManager, new ListadoActividadesFragment(), null);
            }
        });


        TiposDeUnidades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utiles.RedirigirAFragment(fragmentManager, new CrudTiposUnidades(), null);

            }
        });

        return view;
    }
}