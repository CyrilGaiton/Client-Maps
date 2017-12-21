package com.example.enzo.test;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.masterdapm.ancyen.model.Ride;

public class AutorisedEmails extends AppCompatActivity {

    EditText editText;
    LinearLayout lp;
    LinearLayout.LayoutParams llp;
    Ride ride;
    String mail_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autotised_emails);

        Bundle bd = getIntent().getExtras();
        if (bd != null){
            ride = (Ride) bd.get("ride");
            mail_user = (String) bd.get("mail_user");
        }

        lp = findViewById(R.id.lp);
    }

    public void add(View v){
        editText = new EditText(AutorisedEmails.this);
        llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(llp);
        lp.addView(editText);
    }

    public void next(View v){
        List<String> strings = new ArrayList<>();
        for (int i=1; i<lp.getChildCount(); i++){
            editText = (EditText) lp.getChildAt(i);
            strings.add(editText.getText().toString());
            Log.e("lolmdr", "amil : "+editText.getText().toString());
        }


        Object[] objects = new Object[strings.size()+1];
        for (int j=0; j<strings.size(); j++){
            objects[j] = strings.get(j);
        }
        objects[strings.size()] = ride;
        new AsyncConnection().execute(objects);
    }

    private class AsyncConnection extends AsyncTask<Object, Void, Void>{

        @Override
        protected Void doInBackground(Object... objects) {

            Connection connection = Connection.getInstance();

            Ride ride = (Ride) objects[objects.length-1];
            String[] strings = new String[objects.length-1];
            for(int i = 0; i<objects.length-1; i++){
                strings[i] = (String) objects[i];
                Log.e("lolmdr", "amil222 : "+strings[i]);
            }

            ride.setAutorisedEmails(strings);

            try {
                connection.oos.writeObject("addRide");
                connection.oos.writeObject(ride);
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
        protected void onPostExecute(Void res){

            Intent intent = new Intent(AutorisedEmails.this, Account.class);
            intent.putExtra("mail_user", mail_user);
            startActivity(intent);

        }


    }
}
