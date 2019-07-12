package com.example.instagram_mainactivity.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Post") // important! must match class name in parse-dashboard exactly
public class Post extends ParseObject { // parseobject helps with manipulating data

    // mimics column names of the Post class in parse-dashboard (our database)
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_OBJECT_ID = "objectId";

    // the getters/setters with regards to the database (the "accessors")
    public String getDescription() {
        // getString is a ParseObject method, returns the value in column with this key
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description); // also from ParseObject, edits the value
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public static class Query extends ParseQuery<Post> {
        // making querying for posts easier

        public Query() {
            super(Post.class);
        }

        public Query getTop() { // "builder" method ?? builds itself as it goes
            // grab first 20 posts
            setLimit(20);
            return this;
        }

        public Query withUser() {
            // include user attached
            include(KEY_USER);
            return this;
        }

        public Query currentUser() {
            whereEqualTo(KEY_USER, ParseUser.getCurrentUser());
            return this;
        }

        public Query getPost(String id) {
            whereEqualTo(KEY_OBJECT_ID, id);
            return this;
        }

    }
}
