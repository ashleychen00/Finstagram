package com.example.instagram_mainactivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram_mainactivity.activities.DetailsActivity;
import com.example.instagram_mainactivity.model.Post;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<Post> posts;
    private Context context;

    public PostAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post, parent, false);
        PostViewHolder viewHolder = new PostViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.tvUsername.setText(post.getUser().getUsername());
        holder.tvCreatedAt.setText(post.getCreatedAt().toString());
        holder.tvDescription.setText(post.getDescription());

        // image
        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivPhoto);

        String url;
        if (post.getUser().getParseFile("profilePicture") == null) {
            url = "";
        } else {
            url = post.getUser().getParseFile("profilePicture").getUrl();
        }

        Glide.with(context)
                .load(url)
                .error(R.drawable.ic_log_out)
                .bitmapTransform(new RoundedCornersTransformation(context, 5, 0))
                .into(holder.ivProfile);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvUsername;
        TextView tvCreatedAt;
        TextView tvDescription;
        ImageView ivPhoto;
        ImageView ivProfile;

        public PostViewHolder(View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivPhoto = itemView.findViewById(R.id.ivProfilePhoto);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) { // no_position = -1, but better not to hard code
                // means position is valid
                Post post = posts.get(position);
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("post id", post.getObjectId());
                context.startActivity(intent);
            }
        }
    }


}
