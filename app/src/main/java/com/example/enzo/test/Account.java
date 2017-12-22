package com.example.enzo.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


/**
 * Created by enzo on 15/12/17.
 */

public class Account extends Activity {

    String mail_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {

            mail_user = (String) bd.get("mail_user");


        }
    }


    public void create(View v){

        Intent intent = new Intent(Account.this, Creer.class);
        intent.putExtra("mail_user", mail_user );
        startActivity(intent);


    }

    public void join(View v){

        Intent intent = new Intent(Account.this, MyRides.class);
        intent.putExtra("mail_user", mail_user );
        startActivity(intent);


    }

}
