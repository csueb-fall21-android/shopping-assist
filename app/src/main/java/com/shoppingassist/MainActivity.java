package com.shoppingassist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shoppingassist.fragments.CameraFragment;
import com.shoppingassist.fragments.HomeFragment;
import com.shoppingassist.fragments.ProfileFragment;
import com.shoppingassist.interfaces.OnSavedItemDetailsListener;
import com.shoppingassist.interfaces.OnSavedListItemInteractionListener;
import com.shoppingassist.models.Item;
import com.shoppingassist.models.RecommendedItem;

import java.io.File;


public class MainActivity extends AppCompatActivity implements ProfileFragment.LogoutUserListener, CameraFragment.sendPictureListener, OnSavedListItemInteractionListener, OnSavedItemDetailsListener {
    public static final String TAG = "MainActivity";
    public static final String SELECTED_ITEM_ID_KEY = "selected_item";

    private static String HOME_TAG = "posts";
    private static String CAMERA_TAG = "compose";
    private static String PROFILE_TAG = "profile";
    CameraFragment cameraFragment;
    HomeFragment homeFragment;
    ProfileFragment profileFragment;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FragmentManager fm = getSupportFragmentManager();

        homeFragment = (HomeFragment) fm.findFragmentByTag(HOME_TAG);
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
        }

        cameraFragment = (CameraFragment) fm.findFragmentByTag(CAMERA_TAG);
        if (cameraFragment == null) {
            cameraFragment = CameraFragment.newInstance();
        }

        profileFragment = (ProfileFragment) fm.findFragmentByTag(PROFILE_TAG);
        if (profileFragment == null) {
            profileFragment = ProfileFragment.newInstance();
        }

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_home:
                    fragment = homeFragment;
                    break;
                case R.id.action_post:
                    fragment = cameraFragment;
                    break;
                case R.id.action_user:
                default:
                    fragment = profileFragment;
                    break;
            }
            fm.beginTransaction().replace(R.id.flContainer, fragment).commit();

            return true;
        });

        if (savedInstanceState != null) {
            int selected_bottom_item = savedInstanceState.getInt(SELECTED_ITEM_ID_KEY);
            bottomNavigationView.setSelectedItemId(selected_bottom_item);
        }
        else {
            bottomNavigationView.setSelectedItemId(R.id.action_home);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i("MainActivity", "Logging that it's saving to savedInstanceState");
        outState.putInt(SELECTED_ITEM_ID_KEY, bottomNavigationView.getSelectedItemId());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLogoutUser() {
        logoutCurrentUser();
    }

    private void logoutCurrentUser() {
        ParseUser.logOut();

        Toast.makeText(MainActivity.this, "Successfully logged out", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    private void onBeginSearch() {
        ParseQuery<Item> query = ParseQuery.getQuery("Item");
        query.whereEqualTo("objectId", "viU7NASqFU"); // placeholder for now, fetch the first item
        query.getFirstInBackground(new GetCallback<Item>() {
            public void done(Item item, ParseException e) {
                if (e == null) {
                    Log.i(TAG, "Fetched item from server");
                    startSearchActivity("kettle", item); // placeholder query for now
                } else {
                    Log.e(TAG, "Could not fetch item", e);
                }
            }
        });
    }

    /**
     * Used to begin a search on a specific query
     * By default, this is hooked up to the Placeholder Search API, and not the real Serp Search API
     */
    private void startSearchActivity(String query, Item item) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        intent.putExtra("query", query);
        intent.putExtra("item", item);

        MainActivity.this.startActivity(intent);
    }

    @Override
    public void imageSendDetailFound(File photoFile) {
        Intent i = new Intent(this, DetailFoundActivity.class);
        i.putExtra("photoFile", photoFile.toString());
        Log.i(TAG, "Picture set successfully.");
        startActivity(i);
    }

    @Override
    public void onClick(RecommendedItem item) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.getExternalLink())));
    }

    @Override
    public void onClick(Item item) {
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra("vDetails",item.getPictureFile());
        intent.putExtra("prodName",item.getName());
        intent.putExtra("priceText",item.getPrice());
        intent.putExtra("locText",item.getLocation());
        startActivity(intent);
    }
}