package org.techtown.evtalk;

import com.google.gson.annotations.SerializedName;

public class Car {
    @SerializedName("id")
    private int id;

    @SerializedName("enterprise")
    private String enterprise;

    @SerializedName("vehicle_type")
    private String vehicle;

    @SerializedName("year")
    private int year;

    @SerializedName("energy_capacity")
    private float energy_capacity;

    @SerializedName("distance")
    private int distance;

    @SerializedName("charging_type")
    private String charging_type;

    @SerializedName("image")
    private String image;

    public void setId(int id) {
        this.id = id;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setEnergy_capacity(float energy_capacity) {
        this.energy_capacity = energy_capacity;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setCharging_type(String charging_type) {
        this.charging_type = charging_type;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getEnterprise() {
        return enterprise;
    }

    public String getVehicle() {
        return vehicle;
    }

    public int getYear() {
        return year;
    }

    public float getEnergy_capacity() {
        return energy_capacity;
    }

    public int getDistance() {
        return distance;
    }

    public String getCharging_type() {
        return charging_type;
    }

    public String getImage() {
        return image;
    }
}
