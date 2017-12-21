package com.example.enzo.test;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.IOException;

import fr.masterdapm.ancyen.model.Ride;

/**
 * Created by enzo on 15/12/17.
 */

public class Creer extends Activity {

    String mail_user;
    TimePicker simpleTimePicker;
    DatePicker simpleDatePicker;
    EditText departurePlace;
    EditText arrivalPlace;
    EditText duree;
    EditText distance;

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer);

        departurePlace = (EditText) findViewById(R.id.e_depart);
        arrivalPlace = (EditText) findViewById(R.id.e_arrivee);
        duree = (EditText) findViewById(R.id.duree);
        distance = (EditText) findViewById(R.id.distance);

        simpleTimePicker = (TimePicker) findViewById(R.id.simpleTimePicker);
        simpleTimePicker.setIs24HourView(true);

        simpleDatePicker = (DatePicker) findViewById(R.id.simpleDatePicker);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {

            mail_user = (String) bd.get("mail_user");




        }


    }


    public void Enregistrer(View v){


        int hour = simpleTimePicker.getCurrentHour();
        int minute = simpleTimePicker.getCurrentMinute();
        int day = simpleDatePicker.getDayOfMonth();
        int month = simpleDatePicker.getMonth();
        int year = simpleDatePicker.getYear();
        String date = String.valueOf(day) + "-" + String.valueOf(month) + "-" + String.valueOf(year);
        String time_ride = String.valueOf(hour)+ "h " + String.valueOf(minute) + "min";


        Ride ride = new Ride(0, mail_user, departurePlace.getText().toString(),date, time_ride, arrivalPlace.getText().toString(), distance.getText().toString(), duree.getText().toString(), null, null, null, "waiting"  );

        Intent intent = new Intent(Creer.this, MapCreate.class);
        intent.putExtra("mail_user", mail_user);
        intent.putExtra("ride", ride);
        startActivity(intent);



    }



}
