package com.shoppingassist.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShoppingItem {
    @SerializedName("position")
    public String position;

    @SerializedName("link")
    public String link;

    @SerializedName("source")
    public String source;

    @SerializedName("price")
    public String price;

    @SerializedName("title")
    public String title;

//    /* not currently used */
//    @SerializedName("thumbnail")
//    public String thumbnail;
}
