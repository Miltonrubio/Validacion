package com.example.validacion;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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

    String url = "http://192.168.1.252/georgioapi/Controllers/Apiback.php";
    JSONObject jsonObjectUnidades;
    private ArrayAdapter<String> spinnerAdapterUnidades;

    private ArrayList<String> nombresClientes = new ArrayList<>();

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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView LectorQr = view.findViewById(R.id.LectorQr);


        botonAgregarActividad = view.findViewById(R.id.botonAgregarActividad);


        botonAgregarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerNombresClientes();

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.insertar_actividad, null);
                builder.setView(dialogView);

                final Spinner spinnerClientes = dialogView.findViewById(R.id.SpinnerClientes);
                final Spinner SpinnerUnidades = dialogView.findViewById(R.id.SpinnerUnidades);

                final EditText editTextkm = dialogView.findViewById(R.id.editTextkm);
                final EditText editTextmotivo = dialogView.findViewById(R.id.editTextmotivo);

                /*
                final EditText editTextmarca = dialogView.findViewById(R.id.editTextmarca);

                final EditText editTextvin = dialogView.findViewById(R.id.editTextvin);
                final EditText editTextmodelo = dialogView.findViewById(R.id.editTextmodelo);
                final EditText editTextmotor = dialogView.findViewById(R.id.editTextmotor);
                final EditText editTextplacas = dialogView.findViewById(R.id.editTextplacas);
                final EditText editTextanio = dialogView.findViewById(R.id.editTextanio);
*/
                nombresClientes.add(0, "Selecciona al cliente");
                unidadesVehiculos.add(0, "Selecciona la unidad");

                ArrayAdapter<String> spinnerAdapterClientes = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, nombresClientes);
                spinnerAdapterClientes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerClientes.setAdapter(spinnerAdapterClientes);


                spinnerAdapterUnidades = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, unidadesVehiculos);
                spinnerAdapterUnidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SpinnerUnidades.setAdapter(spinnerAdapterUnidades);

                spinnerClientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String nombreCliente = parent.getItemAtPosition(position).toString();
                        if (!nombreCliente.equals("Selecciona al cliente")) {
                            String selectedIDCliente = obtenerIDDesdeNombre(nombreCliente);
                            VerNombresUnidades(selectedIDCliente);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        SpinnerUnidades.setVisibility(View.GONE);
                    }
                });


                SpinnerUnidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String nombreUnidad = parent.getItemAtPosition(position).toString();
                        if (!nombreUnidad.equals("Selecciona la unidad")) {

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Obtener el valor del Spinner después de que el usuario haya hecho una selección
                        String nombreCliente = spinnerClientes.getSelectedItem().toString();
                        String selectedIDCliente = obtenerIDDesdeNombre(nombreCliente);

                        String nombreUnidad = SpinnerUnidades.getSelectedItem().toString();
                        String selectedIDUnidad = obtenerIDDesdeNombreVehiculo(nombreUnidad);

                        // Verificar si se seleccionó el hint
                        if (!nombreUnidad.equals("Selecciona la unidad")) {
                            String kilometrajeVehiculo = editTextkm.getText().toString();
                            String motivoDeIngresoVehiculo = editTextmotivo.getText().toString();


                            //    AgregarActividad(selectedID, descripcionActividad, ID_usuario);
                        } else {
                            // Mostrar un mensaje de error o realizar la acción deseada
                            Toast.makeText(requireContext(), "Debes seleccionar una actividad válida.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", null);

                // Mostrar el AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


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
        recyclerView.setLayoutManager(new

                LinearLayoutManager(getContext()));

        dataList = new ArrayList<>();
        adapter2 = new

                Adapt2(dataList, requireContext());
        recyclerView.setAdapter(adapter2);

        editTextBusqueda = view.findViewById(R.id.searchEditText);

        editTextBusqueda.addTextChangedListener(new

                                                        TextWatcher() {
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

        EnviarWS();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String scanResult = result.getContents();
            editTextBusqueda.setText(scanResult);
        } else {
            Toast.makeText(getContext(), "Escaneo cancelado", Toast.LENGTH_SHORT).show();
        }
    }

    private void EnviarWS() {
        String url = "http://tallergeorgio.hopto.org:5611/georgioapp/georgioapi/Controllers/Apiback.php";
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

        Volley.newRequestQueue(requireContext()).add(postrequest);
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
                Toast.makeText(requireContext(), "Hubo un error", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "19");
                return params;
            }
        };

        Volley.newRequestQueue(requireContext()).add(postrequest);
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
                    JSONArray jsonArrayNombreUnidades = new JSONArray(response);
                    unidadesVehiculos.clear(); // Limpia la lista antes de agregar los nuevos nombres
                    for (int i = 0; i < jsonArrayNombreUnidades.length(); i++) {
                        jsonObjectUnidades = jsonArrayNombreUnidades.getJSONObject(i);
                        String id_serv_unidad = jsonObjectUnidades.getString("id_serv_unidad");
                        String Marca = jsonObjectUnidades.getString("Marca");
                        String Modelo = jsonObjectUnidades.getString("Modelo");
                        unidadesVehiculos.add(id_serv_unidad + ": " + Marca + " " + Modelo); // Agrega el ID y nombre de la unidad a la lista
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(requireContext(), "Hubo un error", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("opcion", "20");
                params.put("id_ser_cliente", id_ser_cliente);
                return params;
            }
        };

        Volley.newRequestQueue(requireContext()).add(postrequest);
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

}


