package com.example.enzo.test;

/**
 * Created by enzo on 12/12/17.
 */


public class Position {
    private int longitude;
    private int latitude;
    private int elevation;

    public Position(int longitude, int latitude, int elevation) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.elevation = elevation;
    }

    public int getLongitude() {
        return longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getElevation() {
        return elevation;
    }

}
