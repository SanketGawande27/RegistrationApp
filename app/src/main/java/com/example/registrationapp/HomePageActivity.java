package com.example.registrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.registrationapp.model.CountUpTimer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageActivity extends AppCompatActivity {

    TextView showtimer;
    Double time = 0.0;
    TimerTask timerTask;

    Timer timer;
    Button markeAttendanceLogin;
    Button markeAttendanceLogout;
    Boolean alreadyMarkedLogout=true;
   // private static final long INTERVAL_MS = 1000;
   // private final long duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        timer = new Timer();
        markeAttendanceLogin = (Button) findViewById(R.id.btnAttendanceMarked);
        markeAttendanceLogout = (Button) findViewById(R.id.btnMakredAttendanceLogout);
        showtimer = (TextView) findViewById(R.id.textTimer);
        markeAttendanceLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markeAttendanceLogin.setEnabled(false);
                if(alreadyMarkedLogout == false){
                    markeAttendanceLogout.setEnabled(true);
                }
                Toast.makeText(HomePageActivity.this, "Login Attendance Marked for today's date" + new Date(), Toast.LENGTH_SHORT).show();
                startTimer();  // for showing timer in screen

            }
        });

        markeAttendanceLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markeAttendanceLogout.setEnabled(false);
                markeAttendanceLogin.setEnabled(true);
                alreadyMarkedLogout = false;
                Toast.makeText(HomePageActivity.this, "Logout Attendance Marked for today's date" + new Date(), Toast.LENGTH_SHORT).show();
                timerTask.cancel();
                Toast.makeText(HomePageActivity.this, "Time Taken : " + showtimer.getText().toString(), Toast.LENGTH_SHORT).show();
                showtimer.setText(formatTime(0,0,0));
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