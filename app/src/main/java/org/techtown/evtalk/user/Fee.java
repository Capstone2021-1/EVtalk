package org.techtown.evtalk.user;

import com.google.gson.annotations.SerializedName;

public class Fee {
    @SerializedName("busiId")   //회사 Id
    private String busiId;

    @SerializedName("fee")      //1kwh당 요금  or 충전시간 설정 시 예상 요금
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
