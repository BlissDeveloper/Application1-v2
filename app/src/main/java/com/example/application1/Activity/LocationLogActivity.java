package com.example.application1.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.application1.Fragment.FragmentLocationLog;
import com.example.application1.R;

public class LocationLogActivity extends AppCompatActivity {
    private Toolbar toolbarLocationLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_log);

        toolbarLocationLog = findViewById(R.id.toolbarLocationLog);
        setSupportActionBar(toolbarLocationLog);

        getSupportActionBar().setTitle("Location Logs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutLocationLog, new FragmentLocationLog());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:

                return false;
        }
    }
}
