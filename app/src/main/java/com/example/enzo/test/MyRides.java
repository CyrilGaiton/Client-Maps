package com.example.enzo.test;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.masterdapm.ancyen.model.Ride;
import fr.masterdapm.ancyen.model.Statistics;

/**
 * Created by enzo on 22/12/17.
 */

public class MyRides extends Activity {


    LinearLayout linear_departureplace;
    LinearLayout linear_distance;
    LinearLayout linear_duration;
    LinearLayout linear_state;
    LinearLayout.LayoutParams llp;
    String mail_user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myride);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {

            mail_user = (String) bd.get("mail_user");

        }
        linear_departureplace = (LinearLayout) findViewById(R.id.linear_departureplace);
        linear_distance = (LinearLayout) findViewById(R.id.linear_distance);
        linear_duration = (LinearLayout) findViewById(R.id.linear_duration);
        linear_state = (LinearLayout) findViewById(R.id.linear_state);
        new ASyncConnection().execute();
    }



    private class ASyncConnection extends AsyncTask<Void, Void, List<Ride>> {

        @Override
        protected List<Ride> doInBackground(Void... params) {
            List<Ride> ride_email = new ArrayList<Ride>();

            Connection connection = Connection.getInstance();

            List<Integer> ids = new ArrayList<>();
            String s = null;
            Ride ride;
            List<Ride> rides = null;
            try {
                // statistics
                connection.oos.writeObject("getStatisticsWithEmail");
                connection.oos.writeObject(mail_user);

                s = (String) connection.ois.readObject();
                while (!s.equals("close")) {
                    ids.add(((Statistics) connection.ois.readObject()).getIdRide());
                    s = (String) connection.ois.readObject();
                }

                //rides
                rides = new ArrayList<>();

                for (int i = 0; i < ids.size(); i++) {

                    connection.oos.writeObject("getRide");
                    connection.oos.writeObject(ids.get(i));
                    rides.add((Ride) connection.ois.readObject());

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                connection.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return rides;
        }



        @Override
        protected void onPostExecute(final List<Ride> rides) {
            for(int i=0; i<rides.size();i++){
                Button button = new Button(MyRides.this);
                button.setText("Lancer");
                if (rides.get(i).getState() == "waiting")
                    button.setEnabled(false);
                final int finalI = i;
                button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MyRides.this, MapsActivity.class);
                            intent.putExtra("mail_user", mail_user);
                            intent.putExtra("idRide", rides.get(finalI).getId());
                            startActivity(intent);
                        }
                    });
                TextView textview_departureplace = new TextView(MyRides.this);
                TextView textview_distance = new TextView(MyRides.this);
                TextView textview_duration = new TextView(MyRides.this);
                textview_departureplace.setText(rides.get(i).getDeparturePlace());
                textview_distance.setText(rides.get(i).getDistance());
                textview_duration.setText(rides.get(i).getDuration());

                llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textview_departureplace.setLayoutParams(llp);
                textview_distance.setLayoutParams(llp);
                textview_duration.setLayoutParams(llp);
                button.setLayoutParams(llp);

                linear_departureplace.addView(textview_departureplace);
                linear_distance.addView(textview_distance);
                linear_duration.addView(textview_duration);
                linear_state.addView(button);

            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

    }


}
