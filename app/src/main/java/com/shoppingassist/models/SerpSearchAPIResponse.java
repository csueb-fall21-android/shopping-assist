package com.shoppingassist.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SerpSearchAPIResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("inline_shopping_results")
    public List<ShoppingItem> results;
}
