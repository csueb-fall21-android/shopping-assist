package com.shoppingassist.networking;

import android.util.Log;

import com.shoppingassist.BuildConfig;
import com.shoppingassist.models.SearchAPIResponse;
import com.shoppingassist.models.SerpSearchAPIResponse;
import com.shoppingassist.models.ShoppingItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SerpSearchApiClient {

    private static final String SERP_API_KEY = BuildConfig.SERP_KEY;
    private static final String SERP_BASE_URL = "https://serpapi.com";
    public static final String TAG = "SerpSearchApiClient";

    private SerpSearchService serpSearchService;

    public SerpSearchApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serpSearchService = retrofit.create(SerpSearchService.class);
    }

    public void getSerpSearchResults(final CallbackResponse<List<ShoppingItem>> searchListResponse, String query, String location) {
        Call<SerpSearchAPIResponse> current = serpSearchService.getSerpShoppingSearchResults(query, "shop", location, "en", "us", SERP_API_KEY);
        current.enqueue(new Callback<SerpSearchAPIResponse>() {
            @Override
            public void onResponse(Call<SerpSearchAPIResponse> call, Response<SerpSearchAPIResponse> response) {
                Log.i(TAG, String.valueOf(response.body()));

                SerpSearchAPIResponse model = response.body();
                if (response.isSuccessful() && model != null) {
                    Log.i(TAG, String.valueOf(model));
                    searchListResponse.onSuccess(model.results);
                } else {
                    searchListResponse.onFailure(new Throwable("error with response code " + response.code() + " " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<SerpSearchAPIResponse> call, Throwable t) {
                searchListResponse.onFailure(t);
            }
        });
    }
}
