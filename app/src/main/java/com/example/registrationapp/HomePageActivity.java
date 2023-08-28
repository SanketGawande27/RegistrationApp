package com.example.registrationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.util.TimeUtils;
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

import com.example.registrationapp.model.RecordDetails;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.content.DialogInterface;
public class HomePageActivity extends AppCompatActivity {

    TextView showtimer, locationView,outLocationView, diplayInOutlabel;
    Double time = 0.0;
    TimerTask timerTask;
    DatabaseReference databaseReference;
    String inTime,outTime = null;
    String inTimeLocation,outTimeLocation;

    LocationManager locationManager;
    Timer timer;
    Button markeAttendanceLogin;
    Button markeAttendanceLogout;
    Boolean alreadyMarkedLogout=true;
    String totalWorkTime;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

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
        databaseReference = FirebaseDatabase.getInstance().getReference().child("AttendanceRecord");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        timer = new Timer();
        markeAttendanceLogin = (Button) findViewById(R.id.btnAttendanceMarked);
        markeAttendanceLogout = (Button) findViewById(R.id.btnMakredAttendanceLogout);
        markeAttendanceLogout.setEnabled(false);
        locationView = (TextView) findViewById(R.id.locationView);
        diplayInOutlabel = (TextView) findViewById(R.id.diplayInOutlabel);
        outLocationView =(TextView) findViewById(R.id.outLocationView);
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
                    time = 0.0;
                    showtimer.setText(formatTime(0,0,0));
                }
                // method to get the location
                diplayInOutlabel.setText("Your Current in-Time location details");
                getCurrentInTimeLocation();
                inTimeLocation = locationView.getText().toString();
                if(inTimeLocation!=null || inTimeLocation!=""){

                    startTimer();  // for showing timer in screen
                    Toast.makeText(HomePageActivity.this, "Login In-Time Marked for today's date : " + inTime, Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(HomePageActivity.this, "SomeThing went Wrong", Toast.LENGTH_LONG).show();
                }
            }
        });

        markeAttendanceLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    outTime = currentDate+" - "+currentTime;
                    markeAttendanceLogout.setEnabled(false);
                    markeAttendanceLogin.setEnabled(true);
                    alreadyMarkedLogout = false;
                    timerTask.cancel();
                    totalWorkTime = showtimer.getText().toString();
                    diplayInOutlabel.setText("Your Current out-Time location details");
                    getCurrentOutTimeLocation();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ConfirmationDialogBoxToStoreData();
                showtimer.setText(formatTime(0,0,0));


            }
        });
    }

    private void ConfirmationDialogBoxToStoreData() {
        AlertDialog.Builder newAlertDialog = new AlertDialog.Builder(this);
        new AlertDialog.Builder(this).setIcon(R.drawable.baseline_logout)
                .setTitle("Confirmation Alert").setMessage("Are you sure you want to mark your InTime and OutTime Attendance?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InsertRecords();

                        //Toast.makeText(HomePageActivity.this, "Logout Out-Time Marked for today's date : " + inTime, Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("No", null).show();
    }


    private void InsertRecords() {
         String email = mUser.getEmail();
         String inTimeandDate = inTime;
         String intimeLocation = inTimeLocation;
         String outTimeandDate = outTime;
         String outtimeLocation = outTimeLocation;
         String totalHr = totalWorkTime;
         if(null!=email && null!=inTimeandDate && null!=intimeLocation && null!=outTimeandDate && null!=outtimeLocation && null!=totalHr){
             RecordDetails recordDetails = new RecordDetails(email,inTimeandDate,intimeLocation,outTimeandDate,outtimeLocation,totalHr);
             databaseReference.push().setValue(recordDetails);
             Toast.makeText(HomePageActivity.this, "Logout Out-Time Marked for today's date : "  +outTime, Toast.LENGTH_SHORT).show();

         }else{
             Toast.makeText(HomePageActivity.this, "Insertion Failed " , Toast.LENGTH_SHORT).show();
         }
    }

    synchronized void getCurrentInTimeLocation() {
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
                               // Toast.makeText(HomePageActivity.this, "intime Index: " +index, Toast.LENGTH_LONG).show();
                                double latitude = locationResult.getLocations().get(index).getLatitude();
                                double longitude = locationResult.getLocations().get(index).getLongitude();
                                locationView.setText("Latitude : " + latitude+ "\n" + "Longitude : "+ longitude);
                                inTimeLocation = locationView.getText().toString();
                                //Toast.makeText(HomePageActivity.this, "InTimeLocation : " +inTimeLocation, Toast.LENGTH_LONG).show();

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

    synchronized void getCurrentOutTimeLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(HomePageActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(HomePageActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            LocationServices.getFusedLocationProviderClient(HomePageActivity.this).removeLocationUpdates(this);

                            if(locationResult != null && locationResult.getLocations().size() >0) {
                                int index = locationResult.getLocations().size() - 1;
                               // Toast.makeText(HomePageActivity.this, "outtime Index: " +index, Toast.LENGTH_LONG).show();
                                 double latitude = locationResult.getLocations().get(index).getLatitude();
                                 double longitude = locationResult.getLocations().get(index).getLongitude();
                                  locationView.setText("Latitude : " + latitude+ "\n" + "Longitude : "+ longitude);
                                  outTimeLocation = locationView.getText().toString();
                                  //Toast.makeText(HomePageActivity.this, "OutTime Location: "+outTimeLocation, Toast.LENGTH_SHORT).show();
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