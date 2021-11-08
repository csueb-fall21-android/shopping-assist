package com.shoppingassist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.shoppingassist.models.SerpSearchAPIResponse;
import com.shoppingassist.models.ShoppingItem;
import com.shoppingassist.networking.CallbackResponse;
import com.shoppingassist.networking.SearchApiClient;
import com.shoppingassist.networking.SerpSearchApiClient;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnListItemInteractionListener {
    public static final String TAG = "SearchActivity";
    public String query;
    // TODO: use the user's default location
    public String location = "Hayward, California, United States";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        query = getIntent().getStringExtra("query");

        ContentLoadingProgressBar progressBar = (ContentLoadingProgressBar) findViewById(R.id.progress);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvSearch);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updatePlaceholderAdapter(progressBar, recyclerView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Search Page");
    }

    /**
     * Used to access the Placeholder Search API, i.e. it returns the same static results
     * @param progressBar
     * @param recyclerView
     */
    private void updatePlaceholderAdapter(final ContentLoadingProgressBar progressBar, final RecyclerView recyclerView) {
        progressBar.show();
        SearchApiClient searchApiClient = new SearchApiClient();

        searchApiClient.getPlaceholderSearchResults(new CallbackResponse<List<ShoppingItem>>() {
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

    /**
     * The real Search API that searches based on the query provided to this activity and the current user's location
     * @param progressBar
     * @param recyclerView
     */
    private void updateAdapter(final ContentLoadingProgressBar progressBar, final RecyclerView recyclerView) {
        progressBar.show();
        SerpSearchApiClient serpSearchApiClient = new SerpSearchApiClient();

        serpSearchApiClient.getSerpSearchResults(new CallbackResponse<List<ShoppingItem>>() {
            @Override
            public void onSuccess(List<ShoppingItem> models) {
                progressBar.hide();
                for (ShoppingItem model : models) {
                    Log.d(TAG, model.title + ": " + model.price);
                }
                recyclerView.setAdapter(new ShoppingSearchItemsRecyclerViewAdapter(models, SearchActivity.this));
                Log.d(TAG, "response successful");
            }

            @Override
            public void onFailure(Throwable error) {
                progressBar.hide();
                Log.e(TAG, error.getMessage());
            }
        }, query, location);
    }

    @Override
    public void onLinkClick(ShoppingItem item) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.link)));
    }
}