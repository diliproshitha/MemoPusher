package com.example.roshitha.memopusher;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by roshitha on 11/16/17.
 */

public class MyLocationListener extends Activity implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Toast.makeText(this, location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
