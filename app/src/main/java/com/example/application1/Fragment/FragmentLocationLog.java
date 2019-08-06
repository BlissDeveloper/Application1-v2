package com.example.application1.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.application1.Location;
import com.example.application1.LocationLogViewHolder;
import com.example.application1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLocationLog extends Fragment {
    private CollectionReference locationLogsRef;

    private View mView;
    private RecyclerView recyclerViewLocationLog;
    private LinearLayoutManager linearLayoutManager;



    public FragmentLocationLog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_fragment_location_log, container, false);

        locationLogsRef = FirebaseFirestore.getInstance().collection("User_Locations");

        recyclerViewLocationLog = mView.findViewById(R.id.recyclerViewLocationLog);
        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerViewLocationLog.setLayoutManager(linearLayoutManager);

        loadLocationLogs();

        return mView;
    }

    public void loadLocationLogs() {
        Query query = locationLogsRef.orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Location> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Location>()
                .setQuery(query, Location.class).build();

        FirestoreRecyclerAdapter<Location, LocationLogViewHolder> firestoreRecyclerAdapter =
                new FirestoreRecyclerAdapter<Location, LocationLogViewHolder>(firestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull LocationLogViewHolder holder, int i, @NonNull Location model) {
                holder.setLongtitude(model.getLongtitude());
                holder.setLatitude(model.getLatitude());
                holder.setDate(model.getDate(), model.getTime());
                holder.setEmail(model.getEmail());
            }

            @NonNull
            @Override
            public LocationLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_location_logs_layout, parent, false);
                return new LocationLogViewHolder(view);
            }
        };
        recyclerViewLocationLog.setAdapter(firestoreRecyclerAdapter);
        firestoreRecyclerAdapter.startListening();

    }

}
