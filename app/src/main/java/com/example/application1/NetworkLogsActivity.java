package com.example.application1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.application1.Fragment.FragmentNetworkLogs;

public class NetworkLogsActivity extends AppCompatActivity {
    private Toolbar toolbarNetworkLogs;
    private FrameLayout frameLayoutNetworkLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_logs);

        toolbarNetworkLogs = findViewById(R.id.toolbarNetworkLogs);
        frameLayoutNetworkLogs = findViewById(R.id.frameLayoutNetworkLogs);

        setSupportActionBar(toolbarNetworkLogs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Network Logs");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutNetworkLogs, new FragmentNetworkLogs());
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
