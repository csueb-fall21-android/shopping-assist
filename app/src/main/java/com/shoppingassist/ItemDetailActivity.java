package com.shoppingassist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

public class ItemDetailActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        Bundle extras = getIntent().getExtras();
        ParseFile imgFile = (ParseFile)extras.get("vDetails");

        String prodNameVal = extras.getString("prodName");
        Number priceTextVal = extras.getInt("priceText");
        String locTextVal = extras.getString("locText");

        ImageView vDetails = findViewById(R.id.vDetails);
        TextView prodName = findViewById(R.id.prodName);
        TextView priceText = findViewById(R.id.priceText);
        TextView locText = findViewById(R.id.locText);

        imgFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    // Decode the Byte[] into Bitmap
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    // Set the Bitmap into the ImageView
                    vDetails.setImageBitmap(bmp);
                } else {
                    Log.d("test","Problem load image the data.");
                }
            }
        });
        prodName.setText(prodNameVal);
        priceText.setText(String.valueOf(priceTextVal));
        locText.setText(locTextVal);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}