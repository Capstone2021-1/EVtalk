package org.techtown.evtalk;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Service {

    @GET("/")
    Call<String> connect();

    @POST("/login")
    Call<String> getUser(@Body User user);

}
