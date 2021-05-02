package org.techtown.evtalk.user;

import com.google.gson.annotations.SerializedName;

public class Fee {
    @SerializedName("busiId")
    private String busiId;

    @SerializedName("fee")
    private float fee;

    public String getBusiId() {
        return busiId;
    }

    public float getFee() {
        return fee;
    }

    public void setBusiId(String busiId) {
        this.busiId = busiId;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }
}
