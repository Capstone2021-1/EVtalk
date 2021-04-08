package org.techtown.evtalk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("profile_image")
    private String profile_image;

    @SerializedName("message")
    private String message;

    @SerializedName("car_number")
    private String car_number;

    @SerializedName("membership")
    private List<String> membership;

    @SerializedName("payment")
    private List<String> payment;

    public User(long id, String name, String profile_image) {
        this.id = id;
        this.name = name;
        this.profile_image = profile_image;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getMessage() {
        return message;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public List<String> getMembership() {
        return membership;
    }

    public List<String> getPayment() {
        return payment;
    }
}
