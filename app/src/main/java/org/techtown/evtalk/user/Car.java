package org.techtown.evtalk.user;

import com.google.gson.annotations.SerializedName;

public class Car {
    @SerializedName("id")   //차량 Id
    private int id;

    @SerializedName("enterprise")   //차량 회사
    private String enterprise;

    @SerializedName("vehicle_type") //차량 이름(년식 + 이름)
    private String vehicle;

    @SerializedName("energy_capacity")  //에너지 용량
    private float energy_capacity;

    @SerializedName("distance") //주행가능 거리
    private int distance;

    @SerializedName("charging_type")    //충전타입
    private String charging_type;

    @SerializedName("image")    //이미지 주소
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
