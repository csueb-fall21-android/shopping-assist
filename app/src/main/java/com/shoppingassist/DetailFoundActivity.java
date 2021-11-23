package com.shoppingassist;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shoppingassist.models.Item;
import com.shoppingassist.models.RecommendedItem;
import com.shoppingassist.models.ShoppingItem;
import com.shoppingassist.networking.CallbackResponse;
import com.shoppingassist.networking.SearchApiClient;
import com.shoppingassist.networking.SerpSearchApiClient;


public class DetailFoundActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{
    Context context;
    Item item;
    ImageView ivCapturedImage;
    Button saveButton, moreDetailsButton, retakeButton;
    TextView longitude_textview,latitude_textview;
    TextView detailsFoundTextHeader;
    ImageView ivFoundImage;
    TextView tvFoundName;
    TextView tvFoundPrice;
    ImageButton ibFoundLink;

    ShoppingItem searchResult;
    public static final String TAG = "DetailsFoundActivity";
    public String placeholderLocation = "Hayward, California, United States";
    File photoFile;
    public GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    Location loc;
    double latitude;
    double longitude;

    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private int NUM_UPDATES = 1;
    private long EXPIRATION_DURATION = 10000; /* 10s */
    private LocationManager locationManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_found);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Searching for item...");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        /** For Captured Image **/
        ivCapturedImage = (ImageView) findViewById(R.id.ivCapturedImage);
        photoFile = new File(getIntent().getStringExtra("photoFile"));
        Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        ivCapturedImage.setImageBitmap(takenImage);

        /** For Current Location of Device **/
        mLatitudeTextView = (TextView) findViewById((R.id.latitude_textview));
        mLongitudeTextView = (TextView) findViewById((R.id.longitude_textview));
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        checkLocation();

        /** For Retake Picture Functionality **/
        retakeButton = findViewById(R.id.retakeButton);
        retakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /* Item Save */
        detailsFoundTextHeader = findViewById(R.id.detailsFoundTextHeader);
        tvFoundName = findViewById(R.id.tvFoundName);
        tvFoundPrice = findViewById(R.id.tvFoundPrice);
        ivFoundImage = findViewById(R.id.ivFoundImage);
        ibFoundLink = findViewById(R.id.ibFoundLink);
        longitude_textview = findViewById(R.id.longitude_textview);
        latitude_textview = findViewById(R.id.latitude_textview);
        moreDetailsButton = findViewById(R.id.moreDetailsButton);
        saveButton = findViewById(R.id.btnDetailsSave);

        context = this;

        ibFoundLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchResult != null) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(searchResult.link)));
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick saveButton");
                try {
                    File pictureFile = getImageFileFromUrl(searchResult.thumbnail);
                    Log.d(TAG, "Picture file being sent");
                    saveItem("view", searchResult.title, Float.valueOf(searchResult.price), searchResult.link, pictureFile);
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, "Error saving item: ", e);
                }
            }
        });

        moreDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick moreDetailsButton");
                try {
                    saveItem("edit", "", 0, "", photoFile);
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, "Error saving item: ", e);
                }
            }
        });

        placeholderSearchForItem("kettle", placeholderLocation);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startLocationUpdates();
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {
            double latitude = mLocation.getLatitude();
            double longitude = mLocation.getLongitude();
            mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Cannot detect location", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Cannot detect location");
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {
        final int LOCATION_PERMISSION_REQUEST_CODE = 100;

        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(NUM_UPDATES)
//                .setInterval(UPDATE_INTERVAL)
//                .setFastestInterval(FASTEST_INTERVAL)
                .setExpirationDuration(EXPIRATION_DURATION);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener < Location > () { // Method });
                FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                ActivityCompat.requestPermissions(this,  new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,  new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        Log.d("reque", "--->>>>");
    }
    @Override
    public void onLocationChanged(Location location) {
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
        mLongitudeTextView.setText(String.valueOf(location.getLongitude() ));
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.i(TAG, msg);
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }
    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }
    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }
    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    public double getLongitude() {
        if (loc != null) {
            longitude = loc.getLongitude();
        }
        return longitude;
    }

    public double getLatitude() {
        if (loc != null) {
            latitude = loc.getLatitude();
        }
        return latitude;
    }

    private void saveItem(String action, String name, Number price, String link, File pictureFile) throws ExecutionException, InterruptedException {
        String longitude_textviewVal = longitude_textview.getText().toString();
        String latitude_textviewVal = latitude_textview.getText().toString();
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(latitude_textviewVal);
        joiner.add(longitude_textviewVal);
        String locationVal = joiner.toString();

        ParseFile parsePictureFile = new ParseFile(pictureFile);
        parsePictureFile.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving ParseFile", e);
                    return;
                }
                Log.i(TAG, "Image save was successful");
            }
        });

        com.shoppingassist.models.Location locationRef = new com.shoppingassist.models.Location();

        locationRef.setDescriptor(locationVal);
        locationRef.setCoordinates(locationVal);

        locationRef.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving location", e);
                    return;
                }
                Log.i(TAG, "Location save was successful");
            }
        });

        item = new Item();

        if (!name.equals("")) {
            item.setName(name);
        }
        else {
            item.setName("Product Name");
        }

        item.setPrice(price);
        item.setLocation(locationRef);
        item.setDetails("");
        item.setExternalLink(link);
        item.setPictureFile(parsePictureFile);
        item.setUser(ParseUser.getCurrentUser());

        item.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving item", e);
                    return;
                }
                Log.i(TAG, "Item saved successfully with image and location");
                Toast.makeText(context, "Item saved successfully", Toast.LENGTH_SHORT).show();

                Intent i = new Intent();
                i.putExtra("item", item);
                i.putExtra("action", action);

                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    private void updateDetails(ShoppingItem shoppingItem) {
        searchResult = shoppingItem;
        if (searchResult.thumbnail != null) {
            Glide.with(context).load(searchResult.thumbnail).into(ivFoundImage);
        }
        tvFoundName.setText(searchResult.title);
        tvFoundPrice.setText(searchResult.price);
    }

    private void hideDetails() {
        tvFoundName.setVisibility(View.GONE);
        tvFoundPrice.setVisibility(View.GONE);
        detailsFoundTextHeader.setText("No Item Found");
        saveButton.setVisibility(View.GONE);
        ibFoundLink.setVisibility(View.GONE);
    }

    private void placeholderSearchForItem(String query, String location) {
        SearchApiClient searchApiClient = new SearchApiClient();

        searchApiClient.getPlaceholderSearchResults(new CallbackResponse<List<ShoppingItem>>() {
            @Override
            public void onSuccess(List<ShoppingItem> models) {
                Log.i(TAG, "Placeholder response successful");
                for (ShoppingItem model : models) {
                    Log.i(TAG, model.title + ": " + model.price);
                }

                if (models.size() != 0) {
                    Log.i(TAG, "At least one item found! Selecting the first...");
                    updateDetails(models.get(0));
                }
                else {
                    Log.i(TAG, "No suggestions found.");
                    hideDetails();
                }
            }

            @Override
            public void onFailure(Throwable error) {
                Log.e(TAG, error.getMessage());
            }
        });

    }

    /**
     * The real Search API that searches based on the query provided to this activity and the current user's location

     */
    private void searchForItem(String query, String location) {
        SerpSearchApiClient serpSearchApiClient = new SerpSearchApiClient();

        serpSearchApiClient.getSerpSearchResults(new CallbackResponse<List<ShoppingItem>>() {
            @Override
            public void onSuccess(List<ShoppingItem> models) {
                Log.i(TAG, "Serp API response successful");

                for (ShoppingItem model : models) {
                    Log.i(TAG, model.title + ": " + model.price);
                }

                if (models.size() != 0) {
                    Log.i(TAG, "At least one item found! Selecting the first...");
                    updateDetails(models.get(0));
                }
                else {
                    Log.i(TAG, "No suggestions found.");
                    hideDetails();
                }
            }

            @Override
            public void onFailure(Throwable error) {
                Log.e(TAG, error.getMessage());
            }
        }, query, location);
    }

    /**
     * Helper function to convert url images into bitmap images
     * @param imageUrl
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private File getImageFileFromUrl(String imageUrl) throws ExecutionException, InterruptedException {
        Context context = this;

        ExecutorService executor = Executors.newSingleThreadExecutor();

        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "Failed to create Directory");
        }
        File imageFile = new File(mediaStorageDir.getPath() + File.separator + "recommendedItem");

        FutureTask<File> future = new FutureTask<File>(new Callable<File>() {
            public File call() {
                try
                {
                    URL url = new URL(imageUrl);
                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    FileOutputStream out = new FileOutputStream(imageFile);
                    image.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    Log.i(TAG, "Picture file saved");
                }
                catch (IOException e) {
                    Log.e(TAG, "Error saving file from url", e);
                }
                return imageFile;
            }});

        executor.execute(future);

        File returnFile =  future.get();
        Log.d(TAG, "Waited to return image file");

        return returnFile;
    }

}
