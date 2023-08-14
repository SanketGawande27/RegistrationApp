package com.example.registrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.registrationapp.data.MyDBHandler;
import com.example.registrationapp.model.RegistrationPojo;

public class Registration extends AppCompatActivity {

    EditText username,email,password,confirmPassword;
    Button register;
    MyDBHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //create dbHandler object for table


        username = (EditText) findViewById(R.id.txtUsername);
        email = (EditText) findViewById(R.id.txtEmail);
        password = (EditText) findViewById(R.id.txtPassword);
        confirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        register = (Button) findViewById(R.id.btnRegister);
        db = new MyDBHandler(this);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String emailID = email.getText().toString();
                String pass = password.getText().toString();
                String confirmpass = confirmPassword.getText().toString();
                
                if(user.equals("")||emailID.equals("")||pass.equals("")||confirmpass.equals("")){
                    Toast.makeText(Registration.this, "Please enter data in all fields ", Toast.LENGTH_SHORT).show();
                }else {
                    if(pass.equals(confirmpass)){
                        Boolean checkemail = db.checkEmail(emailID);
                        if (checkemail==false){
                            Boolean insert = db.insertData(user,emailID,pass,confirmpass);
                            if(insert == true){
                                Toast.makeText(Registration.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                                startActivity(intent);

                            }else {
                                Toast.makeText(Registration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(Registration.this, "User already exists! Please login", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(Registration.this, "Password Not Match! Please check once again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });




        TextView btn = findViewById(R.id.txtAlreadyHaveAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration.this, Login.class));
            }
        });
    }
}