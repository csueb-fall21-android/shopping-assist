package com.shoppingassist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shoppingassist.models.SerpSearchAPIResponse;
import com.shoppingassist.models.ShoppingItem;
import com.shoppingassist.networking.CallbackResponse;
import com.shoppingassist.networking.SearchApiClient;
import com.shoppingassist.networking.SerpSearchApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class SearchActivity extends AppCompatActivity implements OnListItemInteractionListener {
    public static final String TAG = "SearchActivity";
    public String query;
    public Item item;
    // TODO: use the user's default location
    public String location = "Hayward, California, United States";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        query = getIntent().getStringExtra("query");
        item = getIntent().getParcelableExtra("item");

        ContentLoadingProgressBar progressBar = (ContentLoadingProgressBar) findViewById(R.id.progress);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvSearch);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updatePlaceholderAdapter(progressBar, recyclerView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Search Page");
    }

    /**
     * Used to access the Placeholder Search API, i.e. it returns the same static results
     * @param progressBar
     * @param recyclerView
     */
    private void updatePlaceholderAdapter(final ContentLoadingProgressBar progressBar, final RecyclerView recyclerView) {
        progressBar.show();
        SearchApiClient searchApiClient = new SearchApiClient();

        searchApiClient.getPlaceholderSearchResults(new CallbackResponse<List<ShoppingItem>>() {
            @Override
            public void onSuccess(List<ShoppingItem> models) {
                progressBar.hide();
                recyclerView.setAdapter(new ShoppingSearchItemsRecyclerViewAdapter(models, SearchActivity.this));
                Log.d(TAG, "response successful");
            }

            @Override
            public void onFailure(Throwable error) {
                progressBar.hide();
                Log.e(TAG, error.getMessage());
            }
        });
    }

    /**
     * The real Search API that searches based on the query provided to this activity and the current user's location
     * @param progressBar
     * @param recyclerView
     */
    private void updateAdapter(final ContentLoadingProgressBar progressBar, final RecyclerView recyclerView) {
        progressBar.show();
        SerpSearchApiClient serpSearchApiClient = new SerpSearchApiClient();

        serpSearchApiClient.getSerpSearchResults(new CallbackResponse<List<ShoppingItem>>() {
            @Override
            public void onSuccess(List<ShoppingItem> models) {
                progressBar.hide();
                for (ShoppingItem model : models) {
                    Log.d(TAG, model.title + ": " + model.price);
                }
                recyclerView.setAdapter(new ShoppingSearchItemsRecyclerViewAdapter(models, SearchActivity.this));
                Log.d(TAG, "response successful");
            }

            @Override
            public void onFailure(Throwable error) {
                progressBar.hide();
                Log.e(TAG, error.getMessage());
            }
        }, query, location);
    }

    @Override
    public void onLinkClick(ShoppingItem item) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.link)));
    }

    @Override
    /**
     * Save recommended item to backend
     */
    public void onSaveClick(ShoppingItem item) {
        try {
            File pictureFile = getImageFileFromUrl(item.thumbnail);
            Log.d(TAG, "Picture file being sent");
            saveRecommendedItem(item.title, Float.valueOf(item.price), item.link, item.source, pictureFile);
        }
        catch (Exception e) {
            Log.e(TAG, "Error saving recommended item", e);
        }

    }

    private void saveRecommendedItem(String name, Number price, String link, String details, File pictureFile) {
        ParseFile parsePictureFile = new ParseFile(pictureFile);
        parsePictureFile.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (null == e) {
                    RecommendedItem recItem = new RecommendedItem();
                    recItem.setName(name);
                    recItem.setPrice(price);
                    recItem.setExternalLink(link);
                    recItem.setDetails(details);
                    recItem.setPictureFile(parsePictureFile);
                    recItem.setUser(ParseUser.getCurrentUser());

                    recItem.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Error while saving", e);
                                return;
                            }
                            Log.i(TAG, "Recommended item save was successful");

                            ItemRecommendedItem itemRecItem = new ItemRecommendedItem();
                            itemRecItem.setItem(item);
                            itemRecItem.setRecommendedItem(recItem);

                            itemRecItem.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.e(TAG, "Error while saving relationship", e);
                                        return;
                                    }
                                    Log.i(TAG, "Relationship between item and recommended item save was successful");
                                }
                            });
                        }
                    });
                }
                else {
                    Log.e(TAG, "Error while saving ParseFile", e);
                }
            }
        });
    }

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
                    Log.d(TAG, "Picture file saved");
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