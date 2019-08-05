package com.example.application1.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.application1.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private MapView mapView;
    private View mView;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);

        Bundle mapViewBundle = null;

        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = mView.findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);

        return mView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();

        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();

        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(14.5874845, 121.0598923)).title("AM").snippet("AM")).showInfoWindow();
    }
}
