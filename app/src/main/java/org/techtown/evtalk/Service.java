package org.techtown.evtalk;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    @GET("/")
    Call<String> connect();

    @POST("/login")
    Call<String> getUser(@Body User user);

    @GET("/userInfo")
    Call<User> getUserInfo(@Query("id") long id);

    @DELETE("/delete/{id}")
    Call<Void> deleteUser(@Path("id") long id);

}
