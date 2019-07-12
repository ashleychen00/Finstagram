package com.example.instagram_mainactivity;

import android.app.Application;

import com.example.instagram_mainactivity.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // register custom parseObjects
        ParseObject.registerSubclass(Post.class);

        // set up parse
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("fbu_instagram")
                .clientKey("apple_pie")
                .server("https://ashleychen00-fbu-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
