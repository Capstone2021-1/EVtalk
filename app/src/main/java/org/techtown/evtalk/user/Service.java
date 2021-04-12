package org.techtown.evtalk.user;

import java.util.List;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @PUT("/userInfo/update/{id}")
    Call<Void> updateUserInfo(@Path("id") long id, @Body User user);

    @GET("/chargingStation")
    Call<List<ChargingStation>> getChargingStation();

    @GET("/userInfo/car")
    Call<Car> getCarInfo(@Query("id") long id);

    @GET("/userInfo/membership")
    Call<List<Card>> getMembershipInfo(@Query("id") long id);

    @GET("/userInfo/payment")
    Call<List<Card>> getPaymentInfo(@Query("id") long id);

    @GET("/info/car_list")
    Call<List<Car>> getCar_list();

    @GET("/info/membership_list")
    Call<List<Card>> getMembership_list();

    @GET("/info/payment_list")
    Call<List<Card>> getPayment_list();

}
