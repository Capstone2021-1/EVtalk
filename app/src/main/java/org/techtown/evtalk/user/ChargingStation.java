package org.techtown.evtalk.user;

import com.google.gson.annotations.SerializedName;

public class ChargingStation {
    @SerializedName("id")
    private String id;

    @SerializedName("busiNm")
    private String busiNm;

    @SerializedName("name")
    private String name;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    @SerializedName("limitDetail")
    private String limitDetail;

    @SerializedName("note")
    private String note;

    @SerializedName("fee")
    private float fee;

    public void setId(String id) {
        this.id = id;
    }

    public void setBusiNm(String busiNm) {
        this.busiNm = busiNm;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public String getId() {
        return id;
    }

    public String getBusiNm() {
        return busiNm;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public float getFee() {
        return fee;
    }

    public String getLimitDetail() {
        return limitDetail;
    }

    public String getNote() {
        return note;
    }

    public void setLimitDetail(String limitDetail) {
        this.limitDetail = limitDetail;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
