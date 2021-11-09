package com.shoppingassist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

public class DetailFound extends Activity {
    ImageView detailImage;
    public static final String TAG = "Details Found Page";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_found);
        detailImage = (ImageView) findViewById(R.id.camImage);
        Uri selectedImgUri = getIntent().getData();
        Log.d(TAG,"world");
        if (selectedImgUri != null) {
            Log.d(TAG, "" + selectedImgUri);
            String[] selectedImgPath = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImgUri, selectedImgPath, null, null, null);

            if(cursor != null && cursor.moveToFirst()) {
                int indexCol = cursor.getColumnIndex(selectedImgPath[0]);
                String imgPath = cursor.getString(indexCol);
                Log.d(TAG, "Path is - " + imgPath);
                cursor.close();
                Log.d(TAG, "my data is: " + imgPath);

                detailImage.setImageBitmap(BitmapFactory.decodeFile(imgPath));
            }
        }
        /**Bundle bundle = getIntent().getExtras();
        if(bundle !=null){
            int resId = bundle.getInt("resId");
            Log.i("DETAIL","my data is: "+resId);
            detailImage.setImageResource(resId);
        }**/
    }
}
