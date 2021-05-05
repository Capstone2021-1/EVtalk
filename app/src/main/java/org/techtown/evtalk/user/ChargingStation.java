package org.techtown.evtalk.user;

import com.google.gson.annotations.SerializedName;

public class ChargingStation {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    @SerializedName("useTime")
    private String useTime;

    @SerializedName("park")
    private String park;

    @SerializedName("note")
    private String note;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public void setPark(String park) {
        this.park = park;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getUseTime() {
        return useTime;
    }

    public String getPark() {
        return park;
    }

    public String getNote() {
        return note;
    }

}
