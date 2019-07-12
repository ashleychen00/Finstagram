package com.example.instagram_mainactivity.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.instagram_mainactivity.R;
import com.example.instagram_mainactivity.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "Details ACTIVITY";
    ImageView ivProfile;
    ImageView ivPhoto;
    TextView tvUsername;
    TextView tvTime;
    TextView tvDescription;

    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ivProfile = findViewById(R.id.ivProfile);
        ivPhoto = findViewById(R.id.ivPhoto);
        tvUsername = findViewById(R.id.tvUsername);
        tvTime = findViewById(R.id.tvTime);
        tvDescription = findViewById(R.id.tvDescription);

        String post_id = getIntent().getStringExtra("post id");

        Post.Query postQuery = new Post.Query();
        postQuery.getPost(post_id).withUser();

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e != null || objects.size() > 1) {
                    Toast.makeText(DetailsActivity.this, "Finding post failed", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Finding post failed", e);
                }
                else {
                    // successful
                    // input everything
                    Log.d(TAG, "Success retrieving post");

                    post = objects.get(0);

                    tvUsername.setText(post.getUser().getUsername());
                    tvDescription.setText(post.getDescription());
                    // todo - have better time formatting (shows up in PostsFragment as well)
                    tvTime.setText(post.getCreatedAt().toString());

                    // images
                    Glide.with(DetailsActivity.this)
                            .load(post.getImage().getUrl())
                            .into(ivPhoto);

                    // check if profile photo exists before putting it in -- similar to in PostAdapter and ProfileFragment
                    String url;
                    if (post.getUser().getParseFile("profilePicture") == null) {
                        url = "";
                    }
                    else {
                        url = post.getUser().getParseFile("profilePicture").getUrl();
                    }

                    Glide.with(DetailsActivity.this)
                            .load(url)
                            .error(R.drawable.ic_log_out)
                            .into(ivProfile);
                }

            }
        });
    }
}
