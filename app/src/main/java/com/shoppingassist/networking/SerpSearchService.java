package com.shoppingassist.networking;

import com.shoppingassist.models.SearchAPIResponse;
import com.shoppingassist.models.SerpSearchAPIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SerpSearchService {

    @GET("search.json")
    Call<SerpSearchAPIResponse> getSerpShoppingSearchResults(
            @Query("q") String query,
            @Query("tbm") String type,
            @Query("location") String location,
            @Query("hl") String hl,
            @Query("gl") String gl,
            @Query("api_key") String apikey);
}
