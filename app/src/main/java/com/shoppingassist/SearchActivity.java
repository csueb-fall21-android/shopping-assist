package com.shoppingassist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.shoppingassist.models.ShoppingItem;
import com.shoppingassist.networking.CallbackResponse;
import com.shoppingassist.networking.SearchApiClient;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnListItemInteractionListener {
    public static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ContentLoadingProgressBar progressBar = (ContentLoadingProgressBar) findViewById(R.id.progress);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvSearch);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateAdapter(progressBar, recyclerView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Search Page");
    }

    private void updateAdapter(final ContentLoadingProgressBar progressBar, final RecyclerView recyclerView) {
        progressBar.show();
        SearchApiClient searchApiClient = new SearchApiClient();
        searchApiClient.getSearchResults(new CallbackResponse<List<ShoppingItem>>() {
            @Override
            public void onSuccess(List<ShoppingItem> models) {
                progressBar.hide();
                recyclerView.setAdapter(new ShoppingSearchItemsRecyclerViewAdapter(models, SearchActivity.this));
                Log.d(TAG, "response successful");
            }

            @Override
            public void onFailure(Throwable error) {
                progressBar.hide();
                Log.e(TAG, error.getMessage());
            }
        });

    }

    @Override
    public void onItemClick(ShoppingItem item) {

    }
}