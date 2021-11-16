package com.shoppingassist;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Item.class);
        ParseObject.registerSubclass(RecommendedItem.class);
        ParseObject.registerSubclass(ItemRecommendedItem.class);
        ParseObject.registerSubclass(AppLocation.class);
        ParseObject.registerSubclass(Location.class);


        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See https://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        // set applicationId, and server server based on the values in the back4app settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_application_ID))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server))
                .build());
    }
}
