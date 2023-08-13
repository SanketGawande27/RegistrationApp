package com.example.registrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.registrationapp.data.MyDBHandler;
import com.example.registrationapp.model.RegistrationPojo;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //create dbHandler object for table
        MyDBHandler db = new MyDBHandler(Registration.this);

        //create new record for registration
        RegistrationPojo reg = new RegistrationPojo();
        reg.setUserName("SanketGawande");
        reg.setEmail("sanket27@gmail.com");
        reg.setPassword("12345");
        reg.setConfirmPassword("12345");

        //added data into table(Registration)
        db.addRegistration(reg);


        TextView btn = findViewById(R.id.txtAlreadyHaveAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration.this, Login.class));
            }
        });
    }
}