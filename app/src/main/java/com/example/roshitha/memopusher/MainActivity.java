package com.example.roshitha.memopusher;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    DatabaseHelper mDatabaseHelper;
    LocationManager locationManager;
    Context mContext;
    EditText latView;
    EditText lonView;
    EditText txtmemo;
    Button btnCreate;
    Button btnMap;
    Button btnViewMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        isLocationEnabled();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mDatabaseHelper = new DatabaseHelper(this);

        latView = (EditText) findViewById(R.id.txt_lat);
        lonView = (EditText) findViewById(R.id.txt_lon);
        txtmemo = (EditText) findViewById(R.id.txt_memo_content);
        btnCreate = (Button) findViewById(R.id.btn_create_memo);
        btnMap = (Button) findViewById(R.id.btn_map);
        btnViewMemo = (Button) findViewById(R.id.btn_view);

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            toastMessage("Permission Denied!");
        }

        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = Double.parseDouble(latView.getText().toString());
                double lon = Double.parseDouble(lonView.getText().toString());
                String memo = txtmemo.getText().toString();
                String time = "11.10";
                addData(memo, time, lat, lon);
                txtmemo.setText("");
            }
        });

        btnViewMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewMemos.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            toastMessage("Permission Denied!");
        }

        String locationProvider = LocationManager.GPS_PROVIDER;
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
//            this.locationView.setText(location.getLatitude() + " " + location.getLongitude());
            latView.setText(Double.toString(location.getLatitude()));
            lonView.setText(Double.toString(location.getLongitude()));
//            Toast.makeText(mContext, location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();
        }

        Cursor data = mDatabaseHelper.getData();

        while (data.moveToNext()) {
            String time = data.getString(2);
            long difference = checkTime(time);
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
            toastMessage("Lat: " + location.getLatitude() + "\nLon: " + location.getLongitude());
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

    public void addData(String memo, String time, double lat, double lon) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String currentDate = simpleDateFormat.format(new Date());

        boolean insertData = mDatabaseHelper.addData(memo, currentDate, lat, lon);

        if (insertData)
            toastMessage("Insertion successfull" + currentDate);
        else
            toastMessage("Insertion failed");
    }

    public void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public long checkTime(String time) {
        // Current date to compare
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

        Date curentDate = new Date();

        try {
            Date assignedDate = simpleDateFormat.parse(time);

            printDifference(curentDate, assignedDate);

            //milliseconds
            long different = curentDate.getTime() - assignedDate.getTime();

            long days = different/(1000*60*60);
            toastMessage(Long.toString(days));
            return days;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void printDifference(Date startDate, Date endDate) {

        //milliseconds
        long different = startDate.getTime() - endDate.getTime();

        long days = different/(1000*60);
        toastMessage(Long.toString(days));

//        System.out.println("startDate : " + startDate);
//        System.out.println("endDate : "+ endDate);
//        System.out.println("different : " + different);
//
//        long secondsInMilli = 1000;
//        long minutesInMilli = secondsInMilli * 60;
//        long hoursInMilli = minutesInMilli * 60;
//        long daysInMilli = hoursInMilli * 24;
//
//        long elapsedDays = different / daysInMilli;
//        different = different % daysInMilli;
//
//        long elapsedHours = different / hoursInMilli;
//        different = different % hoursInMilli;
//
//        long elapsedMinutes = different / minutesInMilli;
//        different = different % minutesInMilli;
//
//        long elapsedSeconds = different / secondsInMilli;
//
//        System.out.printf(
//                "%d days, %d hours, %d minutes, %d seconds%n",
//                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
    }


}
