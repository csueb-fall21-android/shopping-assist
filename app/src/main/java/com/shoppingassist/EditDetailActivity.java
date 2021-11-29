package com.shoppingassist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;
import com.shoppingassist.models.Item;

import java.io.File;

public class EditDetailActivity extends AppCompatActivity {
    public static final String TAG = "EditDetailActivity";

    ImageView ivEditImage;
    EditText etEditName;
    EditText etEditPrice;
    ImageButton ibEditLocation;
    TextView tvEditLocation;
    Button btnEditSave;
    Button btnEditCancel;
    Item item;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail);

        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Item");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        ivEditImage = (ImageView) findViewById(R.id.ivEditImage);
        etEditName = (EditText) findViewById(R.id.etEditName);
        etEditPrice = (EditText) findViewById(R.id.etEditPrice);
        ibEditLocation = (ImageButton) findViewById(R.id.ibEditLocation);
        tvEditLocation = (TextView) findViewById(R.id.tvEditLocation);
        btnEditSave = (Button) findViewById(R.id.btnEditSave);
        btnEditCancel = (Button) findViewById(R.id.btnEditCancel);

        item = getIntent().getParcelableExtra("item");

        ParseFile image = item.getPictureFile();

        if (image != null) {
            Glide.with(context).load(image.getUrl()).into(ivEditImage);
        }

        etEditName.setText(item.getName());
        etEditPrice.setText(String.valueOf(item.getPrice()));
        tvEditLocation.setText(item.getLocation().getDescriptor());

        btnEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setName(etEditName.getText().toString());
                item.setPrice(Float.valueOf(etEditPrice.getText().toString()));

//                item.setLocation(locationRef);
//                item.setDetails("");
                item.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving item", e);
                            return;
                        }
                        Log.i(TAG, "Item saved successfully with image and location");
                        Toast.makeText(context, "Item saved successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}