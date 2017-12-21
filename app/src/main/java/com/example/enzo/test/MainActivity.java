package com.example.enzo.test;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;

import fr.masterdapm.ancyen.model.User;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "numero";
    Button connexion;
    Button test;
    EditText email;
    EditText password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connexion = (Button) findViewById(R.id.boutonconnexion);


        email = (EditText) findViewById(R.id.editmail);

        password = (EditText) findViewById(R.id.editmdp);

    }


    public void connexiontoprofile(View v) {


        new ASyncConnection().execute();



    }

    public void inscription(View v) {


        Intent intentapp = new Intent(MainActivity.this, Inscrire.class);
        MainActivity.this.startActivity(intentapp);


    }


    public void test(View v) throws IOException, ClassNotFoundException {


     //   InetAddress serverAddr = InetAddress.getByName("10.42.6.131");
       // Socket socketClient = new Socket(serverAddr, 8886);
    }


    private class ASyncConnection extends AsyncTask<Void, Void, Integer> {




        @Override
        protected Integer doInBackground(Void... params) {
            Integer i = 0;
            Connection connection = Connection.getInstance();


            try {
                connection.oos.writeObject("checkUser");
                connection.oos.writeObject(email.getText().toString());
                connection.oos.writeObject(password.getText().toString());
                i = (Integer) connection.ois.readObject();
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
            return i;
        }



        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == 1) {
                Intent intent = new Intent(MainActivity.this, Account.class);
                intent.putExtra("mail_user", email.getText().toString());

                startActivity(intent);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }




}
