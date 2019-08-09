package com.example.application1.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.application1.Activity.ClickedImageActivity;
import com.example.application1.Class.Image;
import com.example.application1.Class.ImageViewHolder;
import com.example.application1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentGallery extends Fragment {
    private FirebaseAuth mAuth;
    private String currentUserID;

    private CollectionReference imagesRef;
    private CollectionReference imagesThemselvesRef;

    private View mView;
    private RecyclerView recyclerViewGallery;
    private GridLayoutManager gridLayoutManager;

    public FragmentGallery() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_fragment_gallery, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        imagesRef = FirebaseFirestore.getInstance().collection("Uploaded_Images");
        imagesThemselvesRef = FirebaseFirestore.getInstance().collection("Images");

        recyclerViewGallery = mView.findViewById(R.id.recyclerViewGallery);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerViewGallery.setLayoutManager(linearLayoutManager);
        recyclerViewGallery.setHasFixedSize(true);

        //loadImages();
        load();

        return mView;
    }


    public void load() {
        Query query = imagesRef;

        FirestoreRecyclerOptions<Image> options = new FirestoreRecyclerOptions.Builder<Image>().setQuery(query, Image.class).build();
        FirestoreRecyclerAdapter<Image, ImageViewHolder> firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<Image, ImageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i, @NonNull Image image) {
                imageViewHolder.setDatestamp(image.getDate_stamp());
            }

            @NonNull
            @Override
            public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_grouped_images_layout, parent, false);
                return new ImageViewHolder(view, getActivity());
            }
        };
        recyclerViewGallery.setAdapter(firestoreRecyclerAdapter);
        firestoreRecyclerAdapter.startListening();
    }
}
