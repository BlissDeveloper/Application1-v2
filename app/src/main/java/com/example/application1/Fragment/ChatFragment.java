package com.example.application1.Fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.application1.Class.Chat;
import com.example.application1.Class.ChatHolder;
import com.example.application1.Class.DateUtils;
import com.example.application1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private FirebaseAuth mAuth;
    private CollectionReference usersRef;
    private CollectionReference chatsRef;
    private String currentUserID;

    private View mView;
    private RecyclerView recyclerViewChat;
    private LinearLayoutManager linearLayoutManager;
    private EditText editTextChatMessage;
    private ImageButton imageButtonSend;

    private ImageView imageViewChatImage;
    private ImageButton imageButtonCloseImage;
    private ImageButton imageButtonSelectImage;

    private static final int PHOTO_PERMISSION_CODE = 9;
    private static final int PHOTO_CHOOSER_CODE = 5;

    private Uri uri;

    DateUtils dateUtils;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_chat, container, false);

        dateUtils = new DateUtils();

        mAuth = FirebaseAuth.getInstance();
        chatsRef = FirebaseFirestore.getInstance().collection("Chats");
        usersRef = FirebaseFirestore.getInstance().collection("Users");
        currentUserID = mAuth.getCurrentUser().getUid();

        linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerViewChat = mView.findViewById(R.id.recyclerViewChat);
        recyclerViewChat.setLayoutManager(linearLayoutManager);

        editTextChatMessage = mView.findViewById(R.id.editTextChatMessage);
        imageButtonSend = mView.findViewById(R.id.imageButtonSend);

        imageViewChatImage = mView.findViewById(R.id.imageViewChatImage);
        imageButtonCloseImage = mView.findViewById(R.id.imageButtonCloseImage);

        imageButtonSelectImage = mView.findViewById(R.id.imageButtonSelectImage);
        imageViewChatImage.setVisibility(View.GONE);
        imageButtonCloseImage.setVisibility(View.GONE);

        imageButtonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionGrantedForPhotos()) {
                    openGallery();
                } else {
                    requestPhotoAccess();
                }
            }
        });

        imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editTextChatMessage.getText().toString();
                editTextChatMessage.setText(null);
                sendMessage(message);
            }
        });

        editTextChatMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int len = charSequence.length();

                if (len == 0) {
                    imageButtonSend.setVisibility(View.GONE);
                } else {
                    imageButtonSend.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loadChats();

        return mView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PHOTO_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "Device storage access is required for this feature", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PHOTO_CHOOSER_CODE && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.getData();
            if (uri == null) {
                Log.e("Avery", "Uri empty");
            } else {
                Log.d("Avery", "Uri not empty");
                imageViewChatImage.setImageURI(uri);
            }
        } else {
            Log.e("Avery", "On activity error");
        }
    }

    public void loadChats() {
        Query query = chatsRef.orderBy("timestamp", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Chat> options = new FirestoreRecyclerOptions.Builder<Chat>().setQuery(query, Chat.class).build();
        final FirestoreRecyclerAdapter<Chat, ChatHolder> firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<Chat, ChatHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatHolder chatHolder, int i, @NonNull Chat chat) {
                chatHolder.setUser_id(chat.getUser_id(), chat);
            }

            @NonNull
            @Override
            public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_chat_layout, parent, false);
                return new ChatHolder(view);
            }
        };

        firestoreRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);

                int messageCount = firestoreRecyclerAdapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisiblePosition == -1 ||
                        (positionStart >= (messageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerViewChat.scrollToPosition(positionStart);
                }
            }
        });

        firestoreRecyclerAdapter.startListening();
        recyclerViewChat.setAdapter(firestoreRecyclerAdapter);


    }

    public void sendMessage(final String message) {
        usersRef.document(currentUserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String date = dateUtils.getCurrentDate();
                    String time = dateUtils.getCurrentTime();
                    String initials = documentSnapshot.get("name_initials").toString();

                    Map<String, Object> map = new ArrayMap<>();
                    map.put("user_id", currentUserID);
                    map.put("initials", initials);
                    map.put("message", message);
                    map.put("date", date);
                    map.put("time", time);
                    map.put("timestamp", dateUtils.getCurrentTimestamp());


                    chatsRef.document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Avery", "Chat sent");
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Avery", e.getMessage());
            }
        });
    }

    public void requestPhotoAccess() {
        // ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, WIFI_STATE_REQUEST);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PHOTO_PERMISSION_CODE);
    }

    public boolean isPermissionGrantedForPhotos() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PHOTO_CHOOSER_CODE);
    }

}
