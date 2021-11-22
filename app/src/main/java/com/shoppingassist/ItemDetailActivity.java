package com.shoppingassist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseFile;
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

        ImageView vDetails = findViewById(R.id.ivItemImage);
        TextView prodName = findViewById(R.id.tvItemName);
        TextView priceText = findViewById(R.id.tvItemPrice);
        TextView locText = findViewById(R.id.tvItemLocation);
        ImageButton ibItemLocation = findViewById(R.id.ibItemLocation);

        ParseFile image = item.getPictureFile();

        if (image != null) {
            Glide.with(context).load(image.getUrl()).into(vDetails);
        }

        prodName.setText(item.getName());
        priceText.setText(String.valueOf(item.getPrice()));
        locText.setText(item.getLocation().getDescriptor());

        FloatingActionButton fab = findViewById(R.id.fabSearch);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearchActivity(item.getName(), item);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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