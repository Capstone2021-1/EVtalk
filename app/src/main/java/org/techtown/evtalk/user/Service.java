package org.techtown.evtalk.user;

import org.techtown.evtalk.ui.station.review.Review;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
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

    @POST("/user")
    Call<String> getUser(@Body User user);

    @GET("/user")
    Call<User> getUserInfo(@Query("id") long id);

    @DELETE("/user/{id}")
    Call<Void> deleteUser(@Path("id") long id);

    @PUT("/user/{id}")
    Call<Void> updateUserInfo(@Path("id") long id, @Body User user);

    @PUT("/user/membership/{id}")
    Call<Void> updateUserMemInfo(@Path("id") long id, @Body List<Card> membership);

    @PUT("/user/payment/{id}")
    Call<Void> updateUserPayInfo(@Path("id") long id, @Body List<Card> payment);

    @PUT("/user/car/{id}")
    Call<Car> updateUserCarInfo(@Path("id") long id, @Body Car car);

    @GET("/chargingStation")
    Call<List<ChargingStation>> getChargingStation();

    @GET("/user/car")
    Call<Car> getCarInfo(@Query("id") long id);

    @GET("/user/membership")
    Call<List<Card>> getMembershipInfo(@Query("id") long id);

    @GET("/user/payment")
    Call<List<Card>> getPaymentInfo(@Query("id") long id);

    @GET("/car")
    Call<List<Car>> getCar_list();

    @GET("/membership")
    Call<List<Card>> getMembership_list();

    @GET("/payment")
    Call<List<Card>> getPayment_list();

    @GET("/user/fee")
    Call<List<Fee>> getChargingFee(@Query("id") long id);

    @GET("/search/charging-station")
    Call<List<SearchResult>> searchChSt(@Query("query") String query);

    @POST("/user/review")
    Call<Void> updateReview(@Body Review review);

    @GET("/search/review")
    Call<List<Review>> getReviews(@Query("stat_id") String stat_id);

    @GET("/user/estimated-charges")
    Call<List<Fee>> getEstimatedFee(@Query("id") long id, @Query("date1") Date d1, @Query("date2") Date d2, @Query("kwh") int kwh);

    @GET("/search/destination")
    Call<String> getDestinationUser(@Query("stat_id") String stat_id);

    @POST("/destination")
    Call<Void> setDestination(@Query("stat_id") String stat_id, @Query("user_id") long user_id, @Query("distance") double distance, @Query("date") Date date);

    @GET("/search/person")
    Call<User> getPerson(@Query("car_number") String car_number);

    @POST("/user/saveToken")
    Call<Void> setUserToken(@Query("id") long id, @Query("token") String token);

    @DELETE("/user/deleteReview")
    Call<Void> deleteReview(@Query("id") long id, @Query("stat_id") String stat_id, @Query("review") String review);

    @GET("/user/getReview")
    Call<User> getUserReview(@Query("id") long id);

}
