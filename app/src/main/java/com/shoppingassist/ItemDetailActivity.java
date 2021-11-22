package com.shoppingassist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.shoppingassist.models.Item;

import java.util.List;

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

		/* Delete Item Functionality */
        Button deleteItemBtn = findViewById(R.id.deleteItemBtn);
        deleteItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ItemDetailActivity.this);
                alert.setTitle("Delete");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        queryDeleteItems();
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();

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

	protected void queryDeleteItems() {

        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("objectId", item.getObjectId());
        query.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> objects, ParseException e) {
                if (e == null) {
                    // calling a delete method to delete this item.
                    objects.get(0).deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            // inside done method checking if the error is null or not.
                            if (e == null) {
                                Toast.makeText(ItemDetailActivity.this, "Item Deleted..", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ItemDetailActivity.this, MainActivity.class);
                                startActivity(i);
                            } else {
                                // if we get error we are displaying it in below line.
                                Toast.makeText(ItemDetailActivity.this, "Fail to delete course..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(ItemDetailActivity.this, "Fail to get the object..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}