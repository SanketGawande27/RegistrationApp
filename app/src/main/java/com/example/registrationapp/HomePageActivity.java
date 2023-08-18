package com.example.registrationapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class HomePageActivity extends AppCompatActivity {

    TextView showtimer, locationView;
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
    // initializing FusedLocationProviderClient object
   FusedLocationProviderClient fusedLocationClient;
    private final static int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
       // ActivityCompat.requestPermissions( this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        timer = new Timer();
        markeAttendanceLogin = (Button) findViewById(R.id.btnAttendanceMarked);
        markeAttendanceLogout = (Button) findViewById(R.id.btnMakredAttendanceLogout);
        locationView = (TextView) findViewById(R.id.locationView);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        showtimer = (TextView) findViewById(R.id.textTimer);
        markeAttendanceLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                inTime = currentDate+ " - "+currentTime;
                markeAttendanceLogin.setEnabled(false);
                if(!alreadyMarkedLogout){
                    markeAttendanceLogout.setEnabled(true);
                    time = 0.0;
                    showtimer.setText(formatTime(0,0,0));
                }

                getLastLocation();
                startTimer();  // for showing timer in screen
                // method to get the location
                //locationView.setText("Current location");
               // Toast.makeText(HomePageActivity.this, "Login Attendance Marked for today's date : " + inTime, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(HomePageActivity.this, "Logout Attendance Marked for today's date : "  +outTime, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLastLocation() {
        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        Geocoder geocoder = new Geocoder(HomePageActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            Log.e("Address : ","Latitude : " + addresses.get(0).getLatitude()+ "Longitude : " +addresses.get(0).getLongitude() );
                            locationView.setText("Latitude : " + addresses.get(0).getLatitude() + "Longitude : " +addresses.get(0).getLongitude() + " Address : "+ addresses.get(0).getAddressLine(0));

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }else {
                askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(HomePageActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull  int[] grantResults) {
       if(requestCode == REQUEST_CODE){

           if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

               getLastLocation();
           }else {
               Toast.makeText(HomePageActivity.this, "Please provide the request permission", Toast.LENGTH_SHORT).show();
           }
       }

       super.onRequestPermissionsResult(requestCode,permissions,grantResults);
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