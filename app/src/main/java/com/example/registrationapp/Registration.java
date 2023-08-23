package com.example.registrationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.registrationapp.data.MyDBHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {

    EditText username,email,password,confirmPassword;
    Button register;
    MyDBHandler db;
    String validateEmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ProgressDialog progessDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //create dbHandler object for table

        progessDialog = new ProgressDialog(this);
        username = (EditText) findViewById(R.id.txtUsername);
        email = (EditText) findViewById(R.id.txtEmail);
        password = (EditText) findViewById(R.id.txtPassword);
        confirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        register = (Button) findViewById(R.id.btnRegister);
        db = new MyDBHandler(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performAuthentication();
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

    private void performAuthentication() {
        String user = username.getText().toString();
        String emailID = email.getText().toString();
        String pass = password.getText().toString();
        String confirmpass = confirmPassword.getText().toString();

        if(!emailID.matches(validateEmailPattern)){
            email.setError("Enter Correct Email");
        }else if (pass.isEmpty() || pass.length()<6){
            password.setError("Enter alteast 6 digit password");
        } else if (!pass.equals(confirmpass)) {
            confirmPassword.setError("Password not match! Please try again");
        }else {
            progessDialog.setMessage("Please wait while Registration .... ");
            progessDialog.setTitle("Registration");
            progessDialog.setCanceledOnTouchOutside(false);
            progessDialog.show();

            mAuth.createUserWithEmailAndPassword(emailID,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progessDialog.dismiss();
                        sendUsertoNextActivity();
                        Toast.makeText(Registration.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                    }else {
                        progessDialog.dismiss();
                        Toast.makeText(Registration.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUsertoNextActivity() {
        Intent intent = new Intent(Registration.this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}