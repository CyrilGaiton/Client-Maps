package com.example.enzo.test;


import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Connection {

    private static Connection instance = null;
    public static Socket client ;
    public static ObjectInputStream ois;
    public static ObjectOutputStream oos;



    public static synchronized Connection getInstance() {

        if (instance == null) {

            try {
                client = new Socket("10.42.6.131", 5700);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                oos = new ObjectOutputStream(client.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ois = new ObjectInputStream(client.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }


}
