package com.example.application1.Class;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.application1.Activity.ClickedImageActivity;
import com.example.application1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    private Context context;

    public TextView textViewImageGroupDate;
    public RecyclerView recyclerViewImageGroup;
    GridLayoutManager gridLayoutManager;

    private CollectionReference imagesThemselvesRef;

    public ImageViewHolder(@NonNull View itemView, Context context) {
        super(itemView);

        recyclerViewImageGroup = itemView.findViewById(R.id.recyclerViewImageGroup);
        textViewImageGroupDate = itemView.findViewById(R.id.textViewImageGroupDate);

        gridLayoutManager = new GridLayoutManager(context, 3);
        recyclerViewImageGroup.setLayoutManager(gridLayoutManager);

        imagesThemselvesRef = FirebaseFirestore.getInstance().collection("Images");

        this.context = context;
    }

    public void setDatestamp(String datestamp) {
        String ds = datestamp;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMM dd yyyy");
        try {
            Date formatted = simpleDateFormat.parse(ds);
            String date = simpleDateFormat1.format(formatted);
            textViewImageGroupDate.setText(date);

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        loadImagesInsideAGroup(datestamp);
    }

    public void loadImagesInsideAGroup(final String date_stamp) {
        Query query = imagesThemselvesRef.whereEqualTo("date_stamp", date_stamp);

        FirestoreRecyclerOptions<ImageItself> options = new FirestoreRecyclerOptions.Builder<ImageItself>().setQuery(query, ImageItself.class).build();
        FirestoreRecyclerAdapter<ImageItself, ImageItselfViewHolder> firestoreRecyclerAdapter =
                new FirestoreRecyclerAdapter<ImageItself, ImageItselfViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ImageItselfViewHolder holder, int i, @NonNull final ImageItself model) {
                        holder.setFull_name(model.getFull_name());
                        holder.setImage_url(model.getImage_url());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, ClickedImageActivity.class);
                                intent.putExtra("image", model);
                                context.startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ImageItselfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_images_layout, parent, false);
                        return new ImageItselfViewHolder(view, context);
                    }
                };
        recyclerViewImageGroup.setAdapter(firestoreRecyclerAdapter);
        firestoreRecyclerAdapter.startListening();
    }

}
