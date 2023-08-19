package com.example.registrationapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class HomePageActivity extends AppCompatActivity {

    TextView showtimer, locationView, diplayInOutlabel;
    Double time = 0.0;
    TimerTask timerTask;
    String inTime = null;
    String outTime = null;

    String inTimeLocation,outTimeLocation;

    LocationManager locationManager;
    String latitude,longit;
    Timer timer;
    Button markeAttendanceLogin;
    Button markeAttendanceLogout;
    Boolean alreadyMarkedLogout=true;

   FusedLocationProviderClient fusedLocationClient;
    private final static int REQUEST_CODE = 100;
    private LocationRequest locationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //for location request
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        //end location request

        /* ----------- initialized variables ------------------- */
        timer = new Timer();
        markeAttendanceLogin = (Button) findViewById(R.id.btnAttendanceMarked);
        markeAttendanceLogout = (Button) findViewById(R.id.btnMakredAttendanceLogout);
        markeAttendanceLogout.setEnabled(false);
        locationView = (TextView) findViewById(R.id.locationView);
        diplayInOutlabel = (TextView) findViewById(R.id.diplayInOutlabel);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        showtimer = (TextView) findViewById(R.id.textTimer);
        /* ----------- end initialized variables ------------------- */
        markeAttendanceLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                inTime = currentDate+ " - "+currentTime;
                markeAttendanceLogin.setEnabled(false);
                markeAttendanceLogout.setEnabled(true);
                if(!alreadyMarkedLogout){
                    markeAttendanceLogout.setEnabled(true);
                    time = 0.0;
                    showtimer.setText(formatTime(0,0,0));
                }
                // method to get the location
                diplayInOutlabel.setText("Your Current in-Time location details");
                getCurentLocation();
                startTimer();  // for showing timer in screen
               Toast.makeText(HomePageActivity.this, "Login Attendance Marked for today's date : " + inTime, Toast.LENGTH_SHORT).show();
            }
        });

        markeAttendanceLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                outTime = currentDate+" - "+currentTime;
                markeAttendanceLogout.setEnabled(false);
                markeAttendanceLogin.setEnabled(true);
                alreadyMarkedLogout = false;
                timerTask.cancel();
                diplayInOutlabel.setText("Your Current out-Time locatioin details");
                //getLastLocation();
                getCurentLocation();
                Toast.makeText(HomePageActivity.this, "Logout Attendance Marked for today's date : "  +outTime, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCurentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(HomePageActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(HomePageActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(HomePageActivity.this).removeLocationUpdates(this);

                            if(locationResult != null && locationResult.getLocations().size() >0){
                                int index = locationResult.getLocations().size() -1;
                                double latitude = locationResult.getLocations().get(index).getLatitude();
                                double longitude = locationResult.getLocations().get(index).getLongitude();
                                locationView.setText("Latitude : " + latitude+ "\n" + "Longitude : "+ longitude);

                            }
                        }
                    }, Looper.getMainLooper());


                } else {
                    turnOnGPS();
                }
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
        }
    }

    private boolean isGPSEnabled() {

        LocationManager locationManager1 = null;
        boolean isEnabled = false;
        if (locationManager1 == null) {
            locationManager1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        }
        isEnabled = locationManager1.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;
    }

    private void turnOnGPS() {


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(HomePageActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(HomePageActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }


    private void startTimer() {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        time++;
                        showtimer.setText(getTimerText());
                    }
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);

    }

    private String getTimerText()
    {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours)
    {
        return String.format("%02d",hours) + " : " + String.format("%02d",minutes) + " : " + String.format("%02d",seconds);
    }




}