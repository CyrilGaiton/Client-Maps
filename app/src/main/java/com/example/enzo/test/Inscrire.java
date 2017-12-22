package com.example.enzo.test;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

import fr.masterdapm.ancyen.model.User;

/**
 * Created by enzo on 09/12/17.
 */

public class Inscrire extends Activity {

    EditText champ_nom;
    EditText champ_prenom;
    EditText champ_mail;
    EditText champ_mdp;
    EditText champ_confmdp;


    final String EXTRA_NOM = "user_name";
    final String EXTRA_PRENOM = "user_firstname";
    final String EXTRA_MAIL = "user_mail";
    final String EXTRA_MDP = "user_mdp";
    final String EXTRA_CMDP = "user_cmdp";


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscrire);

        champ_nom = (EditText) findViewById(R.id.nom);
        champ_prenom = (EditText) findViewById(R.id.prenom);
        champ_mail = (EditText) findViewById(R.id.email);
        champ_mdp = (EditText) findViewById(R.id.password);
        champ_confmdp = (EditText) findViewById(R.id.password_confirm);



    }


    public void inscription(View v) {


        String email = champ_mail.getText().toString();
        String mailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String mdp = champ_mdp.getText().toString();
        String cmdp = champ_confmdp.getText().toString();

        if (email.matches(mailPattern) && mdp.equals(cmdp)) {

            new Inscrire.ASyncConnection().execute();
            User user = new User(champ_mail.getText().toString(), champ_mdp.getText().toString(), champ_nom.getText().toString(), champ_prenom.getText().toString() );

        }

        else {
            String error_mail = "Mail invalide ou inexistant.";
            champ_mail.setText(error_mail);

        }

    }

    private class ASyncConnection extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            Connection connection = Connection.getInstance();

            try {
                connection.oos.writeObject("addUser");
                Log.e("test add user", "Apres premier write");
                User user = new User(champ_mail.getText().toString(), champ_mdp.getText().toString(), champ_nom.getText().toString(), champ_prenom.getText().toString() );
                connection.oos.writeObject(user);
                Log.e("test add user", "Apres second write");
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
        protected void onPostExecute(Void res) {

            Intent intent = new Intent(Inscrire.this, Account.class);
            intent.putExtra("mail_user", champ_mail.getText().toString());
            startActivity(intent);

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }


}
