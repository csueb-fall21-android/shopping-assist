package com.shoppingassist.networking;

import com.shoppingassist.models.SearchAPIResponse;
import com.shoppingassist.models.SerpSearchAPIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SearchService {

    @Headers({"Content-Type: application/json"})
    @GET("v2/sheets/{name}")
    Call<SearchAPIResponse> getPlaceholderShoppingSearchResults(@Path("name") String name, @Header("Authorization") String apiKey, @Header("X-Sheetson-Spreadsheet-Id") String sheetId);
}
