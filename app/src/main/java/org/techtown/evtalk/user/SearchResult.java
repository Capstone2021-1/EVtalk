package org.techtown.evtalk.user;

import com.google.gson.annotations.SerializedName;

public class SearchResult {
    @SerializedName("iOt")
    private String iOt; //id(충전소) or title(충전소x)

    @SerializedName("nOa")
    private String nOa; //name(충전소) or address(충전소x)

    @SerializedName("lngOx")
    private double lngOx;   //lng(충전소) or mapx(충전소x)

    @SerializedName("latOy")
    private double latOy;   //lat(충전소) or mapy(충전소x)

    @SerializedName("isChSt")
    private int isChSt;     //충전소이면 1, 충전소가 아니면 0

    public String getiOt() {
        return iOt;
    }

    public String getnOa() {
        return nOa;
    }

    public double getLngOx() {
        return lngOx;
    }

    public double getLatOy() {
        return latOy;
    }

    public void setiOt(String iOt) {
        this.iOt = iOt;
    }

    public void setnOa(String nOa) {
        this.nOa = nOa;
    }

    public void setLngOx(double lngOx) {
        this.lngOx = lngOx;
    }

    public void setLatOy(double latOy) {
        this.latOy = latOy;
    }
}
