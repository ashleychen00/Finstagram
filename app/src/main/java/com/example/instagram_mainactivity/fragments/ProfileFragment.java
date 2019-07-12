package com.example.instagram_mainactivity.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram_mainactivity.BitmapScaler;
import com.example.instagram_mainactivity.ProfileAdapter;
import com.example.instagram_mainactivity.R;
import com.example.instagram_mainactivity.activities.MainActivity;
import com.example.instagram_mainactivity.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends ComposeFragment {

    private static final String TAG = "Profile Fragment";

    // TODO - add functionality of this profile:
    //      -recyclerview (gridlayout this time)
    //          -- can add optionals if only want photo change but don't want to change adapter
    //          -- or keep posts and ugly or *****change adapter*******

    private TextView tvUsername;
    private ImageView ivPhoto;
    private Button btTakePhoto;
    private Button btSetPhoto;
    private Button btLogOut;

    private RecyclerView rvOwnPosts;
    private ProfileAdapter profileAdapter;
    private ArrayList<Post> posts;

    private ParseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // attach to xml layout views
        tvUsername = view.findViewById(R.id.tvUsername);
        ivPhoto = view.findViewById(R.id.ivNewProfile);
        btTakePhoto = view.findViewById(R.id.btTakePhoto);
        btSetPhoto = view.findViewById(R.id.btSetPhoto);
        btLogOut = view.findViewById(R.id.btLogOut);
        rvOwnPosts = view.findViewById(R.id.rvOwnPosts);

        posts = new ArrayList<>();
        profileAdapter = new ProfileAdapter(posts);
        rvOwnPosts.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rvOwnPosts.setAdapter(profileAdapter);

        // top of profile screen's functionality:
        user = ParseUser.getCurrentUser();
        tvUsername.setText(user.getUsername());

        setProfilePhoto();

        loadOwnPosts();

        btTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        btSetPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photoFile == null || ivPhoto.getDrawable() == null) {
                    Toast.makeText(getContext(),
                            "Please take a photo first!", Toast.LENGTH_LONG).show();
                    return;
                }
                uploadProfile(user, photoFile);
            }
        });

        btLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        ParseUser.logOut();
                        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                            // ^ important so next time doesn't automatically log in
                        Intent intent_logout = new Intent(getContext(), MainActivity.class);
                        startActivity(intent_logout);
                        getActivity().finish();
            }
        });
    }

    private void setProfilePhoto() {
        String url;
        if (user.getParseFile("profilePicture") == null) {
            url = "";
        }
        else {
            url = user.getParseFile("profilePicture").getUrl();
        }

        Glide.with(getContext())
                .load(url)
                .error(R.drawable.ic_log_out)
                .into(ivPhoto);
    }

    // todo (curious) - below, can't call super bc ivPhoto would be a null object reference. same issue with photoFile ?
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk

                // Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                // RESIZE BITMAP, see section below
                Uri takenPhotoUri = Uri.fromFile(getPhotoFileUri(photoFileName));
                // by this point we have the camera photo on disk
                Bitmap rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                // See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, 200);

                // Load the taken image into a preview
                ivPhoto.setImageBitmap(resizedBitmap);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loadOwnPosts() {

        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop()
                .withUser()
                .currentUser();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {

                    for (int i = 0; i < objects.size(); i++) {
                        // load into recyclerview
                        Post post = objects.get(i);
                        posts.add(0, post);
                        profileAdapter.notifyItemInserted(0);

                        Log.d(TAG, String.format("post %s has: \n description: %s \n username: %s",
                                i,
                                objects.get(i).getDescription(),
                                objects.get(i).getUser().getUsername()));
                    }

                    rvOwnPosts.scrollToPosition(0);
                }
                else {
                    Log.e(TAG, "Failed to get own posts", e);
                }
            }
        });
    }

    private void uploadProfile(ParseUser user, File photoFile) {
        user.put("profilePicture", new ParseFile(photoFile));
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getContext(), "Profile uploaded successfully!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Profile failed uploading :(", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "profile failed saving", e);
                }
            }
        });
    }
}
