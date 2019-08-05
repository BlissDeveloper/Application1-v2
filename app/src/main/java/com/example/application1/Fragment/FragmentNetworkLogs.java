package com.example.application1.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.application1.NetworkLogsViewHolder;
import com.example.application1.Network_Log;
import com.example.application1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class FragmentNetworkLogs extends Fragment {
    private View mView;

    private CollectionReference networksRef;

    private RecyclerView recyclerViewNetworkLogs;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_fragment_network_logs, container, false);


        networksRef = FirebaseFirestore.getInstance().collection("User_Networks");

        recyclerViewNetworkLogs = mView.findViewById(R.id.recyclerViewNetworkLogs);
        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerViewNetworkLogs.setLayoutManager(linearLayoutManager);

        Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadNetworkLogs();
    }

    public void loadNetworkLogs() {
        Query query = networksRef.orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Network_Log>().setQuery(query, Network_Log.class).build();

        FirestoreRecyclerAdapter<Network_Log, NetworkLogsViewHolder> firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<Network_Log, NetworkLogsViewHolder>(firestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull NetworkLogsViewHolder holder, int i, @NonNull Network_Log model) {
                holder.setBssid(model.getBssid());
                holder.setCid(model.getCid());
                holder.setLac(model.getLac());
                holder.setUsername(model.getUsername());
                holder.setMnc(model.getMnc());
                holder.setMcc(model.getMcc());
                holder.setDateAndTime(model.getDate(), model.getTime());
            }

            @NonNull
            @Override
            public NetworkLogsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_network_logs_layout, parent, false);
                return new NetworkLogsViewHolder(view);
            }
        };
        recyclerViewNetworkLogs.setAdapter(firestoreRecyclerAdapter);
        firestoreRecyclerAdapter.startListening();
    }
}
