package com.shoppingassist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

//import android.location.Location;

import com.shoppingassist.models.Location;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Button saveButton;
    private String curLoc = "Testing";
    public String sendDescription;
    private boolean locExists =false, locationExists = false;

    private String objectId;
    //private boolean isQueryFinished = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        saveButton = findViewById(R.id.saveButton);

        getLocationPermission();
    }

    private void getDeviceLocation() { //Add functionality to save current location to database
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            android.location.Location currentLocation = (android.location.Location) task.getResult();

                            moveMapCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);

                            //Add functionality to create address here. Do function call
                            try {
                                getAddress(currentLocation);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "Unable to fetch current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }
    //Change function name back to moveCamera if any issues arise
    private void moveMapCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: Moving camera to : Lat: " + latLng.latitude + ", Lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    //Get address from latitude and longitude
    public void getAddress(android.location.Location currentLocation) throws IOException {
        Geocoder geocoder;

        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);

        String address = addresses.get(0).getAddressLine(0);

        //Toast.makeText(MapActivity.this, "Here is address!" + address, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Address here!" + address);

        ParseUser currentUser = ParseUser.getCurrentUser();
        Log.d(TAG, "Address here!" + String.valueOf(currentLocation.getLatitude()));

        queryUserLocation(currentUser);
        //queryLocations(); //Fix to take user into account

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //locExists = checkExistsAddress();
                //checkExistsAddress();
                Toast.makeText(MapActivity.this, "Location exists is so " + locExists, Toast.LENGTH_SHORT).show();
                //saveAddress(address, currentUser, currentLocation);
                savePtrAddress(address, currentUser, currentLocation);
            }
        });

        //saveAddress(address, currentUser, currentLocation);
        //return addresses;
    }

    private void queryUserLocation(ParseUser currentUser){ //check here if location pointer exists
        ParseObject locInfo = currentUser.getParseObject("location");
        //String curLocation = currentUser.getString("location");

        //Toast.makeText(MapActivity.this, "User location is " + curLocation, Toast.LENGTH_SHORT).show();

        if(locInfo == null){
            Toast.makeText(MapActivity.this, "Object is empty! ", Toast.LENGTH_SHORT).show();
        }
        else{
            locationExists = true;
            Toast.makeText(MapActivity.this, "Did not work", Toast.LENGTH_SHORT).show();
        }
    }

    private void queryLocations(){ //Checks if address exists and sets boolean to true to fill in text
        ParseQuery<Location> query = ParseQuery.getQuery(Location.class);
        query.include(Location.KEY_DESCRIPTOR);

        query.findInBackground(new FindCallback<Location>() {
            @Override
            public void done(List<Location> locations, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting Location!", e);
                    return;
                }
                for(Location location : locations){
                    if(location.getDescriptor().equals("My Location")){
                        curLoc = location.getAddress();
                        //Toast.makeText(MapActivity.this, "Address exists!!!", Toast.LENGTH_SHORT).show();
                        locExists = true;
                        //exists[0] = true;
                        Toast.makeText(MapActivity.this, "Address exists here!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //create new location here and then set it to currentUser as PTR reference
    private void savePtrAddress(String address, ParseUser currentUser, android.location.Location curCoordinates){
        String curLatitude = String.valueOf(curCoordinates.getLatitude());
        String curLongitude = String.valueOf(curCoordinates.getLongitude());
        curLoc = address;

        //boolean exists = checkExistsAddress();
        //Toast.makeText(MapActivity.this, "Boolean is " + exists, Toast.LENGTH_SHORT).show();

        //if(exists){
        if(!locationExists){
            Location curLocation = new Location();
            curLocation.setAddress(address);
            curLocation.setDescriptor("Default Location for " + currentUser.getUsername());
            curLocation.setCoordinates(curLatitude + "," + curLongitude);
            curLocation.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Error while saving", e);
                        Toast.makeText(MapActivity.this, "Error while saving default location!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.i(TAG, "Location save was successful!");
                    objectId = curLocation.getObjectId();
                    Toast.makeText(MapActivity.this, "ObjectID is " + objectId, Toast.LENGTH_SHORT).show();
                    currentUser.put("location", ParseObject.createWithoutData("Location", objectId));
                    currentUser.saveInBackground();

                    ParseObject locInfo = currentUser.getParseObject("location");

                    locInfo.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            String locIs = locInfo.getString("address");
                            //curLoc = address;
                            onFinishAddress(locIs);
                            Toast.makeText(MapActivity.this, "User address is here " + locIs, Toast.LENGTH_SHORT).show();
                        }
                    });
                    //curLoc = address;
                    //onFinishAddress(address);
                }
            });
        }
        else {
            ParseObject locInfo = currentUser.getParseObject("location");
            updateLocation(address, currentUser, locInfo);
            Toast.makeText(MapActivity.this, "Do nothing, address exists already", Toast.LENGTH_SHORT).show();
            //onFinishAddress(curLoc); //Change to get new address when figure out why exact location is not working
        }
    }

    private void updateLocation(String address, ParseUser currentUser, ParseObject locInfo){ //Update existing location with current default location for user
        locInfo.put("address", address);
        locInfo.saveInBackground();

        onFinishAddress(address);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
//        Toast.makeText(this, "Map is ready!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready!");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            //Toast.makeText(this, "Made it here!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Location permissions granted");
//            Toast.makeText(this, "Made it here!", Toast.LENGTH_SHORT).show();
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    private void initMap(){
        Log.d(TAG, "initMap: initialize map!");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: called!");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted!");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    public void onFinishAddress(String address){
        Intent data = new Intent();
        data.putExtra("location", address);
        setResult(RESULT_OK, data);
        finish();
    }
}