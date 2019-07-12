package com.example.instagram_mainactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram_mainactivity.model.Post;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private ArrayList<Post> posts;
    private Context context;

    public ProfileAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_profile_post, parent, false);
        ProfileViewHolder viewHolder = new ProfileViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Post post = posts.get(position);

        // image
        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivProfilePhoto);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfilePhoto;

        public ProfileViewHolder(View view) {
            super(view);
            ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto);

        }
    }
}
