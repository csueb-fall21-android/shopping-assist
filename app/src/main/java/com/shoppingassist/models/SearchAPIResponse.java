package com.shoppingassist.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchAPIResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("results")
    public List<ShoppingItem> results;
}
