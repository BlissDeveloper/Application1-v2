package com.example.application1.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.application1.Class.Image;
import com.example.application1.Class.ImageItself;
import com.example.application1.R;

public class ClickedImageActivity extends AppCompatActivity {
    private ImageItself image;

    private ImageView imageViewClickedImage;
    private Toolbar toolbarClickedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_image);

        imageViewClickedImage = findViewById(R.id.imageViewClickedImage);
        toolbarClickedImage = findViewById(R.id.toolbarClickedImage);

        setSupportActionBar(toolbarClickedImage);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("image")) {
            image = (ImageItself) getIntent().getSerializableExtra("image");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadImage();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }

    public void loadImage() {
        // String image_url = image.getImage_url();
        //        //Glide.with(ClickedImageActivity.this).load(image_url).into(imageViewClickedImage);
        String image_url = image.getImage_url();
        Glide.with(ClickedImageActivity.this).load(image_url).into(imageViewClickedImage);
    }
}
