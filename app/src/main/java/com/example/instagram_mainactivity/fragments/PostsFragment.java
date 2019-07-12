package com.example.instagram_mainactivity.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.instagram_mainactivity.PostAdapter;
import com.example.instagram_mainactivity.R;
import com.example.instagram_mainactivity.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {

    private static final String TAG = "posts fragment";

    // recyclerview fields
    PostAdapter postAdapter;
    ArrayList<Post> posts;
    RecyclerView rvPosts;

    SwipeRefreshLayout swipeContainer;

    // onCreateView - first lifecycle method called to inflate the views
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvPosts = view.findViewById(R.id.rvPosts);

        posts = new ArrayList<>();
        postAdapter = new PostAdapter(posts);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPosts.setAdapter(postAdapter);

        loadTopPosts();

        // Lookup the swipe container view
        swipeContainer = view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                loadTopPosts();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private void loadTopPosts() {

        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop()
                .withUser();

        processPosts(postsQuery);
    }

    void clear() {
        posts.clear();
        postAdapter.notifyDataSetChanged();
    }

    protected void processPosts(Post.Query postsQuery) {
        // repeated in ProfileFragment, try to remove a duplicate somehow? todo - move to Post model ?

        // grab all posts in our post class, in background
        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "post query succeeded!");
                    clear();

                    for (int i = 0; i < objects.size(); i++) {
                        // load into recyclerview
                        Post post = objects.get(i);
                        posts.add(0, post);
                        postAdapter.notifyItemInserted(0);

                        Log.d(TAG, String.format("post %s has: \n description: %s \n username: %s",
                                i,
                                objects.get(i).getDescription(),
                                objects.get(i).getUser().getUsername()));
                    }

                    rvPosts.scrollToPosition(0);
                    swipeContainer.setRefreshing(false);

                }
                else {
                    Log.e(TAG, "getquery for posts failed", e);
                }
            }
        });
    }
}
