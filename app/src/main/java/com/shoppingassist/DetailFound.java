package com.shoppingassist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

public class DetailFound extends Activity {
    ImageView detailImage;
    public static final String TAG = "Details Found Page";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_found);
        detailImage = (ImageView) findViewById(R.id.detailImage);
        File photoFile = new File(getIntent().getStringExtra("photoFile"));

        Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        detailImage.setImageBitmap(takenImage);
    }
}
