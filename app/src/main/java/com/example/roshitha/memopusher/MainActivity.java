package com.example.roshitha.memopusher;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    LocationManager locationManager;
    Context mContext;
    EditText latView;
    EditText lonView;
    EditText memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        isLocationEnabled();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        latView = (EditText) findViewById(R.id.txt_lat);
        lonView = (EditText) findViewById(R.id.txt_lon);
        memo = (EditText) findViewById(R.id.txt_memo_content);

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, "Permission Denied!", Toast.LENGTH_LONG).show();
        }

        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

    }

    @Override
    protected void onStart() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, "Permission Denied!", Toast.LENGTH_LONG).show();
        }

        Toast.makeText(this, "Hello World!", Toast.LENGTH_SHORT).show();
        String locationProvider = LocationManager.GPS_PROVIDER;
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
//            this.locationView.setText(location.getLatitude() + " " + location.getLongitude());
            latView.setText(Double.toString(location.getLatitude()));
            lonView.setText(Double.toString(location.getLongitude()));
//            Toast.makeText(mContext, location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();
        }
        super.onStart();
    }

    @Override
    protected void onResume() {
//        isLocationEnabled();
        super.onResume();
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(getApplicationContext(), "Lat: " + location.getLatitude() + "\nLon: " + location.getLongitude(), Toast.LENGTH_LONG).show();
//            locationView.setText(location.getLatitude() + " " + location.getLongitude());
            latView.setText(Double.toString(location.getLatitude()));
            lonView.setText(Double.toString(location.getLongitude()));
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
    };

    private void isLocationEnabled() {

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        }
        else{
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Confirm Location");
            alertDialog.setMessage("Your Location is enabled, please enjoy");
            alertDialog.setNegativeButton("Back to interface",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        }
    }


}
