package com.example.application1.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.application1.Class.DateUtils;
import com.example.application1.R;
import com.example.application1.Class.UsefulClass;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String currentUserID;
    private CollectionReference locationsRef;
    private CollectionReference networksRef;
    private StorageReference imagesRef;

    private CollectionReference usersRef;
    private CollectionReference uploadedImagesRef;
    private CollectionReference imagesThemselvesRef;

    private ImageView imageViewCamera;
    private ImageView imageViewChat;
    private ImageView imageViewSendLocation;
    private ImageView imageViewSendNetwork;
    private ImageView imageViewGallery;
    private ImageView imageViewMap;
    private ImageView imageViewNetworkLogs;
    private ImageView imageViewLocationLogs;

    private CardView cardViewCamera;

    private Toolbar toolbarHome;
    private ProgressDialog progressDialog;

    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;

    private final int CAMERA_REQUEST = 12;
    private final int IMAGE_CAPTURE_REQUEST = 1;
    private final int LOCATION_REQUEST = 2;
    private final int LOCATION_SETTINGS_REQUEST = 3;
    private final int WIFI_STATE_REQUEST = 4;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private FusedLocationProviderApi fusedLocationProviderApi;

    private LocationManager locationManager;

    //Misc
    private WifiManager wifiManager;
    private WifiInfo connection;
    private TelephonyManager telephonyManager;
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(HomeActivity.this);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        locationsRef = FirebaseFirestore.getInstance().collection("User_Locations");
        networksRef = FirebaseFirestore.getInstance().collection("User_Networks");
        imagesRef = FirebaseStorage.getInstance().getReference().child("Images");
        usersRef = FirebaseFirestore.getInstance().collection("Users");
        uploadedImagesRef = FirebaseFirestore.getInstance().collection("Uploaded_Images");
        imagesThemselvesRef = FirebaseFirestore.getInstance().collection("Images");

        toolbarHome = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbarHome);
        getSupportActionBar().setTitle("Home");

        progressDialog = new ProgressDialog(HomeActivity.this);

        imageViewCamera = findViewById(R.id.imageViewCamera);
        imageViewChat = findViewById(R.id.imageViewChat);
        imageViewSendLocation = findViewById(R.id.imageViewLocation);
        imageViewSendNetwork = findViewById(R.id.imageViewNetwork);
        imageViewGallery = findViewById(R.id.imageViewGallery);
        imageViewMap = findViewById(R.id.imageViewMap);
        imageViewNetworkLogs = findViewById(R.id.imageViewNetworkLogs);
        imageViewLocationLogs = findViewById(R.id.imageViewLocationLogs);

        cardViewCamera = findViewById(R.id.cardView);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        connection = wifiManager.getConnectionInfo();

        //Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(HomeActivity.this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        imageViewLocationLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, LocationLogActivity.class);
                startActivity(intent);

            }
        });

        imageViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, LoggedLocationMapsActivity.class);
                startActivity(intent);
            }
        });

        imageViewNetworkLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, NetworkLogsActivity.class);
                startActivity(intent);
            }
        });

        imageViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, GalleryActivity.class);
                startActivity(intent);
            }
        });

        imageViewSendNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_WIFI_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestWifiStatePermission();
                    Log.d("Avery", "Not yet");
                } else {
                    showNetworkDialog();
                    Log.e("Avery", "Permitted");
                }
            }
        });

        imageViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        imageViewSendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
                    //Not granted
                    requestLocation();
                } else {
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        showLocationDialog();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                        Dialog.OnClickListener clickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case Dialog.BUTTON_POSITIVE:
                                        //Send
                                        updateLocationStatus();
                                        break;
                                    case Dialog.BUTTON_NEGATIVE:
                                        dialogInterface.dismiss();
                                        break;
                                }
                            }
                        };
                        builder.setTitle("Send Location?")
                                .setPositiveButton("YES", clickListener)
                                .setNegativeButton("NO", clickListener)
                                .show();
                    }
                }
            }
        });

        cardViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCameraPermissionGranted()) {
                    openCamera();
                } else {
                    requestCameraPermission();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        imageFile = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                Dialog.OnClickListener clickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case Dialog.BUTTON_POSITIVE:
                                signOutUser();
                                break;
                            case Dialog.BUTTON_NEGATIVE:
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                };

                builder.setTitle("Do you want to sign out?")
                        .setPositiveButton("Yes", clickListener)
                        .setNegativeButton("No", clickListener)
                        .show();
                return true;
            case R.id.menu_phone:
                Intent intent = new Intent(HomeActivity.this, ARActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == IMAGE_CAPTURE_REQUEST && resultCode == RESULT_OK && data != null) {
            try {
                progressDialog.setTitle("Saving image...");
                progressDialog.show();
                Bundle extras = data.getExtras();
                Bitmap imageThumbnail = (Bitmap) extras.get("data");
                uploadImageToStorage(imageThumbnail);

            } catch (Exception e) {
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }


        } else if (resultCode == LOCATION_SETTINGS_REQUEST) {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.d("Avery", "GPS disabled");
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, LOCATION_SETTINGS_REQUEST);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                Dialog.OnClickListener clickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case Dialog.BUTTON_POSITIVE:
                                //Send
                                sendCurrentLocation();
                                break;
                            case Dialog.BUTTON_NEGATIVE:
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                };
                builder.setTitle("Send Location?")
                        .setPositiveButton("YES", clickListener)
                        .setNegativeButton("NO", clickListener)
                        .show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Granted
                    Log.d("Avery", "Result: " + grantResults[0]);
                } else {
                    Toast.makeText(this, "Camera permission is required to access this feature", Toast.LENGTH_SHORT).show();
                }
                break;
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Location permission is required to access this feature", Toast.LENGTH_SHORT).show();
                }
                break;
            case WIFI_STATE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Wifi information permission is required to access this feature.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void uploadImageToStorage(final Bitmap image) {
        UsefulClass usefulClass = new UsefulClass();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 50, baos);
        byte[] data = baos.toByteArray();
        String fileName = usefulClass.getUUID() + ".jpg";

        final StorageReference tempRef = imagesRef.child(fileName);
        tempRef.putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    tempRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            uploadImageToFirestore(imageURL);
                        }
                    });
                } else {
                    Toast.makeText(HomeActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    public void uploadImageToFirestore(final String uri) {
        DateUtils dateUtils = new DateUtils();
        final String dateStamp = dateUtils.getCurrentDateTimestamp();

        usersRef.document(currentUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        String first_name = documentSnapshot.get("first_name").toString();
                        String last_name = documentSnapshot.get("last_name").toString();
                        final String full_name = first_name + " " + last_name;

                        uploadedImagesRef.document(dateStamp).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    uploadImage(uri, full_name, dateStamp);
                                } else {
                                    Map<String, Object> dateStampMap = new ArrayMap<>();
                                    dateStampMap.put("date_stamp", dateStamp);
                                    uploadedImagesRef.document(dateStamp).set(dateStampMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                uploadImage(uri, full_name, dateStamp);
                                            } else {
                                                Toast.makeText(HomeActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(HomeActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void uploadImage(final String uri, final String full_name, final String dateStamp) {
        //Meron ng datestimp identifier
        Map<String, Object> imageMap = new ArrayMap<>();
        imageMap.put("image_url", uri);
        imageMap.put("full_name", full_name);
        imageMap.put("date_stamp", dateStamp);

        imagesThemselvesRef.document().set(imageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(HomeActivity.this, "Image successfully uploaded!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showNetworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        Dialog.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case Dialog.BUTTON_POSITIVE:
                        final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
                        progressDialog.setTitle("Sending network info...");
                        progressDialog.show();
                        //Yes
                        String BSSID = connection.getBSSID();
                        String networkOperator = telephonyManager.getNetworkOperator();

                        if (!TextUtils.isEmpty(networkOperator)) {

                            if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
                                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_WIFI_STATE) ==
                                        PackageManager.PERMISSION_GRANTED) {
                                    final GsmCellLocation location = (GsmCellLocation) telephonyManager.getCellLocation();
                                    if (location != null) {
                                        DateUtils dateUtils = new DateUtils();
                                        int mcc = Integer.parseInt(networkOperator.substring(0, 3));
                                        int mnc = Integer.parseInt(networkOperator.substring(3));
                                        int lac = location.getLac();
                                        int cid = location.getCid();

                                        Log.d("Avery", "BSSID: " + BSSID);
                                        Log.d("Avery", "MCC: " + mcc);
                                        Log.d("Avery", "MNC: " + mnc);
                                        Log.d("Avery", "LAC: " + location.getLac());
                                        Log.d("Avery", "CID: " + location.getCid());

                                        Map<String, Object> map = new ArrayMap<>();
                                        map.put("user_id", currentUserID);
                                        map.put("username", mAuth.getCurrentUser().getEmail());
                                        map.put("bssid", BSSID);
                                        map.put("mcc", mcc);
                                        map.put("mnc", mnc);
                                        map.put("lac", lac);
                                        map.put("cid", cid);
                                        map.put("date", dateUtils.getCurrentDate());
                                        map.put("time", dateUtils.getCurrentTime());
                                        map.put("timestamp", dateUtils.getCurrentTimestamp());

                                        networksRef.document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(HomeActivity.this, "Network information sent successfully!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Log.e("Avery", task.getException().getMessage());
                                                }
                                                progressDialog.dismiss();
                                            }
                                        });
                                    } else {
                                        progressDialog.dismiss();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                }
                            }

                        } else {
                            progressDialog.dismiss();
                        }
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        //No
                        dialogInterface.dismiss();
                        break;
                }
            }
        };
        builder.setTitle("Send Network Info?")
                .setPositiveButton("Yes", clickListener)
                .setNegativeButton("No", clickListener)
                .show();
    }

    //Class that handles location changes
    public void showLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setCancelable(false)
                .setTitle("Location Request")
                .setMessage("This feature uses location service to be used. Kindly activate it.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, LOCATION_SETTINGS_REQUEST);
                    }
                });


        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, LOCATION_SETTINGS_REQUEST);
    }

    public void updateLocationStatus() {
        progressDialog.setTitle("Sending location...");
        progressDialog.show();
        final Query query = locationsRef.whereEqualTo("user_id", currentUserID).whereEqualTo("is_latest", true);
        final List<String> idList = new ArrayList<>();

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot d : queryDocumentSnapshots) {
                        String id = d.getId();
                        idList.add(id);
                    }
                    for (String s : idList) {
                        Map<String, Object> map = new ArrayMap<>();
                        map.put("is_latest", false);

                        locationsRef.document(s).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    sendCurrentLocation();
                                } else {
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    }
                } else {
                    sendCurrentLocation();
                }
            }
        });
    }

    public void sendCurrentLocation() {
        //For progress

        try {
            if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLocationAvailability().addOnSuccessListener(new OnSuccessListener<LocationAvailability>() {
                    @Override
                    public void onSuccess(LocationAvailability locationAvailability) {
                        if (locationAvailability.isLocationAvailable()) {
                            Log.d("Avery", "Location avaibled");
                            try {
                                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            if (location != null) {
                                                insertLocationInformation(location.getLatitude(), location.getLongitude());
                                            } else {
                                                Toast.makeText(HomeActivity.this, "Fused location is null", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                Toast.makeText(HomeActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } else {
                            Log.e("Avery", "Location not yet avaiable");

                            locationRequest = LocationRequest.create();
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                            locationRequest.setInterval(120000);
                            locationRequest.setFastestInterval(30000);

                            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(HomeActivity.this, LOCATION_REQUEST, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, pendingIntent);
                        }
                    }
                });
            } else {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }


    }

    public void insertLocationInformation(double lat, double longti) {
        DateUtils dateUtils = new DateUtils();

        Map<String, Object> map = new ArrayMap<>();
        map.put("email", mAuth.getCurrentUser().getEmail());
        map.put("latitude", lat);
        map.put("longtitude", longti);
        map.put("date", getCurrentDate());
        map.put("time", getCurrentTime());
        map.put("user_id", currentUserID);
        map.put("timestamp", dateUtils.getCurrentTimestamp());
        map.put("is_latest", true);

        locationsRef.document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(HomeActivity.this, "Location sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    public boolean areLocationPermissionGranted() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public String getCurrentTimestamp() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        return ts;
    }

    public String getCurrentDate() {
        Date date = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(date);
        return formattedDate;
    }

    public String getCurrentTime() {
        Date date = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss aa");
        String formattedDate = df.format(date);
        return formattedDate;
    }

    public void requestWifiStatePermission() {
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, WIFI_STATE_REQUEST);
    }

    public void requestLocation() {
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            imageFile = null;

            try {
                imageFile = createImageFile();
                startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST);
            } catch (Exception e) {

            }


        }
    }

    private File createImageFile() throws IOException {
        UsefulClass usefulClass = new UsefulClass();
        String fileName = usefulClass.getUUID();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                fileName,
                ".jpg",
                storageDir
        );
        return image;
    }

    public void requestCameraPermission() {
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST);
    }

    public boolean isCameraPermissionGranted() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        } else {
            return true;
        }
    }

    public void signOutUser() {
        mAuth.signOut();
        goToLogin();
    }

    public void goToLogin() {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}
