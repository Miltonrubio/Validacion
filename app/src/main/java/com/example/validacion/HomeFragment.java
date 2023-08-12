package com.example.validacion;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.validacion.Activity_Binding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    private static final String TAG = "POKEDEX";

    private RecyclerView recyclerView;
    private Adaptador adaptador;

    private Retrofit retrofit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerViewCoches); // Encuentra la vista dentro de la vista inflada
        adaptador = new Adaptador(requireContext()); // Usa requireContext() para obtener el contexto del fragment
        recyclerView.setAdapter(adaptador);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2); // También usa requireContext()
        recyclerView.setLayoutManager(layoutManager);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.252/georgioapi")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        obtenerDatos();

        return rootView;

    }



    private void obtenerDatos(){

        CochesService service = retrofit.create(CochesService.class);

        Call<CochesRespuesta> cochesRespuestaCall = service.obtenerCoche(2);
        cochesRespuestaCall.enqueue(new Callback<CochesRespuesta>() {


            @Override
            public void onResponse(Call<CochesRespuesta> call, Response<CochesRespuesta> response) {
                if (response.isSuccessful()){

                    CochesRespuesta cochesRespuesta = response.body();
                    ArrayList<Coches> listaCoches= cochesRespuesta.getResults();

                    adaptador.adicionarListaCoches(listaCoches);

                    for (int i =0; i <listaCoches.size(); i++){
                        Coches p= listaCoches.get(i);
                        Log.i(TAG, "Marca: "+ p.getMarca());
                    }

                }else{
                    Log.e(TAG, "onResponse: " +response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CochesRespuesta> call, Throwable t) {

                Log.e(TAG, "onFailure: " + t.getMessage());
            }

        });

    }
}