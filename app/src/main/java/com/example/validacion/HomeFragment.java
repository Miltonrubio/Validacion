package com.example.validacion;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    JSONArray jsonArrayNombreUnidades= null;
    String valorGas= null;
    private String selectedIDCliente="1";
    ArrayList<String> opciones = new ArrayList<>();

    String url;
    Context context;
    JSONObject jsonObjectUnidades;
    private ArrayAdapter<String> spinnerAdapterUnidades;
    private ArrayList<String> nombresClientes = new ArrayList<>();

    String Marca, Modelo, anio, placas, motor, vin;
    String id_serv_unidad = null;


    private ArrayList<String> unidadesVehiculos = new ArrayList<>();
    private RecyclerView recyclerView;
    private Adapt2 adapter2;
    private List<JSONObject> dataList = new ArrayList<>();
    private EditText editTextBusqueda;

    FloatingActionButton botonAgregarActividad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context= requireContext();
        url = context.getResources().getString(R.string.ApiBack);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        VerNombresClientes();
        ImageView LectorQr = view.findViewById(R.id.LectorQr);
        opciones.add("Lleno");
        opciones.add("3/4");
        opciones.add("1/2");
        opciones.add("1/4");
        opciones.add("Reserva");

        botonAgregarActividad = view.findViewById(R.id.botonAgregarActividad);

        LectorQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(HomeFragment.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Lector de còdigos - Taller Georgio");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        recyclerView = view.findViewById(R.id.recyclerViewFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dataList = new ArrayList<>();
        adapter2 = new Adapt2(dataList, context);
        recyclerView.setAdapter(adapter2);

        editTextBusqueda = view.findViewById(R.id.searchEditText);

        editTextBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter2.filter(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        String permisosUsuario = sharedPreferences.getString("permisos", "");
        String idusuario = sharedPreferences.getString("idusuario", "");

        if(permisosUsuario.equals("RECEPCION") || permisosUsuario.equals("SUPERADMIN")){
            botonAgregarActividad.setVisibility(View.VISIBLE);
            VisualizarServicios();
        }else {
            botonAgregarActividad.setVisibility(View.GONE);
            VisualizarServiciosPorMecanicos(idusuario);
        }


        botonAgregarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.insertar_actividad, null);
                builder.setView(dialogView);

                // Mostrar el AlertDialog
                AlertDialog dialog = builder.create();
                final Button mondongo = dialogView.findViewById(R.id.mondongo);
                final Spinner spinnerClientes = dialogView.findViewById(R.id.SpinnerClientes);
                final Spinner SpinnerUnidades = dialogView.findViewById(R.id.SpinnerUnidades);
                final Spinner SpinnerGas = dialogView.findViewById(R.id.spinnerGas);

                final EditText editTextkm = dialogView.findViewById(R.id.editTextkm);
                final EditText editTextmotivo = dialogView.findViewById(R.id.editTextmotivo);


                ArrayAdapter<String> adaptadorGas = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, opciones);
                adaptadorGas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinnerGas.setAdapter(adaptadorGas);
                SpinnerGas.setSelection(0);


                ArrayAdapter<String> spinnerAdapterClientes = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, nombresClientes);
                spinnerAdapterClientes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerClientes.setAdapter(spinnerAdapterClientes);
                String primerCliente = nombresClientes.get(0);
                int posicionCliente = spinnerAdapterClientes.getPosition(primerCliente);
                spinnerClientes.setSelection(posicionCliente);


                unidadesVehiculos.add(0, "Selecciona la unidad");
                spinnerAdapterUnidades = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, unidadesVehiculos);
                spinnerAdapterUnidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinnerUnidades.setAdapter(spinnerAdapterUnidades);
                SpinnerUnidades.setSelection(0);


                SpinnerGas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        valorGas = parent.getItemAtPosition(position).toString();
                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }

                });

                spinnerClientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String nombreCliente = parent.getItemAtPosition(position).toString();
                        if (!nombreCliente.isEmpty()) {
                            selectedIDCliente = obtenerIDDesdeNombre(nombreCliente);
                            VerNombresUnidades(selectedIDCliente);
                            spinnerAdapterUnidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            SpinnerUnidades.setAdapter(spinnerAdapterUnidades);
                            // Agregar esta línea para seleccionar automáticamente el primer elemento al cambiar la selección en el Spinner de clientes
                            SpinnerUnidades.setSelection(0);

                        }
                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


                SpinnerUnidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mondongo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String nombreUnidad = parent.getItemAtPosition(position).toString();
                                String km = editTextkm.getText().toString();
                                String motivoIngreso = editTextmotivo.getText().toString();

                                // Buscar el objeto JSON correspondiente al nombreUnidad
                                JSONObject unidadSeleccionada = null;
                                for (int i = 0; i < jsonArrayNombreUnidades.length(); i++) {
                                    try {
                                        JSONObject jsonObject = jsonArrayNombreUnidades.getJSONObject(i);
                                        String id_serv_unidad = jsonObject.getString("id_serv_unidad");
                                        if (nombreUnidad.contains(id_serv_unidad)) {
                                            unidadSeleccionada = jsonObject;
                                            break; // Terminar el bucle una vez que se haya encontrado la unidad
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (unidadSeleccionada != null) {
                                    // Aquí puedes acceder a los datos de la unidad seleccionada

                                    try {
                                        id_serv_unidad = unidadSeleccionada.getString("id_serv_unidad");
                                        String Marca = unidadSeleccionada.getString("Marca");
                                        String Modelo = unidadSeleccionada.getString("Modelo");
                                        String anio = unidadSeleccionada.getString("anio");
                                        String placas = unidadSeleccionada.getString("placas");
                                        String motor = unidadSeleccionada.getString("motor");
                                        String vin = unidadSeleccionada.getString("vin");


                                        if (km.isEmpty() || motivoIngreso.isEmpty()) {
                                            if (isAdded()) {
                                                Toast.makeText(context, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                                            }

                                            return;
                                        } else {

                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
                                            alertDialogBuilder.setMessage("Marca: "+Marca +"\nModelo: "+Modelo+"\nGas:"+ valorGas +"\nKilometraje: "+ km + "\nMotivo De Ingreso: " + motivoIngreso +"\n\n¿Estas seguro de mandar este servicio?");
                                            alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    AgregarServicio(selectedIDCliente, id_serv_unidad, km, valorGas, motivoIngreso, Marca, Modelo,motor,vin,placas, anio);
                                                    dialog.dismiss();
                                                    dialogInterface.dismiss();
                                                }
                                            });
                                            alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            });

                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.show();
                                        }

                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

                                }
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


                dialog.show();
            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String scanResult = result.getContents();
            editTextBusqueda.setText(scanResult);
        } else {
            if (isAdded()) {
                Toast.makeText(getContext(), "Escaneo cancelado", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void VisualizarServicios() {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    dataList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        dataList.add(jsonObject); // Agrega cada objeto JSON a la lista
                    }
                    adapter2.notifyDataSetChanged();
                    adapter2.setFilteredData(dataList);
                    adapter2.filter("");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editTextBusqueda.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "2");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private void VisualizarServiciosPorMecanicos(String idmecanico) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    dataList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        dataList.add(jsonObject); // Agrega cada objeto JSON a la lista
                    }
                    adapter2.notifyDataSetChanged();
                    adapter2.setFilteredData(dataList);
                    adapter2.filter("");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editTextBusqueda.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "25");
                params.put("idmecanico", idmecanico);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }






    private void VerNombresClientes() {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    nombresClientes.clear(); // Limpia la lista antes de agregar los nuevos
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id_ser_cliente = jsonObject.getString("id_ser_cliente");
                        String nombre = jsonObject.getString("nombre");
                        nombresClientes.add(id_ser_cliente + ": " + nombre); // Agrega el ID y nombre de la actividad a la lista

                        //   VerNombresUnidades(id_ser_cliente);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (isAdded()) {
                    Toast.makeText(context, "Hubo un error", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "19");
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }

    private String obtenerIDDesdeNombre(String nombreSeleccionado) {
        for (String nombreCliente : nombresClientes) {
            if (nombreCliente.equals(nombreSeleccionado)) {
                // Dividir la cadena para obtener el ID (asumiendo que esté separado por ":")
                String[] partes = nombreCliente.split(":");
                if (partes.length > 0) {
                    return partes[0].trim(); // Devuelve el ID (eliminando espacios en blanco)
                }
            }
        }
        return null; // Si no se encuentra el ID, puedes devolver null o un valor predeterminado
    }

    private void VerNombresUnidades(String id_ser_cliente) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonArrayNombreUnidades = new JSONArray(response);
                    unidadesVehiculos.clear(); // Limpia la lista antes de agregar los nuevos nombres
                    for (int i = 0; i < jsonArrayNombreUnidades.length(); i++) {
                        jsonObjectUnidades = jsonArrayNombreUnidades.getJSONObject(i);
                        id_serv_unidad = jsonObjectUnidades.getString("id_serv_unidad");
                        Marca = jsonObjectUnidades.getString("Marca");
                        Modelo = jsonObjectUnidades.getString("Modelo");
                        anio = jsonObjectUnidades.getString("anio");
                        placas = jsonObjectUnidades.getString("placas");
                        motor = jsonObjectUnidades.getString("motor");
                        vin = jsonObjectUnidades.getString("vin");
                        unidadesVehiculos.add(id_serv_unidad + ": " + Marca + " " + Modelo); // Agrega el ID y nombre de la unidad a la lista
                    }

                    // Notificar al adaptador que los datos han cambiado
                    spinnerAdapterUnidades.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (isAdded()) {
                    Toast.makeText(context, "Hubo un error", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "20");
                params.put("id_ser_cliente", id_ser_cliente);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }


    private String obtenerIDDesdeNombreVehiculo(String nombreSeleccionado) {
        for (String vehiculos : unidadesVehiculos) {
            if (vehiculos.equals(nombreSeleccionado)) {
                // Dividir la cadena para obtener el ID (asumiendo que esté separado por ":")
                String[] partes2 = vehiculos.split(":");
                if (partes2.length > 0) {
                    return partes2[0].trim(); // Devuelve el ID (eliminando espacios en blanco)
                }
            }
        }
        return null; // Si no se encuentra el ID, puedes devolver null o un valor predeterminado
    }


    private void AgregarServicio(String id_ser_cliente, String idunidad, String km, String gas, String motivo, String marca, String modelo, String motor, String vin, String placas, String anio) {
        StringRequest postrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                VisualizarServicios();
                if (isAdded()) {
                    Toast.makeText(context, "Servicio Agregado", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (isAdded()) {
                    Toast.makeText(context, "Hubo un error", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "21");
                params.put("id_ser_cliente", id_ser_cliente);
                params.put("idunidad", idunidad);
                params.put("km", km);
                params.put("gas", gas);
                params.put("motivo", motivo);
                params.put("marca", marca);
                params.put("modelo", modelo);
                params.put("motor", motor);
                params.put("vin", vin);
                params.put("placas", placas);
                params.put("anio", anio);
                return params;
            }
        };

        Volley.newRequestQueue(context).add(postrequest);
    }

}


