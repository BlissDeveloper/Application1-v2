package com.example.application1.Class;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.application1.R;

public class ImageItselfViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewGalleryImage;
    public TextView textViewGalleryUsername;

    Context mContext;

    public ImageItselfViewHolder(@NonNull View itemView, Context context) {
        super(itemView);

        mContext = context;

        imageViewGalleryImage = itemView.findViewById(R.id.imageViewGalleryImage);
        textViewGalleryUsername = itemView.findViewById(R.id.textViewGalleryUsername);
    }

    public void setFull_name(String full_name) {
        textViewGalleryUsername.setText(full_name);
    }

    public void setImage_url(String image_url) {
        Glide.with(mContext).load(image_url).
                diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViewGalleryImage);
    }

}
