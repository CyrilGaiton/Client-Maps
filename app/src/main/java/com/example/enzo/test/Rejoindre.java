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

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.masterdapm.ancyen.model.Ride;
import fr.masterdapm.ancyen.model.Statistics;

/**
 * Created by enzo on 18/12/17.
 */

public class Rejoindre extends Activity {

    LinearLayout linear_departureplace;
    LinearLayout linear_distance;
    LinearLayout linear_duration;
    LinearLayout linear_button;
    LinearLayout.LayoutParams llp;
    String mail_user;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejoindre);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {

            mail_user = (String) bd.get("mail_user");

        }
        linear_departureplace = (LinearLayout) findViewById(R.id.linear_departureplace);
        linear_distance = (LinearLayout) findViewById(R.id.linear_distance);
        linear_duration = (LinearLayout) findViewById(R.id.linear_duration);
        linear_button = (LinearLayout) findViewById(R.id.linear_button);
        new ASyncConnection().execute();
    }

    public void rejoindre(View v){

        Intent intent = new Intent(Rejoindre.this, MapsActivity.class);
        startActivity(intent);

    }

    public void log(String s){
        Log.e("lolmdr", s);
    }




    private class ASyncConnection extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... params) {
            List<Ride> ride_email = new ArrayList<Ride>();

            Connection connection = Connection.getInstance();

            List<String> strings = new ArrayList<>();
            String s = null;
            Statistics statistics;
            Ride ride;
            try {
                // statistics
                connection.oos.writeObject("getStatisticsWithEmail");
                connection.oos.writeObject(mail_user);

                s = (String) connection.ois.readObject();
                while (!s.equals("close")) {
                    statistics = (Statistics) connection.ois.readObject();
                    strings.add(Integer.toString(statistics.getIdRide()));
                    s = (String) connection.ois.readObject();
                }
                strings.add(0, Integer.toString(strings.size()));

                //rides
                connection.oos.writeObject("getRidesWithEmail");
                connection.oos.writeObject(mail_user);

                s = (String) connection.ois.readObject();
                while (!s.equals("close")) {
                    ride = (Ride) connection.ois.readObject();
                    strings.add(Integer.toString(ride.getId()));
                    strings.add(ride.getDeparturePlace());
                    strings.add(ride.getDistance());
                    strings.add(ride.getDuration());
                    s = (String) connection.ois.readObject();
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
            return strings;
        }



        @Override
        protected void onPostExecute(List<String> strings) {
            int coupure = Integer.parseInt(strings.get(0));
            int[] ids = null;
            if (coupure > 0) {
                ids = new int[strings.size() / 4];
                for (int i = 1; i < coupure + 1; i++) {
                    ids[i - 1] = Integer.parseInt(strings.get(i));
                }
            }

            final String[] infos = new String[(strings.size()-coupure-1)];
            for (int j=coupure+1; j<strings.size(); j++){
                infos[j-coupure-1] = strings.get(j);
            }



           for(int k=0; k<infos.length;k+=4){

               if (!idIn(ids, Integer.parseInt(infos[k]))) {
                   Button button = new Button(Rejoindre.this);
                   button.setText("Accepter");
                   final int finalK = k;
                   button.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           new ASyncConnectionforStatistics().execute(Integer.parseInt(infos[finalK]));
                       }
                   });
                   TextView textview_departureplace = new TextView(Rejoindre.this);
                   TextView textview_distance = new TextView(Rejoindre.this);
                   TextView textview_duration = new TextView(Rejoindre.this);
                   textview_departureplace.setText(infos[k+1]);
                   textview_distance.setText(infos[k+2]);
                   textview_duration.setText(infos[k+3]);

                   llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                   textview_departureplace.setLayoutParams(llp);
                   textview_distance.setLayoutParams(llp);
                   textview_duration.setLayoutParams(llp);
                   button.setLayoutParams(llp);

                   linear_departureplace.addView(textview_departureplace);
                   linear_distance.addView(textview_distance);
                   linear_duration.addView(textview_duration);
                   linear_button.addView(button);
               }
           }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }


        public boolean idIn(int[] ids, int id) {
            if (ids != null) {
                boolean find = false;
                for (int i=0; i<ids.length; i++) {
                    if (ids[i] == id) find = true;
                }
                return find;
            }
            return false;
        }
    }


    private class ASyncConnectionforStatistics extends AsyncTask<Integer, Void, Void> {




        @Override
        protected Void doInBackground(Integer... params) {

            Connection connection = Connection.getInstance();

            try {
                connection.oos.writeObject("addStatistics");
                connection.oos.writeObject(new Statistics(params[0], mail_user, null));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                connection.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(Void result) {

            finish();
            startActivity(getIntent());


        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }




}
