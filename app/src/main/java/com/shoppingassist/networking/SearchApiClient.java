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

public class SearchApiClient {
    private static final String PLACEHOLDER_API_KEY = "Bearer " + BuildConfig.SHEETSON_KEY;
    private static final String PLACEHOLDER_SHEET_NAME = "SerpShoppingResults";
    private static final String PLACEHOLDER_SHEET_ID = BuildConfig.SHEETSON_ID;
    private static final String PLACEHOLDER_BASE_URL = "https://api.sheetson.com";
    public static final String TAG = "SearchApiClient";

    private SearchService searchService;

    public SearchApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PLACEHOLDER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        searchService = retrofit.create(SearchService.class);
    }

    public void getPlaceholderSearchResults(final CallbackResponse<List<ShoppingItem>> searchListResponse) {
        Call<SearchAPIResponse> current = searchService.getPlaceholderShoppingSearchResults(PLACEHOLDER_SHEET_NAME, PLACEHOLDER_API_KEY, PLACEHOLDER_SHEET_ID);
        current.enqueue(new Callback<SearchAPIResponse>() {
            @Override
            public void onResponse(Call<SearchAPIResponse> call, Response<SearchAPIResponse> response) {
                Log.i(TAG, String.valueOf(response.body()));

                SearchAPIResponse model = response.body();
                if (response.isSuccessful() && model != null) {
                    Log.i(TAG, String.valueOf(model));
                    searchListResponse.onSuccess(model.results);
                } else {
                    searchListResponse.onFailure(new Throwable("error with response code " + response.code() + " " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<SearchAPIResponse> call, Throwable t) {
                searchListResponse.onFailure(t);
            }
        });
    }

    /*
        Sample usage of the Placeholder API
        This method should be placed where the Search API is called
     */
//    private void getShoppingResults() {
//        SearchApiClient searchApiClient = new SearchApiClient();
//        searchApiClient.getSearchResults(new CallbackResponse<List<ShoppingItem>>() {
//            @Override
//            public void onSuccess(List<ShoppingItem> models) {
//
//                for (ShoppingItem item : models) {
//                    Log.i(TAG, item.title + " " + item.price);
//                }
//                Log.d(TAG, "response successful");
//            }
//
//            @Override
//            public void onFailure(Throwable error) {
//                Log.e(TAG, error.getMessage());
//            }
//        });
//    }
}
