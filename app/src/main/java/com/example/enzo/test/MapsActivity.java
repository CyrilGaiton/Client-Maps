package com.example.enzo.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

import fr.masterdapm.ancyen.model.Ride;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    LocationSettingsRequest.Builder builder;
    SettingsClient client;
    Task<LocationSettingsResponse> task;

    private Intent intent;
    private String mail_user;
    private int idRide;

    private Polyline activeLine;
    private List <Marker> markers = new ArrayList<>();
    public static final int REQUEST_LOCATION = 199;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd!=null){
            mail_user = (String) bd.get("mail_user");
            idRide = (Integer) bd.get("idRide");
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        client = LocationServices.getSettingsClient(this);

        task = client.checkLocationSettings(builder.build());

        progressBar = findViewById(R.id.pb);

        intent = new Intent(MapsActivity.this, CameraPreview.class);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    double alt = location.getAltitude();
                    progressBar.setProgress((int) alt);
                    LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
                    new ASyncConnection().execute(pos);
                }
            }
        };


    }

    @Override
    public void onResume() {
        super.onResume();
//        startLocation();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setIndoorEnabled(false);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                startActivity(intent);
            }
        });

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocation();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statuscode = ((ApiException)e).getStatusCode();
                switch (statuscode){
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(MapsActivity.this,
                                    REQUEST_LOCATION);
                        }
                        catch (IntentSender.SendIntentException sendEx){
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_LOCATION){
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Toast.makeText(MapsActivity.this,
                            "Location enabled by user!", Toast.LENGTH_LONG).show();
                    startLocation();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(MapsActivity.this,
                            "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    }

    private void startLocation(){
        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }

        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public boolean onMyLocationButtonClick() {
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(MapsActivity.this,
                                new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng pos = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            List<LatLng> latLngs = new ArrayList<>();
                            latLngs.add(pos);
                            createMarkers(latLngs);
                            }
                    }
                });
                return false;
            }
        });
    }

    private void createMarkers(List<LatLng> latLngs){
        for (LatLng pos:latLngs) {
            TextView name = new TextView(MapsActivity.this);
            name.setText("Moi lol");
            IconGenerator test = new IconGenerator(MapsActivity.this);
            test.setColor(Color.parseColor("red"));
            test.setContentView(name);
            markers.add(mMap.addMarker(new MarkerOptions().position(pos)
                    .icon(BitmapDescriptorFactory.fromBitmap(test.makeIcon()))));
        }
    }

    private void replaceMarkers(List<LatLng> latLngs){
        for (int i = 0; i < latLngs.size() ; i++) {
            markers.get(i).setPosition(latLngs.get(i));
        }
    }

    private void displayPath(List<LatLng> latLngs){
        activeLine = mMap.addPolyline(new PolylineOptions());
        activeLine.setPoints(latLngs);
    }

    private void removePath(){
        activeLine.remove();
    }

    public void showAlt(View view) {
        Toast.makeText(MapsActivity.this, Integer.toString(progressBar.getProgress()), Toast.LENGTH_LONG).show();
    }



    private class ASyncConnection extends AsyncTask<LatLng, Void, Void> {


        @Override
        protected Void doInBackground(LatLng... pos) {

            Connection connection = Connection.getInstance();

            connection.oos.writeObject("updateStatistics");
            connection.oos.writeObject(mail_user);

            try {
                connection.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return rides;
        }



        @Override
        protected void onPostExecute(final List<Ride> rides) {
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

}

}
