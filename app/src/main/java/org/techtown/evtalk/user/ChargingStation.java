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
    private float lat;

    @SerializedName("lng")
    private float lng;

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

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLng(float lng) {
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

    public float getLat() {
        return lat;
    }

    public float getLng() {
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
