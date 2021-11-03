package com.shoppingassist.networking;

import com.shoppingassist.models.SearchAPIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface SearchService {

    @Headers({"Content-Type: application/json"})
    @GET("v2/sheets/{name}")
    Call<SearchAPIResponse> getShoppingSearchResults(@Path("name") String name, @Header("Authorization") String apiKey, @Header("X-Sheetson-Spreadsheet-Id") String sheetId);
}
