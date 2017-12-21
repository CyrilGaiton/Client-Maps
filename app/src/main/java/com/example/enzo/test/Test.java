package com.example.enzo.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enzo on 01/12/17.
 */

public class Test extends Activity {


    public static List<String> heures = new ArrayList<String>();
    static {
        heures.add("00");
        heures.add("01");
        heures.add("02");
        heures.add("03");
        heures.add("04");
        heures.add("05");
        heures.add("06");
        heures.add("07");
        heures.add("08");
        heures.add("09");
        heures.add("10");
        heures.add("11");
        heures.add("12");
        heures.add("13");
        heures.add("14");
        heures.add("15");
        heures.add("16");
        heures.add("17");
        heures.add("18");
        heures.add("19");
        heures.add("20");
        heures.add("21");
        heures.add("22");
        heures.add("23");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer);
    /*
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, heures);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
*/
    }


/*
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

*/

}
