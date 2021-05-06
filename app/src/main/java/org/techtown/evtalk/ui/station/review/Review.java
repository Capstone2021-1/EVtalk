package org.techtown.evtalk.ui.station.review;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Review {
    @SerializedName("user_id")
    private long user_id;    //사용자 id

    @SerializedName("name")
    private String name;    //사용자 이름

    @SerializedName("stat_id")
    private String stat_id; //충전소 id

    @SerializedName("review")
    private String review;  //리뷰

    @SerializedName("date")
    private Date date;  //날짜

    public long getUser_id() {
        return user_id;
    }

    public String getStat_id() {
        return stat_id;
    }

    public String getReview() {
        return review;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public void setStat_id(String stat_id) {
        this.stat_id = stat_id;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}