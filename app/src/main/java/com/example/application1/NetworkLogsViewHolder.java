package com.example.application1;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NetworkLogsViewHolder extends RecyclerView.ViewHolder {
    public View mView;

    public TextView textViewUsername;
    public TextView textViewBSSID;
    public TextView textViewMCC;
    public TextView textViewMNC;
    public TextView textViewLAC;
    public TextView textViewCID;
    public TextView textViewDateAndTime;

    public NetworkLogsViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        textViewUsername = mView.findViewById(R.id.textViewUsername);
        textViewBSSID = mView.findViewById(R.id.textViewBSSID);
        textViewMCC = mView.findViewById(R.id.textViewMCC);
        textViewMNC = mView.findViewById(R.id.textViewMNC);
        textViewLAC = mView.findViewById(R.id.textViewLAC);
        textViewCID = mView.findViewById(R.id.textViewCID);
        textViewDateAndTime = mView.findViewById(R.id.textViewDateAndTime);


    }

    public void setBssid(String bssid) {
        textViewBSSID.setText("BSSID: " + bssid);
    }

    public void setUsername(String username) {
        textViewUsername.setText(username);
    }

    public void setCid(int cid) {
        textViewCID.setText("CID: " + String.valueOf(cid));
    }

    public void setLac(int lac) {
        textViewLAC.setText("LAC: " + String.valueOf(lac));
    }

    public void setMcc(int mcc) {
        textViewMCC.setText("MCC: " + String.valueOf(mcc));
    }

    public void setMnc(int mnc) {
        textViewMNC.setText("MNC: " + String.valueOf(mnc));
    }

    public void setDateAndTime(String date, String time) {
        textViewDateAndTime.setText("Posted on: " + date + " on " + time);
    }
}
