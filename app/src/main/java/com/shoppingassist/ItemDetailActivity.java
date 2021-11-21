package com.shoppingassist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.shoppingassist.models.Item;

public class ItemDetailActivity extends AppCompatActivity {
    private static final String TAG = "ItemDetailActivity";
    private Context context;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        item = getIntent().getParcelableExtra("item");

        ImageView vDetails = findViewById(R.id.vDetails);
        TextView prodName = findViewById(R.id.prodName);
        TextView priceText = findViewById(R.id.priceText);
        TextView locText = findViewById(R.id.locText);

        ParseFile image = item.getPictureFile();

        if (image != null) {
            Glide.with(context).load(image.getUrl()).into(vDetails);
        }

        prodName.setText(item.getName());
        priceText.setText(String.valueOf(item.getPrice()));
        locText.setText(item.getLocation());

        FloatingActionButton fab = findViewById(R.id.fabSearch);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginSearch();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    private void beginSearch() {
        startSearchActivity(item.getName(), item);
    }

    /**
     * Used to begin a search on a specific query
     * By default, this is hooked up to the Placeholder Search API, and not the real Serp Search API
     */
    private void startSearchActivity(String query, Item item) {
        Intent intent = new Intent(ItemDetailActivity.this, SearchActivity.class);
        intent.putExtra("query", query);
        intent.putExtra("item", item);

        ItemDetailActivity.this.startActivity(intent);
    }
}