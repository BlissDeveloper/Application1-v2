package com.example.application1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.application1.Fragment.FragmentLocationLog;

public class LocationLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_log);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutLocationLog, new FragmentLocationLog());
        fragmentTransaction.commit();
    }
}
