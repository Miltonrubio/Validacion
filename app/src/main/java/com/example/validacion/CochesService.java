package com.example.validacion;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface CochesService {


 @POST("Controllers/Apiback.php")
 Call<Coches> getCoches(@Path("cochesNumber") int cochesNumber);


 @POST("Controllers/Apiback.php")
 Call<CochesRespuesta> obtenerCoche(@Field("opcion") int opcion);


 @POST
 Call<Coches> getFotoByUrl(@Url String url);


}




