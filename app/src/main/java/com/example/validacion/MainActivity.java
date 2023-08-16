package com.example.validacion;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "PreferenciaInicial";
    private static final String EVENT_KEY = "Evento Realizado";
    String personalToken;


    String url = "http://192.168.1.124/android/guardar.php";


    String UrlApiRH = "http://192.168.1.252/georgioapi/Controllers/Apiback.php";
    private RequestQueue rq;
    Context context;

    TextView inputUsername, inputPassword;

    CheckBox checkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        rq = Volley.newRequestQueue(context);
        inputUsername = findViewById(R.id.correoET);
        inputPassword = findViewById(R.id.passwordET);
        checkBoxRememberMe = findViewById(R.id.checkBoxRememberMe);


        tomarToken();
        /*
        SharedPreferences preferenciaInicial = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean eventOccurred = preferenciaInicial.getBoolean(EVENT_KEY, false);

        if (!eventOccurred) {
            tomarToken();
            SharedPreferences.Editor editor = preferenciaInicial.edit();
            editor.putBoolean(EVENT_KEY, true);
            editor.apply();
        }

*/
        SharedPreferences sharedPreferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);
        if (rememberMe) {
            String savedUsername = sharedPreferences.getString("email", "");
            String savedPassword = sharedPreferences.getString("password", "");
            inputUsername.setText(savedUsername);
            inputPassword.setText(savedPassword);
            checkBoxRememberMe.setChecked(true);
            Intent intent = new Intent(MainActivity.this, Activity_Binding.class);
            startActivity(intent);
            finish();
        }
    }

    private void guardarCredenciales(String username, String password, boolean rememberMe) {
        SharedPreferences sharedPreferences = getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", username);
        editor.putString("password", rememberMe ? password : "");
        editor.putBoolean("rememberMe", rememberMe);
        editor.apply();
    }


    public void IniciarSession(View view) {
        IniciarSession();
    }

    private void IniciarSession() {
        String valorusername = inputUsername.getText().toString();
        String valorpassword = inputPassword.getText().toString();
        boolean rememberMe = checkBoxRememberMe.isChecked();




        if (valorusername.isEmpty() || valorpassword.isEmpty()) {
            Toast.makeText(context, "LLENE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
        } else {
            Login(valorusername, valorpassword);
            RegistrarToken(valorusername,valorpassword);
            guardarCredenciales(valorusername, valorpassword, rememberMe);
        }
    }


    private void Login(String Username, String Password) {

        StringRequest requestLogin = new StringRequest(Request.Method.POST, UrlApiRH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals(response)) {
                    if (response.equals("fallo")) {
                        Toast.makeText(context, "USUARIO O CONTRASEÑA INCORRECTA", Toast.LENGTH_SHORT).show();
                    } else {

                        try {
                            // Convertir la respuesta en un JSONArray
                            JSONArray jsonArray = new JSONArray(response);

                            // Procesar los datos del JSONArray si es necesario
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                            }


                            // Inicio de sesión exitoso, redirigir a la actividad PrincipalActivity
                            Intent intent = new Intent(MainActivity.this, Activity_Binding.class);
                            startActivity(intent);
                            finish();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(context, "SERVIDORES EN MANTENIMIENTO... VUELVA A INTENTAR MAS TARDE ", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(context, "ERROR AL CONECTAR", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "SERVIDORES EN MANTENIMIENTO... VUELVA A INTENTAR MAS TARDE ", Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("opcion", "1");
                params.put("email", Username);
                params.put("password", Password);
                return params;
            }
        };
        rq.add(requestLogin);
    }


    private void tomarToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String token) {
                        Log.d(Utils.TAG, token);
                        personalToken = token;
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "No se recibio el token", Toast.LENGTH_LONG).show();
                });
    }



    public  void RegistrarToken (String email, String password){

           String str_token=personalToken;

            StringRequest request2= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                public void onResponse(String response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            ){
                protected Map<String, String>getParams() throws AuthFailureError {

                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Token", str_token);
                    params.put("Password", password);
                    params.put("Email", email);

                    return params;
                }
            };

            RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(request2);

        }


    }