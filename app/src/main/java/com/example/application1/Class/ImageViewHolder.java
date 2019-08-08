package com.example.application1.Class;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.application1.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    private Context context;

    public TextView textViewImageGroupDate;
    public RecyclerView recyclerViewImageGroup;

    public ImageViewHolder(@NonNull View itemView, Context context) {
        super(itemView);

        recyclerViewImageGroup = itemView.findViewById(R.id.recyclerViewImageGroup);
        textViewImageGroupDate = itemView.findViewById(R.id.textViewImageGroupDate);
        this.context = context;
    }

    public void setArray_url(List<Map<String, Object>> array_url) {

    }

    public void setDatestamp(String datestamp) {

    }

    private static class ImageGroupViewHolder extends RecyclerView.ViewHolder {
        List<Map<String, Object>> mapList;
        ImageView imageViewGalleryImage;
        TextView textViewGalleryUsername;

        public ImageGroupViewHolder(@NonNull View itemView, List<Map<String, Object>> list) {
            super(itemView);

            mapList = list;
            imageViewGalleryImage = itemView.findViewById(R.id.imageViewGalleryImage);
            textViewGalleryUsername = itemView.findViewById(R.id.textViewGalleryUsername);
        }
    }
}
