package com.example.enzo.test;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fr.masterdapm.ancyen.model.Position;
import fr.masterdapm.ancyen.model.Ride;

public class MapCreate extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private Polyline polyline;
    private List<LatLng> latLngList = new ArrayList<>();
    private EditText editText;
    private Marker currentMarker;
    private PopupWindow popupWindow;

    String mail_user;
    Connection connection;
    Ride ride;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_create);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {

            mail_user = (String) bd.get("mail_user");
            connection = (Connection) bd.get("singleton");
            ride = (Ride) bd.get("ride");

        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setIndoorLevelPickerEnabled(false);

        polyline = mMap.addPolyline(new PolylineOptions());
        polyline.setColor(Color.parseColor("red"));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                showPopup(mMap.addMarker(new MarkerOptions().position(latLng)));
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latLngList.add(latLng);
                polyline.setPoints(latLngList);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                showPopup(marker);
                return false;
            }
        });
    }

    public void setTitle(View view){
        currentMarker.setTitle(editText.getText().toString());
        popupWindow.dismiss();
        currentMarker.showInfoWindow();
    }

    public void showPopup(Marker marker){
        currentMarker = marker;
        LinearLayout mainLayout = findViewById(R.id.general_create);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = null;
        if (inflater != null) {
            popupView = inflater.inflate(R.layout.popup, null);
        }
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        if (popupView != null) {
            editText = popupView.findViewById(R.id.editText);
            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    popupWindow.dismiss();
                    return false;
                }
            });
        }
    }

    public void created(View view) {

        Position[] positions = new Position[latLngList.size()];
        for (int i =0; i<latLngList.size();i++){
            positions[i] = new Position(latLngList.get(i).longitude, latLngList.get(i).latitude, 0);

        }


        ride.setPositions(positions);



        Intent intent = new Intent(MapCreate.this, AutorisedEmails.class);
        intent.putExtra("ride", ride);
        intent.putExtra("mail_user", mail_user);
        startActivity(intent);
    }
}
