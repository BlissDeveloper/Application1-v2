package com.example.application1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.charset.CoderMalfunctionError;

import javax.annotation.Nullable;

public class LoggedLocationMapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private CollectionReference locationsRef;

    private GoogleMap mMap;
    private Toolbar toolbarMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_location_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationsRef = FirebaseFirestore.getInstance().collection("User_Locations");

        toolbarMap = findViewById(R.id.toolbarMap);
        setSupportActionBar(toolbarMap);
        getSupportActionBar().setTitle("Logged Location Map");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadMarkers();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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

    public void loadMarkers() {
        Query query = locationsRef.whereEqualTo("is_latest", true);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty() && e == null) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (DocumentSnapshot d : queryDocumentSnapshots) {
                        double lat, longti;
                        lat = d.getDouble("latitude");
                        longti = d.getDouble("longtitude");
                        Log.d("Avery", String.valueOf(lat));
                        Log.d("Avery", String.valueOf(longti));
                        String email = d.getString("email");
                        Log.d("Avery", email);
                        LatLng latLng = new LatLng(lat, longti);
                        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(email));
                        marker.showInfoWindow();
                        builder.include(marker.getPosition());
                    }
                    LatLngBounds bounds = builder.build();

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 25, 25, 10);
                    mMap.animateCamera(cameraUpdate);
                }
            }
        });
    }
}
