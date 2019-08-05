package com.example.application1;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LocationLogViewHolder extends RecyclerView.ViewHolder {
    public View mView;
    public TextView textViewLocationLatitude;
    public TextView textViewLocationLongtitude;
    public TextView textViewLocationUsername;
    public TextView textViewLocationDateAndTime;

    public LocationLogViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        textViewLocationLatitude = mView.findViewById(R.id.textViewLocationLatitude);
        textViewLocationLongtitude = mView.findViewById(R.id.textViewLocationLongtitude);
        textViewLocationUsername = mView.findViewById(R.id.textViewLocationUsername);
        textViewLocationDateAndTime = mView.findViewById(R.id.textViewLocationDateAndTime);
    }

    public void setLatitude(String latitude) {

    }

    public void setLongtitude(String longtitude) {

    }

    public void setEmail(String email) {

    }

    public void setDate(String date) {

    }
}
