package com.example.enzo.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by enzo on 09/12/17.
 */

public class SignUpDatabase extends Activity {


    final String EXTRA_NOM = "user_name";
    final String EXTRA_PRENOM = "user_firstname";
    final String EXTRA_MAIL = "user_mail";
    final String EXTRA_MDP = "user_mdp";
    final String EXTRA_CMDP = "user_cmdp";



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merde);
        Intent intent = getIntent();

        TextView namedisplay = (TextView) findViewById(R.id.merde);


        if (intent != null) {


            namedisplay.setText(intent.getStringExtra(EXTRA_NOM));


        }



    }



}
