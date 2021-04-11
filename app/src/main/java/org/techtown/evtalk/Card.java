package org.techtown.evtalk;

import com.google.gson.annotations.SerializedName;

public class Card {
    @SerializedName("id")
    private int id;

    @SerializedName("card_name")
    private String name;

    @SerializedName("image")
    private String image;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
